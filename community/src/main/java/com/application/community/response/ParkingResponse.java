package com.application.community.response;

import com.application.community.models.ParkingReservation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ParkingResponse {
    private ParkingReservation reservation;
    private String errorMessage;
}
