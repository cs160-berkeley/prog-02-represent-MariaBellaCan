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
    char party;

    public CongressionalRep(String name, String tweet, String website, char party, String email, String termEnds){
        this.name = name;
        this.tweet = tweet;
        this.website = website;
        this.party = party;
        this.email = email;
        this.termEnds = termEnds;
    }
}
