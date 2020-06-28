package com.application.community.controllers;

import com.application.community.models.ParkingReservation;
import com.application.community.response.ParkingResponse;
import com.application.community.services.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/parking")
public class ParkingController {

    @Autowired
    private ParkingService parkingService;

    @GetMapping
    public List<ParkingReservation> getAllReservations() {
        return parkingService.getAllReservations();
    }

    @PostMapping
    @RequestMapping("/reserve")
    @ResponseBody
    public ResponseEntity<ParkingResponse> reserveParking(@RequestParam("slotId") Integer slotId,
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date end,
            @RequestParam("userId") Long userId) {
        ParkingReservation reservation = parkingService.reserveParking(slotId, userId, start, end);
        String errorMessage = null;
        HttpStatus status = HttpStatus.OK;
        if (reservation == null) {
            errorMessage = "Unable to reserve parking with given time range.";
            status = HttpStatus.CONFLICT;
        }
        return new ResponseEntity<>(new ParkingResponse(reservation, errorMessage), status);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void deleteReservation(@PathVariable("id") Long reservationId) {
        parkingService.deleteReservation(reservationId);
    }
}
