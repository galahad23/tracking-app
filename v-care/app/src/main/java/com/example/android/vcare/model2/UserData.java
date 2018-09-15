package com.example.android.vcare.model2;

public class UserData {

    //private variables
    int _id;
    String _imei;
    String _name;
    String _message;
    String _email;
    String _mobile;




    // Empty constructor
    public UserData() {

    }

    // constructor
    public UserData(int id, String imei, String name, String message, String email, String mobile) {
        this._id = id;
        this._imei = imei;
        this._name = name;
        this._message = message;
        this._email = email;
        this._mobile = mobile;
    }

    // getting ID
    public int getID() {
        return this._id;
    }

    // setting id
    public void setID(int id) {
        this._id = id;
    }

    // getting imei
    public String getIMEI() {
        return this._imei;
    }

    // setting imei
    public void setIMEI(String imei) {
        this._imei = imei;
    }

    // getting name
    public String getName() {
        return this._name;
    }

    // setting name
    public void setName(String name) {
        this._name = name;
    }

    // getting Message
    public String getMessage() {
        return this._message;
    }

    // setting Message
    public void setMessage(String message) {
        this._message = message;
    }

    public String getEmail() {
        return this._email;
    }

    public void setEmail(String email) {
        this._email = email;
    }

    // getting Message
    public String getMobile() {
        return this._mobile;
    }

    // setting Message
    public void setMobile(String mobile) {
        this._mobile = mobile;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "UserInfo [name=" + _name + "]";
    }

}
