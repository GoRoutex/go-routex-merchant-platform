package vn.com.routex.merchant.platform.interfaces.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.routex.merchant.platform.application.command.holiday.CreateHolidayCommand;
import vn.com.routex.merchant.platform.application.command.holiday.DeleteHolidayCommand;
import vn.com.routex.merchant.platform.application.command.holiday.DeleteHolidayResult;
import vn.com.routex.merchant.platform.application.command.holiday.HolidayResult;
import vn.com.routex.merchant.platform.application.command.holiday.UpdateHolidayCommand;
import vn.com.routex.merchant.platform.application.service.HolidayService;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.ApiRequestUtils;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.HttpUtils;
import vn.com.routex.merchant.platform.interfaces.factory.ApiResultFactory;
import vn.com.routex.merchant.platform.interfaces.model.holiday.request.CreateHolidayRequest;
import vn.com.routex.merchant.platform.interfaces.model.holiday.request.DeleteHolidayRequest;
import vn.com.routex.merchant.platform.interfaces.model.holiday.request.UpdateHolidayRequest;
import vn.com.routex.merchant.platform.interfaces.model.holiday.response.DeleteHolidayResponse;
import vn.com.routex.merchant.platform.interfaces.model.holiday.response.HolidayResponse;

import java.util.List;

import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.DELETE_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.MERCHANT_SERVICE;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.UPDATE_PATH;

@RestController
@RequestMapping(API_PATH + API_VERSION + MERCHANT_SERVICE + "/holidays")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN') or hasRole('MERCHANT_ADMIN')")
public class HolidayController {

    private final HolidayService holidayService;
    private final ApiResultFactory apiResultFactory;

    @PostMapping
    public ResponseEntity<HolidayResponse> createHoliday(
            @Valid @RequestBody CreateHolidayRequest request) {
        CreateHolidayCommand command = CreateHolidayCommand.builder()
                .holidayDate(request.getData().getHolidayDate())
                .name(request.getData().getName())
                .isPeakDay(request.getData().getIsPeakDay())
                .surchargeRate(request.getData().getSurchargeRate())
                .description(request.getData().getDescription())
                .context(ApiRequestUtils.getRequestContext(request))
                .build();

        HolidayResult result = holidayService.createHoliday(command);

        HolidayResponse response = HolidayResponse.builder()
                .result(apiResultFactory.buildSuccess())
                .data(mapToData(result))
                .build();

        return HttpUtils.buildResponse(request, response);
    }

    @PostMapping(UPDATE_PATH + "/{id}")
    public ResponseEntity<HolidayResponse> updateHoliday(
            @PathVariable String id,
            @Valid @RequestBody UpdateHolidayRequest request) {

        UpdateHolidayCommand command = UpdateHolidayCommand.builder()
                .id(id)
                .holidayDate(request.getData().getHolidayDate())
                .name(request.getData().getName())
                .isPeakDay(request.getData().getIsPeakDay())
                .surchargeRate(request.getData().getSurchargeRate())
                .description(request.getData().getDescription())
                .context(ApiRequestUtils.getRequestContext(request))
                .build();

        HolidayResult result = holidayService.updateHoliday(command);

        HolidayResponse response = HolidayResponse.builder()
                .result(apiResultFactory.buildSuccess())
                .data(mapToData(result))
                .build();

        return HttpUtils.buildResponse(request, response);
    }

    @PostMapping(DELETE_PATH)
    public ResponseEntity<DeleteHolidayResponse> deleteHoliday(
            @Valid @RequestBody DeleteHolidayRequest request) {

        DeleteHolidayCommand command = DeleteHolidayCommand.builder()
                .id(request.getData().getId())
                .context(ApiRequestUtils.getRequestContext(request))
                .build();

        DeleteHolidayResult result = holidayService.deleteHoliday(command);

        DeleteHolidayResponse response = DeleteHolidayResponse.builder()
                .result(apiResultFactory.buildSuccess())
                .data(DeleteHolidayResponse.DeleteHolidayData.builder()
                        .id(result.id())
                        .status(result.status())
                        .build())
                .build();

        return HttpUtils.buildResponse(request, response);
    }


    @GetMapping
    public ResponseEntity<List<HolidayResponse.HolidayData>> getAllHolidays() {
        return ResponseEntity.ok(holidayService.getAllHolidays().stream()
                .map(this::mapToData)
                .toList());
    }

    private HolidayResponse.HolidayData mapToData(HolidayResult result) {
        return HolidayResponse.HolidayData.builder()
                .id(result.id())
                .holidayDate(result.holidayDate())
                .name(result.name())
                .isPeakDay(result.isPeakDay())
                .surchargeRate(result.surchargeRate())
                .description(result.description())
                .build();
    }
}


