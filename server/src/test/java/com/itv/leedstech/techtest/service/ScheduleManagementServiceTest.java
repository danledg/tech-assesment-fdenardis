package com.itv.leedstech.techtest.service;

import com.itv.leedstech.techtest.dto.ProgrammeDto;
import com.itv.leedstech.techtest.dto.ScheduleEntryDto;
import com.itv.leedstech.techtest.exception.ProgrammeNotFoundException;
import com.itv.leedstech.techtest.repository.ProgrammeEntity;
import com.itv.leedstech.techtest.repository.ProgrammeRepository;
import com.itv.leedstech.techtest.repository.ScheduleEntryEntity;
import com.itv.leedstech.techtest.repository.ScheduleEntryRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(JUnit4.class)
public class ScheduleManagementServiceTest {

    public static final String ID = "ID001";
    private static final ProgrammeDto aProgramme = ProgrammeDto.builder()
            .id(ID)
            .title("La notte dei lunghi coltelli")
            .playoutCost(1000)
            .isDiscountable(true)
            .build();
    private static final ScheduleEntryDto aScheduleEntry = ScheduleEntryDto.builder()
            .programId(ID)
            .startDateTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(21,15)))
            .endDateTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(22,45)))
            .build();

    private ScheduleManagementService scheduleManagementService;

    @Mock
    public ProgrammeRepository programmeRepository;

    @Mock
    public ScheduleEntryRepository scheduleEntryRepository;

    @Before
    public void setUp() {
        initMocks(this);
        scheduleManagementService = new ScheduleManagementServiceImpl(programmeRepository, scheduleEntryRepository);
    }

    @Test
    public void shouldSaveProgrammeToRepository() {
        scheduleManagementService.storeProgramme(aProgramme);

        verify(programmeRepository, times(1)).save(ArgumentMatchers.any());
    }

    @Test
    public void shouldMapAllProgrammeFieldsFromDtoToEntity() {
        scheduleManagementService.storeProgramme(aProgramme);

        ArgumentCaptor<ProgrammeEntity> programmeEntityCaptor = ArgumentCaptor.forClass(ProgrammeEntity.class);
        verify(programmeRepository).save(programmeEntityCaptor.capture());
        ProgrammeEntity savedEntity = programmeEntityCaptor.getValue();
        Assertions.assertThat(savedEntity).hasFieldOrPropertyWithValue("id", aProgramme.getId());
        Assertions.assertThat(savedEntity).hasFieldOrPropertyWithValue("title", aProgramme.getTitle());
        Assertions.assertThat(savedEntity).hasFieldOrPropertyWithValue("playoutCost", aProgramme.getPlayoutCost());
        Assertions.assertThat(savedEntity).hasFieldOrPropertyWithValue("isDiscountable", aProgramme.getIsDiscountable());
    }

    @Test
    public void shouldSaveScheduleEntityToRepository() {
        when(programmeRepository.findById(ID))
                .thenReturn(Optional.of(ProgrammeEntity.builder().id(ID).build()));

        scheduleManagementService.storeScheduleEntry(aScheduleEntry);

        verify(scheduleEntryRepository, times(1)).save(ArgumentMatchers.any());
    }

    @Test
    public void shouldThrowExceptionWhenReferencingNonExistingProgramme() {
        when(programmeRepository.findById(ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> scheduleManagementService.storeScheduleEntry(aScheduleEntry))
                .isInstanceOf(ProgrammeNotFoundException.class);
    }


    @Test
    public void shouldMapAllScheduleEntryFieldsFromDtoToEntity() {
        when(programmeRepository.findById(ID))
                .thenReturn(Optional.of(ProgrammeEntity.builder().id(ID).build()));

        scheduleManagementService.storeScheduleEntry(aScheduleEntry);

        ArgumentCaptor<ScheduleEntryEntity> scheduleEntryEntityCaptor = ArgumentCaptor.forClass(ScheduleEntryEntity.class);
        verify(scheduleEntryRepository).save(scheduleEntryEntityCaptor.capture());
        ScheduleEntryEntity savedEntity = scheduleEntryEntityCaptor.getValue();
        Assertions.assertThat(savedEntity.getProgramme()).hasFieldOrPropertyWithValue("id", aScheduleEntry.getProgramId());
        Assertions.assertThat(savedEntity).hasFieldOrPropertyWithValue("startDateTime", aScheduleEntry.getStartDateTime());
        Assertions.assertThat(savedEntity).hasFieldOrPropertyWithValue("endDateTime", aScheduleEntry.getEndDateTime());
    }

}
