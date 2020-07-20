package com.upgrad.quora.service.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
//@Entity annotation specifies that the corresponding class is a JPA entity
@Entity
@Table(name = "QUESTIONS", schema = "quora")
@NamedQueries({
        @NamedQuery(name = "QuestionEntityByUuid", query = "select i from QuestionEntity i where i.uuid = :uuid"),
        @NamedQuery(name = "QuestionEntityByid", query = "select i from QuestionEntity i where i.id = :id")
})
public class QuestionEntity {
    //@Id annotation specifies that the corresponding attribute is a primary key
        @Id
        @Column(name = "ID")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

        @Column(name = "UUID")
        @Size(max = 200)
        private String uuid;


        @Column(name = "CONTENT")
        @Size(max = 500)
        private String content;

        @Column(name = "DATE")
        private LocalDateTime date;


        @JoinColumn(name = "USER_ID")
        private Integer user_id;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }
}
