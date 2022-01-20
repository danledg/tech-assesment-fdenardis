package com.itv.leedstech.techtest.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ScheduleEntryEntity {
    @Id
    @GeneratedValue
    private long id;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    @ManyToOne
    @JoinColumn(name = "programme_id")
    private ProgrammeEntity programme;
}
