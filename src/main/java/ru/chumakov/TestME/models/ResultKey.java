package ru.chumakov.TestME.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ResultKey implements Serializable {

    @Column(name = "testing_id")
    private Long testingId;

    @Column(name = "question_id")
    private Long questionId;

    @Column(name = "answer_id")
    private Long answerId;

    public ResultKey() {
    }

    public ResultKey(Testing testing, Question question, Answer answer) {
        this.testingId = testing.getId();
        this.questionId = question.getId();
        this.answerId = answer.getId();
    }

    public Long getTestingId() {
        return testingId;
    }

    public void setTestingId(Long testingId) {
        this.testingId = testingId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultKey resultKey = (ResultKey) o;
        return Objects.equals(testingId, resultKey.testingId) &&
                Objects.equals(questionId, resultKey.questionId) &&
                Objects.equals(answerId, resultKey.answerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(testingId, questionId, answerId);
    }
}
