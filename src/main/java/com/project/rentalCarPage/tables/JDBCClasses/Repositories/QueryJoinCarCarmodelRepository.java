package com.project.rentalCarPage.tables.JDBCClasses.Repositories;

import com.project.rentalCarPage.tables.JDBCClasses.QueryJoinCarCarmodel;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
//@Transactional(r)
@Repository
public interface QueryJoinCarCarmodelRepository extends CrudRepository<QueryJoinCarCarmodel,Integer> {
    @Query("SELECT car.idcar, carmodel.modelname, carmodel.kmperl,\n" +
            "carmodel.auttransmission, carmodel.peoplecapacity,\n" +
            "carmodel.luggagecapacity, carmodel.cartype,car.priceperday,\n" +
            "carmodel.imagepath, car.availability\n" +
            "FROM CAR\n" +
            "INNER JOIN carmodel\n" +
            "ON car.idModel=carmodel.idModel\n" +
            "INNER JOIN Reservation\n" +
            "ON reservation.idreservation=car.idreservation\n" +
            "WHERE (( :endTime < reservation.pickupdate )\n" +
            "OR ( reservation.returndate < :startTime))\n" +
            "AND car.availability=1\n" +
            "AND carmodel.cartype LIKE :carType\n" +
            "AND car.priceperday < :maxPrice\n" +
            "ORDER BY car.priceperday ASC;")
    public ArrayList<QueryJoinCarCarmodel> findByTimeFrameTypeAndPriceAsc(@Param("endTime") String endTime,@Param("startTime") String startTime,@Param("carType") String carType,@Param("maxPrice") Integer maxPrice);

    @Query("SELECT car.idcar, carmodel.modelname, carmodel.kmperl,\n" +
            "carmodel.auttransmission, carmodel.peoplecapacity,\n" +
            "carmodel.luggagecapacity, carmodel.cartype,car.priceperday,\n" +
            "carmodel.imagepath, car.availability\n" +
            "FROM CAR\n" +
            "INNER JOIN carmodel\n" +
            "ON car.idModel=carmodel.idModel\n" +
            "INNER JOIN Reservation\n" +
            "ON reservation.idreservation=car.idreservation\n" +
            "WHERE (( :endTime < reservation.pickupdate )\n" +
            "OR ( reservation.returndate < :startTime))\n" +
            "AND car.availability=1\n" +
            "AND carmodel.cartype LIKE :carType\n" +
            "AND car.priceperday < :maxPrice\n" +
            "ORDER BY car.priceperday DESC;")
    public ArrayList<QueryJoinCarCarmodel> findByTimeFrameTypeAndPriceDesc(@Param("endTime") String endTime,@Param("startTime") String startTime,@Param("carType") String carType,@Param("maxPrice") Integer maxPrice);


    @Query("SELECT car.idcar, carmodel.modelname, carmodel.kmperl,\n" +
            "carmodel.auttransmission, carmodel.peoplecapacity,\n" +
            "carmodel.luggagecapacity, carmodel.cartype,car.priceperday,\n" +
            "carmodel.imagepath, car.availability\n" +
            "FROM CAR\n" +
            "INNER JOIN carmodel\n" +
            "ON car.idModel=carmodel.idModel\n" +
            "INNER JOIN Reservation\n" +
            "ON reservation.idreservation=car.idreservation\n" +
            "WHERE (car.idcar = :myIdCar )\n"+
            "ORDER BY car.priceperday DESC;")
    public ArrayList<QueryJoinCarCarmodel> findByIdCar(@Param("myIdCar") Integer idCar);

}
