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
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idclient;
    private String Name;
    private String Lastname;
    private String Email;
    private String Password;
    private String Phone;
    private Integer Age;


}
