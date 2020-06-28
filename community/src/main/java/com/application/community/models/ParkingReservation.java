package com.application.community.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Getter
@Setter
@Entity(name = "parking_reservations")
public class ParkingReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long reservationId;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "parking_slot_id", nullable = false)
    private ParkingSlot parkingSlot;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "reservation_start_time", columnDefinition = "timestamp with time zone")
    private Date reservationStart;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "reservation_end_time", columnDefinition = "timestamp with time zone")
    private Date reservationEnd;
}
