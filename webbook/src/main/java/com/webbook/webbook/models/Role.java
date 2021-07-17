package com.webbook.webbook.models;


import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "roles")
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true,onlyExplicitlyIncluded = true)
@ToString(of = {"roleName"})
public class Role extends  AbstractEntity{


    @Column(unique = true, nullable = false, name="role")
    @NonNull private String roleName;

    @ManyToMany(mappedBy = "roles",fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<User> users;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    },fetch = FetchType.EAGER)
    @JoinTable(name = "roles_authorities",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Authority> authorities;
}
