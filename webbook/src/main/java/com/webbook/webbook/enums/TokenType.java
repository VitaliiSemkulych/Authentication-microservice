package com.webbook.webbook.enums;

import org.springframework.beans.factory.annotation.Value;

public enum TokenType {
    ACCESS_TOKEN,
    REFRESH_TOKEN;

//    private String deviceInfo;
//    private Boolean rememberMe;

//    public TokenType setDeviceInfo(String deviceInfo) {
//        this.deviceInfo = this.equals(TokenType.REFRESH_TOKEN)? deviceInfo:null;
//        return this;
//    }
//    public String getDeviceInfo() {
//        return deviceInfo;
//    }
//
//    public TokenType setRememberMe(Boolean rememberMe) {
//        this.rememberMe = this.equals(TokenType.REFRESH_TOKEN)? rememberMe:null;
//        return this;
//    }
//    public Boolean getRememberMe() {
//        return rememberMe;
//    }
}
