package com.unishare.unishare.repositories;

import com.unishare.unishare.entities.MeetUpLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MeetupLocationRepository extends JpaRepository<MeetUpLocation,Long> {

    List<MeetUpLocation> findByIsActiveTrue();
}
