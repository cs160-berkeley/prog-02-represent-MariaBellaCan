package com.example.mariacan.campaignupdates2016;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;
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


        if (path.equals("watchMainActivity")){
            System.out.println("MESSAGE" + path);
            Intent mainActivity = new Intent(this, MainActivity.class);
            mainActivity.putExtra("congressionalInfo", message);
            mainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mainActivity);
        } else if (path.equals("voteView")){
//            Intent voteView = new Intent(this, VoteView.class);
//            voteView.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(voteView);
        }

    }

    public static void messageSend(String path, byte[] message, Context context){
        System.out.println("SENDING A MESSAGE TO MOBILE WITH PATH " + path);
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
                    System.out.println("FOUND WATCH NODES");
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
