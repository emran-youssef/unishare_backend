package com.unishare.unishare.services;

import com.unishare.unishare.dtos.meetUpLocation.MeetUpLocationDto;
import com.unishare.unishare.mappers.MeetUpLocationMapper;
import com.unishare.unishare.repositories.MeetupLocationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class MeetUpLocationService {

    private final MeetupLocationRepository meetupLocationRepository;
    private final MeetUpLocationMapper meetUpLocationMapper;

    public List<MeetUpLocationDto> getAllActiveLocations(){
        return meetupLocationRepository.findByIsActiveTrue()
                .stream()
                .map(meetUpLocationMapper::toDto)
                .toList();
    }
}
