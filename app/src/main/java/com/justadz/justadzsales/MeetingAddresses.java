package com.justadz.justadzsales;

/**
 * Created by Chetan G on 5/25/2016.
 */
public class MeetingAddresses {
    String Meetingid, CustomerName, CustomerMobile, CustomerAddress, MeetingDate, MeetingTime;
    boolean isSelected;

    public MeetingAddresses() {
    }

    public MeetingAddresses(String meetingid, String customerName, String customerMobile,
                            String customerAddress, String meetingDate, String meetingTime) {
        Meetingid = meetingid;
        CustomerName = customerName;
        CustomerMobile = customerMobile;
        CustomerAddress = customerAddress;
        MeetingDate = meetingDate;
        MeetingTime = meetingTime;
    }

    public String getMeetingid() {
        return Meetingid;
    }

    public void setMeetingid(String meetingid) {
        Meetingid = meetingid;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getCustomerMobile() {
        return CustomerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        CustomerMobile = customerMobile;
    }

    public String getCustomerAddress() {
        return CustomerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        CustomerAddress = customerAddress;
    }

    public String getMeetingDate() {
        return MeetingDate;
    }

    public void setMeetingDate(String meetingDate) {
        MeetingDate = meetingDate;
    }

    public String getMeetingTime() {
        return MeetingTime;
    }

    public void setMeetingTime(String meetingTime) {
        MeetingTime = meetingTime;
    }

    public MeetingAddresses(String customerName, String customerMobile, String customerAddress, String meetingDate,
                            String meetingTime) {
        CustomerName = customerName;
        CustomerMobile = customerMobile;
        CustomerAddress = customerAddress;
        MeetingDate = meetingDate;
        MeetingTime = meetingTime;
    }

    public MeetingAddresses(String meetingid, String customerName, String customerMobile,
                            String customerAddress, String meetingDate, String meetingTime, boolean isSelected) {
        Meetingid = meetingid;
        CustomerName = customerName;
        CustomerMobile = customerMobile;
        CustomerAddress = customerAddress;
        MeetingDate = meetingDate;
        MeetingTime = meetingTime;
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
