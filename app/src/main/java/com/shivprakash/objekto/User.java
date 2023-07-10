package com.shivprakash.objekto;

import android.graphics.Bitmap;

public class User {
    String Email;
    String Name;
    String DOB;
    User(String name,String email,String dob){
        this.Email=email;
        this.Name=name;
        this.DOB=dob;
    }

    public String getEmail() {
        return Email;
    }
    public String getName() {
        return Name;}

    public String getDob() {
        return DOB;
    }
}
