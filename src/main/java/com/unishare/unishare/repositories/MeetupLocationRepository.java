package com.unishare.unishare.repositories;

import com.unishare.unishare.entities.MeetUpLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetupLocationRepository extends JpaRepository<MeetUpLocation,Long> {

    List<MeetUpLocation> findByIsActiveTrue();
}
