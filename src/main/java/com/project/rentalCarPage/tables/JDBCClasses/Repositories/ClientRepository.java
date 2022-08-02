package com.project.rentalCarPage.tables.JDBCClasses.Repositories;

import com.project.rentalCarPage.tables.JDBCClasses.Client;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

public interface ClientRepository extends CrudRepository<Client,Integer>{
    @Query("SELECT * FROM client \n" +
            "WHERE client.email LIKE :email\n" +
            "AND client.password LIKE :password;")
    public ArrayList<Client> findByEmailAndPassword(@Param("email") String email,@Param("password") String password);

    @Query("SELECT * FROM client " +
            "WHERE client.email LIKE :email;")
    public ArrayList<Client> findByEmail(@Param("email") String email);
}
