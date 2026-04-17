package vn.com.routex.merchant.platform.interfaces.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.merchant.platform.application.command.maintenance.CreateMaintenancePlanCommand;
import vn.com.routex.merchant.platform.application.command.maintenance.CreateMaintenancePlanResult;
import vn.com.routex.merchant.platform.application.service.MaintenancePlanManagementService;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.ApiRequestUtils;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.HttpUtils;
import vn.com.routex.merchant.platform.interfaces.factory.ApiResultFactory;
import vn.com.routex.merchant.platform.interfaces.model.maintenance.CreateMaintenancePlanRequest;
import vn.com.routex.merchant.platform.interfaces.model.maintenance.CreateMaintenancePlanResponse;

import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.CREATE_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.MAINTENANCE_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.MERCHANT_SERVICE;

@RestController
@RequestMapping(API_PATH + API_VERSION + MERCHANT_SERVICE)
@RequiredArgsConstructor
public class MerchantMaintenanceController {

    private final MaintenancePlanManagementService maintenancePlanManagementService;
    private final ApiResultFactory apiResultFactory;
    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @PostMapping(MAINTENANCE_PATH + CREATE_PATH)
    public ResponseEntity<CreateMaintenancePlanResponse> createMaintenancePlan(HttpServletRequest servletRequest,
                                                                               @Valid @RequestBody CreateMaintenancePlanRequest request){
        String merchantId = ApiRequestUtils.getMerchantId(servletRequest);

        sLog.info("[MAINTENANCE-PLAN] Create Maintenance Plan Request: {}",  request);
        CreateMaintenancePlanResult result = maintenancePlanManagementService.createMaintenancePlan(
                CreateMaintenancePlanCommand
                        .builder()
                        .merchantId(merchantId)
                        .creator(request.getCreator())
                        .vehicleId(request.getVehicleId())
                        .code(request.getCode())
                        .title(request.getTitle())
                        .description(request.getDescription())
                        .type(request.getType())
                        .plannedDate(request.getPlannedDate())
                        .dueDate(request.getDueDate())
                        .currentOdometerKm(request.getCurrentOdometerKm())
                        .targetOdometerKm(request.getTargetOdometerKm())
                        .estimatedCost(request.getEstimatedCost())
                        .serviceProvider(request.getServiceProvider())
                        .note(request.getNote())
                        .build()
        );

        CreateMaintenancePlanResponse response = CreateMaintenancePlanResponse.builder()
                .requestId(request.getRequestId())
                .requestDateTime(request.getRequestDateTime())
                .channel(request.getChannel())
                .result(apiResultFactory.buildSuccess())
                .data(CreateMaintenancePlanResponse.CreateMaintenancePlanResponseData
                        .builder()
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


        sLog.info("[MAINTENANCE-PLAN] Create Maintenance Plan Response: {}", response);
        return HttpUtils.buildResponse(request, response);
    }
}
