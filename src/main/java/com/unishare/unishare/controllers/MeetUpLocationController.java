package com.unishare.unishare.controllers;

import com.unishare.unishare.dtos.meetUpLocation.MeetUpLocationDto;
import com.unishare.unishare.services.MeetUpLocationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/meetup-locations")
public class MeetUpLocationController {

    private final MeetUpLocationService meetUpLocationService;

    @GetMapping
    ResponseEntity<List<MeetUpLocationDto>> getAllLocations(){
        return ResponseEntity.ok(meetUpLocationService.getAllActiveLocations());

    }
}
