/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resources;

import com.smartcampus.data.MockDataStore;
import com.smartcampus.models.SensorReading;
import com.smartcampus.models.Sensor;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.smartcampus.exceptions.SensorUnavailableException;

public class SensorReadingResource {
    private String sensorId;
    // Static storage to keep history alive 
    private static final Map<String, List<SensorReading>> readingsHistory = new ConcurrentHashMap<>();

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    // Fetch history for this specific sensor 
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SensorReading> getHistory() {
        return readingsHistory.getOrDefault(sensorId, new ArrayList<>());
    }

    // Append new reading and update parent 
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReading(SensorReading reading) {
       Sensor sensor = MockDataStore.getSensors().get(sensorId);
        
        if (sensor != null && "MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException("Sensor " + sensorId + " is in MAINTENANCE mode.");
        }

        readingsHistory.computeIfAbsent(sensorId, k -> new ArrayList<>()).add(reading);

        if (sensor != null) {
            sensor.setCurrentValue(reading.getValue());
        }

        return Response.status(Response.Status.CREATED).entity(reading).build();
    }
}
