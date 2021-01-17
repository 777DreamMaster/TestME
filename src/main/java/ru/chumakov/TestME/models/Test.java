package ru.chumakov.TestME.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private LocalDateTime creationDate;

    @ManyToOne(fetch = FetchType.EAGER)
    private Groupy fromGroup;

    @OneToMany(mappedBy = "test",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Set<Question> questions;

    @OneToMany(mappedBy = "test",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Set<Testing> testings;

    //constructors
    public Test() {
    }

    public Test(String name, LocalDateTime creationDate) {
        this.name = name;
        this.creationDate = creationDate;
    }

    public Test(String name, LocalDateTime creationDate, Groupy fromGroup) {
        this.name = name;
        this.creationDate = creationDate;
        this.fromGroup = fromGroup;
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

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Groupy getFromGroup() {
        return fromGroup;
    }

    public void setFromGroup(Groupy fromGroup) {
        this.fromGroup = fromGroup;
    }
}
