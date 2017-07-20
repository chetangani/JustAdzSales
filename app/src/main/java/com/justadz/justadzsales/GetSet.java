package com.justadz.justadzsales;

/**
 * Created by Admin on 24-May-16.
 */
public class GetSet {
    String first, second, third, Loginuser;
    boolean login, loginerror, confirmedsuccess, noConfirmedAddress, notifyupdated, notifynotupdated;

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getThird() {
        return third;
    }

    public void setThird(String third) {
        this.third = third;
    }

    public String getLoginuser() {
        return Loginuser;
    }

    public void setLoginuser(String loginuser) {
        this.Loginuser = loginuser;
    }

    public boolean isConfirmedsuccess() {
        return confirmedsuccess;
    }

    public void setConfirmedsuccess(boolean confirmedsuccess) {
        this.confirmedsuccess = confirmedsuccess;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public boolean isLoginerror() {
        return loginerror;
    }

    public void setLoginerror(boolean loginerror) {
        this.loginerror = loginerror;
    }

    public boolean isNoConfirmedAddress() {
        return noConfirmedAddress;
    }

    public void setNoConfirmedAddress(boolean noConfirmedAddress) {
        this.noConfirmedAddress = noConfirmedAddress;
    }

    public boolean isNotifyupdated() {
        return notifyupdated;
    }

    public void setNotifyupdated(boolean notifyupdated) {
        this.notifyupdated = notifyupdated;
    }

    public boolean isNotifynotupdated() {
        return notifynotupdated;
    }

    public void setNotifynotupdated(boolean notifynotupdated) {
        this.notifynotupdated = notifynotupdated;
    }
}
