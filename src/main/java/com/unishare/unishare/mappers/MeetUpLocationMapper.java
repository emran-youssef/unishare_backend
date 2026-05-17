package com.unishare.unishare.mappers;

import com.unishare.unishare.dtos.meetUpLocation.MeetUpLocationDto;
import com.unishare.unishare.entities.MeetUpLocation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MeetUpLocationMapper {

    MeetUpLocationDto toDto(MeetUpLocation meetUpLocation);
}
