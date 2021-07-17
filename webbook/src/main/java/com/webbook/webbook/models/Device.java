package com.webbook.webbook.models;


import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "devices")
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper=true,onlyExplicitlyIncluded = true)
@ToString(of = {"deviceInfo"})
@Builder

public class Device extends  AbstractEntity{

    @Column(name = "device_information",nullable = false )
    private String deviceInfo;

    @Column(name = "refresh_token",nullable = false,unique = true)
    private String refreshToken;

    @Column(name = "remember_me",nullable = false)
    private Boolean rememberMe = false;

    @Column(name = "expiration_Date",nullable = false)
    private Date expirationDate;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;


}
