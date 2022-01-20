package com.itv.leedstech.techtest.validation;

import com.itv.leedstech.techtest.dto.ScheduleEntryDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class ScheduleEntryValidatorTest {

    private ScheduleEntryValidator scheduleEntryValidator = new ScheduleEntryValidator();


    @Test
    public void validationShouldPassIfStartAlignedAndSlotDurationIsMultipleOf30Minutes() {
        ScheduleEntryDto scheduleEntryDto = ScheduleEntryDto.builder()
                .programId("ID_123")
                .startDateTime(alignedDatetime())
                .endDateTime(alignedDatetime().plusMinutes(90))
                .build();

        assertThat(scheduleEntryValidator.isValidScheduleEntry(scheduleEntryDto)).isTrue();
    }

    @Test
    public void validationShouldFailIfStartDatetimeNotAligned() {
        ScheduleEntryDto scheduleEntryDto = ScheduleEntryDto.builder()
                .programId("ID_123")
                .startDateTime(notAlignedDatetime())
                .endDateTime(notAlignedDatetime().plusMinutes(90))
                .build();

        assertThat(scheduleEntryValidator.isValidScheduleEntry(scheduleEntryDto)).isFalse();
    }

    @Test
    public void validationShouldFailIfEndDateEarlierThanStartDate() {
        ScheduleEntryDto scheduleEntryDto = ScheduleEntryDto.builder()
                .programId("ID_123")
                .startDateTime(alignedDatetime())
                .endDateTime(alignedDatetime().minusMinutes(90))
                .build();

        assertThat(scheduleEntryValidator.isValidScheduleEntry(scheduleEntryDto)).isFalse();
    }

    @Test
    public void validationShouldFailIfDurationNotMultipleOf30() {
        ScheduleEntryDto scheduleEntryDto = ScheduleEntryDto.builder()
                .programId("ID_123")
                .startDateTime(alignedDatetime())
                .endDateTime(alignedDatetime().plusMinutes(93))
                .build();

        assertThat(scheduleEntryValidator.isValidScheduleEntry(scheduleEntryDto)).isFalse();
    }

    private LocalDateTime notAlignedDatetime() {
        return LocalDateTime.now().withHour(12).withMinute(26);
    }

    private LocalDateTime alignedDatetime() {
        return LocalDateTime.now().withHour(12).withMinute(15);
    }

}