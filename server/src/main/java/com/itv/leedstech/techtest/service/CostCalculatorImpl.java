package com.itv.leedstech.techtest.service;

import com.itv.leedstech.techtest.dto.ProgrammeDto;
import com.itv.leedstech.techtest.dto.ScheduleEntryDto;
import com.itv.leedstech.techtest.exception.ProgrammeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CostCalculatorImpl implements CostCalculator {

    private final ScheduleManagementService scheduleManagementService;

    @Override
    public BigDecimal getFullScheduleTotalPlayoutCost() {
        return getFullSchedulePlayoutCost().stream()
                .map(ScheduleEntryPlayoutCost::getActualPlayoutCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getProgrammeTotalPlayoutCost(final String programmeId) {
        return getFullSchedulePlayoutCost().stream()
                .filter(entry -> entry.getScheduleEntry().getProgramId().equals(programmeId))
                .map(ScheduleEntryPlayoutCost::getActualPlayoutCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getFullScheduleTotalPlayoutCostBetweenDates(final LocalDate startDate, final LocalDate endDate) {
        return getFullSchedulePlayoutCost().stream()
                .filter(entry -> isBetweenDates(entry, startDate, endDate))
                .map(ScheduleEntryPlayoutCost::getActualPlayoutCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getProgrammeTotalPlayoutCostBetweenDates(final String programmeId, final LocalDate startDate, final LocalDate endDate) {
        return getFullSchedulePlayoutCost().stream()
                .filter(entry -> entry.getScheduleEntry().getProgramId().equals(programmeId))
                .filter(entry -> isBetweenDates(entry, startDate, endDate))
                .map(ScheduleEntryPlayoutCost::getActualPlayoutCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    @Override
    public List<ScheduleEntryPlayoutCost> getFullSchedulePlayoutCost(final List<ScheduleEntryDto> programmeSchedule) {
        return programmeSchedule.stream()
                .collect(Collectors.groupingBy(ScheduleEntryDto::getProgramId))
                .entrySet()
                .stream()
                .map(mapEntry -> getProgrammePlayoutCost(getProgramme(mapEntry.getKey()), mapEntry.getValue()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleEntryPlayoutCost> getProgrammePlayoutCost(final ProgrammeDto programme, final List<ScheduleEntryDto> programmeSchedule) {

        final List<ScheduleEntryPlayoutCost> result = new ArrayList<>();

        LocalDateTime lastFullCostPlayoutTime = LocalDateTime.MIN;
        boolean isFreeRepetitionApplied = false;
        boolean isDiscountRepetitionApplied = false;

        List<ScheduleEntryDto> sortedSchedule = programmeSchedule.stream()
                .sorted(Comparator.comparing(ScheduleEntryDto::getStartDateTime))
                .collect(Collectors.toList());
        for (ScheduleEntryDto scheduleEntry: sortedSchedule) {

            if(!programme.getIsDiscountable()) {
                result.add(ScheduleEntryPlayoutCost.builder()
                        .scheduleEntry(scheduleEntry)
                        .actualPlayoutCost(BigDecimal.valueOf(programme.getPlayoutCost()))
                        .build());
            } else if (isWithinThreeDays(lastFullCostPlayoutTime, scheduleEntry)  && !isFreeRepetitionApplied) {
                isFreeRepetitionApplied = true;
                result.add(ScheduleEntryPlayoutCost.builder()
                        .scheduleEntry(scheduleEntry)
                        .actualPlayoutCost(BigDecimal.ZERO)
                        .build());
            } else if (isWithinSevenDays(lastFullCostPlayoutTime, scheduleEntry) && !isDiscountRepetitionApplied) {
                isDiscountRepetitionApplied = true;
                result.add(ScheduleEntryPlayoutCost.builder()
                        .scheduleEntry(scheduleEntry)
                        .actualPlayoutCost(BigDecimal.valueOf(programme.getPlayoutCost()).divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP))
                        .build());
            } else {
                result.add(ScheduleEntryPlayoutCost.builder()
                        .scheduleEntry(scheduleEntry)
                        .actualPlayoutCost(BigDecimal.valueOf(programme.getPlayoutCost()))
                        .build());
                isFreeRepetitionApplied = false;
                isDiscountRepetitionApplied = false;
                lastFullCostPlayoutTime = scheduleEntry.getStartDateTime();
            }
        }

        return result;
    }

    private boolean isWithinThreeDays(final LocalDateTime lastFullCostPlayoutTime, final ScheduleEntryDto scheduleEntry) {
        return lastFullCostPlayoutTime.plusDays(3).isAfter(scheduleEntry.getStartDateTime());
    }

    private boolean isWithinSevenDays(final LocalDateTime lastFullCostPlayoutTime, final ScheduleEntryDto scheduleEntry) {
        return lastFullCostPlayoutTime.plusDays(7).isAfter(scheduleEntry.getStartDateTime());
    }

    private boolean isBetweenDates(final ScheduleEntryPlayoutCost entry, final LocalDate startDate, final LocalDate endDate) {
        return !entry.getScheduleEntry().getStartDateTime().toLocalDate().isAfter(endDate) &&
                !entry.getScheduleEntry().getStartDateTime().toLocalDate().isBefore(startDate);
    }

    private List<ScheduleEntryPlayoutCost> getFullSchedulePlayoutCost() {
        List<ScheduleEntryDto> fullSchedule = scheduleManagementService.retrieveScheduleEntries().stream()
                .sorted(Comparator.comparing(ScheduleEntryDto::getStartDateTime))
                .collect(Collectors.toList());
        return getFullSchedulePlayoutCost(fullSchedule);
    }

    private ProgrammeDto getProgramme(final String programmeId) {
        return scheduleManagementService
                .retrieveProgrammeById(programmeId)
                .orElseThrow(() -> new ProgrammeNotFoundException("Programme not found: " + programmeId));
    }

}
