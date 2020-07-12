package com.upgrad.quora.service.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

@Entity
@Table(name = "ANSWER", schema = "quora")
@NamedQueries({
        @NamedQuery(name = "AnswerEntityByUuid", query = "select i from AnswerEntity i where i.uuid = :uuid"),
        @NamedQuery(name = "AnswerEntityByid", query = "select i from AnswerEntity i where i.id = :id")
})
public class AnswerEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "UUID")
    @Size(max = 200)
    private String uuid;

    @Column(name = "ANSWER")
    @Size(max = 255)
    private String ans;

    @Column(name = "DATE")
    private ZonedDateTime date;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private UserEntity user_id;

    @ManyToOne
    @JoinColumn(name = "QUESTION_ID")
    private QuestionEntity question_id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public UserEntity getUser_id() {
        return user_id;
    }

    public void setUser_id(UserEntity user_id) {
        this.user_id = user_id;
    }

    public QuestionEntity getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(QuestionEntity question_id) {
        this.question_id = question_id;
    }
}
