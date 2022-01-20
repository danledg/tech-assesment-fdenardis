package com.itv.leedstech.techtest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgrammeRepository extends JpaRepository<ProgrammeEntity, String> {
}
