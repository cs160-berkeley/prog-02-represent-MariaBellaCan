package com.example.mariacan.campaignupdates2016;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by mariacan on 3/1/16.
 */
public class CongressionalViewAdapter extends BaseAdapter{

    Context context;
    //boolean enteredLocation = false;
    Bitmap[] congressionalBitmap;
    Bitmap[] partyIconBitmap;
    Bitmap[] twitterIconBitmap;

    //create an array of a bunch of Barbara Boxers
//    CongressionalRep[] congressPeoples = new CongressionalRep[] {new CongressionalRep("Barbara Boxer",
//            "tweet", "BarbaraBoxer.com", 'd', "Boxer@Yahoo.com", "January 3, 2016"), new CongressionalRep("Barbara Boxer",
//            "tweet", "BarbaraBoxer.com", 'd', "Boxer@Yahoo.com", "January 3, 2016"), new CongressionalRep("Barbara Boxer",
//            "tweet", "BarbaraBoxer.com", 'd', "Boxer@Yahoo.com", "January 3, 2016")};
    ArrayList<CongressionalRep> congressionalReps;
    String[] congressionalNames;
    //NEED TO GET THESE IMAGES FROM TWITTER API
    ArrayList<Integer> congressionalImages;
    ArrayList<Integer> partyIconImages;
    ArrayList<Integer> twitterIconImages;
    String[] latestTweets;

    public CongressionalViewAdapter(Context context, ArrayList<CongressionalRep> congressionalReps){
        this.context = context;
        this.congressionalReps = congressionalReps;
        this.congressionalNames = generateCongressionalNames();
        this.partyIconImages = this.generatePartyIcons();
        this.congressionalImages = this.generateCongressionalImages();
        this.twitterIconImages = this.generateTwitterImages();
        this.latestTweets = this.generateLatestTweets();
        this.congressionalBitmap = generateBitmap(2, congressionalImages, context);
        this.partyIconBitmap = generateBitmap(2, partyIconImages, context);
        this.twitterIconBitmap = generateBitmap(2, twitterIconImages, context);

    }

    @Override
    public int getCount() {
//        if (!enteredLocation){
//            return 0;
//        }
        return congressionalReps.size();
    }

    @Override
    public CongressionalRep getItem(int position) {
        return congressionalReps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.representative_info, null);
        }

        ImageView rep_image = (ImageView) convertView.findViewById(R.id.rep_image);
        ImageView party_icon = (ImageView) convertView.findViewById(R.id.party_icon);
        TextView congressional_name = (TextView) convertView.findViewById(R.id.congressional_name);
        TextView latest_tweet = (TextView) convertView.findViewById(R.id.latest_tweet);
        ImageView twitter_icon = (ImageView) convertView.findViewById(R.id.twitter_icon);

        //TODO change this value to position later
        rep_image.setImageBitmap(congressionalBitmap[position]);
        party_icon.setImageBitmap(partyIconBitmap[position]);
        congressional_name.setText(congressionalNames[position]);
        latest_tweet.setText(latestTweets[position]);
        twitter_icon.setImageBitmap(twitterIconBitmap[position]);

        //TODO:set all the values

        return convertView;

    }

    private ArrayList<Integer> generatePartyIcons(){
        ArrayList<Integer> toReturn = new ArrayList<>();
        for (int i = 0; i < congressionalReps.size(); i++){
            if (congressionalReps.get(i).party.equals("D")){
                toReturn.add(R.drawable.donkey);
            } else{
                toReturn.add(R.drawable.elephant);
            }
        }
        return toReturn;
    }

    private ArrayList<Integer> generateCongressionalImages(){
        ArrayList<Integer> toReturn = new ArrayList<>();
        for (int i = 0; i < congressionalReps.size(); i++){
            if (congressionalReps.get(i).party.equals("D")){
                toReturn.add(R.drawable.boxer);
            } else{
                toReturn.add(R.drawable.boxer);
            }
        }
        return toReturn;
    }

    private ArrayList<Integer> generateTwitterImages(){
        ArrayList<Integer> toReturn = new ArrayList<>();
        for (int i = 0; i < congressionalReps.size(); i++){
            if (congressionalReps.get(i).party.equals("D")){
                toReturn.add(R.drawable.twitter_icon);
            } else{
                toReturn.add(R.drawable.twitter_icon);
            }
        }
        return toReturn;
    }

    private String[] generateCongressionalNames(){
        String[] toReturn = new String[congressionalReps.size()];
        for (int i = 0; i < congressionalReps.size(); i++){
            toReturn[i] = congressionalReps.get(i).name;
        }
        return toReturn;
    }

    private String[] generateLatestTweets(){
        String[] toReturn = new String[congressionalReps.size()];
        for (int i = 0; i < congressionalReps.size(); i++){
            toReturn[i] = congressionalReps.get(i).tweet;
        }
        return toReturn;
    }


    private Bitmap[] generateBitmap(final int sampleSize, ArrayList<Integer> images, Context context){
        Bitmap[] map = new Bitmap[images.size()];
        for (int i = 0; i < images.size(); i++){
            final BitmapFactory.Options bitmapOptions=new BitmapFactory.Options();
            bitmapOptions.inDensity=sampleSize;
            bitmapOptions.inTargetDensity=1;
            final Bitmap bMap =  BitmapFactory.decodeResource(context.getResources(), images.get(i), bitmapOptions);
            bMap.setDensity(Bitmap.DENSITY_NONE);
            map[i] = bMap;
        }
        return map;
    }
}
