package com.atrio.rollcall.model;

/**
 * Created by Arpita Patel on 17-04-2017.
 */

public class StudentUser {
    public  String status;
    public String first_name;
    public String last_name;
    public String studclass;
    public String rollno;
    public String parents_name;
    public String emailid;
    public String per_address;
    public String dob;
    public String mob;
    public String gender;
    public String section;
    public  String stud_class;

    public String getStud_class() {
        return stud_class;
    }

    public void setStud_class(String stud_class) {
        this.stud_class = stud_class;
    }

    public String getStud_sec() {
        return stud_sec;
    }

    public void setStud_sec(String stud_sec) {
        this.stud_sec = stud_sec;
    }

    public String getStud_sub() {
        return stud_sub;
    }

    public void setStud_sub(String stud_sub) {
        this.stud_sub = stud_sub;
    }

    public String stud_sec;
    public String stud_sub;




    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String date;

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }


    public StudentUser(){

    }

    public StudentUser(String first_name, String last_name, String studclass, String rollno, String pname, String emailid, String per_address, String dob, String mob, String gender,String section,String date) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.studclass = studclass;
        this.rollno = rollno;
        this.parents_name = pname;
        this.emailid = emailid;
        this.per_address = per_address;
        this.dob = dob;
        this.mob = mob;
        this.gender = gender;
        this.section = section;
        this.date = date;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getStudclass() {
        return studclass;
    }

    public void setStudclass(String studclass) {
        this.studclass = studclass;
    }

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public String getParents_name() {
        return parents_name;
    }

    public void setParents_name(String parents_name) {
        this.parents_name = parents_name;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getPer_address() {
        return per_address;
    }

    public void setPer_address(String per_address) {
        this.per_address = per_address;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


}

