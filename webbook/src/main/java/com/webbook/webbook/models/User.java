package com.webbook.webbook.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.webbook.webbook.enums.LoginType;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "users")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
@ToString(of = {"userName","email"})
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends AbstractEntity{

//Casual endpoint for GOOGLE oauth2 authorization  /oauth2/authorization/google
//Casual endpoint for FACEBOOK oauth2 authorization  /oauth2/authorization/facebook

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(name = "phone",nullable = true )
    private String phoneNumber;


    @Column(name = "active",nullable = true )
    private Boolean active;

    private String activationCode;


    @OneToMany(mappedBy="user")
    private List<Device> devices;

    @ElementCollection(targetClass = LoginType.class)
    @CollectionTable(name="login_types")
    @Column(name = "loginType", nullable = false)
    @Enumerated(EnumType.STRING)
    private List<LoginType> loginType;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    },fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Role> roles;





}
