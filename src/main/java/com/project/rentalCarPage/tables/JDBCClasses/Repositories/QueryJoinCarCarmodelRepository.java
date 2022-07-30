package com.project.rentalCarPage.tables.JDBCClasses.Repositories;

import com.project.rentalCarPage.tables.JDBCClasses.QueryJoinCarCarmodel;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface QueryJoinCarCarmodelRepository extends CrudRepository<QueryJoinCarCarmodel,Integer> {
    @Query("SELECT carmodel.modelname, carmodel.kmperl,\n" +
            "carmodel.auttransmission, carmodel.peoplecapacity,\n" +
            "carmodel.luggagecapacity, carmodel.cartype,car.priceperday,\n" +
            "carmodel.imagepath, car.availability\n" +
            "FROM CAR\n" +
            "INNER JOIN carmodel\n" +
            "ON car.idModel=carmodel.idModel\n" +
            "INNER JOIN Reservation\n" +
            "ON reservation.idreservation=car.idreservation\n" +
            "WHERE (( \"2020-08-21 11:12:00\" < reservation.pickupdate )\n" +
            "OR ( reservation.returndate <\"2020-08-01 11:12:00\"))\n" +
            "AND car.availability=1\n" +
            "AND carmodel.cartype LIKE \"%Cheaper%\";")
    public ArrayList<QueryJoinCarCarmodel> find();
}
