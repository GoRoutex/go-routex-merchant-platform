package vn.com.routex.merchant.platform.application.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.routex.merchant.platform.application.command.vehicletemplate.CreateVehicleTemplateCommand;
import vn.com.routex.merchant.platform.application.command.vehicletemplate.CreateVehicleTemplateResult;
import vn.com.routex.merchant.platform.application.command.vehicletemplate.DeleteVehicleTemplateCommand;
import vn.com.routex.merchant.platform.application.command.vehicletemplate.DeleteVehicleTemplateResult;
import vn.com.routex.merchant.platform.application.command.vehicletemplate.FetchVehicleTemplateDetailQuery;
import vn.com.routex.merchant.platform.application.command.vehicletemplate.FetchVehicleTemplateDetailResult;
import vn.com.routex.merchant.platform.application.command.vehicletemplate.FetchVehicleTemplatesQuery;
import vn.com.routex.merchant.platform.application.command.vehicletemplate.FetchVehicleTemplatesResult;
import vn.com.routex.merchant.platform.application.command.vehicletemplate.UpdateVehicleTemplateCommand;
import vn.com.routex.merchant.platform.application.command.vehicletemplate.UpdateVehicleTemplateResult;
import vn.com.routex.merchant.platform.application.service.VehicleTemplateManagementService;
import vn.com.routex.merchant.platform.domain.common.PagedResult;
import vn.com.routex.merchant.platform.domain.vehicle.VehicleTemplateStatus;
import vn.com.routex.merchant.platform.domain.vehicle.model.VehicleTemplate;
import vn.com.routex.merchant.platform.domain.vehicle.port.VehicleTemplateRepositoryPort;
import vn.com.routex.merchant.platform.infrastructure.persistence.exception.BusinessException;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.ApiRequestUtils;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.ExceptionUtils;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApplicationConstant.DEFAULT_PAGE_NUMBER;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApplicationConstant.DEFAULT_PAGE_SIZE;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_ERROR;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_VEHICLE_TEMPLATE_CATEGORY_TYPE;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.DUPLICATE_VEHICLE_TEMPLATE_CODE;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.INVALID_INPUT_ERROR;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.INVALID_PAGE_NUMBER;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.INVALID_PAGE_SIZE;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.RECORD_NOT_FOUND;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ErrorConstant.VEHICLE_TEMPLATE_NOT_FOUND_BY_ID;

@Service
@RequiredArgsConstructor
public class VehicleTemplateManagementServiceImpl implements VehicleTemplateManagementService {

    private final VehicleTemplateRepositoryPort vehicleTemplateRepositoryPort;

    @Override
    @Transactional
    public CreateVehicleTemplateResult createVehicleTemplate(CreateVehicleTemplateCommand command) {
        validateTemplateCodeUniqueness(command.code(), command.merchantId(), command);
        validateTemplateCategoryTypeUniqueness(command.category().name(), command.type().name(), command.merchantId(), command);

        VehicleTemplate template = VehicleTemplate.builder()
                .id(UUID.randomUUID().toString())
                .merchantId(command.merchantId())
                .code(command.code())
                .name(command.name())
                .manufacturer(command.manufacturer())
                .model(command.model())
                .seatCapacity(command.seatCapacity())
                .category(command.category())
                .type(command.type())
                .fuelType(command.fuelType())
                .hasFloor(Boolean.TRUE.equals(command.hasFloor()))
                .status(command.status() == null ? VehicleTemplateStatus.ACTIVE : command.status())
                .createdAt(OffsetDateTime.now())
                .createdBy(command.creator())
                .build();

        vehicleTemplateRepositoryPort.save(template);
        return toCreateResult(template);
    }

    @Override
    @Transactional
    public UpdateVehicleTemplateResult updateVehicleTemplate(UpdateVehicleTemplateCommand command) {
        VehicleTemplate existing = findTemplate(command.templateId(), command.merchantId(),
                command.context().requestId(), command.context().requestDateTime(), command.context().channel());

        validateUpdatedCode(existing, command);
        validateUpdatedCategoryType(existing, command);

        VehicleTemplate updated = VehicleTemplate.builder()
                .id(existing.getId())
                .merchantId(existing.getMerchantId())
                .code(ApiRequestUtils.firstNonBlank(command.code(), existing.getCode()))
                .name(ApiRequestUtils.firstNonBlank(command.name(), existing.getName()))
                .manufacturer(ApiRequestUtils.firstNonBlank(command.manufacturer(), existing.getManufacturer()))
                .model(ApiRequestUtils.firstNonBlank(command.model(), existing.getModel()))
                .seatCapacity(command.seatCapacity() == null ? existing.getSeatCapacity() : command.seatCapacity())
                .category(command.category() == null ? existing.getCategory() : command.category())
                .type(command.type() == null ? existing.getType() : command.type())
                .fuelType(command.fuelType() == null ? existing.getFuelType() : command.fuelType())
                .hasFloor(command.hasFloor() == null ? existing.isHasFloor() : command.hasFloor())
                .status(command.status() == null ? existing.getStatus() : command.status())
                .createdAt(existing.getCreatedAt())
                .createdBy(existing.getCreatedBy())
                .updatedAt(OffsetDateTime.now())
                .updatedBy(command.creator())
                .build();

        vehicleTemplateRepositoryPort.save(updated);
        return toUpdateResult(updated);
    }

    @Override
    @Transactional
    public DeleteVehicleTemplateResult deleteVehicleTemplate(DeleteVehicleTemplateCommand command) {
        VehicleTemplate existing = findTemplate(command.templateId(), command.merchantId(),
                command.context().requestId(), command.context().requestDateTime(), command.context().channel());

        VehicleTemplate deleted = VehicleTemplate.builder()
                .id(existing.getId())
                .merchantId(existing.getMerchantId())
                .code(existing.getCode())
                .name(existing.getName())
                .manufacturer(existing.getManufacturer())
                .model(existing.getModel())
                .seatCapacity(existing.getSeatCapacity())
                .category(existing.getCategory())
                .type(existing.getType())
                .fuelType(existing.getFuelType())
                .hasFloor(existing.isHasFloor())
                .status(VehicleTemplateStatus.INACTIVE)
                .createdAt(existing.getCreatedAt())
                .createdBy(existing.getCreatedBy())
                .updatedAt(OffsetDateTime.now())
                .updatedBy(command.creator())
                .build();

        vehicleTemplateRepositoryPort.save(deleted);
        return DeleteVehicleTemplateResult.builder()
                .id(deleted.getId())
                .code(deleted.getCode())
                .status(deleted.getStatus())
                .build();
    }

    @Override
    public FetchVehicleTemplatesResult fetchVehicleTemplates(FetchVehicleTemplatesQuery query) {
        int pageSize = ApiRequestUtils.parseIntOrDefault(query.pageSize(), DEFAULT_PAGE_SIZE, "pageSize",
                query.context().requestId(), query.context().requestDateTime(), query.context().channel());
        int pageNumber = ApiRequestUtils.parseIntOrDefault(query.pageNumber(), DEFAULT_PAGE_NUMBER, "pageNumber",
                query.context().requestId(), query.context().requestDateTime(), query.context().channel());

        validatePaging(query, pageSize, pageNumber);

        PagedResult<VehicleTemplate> page = vehicleTemplateRepositoryPort.fetch(
                query.merchantId(),
                query.status(),
                query.category(),
                query.type(),
                pageNumber - 1,
                pageSize);
        List<FetchVehicleTemplatesResult.FetchVehicleTemplateItemResult> items = page.getItems().stream()
                .map(this::toFetchItemResult)
                .toList();

        return FetchVehicleTemplatesResult.builder()
                .items(items)
                .pageNumber(page.getPageNumber() + 1)
                .pageSize(page.getPageSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    public FetchVehicleTemplateDetailResult fetchVehicleTemplateDetail(FetchVehicleTemplateDetailQuery query) {
        VehicleTemplate template = findTemplate(query.templateId(), query.merchantId(),
                query.context().requestId(), query.context().requestDateTime(), query.context().channel());
        return toDetailResult(template);
    }

    private void validatePaging(FetchVehicleTemplatesQuery query, int pageSize, int pageNumber) {
        if (pageSize < 1 || pageSize > 100) {
            throw new BusinessException(query.context().requestId(), query.context().requestDateTime(), query.context().channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_PAGE_SIZE));
        }
        if (pageNumber < 1) {
            throw new BusinessException(query.context().requestId(), query.context().requestDateTime(), query.context().channel(),
                    ExceptionUtils.buildResultResponse(INVALID_INPUT_ERROR, INVALID_PAGE_NUMBER));
        }
    }

    private void validateTemplateCodeUniqueness(String code, String merchantId, CreateVehicleTemplateCommand command) {
        if (vehicleTemplateRepositoryPort.existsByCode(code, merchantId)) {
            throw new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                    ExceptionUtils.buildResultResponse(DUPLICATE_ERROR, String.format(DUPLICATE_VEHICLE_TEMPLATE_CODE, code)));
        }
    }

    private void validateTemplateCategoryTypeUniqueness(String category, String type, String merchantId, CreateVehicleTemplateCommand command) {
        if (vehicleTemplateRepositoryPort.existsByCategoryAndType(category, type, merchantId)) {
            throw new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                    ExceptionUtils.buildResultResponse(DUPLICATE_ERROR, String.format(DUPLICATE_VEHICLE_TEMPLATE_CATEGORY_TYPE, category, type)));
        }
    }

    private void validateUpdatedCode(VehicleTemplate existing, UpdateVehicleTemplateCommand command) {
        if (command.code() == null || command.code().isBlank() || command.code().trim().equals(existing.getCode())) {
            return;
        }
        if (vehicleTemplateRepositoryPort.existsByCode(command.code().trim(), command.merchantId())) {
            throw new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                    ExceptionUtils.buildResultResponse(DUPLICATE_ERROR, String.format(DUPLICATE_VEHICLE_TEMPLATE_CODE, command.code().trim())));
        }
    }

    private void validateUpdatedCategoryType(VehicleTemplate existing, UpdateVehicleTemplateCommand command) {
        String category = command.category() == null ? existing.getCategory().name() : command.category().name();
        String type = command.type() == null ? existing.getType().name() : command.type().name();
        boolean sameCategoryType = existing.getCategory().name().equals(category) && existing.getType().name().equals(type);
        if (sameCategoryType) {
            return;
        }
        if (vehicleTemplateRepositoryPort.existsByCategoryAndType(category, type, command.merchantId())) {
            throw new BusinessException(command.context().requestId(), command.context().requestDateTime(), command.context().channel(),
                    ExceptionUtils.buildResultResponse(DUPLICATE_ERROR, String.format(DUPLICATE_VEHICLE_TEMPLATE_CATEGORY_TYPE, category, type)));
        }
    }

    private VehicleTemplate findTemplate(String templateId, String merchantId, String requestId, String requestDateTime, String channel) {
        return vehicleTemplateRepositoryPort.findById(templateId, merchantId)
                .orElseThrow(() -> new BusinessException(requestId, requestDateTime, channel,
                        ExceptionUtils.buildResultResponse(RECORD_NOT_FOUND, String.format(VEHICLE_TEMPLATE_NOT_FOUND_BY_ID, templateId))));
    }

    private CreateVehicleTemplateResult toCreateResult(VehicleTemplate template) {
        return CreateVehicleTemplateResult.builder()
                .id(template.getId())
                .merchantId(template.getMerchantId())
                .code(template.getCode())
                .name(template.getName())
                .manufacturer(template.getManufacturer())
                .model(template.getModel())
                .seatCapacity(template.getSeatCapacity())
                .category(template.getCategory())
                .type(template.getType())
                .fuelType(template.getFuelType())
                .hasFloor(template.isHasFloor())
                .status(template.getStatus())
                .build();
    }

    private UpdateVehicleTemplateResult toUpdateResult(VehicleTemplate template) {
        return UpdateVehicleTemplateResult.builder()
                .id(template.getId())
                .merchantId(template.getMerchantId())
                .code(template.getCode())
                .name(template.getName())
                .manufacturer(template.getManufacturer())
                .model(template.getModel())
                .seatCapacity(template.getSeatCapacity())
                .category(template.getCategory())
                .type(template.getType())
                .fuelType(template.getFuelType())
                .hasFloor(template.isHasFloor())
                .status(template.getStatus())
                .build();
    }

    private FetchVehicleTemplatesResult.FetchVehicleTemplateItemResult toFetchItemResult(VehicleTemplate template) {
        return FetchVehicleTemplatesResult.FetchVehicleTemplateItemResult.builder()
                .id(template.getId())
                .merchantId(template.getMerchantId())
                .code(template.getCode())
                .name(template.getName())
                .manufacturer(template.getManufacturer())
                .model(template.getModel())
                .seatCapacity(template.getSeatCapacity())
                .category(template.getCategory())
                .type(template.getType())
                .fuelType(template.getFuelType())
                .hasFloor(template.isHasFloor())
                .status(template.getStatus())
                .build();
    }

    private FetchVehicleTemplateDetailResult toDetailResult(VehicleTemplate template) {
        return FetchVehicleTemplateDetailResult.builder()
                .id(template.getId())
                .merchantId(template.getMerchantId())
                .code(template.getCode())
                .name(template.getName())
                .manufacturer(template.getManufacturer())
                .model(template.getModel())
                .seatCapacity(template.getSeatCapacity())
                .category(template.getCategory())
                .type(template.getType())
                .fuelType(template.getFuelType())
                .hasFloor(template.isHasFloor())
                .status(template.getStatus())
                .build();
    }
}
