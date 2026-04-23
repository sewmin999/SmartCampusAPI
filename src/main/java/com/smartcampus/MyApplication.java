/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.smartcampus; 

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;
import com.smartcampus.resources.*; 
import com.smartcampus.filters.LoggingFilter;
import com.smartcampus.mappers.GlobalExceptionMapper;

@ApplicationPath("/api/v1") 
public class MyApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
       classes.add(DiscoveryResource.class);
        classes.add(SensorResource.class);
        classes.add(SensorReadingResource.class);
        classes.add(SensorRoomResource.class);
        
        classes.add(LoggingFilter.class);
        classes.add(GlobalExceptionMapper.class);
        
        return classes;
    }
}
