package com.atrio.rollcall.model;

/**
 * Created by Admin on 05-05-2017.
 */

public class EmailAddress {

    public EmailAddress(){

    }
    public EmailAddress(String paresnt_email){
        this.emailid = paresnt_email;

    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String emailid;
}
