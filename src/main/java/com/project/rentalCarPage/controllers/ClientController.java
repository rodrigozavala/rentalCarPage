package com.project.rentalCarPage.controllers;


import com.project.rentalCarPage.tables.JDBCClasses.Client;
import com.project.rentalCarPage.tables.JDBCClasses.Repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller // This means that this class is a Controller
@RequestMapping(path="/")
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
    public  String showView(Model model){
        /*File f=new File("index.html");

        try {
            OutputStream oS=new FileOutputStream(f);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);

        }*/
        model.addAttribute("navInformation","it works fine");
        model.addAttribute("cosa","Funcionó");
        return "index";
    }
}
