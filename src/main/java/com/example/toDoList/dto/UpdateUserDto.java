package com.example.toDoList.dto;

import javax.validation.constraints.*;

public class UpdateUserDto {

    @Email
    @Size(max = 254, min = 6)
    private String email;

    @Size(max = 250, min = 2)
    private String name;

//    public UpdateUserDto() {
//    }
//
//    public UpdateUserDto(String email, String name) {
//        this.email = email;
//        this.name = name;
//    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "UpdateUserDto{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}