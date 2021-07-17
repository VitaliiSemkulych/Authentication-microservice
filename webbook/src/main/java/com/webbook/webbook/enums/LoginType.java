package com.webbook.webbook.enums;

import com.webbook.webbook.exceptions.UnsupportedLoginTypeNameException;

public enum LoginType {
    LOCAL("local"), GOOGLE("google"), FACEBOOK("facebook");

    private final String clientName;

    LoginType(String clientName) {
        this.clientName = clientName;
    }

    public String getClientName() {
        return clientName;
    }

    public static LoginType getLoginTypeByClientName(String clientName){
        switch (clientName){
            case "local": return LOCAL;
            case "google": return GOOGLE;
            case "facebook": return FACEBOOK;
            default: throw new UnsupportedLoginTypeNameException();
        }
    }
}
