package com.project.rentalCarPage.tables.JDBCClasses;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Setter
@Getter
//@Entity // This tells Hibernate to make a table out of this class
@Table
public class Client {
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idclient;
    private String Name;
    private String Lastname;
    private String Email;
    private String Password;
    private String Phone;
    private Integer Age;


}
