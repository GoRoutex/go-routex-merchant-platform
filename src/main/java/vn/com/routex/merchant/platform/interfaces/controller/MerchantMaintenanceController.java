package vn.com.routex.merchant.platform.interfaces.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.merchant.platform.application.command.maintenance.CreateMaintenancePlanCommand;
import vn.com.routex.merchant.platform.application.command.maintenance.CreateMaintenancePlanResult;
import vn.com.routex.merchant.platform.application.command.maintenance.DeleteMaintenancePlanCommand;
import vn.com.routex.merchant.platform.application.command.maintenance.DeleteMaintenancePlanResult;
import vn.com.routex.merchant.platform.application.command.maintenance.FetchMaintenancePlanDetailQuery;
import vn.com.routex.merchant.platform.application.command.maintenance.FetchMaintenancePlanDetailResult;
import vn.com.routex.merchant.platform.application.command.maintenance.FetchMaintenancePlansQuery;
import vn.com.routex.merchant.platform.application.command.maintenance.FetchMaintenancePlansResult;
import vn.com.routex.merchant.platform.application.command.maintenance.UpdateMaintenancePlanCommand;
import vn.com.routex.merchant.platform.application.command.maintenance.UpdateMaintenancePlanResult;
import vn.com.routex.merchant.platform.application.service.MaintenancePlanManagementService;
import vn.com.routex.merchant.platform.domain.maintenance.MaintenancePlanStatus;
import vn.com.routex.merchant.platform.domain.maintenance.MaintenancePlanType;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.ApiRequestUtils;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.HttpUtils;
import vn.com.routex.merchant.platform.interfaces.factory.ApiResultFactory;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseRequest;
import vn.com.routex.merchant.platform.interfaces.model.maintenance.CreateMaintenancePlanRequest;
import vn.com.routex.merchant.platform.interfaces.model.maintenance.CreateMaintenancePlanResponse;
import vn.com.routex.merchant.platform.interfaces.model.maintenance.DeleteMaintenancePlanRequest;
import vn.com.routex.merchant.platform.interfaces.model.maintenance.DeleteMaintenancePlanResponse;
import vn.com.routex.merchant.platform.interfaces.model.maintenance.FetchMaintenancePlanDetailResponse;
import vn.com.routex.merchant.platform.interfaces.model.maintenance.FetchMaintenancePlanResponse;
import vn.com.routex.merchant.platform.interfaces.model.maintenance.UpdateMaintenancePlanRequest;
import vn.com.routex.merchant.platform.interfaces.model.maintenance.UpdateMaintenancePlanResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.CREATE_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.DELETE_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.DETAIL_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.FETCH_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.MAINTENANCE_PLAN_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.MERCHANT_SERVICE;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.UPDATE_PATH;

@RestController
@RequestMapping(API_PATH + API_VERSION + MERCHANT_SERVICE)
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('vehicle:management') or hasRole('ADMIN')")
public class MerchantMaintenanceController {

    private final MaintenancePlanManagementService maintenancePlanManagementService;
    private final ApiResultFactory apiResultFactory;
    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder, WebRequest webRequest) {
        webDataBinder.setDisallowedFields("requestId", "requestDateTime", "channel", "data");
    }

    @PostMapping(MAINTENANCE_PLAN_PATH + CREATE_PATH)
    public ResponseEntity<CreateMaintenancePlanResponse> createMaintenancePlan(
            HttpServletRequest servletRequest,
            @Valid @RequestBody CreateMaintenancePlanRequest request
    ) {
        String merchantId = ApiRequestUtils.requireMerchantId(servletRequest, request);
        sLog.info("[MAINTENANCE-PLAN] Create Maintenance Plan Request: {}", request);

        CreateMaintenancePlanResult result = maintenancePlanManagementService.createMaintenancePlan(
                CreateMaintenancePlanCommand.builder()
                        .context(HttpUtils.toContext(request, merchantId))
                        .merchantId(merchantId)
                        .creator(request.getData().getCreator())
                        .vehicleId(request.getData().getVehicleId())
                        .code(request.getData().getCode())
                        .title(request.getData().getTitle())
                        .description(request.getData().getDescription())
                        .type(request.getData().getType())
                        .plannedDate(request.getData().getPlannedDate())
                        .dueDate(request.getData().getDueDate())
                        .currentOdometerKm(request.getData().getCurrentOdometerKm())
                        .targetOdometerKm(request.getData().getTargetOdometerKm())
                        .estimatedCost(request.getData().getEstimatedCost())
                        .serviceProvider(request.getData().getServiceProvider())
                        .note(request.getData().getNote())
                        .build()
        );

        CreateMaintenancePlanResponse response = CreateMaintenancePlanResponse.builder()
                .result(apiResultFactory.buildSuccess())
                .data(CreateMaintenancePlanResponse.CreateMaintenancePlanResponseData.builder()
                        .id(result.id())
                        .merchantId(result.merchantId())
                        .vehicleId(result.vehicleId())
                        .code(result.code())
                        .title(result.title())
                        .description(result.description())
                        .type(result.type())
                        .status(result.status())
                        .plannedDate(result.plannedDate())
                        .dueDate(result.dueDate())
                        .completedDate(result.completedDate())
                        .currentOdometerKm(result.currentOdometerKm())
                        .targetOdometerKm(result.targetOdometerKm())
                        .estimatedCost(result.estimatedCost())
                        .actualCost(result.actualCost())
                        .serviceProvider(result.serviceProvider())
                        .note(result.note())
                        .build())
                .build();

        return HttpUtils.buildResponse(request, response);
    }

    @PostMapping(MAINTENANCE_PLAN_PATH + UPDATE_PATH)
    public ResponseEntity<UpdateMaintenancePlanResponse> updateMaintenancePlan(
            HttpServletRequest servletRequest,
            @Valid @RequestBody UpdateMaintenancePlanRequest request
    ) {
        String merchantId = ApiRequestUtils.requireMerchantId(servletRequest, request);
        sLog.info("[MAINTENANCE-PLAN] Update Maintenance Plan Request: {}", request);

        UpdateMaintenancePlanResult result = maintenancePlanManagementService.updateMaintenancePlan(
                UpdateMaintenancePlanCommand.builder()
                        .context(HttpUtils.toContext(request, merchantId))
                        .merchantId(merchantId)
                        .creator(request.getData().getCreator())
                        .maintenancePlanId(request.getData().getMaintenancePlanId())
                        .vehicleId(request.getData().getVehicleId())
                        .code(request.getData().getCode())
                        .title(request.getData().getTitle())
                        .description(request.getData().getDescription())
                        .type(request.getData().getType())
                        .status(request.getData().getStatus())
                        .plannedDate(request.getData().getPlannedDate())
                        .dueDate(request.getData().getDueDate())
                        .completedDate(request.getData().getCompletedDate())
                        .currentOdometerKm(request.getData().getCurrentOdometerKm())
                        .targetOdometerKm(request.getData().getTargetOdometerKm())
                        .estimatedCost(request.getData().getEstimatedCost())
                        .actualCost(request.getData().getActualCost())
                        .serviceProvider(request.getData().getServiceProvider())
                        .note(request.getData().getNote())
                        .build()
        );

        UpdateMaintenancePlanResponse response = UpdateMaintenancePlanResponse.builder()
                .result(apiResultFactory.buildSuccess())
                .data(UpdateMaintenancePlanResponse.UpdateMaintenancePlanResponseData.builder()
                        .id(result.id())
                        .merchantId(result.merchantId())
                        .vehicleId(result.vehicleId())
                        .code(result.code())
                        .title(result.title())
                        .description(result.description())
                        .type(result.type())
                        .status(result.status())
                        .plannedDate(result.plannedDate())
                        .dueDate(result.dueDate())
                        .completedDate(result.completedDate())
                        .currentOdometerKm(result.currentOdometerKm())
                        .targetOdometerKm(result.targetOdometerKm())
                        .estimatedCost(result.estimatedCost())
                        .actualCost(result.actualCost())
                        .serviceProvider(result.serviceProvider())
                        .note(result.note())
                        .build())
                .build();

        return HttpUtils.buildResponse(request, response);
    }

    @PostMapping(MAINTENANCE_PLAN_PATH + DELETE_PATH)
    public ResponseEntity<DeleteMaintenancePlanResponse> deleteMaintenancePlan(
            HttpServletRequest servletRequest,
            @Valid @RequestBody DeleteMaintenancePlanRequest request
    ) {
        String merchantId = ApiRequestUtils.requireMerchantId(servletRequest, request);
        sLog.info("[MAINTENANCE-PLAN] Delete Maintenance Plan Request: {}", request);

        DeleteMaintenancePlanResult result = maintenancePlanManagementService.deleteMaintenancePlan(
                DeleteMaintenancePlanCommand.builder()
                        .context(HttpUtils.toContext(request, merchantId))
                        .merchantId(merchantId)
                        .creator(request.getData().getCreator())
                        .maintenancePlanId(request.getData().getMaintenancePlanId())
                        .build()
        );

        DeleteMaintenancePlanResponse response = DeleteMaintenancePlanResponse.builder()
                .result(apiResultFactory.buildSuccess())
                .data(DeleteMaintenancePlanResponse.DeleteMaintenancePlanResponseData.builder()
                        .id(result.id())
                        .code(result.code())
                        .status(result.status())
                        .build())
                .build();

        return HttpUtils.buildResponse(request, response);
    }

    @GetMapping(MAINTENANCE_PLAN_PATH + FETCH_PATH)
    public ResponseEntity<FetchMaintenancePlanResponse> fetchMaintenancePlans(
            HttpServletRequest servletRequest,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String vehicleId,
            @RequestParam(required = false) MaintenancePlanStatus status,
            @RequestParam(required = false) MaintenancePlanType type,
            @RequestParam(required = false) LocalDate fromPlannedDate,
            @RequestParam(required = false) LocalDate toPlannedDate
    ) {
        BaseRequest baseRequest = ApiRequestUtils.getBaseRequestOrDefault(servletRequest);
        String merchantId = ApiRequestUtils.requireMerchantId(servletRequest, baseRequest);

        FetchMaintenancePlansResult result = maintenancePlanManagementService.fetchMaintenancePlans(
                FetchMaintenancePlansQuery.builder()
                        .context(HttpUtils.toContext(baseRequest, merchantId))
                        .merchantId(merchantId)
                        .pageNumber(String.valueOf(pageNumber))
                        .pageSize(String.valueOf(pageSize))
                        .vehicleId(vehicleId)
                        .status(status)
                        .type(type)
                        .fromPlannedDate(fromPlannedDate)
                        .toPlannedDate(toPlannedDate)
                        .build()
        );

        List<FetchMaintenancePlanResponse.FetchMaintenancePlanResponseData> items = result.items().stream()
                .map(item -> FetchMaintenancePlanResponse.FetchMaintenancePlanResponseData.builder()
                        .id(item.id())
                        .vehicle(FetchMaintenancePlanResponse.MaintenancePlanVehicleResponseData.builder()
                                .id(item.vehicle().id())
                                .templateId(item.vehicle().templateId())
                                .status(item.vehicle().status())
                                .category(item.vehicle().category())
                                .type(item.vehicle().type())
                                .vehiclePlate(item.vehicle().vehiclePlate())
                                .seatCapacity(item.vehicle().seatCapacity())
                                .hasFloor(item.vehicle().hasFloor())
                                .manufacturer(item.vehicle().manufacturer())
                                .build())
                        .code(item.code())
                        .title(item.title())
                        .type(item.type())
                        .status(item.status())
                        .plannedDate(item.plannedDate())
                        .dueDate(item.dueDate())
                        .targetOdometerKm(item.targetOdometerKm())
                        .estimatedCost(item.estimatedCost())
                        .serviceProvider(item.serviceProvider())
                        .build())
                .collect(Collectors.toList());

        FetchMaintenancePlanResponse response = FetchMaintenancePlanResponse.builder()
                .requestId(baseRequest.getRequestId())
                .requestDateTime(baseRequest.getRequestDateTime())
                .channel(baseRequest.getChannel())
                .result(apiResultFactory.buildSuccess())
                .data(FetchMaintenancePlanResponse.FetchMaintenancePlanResponsePage.builder()
                        .items(items)
                        .pagination(FetchMaintenancePlanResponse.Pagination.builder()
                                .pageNumber(result.pageNumber())
                                .pageSize(result.pageSize())
                                .totalElements(result.totalElements())
                                .totalPages(result.totalPages())
                                .build())
                        .build())
                .build();

        return HttpUtils.buildResponse(baseRequest, response);
    }

    @GetMapping(MAINTENANCE_PLAN_PATH + DETAIL_PATH)
    public ResponseEntity<FetchMaintenancePlanDetailResponse> fetchMaintenancePlanDetail(
            HttpServletRequest servletRequest,
            @RequestParam String maintenancePlanId
    ) {
        BaseRequest baseRequest = ApiRequestUtils.getBaseRequestOrDefault(servletRequest);
        String merchantId = ApiRequestUtils.requireMerchantId(servletRequest, baseRequest);

        FetchMaintenancePlanDetailResult result = maintenancePlanManagementService.fetchMaintenancePlanDetail(
                FetchMaintenancePlanDetailQuery.builder()
                        .context(HttpUtils.toContext(baseRequest, merchantId))
                        .merchantId(merchantId)
                        .maintenancePlanId(maintenancePlanId)
                        .build()
        );

        FetchMaintenancePlanDetailResponse response = FetchMaintenancePlanDetailResponse.builder()
                .requestId(baseRequest.getRequestId())
                .requestDateTime(baseRequest.getRequestDateTime())
                .channel(baseRequest.getChannel())
                .result(apiResultFactory.buildSuccess())
                .data(FetchMaintenancePlanDetailResponse.FetchMaintenancePlanDetailResponseData.builder()
                        .id(result.id())
                        .merchantId(result.merchantId())
                        .vehicle(FetchMaintenancePlanDetailResponse.MaintenancePlanVehicleDetailResponseData.builder()
                                .id(result.vehicle().id())
                                .templateId(result.vehicle().templateId())
                                .status(result.vehicle().status())
                                .category(result.vehicle().category())
                                .type(result.vehicle().type())
                                .vehiclePlate(result.vehicle().vehiclePlate())
                                .seatCapacity(result.vehicle().seatCapacity())
                                .hasFloor(result.vehicle().hasFloor())
                                .manufacturer(result.vehicle().manufacturer())
                                .build())
                        .code(result.code())
                        .title(result.title())
                        .description(result.description())
                        .type(result.type())
                        .status(result.status())
                        .plannedDate(result.plannedDate())
                        .dueDate(result.dueDate())
                        .completedDate(result.completedDate())
                        .currentOdometerKm(result.currentOdometerKm())
                        .targetOdometerKm(result.targetOdometerKm())
                        .estimatedCost(result.estimatedCost())
                        .actualCost(result.actualCost())
                        .serviceProvider(result.serviceProvider())
                        .note(result.note())
                        .build())
                .build();

        return HttpUtils.buildResponse(baseRequest, response);
    }
}
