package com.example.signuploginrealtime;

public class Addorganizer {

    String firstname, lastname, email, password, bussiness;

    public String getfirstname() {
        return firstname;
    }

    public void setfirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getlastname() {
        return lastname;
    }

    public void setlastname(String lastname) {
        this.lastname = lastname;
    }

    public String getemail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setbussiness(String bussiness) {
        this.bussiness = bussiness;
    }


    public String getbussiness() {
        return bussiness;
    }




    public Addorganizer(String firstname, String lastname, String email, String password, String bussiness) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.bussiness = bussiness;
    }

    public Addorganizer() {
    }
}
