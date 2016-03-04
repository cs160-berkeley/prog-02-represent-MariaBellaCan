package com.example.mariacan.campaignupdates2016;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements SensorEventListener{

    private static final float SHAKE = 14;
    SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        if (getIntent().getByteArrayExtra("zipcode") == null){
            return;
        }
        VoteViewFragment.zipCode = new String(getIntent().getByteArrayExtra("zipcode"));
        CongressPersonAdapter congressPersonAdapter = new CongressPersonAdapter(getSupportFragmentManager());
        viewPager.setAdapter(congressPersonAdapter);

        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onPause(){
        super.onPause();
        if (this.sensorManager == null){
            return;
        }
        this.sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        double distance = Math.sqrt(x*x + y*y + z*z);

        if (distance > SHAKE){
            Message.messageSend("shake", null, this);
            finish();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private class CongressPersonAdapter extends FragmentPagerAdapter {

        Fragment[] fragments = new Fragment[4];

        private CongressPersonAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            fragments[0] = new CongressFragment();
            fragments[1] = new CongressFragment();
            fragments[2] = new CongressFragment();
            fragments[3] = new VoteViewFragment();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }


    }

    public static class CongressFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_congress_person_view, container, false);

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Message.messageSend("detailedView", "message".getBytes(), getActivity());
                }
            });
            return rootView;

        }

    }

    public static class VoteViewFragment extends Fragment {

        public static String zipCode;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_vote_view, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.city_name);
            String cityName;
            if (zipCode.equals("91741")){
                cityName = "Los Angeles";
            } else if (zipCode.equals("11111")){
                cityName = "San Diego";
            } else{
                cityName = "Reno";
            }
            textView.setText(cityName);
            return rootView;
        }
    }
}
