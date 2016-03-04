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

/**
 * Created by mariacan on 3/1/16.
 */
public class CongressionalViewAdapter extends BaseAdapter{

    Context context;
    boolean enteredLocation = false;
    Bitmap[] congressionalBitmap;
    Bitmap[] partyIconBitmap;
    Bitmap[] twitterIconBitmap;

    //create an array of a bunch of Barbara Boxers
    CongressionalRep[] congressPeoples = new CongressionalRep[] {new CongressionalRep("Barbara Boxer",
            "tweet", "BarbaraBoxer.com", 'd', "Boxer@Yahoo.com", "January 3, 2016"), new CongressionalRep("Barbara Boxer",
            "tweet", "BarbaraBoxer.com", 'd', "Boxer@Yahoo.com", "January 3, 2016"), new CongressionalRep("Barbara Boxer",
            "tweet", "BarbaraBoxer.com", 'd', "Boxer@Yahoo.com", "January 3, 2016")};
    int[] congressionalImages = new int[]{R.drawable.boxer};
    int[] partyIconImages = new int[] {R.drawable.donkey};
    int[] twitterIconImages = new int[] {R.drawable.twitter_icon};

    public CongressionalViewAdapter(Context context){
        this.context = context;
        this.congressionalBitmap = generateBitmap(2, congressionalImages, context);
        this.partyIconBitmap = generateBitmap(2, partyIconImages, context);
        this.twitterIconBitmap = generateBitmap(2, twitterIconImages, context);

    }

    @Override
    public int getCount() {
//        if (!enteredLocation){
//            return 0;
//        }
        return 3;
    }

    @Override
    public CongressionalRep getItem(int position) {
        return congressPeoples[position];
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
        rep_image.setImageBitmap(congressionalBitmap[0]);
        party_icon.setImageBitmap(partyIconBitmap[0]);
        twitter_icon.setImageBitmap(twitterIconBitmap[0]);

        //TODO:set all the values

        return convertView;

    }

    private Bitmap[] generateBitmap(final int sampleSize, int[] images, Context context){


        Bitmap[] map = new Bitmap[images.length];
        for (int i = 0; i < images.length; i++){
            final BitmapFactory.Options bitmapOptions=new BitmapFactory.Options();
            bitmapOptions.inDensity=sampleSize;
            bitmapOptions.inTargetDensity=1;
            final Bitmap bMap =  BitmapFactory.decodeResource(context.getResources(), images[i], bitmapOptions);
            bMap.setDensity(Bitmap.DENSITY_NONE);
            map[i] = bMap;
        }
        return map;
    }
}
