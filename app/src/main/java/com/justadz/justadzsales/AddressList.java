package com.justadz.justadzsales;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import com.justadz.justadzsales.Adapters.ConfirmedAddressAdapter;
import com.justadz.justadzsales.Postingdata.ConnectingTask;
import com.justadz.justadzsales.Postingdata.ConnectingTask.UpdateNotify;
import com.justadz.justadzsales.Postingdata.ConnectingTask.FetchMeetingAddresses;

public class AddressList extends AppCompatActivity {
    TextView tvconfirmedTxt;
    ArrayList<MeetingAddresses> addresslist;
    RecyclerView addresses;
    ConfirmedAddressAdapter addressadapter;
    RecyclerView.LayoutManager layoutmanager;
    String IMEI, Notify = "";
    Adapters adapters;
    ConnectingTask task;
    GetSet getSet;
    Thread mythread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);

        adapters = new Adapters();
        task = new ConnectingTask();
        getSet = new GetSet();

        tvconfirmedTxt = (TextView) findViewById(R.id.confirmed_Txt);
        addresses = (RecyclerView) findViewById(R.id.meetingaddress);
        addresslist = new ArrayList<MeetingAddresses>();
        layoutmanager = new LinearLayoutManager(this);
        addressadapter = adapters.new ConfirmedAddressAdapter(addresslist, this);

        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        IMEI = tm.getDeviceId();

        addresses.setHasFixedSize(true);
        addresses.setLayoutManager(layoutmanager);
        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider);
        addresses.addItemDecoration(adapters.new DividerItemDecoration(dividerDrawable));

        Intent in = getIntent();
        if (in.getExtras() != null) {
            Bundle bnd = in.getExtras();
            Notify = bnd.getString("NotifyUpdate");
        }

        if (isInternetOn()) {
            FetchMeetingAddresses meetingAddresses = task.new FetchMeetingAddresses(IMEI, addresslist,
                    addresses, addressadapter, getSet);
            meetingAddresses.execute();
            mythread = null;
            Runnable runnable = new CountDownRunner();
            mythread= new Thread(runnable);
            mythread.start();
            if (Notify.equals("update")) {
                UpdateNotify updateNotify = task.new UpdateNotify(IMEI, getSet, "0");
                updateNotify.execute();
            }
        } else {
            Toast.makeText(AddressList.this, "Please switch on the Internet", Toast.LENGTH_SHORT).show();
        }
    }

    public final boolean isInternetOn() {
        ConnectivityManager connect = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        if (connect.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
                connect.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
                connect.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ||
                connect.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        } else if (connect.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||
                connect.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }

    class CountDownRunner implements Runnable{
        // @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    doWork();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception e){
                }
            }
        }
    }

    public void doWork() {
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    if (getSet.isNoConfirmedAddress()) {
                        getSet.setNoConfirmedAddress(false);
                        mythread.interrupt();
                        tvconfirmedTxt.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {}
            }
        });
    }
}
