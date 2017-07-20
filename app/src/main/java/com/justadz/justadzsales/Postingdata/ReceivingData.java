package com.justadz.justadzsales.Postingdata;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.justadz.justadzsales.Adapters.AddressAdapter;
import com.justadz.justadzsales.Adapters.ConfirmedAddressAdapter;
import com.justadz.justadzsales.GetSet;
import com.justadz.justadzsales.LocationService;
import com.justadz.justadzsales.MapsActivity;
import com.justadz.justadzsales.MeetingAddresses;
import com.justadz.justadzsales.MyReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Admin on 25-May-16.
 */
public class ReceivingData {

    public void Logindetails(Context context, String result, GetSet getset) {
        String User;
        try {
            JSONObject jo = new JSONObject(result);
            if (jo != null) {
                User = jo.getString("user_role_id");
                getset.setLoginuser(User);
                if (User.equals("6")) {
                    getset.setLogin(true);
                    Intent intent = new Intent(context, LocationService.class);
                    context.startService(intent);
                } else {
                    if (User.equals("1")) {
                        getset.setLogin(true);
                    } else {
                        getset.setLoginerror(true);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void CompleteAddressdetails(String result, ArrayList<MeetingAddresses> arrayList, RecyclerView recyclerView,
                                       AddressAdapter adapter) {
        try {
            JSONArray ja = new JSONArray(result);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                if (jo != null) {
                    try {
                        String meetingid = jo.getString("meeting_id");
                        String customername = jo.getString("customer_name");
                        String customermobile = jo.getString("customer_mobile");
                        String customeraddress = jo.getString("customer_address");
                        String meetingdate = jo.getString("meeting_date");
                        String meetingtime = jo.getString("meeting_time");
                        MeetingAddresses address = new MeetingAddresses(meetingid, customername, customermobile, customeraddress,
                                meetingdate, meetingtime);
                        arrayList.add(address);
                    } catch (Exception e) {
                    }
                }
                recyclerView.setAdapter(adapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void GetMeetingDetails(String result, ArrayList<MeetingAddresses> arrayList, RecyclerView recyclerView,
                                  ConfirmedAddressAdapter adapter, GetSet getSet) {
        Log.d("debug", "Confirmed Addresses: "+result);
        if (result.equals("nullHi")) {
            getSet.setNoConfirmedAddress(true);
        } else {
            try {
                JSONArray ja = new JSONArray(result);
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    if (jo != null) {
                        try {
                            String customername = jo.getString("customer_name");
                            String customermobile = jo.getString("customer_mobile");
                            String customeraddress = jo.getString("customer_address");
                            String meetingdate = jo.getString("meeting_date");
                            String meetingtime = jo.getString("meeting_time");
                            MeetingAddresses address = new MeetingAddresses(customername, customermobile, customeraddress,
                                    meetingdate, meetingtime);
                            arrayList.add(address);
                        } catch (Exception e) {
                        }
                    }
                }
                recyclerView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void FetchSalesLocation(Context context, GoogleMap mMap, Marker marker, ArrayList<GetSet> arraylist, String result) {
        LatLng userlocation = null;
        String Username, IMEI;
        double Userlatitude, UserLongitude;
        MapsActivity maps = new MapsActivity();
        try {
            JSONArray ja = new JSONArray(result);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                if (jo != null) {
                    try {
                        GetSet getset = new GetSet();
                        Username = jo.getString("user_name");
                        getset.setFirst(Username);
                        Userlatitude = Double.parseDouble(jo.getString("user_location_latitude"));
                        UserLongitude = Double.parseDouble(jo.getString("user_location_longitude"));
                        getset.setSecond("" + UserLongitude);
                        IMEI = jo.getString("imei");
                        getset.setThird(IMEI);
                        arraylist.add(getset);
                        // Add a marker to User Location and move the camera
                        userlocation = new LatLng(Userlatitude, UserLongitude);
                        if (marker != null) {
                            marker.remove();
                        }
                        marker = mMap.addMarker(new MarkerOptions().position(userlocation).title(Username)
                                .snippet(maps.GetLocationAddress(context, Userlatitude, UserLongitude)));
                    } catch (Exception e) {
                    }
                }
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLng(userlocation));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userlocation, 12.0f));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void CheckingNotification(String result, Context context) {
        try {
            JSONObject jo = new JSONObject(result);
            if (jo != null) {
                String User = jo.getString("sales_notification_status");
                if (User.equals("1")) {
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR,
                            Calendar.MONTH,
                            Calendar.DAY_OF_MONTH,
                            Calendar.HOUR_OF_DAY,
                            Calendar.MINUTE,
                            00);
                    Intent intent = new Intent(context, MyReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void UpdatedNotification(String result, GetSet getset) {
        try {
            JSONObject jo = new JSONObject(result);
            if (jo != null) {
                String Message = jo.getString("sales_notification_message");
                if (Message.equals("Success")) {
                    getset.setNotifyupdated(true);
                }
                else {
                    getset.setNotifynotupdated(true);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
