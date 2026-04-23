/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/") // This maps to /api/v1 because your Config is the root
public class DiscoveryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDiscoveryInfo() {
        
        Map<String, Object> discovery = new HashMap<>();
        
        discovery.put("version", "1.0.0");
        discovery.put("description", "Smart Campus Sensor & Room Management API");
        discovery.put("admin_contact", "w2120081@westminster.ac.uk");

        Map<String, String> links = new HashMap<>();
        links.put("rooms", "/SmartCampusWeb/api/v1/rooms");
        links.put("sensors", "/SmartCampusWeb/api/v1/sensors");
        
        discovery.put("_links", links);

        return Response.ok(discovery).build();
    }
}