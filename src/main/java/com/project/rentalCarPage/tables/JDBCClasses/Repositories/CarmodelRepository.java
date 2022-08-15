package com.project.rentalCarPage.tables.JDBCClasses.Repositories;

import com.project.rentalCarPage.tables.JDBCClasses.Carmodel;

import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Repository;


@Repository
public interface CarmodelRepository extends CrudRepository <Carmodel,Integer>{

}
