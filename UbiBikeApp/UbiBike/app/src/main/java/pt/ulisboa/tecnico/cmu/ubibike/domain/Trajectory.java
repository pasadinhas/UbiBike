package pt.ulisboa.tecnico.cmu.ubibike.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by André on 21-03-2016.
 */
public class Trajectory implements Serializable{

    private Date date;

    public Trajectory(Date date){
        this.date = date;
    }

    protected Trajectory() { }	//Needed for JPA/Hibernate

    public Date getDate(){
        return date;
    }

    public void setDate(Date date){
        this.date = date;
    }

}

