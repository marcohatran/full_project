package com.gateway.full_project.model;

public class JwtResponse {
    private String data;

    public JwtResponse(String token) {
        super();
        this.data = token;
    }

    public String getToken() {
        return data;
    }

    public void setToken(String data) {
        this.data = data;
    }
}
