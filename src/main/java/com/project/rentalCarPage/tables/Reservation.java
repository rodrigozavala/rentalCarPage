package com.project.rentalCarPage.tables;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Setter
@Getter
@Entity // This tells Hibernate to make a table out of this class
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idreservation;
    private Integer idcar;
    private Integer idclient;
    private Integer validity;
    private String reservationdate;
    private String Pickupdate;
    private String Returndate;

}
