package com.itv.leedstech.techtest.service;

import com.itv.leedstech.techtest.dto.ScheduleEntryDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.math.BigDecimal;

@Builder
@Value
@Getter
public class ScheduleEntryPlayoutCost {
    private ScheduleEntryDto scheduleEntry;
    private BigDecimal actualPlayoutCost;
}
