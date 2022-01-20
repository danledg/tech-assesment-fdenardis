package com.itv.leedstech.techtest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@ToString
public class ProgrammeDto {
    @NotNull
    @NotEmpty
    private final String id;

    @NotNull
    @NotEmpty
    private final String title;

    @NotNull
    @Min(value = 0)
    private final Integer playoutCost;

    @NotNull
    private final Boolean isDiscountable;
}
