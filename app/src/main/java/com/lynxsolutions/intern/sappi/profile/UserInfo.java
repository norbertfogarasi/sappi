package com.lynxsolutions.intern.sappi.profile;

/**
 * Created by farkaszsombor on 26.07.2017.
 */

public class UserInfo {

    private String email;
    private String name;
    private String phonenumber;
    private String photo;

    public UserInfo(){

    }

    public UserInfo(String name,String email,String phonenumber,String photo)
    {
        this.name=name;
        this.email=email;
        this.phonenumber=phonenumber;
        this.photo=photo;
    }

    public UserInfo(String email, String name, String photo) {
        this.email = email;
        this.name = name;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}