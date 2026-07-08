package com.sisencodigital.dashboard.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@EqualsAndHashCode(of = "id")
@ToString(exclude = {"teamMembers"})
@Getter
@Setter
@Entity
@Builder
@Table(name = "projects")
@NoArgsConstructor
@AllArgsConstructor
public class Project extends BaseEntity{
    private String name;
    private String description;
    @OneToMany(mappedBy = "project", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Report> reports;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<User> teamMembers;
}
