package com.example.IotDemo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JsonDataService {
	
	@Autowired
    private  SensorDataRepository repository;
   
    public List<JsonData> getAllEntities() {
        return repository.findAll();
    }

}
