package com.example.session03.models;


import com.example.session03.constant.UserStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "username", length = 50)

    private String username;

    @Column(name = "email")

    private String email;

    @Column(name = "password", length = 50)

    private String password;
    @Column(name = "fullname", length = 50)

    private String fullname;


    @Column(name = "gender")
    private boolean gender;


    @Column(name = "dob")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date dob;


    @Column(nullable = true)
    private String photos;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @Column(name = "create_time", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;


    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.NEW;


    @Transient
    public String getPhotosImagePath()
    {
        if (photos == null || id == null)
            return null;
        return "/user-photos/" + id + "/" + photos;


    }

}
