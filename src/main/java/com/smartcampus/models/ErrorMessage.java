/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.models;
public class ErrorMessage {
    private String error;
    private int status;
    
    public ErrorMessage() {
    }

    public ErrorMessage(String error, int status) {
        this.error = error;
        this.status = status;
    }
    // Getters and Setters
    public String getError() { 
        return error; 
    }
    public int getStatus() { 
        return status; 
    }
    
    public void setError(String error) {
        this.error = error;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
