package com.upgrad.quora.service.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
@Entity
@Table(name = "IMAGES", schema = "imagehoster")
@NamedQueries({
        @NamedQuery(name = "QuestionEntityByUuid", query = "select i from QuestionEntity i where i.uuid = :uuid"),
        @NamedQuery(name = "QuestionEntityByid", query = "select i from QuestionEntity i where i.id = :id")
})
public class QuestionEntity {

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


        @ManyToOne
        @JoinColumn(name = "USER_ID")
        private UserEntity user_id;

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

    public UserEntity getUser_id() {
        return user_id;
    }

    public static void setUser_id(UserEntity user_id) {
        this.user_id = user_id;
    }
}
