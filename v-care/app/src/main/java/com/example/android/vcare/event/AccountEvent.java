package com.example.android.vcare.event;

import com.example.android.vcare.model.User;

public class AccountEvent {

    public static class OnLogin {
        private int hashCode;

        public OnLogin(int hashCode) {
            this.hashCode = hashCode;
        }

        public int getHashCode() {
            return hashCode;
        }
    }

    public static class OnFacebookLogin {
        private int hashCode;

        public OnFacebookLogin(int hashCode) {
            this.hashCode = hashCode;
        }

        public int getHashCode() {
            return hashCode;
        }
    }

//    public static class OnFacebookLoginFailed {
//        private User user;
//        private int hashCode;
//
//        public OnFacebookLoginFailed(User user, int hashCode) {
//            this.user = user;
//            this.hashCode = hashCode;
//        }
//
//        public User getUser() {
//            return user;
//        }
//
//        public int getHashCode() {
//            return hashCode;
//        }
//    }
//
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

    public static class OnGoogleLogin {
        private int hashCode;

        public OnGoogleLogin(int hashCode) {
            this.hashCode = hashCode;
        }

        public int getHashCode() {
            return hashCode;
        }
    }

//    public static class OnGoogleLoginFailed {
//        private User user;
//        private int hashCode;
//
//        public OnGoogleLoginFailed(User user, int hashCode) {
//            this.user = user;
//            this.hashCode = hashCode;
//        }
//
//        public User getUser() {
//            return user;
//        }
//
//        public int getHashCode() {
//            return hashCode;
//        }
//    }

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
        private int hashCode;

        public OnSignUp(int hashCode) {
            this.hashCode = hashCode;
        }

        public int getHashCode() {
            return hashCode;
        }
    }
}
