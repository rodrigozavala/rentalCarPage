package com.project.rentalCarPage.tables.JDBCClasses;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QueryJoinCarCarmodel {
    private Integer idcar;
    private String modelname;
    private Float kmperl;
    private Integer auttransmission;
    private Integer peoplecapacity;
    private Integer luggagecapacity;
    private String cartype;
    private Integer priceperday;
    private String imagepath;
    private Integer availability;
}
