package com.webbook.webbook.models;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "authorities")
@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper=true,onlyExplicitlyIncluded = true)
@ToString(of = {"authorityName"})
public class Authority extends  AbstractEntity{

    @Column(unique = true, nullable = false, name="authority")
    private String authorityName;

    @ManyToMany(mappedBy = "authorities",fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Role> roles;
}
