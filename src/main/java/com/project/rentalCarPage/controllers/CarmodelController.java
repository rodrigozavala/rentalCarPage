package com.project.rentalCarPage.controllers;

import com.project.rentalCarPage.tables.JDBCClasses.Carmodel;
import com.project.rentalCarPage.tables.JDBCClasses.Repositories.CarmodelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller // This means that this class is a Controller
@RequestMapping(path="/carmodel")
public class CarmodelController {
    @Autowired
    private CarmodelRepository carmodelRepository;

    @GetMapping(path="/allCarmodels")
    public @ResponseBody Iterable<Carmodel> getAllUsers() {
        // This returns a JSON or XML with the users
        return carmodelRepository.findAll();
    }
}
