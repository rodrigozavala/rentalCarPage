package com.project.rentalCarPage.controllers;


import com.project.rentalCarPage.tables.JDBCClasses.Client;
import com.project.rentalCarPage.tables.JDBCClasses.Repositories.ClientRepository;
import com.project.rentalCarPage.tables.JDBCClasses.Reservation;
import com.project.rentalCarPage.tables.JDBCClasses.toolsToCustomizeNav;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

@Controller // This means that this class is a Controller
@RequestMapping(path="/")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;
    @PostMapping(value="/clientInfo")
    public String getUserProfile(Model model,HttpServletResponse response,HttpServletRequest request){

        if(request.getCookies()!=null){//(COOKIES) we have cookies
            Cookie myCookie=null;
            Cookie userName=null;
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
                return "/clientInfo";
            }

            if(myCookie.getValue().equals("null")){//(COOKIES)(NOT CONNECTED)Not connected to the user email
                //Getting the parameters
                String email=request.getParameter("Email");
                String password=request.getParameter("Password");
                //System.out.println(email+"###"+password);
                if(email !=null && password!=null){//(COOKIES)(NOT CONNECTED)(LOGIN)if we come from login
                    //we use authentication to connect or send a message of
                    //failed login, wrong password or email
                    userAuthentication(model,request,response,email,password);
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

        return "/clientInfo";
    }
    @GetMapping(value="/index")
    public  String showMainView(Model model,HttpServletResponse response,HttpServletRequest request){
        toolsToCustomizeNav.navCustomization(model,request,response);
        //model.addAttribute("cosa","Funcionó");
        return "index";
    }

    @GetMapping(value="/login")
    public  String showLoginView(Model model,HttpServletResponse response,HttpServletRequest request){
        toolsToCustomizeNav.navCustomization(model,request,response);


        //model.addAttribute("cosa","Funcionó");
        return "login";
    }

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


    private void showUserInfo(Model model,ArrayList<Client> list,HttpServletRequest request){
        String message="<h1>User Info</h1>";
        message+=String.format("<h3>Name: %s</h3> <br>",list.get(0).getName());
        message+=String.format("<h3>Lastname: %s</h3> <br>",list.get(0).getLastname());
        message+=String.format("<h3>Email: %s</h3><br>",list.get(0).getEmail());
        message+=String.format("<h3>Phone: %s</h3><br>",list.get(0).getPhone());
        message+=String.format("<h3>Age: %s</h3><br>",list.get(0).getAge());

        /*
        Cookie carData=Arrays.stream(request.getCookies()).
                filter(c->c.getName().equals(toolsToCustomizeNav.COOKIE_CARDATA)).
                findFirst().get();
        if(carData!=null){
            JSONObject res= new JSONObject(carData.getValue());
            message+=String.format("<br><form><button method=\"get\" action=\"\">Change values</button></form>");

        }*/
        message+="<br><form method=\"get\" action=\"changeData\"><button type=\"submit\">Change User data</button></form>";
        model.addAttribute("userInfo",message);
    }

    private  void noUser(Model model){
        String message="<h1>There is no user, please login</h1>";
        model.addAttribute("userInfo",message);
    }

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
}
