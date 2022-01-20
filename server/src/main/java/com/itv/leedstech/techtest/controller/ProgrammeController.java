package com.itv.leedstech.techtest.controller;

import com.itv.leedstech.techtest.dto.ProgrammeDto;
import com.itv.leedstech.techtest.service.ScheduleManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("programme")
@RequiredArgsConstructor
@Slf4j
public class ProgrammeController {

    private final ScheduleManagementService scheduleManagementService;

    @PostMapping
    public void addProgramme(@RequestBody @Validated ProgrammeDto programmeDto) {
        log.info("programme controller accepting POST request for {}", programmeDto);
        scheduleManagementService.storeProgramme(programmeDto);
    }

    @GetMapping
    public Collection<ProgrammeDto> retrieveProgrammes() {
        return scheduleManagementService.retrieveProgrammes();
    }
}
