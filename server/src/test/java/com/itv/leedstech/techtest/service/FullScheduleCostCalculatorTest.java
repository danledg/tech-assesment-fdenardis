package com.itv.leedstech.techtest.service;

import com.itv.leedstech.techtest.dto.ProgrammeDto;
import com.itv.leedstech.techtest.dto.ScheduleEntryDto;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.MockitoAnnotations.initMocks;

public class FullScheduleCostCalculatorTest {

    public static final String PROGRAM_ID = "PGM000";
    public static final String PROGRAM_ID_2 = "XXX999";

    private CostCalculator costCalculator;

    @Mock
    ScheduleManagementService scheduleManagementService;

    @Before
    public void init() {
        initMocks(this);
        costCalculator = new CostCalculatorImpl(scheduleManagementService);
    }

    @Test
    public void shouldCalculateTotalCostAsZeroWhenScheduleIsEmpty() {
        when(scheduleManagementService.retrieveScheduleEntries()).thenReturn(Collections.emptyList());
        BigDecimal totalCost = costCalculator.getFullScheduleTotalPlayoutCost();
        assertThat(totalCost).isEqualTo("0");
    }

    @Test
    public void shouldCalculateTotalCostOfSchedule() {
        List<ScheduleEntryDto> aSchedule = Arrays.asList(
                ScheduleEntryDto.builder()
                        .programId(PROGRAM_ID)
                        .startDateTime(aDateTimeInFuture())
                        .endDateTime(aDateTimeInFuture().plusMinutes(60))
                        .build(),
                ScheduleEntryDto.builder()
                        .programId(PROGRAM_ID)
                        .startDateTime(aDateTimeInFuture().plusDays(1))
                        .endDateTime(aDateTimeInFuture().plusDays(1).plusMinutes(60))
                        .build(),
                ScheduleEntryDto.builder()
                        .programId(PROGRAM_ID)
                        .startDateTime(aDateTimeInFuture().plusDays(5))
                        .endDateTime(aDateTimeInFuture().plusDays(5).plusMinutes(60))
                        .build()
        );        
        when(scheduleManagementService.retrieveScheduleEntries()).thenReturn(aSchedule);
        when(scheduleManagementService.retrieveProgrammeById(PROGRAM_ID)).thenReturn(Optional.of(aProgramme()));
        BigDecimal totalCost = costCalculator.getFullScheduleTotalPlayoutCost();
        // cost 2000 one free rep one half price
        assertThat(totalCost).isEqualTo("3000");
    }

    @Test
    public void shouldCalculateCostOfScheduleBetweenDates() {
        List<ScheduleEntryDto> aSchedule = Arrays.asList(
                ScheduleEntryDto.builder()
                        .programId(PROGRAM_ID)
                        .startDateTime(aDateTimeInFuture())
                        .endDateTime(aDateTimeInFuture().plusMinutes(60))
                        .build(),
                ScheduleEntryDto.builder()
                        .programId(PROGRAM_ID)
                        .startDateTime(aDateTimeInFuture().plusDays(1))
                        .endDateTime(aDateTimeInFuture().plusDays(1).plusMinutes(60))
                        .build(),
                ScheduleEntryDto.builder()
                        .programId(PROGRAM_ID)
                        .startDateTime(aDateTimeInFuture().plusDays(5))
                        .endDateTime(aDateTimeInFuture().plusDays(5).plusMinutes(60))
                        .build()
        );
        when(scheduleManagementService.retrieveScheduleEntries()).thenReturn(aSchedule);
        when(scheduleManagementService.retrieveProgrammeById(PROGRAM_ID)).thenReturn(Optional.of(aProgramme()));
        BigDecimal totalCost =
                costCalculator.getFullScheduleTotalPlayoutCostBetweenDates(aDateTimeInFuture().toLocalDate().plusDays(1),
                        aDateTimeInFuture().toLocalDate().plusDays(10));
        // one free rep one half price (full cost playout is outside date interval)
        assertThat(totalCost).isEqualTo("1000");
    }


    @Test
    public void shouldCalculateTotalCostOfProgramme() {
        List<ScheduleEntryDto> aSchedule = Arrays.asList(
                ScheduleEntryDto.builder()
                        .programId(PROGRAM_ID)
                        .startDateTime(aDateTimeInFuture())
                        .endDateTime(aDateTimeInFuture().plusMinutes(60))
                        .build(),
                ScheduleEntryDto.builder()
                        .programId(PROGRAM_ID_2)
                        .startDateTime(aDateTimeInFuture().plusDays(1))
                        .endDateTime(aDateTimeInFuture().plusDays(1).plusMinutes(60))
                        .build(),
                ScheduleEntryDto.builder()
                        .programId(PROGRAM_ID)
                        .startDateTime(aDateTimeInFuture().plusDays(5))
                        .endDateTime(aDateTimeInFuture().plusDays(5).plusMinutes(60))
                        .build()
        );
        when(scheduleManagementService.retrieveScheduleEntries()).thenReturn(aSchedule);

        Answer<Optional<ProgrammeDto>> programmeDtoAnswer = new Answer<Optional<ProgrammeDto>>() {
            @Override
            public Optional<ProgrammeDto> answer(InvocationOnMock invocationOnMock) throws Throwable {
                return Optional.of(aProgramme(invocationOnMock.getArgument(0, String.class)));
            }
        };
        doAnswer(programmeDtoAnswer).when(scheduleManagementService).retrieveProgrammeById(ArgumentMatchers.anyString());

        BigDecimal totalCost = costCalculator.getProgrammeTotalPlayoutCost(PROGRAM_ID_2);
        assertThat(totalCost).isEqualTo("2000");
        totalCost = costCalculator.getProgrammeTotalPlayoutCost(PROGRAM_ID);
        assertThat(totalCost).isEqualTo("3000");
    }

    @Test
    public void shouldCalculateTotalCostOfProgrammeBetweenDates() {
        List<ScheduleEntryDto> aSchedule = Arrays.asList(
                ScheduleEntryDto.builder()
                        .programId(PROGRAM_ID)
                        .startDateTime(aDateTimeInFuture())
                        .endDateTime(aDateTimeInFuture().plusMinutes(60))
                        .build(),
                ScheduleEntryDto.builder()
                        .programId(PROGRAM_ID_2)
                        .startDateTime(aDateTimeInFuture().plusDays(1))
                        .endDateTime(aDateTimeInFuture().plusDays(1).plusMinutes(60))
                        .build(),
                ScheduleEntryDto.builder()
                        .programId(PROGRAM_ID)
                        .startDateTime(aDateTimeInFuture().plusDays(5))
                        .endDateTime(aDateTimeInFuture().plusDays(5).plusMinutes(60))
                        .build()
        );
        when(scheduleManagementService.retrieveScheduleEntries()).thenReturn(aSchedule);

        Answer<Optional<ProgrammeDto>> programmeDtoAnswer = new Answer<Optional<ProgrammeDto>>() {
            @Override
            public Optional<ProgrammeDto> answer(InvocationOnMock invocationOnMock) throws Throwable {
                return Optional.of(aProgramme(invocationOnMock.getArgument(0, String.class)));
            }
        };
        doAnswer(programmeDtoAnswer).when(scheduleManagementService).retrieveProgrammeById(ArgumentMatchers.anyString());

        BigDecimal totalCost = costCalculator.getProgrammeTotalPlayoutCostBetweenDates(PROGRAM_ID, aDateTimeInFuture().toLocalDate().plusDays(1),
                aDateTimeInFuture().toLocalDate().plusDays(10));
        assertThat(totalCost).isEqualTo("1000");
    }
    @Test
    public void shouldReturnAnEmptyListWithEmptySchedule() {
        List<ScheduleEntryPlayoutCost> calculatedCosts = costCalculator.getFullSchedulePlayoutCost(Collections.emptyList());
        assertThat(calculatedCosts).isNotNull();
        assertThat(calculatedCosts).isEmpty();
    }

    @Test
    public void shouldReturnTheCostOfASingleScheduledEntry() {
        List<ScheduleEntryDto> aSchedule = Arrays.asList(
                ScheduleEntryDto.builder()
                .programId(PROGRAM_ID)
                .startDateTime(aDateTimeInFuture())
                .endDateTime(aDateTimeInFuture().plusMinutes(60))
                .build()
        );

        when(scheduleManagementService.retrieveProgrammeById(PROGRAM_ID)).thenReturn(Optional.of(aProgramme()));

        List<ScheduleEntryPlayoutCost> calculatedCosts = costCalculator.getFullSchedulePlayoutCost(aSchedule);

        assertThat(calculatedCosts).hasSize(1);
        assertThat(calculatedCosts.get(0).getActualPlayoutCost()).isEqualTo("2000");

    }

    @Test
    public void shouldReturnAsManyEntriesAsThereAreInTheSchedule() {
        List<ScheduleEntryDto> aSchedule = Arrays.asList(
                ScheduleEntryDto.builder()
                        .programId(PROGRAM_ID)
                        .startDateTime(aDateTimeInFuture())
                        .endDateTime(aDateTimeInFuture().plusMinutes(60))
                        .build(),
                ScheduleEntryDto.builder()
                        .programId(PROGRAM_ID)
                        .startDateTime(aDateTimeInFuture())
                        .endDateTime(aDateTimeInFuture().plusMinutes(60))
                        .build(),
                ScheduleEntryDto.builder()
                        .programId(PROGRAM_ID)
                        .startDateTime(aDateTimeInFuture())
                        .endDateTime(aDateTimeInFuture().plusMinutes(60))
                        .build()
        );
        when(scheduleManagementService.retrieveProgrammeById(PROGRAM_ID)).thenReturn(Optional.of(aProgramme()));
        List<ScheduleEntryPlayoutCost> calculatedCosts = costCalculator.getFullSchedulePlayoutCost(aSchedule);

        assertThat(calculatedCosts).hasSize(3);
    }

    @Test
    public void shouldCalculateCostsForMultiplePrograms() {
        List<ScheduleEntryDto> aSchedule = Arrays.asList(
                ScheduleEntryDto.builder()
                        .programId(PROGRAM_ID)
                        .startDateTime(aDateTimeInFuture())
                        .endDateTime(aDateTimeInFuture().plusMinutes(60))
                        .build(),
                ScheduleEntryDto.builder()
                        .programId(PROGRAM_ID_2)
                        .startDateTime(aDateTimeInFuture())
                        .endDateTime(aDateTimeInFuture().plusMinutes(60))
                        .build(),
                ScheduleEntryDto.builder()
                        .programId(PROGRAM_ID)
                        .startDateTime(aDateTimeInFuture())
                        .endDateTime(aDateTimeInFuture().plusMinutes(60))
                        .build()
        );

        Answer<Optional<ProgrammeDto>> programmeDtoAnswer = new Answer<Optional<ProgrammeDto>>() {
            @Override
            public Optional<ProgrammeDto> answer(InvocationOnMock invocationOnMock) throws Throwable {
                return Optional.of(aProgramme(invocationOnMock.getArgument(0, String.class)));
            }
        };
        doAnswer(programmeDtoAnswer).when(scheduleManagementService).retrieveProgrammeById(ArgumentMatchers.anyString());

        List<ScheduleEntryPlayoutCost> calculatedCosts = costCalculator.getFullSchedulePlayoutCost(aSchedule);

        assertThat(calculatedCosts).hasSize(3);
        assertThat(calculatedCosts.stream().filter(t -> t.getScheduleEntry().getProgramId().equals(PROGRAM_ID)).findFirst()).isNotEmpty();
        assertThat(calculatedCosts.stream().filter(t -> t.getScheduleEntry().getProgramId().equals(PROGRAM_ID_2)).findFirst()).isNotEmpty();
    }

    LocalDateTime aDateTimeInFuture() {
        return LocalDateTime.of(LocalDate.now().plusMonths(1), LocalTime.of(12, 0));
    }

    ProgrammeDto aProgramme() {
        return ProgrammeDto.builder()
                .id(PROGRAM_ID)
                .title("I am a programme")
                .playoutCost(2000)
                .isDiscountable(true)
                .build();
    }
    ProgrammeDto aProgramme(String id) {
        return ProgrammeDto.builder()
                .id(id)
                .title("I am a programme")
                .playoutCost(2000)
                .isDiscountable(true)
                .build();
    }
}
