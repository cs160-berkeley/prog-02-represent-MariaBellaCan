package com.example.mariacan.campaignupdates2016;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.messages.Messages;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class CongressionalView extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    Intent congressionalView;
    public static String urlRequest;
    public static String gmapsRequest;
    public static String percentageRequest;
    CongressionalViewAdapter congressionalViewAdapter;
    public static ArrayList<CongressionalRep> congressionalReps;
    public static String[] countyState;
    public static String[] percentages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congressional_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Representatives");

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        congressionalView = getIntent();
        if (congressionalView.hasExtra("congressionalRequest")) {
            urlRequest = new String(congressionalView.getByteArrayExtra("congressionalRequest"));
            JSONObject results = this.requestURL(urlRequest);
            System.out.println(urlRequest);
            System.out.println("RESULTS " + results);
            congressionalReps = this.getCongressionalMembers(results);
            System.out.println("CONGRESSIOANL REPS " + congressionalReps);
        }
        if (congressionalView.hasExtra("countyStateRequest")){

            gmapsRequest = new String(congressionalView.getStringExtra("countyStateRequest"));
            JSONObject gmapsResults = this.requestURL(gmapsRequest);
            countyState = this.getCountyState(gmapsResults);

            //PARSE HEREEEEEEE
        }
        if (congressionalView.hasExtra("percentages")){
            System.out.println("CONGRESSIONAL VIEW HAS PERCENTAGES");
            percentageRequest = new String(congressionalView.getStringExtra("percentages"));
            System.out.println("PERCENTAGE REWUESTTTTT " + percentageRequest);
            JSONArray percentageResult = this.requestPercentageUrl(percentageRequest);
            System.out.println("REQUESTEEEEEDDDDDDD URL JSONOBJ: "+ percentageResult);
            percentages = this.getPercentages(percentageResult);
            System.out.println("PERCENTAGES ARE "+ percentages);

        }

        String mainInfo = this.watchMainInfo();
        Message.messageSend("watchMainActivity", mainInfo.getBytes(), getBaseContext());
        congressionalViewAdapter = new CongressionalViewAdapter(this, congressionalReps);
        Message.congressionalViewAdapter = congressionalViewAdapter;

        ListView infoBoxes = (ListView)findViewById(R.id.listview);
        infoBoxes.setAdapter(congressionalViewAdapter);




        infoBoxes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CongressionalRep congressionalRep = congressionalViewAdapter.getItem(position);
                Intent detailedViewIntent = new Intent(getBaseContext(), DetailedView.class);
                detailedViewIntent.putExtra("name", congressionalRep.name);
                detailedViewIntent.putExtra("party", congressionalRep.party);
                detailedViewIntent.putExtra("term", congressionalRep.termEnds);
                //CHANGE THIS
                detailedViewIntent.putExtra("bioguideid", congressionalRep.bioguideid);
                startActivity(detailedViewIntent);
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "CongressionalView Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.mariacan.campaignupdates2016/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "CongressionalView Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.mariacan.campaignupdates2016/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public JSONObject requestURL(String UrlReq){
        String[] params = new String[] {UrlReq};
        JSONObject results = null;
        AsyncTask<String, Void, String> asyncTask = new URLRequestHandler().execute(params);
        try{
            results = new JSONObject(asyncTask.get());
        } catch (Exception e){
            e.printStackTrace();
        }
        return results;
    }

    public JSONArray requestPercentageUrl(String UrlReq){
        String[] params = new String[] {UrlReq};
        JSONArray results = null;
        AsyncTask<String, Void, String> asyncTask = new URLRequestHandler().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        try{
            results = new JSONArray(asyncTask.get());
        } catch (Exception e){
            e.printStackTrace();
        }
        return results;
    }

    private String watchMainInfo(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(congressionalReps.size());
        stringBuilder.append("&");
        stringBuilder.append(countyState[0]);
        stringBuilder.append("&");
        stringBuilder.append(countyState[1]);
        stringBuilder.append("&");
        stringBuilder.append(percentages[0]);
        stringBuilder.append("&");
        stringBuilder.append(percentages[1]);
        stringBuilder.append("&");
        for (int i = 0; i < congressionalReps.size() - 1; i++){
            stringBuilder.append(congressionalReps.get(i).name);
            stringBuilder.append("&");
            stringBuilder.append(congressionalReps.get(i).party);
            stringBuilder.append("&");
            stringBuilder.append(congressionalReps.get(i).title);
            stringBuilder.append("&");
        }
        stringBuilder.append(congressionalReps.get(congressionalReps.size() - 1).name);
        stringBuilder.append("&");
        stringBuilder.append(congressionalReps.get(congressionalReps.size() - 1).party);
        stringBuilder.append("&");
        stringBuilder.append(congressionalReps.get(congressionalReps.size() - 1).title);
        return stringBuilder.toString();
    }

    private ArrayList<CongressionalRep> getCongressionalMembers(JSONObject jsonObject){
        ArrayList<CongressionalRep> congressionalReps = new ArrayList<>();
        try{
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject congressionalMember = jsonArray.getJSONObject(i);
                String first_name = congressionalMember.optString("first_name");
                String last_name = congressionalMember.optString("last_name");
                String full_name = first_name + " " + last_name;
                String website = congressionalMember.optString("website");
                String tweet = "";
                String party = congressionalMember.optString("party");
                String email = congressionalMember.optString("oc_email");
                String termEnds = congressionalMember.optString("term_end");
                String bioguideid = congressionalMember.optString("bioguide_id");
                String title = congressionalMember.optString("title");
                CongressionalRep rep = new CongressionalRep(full_name, tweet, website, party, email, termEnds, bioguideid, title);
                congressionalReps.add(rep);
            }
        } catch (JSONException e){
        }
        return congressionalReps;
    }

    private String[] getCountyState(JSONObject jsonObject){
        String[] countyState = new String[2];
        try{
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            JSONObject components = (JSONObject) jsonArray.get(0);
            JSONArray address_comp = (JSONArray) components.get("address_components");
            for (int i = 0; i < address_comp.length(); i++){
                JSONObject location = (JSONObject) address_comp.get(i);
                JSONArray types = location.getJSONArray("types");
                if (types.get(0).equals("administrative_area_level_1")){
                    countyState[1] = location.optString("long_name");
                    System.out.println(countyState[1]);
                } else if (types.get(0).equals("administrative_area_level_2")){
                    countyState[0] = location.optString("long_name");
                    System.out.println(countyState[0]);
                }
            }
        } catch (JSONException e){
        }
        return countyState;
        //return congressionalReps;

    }

    private String[] getPercentages(JSONArray jsonArray){
        String[] percentages = new String[2];
        try{
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
                if (jsonObject.get("county-name").equals(countyState[0])){
                    String obama = jsonObject.get("obama-percentage").toString();
                    String romney = jsonObject.get("romney-percentage").toString();
                    percentages[0] = obama;
                    percentages[1] = romney;
                }
            }
        }catch (Exception e){
        }
        return percentages;
    }

}
