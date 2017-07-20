package com.justadz.justadzsales.Postingdata;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.justadz.justadzsales.Adapters.AddressAdapter;
import com.justadz.justadzsales.Adapters.ConfirmedAddressAdapter;
import com.justadz.justadzsales.GetSet;
import com.justadz.justadzsales.MeetingAddresses;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Admin on 25-May-16.
 */
public class ConnectingTask {
    SendingData sendingData = new SendingData();
    ReceivingData receivingData = new ReceivingData();

    public class PostLoginData extends AsyncTask<String, String, String> {
        String result = "", IMEI, Mobile, Password;
        Context context;
        GetSet getset;

        public PostLoginData(Context context, String imei, String mobile, String password, GetSet getSet) {
            this.context = context;
            this.IMEI = imei;
            this.Mobile = mobile;
            this.Password = password;
            this.getset = getSet;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            // do stuff before posting data
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                result = sendingData.postText(IMEI, Mobile, Password);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.Logindetails(context, result, getset);
        }
    }

    public class PostLocationData extends AsyncTask<String, String, String> {
        double latitude, longitude;
        String currenttime, IMEI;

        public PostLocationData(double latitude, double longitude, String currenttime, String IMEI) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.currenttime = currenttime;
            this.IMEI = IMEI;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            // do stuff before posting data
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                sendingData.postlocation(latitude, longitude, currenttime, IMEI);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }

    public class FetchAddresses extends AsyncTask<String, String, String> {
        String result = "";
        ArrayList<MeetingAddresses> arrayList;
        RecyclerView recyclerView;
        AddressAdapter adapter;

        public FetchAddresses(ArrayList<MeetingAddresses> arrayList, RecyclerView recyclerView, AddressAdapter adapter) {
            this.arrayList = arrayList;
            this.recyclerView = recyclerView;
            this.adapter = adapter;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            // do stuff before posting data
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                result = sendingData.GetAddresses();

            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.CompleteAddressdetails(result, arrayList, recyclerView, adapter);
        }
    }

    public class ConfirmedAddresses extends AsyncTask<String, String, String> {
        String result = "", UserIMEI;
        ArrayList<MeetingAddresses> MeetingID = new ArrayList<MeetingAddresses>();
        GetSet getset;

        public ConfirmedAddresses(ArrayList<MeetingAddresses> meetingID, String userIMEI, GetSet getSet) {
            this.MeetingID = meetingID;
            this.UserIMEI = userIMEI;
            this.getset = getSet;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            // do stuff before posting data
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                result = sendingData.ConfirmAddresses(MeetingID, UserIMEI);

            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("debug", "ConfirmedAddresses: "+result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jo = new JSONObject(result);
                if (jo != null) {
                    String data = jo.getString("insert_meeting_message");
                    if (data.equals("Success")) {
                        getset.setConfirmedsuccess(true);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class FetchMeetingAddresses extends AsyncTask<String, String, String> {
        ArrayList<MeetingAddresses> arrayList;
        String result = "", IMEI;
        RecyclerView recyclerView;
        ConfirmedAddressAdapter adapter;
        GetSet getset;

        public FetchMeetingAddresses(String IMEI, ArrayList<MeetingAddresses> arrayList,
                                     RecyclerView recyclerView, ConfirmedAddressAdapter adapter, GetSet getSet) {
            this.IMEI = IMEI;
            this.arrayList = arrayList;
            this.recyclerView = recyclerView;
            this.adapter = adapter;
            this.getset = getSet;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                result = sendingData.GetMeetingAddresses(IMEI);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.GetMeetingDetails(result, arrayList, recyclerView, adapter, getset);
        }
    }

    public class FetchLocationData extends AsyncTask<String, String, String> {
        String result = "";
        Context context;
        GoogleMap mMap;
        Marker marker;
        ArrayList<GetSet> arrayList;

        public FetchLocationData(Context context, GoogleMap mMap, Marker marker, ArrayList<GetSet> arrayList) {
            this.context = context;
            this.mMap = mMap;
            this.marker = marker;
            this.arrayList = arrayList;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            // do stuff before posting data
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                result = sendingData.Getlocations();

            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.FetchSalesLocation(context, mMap, marker, arrayList, result);
        }
    }

    public class CheckNotify extends AsyncTask<String, String, String> {
        String result = "", IMEI;
        Context context;

        public CheckNotify(String IMEI, Context context) {
            this.IMEI = IMEI;
            this.context = context;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            // do stuff before posting data
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                result = sendingData.CheckNotify(IMEI);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.CheckingNotification(result, context);
        }
    }

    public class UpdateNotify extends AsyncTask<String, String, String> {
        String result = "", IMEI, notifystatus;
        GetSet getset;

        public UpdateNotify(String IMEI, GetSet getset, String Status) {
            this.IMEI = IMEI;
            this.getset = getset;
            this.notifystatus = Status;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            // do stuff before posting data
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                result = sendingData.UpdateNotifyStatus(IMEI, notifystatus);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.UpdatedNotification(result, getset);
        }
    }
}
