package ru.chumakov.TestME.models;

import javax.persistence.*;
import java.sql.Date;

@Entity
public class Testing {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date passDate;

    @ManyToOne(fetch = FetchType.EAGER)
    private Test test;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    //constructors
    public Testing() {
    }

    public Testing(Date passDate, Test test, User user) {
        this.passDate = passDate;
        this.test = test;
        this.user = user;
    }

    //getters & setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getPassDate() {
        return passDate;
    }

    public void setPassDate(Date passDate) {
        this.passDate = passDate;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
