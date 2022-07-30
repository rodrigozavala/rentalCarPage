package com.project.rentalCarPage.tables.JDBCClasses;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
/*
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
*/
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
    private String reservationdate;
    private String Pickupdate;
    private String Returndate;

}
