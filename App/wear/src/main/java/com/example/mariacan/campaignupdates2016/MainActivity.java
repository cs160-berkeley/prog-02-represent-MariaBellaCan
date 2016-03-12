package com.example.mariacan.campaignupdates2016;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements SensorEventListener {

    private static final float SHAKE = 14;
    SensorManager sensorManager;
    static ArrayList<String> names;
    static ArrayList<String> parties;
    static ArrayList<String> titles;
    static int count;
    static String state;
    static String county;
    static String obama;
    static String romney;
    static ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.viewPager = (ViewPager) findViewById(R.id.viewPager);
        if (getIntent().getByteArrayExtra("congressionalInfo") == null){
            return;
        }
        names = new ArrayList<>();
        parties = new ArrayList<>();
        titles = new ArrayList<>();
        String data = new String(getIntent().getByteArrayExtra("congressionalInfo"));
        System.out.println("DATA IS " + data);
        this.populateCongressionalInfo(data);
        //VoteViewFragment.zipCode = new String(getIntent().getByteArrayExtra("congressionalInfo"));
        CongressPersonAdapter congressPersonAdapter = new CongressPersonAdapter(getSupportFragmentManager());
        viewPager.setAdapter(congressPersonAdapter);

        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);

    }

    private void populateCongressionalInfo(String string){
        String[] data = string.split("&");
        count = Integer.parseInt(data[0]);
        county = data[1];
        state = data[2];
        obama = data[3];
        romney = data[4];

        for (int i = 5; i < (data.length); i+=3){
            names.add(data[i]);
            if (data[i + 1].equals("D")){
                parties.add("Democrat");
            } else {
                parties.add("Republican");
            }
            titles.add(data[i + 2]);
        }
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

        Fragment[] fragments = new Fragment[count + 1];


        private CongressPersonAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            for (int i = 0; i < count; i++){
                fragments[i] = new CongressFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("index", i);
                fragments[i].setArguments(bundle);
            }
            fragments[count] = new VoteViewFragment();
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
            final View rootView = inflater.inflate(R.layout.activity_congress_person_view, container, false);
            int index = getArguments().getInt("index");

            TextView name = (TextView) rootView.findViewById(R.id.name);
            name.setText(names.get(index));
            TextView party = (TextView) rootView.findViewById(R.id.party);
            party.setText(parties.get(index));
            TextView title = (TextView) rootView.findViewById(R.id.title);
            title.setText(titles.get(index) + ".");

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    byte item = (byte) viewPager.getCurrentItem();
                    byte[] message = new byte[]{item};
                    Message.messageSend("detailedView", message, getActivity());
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
            TextView countyName = (TextView) rootView.findViewById(R.id.county_name);
            TextView stateName = (TextView) rootView.findViewById(R.id.state_name);
            TextView obamaP = (TextView) rootView.findViewById(R.id.obama);
            TextView romneyP = (TextView) rootView.findViewById(R.id.romney);
            System.out.println("COUTNY IS" + county);
            System.out.println("State is " + state);
            countyName.setText(county);
            stateName.setText(state);
            obamaP.setText(obama);
            romneyP.setText(romney);

            return rootView;
        }
    }
}
