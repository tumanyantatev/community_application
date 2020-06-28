package com.application.community.models;

import com.application.community.models.Chat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.UniqueConstraint;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "about")
    private String about;

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    @Column(name = "image")
    private byte[] image;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_chats",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "chat_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "chat_id"})
    )
    @JsonIgnore
    private List<Chat> chats;
    //many to many chats ?

    @Override
    public String toString() {
        return "User{" + "userId=" + userId + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", about='" + about + '\''
                + ", image=" + Arrays.toString(image)/* + ", chats=" + chats*/ + '}';
    }
}
