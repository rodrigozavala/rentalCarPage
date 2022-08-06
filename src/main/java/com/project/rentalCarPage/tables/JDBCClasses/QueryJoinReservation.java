package com.project.rentalCarPage.tables.JDBCClasses;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class QueryJoinReservation {
    private String name;
    private String lastname;
    private String email;
    private String phone;
    private Integer idcar;
    private String modelname;
    private String imagepath;
    private Integer priceperday;
    private Float kmperl;
    private Integer auttransmission;
    private Integer peoplecapacity;
    private Integer luggagecapacity;
    private Integer idreservation;
    private LocalDateTime reservationdate;
    private LocalDateTime pickupdate;
    private LocalDateTime returndate;
    private Integer validity;
    private Integer availability;
}
