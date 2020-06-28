package com.application.community.repositories;

import com.application.community.models.ParkingReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ParkingReservationRepository extends JpaRepository<ParkingReservation, Long> {

    @Query("SELECT r FROM parking_reservations r WHERE r.parkingSlot.parkingSlotId = :parkingSlotId")
    List<ParkingReservation> findReservationsForSlot(@Param("parkingSlotId") Integer parkingSlotId);
}
