package com.example.mariacan.campaignupdates2016;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.ListAdapter;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by mariacan on 3/2/16.
 */
public class Message extends WearableListenerService implements GoogleApiClient.ConnectionCallbacks{

    private static GoogleApiClient googleApiClient;
    static String path;
    static byte[] message;
    public static CongressionalViewAdapter congressionalViewAdapter;

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        String path = messageEvent.getPath();
        byte[] message = messageEvent.getData();

        if (path.equals("detailedView")){
            if (congressionalViewAdapter != null){
                int position = (int) message[0];
                CongressionalRep congressionalRep = congressionalViewAdapter.getItem(position);
                Intent detailedViewIntent = new Intent(getBaseContext(), DetailedView.class);
                detailedViewIntent.putExtra("name", congressionalRep.name);
                detailedViewIntent.putExtra("party", congressionalRep.party);
                detailedViewIntent.putExtra("term", congressionalRep.termEnds);
                detailedViewIntent.putExtra("bioguideid", congressionalRep.bioguideid);
                detailedViewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(detailedViewIntent);

            }
        } else if (path.equals("shake")){
            Intent congressionalView = new Intent(this, CongressionalView.class);
            congressionalView.putExtra("zipcode", "42069".getBytes());
            congressionalView.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(congressionalView);
        }
    }

    public static void messageSend(String path, byte[] message, Context context){
        Message.path = path;
        Message.message = message;
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .addConnectionCallbacks(new Message())
                .build();
        googleApiClient.connect();

    }

    @Override
    public void onConnected(Bundle bundle) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult result = Wearable.NodeApi.getConnectedNodes(googleApiClient).await();
                for (Node node : result.getNodes()) {
                    Wearable.MessageApi.sendMessage(googleApiClient, node.getId(), Message.path, Message.message);
                }

                googleApiClient.disconnect();
            }
        }).start();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


}
