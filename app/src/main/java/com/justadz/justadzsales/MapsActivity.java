package com.justadz.justadzsales;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.justadz.justadzsales.Adapters.AddressAdapter;
import com.justadz.justadzsales.Postingdata.ConnectingTask;
import com.justadz.justadzsales.Postingdata.ConnectingTask.FetchLocationData;
import com.justadz.justadzsales.Postingdata.ConnectingTask.FetchAddresses;
import com.justadz.justadzsales.Postingdata.ConnectingTask.ConfirmedAddresses;
import com.justadz.justadzsales.Postingdata.ConnectingTask.UpdateNotify;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int SALES_DLG = 1;
    private static final int ADDRESS_DLG = 2;
    private GoogleMap mMap;
    String Userselected, UserIMEI, MeetingID;
    ArrayList<GetSet> arraylist;
    GetSet getset;
    ArrayList<MeetingAddresses> addresslist;
    RecyclerView addresses;
    AddressAdapter addressadapter;
    RecyclerView.LayoutManager layoutmanager;
    Adapters adapter;
    ConnectingTask task;
    Thread mythread, refreshthread;
    boolean mapready = false;
    Marker marker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        arraylist = new ArrayList<GetSet>();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        refreshthread = null;
        adapter = new Adapters();
        task = new ConnectingTask();
        getset = new GetSet();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (isInternetOn()) {
            mMap = googleMap;
            mapready = true;
            clickonmarker();
        }
        else {
            Toast.makeText(MapsActivity.this, "Please switch on the Internet", Toast.LENGTH_SHORT).show();
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

    private void clickonmarker() {
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Userselected = marker.getTitle();
                UserIMEI = getIMEI(Userselected);
                ShowDialog(SALES_DLG);
            }
        });
    }

    public String getIMEI(String sales) {
        for (int i = 0; i < arraylist.size(); i++) {
            GetSet getset = arraylist.get(i);
            if (sales.equals(getset.getFirst())) {
                return getset.getThird();
            }
        }
        return null;
    }

    @Override
    protected void onStart() {
        refreshthread = null;
        Runnable runnable = new MapsLocationUpdates();
        refreshthread= new Thread(runnable);
        refreshthread.start();
        super.onStart();
    }

    @Override
    protected void onResume() {
        if (refreshthread.isInterrupted()) {
            refreshthread = null;
            Runnable runnable = new MapsLocationUpdates();
            refreshthread= new Thread(runnable);
            refreshthread.start();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    protected void ShowDialog(int id) {
        Dialog dialog = null;
        switch (id) {
            case SALES_DLG:
                AlertDialog.Builder salesdlg = new AlertDialog.Builder(MapsActivity.this);
                salesdlg.setTitle("Meeting Event");
                salesdlg.setMessage("Need to set Meeting Event to "+Userselected+" ..??");
                salesdlg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ShowDialog(ADDRESS_DLG);
                    }
                });
                
                salesdlg.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog = salesdlg.create();
                dialog.show();
                break;

            case ADDRESS_DLG:
                AlertDialog.Builder addressdlg = new AlertDialog.Builder(MapsActivity.this);
                addressdlg.setTitle("Addresses");
                RelativeLayout rl = (RelativeLayout) getLayoutInflater().inflate(R.layout.address_list, null);
                addresses = (RecyclerView) rl.findViewById(R.id.meetingaddress);
                addresslist = new ArrayList<MeetingAddresses>();
                layoutmanager = new LinearLayoutManager(this);
                addressadapter = adapter.new AddressAdapter(addresslist, this);
                addresses.setHasFixedSize(true);
                addresses.setLayoutManager(layoutmanager);
                Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider);
                addresses.addItemDecoration(adapter.new DividerItemDecoration(dividerDrawable));
                addressdlg.setView(rl);
                /*addresses.setAdapter(addressadapter);*/
                FetchAddresses fetchAddresses = task.new FetchAddresses(addresslist, addresses, addressadapter);
                fetchAddresses.execute();
                addressdlg.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<MeetingAddresses> confirmedarray = new ArrayList<MeetingAddresses>();
                        ArrayList<MeetingAddresses> array = addressadapter.getMeetingid();
                        for (int i = 0; i < array.size(); i++) {
                            MeetingAddresses address = array.get(i);
                            if (address.isSelected() == true) {
                                MeetingID = address.getMeetingid().toString();
                                MeetingAddresses confirmed = new MeetingAddresses();
                                confirmed.setMeetingid(MeetingID);
                                confirmedarray.add(confirmed);
                            }
                        }
                        ConfirmedAddresses confirmed = task.new ConfirmedAddresses(confirmedarray, UserIMEI, getset);
                        confirmed.execute();
                        mythread = null;
                        Runnable runnable = new CountDownRunner();
                        mythread= new Thread(runnable);
                        mythread.start();
                    }
                });
                addressdlg.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog = addressdlg.create();
                dialog.show();
                break;
        }
    }

    public String GetLocationAddress(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        // Get the current location from the input parameter list
        // Create a list to contain the result address
        List<Address> addresses = null;
        try {
                /*
                 * Return 1 address.
                 */
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e1) {
            e1.printStackTrace();
            return ("IO Exception trying to get address:" + e1);
        } catch (IllegalArgumentException e2) {
            // Error message to post in the log
            String errorString = "Illegal arguments "
                    + Double.toString(latitude) + " , "
                    + Double.toString(longitude)
                    + " passed to address service";
            e2.printStackTrace();
            return errorString;
        }
        // If the reverse geocode returned an address
        if (addresses != null && addresses.size() > 0) {
            // Get the first address
            Address address = addresses.get(0);
                /*
                 * Format the first line of address (if available), city, and
                 * country name.
                 */
            String addressText = String.format(
                    "%s, %s, %s",
                    // If there's a street address, add it
                    address.getMaxAddressLineIndex() > 0 ?
                            address.getAddressLine(1) : "",
                    //        address.getAddressLine(1),
                    address.getAddressLine(2),
                    address.getAddressLine(3),
                    address.getAddressLine(4),
                    address.getLocality(),
                    address.getCountryName());
            // Return the text
            return addressText;
        } else {
            return "No address found by the service: Note to the developers, If no address is found by google itself, there is nothing you can do about it. :(";
        }
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
                    if (getset.isConfirmedsuccess()) {
                        getset.setConfirmedsuccess(false);
                        mythread.interrupt();
                        UpdateNotify updateNotify = task.new UpdateNotify(UserIMEI, getset, "1");
                        updateNotify.execute();
                        mythread = null;
                        Runnable runnable = new CountDownRunner();
                        mythread= new Thread(runnable);
                        mythread.start();
                    }
                    if (getset.isNotifyupdated()) {
                        getset.setNotifyupdated(false);
                        mythread.interrupt();
                        Toast.makeText(MapsActivity.this, Userselected + " updated", Toast.LENGTH_SHORT).show();
                    }
                    if (getset.isNotifynotupdated()) {
                        getset.setNotifynotupdated(false);
                        UpdateNotify updateNotify = task.new UpdateNotify(UserIMEI, getset, "1");
                        updateNotify.execute();
                    }
                } catch (Exception e) {}
            }
        });
    }

    class MapsLocationUpdates implements Runnable{
        // @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    DataRunner();
                    Thread.sleep(1000 * 30 * 3);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception e){
                }
            }
        }
    }

    public void DataRunner() {
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    if (mapready) {
                        FetchLocationData fetchLocationData = task.new FetchLocationData(MapsActivity.this, mMap, marker, arraylist);
                        fetchLocationData.execute();
                    }
                } catch (Exception e) {}
            }
        });
    }

}
