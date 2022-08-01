package com.project.rentalCarPage.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.rentalCarPage.tables.JDBCClasses.Car;
import com.project.rentalCarPage.tables.JDBCClasses.Carmodel;
import com.project.rentalCarPage.tables.JDBCClasses.QueryJoinCarCarmodel;
import com.project.rentalCarPage.tables.JDBCClasses.Repositories.CarmodelRepository;
import com.project.rentalCarPage.tables.JDBCClasses.Repositories.QueryJoinCarCarmodelRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.QueryAnnotation;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller // This means that this class is a Controller
@RequestMapping(path="/pickACar")
public class CarmodelController {
    @Autowired
    private CarmodelRepository carmodelRepository;

    @Autowired
    private QueryJoinCarCarmodelRepository query;

    /*
    @GetMapping(path="/allCarmodels")
    public @ResponseBody Iterable<Carmodel> getAllUsers() {
        // This returns a JSON or XML with the users
        return carmodelRepository.findAll();
    }
    @GetMapping(path="/allCarmodels2")
    public @ResponseBody Iterable<QueryJoinCarCarmodel> getSomeCarsByPost() {
        //Change this later to receive parameters and map from post
        LocalDateTime start=LocalDateTime.of(2022,8,1,11,0,0);
        LocalDateTime end=LocalDateTime.of(2022,8,21,11,30,0);
        String endString=end.toString().replace("T", " ");
        String startString=start.toString().replace("T"," ");
        String carType="%Cheaper%";
        Integer maxPrice=10000;
        ArrayList<QueryJoinCarCarmodel> list=query.findByTimeFrameTypeAndPriceAsc(endString,startString,carType,maxPrice);
        return (Iterable<QueryJoinCarCarmodel>) list;

    }
    @GetMapping(path="/allCarmodels3")
    public @ResponseBody String getSomeCars(){
        LocalDateTime start=LocalDateTime.of(2022,8,1,11,0,0);
        LocalDateTime end=LocalDateTime.of(2022,8,21,11,30,0);
        String endString=end.toString().replace("T", " ");
        String startString=start.toString().replace("T"," ");
        String carType="%Cheaper%";
        Integer maxPrice=10000;
        ArrayList<QueryJoinCarCarmodel> list=query.findByTimeFrameTypeAndPriceAsc(endString,startString,carType,maxPrice);
        return convertToTable(list,true);
    }*/

    @PostMapping(value="/paying")
    public String showSelected(Model model, @RequestParam("selection") Integer selection) {
        // This returns a JSON or XML with the users
        String table=convertToTable(query.findByIdCar(selection),false);
        model.addAttribute("vehicleInformation",table);

        return "paying";
    }
    @GetMapping(value="/CarsBetween")
    public String selectCars(Model model, @RequestParam(name="PickUpDate", required = false) String pickUpDate,@RequestParam(name="ReturnDate",required = false) String returnDate,@RequestParam(name="MaxPricePerDay",required = false) Integer MaxPrice,@RequestParam(name="TypeCar",required = false) String CarType){
        if(pickUpDate==null || returnDate==null || CarType==null){
            model.addAttribute("navInformation", "it works");
        }else{

            model.addAttribute("navInformation",returnDate+":00"+"/ "+pickUpDate+" / "+MaxPrice+": "+ CarType);
            ArrayList<QueryJoinCarCarmodel> list=query.findByTimeFrameTypeAndPriceAsc(returnDate.replace("T"," ")+":00",pickUpDate.replace("T"," ")+":00","%"+CarType+"%",MaxPrice);
            if(list.size()!=0){
                String table= convertToTable(list,true);
                model.addAttribute("tableInformation",table);
            }else{
                String message=String.format("<h1>There are %d cars with this characteristics, <br> please chose again</h1>",list.size());
                model.addAttribute("tableInformation",message);
            }

        }
        return "CarsBetween";
    }

    private String convertToTable(ArrayList<QueryJoinCarCarmodel> list,Boolean isTable){
        //Change later to implement CSS
        String result="<table>";
        //result+="<th></th><th></th><th></th>";
        //result+="<th></th><th></th><th></th>";
        for(QueryJoinCarCarmodel q: list){
            result+="<tr>";
            result+=String.format("<td><img src=\"%s\" width=%d height=%d> <br>\n",q.getImagepath(),100,100);//1
            result+=String.format("Model name:<br>%s</td>\n",q.getModelname());
            result+=String.format("<td>Car Type:<br> %s <br>\n",q.getCartype().replace("types:"," ").replace(":"," "));//2
            result+=String.format("Car Price Per Day $:<br> %d</td>\n",q.getPriceperday());
            result+=String.format("<td>Seats:<br> %d <br>\n",q.getPeoplecapacity());//3
            result+=String.format("Luggage: <br> %d </td>\n",q.getLuggagecapacity());
            result+=String.format("<td>Km per L:<br> %f<br>\n",q.getKmperl());//4
            result+=String.format("Automatic transmission:<br> %s</td>\n",(q.getAuttransmission()==1)?"Yes":"No");
            //Need to change this to send the id to
            //result+=String.format("<td><form method=\"get\" action=\"paying/?selection=%d\">",q.getIdcar());
            if(isTable){
                result+="<td><form method=\"post\" action=\"paying\">";
                result+=String.format("<input type=\"hidden\" name=\"selection\" value=%d>", q.getIdcar());
                result+="<button type=\"submit\"><strong>Chose this <br> car </strong></button></form></td>\n";
            }
            result+="</tr>";
        }
        result+="</table>";
        return result;
    }



}
