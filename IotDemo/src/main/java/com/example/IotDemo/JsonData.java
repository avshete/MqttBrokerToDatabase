package com.example.IotDemo;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;



@Entity
@Getter
@Setter
public class JsonData {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer data_id;
	    

	    //@Column(columnDefinition = "LED") // can store upto 64KB
	    private String paramName;
	    
	    //@Column(columnDefinition = "Value") // can store upto 64KB
	    private int value;
	    
	    private String timestamp;


}
