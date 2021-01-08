package ru.chumakov.TestME.models;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Result {

    @EmbeddedId
    private ResultKey id;

    @OneToOne
    @JoinColumn(name = "testing_id", insertable = false, updatable = false)
    private Testing testing;

    @OneToOne
    @JoinColumn(name = "question_id", insertable = false, updatable = false)
    private Question question;

    @OneToOne
    @JoinColumn(name = "answer_id", insertable = false, updatable = false)
    private Answer answer;

    public Result() {
    }

    public Result(ResultKey id, Testing testing, Question question, Answer answer) {
        this.id = id;
        this.testing = testing;
        this.question = question;
        this.answer = answer;
    }

    public Result(ResultKey id) {
        this.id = id;
    }

    public ResultKey getId() {
        return id;
    }

    public void setId(ResultKey id) {
        this.id = id;
    }

    public Testing getTesting() {
        return testing;
    }

    public void setTesting(Testing testing) {
        this.testing = testing;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }


}
