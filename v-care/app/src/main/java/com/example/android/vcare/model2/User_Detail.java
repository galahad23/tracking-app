package com.example.android.vcare.model2;

/**
 * Created by Mtoag on 3/19/2016.
 */
public class User_Detail {

    String id,name,email,address,school,last,image,mobile;

    public User_Detail(){

    }

    public User_Detail(String id, String name, String email, String address, String school, String last, String image, String mobile){
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.school = school;
        this.last = last;
        this.image = image;
        this.mobile = mobile;

    }

    public String getId (){
        return id;
    }
    public void setId(String id1){
        this.id=id1;
    }

    public String getName (){
        return name;
    }
    public void setName(String name1){
        this.name=name1;
    }

    public String getEmail (){
        return email;
    }
    public void setEmail(String email1){
        this.email=email1;
    }

    public String getaddress (){
        return address;
    }
    public void setAddress(String address1){
        this.address=address1;
    }

    public String getSchool (){
        return school;
    }
    public void setSchool(String school1){
        this.school=school1;
    }

    public String getLast (){
        return last;
    }
    public void setLast(String last1){
        this.last=last1;
    }


    public String getMobile (){
        return mobile;
    }
    public void setMobile(String mobile1){
        this.mobile=mobile1;
    }

    public String getImage (){
        return image;
    }
    public void setImage(String image1){this.image=image1;}
}