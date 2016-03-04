package com.example.mariacan.campaignupdates2016;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
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

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        String path = messageEvent.getPath();
        byte[] message = messageEvent.getData();
        //System.out.println("received message " + new String(message));

        //filter messages received to start a new activity

        System.out.println("MESSAGE RECEIVED ON MOBILE" + path);

        if (path.equals("detailedView")){
            Intent detailedView = new Intent(this, DetailedView.class);
            detailedView.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(detailedView);
        } else if (path.equals("shake")){
            Intent congressionalView = new Intent(this, CongressionalView.class);
            congressionalView.putExtra("zipcode", "42069".getBytes());
            congressionalView.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(congressionalView);
        }
    }

    public static void messageSend(String path, byte[] message, Context context){
        System.out.println("SENDING A MESSAGE TO WATCH WITH PATH " + path);
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
                    System.out.println("FOUND MOBILE NODES");
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
