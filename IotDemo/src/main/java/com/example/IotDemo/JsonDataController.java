package com.example.IotDemo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/entities")
public class JsonDataController {
	
	@Autowired
    private JsonDataService service;
   
    @GetMapping
    public List<JsonData> getAllEntities() {
        return service.getAllEntities();
    }

}
