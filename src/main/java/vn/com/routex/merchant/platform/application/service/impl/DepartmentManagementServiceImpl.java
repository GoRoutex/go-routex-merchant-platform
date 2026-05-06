package vn.com.routex.merchant.platform.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.merchant.platform.application.command.department.CreateDepartmentCommand;
import vn.com.routex.merchant.platform.application.command.department.CreateDepartmentResult;
import vn.com.routex.merchant.platform.application.command.department.DeleteDepartmentCommand;
import vn.com.routex.merchant.platform.application.command.department.DeleteDepartmentResult;
import vn.com.routex.merchant.platform.application.command.department.FetchDepartmentQuery;
import vn.com.routex.merchant.platform.application.command.department.FetchDepartmentResult;
import vn.com.routex.merchant.platform.application.command.department.GetDepartmentDetailQuery;
import vn.com.routex.merchant.platform.application.command.department.GetDepartmentDetailResult;
import vn.com.routex.merchant.platform.application.command.department.UpdateDepartmentCommand;
import vn.com.routex.merchant.platform.application.command.department.UpdateDepartmentResult;
import vn.com.routex.merchant.platform.application.service.DepartmentManagementService;
import vn.com.routex.merchant.platform.domain.common.PagedResult;
import vn.com.routex.merchant.platform.domain.department.DepartmentStatus;
import vn.com.routex.merchant.platform.domain.department.model.Department;
import vn.com.routex.merchant.platform.domain.department.port.DepartmentRepositoryPort;
import vn.com.routex.merchant.platform.domain.provinces.port.DistrictRepositoryPort;
import vn.com.routex.merchant.platform.domain.provinces.port.ProvincesRepositoryPort;
import vn.com.routex.merchant.platform.domain.provinces.port.WardRepositoryPort;
import vn.com.routex.merchant.platform.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.DateTimeUtils;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.ExceptionUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.DEPARTMENT_NOT_FOUND;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_DEPARTMENT_MESSAGE;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_ERROR;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.INVALID_INPUT_ERROR;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.INVALID_PAGE_NUMBER;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.INVALID_PAGE_SIZE;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class DepartmentManagementServiceImpl implements DepartmentManagementService {

    private final SystemLog sLog = SystemLog.getLogger(this.getClass());
    private final DepartmentRepositoryPort departmentRepositoryPort;
    private final ProvincesRepositoryPort provincesRepositoryPort;
    private final DistrictRepositoryPort districtRepositoryPort;
    private final WardRepositoryPort wardRepositoryPort;

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int DEFAULT_PAGE_NUMBER = 1;

    @Override
    public CreateDepartmentResult createDepartment(CreateDepartmentCommand command) {
        boolean existPoint = departmentRepositoryPort.existsByCode(command.code(), command.merchantId());
        if(existPoint) {
            throw new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                    ExceptionUtils.buildResultResponse(DUPLICATE_ERROR, DUPLICATE_DEPARTMENT_MESSAGE));
        }
        Department department = Department.builder()
                .id(UUID.randomUUID().toString())
                .merchantId(command.merchantId())
                .code(command.code())
                .name(command.name())
                .type(command.type())
                .address(command.address())
                .wardId(command.wardId())
                .districtId(command.districtId())
                .provinceId(command.provinceId())
                .timeAtDepartment(command.timeAtDepartment())
                .isShuttleService(command.isShuttleService())
                .openingTime(command.openingTime())
                .closingTime(command.closingTime())
                .latitude(command.latitude())
                .longitude(command.longitude())
                .status(command.status())
                .build();

        enrichAdministrativeNames(department);
        departmentRepositoryPort.save(department);

        return CreateDepartmentResult.builder()
                .id(department.getId())
                .code(department.getCode())
                .name(department.getName())
                .type(department.getType())
                .address(department.getAddress())
                .wardId(department.getWardId())
                .wardName(department.getWardName())
                .districtId(department.getDistrictId())
                .districtName(department.getDistrictName())
                .provinceId(department.getProvinceId())
                .provinceName(department.getProvinceName())
                .timeAtDepartment(department.getTimeAtDepartment())
                .isShuttleService(department.isShuttleService())
                .openingTime(department.getOpeningTime())
                .closingTime(department.getClosingTime())
                .latitude(department.getLatitude())
                .longitude(department.getLongitude())
                .status(department.getStatus())
                .build();
    }

    @Override
    public UpdateDepartmentResult updateDepartment(UpdateDepartmentCommand command) {
        Department existing = departmentRepositoryPort.findById(command.id(), command.merchantId())
                .orElseThrow(() -> new BusinessException(
                        command.context().requestId(),
                        command.context().requestDateTime(),
                        command.context().channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(DEPARTMENT_NOT_FOUND, command.id()))
                ));

        if (command.code() != null && !command.code().isBlank() && !command.code().equals(existing.getCode())) {
            if (departmentRepositoryPort.existsByCode(command.code(), command.merchantId())) {
                throw new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                        ExceptionUtils.buildResultResponse(DUPLICATE_ERROR, DUPLICATE_DEPARTMENT_MESSAGE));
            }
        }

        Department updated = Department.builder()
                .id(existing.getId())
                .merchantId(existing.getMerchantId())
                .code(firstNonBlank(command.code(), existing.getCode()))
                .name(firstNonBlank(command.name(), existing.getName()))
                .type(command.type() != null ? command.type() : existing.getType())
                .address(firstNonBlank(command.address(), existing.getAddress()))
                .wardId(firstNonBlank(command.wardId(), existing.getWardId()))
                .districtId(firstNonBlank(command.districtId(), existing.getDistrictId()))
                .provinceId(firstNonBlank(command.provinceId(), existing.getProvinceId()))
                .timeAtDepartment(command.timeAtDepartment() != null ? command.timeAtDepartment() : existing.getTimeAtDepartment())
                .isShuttleService(command.isShuttleService())
                .openingTime(firstNonBlank(command.openingTime(), existing.getOpeningTime()))
                .closingTime(firstNonBlank(command.closingTime(), existing.getClosingTime()))
                .latitude(command.latitude() != null ? command.latitude() : existing.getLatitude())
                .longitude(command.longitude() != null ? command.longitude() : existing.getLongitude())
                .status(command.status() != null ? command.status() : existing.getStatus())
                .createdAt(existing.getCreatedAt())
                .createdBy(existing.getCreatedBy())
                .updatedAt(existing.getUpdatedAt())
                .updatedBy(existing.getUpdatedBy())
                .build();

        enrichAdministrativeNames(updated);
        departmentRepositoryPort.save(updated);

        return UpdateDepartmentResult.builder()
                .id(updated.getId())
                .code(updated.getCode())
                .name(updated.getName())
                .type(updated.getType())
                .address(updated.getAddress())
                .wardId(updated.getWardId())
                .wardName(updated.getWardName())
                .districtId(updated.getDistrictId())
                .districtName(updated.getDistrictName())
                .provinceId(updated.getProvinceId())
                .provinceName(updated.getProvinceName())
                .timeAtDepartment(updated.getTimeAtDepartment())
                .isShuttleService(updated.isShuttleService())
                .openingTime(updated.getOpeningTime())
                .closingTime(updated.getClosingTime())
                .latitude(updated.getLatitude())
                .longitude(updated.getLongitude())
                .status(updated.getStatus())
                .build();
    }

    private void enrichAdministrativeNames(Department department) {
        if (department.getWardId() != null && !department.getWardId().isBlank()) {
            try {
                wardRepositoryPort.findById(Integer.parseInt(department.getWardId()))
                        .ifPresent(w -> department.setWardName(w.getName()));
            } catch (NumberFormatException ignored) {}
        }
        if (department.getDistrictId() != null && !department.getDistrictId().isBlank()) {
            try {
                districtRepositoryPort.findById(Integer.parseInt(department.getDistrictId()))
                        .ifPresent(d -> department.setDistrictName(d.getName()));
            } catch (NumberFormatException ignored) {}
        }
        if (department.getProvinceId() != null && !department.getProvinceId().isBlank()) {
            try {
                provincesRepositoryPort.findById(Integer.parseInt(department.getProvinceId()))
                        .ifPresent(p -> department.setProvinceName(p.getName()));
            } catch (NumberFormatException ignored) {}
        }
    }

    @Override
    public DeleteDepartmentResult deleteDepartment(DeleteDepartmentCommand command) {
        Department existing = departmentRepositoryPort.findById(command.id(), command.merchantId())
                .orElseThrow(() -> new BusinessException(
                        command.context().requestId(),
                        command.context().requestDateTime(),
                        command.context().channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(DEPARTMENT_NOT_FOUND, command.id()))
                ));

        Department closed = Department.builder()
                .id(existing.getId())
                .merchantId(existing.getMerchantId())
                .code(existing.getCode())
                .name(existing.getName())
                .type(existing.getType())
                .address(existing.getAddress())
                .wardId(existing.getWardId())
                .wardName(existing.getWardName())
                .districtId(existing.getDistrictId())
                .districtName(existing.getDistrictName())
                .provinceId(existing.getProvinceId())
                .provinceName(existing.getProvinceName())
                .timeAtDepartment(existing.getTimeAtDepartment())
                .isShuttleService(existing.isShuttleService())
                .openingTime(existing.getOpeningTime())
                .closingTime(existing.getClosingTime())
                .latitude(existing.getLatitude())
                .longitude(existing.getLongitude())
                .status(DepartmentStatus.CLOSED)
                .createdAt(existing.getCreatedAt())
                .createdBy(existing.getCreatedBy())
                .updatedAt(existing.getUpdatedAt())
                .updatedBy(existing.getUpdatedBy())
                .build();

        departmentRepositoryPort.save(closed);

        return DeleteDepartmentResult.builder()
                .id(closed.getId())
                .code(closed.getCode())
                .status(closed.getStatus())
                .build();
    }

    @Override
    public FetchDepartmentResult fetchDepartment(FetchDepartmentQuery query) {
        int pageSize = parseIntOrDefault(query.pageSize(), DEFAULT_PAGE_SIZE, "pageSize",
                query.context().requestId(), query.context().requestDateTime(), query.context().channel());
        int pageNumber = parseIntOrDefault(query.pageNumber(), DEFAULT_PAGE_NUMBER, "pageNumber",
                query.context().requestId(), query.context().requestDateTime(), query.context().channel());

        if (pageSize < 1 || pageSize > 100) {
            throw new BusinessException(query.context().requestId(), query.context().requestDateTime(), query.context().channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_PAGE_SIZE));
        }
        if (pageNumber < 1) {
            throw new BusinessException(query.context().requestId(), query.context().requestDateTime(), query.context().channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_PAGE_NUMBER));
        }

        PagedResult<Department> page = departmentRepositoryPort.fetch(query.merchantId(), pageNumber - 1, pageSize);
        List<FetchDepartmentResult.FetchDepartmentItemResult> items = page.getItems().stream()
                .map(p -> FetchDepartmentResult.FetchDepartmentItemResult.builder()
                        .id(p.getId())
                        .code(p.getCode())
                        .name(p.getName())
                        .type(p.getType())
                        .address(p.getAddress())
                        .wardId(p.getWardId())
                        .wardName(p.getWardName())
                        .districtId(p.getDistrictId())
                        .districtName(p.getDistrictName())
                        .provinceId(p.getProvinceId())
                        .provinceName(p.getProvinceName())
                        .isShuttleService(p.isShuttleService())
                        .latitude(p.getLatitude())
                        .longitude(p.getLongitude())
                        .status(p.getStatus())
                        .build())
                .toList();

        return FetchDepartmentResult.builder()
                .items(items)
                .pageNumber(page.getPageNumber() + 1)
                .pageSize(page.getPageSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    public GetDepartmentDetailResult getDepartmentDetail(GetDepartmentDetailQuery query) {
        Department department = resolveDepartment(query)
                .orElseThrow(() -> new BusinessException(
                        query.context().requestId(),
                        query.context().requestDateTime(),
                        query.context().channel(),
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, buildDepartmentLookupMessage(query))
                ));

        return GetDepartmentDetailResult.builder()
                .id(department.getId())
                .code(department.getCode())
                .name(department.getName())
                .type(department.getType())
                .address(department.getAddress())
                .wardId(department.getWardId())
                .wardName(department.getWardName())
                .districtId(department.getDistrictId())
                .districtName(department.getDistrictName())
                .provinceId(department.getProvinceId())
                .provinceName(department.getProvinceName())
                .timeAtDepartment(department.getTimeAtDepartment())
                .isShuttleService(department.isShuttleService())
                .openingTime(department.getOpeningTime())
                .closingTime(department.getClosingTime())
                .latitude(department.getLatitude())
                .longitude(department.getLongitude())
                .status(department.getStatus())
                .build();
    }

    private Optional<Department> resolveDepartment(GetDepartmentDetailQuery query) {
        if (hasText(query.departmentId())) {
            return departmentRepositoryPort.findById(query.departmentId().trim(), query.merchantId());
        }
        throw new BusinessException(
                query.context().requestId(),
                query.context().requestDateTime(),
                query.context().channel(),
                ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, "departmentId or code or name is required")
        );
    }

    private String buildDepartmentLookupMessage(GetDepartmentDetailQuery query) {
        if (hasText(query.departmentId())) {
            return String.format(DEPARTMENT_NOT_FOUND, query.departmentId().trim());
        }
        return "Department not found";
    }

    private static int parseIntOrDefault(
            String v,
            int defaultValue,
            String field,
            String requestId,
            String requestDateTime,
            String channel
    ) {
        if (v == null || v.isBlank()) return defaultValue;
        return DateTimeUtils.parseIntOrThrow(v, field, requestId, requestDateTime, channel);
    }

    private static String firstNonBlank(String value, String fallback) {
        return (value == null || value.isBlank()) ? fallback : value;
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
