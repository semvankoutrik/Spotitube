package nl.han.oose.dea.presentation.models;

import java.util.Map;

public class BadRequest {
    private String message;
    private Map<String, String> errors;

    public static BadRequest fromMessage(String message) {
        BadRequest badRequest = new BadRequest();

        badRequest.message = message;

        return badRequest;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    public BadRequest addError(String key, String message) {
        errors.put(key, message);

        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
