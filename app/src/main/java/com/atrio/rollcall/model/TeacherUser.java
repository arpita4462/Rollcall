package com.atrio.rollcall.model;

/**
 * Created by Admin on 06-04-2017.
 */

public class TeacherUser {
   public String emp_ID,name,email_id,mobile,password,gender;

   public TeacherUser(String emp_ID, String name, String email_id, String mobile, String password, String gender) {
        this.emp_ID = emp_ID;
        this.name = name;
        this.email_id = email_id;
        this.mobile = mobile;
        this.password = password;
        this.gender = gender;
    }

    public TeacherUser() {

    }

    public String getEmp_ID() {
        return emp_ID;
    }

    public void setEmp_ID(String emp_ID) {
        this.emp_ID = emp_ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
