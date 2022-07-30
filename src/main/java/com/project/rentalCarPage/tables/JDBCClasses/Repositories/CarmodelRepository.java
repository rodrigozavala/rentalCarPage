package com.project.rentalCarPage.tables.JDBCClasses.Repositories;

import com.project.rentalCarPage.tables.JDBCClasses.Carmodel;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;

@Repository
public interface CarmodelRepository extends CrudRepository <Carmodel,Integer>{
    /*@Query("SELECT * FROM CAR INNER JOIN CARMODEL ON CAR.idModel=CARMODEL.idModel")
    public ArrayList<Object> joinCarCarmodel();*/
}
