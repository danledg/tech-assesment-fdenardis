package com.itv.leedstech.techtest.controller;

import com.itv.leedstech.techtest.service.CostCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@RestController
@RequestMapping("playoutcost")
@RequiredArgsConstructor
@Slf4j
public class CostingController {

    private final CostCalculator costCalculator;

    @GetMapping(path = "/schedule")
    public BigDecimal getTotalScheduleCost(@RequestParam(required = false)
                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                           @RequestParam(required = false)
                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        if (Objects.nonNull(from) && Objects.nonNull(to)) {
            return costCalculator.getFullScheduleTotalPlayoutCostBetweenDates(from, to);
        }
        return costCalculator.getFullScheduleTotalPlayoutCost();
    }

    @GetMapping(path = "/programme/{id}")
    public BigDecimal getTotalProgrammeCost(@PathVariable final String id,
                                            @RequestParam(required = false)
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                            @RequestParam(required = false)
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        if (Objects.nonNull(from) && Objects.nonNull(to)) {
            return costCalculator.getProgrammeTotalPlayoutCostBetweenDates(id, from, to);
        }
        return costCalculator.getProgrammeTotalPlayoutCost(id);
    }

}
