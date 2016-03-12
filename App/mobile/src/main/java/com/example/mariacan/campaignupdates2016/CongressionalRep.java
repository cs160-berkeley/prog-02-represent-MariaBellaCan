package com.example.mariacan.campaignupdates2016;

/**
 * Created by mariacan on 3/1/16.
 */
public class CongressionalRep {

    String name;
    String tweet;
    String website;
    String email;
    String termEnds;
    String party;
    String bioguideid;
    String title;

    public CongressionalRep(String name, String tweet, String website, String party, String email, String termEnds, String bioguideid, String title){
        this.name = name;
        this.tweet = tweet;
        this.website = website;
        this.party = party;
        this.email = email;
        this.termEnds = termEnds;
        this.bioguideid = bioguideid;
        this.title = title;
    }
}
