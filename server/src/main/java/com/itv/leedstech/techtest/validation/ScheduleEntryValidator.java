package com.itv.leedstech.techtest.validation;

import com.itv.leedstech.techtest.dto.ScheduleEntryDto;

import static java.time.temporal.ChronoUnit.MINUTES;

public class ScheduleEntryValidator {
    public boolean isValidScheduleEntry(final ScheduleEntryDto scheduleEntryDto) {
        return isMultipleOf15(scheduleEntryDto.getStartDateTime().getMinute())
                && scheduleEntryDto.getEndDateTime().isAfter(scheduleEntryDto.getStartDateTime())
                && isMultipleOf30(getDurationInMinutes(scheduleEntryDto));
    }

    private boolean isMultipleOf15(final int value) {
        return value % 15 == 0;
    }

    private long getDurationInMinutes(final ScheduleEntryDto scheduleEntryDto) {
        return MINUTES.between(scheduleEntryDto.getStartDateTime(), scheduleEntryDto.getEndDateTime());
    }

    private boolean isMultipleOf30(final long value) {
        return value % 30 == 0;
    }

}

