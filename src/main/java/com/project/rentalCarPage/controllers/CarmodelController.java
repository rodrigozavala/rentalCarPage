package com.project.rentalCarPage.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.rentalCarPage.tables.JDBCClasses.Carmodel;
import com.project.rentalCarPage.tables.JDBCClasses.QueryJoinCarCarmodel;
import com.project.rentalCarPage.tables.JDBCClasses.Repositories.CarmodelRepository;
import com.project.rentalCarPage.tables.JDBCClasses.Repositories.QueryJoinCarCarmodelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.QueryAnnotation;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller // This means that this class is a Controller
@RequestMapping(path="/carmodel")
public class CarmodelController {
    @Autowired
    private CarmodelRepository carmodelRepository;

    @Autowired
    private QueryJoinCarCarmodelRepository query;

    @GetMapping(path="/allCarmodels")
    public @ResponseBody Iterable<Carmodel> getAllUsers() {
        // This returns a JSON or XML with the users
        return carmodelRepository.findAll();
    }
    @GetMapping(path="/allCarmodels2")
    public @ResponseBody Iterable<QueryJoinCarCarmodel> getSomeUsers() {
        // This returns a JSON or XML with the users
        String c="1";
        System.out.println("##############################\n\n\n"+query.find());
        return (Iterable<QueryJoinCarCarmodel>) query.find();
        /*
        try{

            c+="2";
            //System.out.println(carmodelRepository.joinCarCarmodel());
            c+="3";
            //ArrayList jdbc=carmodelRepository.joinCarCarmodel();
            c+="3";
            System.out.println("#################################");
            for(Object o: jdbc){
                System.out.println(o.toString());
            }
            String cad= jdbc.toString();
            c+="4";
            List<String> list= new ArrayList<>();
            list.add(cad);
            return cad;
        }catch(Exception e){
            throw new RuntimeException(e.getMessage()+"#############"+c);
        }*/

    }



}
