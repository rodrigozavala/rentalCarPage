package com.project.rentalCarPage.tables.JDBCClasses.Repositories;

import com.project.rentalCarPage.tables.JDBCClasses.Client;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Repository
public interface ClientRepository extends CrudRepository<Client,Integer>{
    @Query("SELECT * FROM client \n" +
            "WHERE client.email LIKE :email\n" +
            "AND client.password LIKE :password;")
    public ArrayList<Client> findByEmailAndPassword(@Param("email") String email,@Param("password") String password);

    @Query("SELECT * FROM client " +
            "WHERE client.email LIKE :email;")
    public ArrayList<Client> findByEmail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query("INSERT INTO CLIENT(Name,LastName,Email,Password,Phone,Age)\n" +
            "VALUES( :name , :lastName , :email , :password , :phone , :age);")
    public int insertClient(@Param("name") String name,
                            @Param("lastName")String lastName,
                            @Param("email") String email,
                            @Param("password") String password,
                            @Param("phone") String phone,
                            @Param("age") Integer age);
}
