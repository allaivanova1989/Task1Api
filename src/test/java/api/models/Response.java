package api.models;

import lombok.Builder;

@lombok.Data

public class Response {
    private String name;
    private String job;
    private int id;
    private String token;

    private String error;



}
