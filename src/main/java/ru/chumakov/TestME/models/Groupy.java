package ru.chumakov.TestME.models;


import javax.persistence.*;

@Entity
public class Groupy {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String code;

    @ManyToOne(fetch = FetchType.EAGER)
    private User owner;

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
}
