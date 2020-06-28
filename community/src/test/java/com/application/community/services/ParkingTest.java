package com.application.community.services;

import com.application.community.controllers.ParkingController;
import com.application.community.models.ParkingReservation;
import com.application.community.models.ParkingSlot;
import com.application.community.models.User;
import com.application.community.repositories.ParkingRepository;
import com.application.community.repositories.ParkingReservationRepository;
import com.application.community.repositories.UserRepository;
import com.application.community.response.ParkingResponse;
import com.application.community.services.ParkingService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.application.community.helper.UsersHelper.getUser;
import static com.application.community.services.ParkingService.NUMBER_OF_PARKING_SLOTS;
import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class ParkingTest {

    private static final int NUMBER_OF_USERS = 10;
    private static final List<Long> userIds = new ArrayList<>(10);
    private static final List<Integer> parkingSlotIds = new ArrayList<>(NUMBER_OF_PARKING_SLOTS);

    @Autowired
    private ParkingController parkingController;

    @Autowired
    private ParkingService parkingService;

    @Autowired
    private ParkingReservationRepository parkingReservationRepository;

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    public void prepareEntities() {
        for (int i = 0; i < NUMBER_OF_PARKING_SLOTS; ++i) {
            ParkingSlot parkingSlot = parkingRepository.saveAndFlush(new ParkingSlot());
            parkingSlotIds.add(parkingSlot.getParkingSlotId());
        }

        for (int i = 0; i < NUMBER_OF_USERS; ++i) {
            User user = userRepository.saveAndFlush(getUser(i));
            userIds.add(user.getUserId());
        }
    }

    @BeforeEach
    public void deleteReservations() {
        parkingReservationRepository.deleteAll();
    }

    @Test
    public void testReserveParkingNoConflict() {
        long now = System.currentTimeMillis();
        Date resStart = new Date(now + 10000);
        Date resEnd = new Date(now + 20000);
        ResponseEntity<ParkingResponse> parkingResponseResponseEntity = parkingController
                .reserveParking(parkingSlotIds.get(0), resStart, resEnd, userIds.get(0));
        assertNotNull(parkingResponseResponseEntity.getBody());
        assertNull(parkingResponseResponseEntity.getBody().getErrorMessage());
        assertEquals(parkingResponseResponseEntity.getStatusCode(), HttpStatus.OK);


        Date resStart2 = new Date(now + 10000);
        Date resEnd2 = new Date(now + 20000);
        parkingResponseResponseEntity = parkingController
                .reserveParking(parkingSlotIds.get(1), resStart2, resEnd2, userIds.get(0));
        assertNull(parkingResponseResponseEntity.getBody().getErrorMessage());
        assertEquals(parkingResponseResponseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testReserveParkingWithConflict() {
        long now = System.currentTimeMillis();
        Date resStart1 = new Date(now + 10000);
        Date resEnd1 = new Date(now + 20000);
        ResponseEntity<ParkingResponse> parkingResponseResponseEntity = parkingController
                .reserveParking(parkingSlotIds.get(0), resStart1, resEnd1, userIds.get(0));
        assertNotNull(parkingResponseResponseEntity.getBody());
        assertNull(parkingResponseResponseEntity.getBody().getErrorMessage());
        assertEquals(parkingResponseResponseEntity.getStatusCode(), HttpStatus.OK);


        Date resStart2 = new Date(now + 10000);
        Date resEnd2 = new Date(now + 15000);
        parkingResponseResponseEntity = parkingController
                .reserveParking(parkingSlotIds.get(0), resStart2, resEnd2, userIds.get(0));
        assertNotNull(parkingResponseResponseEntity.getBody().getErrorMessage());
        assertEquals(parkingResponseResponseEntity.getStatusCode(), HttpStatus.CONFLICT);
    }

    @Test
    public void testReserveParkingMultipleRequestForSameSlot() {
        long now = System.currentTimeMillis();
        Date resStart1 = new Date(now + 10000);
        Date resEnd1 = new Date(now + 20000);


        ExecutorService executor = Executors.newFixedThreadPool(10);
        Callable<ResponseEntity<ParkingResponse>> reservationTask = (Callable<ResponseEntity<ParkingResponse>>) () -> parkingController.reserveParking(parkingSlotIds.get(0), resStart1, resEnd1, userIds.get(0));
        try {
            int reservedCount = 0;
            var futures = executor.invokeAll(Arrays.asList(reservationTask, reservationTask, reservationTask));
            for (var future : futures) {
                ResponseEntity<ParkingResponse> result = future.get();
                if (result.getStatusCode() == HttpStatus.OK) {
                    ++reservedCount;
                }
            }

            assertEquals(1, reservedCount);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    @Test
    public void testGetAllReservations() {
        long now = System.currentTimeMillis();
        Date resStart = new Date(now + 10000);
        Date resEnd = new Date(now + 20000);
        ResponseEntity<ParkingResponse> parkingResponseResponseEntity = parkingController
                .reserveParking(parkingSlotIds.get(0), resStart, resEnd, userIds.get(0));
        assertNotNull(parkingResponseResponseEntity.getBody());
        assertNull(parkingResponseResponseEntity.getBody().getErrorMessage());
        assertEquals(parkingResponseResponseEntity.getStatusCode(), HttpStatus.OK);


        Date resStart2 = new Date(now + 10000);
        Date resEnd2 = new Date(now + 20000);
        parkingResponseResponseEntity = parkingController
                .reserveParking(parkingSlotIds.get(1), resStart2, resEnd2, userIds.get(0));
        assertNull(parkingResponseResponseEntity.getBody().getErrorMessage());
        assertEquals(parkingResponseResponseEntity.getStatusCode(), HttpStatus.OK);

        List<ParkingReservation> allReservations = parkingController.getAllReservations();
        assertEquals(2, allReservations.size());
    }

    @Test
    public void testDeleteReservation() {
        long now = System.currentTimeMillis();
        Date resStart = new Date(now + 10000);
        Date resEnd = new Date(now + 20000);
        ResponseEntity<ParkingResponse> parkingResponseResponseEntity = parkingController
                .reserveParking(parkingSlotIds.get(0), resStart, resEnd, userIds.get(0));
        assertNotNull(parkingResponseResponseEntity.getBody());
        assertNull(parkingResponseResponseEntity.getBody().getErrorMessage());
        assertEquals(parkingResponseResponseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void testIntersect() {
        long now = System.currentTimeMillis();
        Date requestedReservationStart = new Date(now - 100);
        Date requestedReservationEnd = new Date(now + 100);
        assertTrue(parkingService.doesIntersect(requestedReservationStart, requestedReservationEnd, new Date(now - 150), new Date(now + 50)));
        assertTrue(parkingService.doesIntersect(requestedReservationStart, requestedReservationEnd, new Date(now - 50), new Date(now + 150)));
        assertTrue(parkingService.doesIntersect(requestedReservationStart, requestedReservationEnd, new Date(now - 50), new Date(now + 50)));
        assertTrue(parkingService.doesIntersect(requestedReservationStart, requestedReservationEnd, new Date(now - 150), new Date(now + 150)));
    }
}
