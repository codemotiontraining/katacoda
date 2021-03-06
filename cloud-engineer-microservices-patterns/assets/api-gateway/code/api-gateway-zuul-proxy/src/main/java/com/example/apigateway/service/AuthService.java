package com.example.apigateway.service;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    /* ~~~ OVVIAMENTE NON METTERE QUESTI VALORI QUI IN UN CASO REALE! ~~~~ */
    private final static String TOKEN = "Bearer Fr45dgUDJs8e3hdjke3idhj3hdk8hd";
    private final static String ADMIN = "shopAdmin";
    private final static String PASSWORD = "secretPassword";
    /* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

    public String getToken(String username, String password) {
        return (ADMIN.equals(username) && PASSWORD.equals(password))
                  ? TOKEN
                  : null;
    }

    public boolean validateToken(String token) {
        return TOKEN.equals(token);
    }
}
