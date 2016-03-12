package com.example.mariacan.campaignupdates2016;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void findOnClick(View view){
        EditText zip_enter = (EditText) findViewById(R.id.zip_enter);

        String zip = zip_enter.getText().toString();
        String countyStateUrl = "";
        if (zip != null && zip.length() == 5){
            try{
                System.out.println("ZIP " + zip);
                List<Address> addresses;
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                addresses = geocoder.getFromLocationName(zip, 1);
                if (addresses != null && !addresses.isEmpty()){
                    String lat = Double.toString(addresses.get(0).getLatitude());
                    System.out.println("LAT " + lat);
                    String lon = Double.toString(addresses.get(0).getLongitude());
                    System.out.println("LON " + lon);
                    countyStateUrl = generateCountyStateUrl(lat, lon);
                    System.out.println("COUNTY STATE URL IS IN If " + countyStateUrl);
                }
            } catch (Exception e){
                e.printStackTrace();
                System.out.println("FAILED TO CONVERT TO LAT LONG FROM ZIP");
            }


            String zipUrlReq = this.generateZipcodeUrl(zip);
            String percentageUrl = this.generatePercentageUrl();
            System.out.println("COUNTY STATE URL IS OUTSIDE IF " + countyStateUrl);
            Message.messageSend("mainActivity", zip.getBytes(), this);
            Intent congressionalView = new Intent(this, CongressionalView.class);
            congressionalView.putExtra("congressionalRequest", zipUrlReq.getBytes());
            congressionalView.putExtra("countyStateRequest", countyStateUrl);
            congressionalView.putExtra("percentages", percentageUrl);
            congressionalView.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(congressionalView);
        } else{
            //NEED TO MAKE IT SO THEY CANT HIT THE ENTER BUTTON HERE
        }


    }

    private String generateZipcodeUrl(String zip){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://congress.api.sunlightfoundation.com/legislators/locate?zip=");
        stringBuilder.append(zip);
        stringBuilder.append("&");
        stringBuilder.append(getBaseContext().getResources().getString(R.string.sunlight_api_key));
        return stringBuilder.toString();
    }

    private String generatePercentageUrl(){
        return getBaseContext().getResources().getString(R.string.percentage_api_key);
    }

    public void currentLocation(View view){
        GPSManager.getLatAndLongforGPS(new GPSCallback() {

            @Override
            public void OnFinished(double latitude, double longitude) {
                String stringLat = Double.toString(latitude);
                String stringLong = Double.toString(longitude);
                String currLocationUrl = generateCurrLocationUrl(stringLat, stringLong);
                String countyStateUrl = generateCountyStateUrl(stringLat, stringLong);

                //Message.messageSend("mainActivity", "1234".getBytes(), getBaseContext());
                Intent congressionalView = new Intent(getBaseContext(), CongressionalView.class);
                congressionalView.putExtra("congressionalRequest", currLocationUrl.getBytes());
                congressionalView.putExtra("countyStateRequest", countyStateUrl);
                startActivity(congressionalView);
            }
        }, this);
    }

    private String generateCurrLocationUrl(String latitude, String longitude){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://congress.api.sunlightfoundation.com/legislators/locate?latitude=");
        stringBuilder.append(latitude);
        stringBuilder.append("&longitude=");
        stringBuilder.append(longitude);
        stringBuilder.append("&");
        stringBuilder.append(getBaseContext().getResources().getString(R.string.sunlight_api_key));
        return stringBuilder.toString();
    }

    private String generateCountyStateUrl(String latitude, String longitude){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("https://maps.googleapis.com/maps/api/geocode/json?latlng=");
        stringBuilder.append(latitude);
        stringBuilder.append(",");
        stringBuilder.append(longitude);
        stringBuilder.append("&key=");
        stringBuilder.append(getBaseContext().getResources().getString(R.string.gmaps_api_key));
        return stringBuilder.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult){
        if (requestCode == GPSManager.LOCATION_PERMISSION && grantResult.length > 0 &&
                (grantResult[0] == PackageManager.PERMISSION_GRANTED)){
            this.currentLocation(null);
        }
    }
}
