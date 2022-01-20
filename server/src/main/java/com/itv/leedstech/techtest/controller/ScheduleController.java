package com.itv.leedstech.techtest.controller;

import com.itv.leedstech.techtest.dto.ScheduleEntryDto;
import com.itv.leedstech.techtest.service.ScheduleManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("schedule")
@RequiredArgsConstructor
@Slf4j
public class ScheduleController {

    private final ScheduleManagementService scheduleManagementService;

    @PostMapping
    public void addScheduleEntry(@RequestBody @Validated ScheduleEntryDto scheduleEntryDto) {
        log.info("schedule controller accepting POST request for {}", scheduleEntryDto);
        scheduleManagementService.storeScheduleEntry(scheduleEntryDto);
    }

    @GetMapping
    public Collection<ScheduleEntryDto> retrieveScheduleEntries() {
        return scheduleManagementService.retrieveScheduleEntries();
    }
}
