package com.example.IotDemo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface SensorDataRepository extends JpaRepository<JsonData, Integer>{
	
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO json_data(data_text) VALUE(?1)" ,nativeQuery = true)
    void addData(String payload);

}
