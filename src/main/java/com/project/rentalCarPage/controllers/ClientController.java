package com.project.rentalCarPage.controllers;


import com.project.rentalCarPage.tables.JDBCClasses.Client;
import com.project.rentalCarPage.tables.JDBCClasses.QueryJoinReservation;
import com.project.rentalCarPage.tables.JDBCClasses.Repositories.ClientRepository;
import com.project.rentalCarPage.tables.JDBCClasses.Repositories.QueryJoinReservationRepository;

import com.project.rentalCarPage.tables.JDBCClasses.toolsToCustomizeNav;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

@Controller // This means that this class is a Controller
@RequestMapping(path="/")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private QueryJoinReservationRepository queryJoinReservationRepository;

    /**
     * Shows clientInfo.html with client info depending on cookies and parameters
     * @param model
     * @param response
     * @param request
     * @return retrieves clientInfo.html file when requested by a Post Method only.
     */
    @PostMapping(value="/clientInfo")
    public String getUserProfile(Model model,HttpServletResponse response,HttpServletRequest request){

        if(request.getCookies()!=null){//(COOKIES) we have cookies
            Cookie myCookie=null;
            Cookie userName=null;
            //PICKING THE AVAILABLE COOKIES
            try{
                myCookie=Arrays.stream(request.getCookies()).
                        filter(c->c.getName().equals(toolsToCustomizeNav.COOKIE_SESSION)).
                        findFirst().get();

            }catch (NoSuchElementException e) {
                System.out.println("Here it happens with myCookie");
                System.out.println(e.getMessage());
                myCookie = null;
            }
            try {
                userName = Arrays.stream(request.getCookies()).
                        filter(c -> c.getName().equals(toolsToCustomizeNav.COOKIE_USER_NAME)).
                        findFirst().get();
            }catch (NoSuchElementException e){
                System.out.println("Here it happens with userName");
                System.out.println(e.getMessage());
                userName=null;
            }


            if(myCookie==null){//(COOKIES)(NOT CONNECTED)(FIRST TIME)this is the first time we enter the site
                toolsToCustomizeNav.navCustomization(model,request,response);
                noUser(model);
                return "clientInfo";
            }

            if(myCookie.getValue().equals("null")){//(COOKIES)(NOT CONNECTED)Not connected to the user email
                //Getting the parameters
                String email=request.getParameter("Email");
                String password=request.getParameter("Password");
                String phone=request.getParameter("Phone");
                //System.out.println(email+"###"+password);
                if(email !=null && password!=null){//(COOKIES)(NOT CONNECTED)(LOGIN)if we come from login or signup
                    //we use authentication to connect or send a message of
                    //failed login, wrong password or email
                    if(phone==null){//The user comes from login
                        userAuthentication(model,request,response,email,password);
                    }else{//The user is signing-up

                        signingUp(model,request,response);
                    }
                    //there is no need to add toolsToCustomizeNav.navCustomization(model,request,response);

                }else{//(COOKIES)(NOT CONNECTED)(NO LOGIN)we don't came from login
                    noUser(model);
                    toolsToCustomizeNav.navCustomization(model,request,response);
                }
            }else{//(COOKIES)(CONNECTED)
                System.out.println("############"+myCookie.getValue());
                String email=myCookie.getValue().split("\\#\\&\\#")[0];
                ArrayList<Client> list=clientRepository.findByEmail(email);
                showUserInfo(model,list,request);
                toolsToCustomizeNav.navCustomization(model,request,response);
            }
        }else{//(NO COOKIES) (NOT CONNECTED)
            //we put the RCID cookie as 'null' with navCustomization
            noUser(model);
            toolsToCustomizeNav.navCustomization(model,request,response);
        }

        return "clientInfo";
    }

    /**
     * Shows the main page
     * @param model
     * @param response
     * @param request
     * @return index.html
     */
    @GetMapping(value="/index")
    public  String showMainView(Model model,HttpServletResponse response,HttpServletRequest request){
        toolsToCustomizeNav.navCustomization(model,request,response);
        //model.addAttribute("cosa","Funcionó");
        return "index";
    }

    /**
     * Shows login page for clients.
     * @param model
     * @param response
     * @param request
     * @return login.html
     */
    @GetMapping(value="/login")
    public  String showLoginView(Model model,HttpServletResponse response,HttpServletRequest request){
        toolsToCustomizeNav.navCustomization(model,request,response);

        //model.addAttribute("cosa","Funcionó");
        return "login";
    }

    /**
     * Shows SignUp page for clients
     * @param model
     * @param response
     * @param request
     * @return SignUp.html
     */
    @GetMapping(value="/SignUp")
    public  String showSignUpView(Model model,HttpServletResponse response,HttpServletRequest request){
        toolsToCustomizeNav.navCustomization(model,request,response);

        return "SignUp";
    }

    @GetMapping(value="/changeData")
    public  String showChangeDataView(Model model,HttpServletResponse response,HttpServletRequest request){
        toolsToCustomizeNav.navCustomization(model,request,response);
        String message="<h1>Not available in this version yet :( <br> please go back</h1>";
        model.addAttribute("cdInfo",message);

        return "changeData";
    }


    /**
     * Shows logout page to users and erases cookies
     * @param model
     * @param response
     * @param request
     * @return logout.html
     */
    @GetMapping(value="/logout")
    public  String showLogoutView(Model model,HttpServletResponse response,HttpServletRequest request){
        toolsToCustomizeNav.navCustomization(model,request,response);
        Cookie endSession=new Cookie(toolsToCustomizeNav.COOKIE_SESSION,"null");
        Cookie userNameCookie=null;
        if(request.getCookies()!=null){
            for(Cookie c: request.getCookies()){
                if(c.getName().equals(toolsToCustomizeNav.COOKIE_USER_NAME)){
                    userNameCookie=c;
                    userNameCookie.setValue("");
                    userNameCookie.setMaxAge(1);
                    response.addCookie(userNameCookie);
                    break;
                }
            }
        }
        response.addCookie(endSession);

        return "logout";
    }


    /**
     * Shows user's info from DB
     * @param model
     * @param list
     * @param request
     */
    private void showUserInfo(Model model,ArrayList<Client> list,HttpServletRequest request){
        String message="<h1>User Info</h1>";
        message+=String.format("<h3>Name: %s</h3> <br>",list.get(0).getName());
        message+=String.format("<h3>Lastname: %s</h3> <br>",list.get(0).getLastname());
        message+=String.format("<h3>Email: %s</h3><br>",list.get(0).getEmail());
        message+=String.format("<h3>Phone: %s</h3><br>",list.get(0).getPhone());
        message+=String.format("<h3>Age: %s</h3><br>",list.get(0).getAge());

        message+="<br><form method=\"get\" action=\"changeData\"><button type=\"submit\">Change User data</button></form>";
        if(request.getCookies()!=null){
            for(Cookie c: request.getCookies()){
                if(c.getName().equals(toolsToCustomizeNav.COOKIE_CARDATA)){
                    message+="<div>\n" +
                            "                <form method=\"post\" action=\"paying\">\n" +
                            "                    <p>You have a reservation to do</p>\n" +
                            String.format("                    <input type=\"hidden\" id=\"selection\" name=\"selection\" value=\"%s\">\n",c.getValue()) +
                            "                    <button type=\"submit\">Check reservation</button>\n" +
                            "                </form>\n" +
                            "            </div>";
                }
            }
        }
        model.addAttribute("userInfo",message);
        showReservations(model,list.get(0).getIdclient());

    }

    /**
     * In case user hasn't login into the web application. This shows a message to login.
     * @param model
     */
    private  void noUser(Model model){
        String message="<h1>There is no user, please login</h1>";
        model.addAttribute("userInfo",message);
    }

    /**
     * Search for the user in DB, shows information and add cookies. If the user info is erroneous,
     * then it shows a friendly message stating the info is wrong.
     * @param model
     * @param request
     * @param response
     * @param email
     * @param password
     */
    private void userAuthentication(Model model,HttpServletRequest request,HttpServletResponse response,String email, String password){
        ArrayList<Client>list=clientRepository.findByEmailAndPassword(email,password);

        if(list.size()==0){//(ISN'T THERE)we didn't find such user
            String message="<h1>There is no such user, <br> the email or password is wrong, please try again</h1>";
            message+="<form method=\"get\" action=\"/login\">" +
                    "<button type=\"submit\"><strong>Go Back to login</strong>" +
                    "</button></form>";
            model.addAttribute("userInfo",message);
            toolsToCustomizeNav.navCustomization(model,request,response);
        }else{//(IS THERE)we find the user in DB
            Cookie userName=new Cookie(toolsToCustomizeNav.COOKIE_USER_NAME,list.get(0).getName());
            response.addCookie(userName);
            String cookieVal=email+"#&#"+list.get(0).getIdclient();
            //System.out.println("asasas###################"+cookieVal);
            response.addCookie(new Cookie(toolsToCustomizeNav.COOKIE_SESSION,cookieVal));
            showUserInfo(model,list,request);
            toolsToCustomizeNav.updateNavWithUserData(model,userName);
        }
    }

    /**
     * Inserts a new user in DB if it does not already have an account. Show's a message otherwise.
     * @param model
     * @param request
     * @param response
     */
    private void signingUp(Model model,HttpServletRequest request,HttpServletResponse response){
        String email=request.getParameter("Email");
        ArrayList<Client> list=clientRepository.findByEmail("%"+email+"%");
        if(list.size()==0){
            String name=request.getParameter("Name");
            String lastname=request.getParameter("Lastname");
            String phone=request.getParameter("Phone");
            String password=request.getParameter("Password");
            Integer age=Integer.valueOf(request.getParameter("Age"));
            clientRepository.insertClient(name,lastname,email,password,phone,age);
            String message=String.format("<h1>Welcome to Rental Car %s</h1>",name);
            model.addAttribute("aTest",message);
            userAuthentication(model,request,response,email,password);

        }else{
            String message=String.format("<h1>This email: %s <br> already belongs to an account,<br>",email) +
                    " please enter with another email account or click on <a href=\"#\">Password recovery</a></h1>";
            model.addAttribute("aTest",message);
        }
    }

    /**
     * Shows reservations made by some user. Finds its reservations by the user's ID.
     * @param model
     * @param id
     */
    private void showReservations(Model model,Integer id){
        String message="";
        ArrayList<QueryJoinReservation> list= queryJoinReservationRepository.findReservationsById(id);
        if(list==null){
            message+="<h2>There are no reservations to show</h2>";
        }else if(list.size()==0){
            message+="<h2>There are no reservations to show</h2>";
        }else{
            message+="<table class=\"table table-striped table-hover\">";
            DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            for (QueryJoinReservation q : list){
                message+=(q.getValidity()==1)?"<tr style='background-color: #64FF33;'>":"<tr style='background-color: #F34327;'>";
                message+=String.format("<td> Reservation id: %d <br>",q.getIdreservation());//1
                message+=String.format("Is Valid? : %s <br></td>",(q.getValidity()==1)?"Yes":"No");
                message+=String.format("<td>Reservation Date: %s <br>",q.getReservationdate());//2
                message+=String.format("PickUp Date: %s <br>",q.getPickupdate());
                message+=String.format("Return Date: %s <br>",q.getReturndate());
                message+=String.format("Total amount:$ %f <br> </td>",Float.valueOf(q.getPriceperday()* ChronoUnit.DAYS.between(q.getPickupdate(),q.getReturndate())));//3

                message+=String.format("<td><img src=\"%s\" width=%d height=%d> <br>\n",q.getImagepath(),100,100);//4
                message+=String.format("Model name:<br> %s %d<br>\n",q.getModelname(),q.getIdcar());
                message+=String.format("Car Price Per Day:$  %d <br></td>\n",q.getPriceperday());
                message+=String.format("<td>Seats: %d <br>\n",q.getPeoplecapacity());//5
                message+=String.format("Luggage: %d <br>\n",q.getLuggagecapacity());
                message+=String.format("Km per L: %f <br>\n",q.getKmperl());
                message+=String.format("Automatic transmission: %s</td>\n",(q.getAuttransmission()==1)?"Yes":"No");
                if(q.getValidity()==1){
                    message+="<td><form method=\"post\" action=\"cancelReservation\">";
                    message+=String.format("<input type=\"hidden\" name=\"selectedRes\" value=%d>", q.getIdreservation());
                    message+="<button type=\"submit\"><strong>Cancel this <br> reservation </strong></button></form></td>\n";
                }else{
                    message+="<td><form method=\"post\" action=\"cancelReservation\">";
                    message+=String.format("<input type=\"hidden\" name=\"selectedRes\" value=%d>", q.getIdreservation());
                    message+="<button disabled type=\"submit\"><strong>Cancel this <br> reservation </strong></button></form></td>\n";
                }
                message+="</tr>";
            }
        }
        model.addAttribute("clientReservations",message);
    }
}
