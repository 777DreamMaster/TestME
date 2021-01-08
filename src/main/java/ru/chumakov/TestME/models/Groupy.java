package ru.chumakov.TestME.models;


import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
public class Groupy {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String code;

    @ManyToOne(fetch = FetchType.EAGER)
    private User owner;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "belong",
            joinColumns = {@JoinColumn(name = "group_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<User> containsUsers;

    //constructors
    public Groupy() {
    }

    public Groupy(String name, String code, User user) {
        this.name = name;
        this.code = code;
        this.owner = user;
    }

    //getters & setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Set<User> getContainsUsers() {
        return containsUsers;
    }

    public void setContainsUsers(Set<User> containsUsers) {
        this.containsUsers = containsUsers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Groupy groupy = (Groupy) o;
        return Objects.equals(id, groupy.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
