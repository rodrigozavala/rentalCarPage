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
@Getter
@Setter
//@Entity // This tells Hibernate to make a table out of this class
@Table
public class Carmodel {
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idmodel;
    private String Modelname;
    private Float Kmperl;
    private Integer Auttransmission;
    private Integer Peoplecapacity;
    private Integer Luggagecapacity;
    private String Cartype;
    private String Imagepath;
}
