package com.project.rentalCarPage.tables.JDBCClasses.Repositories;

import com.project.rentalCarPage.tables.JDBCClasses.Reservation;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ReservationRepository extends CrudRepository<Reservation, Integer>{
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO reservation (idCar,idClient,validity,reservationDate,pickUpDate,returnDate)\n" +
            "VALUES( :idCar , :idClient , :Validity , :resDate , :pDate, :rDate );")
    public int insertReservation(@Param("idCar")Integer idcar,
                                  @Param("idClient")Integer idclient,
                                  @Param("Validity")Integer validity,
                                  @Param("resDate") String resDate,
                                  @Param("pDate")String pDate,
                                  @Param("rDate")String rDate);

}
