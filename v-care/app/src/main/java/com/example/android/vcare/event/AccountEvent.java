package com.example.android.vcare.event;

import com.example.android.vcare.model.User;

import java.util.List;

public class AccountEvent {

    public static class OnLogin {
        private User user;
        private int hashCode;

        public OnLogin(User user, int hashCode) {
            this.user = user;
            this.hashCode = hashCode;
        }

        public User getUser() {
            return user;
        }

        public int getHashCode() {
            return hashCode;
        }
    }
    public static class OnRegisterRequire{
        private User user;
        private int hashCode;

        public OnRegisterRequire(User user, int hashCode) {
            this.user = user;
            this.hashCode = hashCode;
        }

        public User getUser() {
            return user;
        }

        public int getHashCode() {
            return hashCode;
        }
    }

    public static class OnSocialLogin {
        private User user;
        private int hashCode;

        public OnSocialLogin(User user, int hashCode) {
            this.user = user;
            this.hashCode = hashCode;
        }

        public User getUser() {
            return user;
        }

        public int getHashCode() {
            return hashCode;
        }
    }

    public static class OnForgotPassword {
        private String message;
        private int hashCode;

        public OnForgotPassword(String message, int hashCode) {
            this.message = message;
            this.hashCode = hashCode;
        }

        public String getMessage() {
            return message;
        }

        public int getHashCode() {
            return hashCode;
        }
    }

    public static class OnSignUp {
        private User user;
        private int hashCode;

        public OnSignUp(User user, int hashCode) {
            this.user = user;
            this.hashCode = hashCode;
        }

        public User getUser() {
            return user;
        }

        public int getHashCode() {
            return hashCode;
        }
    }

    public static class OnGetProfile{
        private User user;
        private int hashCode;

        public OnGetProfile(User user, int hashCode) {
            this.user = user;
            this.hashCode = hashCode;
        }

        public User getUser() {
            return user;
        }

        public int getHashCode() {
            return hashCode;
        }
    }

    public static class OnResendOTP {
        private int hashCode;

        public OnResendOTP(int hashCode) {
            this.hashCode = hashCode;
        }

        public int getHashCode() {
            return hashCode;
        }
    }

    public static class OnSubmitOTP {
        private int hashCode;

        public OnSubmitOTP(int hashCode) {
            this.hashCode = hashCode;
        }

        public int getHashCode() {
            return hashCode;
        }
    }

    public static class OnChangePassword {
        private String message;
        private int hashCode;

        public OnChangePassword(String message, int hashCode) {
            this.message = message;
            this.hashCode = hashCode;
        }

        public String getMessage() {
            return message;
        }

        public int getHashCode() {
            return hashCode;
        }
    }

    public static class OnGetEmergencyContact{
        private List<User> emergencyContactList;
        private int hashCode;

        public OnGetEmergencyContact(List<User> emergencyContactList, int hashCode) {
            this.emergencyContactList = emergencyContactList;
            this.hashCode = hashCode;
        }

        public List<User> getEmergencyContactList() {
            return emergencyContactList;
        }

        public int getHashCode() {
            return hashCode;
        }
    }

    public static class OnUpdateEmergencyContact{
        private String message;
        private int hashCode;

        public OnUpdateEmergencyContact(String message, int hashCode) {
            this.message = message;
            this.hashCode = hashCode;
        }

        public String getMessage() {
            return message;
        }

        public int getHashCode() {
            return hashCode;
        }
    }
}
