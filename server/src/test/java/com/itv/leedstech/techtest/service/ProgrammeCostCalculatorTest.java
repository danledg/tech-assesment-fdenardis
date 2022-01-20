package com.itv.leedstech.techtest.service;

import com.itv.leedstech.techtest.dto.ProgrammeDto;
import com.itv.leedstech.techtest.dto.ScheduleEntryDto;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

public class ProgrammeCostCalculatorTest {

    public static final String ID_NON_DISC_PRG = "NOND001";
    public static final String ID_DISC_PRG = "DISC001";
    private CostCalculator costCalculator;

    @Mock
    ScheduleManagementService scheduleManagementService;

    @Before
    public void init() {
        initMocks(this);
        costCalculator = new CostCalculatorImpl(scheduleManagementService);
    }

    @Test
    public void shouldReturnAnEmptyListWithEmptySchedule() {
        List<ScheduleEntryPlayoutCost> calculatedCosts = costCalculator.getProgrammePlayoutCost(aDiscountableProgramme(), Collections.emptyList());
        assertThat(calculatedCosts).isNotNull();
        assertThat(calculatedCosts).isEmpty();
    }

    @Test
    public void shouldCalculateFullCostForSingleSchedule() {
        List<ScheduleEntryDto> programmeSchedule = Arrays.asList(
                ScheduleEntryDto.builder()
                        .programId(ID_DISC_PRG)
                        .startDateTime(aDateTimeInFuture())
                        .endDateTime(aDateTimeInFuture().plusMinutes(75))
                        .build()
        );

        List<ScheduleEntryPlayoutCost> calculatedCosts = costCalculator.getProgrammePlayoutCost(aDiscountableProgramme(), programmeSchedule);
        assertThat(calculatedCosts).hasSize(1);
        assertThat(calculatedCosts.get(0).getActualPlayoutCost()).isEqualTo("1000");
    }

    @Test
    public void firstRepetitionFreeWithinThreeDays() {
        List<ScheduleEntryDto> programmeSchedule = Arrays.asList(
                ScheduleEntryDto.builder()
                        .programId(ID_DISC_PRG)
                        .startDateTime(aDateTimeInFuture())
                        .endDateTime(aDateTimeInFuture().plusMinutes(75))
                        .build(),
                ScheduleEntryDto.builder()
                        .programId(ID_DISC_PRG)
                        .startDateTime(aDateTimeInFuture().plusDays(2))
                        .endDateTime(aDateTimeInFuture().plusDays(2).plusMinutes(75))
                        .build()
        );

        List<ScheduleEntryPlayoutCost> calculatedCosts = costCalculator.getProgrammePlayoutCost(aDiscountableProgramme(), programmeSchedule);
        assertThat(calculatedCosts).hasSize(2);
        assertThat(calculatedCosts.get(0).getActualPlayoutCost()).isEqualTo("1000");
        assertThat(calculatedCosts.get(1).getActualPlayoutCost()).isEqualTo("0");
    }

    @Test
    public void secondRepetitionWithinThreeDaysWillBeHalfPrice() {
        List<ScheduleEntryDto> programmeSchedule = Arrays.asList(
                ScheduleEntryDto.builder()
                        .programId(ID_DISC_PRG)
                        .startDateTime(aDateTimeInFuture())
                        .endDateTime(aDateTimeInFuture().plusMinutes(75))
                        .build(),
                ScheduleEntryDto.builder()
                        .programId(ID_DISC_PRG)
                        .startDateTime(aDateTimeInFuture().plusDays(1))
                        .endDateTime(aDateTimeInFuture().plusDays(1).plusMinutes(75))
                        .build(),
                ScheduleEntryDto.builder()
                        .programId(ID_DISC_PRG)
                        .startDateTime(aDateTimeInFuture().plusDays(2))
                        .endDateTime(aDateTimeInFuture().plusDays(2).plusMinutes(75))
                        .build()
        );

        List<ScheduleEntryPlayoutCost> calculatedCosts = costCalculator.getProgrammePlayoutCost(aDiscountableProgramme(), programmeSchedule);
        assertThat(calculatedCosts).hasSize(3);
        assertThat(calculatedCosts.get(0).getActualPlayoutCost()).isEqualTo("1000");
        assertThat(calculatedCosts.get(1).getActualPlayoutCost()).isEqualTo("0");
        assertThat(calculatedCosts.get(2).getActualPlayoutCost()).isEqualTo("500");
    }

    @Test
    public void thirdRepetitionWithinThreeDaysWillBeFullPrice() {
        List<ScheduleEntryDto> programmeSchedule = Arrays.asList(
                ScheduleEntryDto.builder()
                        .programId(ID_DISC_PRG)
                        .startDateTime(aDateTimeInFuture())
                        .endDateTime(aDateTimeInFuture().plusMinutes(75))
                        .build(),
                ScheduleEntryDto.builder()
                        .programId(ID_DISC_PRG)
                        .startDateTime(aDateTimeInFuture().plusHours(8))
                        .endDateTime(aDateTimeInFuture().plusHours(8).plusMinutes(75))
                        .build(),
                ScheduleEntryDto.builder()
                        .programId(ID_DISC_PRG)
                        .startDateTime(aDateTimeInFuture().plusDays(1))
                        .endDateTime(aDateTimeInFuture().plusDays(1).plusMinutes(75))
                        .build(),
                ScheduleEntryDto.builder()
                        .programId(ID_DISC_PRG)
                        .startDateTime(aDateTimeInFuture().plusDays(2))
                        .endDateTime(aDateTimeInFuture().plusDays(2).plusMinutes(75))
                        .build()
        );

        List<ScheduleEntryPlayoutCost> calculatedCosts = costCalculator.getProgrammePlayoutCost(aDiscountableProgramme(), programmeSchedule);
        assertThat(calculatedCosts).hasSize(4);
        assertThat(calculatedCosts.get(0).getActualPlayoutCost()).isEqualTo("1000");
        assertThat(calculatedCosts.get(1).getActualPlayoutCost()).isEqualTo("0");
        assertThat(calculatedCosts.get(2).getActualPlayoutCost()).isEqualTo("500");
        assertThat(calculatedCosts.get(3).getActualPlayoutCost()).isEqualTo("1000");
    }

    @Test
    public void firstRepetitionWithinSevenDaysButAfterThreeDaysWillBeHalfPrice() {
        List<ScheduleEntryDto> programmeSchedule = Arrays.asList(
                ScheduleEntryDto.builder()
                        .programId(ID_DISC_PRG)
                        .startDateTime(aDateTimeInFuture())
                        .endDateTime(aDateTimeInFuture().plusMinutes(75))
                        .build(),
                ScheduleEntryDto.builder()
                        .programId(ID_DISC_PRG)
                        .startDateTime(aDateTimeInFuture().plusDays(4))
                        .endDateTime(aDateTimeInFuture().plusDays(4).plusMinutes(75))
                        .build()
        );

        List<ScheduleEntryPlayoutCost> calculatedCosts = costCalculator.getProgrammePlayoutCost(aDiscountableProgramme(), programmeSchedule);
        assertThat(calculatedCosts).hasSize(2);
        assertThat(calculatedCosts.get(0).getActualPlayoutCost()).isEqualTo("1000");
        assertThat(calculatedCosts.get(1).getActualPlayoutCost()).isEqualTo("500");
    }


    @Test
    public void allRepetitionsWillBeFullPriceIfNotDiscountable() {
        List<ScheduleEntryDto> programmeSchedule = Arrays.asList(
                ScheduleEntryDto.builder()
                        .programId(ID_NON_DISC_PRG)
                        .startDateTime(aDateTimeInFuture())
                        .endDateTime(aDateTimeInFuture().plusMinutes(75))
                        .build(),
                ScheduleEntryDto.builder()
                        .programId(ID_NON_DISC_PRG)
                        .startDateTime(aDateTimeInFuture().plusHours(8))
                        .endDateTime(aDateTimeInFuture().plusHours(8).plusMinutes(75))
                        .build(),
                ScheduleEntryDto.builder()
                        .programId(ID_NON_DISC_PRG)
                        .startDateTime(aDateTimeInFuture().plusDays(1))
                        .endDateTime(aDateTimeInFuture().plusDays(1).plusMinutes(75))
                        .build(),
                ScheduleEntryDto.builder()
                        .programId(ID_NON_DISC_PRG)
                        .startDateTime(aDateTimeInFuture().plusDays(2))
                        .endDateTime(aDateTimeInFuture().plusDays(2).plusMinutes(75))
                        .build()
        );

        List<ScheduleEntryPlayoutCost> calculatedCosts = costCalculator.getProgrammePlayoutCost(aNonDiscountableProgramme(), programmeSchedule);
        assertThat(calculatedCosts).hasSize(4);
        assertThat(calculatedCosts.get(0).getActualPlayoutCost()).isEqualTo("1000");
        assertThat(calculatedCosts.get(1).getActualPlayoutCost()).isEqualTo("1000");
        assertThat(calculatedCosts.get(2).getActualPlayoutCost()).isEqualTo("1000");
        assertThat(calculatedCosts.get(3).getActualPlayoutCost()).isEqualTo("1000");
    }

    LocalDateTime aDateTimeInFuture() {
        return LocalDateTime.of(LocalDate.now().plusMonths(1), LocalTime.of(12, 0));
    }

    ProgrammeDto aNonDiscountableProgramme() {
        return ProgrammeDto.builder()
                .id(ID_NON_DISC_PRG)
                .title("I am not discountable")
                .playoutCost(1000)
                .isDiscountable(false)
                .build();
    }

    ProgrammeDto aDiscountableProgramme() {
        return ProgrammeDto.builder()
                .id(ID_DISC_PRG)
                .title("I am discountable")
                .playoutCost(1000)
                .isDiscountable(true)
                .build();
    }

}
