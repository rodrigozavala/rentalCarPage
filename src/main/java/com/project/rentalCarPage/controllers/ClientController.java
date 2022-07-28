package com.project.rentalCarPage.controllers;


import com.project.rentalCarPage.tables.Car;
import com.project.rentalCarPage.tables.Client;
import com.project.rentalCarPage.tables.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller // This means that this class is a Controller
@RequestMapping(path="/clients")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;
    @PostMapping(path="/client")
    public @ResponseBody Client getCarsBetween(@RequestParam String email, @RequestParam String password) {
        // This returns a JSON or XML with the users

        for (Client c : clientRepository.findAll()) {
            if (c.getEmail().equals(email) && c.getPassword().equals(password)) {
                return c;
            }
        }

        return new Client();
    }
    //path = "/thins"
    @GetMapping(value="/index")
    public  String showView(){
        /*File f=new File("index.html");

        try {
            OutputStream oS=new FileOutputStream(f);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);

        }*/
        return "index";
    }
}
