package com.example.mariacan.campaignupdates2016;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailedView extends AppCompatActivity {

    Intent detailedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);
        detailedView = getIntent();
        if (detailedView.hasExtra("name")) {
            String name = detailedView.getStringExtra("name");
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(name);
        }
        if (detailedView.hasExtra("party")){
            String party = detailedView.getStringExtra("party");
            if (party.equals("D")){
                party = "Democrat";
            } else{
                party = "Republican";
            }
            TextView detailed_party = (TextView) this.findViewById(R.id.party_value);
            detailed_party.setText(party);
        }
        if (detailedView.hasExtra("term")){
            String term = detailedView.getStringExtra("term");
            TextView detailed_term = (TextView) this.findViewById(R.id.term_value);
            detailed_term.setText(term);
        }
        if (detailedView.hasExtra("bioguideid")){
            String bioguideid = detailedView.getStringExtra("bioguideid");
            String committeesUrl = this.generateCommitteesUrl(bioguideid);
            JSONObject committeesResult = this.requestURL(committeesUrl);
            String billsUrl = this.generateBillsUrl(bioguideid);
            JSONObject billsResult = this.requestURL(billsUrl);
            ArrayList<String> committees = this.getCommittees(committeesResult);
            StringBuilder committeesStringBuilder = new StringBuilder();
            for (int i = 0; i < committees.size(); i++){
                if (i != committees.size() - 1){
                    committeesStringBuilder.append(committees.get(i) + "\n\n");
                } else {
                    committeesStringBuilder.append(committees.get(i));
                }
            }
            ArrayList<String> bills = this.getBills(billsResult);
            StringBuilder billsStringBuilder = new StringBuilder();
            for (int i = 0; i < bills.size(); i++){

                if (i != bills.size() - 1){
                    billsStringBuilder.append(bills.get(i) + "\n\n");
                } else {
                    billsStringBuilder.append(bills.get(i));
                }
            }

            TextView detailed_committee = (TextView) this.findViewById(R.id.committee_value);
            detailed_committee.setText(committeesStringBuilder.toString());

            TextView detailed_bills = (TextView) this.findViewById(R.id.bill_value);
            detailed_bills.setText(billsStringBuilder.toString());

        }

    }

    private String generateCommitteesUrl(String bioguideid){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://congress.api.sunlightfoundation.com/committees?member_ids=");
        stringBuilder.append(bioguideid);
        stringBuilder.append("&");
        stringBuilder.append(getBaseContext().getResources().getString(R.string.sunlight_api_key));
        return stringBuilder.toString();
    }

    private String generateBillsUrl(String bioguideid){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://congress.api.sunlightfoundation.com/bills?sponsor_id=");
        stringBuilder.append(bioguideid);
        stringBuilder.append("&");
        stringBuilder.append(getBaseContext().getResources().getString(R.string.sunlight_api_key));
        return stringBuilder.toString();
    }


    public JSONObject requestURL(String UrlReq){
        String[] params = new String[] {UrlReq};
        JSONObject results = null;
        AsyncTask<String, Void, String> asyncTask = new URLRequestHandler().execute(params);
        try{
            results = new JSONObject(asyncTask.get());
        } catch (Exception e){
            System.out.println("RUNNING INTO PROBS");
        }
        return results;
    }

    private ArrayList<String> getCommittees(JSONObject jsonObject){
        ArrayList<String> committees = new ArrayList<>();
        try{
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject congressionalCommittees = jsonArray.getJSONObject(i);
                String committee = congressionalCommittees.optString("name");
                if (!committee.equals("null")){
                    committees.add(committee);
                }

            }
        } catch (JSONException e){
        }

        return committees;

    }


    private ArrayList<String> getBills(JSONObject jsonObject){
        ArrayList<String> bills = new ArrayList<>();
        try{
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject congressionalBills = jsonArray.getJSONObject(i);
                String bill = congressionalBills.optString("short_title");
                if (!bill.equals("null")){
                    bills.add(bill);
                }
            }
        } catch (JSONException e){
        }

        return bills;

    }

}
