package vn.com.routex.merchant.platform.application.service;

import vn.com.routex.merchant.platform.application.command.holiday.CreateHolidayCommand;
import vn.com.routex.merchant.platform.application.command.holiday.DeleteHolidayCommand;
import vn.com.routex.merchant.platform.application.command.holiday.DeleteHolidayResult;
import vn.com.routex.merchant.platform.application.command.holiday.HolidayResult;
import vn.com.routex.merchant.platform.application.command.holiday.UpdateHolidayCommand;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface HolidayService {
    boolean isHolidayOrPeakDay(LocalDate date);
    String getHolidayName(LocalDate date);
    BigDecimal getSurchargeRate(LocalDate date);
    
    HolidayResult createHoliday(CreateHolidayCommand command);
    HolidayResult updateHoliday(UpdateHolidayCommand command);
    DeleteHolidayResult deleteHoliday(DeleteHolidayCommand command);
    List<HolidayResult> getAllHolidays();
}

