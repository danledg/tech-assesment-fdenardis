package com.itv.leedstech.techtest.service;

import com.itv.leedstech.techtest.dto.ProgrammeDto;
import com.itv.leedstech.techtest.dto.ScheduleEntryDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface CostCalculator {

    BigDecimal getFullScheduleTotalPlayoutCost();

    BigDecimal getProgrammeTotalPlayoutCost(String programmeId);

    BigDecimal getFullScheduleTotalPlayoutCostBetweenDates(LocalDate startDate, LocalDate endDate);

    BigDecimal getProgrammeTotalPlayoutCostBetweenDates(String programmeId, LocalDate startDate, LocalDate endDate);

    List<ScheduleEntryPlayoutCost> getFullSchedulePlayoutCost(List<ScheduleEntryDto> programmeSchedule);

    List<ScheduleEntryPlayoutCost> getProgrammePlayoutCost(ProgrammeDto programme, List<ScheduleEntryDto> programmeSchedule);
}
