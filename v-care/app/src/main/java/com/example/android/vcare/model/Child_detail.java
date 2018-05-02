package com.example.android.vcare.model;

import java.io.Serializable;

/**
 * Created by Mtoag on 5/31/2016.
 */
public class Child_detail implements Serializable {
    private static final long serialVersionUID = 1L;
    String id,name,parent_name,email,country_name,phone_number,profile_image;

    private boolean isSelected;

    public Child_detail(){

    }

    public Child_detail(String id, String name, String parent_name, boolean isSelected,
                        String email,String country_name, String phone_number,String profile_image) {
        this.id = id;
        this.name = name;
        this.parent_name = parent_name;
        this.isSelected = isSelected;
        this.email = email;
        this.country_name = country_name;
        this.phone_number = phone_number;
        this.profile_image = profile_image;
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

    public String getParent_name (){
        return parent_name;
    }
    public void setParent_name(String parent_name1){
        this.parent_name=parent_name1;
    }



    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }
}
