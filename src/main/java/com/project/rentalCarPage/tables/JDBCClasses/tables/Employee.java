package com.project.rentalCarPage.tables.JDBCClasses.tables;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Setter
@Getter
@Table
public class Employee {
    @Id
    private Integer idemployee;
    private String name;
    private String lastname;
    private String username;
    private String email;
    private String password;
    private String phone;
    private Integer age;
}
