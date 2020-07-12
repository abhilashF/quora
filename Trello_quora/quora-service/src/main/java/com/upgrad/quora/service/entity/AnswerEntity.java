package com.upgrad.quora.service.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "ANSWERS", schema = "quora")
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
    private LocalDateTime date;

    @JoinColumn(name = "USER_ID")
    private int user_id;


    @JoinColumn(name = "QUESTION_ID")
    private int question_id;

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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }
}
