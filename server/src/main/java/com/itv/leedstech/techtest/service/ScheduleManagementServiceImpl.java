package com.itv.leedstech.techtest.service;

import com.itv.leedstech.techtest.dto.ProgrammeDto;
import com.itv.leedstech.techtest.dto.ScheduleEntryDto;
import com.itv.leedstech.techtest.exception.InvalidScheduleEntryException;
import com.itv.leedstech.techtest.exception.ProgrammeNotFoundException;
import com.itv.leedstech.techtest.repository.ProgrammeEntity;
import com.itv.leedstech.techtest.repository.ProgrammeRepository;
import com.itv.leedstech.techtest.repository.ScheduleEntryEntity;
import com.itv.leedstech.techtest.repository.ScheduleEntryRepository;
import com.itv.leedstech.techtest.validation.ScheduleEntryValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleManagementServiceImpl implements ScheduleManagementService {

    private final ProgrammeRepository programmeRepository;
    private final ScheduleEntryRepository scheduleEntryRepository;
    private final ScheduleEntryValidator scheduleEntryValidator = new ScheduleEntryValidator();

    @Override
    public void storeProgramme(final ProgrammeDto programmeDto) {
        programmeRepository.save(programmeEntityFromDto(programmeDto));
    }

    @Override
    public Collection<ProgrammeDto> retrieveProgrammes() {
        return programmeRepository.findAll().stream()
                .map(this::programmeDtoFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProgrammeDto> retrieveProgrammeById(final String programmeId) {
        return programmeRepository.findById(programmeId)
                .map(this::programmeDtoFromEntity);
    }
    @Override
    public void storeScheduleEntry(ScheduleEntryDto scheduleEntryDto) {
        if (scheduleEntryValidator.isValidScheduleEntry(scheduleEntryDto)) {
            scheduleEntryRepository.save(scheduleEntryEntityFromDto(scheduleEntryDto));
        } else {
            throw new InvalidScheduleEntryException("The given schedule entry is not valid: " + scheduleEntryDto);
        }
    }

    @Override
    public Collection<ScheduleEntryDto> retrieveScheduleEntries() {
        return scheduleEntryRepository.findAll().stream()
                .map(this::scheduleEntryDtoFromEntity)
                .collect(Collectors.toList());
    }

    private ProgrammeEntity programmeEntityFromDto(final ProgrammeDto programmeDto) {
        return ProgrammeEntity.builder()
                .id(programmeDto.getId())
                .title(programmeDto.getTitle())
                .playoutCost(programmeDto.getPlayoutCost())
                .isDiscountable(programmeDto.getIsDiscountable())
                .build();
    }

    private ProgrammeDto programmeDtoFromEntity(final ProgrammeEntity programmeEntity) {
        return ProgrammeDto.builder()
                .id(programmeEntity.getId())
                .title(programmeEntity.getTitle())
                .playoutCost(programmeEntity.getPlayoutCost())
                .isDiscountable(programmeEntity.getIsDiscountable())
                .build();
    }

    private ScheduleEntryEntity scheduleEntryEntityFromDto(final ScheduleEntryDto scheduleEntryDto) {
        ProgrammeEntity programme = programmeRepository.findById(scheduleEntryDto.getProgramId())
                .orElseThrow(() -> new ProgrammeNotFoundException("Programme not found: " + scheduleEntryDto.getProgramId()));

        return ScheduleEntryEntity.builder()
                .programme(programme)
                .startDateTime(scheduleEntryDto.getStartDateTime())
                .endDateTime(scheduleEntryDto.getEndDateTime())
                .build();
    }

    private ScheduleEntryDto scheduleEntryDtoFromEntity(final ScheduleEntryEntity scheduleEntryEntity) {
        return ScheduleEntryDto.builder()
                .programId(scheduleEntryEntity.getProgramme().getId())
                .startDateTime(scheduleEntryEntity.getStartDateTime())
                .endDateTime(scheduleEntryEntity.getEndDateTime())
                .build();
    }
}
