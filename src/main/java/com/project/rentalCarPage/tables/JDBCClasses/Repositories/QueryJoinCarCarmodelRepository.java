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

    @Query("SELECT DISTINCT r.idcar, carmodel.modelname, carmodel.kmperl,\n" +
            "carmodel.auttransmission, carmodel.peoplecapacity,\n" +
            "carmodel.luggagecapacity, carmodel.cartype, car.priceperday,\n" +
            "carmodel.imagepath, car.availability\n" +
            "FROM  (SELECT *\n" +
            "FROM car\n" +
            "WHERE car.idcar NOT IN(\n" +
            "SELECT r.idcar \n" +
            "FROM reservation AS r\n" +
            "WHERE ((r.pickupdate< :endTime AND :endTime <r.returndate) \n" +
            "OR (r.pickupdate< :startTime AND :startTime <r.returndate)\n" +
            "OR ( :startTime <r.pickupdate AND r.returndate <:endTime)\n" +
            "OR (r.pickupdate< :startTime AND :endTime <r.returndate)) \n" +
            "AND r.validity=1)) AS car\n" +
            "LEFT JOIN reservation AS r\n" +
            "ON r.idcar=car.idcar\n" +
            "LEFT JOIN carmodel\n" +
            "ON carmodel.idmodel=car.idmodel\n" +
            "WHERE car.availability=1\n" +
            "AND carmodel.cartype LIKE :carType\n" +
            "AND car.priceperday < :maxPrice\n" +
            "ORDER BY car.priceperday ASC;")
    public ArrayList<QueryJoinCarCarmodel> findByTimeFrameTypeAndPriceAsc(@Param("endTime") String endTime,
                                                                          @Param("startTime") String startTime,
                                                                          @Param("carType") String carType,
                                                                          @Param("maxPrice") Integer maxPrice);

    @Query("SELECT DISTINCT r.idcar, carmodel.modelname, carmodel.kmperl,\n" +
            "carmodel.auttransmission, carmodel.peoplecapacity,\n" +
            "carmodel.luggagecapacity, carmodel.cartype, car.priceperday,\n" +
            "carmodel.imagepath, car.availability\n" +
            "FROM  (SELECT *\n" +
            "FROM car\n" +
            "WHERE car.idcar NOT IN(\n" +
            "SELECT r.idcar \n" +
            "FROM reservation AS r\n" +
            "WHERE ((r.pickupdate< :endTime AND :endTime <r.returndate) \n" +
            "OR (r.pickupdate< :startTime AND :startTime <r.returndate)\n" +
            "OR ( :startTime <r.pickupdate AND r.returndate <:endTime)\n" +
            "OR (r.pickupdate< :startTime AND :endTime <r.returndate)) \n" +
            "AND r.validity=1)) AS car\n" +
            "LEFT JOIN reservation AS r\n" +
            "ON r.idcar=car.idcar\n" +
            "LEFT JOIN carmodel\n" +
            "ON carmodel.idmodel=car.idmodel\n" +
            "WHERE car.availability=1\n" +
            "AND carmodel.cartype LIKE :carType\n" +
            "AND car.priceperday < :maxPrice\n" +
            "ORDER BY car.priceperday DESC;")
    public ArrayList<QueryJoinCarCarmodel> findByTimeFrameTypeAndPriceDesc(@Param("endTime") String endTime,
                                                                           @Param("startTime") String startTime,
                                                                           @Param("carType") String carType,
                                                                           @Param("maxPrice") Integer maxPrice);


    @Query("SELECT car.idcar, carmodel.modelname, carmodel.kmperl,\n" +
            "carmodel.auttransmission, carmodel.peoplecapacity,\n" +
            "carmodel.luggagecapacity, carmodel.cartype,car.priceperday,\n" +
            "carmodel.imagepath, car.availability\n" +
            "FROM CAR\n" +
            "INNER JOIN carmodel\n" +
            "ON car.idModel=carmodel.idModel\n" +
            "INNER JOIN Reservation\n" +
            "ON reservation.idreservation=car.idreservation\n" +
            "WHERE (car.idcar = :myIdCar );")
    public ArrayList<QueryJoinCarCarmodel> findByIdCar(@Param("myIdCar") Integer idCar);

    @Query("SELECT DISTINCT car.idcar, carmodel.modelname, carmodel.kmperl,\n" +
            "carmodel.auttransmission, carmodel.peoplecapacity,\n" +
            "carmodel.luggagecapacity, carmodel.cartype, car.priceperday,\n" +
            "carmodel.imagepath, car.availability\n" +
            "FROM  car\n" +
            "INNER JOIN carmodel\n" +
            "ON carmodel.idmodel=car.idmodel;")
    public ArrayList<QueryJoinCarCarmodel> findAll();

}
