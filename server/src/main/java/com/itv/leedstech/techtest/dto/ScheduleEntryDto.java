package com.itv.leedstech.techtest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@Getter
@ToString
@Value
public class ScheduleEntryDto {
    @NotNull
    private LocalDateTime startDateTime;
    @NotNull
    private LocalDateTime endDateTime;
    @NotNull
    private String programId;
}
