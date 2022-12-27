package org.example.login;

public class LoginSystem {
    private boolean loggedIn = false;
    private boolean alertsBeingViewed = false;

    boolean isLoggedIn(){
        return loggedIn;
    }

    boolean isAlertsBeingViewed(){
        return alertsBeingViewed;
    }

    void goodLogin() {
        if (!loggedIn){
            loggedIn = true;
        }
    }

    void viewAlerts() {
        if (loggedIn) {
            alertsBeingViewed = true;
        }
    }

    void badLogin(){
        if (!loggedIn){
            loggedIn = false;
        }
    }

    void logOut() {
        if (loggedIn) {
            loggedIn = false;
            alertsBeingViewed = false;
        }
    }
}
