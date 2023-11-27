package com.example.toDoList.util;

public enum Message {
    ADD_MODEL("adding the model {}"),
    UPDATED_MODEL("updating the model with id {} {}"),
    DELETE_MODEL("deleting the model with id {}"),
    DELETE_ALL_MODEL("deleting all model with user id {}"),
    DELETE_ALL_MODEL_SUB("deleting all model with user id {} and task id {}"),
    GET_MODEL_BY_ID("Get model {}"),
    REQUEST_ALL("Request all models"),
    NAME_MAY_NOT_CONTAIN_SPACES("Name may not be empty or contain spaces"),
    MODEL_NOT_FOUND("model was not found by the passed ID: "),
    INVALID_DATE("Incorrect start or end date. " +
            "The end date cannot be equal to the start date and cannot be before the start date.");

    private final String message;

    Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}