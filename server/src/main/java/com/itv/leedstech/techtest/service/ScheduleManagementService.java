package com.itv.leedstech.techtest.service;

import com.itv.leedstech.techtest.dto.ProgrammeDto;
import com.itv.leedstech.techtest.dto.ScheduleEntryDto;

import java.util.Collection;
import java.util.Optional;

public interface ScheduleManagementService {
    void storeProgramme(ProgrammeDto programmeDto);
    Collection<ProgrammeDto> retrieveProgrammes();
    Optional<ProgrammeDto> retrieveProgrammeById(String programmeId);

    void storeScheduleEntry(ScheduleEntryDto scheduleEntryDto);
    Collection<ScheduleEntryDto> retrieveScheduleEntries();
}
