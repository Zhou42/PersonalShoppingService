package com.futureshop.pojo;

import javax.validation.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.soap.SAAJResult;
import java.util.Date;

/**
 * Created by yangzhou on 2017-11-22.
 */


public class User {

    private String id;

    private String username;

    @Size(min = 6, message = "password must be long than 6")
    private String password;

    @Email(message = "Not a valid email")
    private String email;

    private String phone;
    private String question;
    private String answer;
    private Integer role;
    private Date create_time;
    private Date update_time;


    public User(String id, String username, String password, String email, String phone, String question, String answer, Integer role, Date create_time, Date update_time) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.question = question;
        this.answer = answer;
        this.role = role;
        this.create_time = create_time;
        this.update_time = update_time;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", role=" + role +
                ", create_time=" + create_time +
                ", update_time=" + update_time +
                '}';
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }
}
