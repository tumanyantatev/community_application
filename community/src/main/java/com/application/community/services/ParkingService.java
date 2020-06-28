package com.application.community.services;

import com.application.community.models.ParkingReservation;
import com.application.community.models.ParkingSlot;
import com.application.community.models.User;
import com.application.community.repositories.ParkingReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class ParkingService {
    public static final int NUMBER_OF_PARKING_SLOTS = 100;

    @Autowired
    private ParkingReservationRepository parkingReservationRepository;

    private Lock[] slotLocks;

    @PostConstruct
    public void init() {
        slotLocks = new Lock[NUMBER_OF_PARKING_SLOTS];
        for (int i = 0; i < NUMBER_OF_PARKING_SLOTS; ++i) {
            slotLocks[i] = new ReentrantLock();
        }
    }

    public List<ParkingReservation> getAllReservations() {
        return parkingReservationRepository.findAll();
    }

    public ParkingReservation reserveParking(Integer parkingSlotId, Long userId, Date requestedReservationStart, Date requestedReservationEnd) {
        Lock slotLock = slotLocks[parkingSlotId];
        slotLock.lock();
        ParkingReservation reservation = null;
        try {
            List<ParkingReservation> reservations = parkingReservationRepository.findReservationsForSlot(parkingSlotId);
            boolean isAvailable = isAvailable(requestedReservationStart, requestedReservationEnd, reservations);
            if (isAvailable) {
                reservation = saveReservation(parkingSlotId, userId, requestedReservationStart, requestedReservationEnd);
            } else {
                System.out.println("Unable to reserve for given time range");
            }
        } finally {
            slotLock.unlock();
        }
        return reservation;
    }

    public void deleteReservation(Long reservationId) {
        parkingReservationRepository.deleteById(reservationId);
    }

    private boolean isAvailable(Date requestedReservationStart, Date requestedReservationEnd,
            List<ParkingReservation> reservations) {
        boolean isAvailable = true;
        for (ParkingReservation reservation : reservations) {
            Date start = reservation.getReservationStart();
            Date end = reservation.getReservationEnd();
            if (doesIntersect(requestedReservationStart, requestedReservationEnd, start, end)) {
                isAvailable = false;
                break;
            }
        }
        return isAvailable;
    }

    private ParkingReservation saveReservation(Integer parkingSlotId, Long userId, Date requestedReservationStart, Date requestedReservationEnd) {
        ParkingReservation newReservation = new ParkingReservation();
        newReservation.setReservationStart(requestedReservationStart);
        newReservation.setReservationEnd(requestedReservationEnd);
        ParkingSlot parkingSlot = new ParkingSlot();
        parkingSlot.setParkingSlotId(parkingSlotId);
        newReservation.setParkingSlot(parkingSlot);
        User user = new User();
        user.setUserId(userId);
        newReservation.setUser(user);
        return parkingReservationRepository.saveAndFlush(newReservation);
    }

    public boolean doesIntersect(Date requestedReservationStart, Date requestedReservationEnd, Date start, Date end) {
        return (isEqualOrAfter(start, requestedReservationStart) && isEqualOrBefore(start, requestedReservationEnd))
                ||
               (isEqualOrBefore(end, requestedReservationEnd) && isEqualOrAfter(end, requestedReservationStart))
                ||
               (isEqualOrAfter(requestedReservationStart, start) && isEqualOrBefore(requestedReservationEnd, end));
    }

    private boolean isEqualOrAfter(Date date1, Date date2) {
        return date1.equals(date2) || date1.after(date2);
    }

    private boolean isEqualOrBefore(Date date1, Date date2) {
        return date1.equals(date2) || date1.before(date2);
    }
}
