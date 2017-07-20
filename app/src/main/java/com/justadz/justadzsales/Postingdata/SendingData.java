package com.justadz.justadzsales.Postingdata;


import com.justadz.justadzsales.MeetingAddresses;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 25-May-16.
 */
public class SendingData {

    // this will post our text data
    public String postText(String IMEI, String Mobile, String Password){
        String responseStr = "";
        try{
            // url where the data will be posted
            String postReceiverUrl = "http://www.justadz.com/index.php/Check_login_api";

            // HttpClient
            HttpClient httpClient = new DefaultHttpClient();

            // post header
            HttpPost httpPost = new HttpPost(postReceiverUrl);

            // add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("user_mobile", Mobile));
            nameValuePairs.add(new BasicNameValuePair("user_password", Password));
            nameValuePairs.add(new BasicNameValuePair("imei", IMEI));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            // execute HTTP post request
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                responseStr = EntityUtils.toString(resEntity).trim();
                // you can add an if statement here and do other actions based on the response
            }
            else {

            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseStr;
    }

    // this will post our text data
    public void postlocation(double mLatitude, double mLongitude, String Currenttime, String IMEI){
        String responseStr = "";
        try{
            // url where the data will be posted
            String postReceiverUrl = "http://www.justadz.com/index.php/Insert_user_location";

            // HttpClient
            HttpClient httpClient = new DefaultHttpClient();

            // post header
            HttpPost httpPost = new HttpPost(postReceiverUrl);
            // add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("user_location_latitude", ""+mLatitude));
            nameValuePairs.add(new BasicNameValuePair("user_location_longitude", ""+mLongitude));
            nameValuePairs.add(new BasicNameValuePair("user_location_time", Currenttime));
            nameValuePairs.add(new BasicNameValuePair("imei", IMEI));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            // execute HTTP post request
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                responseStr = EntityUtils.toString(resEntity).trim();
                // you can add an if statement here and do other actions based on the response
            }
            else {

            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String GetAddresses(){
        String responseStr = "";
        try{
            // url where the data will be posted
            String postReceiverUrl = "http://www.justadz.com/index.php/Fetch_meeting_address";

            // HttpClient
            HttpClient httpClient = new DefaultHttpClient();

            // post header
            HttpPost httpPost = new HttpPost(postReceiverUrl);
            // execute HTTP post request
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                responseStr = EntityUtils.toString(resEntity).trim();
                // you can add an if statement here and do other actions based on the response
            }
            else {

            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseStr;
    }

    public String ConfirmAddresses(ArrayList<MeetingAddresses> MeetingID, String UserIMEI){
        String responseStr = "";
        try{
            // url where the data will be posted
            String postReceiverUrl = "http://www.justadz.com/index.php/Insert_meeting_details";

            // HttpClient
            HttpClient httpClient = new DefaultHttpClient();

            // post header
            HttpPost httpPost = new HttpPost(postReceiverUrl);
            HttpResponse response = null;
            // add your data
            for (int i = 0; i < MeetingID.size(); i++) {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("meeting_id", MeetingID.get(i).getMeetingid()));
                nameValuePairs.add(new BasicNameValuePair("imei", UserIMEI));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                // execute HTTP post request
                response = httpClient.execute(httpPost);
            }
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                responseStr = EntityUtils.toString(resEntity).trim();
                // you can add an if statement here and do other actions based on the response
            }
            else {

            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseStr;
    }

    public String GetMeetingAddresses(String IMEI){
        String responseStr = "";
        try{
            // url where the data will be posted
            String postReceiverUrl = "http://www.justadz.com/index.php/Fetch_meeting_status";

            // HttpClient
            HttpClient httpClient = new DefaultHttpClient();

            // post header
            HttpPost httpPost = new HttpPost(postReceiverUrl);

            // add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(0);
            nameValuePairs.add(new BasicNameValuePair("imei", IMEI));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // execute HTTP post request
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                responseStr = EntityUtils.toString(resEntity).trim();
                // you can add an if statement here and do other actions based on the response
            }
            else {

            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseStr;
    }

    public String Getlocations(){
        String responseStr = "";
        try{
            // url where the data will be posted
            String postReceiverUrl = "http://www.justadz.com/index.php/Fetch_location";

            // HttpClient
            HttpClient httpClient = new DefaultHttpClient();

            // post header
            HttpPost httpPost = new HttpPost(postReceiverUrl);
            // execute HTTP post request
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                responseStr = EntityUtils.toString(resEntity).trim();
                // you can add an if statement here and do other actions based on the response
            }
            else {

            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseStr;
    }

    public String CheckNotify(String IMEI){
        String responseStr = "";
        try{
            // url where the data will be posted
            String postReceiverUrl = "http://www.justadz.com/index.php/Check_user_notification";

            // HttpClient
            HttpClient httpClient = new DefaultHttpClient();

            // post header
            HttpPost httpPost = new HttpPost(postReceiverUrl);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(0);
            nameValuePairs.add(new BasicNameValuePair("imei", IMEI));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            // execute HTTP post request
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                responseStr = EntityUtils.toString(resEntity).trim();
                // you can add an if statement here and do other actions based on the response
            }
            else {

            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseStr;
    }

    public String UpdateNotifyStatus(String IMEI, String status){
        String responseStr = "";
        try{
            // url where the data will be posted
            String postReceiverUrl = "http://www.justadz.com/index.php/Insert_notification_details";

            // HttpClient
            HttpClient httpClient = new DefaultHttpClient();

            // post header
            HttpPost httpPost = new HttpPost(postReceiverUrl);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("imei", IMEI));
            nameValuePairs.add(new BasicNameValuePair("sales_notification_status", status));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            // execute HTTP post request
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                responseStr = EntityUtils.toString(resEntity).trim();
                // you can add an if statement here and do other actions based on the response
            }
            else {

            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseStr;
    }
}
