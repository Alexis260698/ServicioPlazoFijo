package com.example.Proyecto.configuration;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GeneralsExceptions extends Exception{
    private String body;

    public GeneralsExceptions(String message) {
        this.body = message;
    }

    public GeneralsExceptions(String message, String message1) {
        super(message);
        this.body = message1;
    }
}
