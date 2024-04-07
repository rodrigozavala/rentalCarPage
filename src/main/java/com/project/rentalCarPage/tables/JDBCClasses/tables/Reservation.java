package com.project.rentalCarPage.tables.JDBCClasses.tables;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Setter
@Getter
//@Entity // This tells Hibernate to make a table out of this class
@Table
public class Reservation {
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idreservation;
    private Integer idcar;
    private Integer idclient;
    private Integer validity;
    private LocalDateTime reservationdate;
    private LocalDateTime Pickupdate;
    private LocalDateTime Returndate;

}
