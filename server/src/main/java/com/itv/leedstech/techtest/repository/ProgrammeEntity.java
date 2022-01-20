package com.itv.leedstech.techtest.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ProgrammeEntity {
    @Id
    private String id;
    private String title;
    private Integer playoutCost;
    private Boolean isDiscountable;
}
