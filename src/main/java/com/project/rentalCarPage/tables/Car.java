package com.project.rentalCarPage.tables;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
@Getter
@Setter
@Entity // This tells Hibernate to make a table out of this class
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idcar;
    private Integer idmodel;
    private Integer idclient;
    private Integer Availability;
    private Integer Idreservation;
    private Integer Priceperday;

}
