package com.justadz.justadzsales;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.justadz.justadzsales.Postingdata.ConnectingTask;
import com.justadz.justadzsales.Postingdata.ConnectingTask.PostLoginData;
import com.justadz.justadzsales.Postingdata.ReceivingData;

public class MainActivity extends AppCompatActivity {
    EditText etmobile, etpassword;
    Button loginbtn, logoutbtn, notifybtn;
    CheckBox cbpassword;
    String IMEI, Mobile = "", Password = "", User;
    // Declaring a Location Manager
    protected LocationManager locationManager;
    ConnectingTask task;
    Thread mythread;
    ReceivingData receivingData;
    ProgressDialog progressDialog;
    GetSet getset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginbtn = (Button) findViewById(R.id.loginbtn);
        logoutbtn = (Button) findViewById(R.id.logoutbtn);
        notifybtn = (Button) findViewById(R.id.notifybtn);
        etmobile = (EditText) findViewById(R.id.mobile);
        etpassword = (EditText) findViewById(R.id.password);
        cbpassword = (CheckBox) findViewById(R.id.passwordbox);

        task = new ConnectingTask();
        receivingData = new ReceivingData();
        getset = new GetSet();

        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        IMEI = tm.getDeviceId();

        cbpassword.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    etpassword.setSelection(etpassword.getText().length());
                }
                else {
                    etpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etpassword.setSelection(etpassword.getText().length());
                }
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mobile = etmobile.getText().toString();
                if (!Mobile.equals("")) {
                    if (Mobile.length() == 10) {
                        Password = etpassword.getText().toString();
                        if (!Password.equals("")) {
                            if (isInternetOn()) {
                                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                                if (checkGPStatus(locationManager)) {
                                    PostLoginData loginData = task.new PostLoginData(MainActivity.this, IMEI, Mobile,
                                            Password, getset);
                                    loginData.execute();
                                    checkkeyboard();
                                    etmobile.setText("");
                                    etpassword.setText("");
                                    etmobile.requestFocus();
                                    progressDialog = new ProgressDialog(MainActivity.this,
                                            R.style.AppTheme_Dark_Dialog);
                                    progressDialog.setIndeterminate(true);
                                    progressDialog.setMessage("Authenticating...");
                                    progressDialog.show();
                                    mythread = null;
                                    Runnable runnable = new CountDownRunner();
                                    mythread= new Thread(runnable);
                                    mythread.start();
                                } else {
                                    showSettingsAlert();
                                }
                            } else {
                                showsnackbar(v, "Turn On the Internet");
                            }
                        } else {
                            showsnackbar(v, "Enter Password");
                        }
                    } else {
                        etmobile.setError("Check Number");
                    }
                } else {
                    showsnackbar(v, "Enter Mobile Number");
                }
            }
        });

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "Stopped Location Service", Toast.LENGTH_SHORT).show();
                Intent in = new Intent(MainActivity.this, LocationService.class);
                stopService(in);
                //mythread.interrupt();
                canceltimer();
            }
        });

        notifybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNotification();
            }
        });
    }

    public void showNotification() {
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, MapsActivity.class), 0);
        //build notification
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                        .setContentTitle("Ping Notification")
                        .setContentText("Tomorrow will be your birthday.")
                        .setDefaults(Notification.DEFAULT_ALL) // must requires VIBRATE permission
                        .setPriority(NotificationCompat.PRIORITY_HIGH) //must give priority to High,
                        // Max which will considered as heads-up notification
                        .setContentIntent(pi)
                        .setAutoCancel(true);

        // Gets an instance of the NotificationManager service
                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //to post your notification to the notification bar with a id. If a notification with same id already exists,
        // it will get replaced with updated information.
                notificationManager.notify(0, builder.build());
    }

    public void showsnackbar(View view, String data) {
        Snackbar.make(view, data, Snackbar.LENGTH_LONG).show();
    }

    public void checkNotification(Context context) {
        Intent intent = new Intent(context, LocationFinder.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        // Schedule the alarm!
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), (1000 * 60 * 1), sender);
    }

    public void canceltimer() {
        Context context = getBaseContext();
        Intent intent = new Intent(context, LocationFinder.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(sender);
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

    private void checkkeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Function to show settings alert dialog
     * */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     * */
    /*public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(MainActivity.this);
        }
    }*/

    public boolean checkGPStatus(LocationManager locManager) {
        locationManager = locManager;
        // getting GPS status
        return locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
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
                    if (getset.isLogin()) {
                        progressDialog.dismiss();
                        getset.setLogin(false);
                        User = getset.getLoginuser();
                        if (User.equals("1")) {
                            mythread.interrupt();
                            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (User.equals("6")) {
                            mythread.interrupt();
                            checkNotification(MainActivity.this);
                            Intent intent = new Intent(MainActivity.this, AddressList.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    if (getset.isLoginerror()) {
                        progressDialog.dismiss();
                        getset.setLoginerror(false);
                        mythread.interrupt();
                        Toast.makeText(MainActivity.this, "Password for Mobile number is not matching.. " +
                                "Please check it..", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {}
            }
        });
    }
}