package com.application.community.repositories;

import com.application.community.models.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingRepository extends JpaRepository<ParkingSlot, Long> {
}
