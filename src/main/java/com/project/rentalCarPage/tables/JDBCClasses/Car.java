package com.project.rentalCarPage.tables.JDBCClasses;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

//import javax.persistence.*;
@Getter
@Setter
//@Entity // This tells Hibernate to make a table out of this class
@Table
public class Car {
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idcar;
    private Integer idmodel;
    private Integer idclient;
    private Integer Availability;
    private Integer Idreservation;
    private Integer Priceperday;

}
