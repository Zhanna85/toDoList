package com.example.toDoList.dto;


import javax.validation.constraints.*;

public class NewUserDto {

    @NotBlank
    @Email
    @Size(max = 254, min = 6)
    private String email;

    @NotBlank(message = "Field: name. Error: must not be blank. Value: null")
    @Size(max = 250, min = 2)
    private String name;

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "NewUserDto{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}