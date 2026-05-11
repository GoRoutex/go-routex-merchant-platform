package vn.com.routex.merchant.platform.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.routex.merchant.platform.application.command.provinces.FetchProvincesQuery;
import vn.com.routex.merchant.platform.application.command.provinces.FetchProvincesResult;
import vn.com.routex.merchant.platform.application.command.provinces.SearchProvincesQuery;
import vn.com.routex.merchant.platform.application.command.provinces.SearchProvincesResult;
import vn.com.routex.merchant.platform.application.service.ProvincesManagementService;
import vn.com.routex.merchant.platform.domain.common.PagedResult;
import vn.com.routex.merchant.platform.domain.department.model.Department;
import vn.com.routex.merchant.platform.domain.department.port.DepartmentRepositoryPort;
import vn.com.routex.merchant.platform.domain.provinces.port.ProvincesQueryPort;
import vn.com.routex.merchant.platform.domain.provinces.readmodel.ProvincesFetchView;
import vn.com.routex.merchant.platform.domain.route.port.RouteStopRepositoryPort;
import vn.com.routex.merchant.platform.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.ApiRequestUtils;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.ExceptionUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.INVALID_INPUT_ERROR;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.INVALID_PAGE_NUMBER;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.INVALID_PAGE_SIZE;

@Service
@RequiredArgsConstructor
public class ProvincesManagementServiceImpl implements ProvincesManagementService {

    private final ProvincesQueryPort provincesQueryPort;
    private final DepartmentRepositoryPort departmentRepositoryPort;
    private final RouteStopRepositoryPort routeStopRepositoryPort;

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int DEFAULT_PAGE_NUMBER = 1;

    @Override
    public SearchProvincesResult searchProvinces(SearchProvincesQuery query) {
        String keyword = query.keyword();
        int page = query.page() - 1;
        int size = query.size();

        Set<SearchProvincesResult.SearchProvincesItemResult> allItemsSet = new LinkedHashSet<>();

        // 1. Search Provinces directly
        provincesQueryPort.search(keyword, page, size)
                .forEach(item -> allItemsSet.add(SearchProvincesResult.SearchProvincesItemResult.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .code(item.getCode())
                        .type("PROVINCE")
                        .build()));

        // 2. Search Departments and their parent Provinces
        List<Department> departments = departmentRepositoryPort.search(keyword, page, size);
        for (Department dept : departments) {
            // Add the department itself
            allItemsSet.add(SearchProvincesResult.SearchProvincesItemResult.builder()
                    .id(dept.getId())
                    .name(dept.getName())
                    .code(dept.getMerchantId())
                    .type("DEPARTMENT")
                    .build());

            // Add its parent province as a suggestion
            if (dept.getProvinceId() != null && dept.getProvinceName() != null) {
                allItemsSet.add(SearchProvincesResult.SearchProvincesItemResult.builder()
                        .id(dept.getProvinceId())
                        .name(dept.getProvinceName())
                        .type("PROVINCE")
                        .build());
            }
        }

        // 3. Search Route Stops
        routeStopRepositoryPort.search(keyword, page, size)
                .forEach(item -> allItemsSet.add(SearchProvincesResult.SearchProvincesItemResult.builder()
                        .id(item.getId())
                        .name(item.getStopName())
                        .code(item.getStopCity())
                        .type("STOP")
                        .build()));

        return SearchProvincesResult.builder()
                .data(new ArrayList<>(allItemsSet))
                .build();
    }

    @Override
    public FetchProvincesResult fetchProvinces(FetchProvincesQuery query) {
        PagedResult<ProvincesFetchView> page = fetchProvincePage(query);
        List<ProvincesFetchView> provinces = page.getItems();

        List<FetchProvincesResult.FetchProvinceResult> resultList = provinces.stream()
                .map(p -> FetchProvincesResult.FetchProvinceResult.builder()
                        .id(p.getId())
                        .name(p.getName())
                        .code(p.getCode())
                        .build())
                .toList();

        return FetchProvincesResult.builder()
                .items(resultList)
                .pageNumber(page.getPageNumber() + 1)
                .pageSize(page.getPageSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    private PagedResult<ProvincesFetchView> fetchProvincePage(FetchProvincesQuery query) {
        int pageSize = ApiRequestUtils.parseIntOrDefault(query.pageSize(), DEFAULT_PAGE_SIZE, "pageSize",
                query.context().requestId(), query.context().requestDateTime(), query.context().channel());

        int pageNumber = ApiRequestUtils.parseIntOrDefault(query.pageNumber(), DEFAULT_PAGE_NUMBER, "pageNumber",
                query.context().requestId(), query.context().requestDateTime(), query.context().channel());

        if (pageSize < 1 || pageSize > 100) {
            throw new BusinessException(query.context().requestId(), query.context().requestDateTime(), query.context().channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_PAGE_SIZE));
        }
        if (pageNumber < 1) {
            throw new BusinessException(query.context().requestId(), query.context().requestDateTime(), query.context().channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_PAGE_NUMBER));
        }
        return provincesQueryPort.fetchProvinces(pageNumber - 1, pageSize);
    }
}
