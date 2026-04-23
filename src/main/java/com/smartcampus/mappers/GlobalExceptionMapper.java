/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.mappers;

import com.smartcampus.exceptions.*;
import com.smartcampus.models.ErrorMessage;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Produces;

@Provider
@Produces(MediaType.APPLICATION_JSON) 
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable ex) {
        if (ex instanceof RoomNotEmptyException) {
            return Response.status(409).entity(new ErrorMessage(ex.getMessage(), 409)).type(MediaType.APPLICATION_JSON) .build();
        }
        if (ex instanceof LinkedResourceNotFoundException) {
            return Response.status(422).entity(new ErrorMessage(ex.getMessage(), 422)).type(MediaType.APPLICATION_JSON) .build();
        }
        if (ex instanceof SensorUnavailableException) {
            return Response.status(403).entity(new ErrorMessage(ex.getMessage(), 403)).type(MediaType.APPLICATION_JSON) .build();
        }
        
        return Response.status(500).entity(new ErrorMessage("Internal Server Error: Something went wrong.", 500)).type(MediaType.APPLICATION_JSON) .build();
    }
}
