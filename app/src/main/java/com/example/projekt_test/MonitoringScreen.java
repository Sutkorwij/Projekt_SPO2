package com.example.projekt_test;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.time.LocalDate;
import java.util.UUID;

public class MonitoringScreen extends FragmentActivity {

    private static final String TAG = "BlueTest5-MainActivity";

    //region BLUETOOTH
    private UUID mDeviceUUID;
    private BluetoothSocket mBTSocket;
    //private ReadInput mReadThread = null;
    private static final String BT_TAG = "BT_Monitoring";
    private final boolean mIsUserInitiatedDisconnect = false;
    private final boolean mIsBluetoothConnected = false;
    private BluetoothDevice mDevice;
    private ProgressDialog progressDialog;
    //endregion

    private final int BPM = 0;
    private final int SPO2 = 0;
    private final String[] temp = new String[4];

    private DatabaseHelper db;

    private int User_ID = -1;
    private int Date_ID = -1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring_screen);
        ActivityHelper.initialize(this);

        //region pages
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        String[] tabTitles = {"BPM", "SPO2", "Settings", "BT LOG"};
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        PagerAdapter pagerAdapter = new PagerAdapter(this, tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(tabTitles[position])
        ).attach();
        //endregion

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        mDevice = b.getParcelable(MainActivity.DEVICE_EXTRA);
        mDeviceUUID = UUID.fromString(b.getString(MainActivity.DEVICE_UUID));
        Log.d(TAG, "Ready");

        db = new DatabaseHelper(this);

        Cursor res = db.getIDAndNameOfSelectedUser();
        while(res.moveToNext()){
            User_ID = Integer.parseInt(res.getString(0));
        }
        LocalDate localDate = LocalDate.now();
        res = db.checkExistenceOfUserAndDate(String.valueOf(User_ID), localDate.toString());
        if(res != null){
            while (res.moveToNext()){
                Date_ID = Integer.parseInt(res.getString(0));
            }
        }
        else{
            db.insertDate(localDate.toString(), String.valueOf(User_ID));
            Date_ID = Integer.parseInt(db.maxDateID());
        }
    }

}