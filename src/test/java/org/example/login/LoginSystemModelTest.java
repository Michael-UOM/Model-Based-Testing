package org.example.login;

import junit.framework.Assert;
import nz.ac.waikato.modeljunit.Action;
import nz.ac.waikato.modeljunit.*;
import nz.ac.waikato.modeljunit.coverage.ActionCoverage;
import nz.ac.waikato.modeljunit.coverage.StateCoverage;
import nz.ac.waikato.modeljunit.coverage.TransitionPairCoverage;
import org.example.login.enums.LoginSystemStates;

import org.junit.Test;

import java.io.IOException;
import java.util.Random;

public class LoginSystemModelTest implements FsmModel{
    private LoginSystemStates modelState;

    private boolean loggedIn;
    private boolean alertsBeingViewed;

    private LoginSystem sut;

    public LoginSystemStates getState() { return modelState; }

    public void reset(final boolean b){
        modelState = LoginSystemStates.LOGGED_OUT;
        loggedIn = false;
        alertsBeingViewed = false;
        if (b) {
            sut = new LoginSystem();
        }
    }

    public boolean goodLoginGuard() {
        return getState().equals(LoginSystemStates.LOGGED_OUT);
    }
    public @Action void goodLogin() {
        sut.goodLogin();

        loggedIn = true;

        modelState = LoginSystemStates.LOGGED_IN;

        Assert.assertEquals("The model's logged in state doesn't match the SUT's state.", loggedIn, sut.isLoggedIn());
    }

    public boolean badLoginGuard() {
        return getState().equals(LoginSystemStates.LOGGED_OUT);
    }
    public @Action void badLogin() {
        sut.badLogin();

        loggedIn = false;

        modelState = LoginSystemStates.LOGGED_OUT;

        Assert.assertEquals("The model's logged in state doesn't match the SUT's state.", loggedIn, sut.isLoggedIn());
    }

    public boolean logOutGuard() {
        return getState().equals(LoginSystemStates.LOGGED_IN) || getState().equals(LoginSystemStates.VIEWING_ALERTS);
    }
    public @Action void logOut() {
        sut.logOut();

        loggedIn = false;
        alertsBeingViewed = false;

        modelState = LoginSystemStates.LOGGED_OUT;

        Assert.assertEquals("The model's logged in state doesn't match the SUT's state.", loggedIn, sut.isLoggedIn());
        Assert.assertEquals("The model's alert viewing state doesn't match the SUT's state.", alertsBeingViewed, sut.isAlertsBeingViewed());
    }

    public boolean viewAlertsGuard() {
        return getState().equals(LoginSystemStates.LOGGED_IN);
    }
    public @Action void viewAlerts() {
        sut.viewAlerts();

        alertsBeingViewed = true;

        modelState = LoginSystemStates.VIEWING_ALERTS;
        Assert.assertEquals("The model's logged in state doesn't match the SUT's state.", loggedIn, sut.isLoggedIn());
        Assert.assertEquals("The model's alert viewing state doesn't match the SUT's state.", alertsBeingViewed, sut.isAlertsBeingViewed());
    }

    @Test
    public void LoginSystemModelTestRunner() throws IOException {
        final Tester tester = new RandomTester(new LoginSystemModelTest());
        tester.setRandom(new Random());
        tester.buildGraph();
        tester.addListener(new StopOnFailureListener());
        tester.addListener("verbose");
        tester.addCoverageMetric(new TransitionPairCoverage());
        tester.addCoverageMetric(new StateCoverage());
        tester.addCoverageMetric(new ActionCoverage());
        tester.generate(250);
        tester.printCoverage();
    }
}
