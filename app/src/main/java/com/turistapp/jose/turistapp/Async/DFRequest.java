package com.turistapp.jose.turistapp.Async;


import android.os.AsyncTask;


import com.google.cloud.dialogflow.v2.DetectIntentRequest;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.turistapp.jose.turistapp.Fragments.Chatbot;


public class DFRequest extends AsyncTask<Void, Void, DetectIntentResponse> {

    private Chatbot CBFragment;
    private SessionName session;
    private SessionsClient sessionsClient;
    private QueryInput queryInput;

    public DFRequest(Chatbot activity, SessionName session, SessionsClient sessionsClient, QueryInput queryInput) {
        this.CBFragment = activity;
        this.session = session;
        this.sessionsClient = sessionsClient;
        this.queryInput = queryInput;
    }

    @Override
    protected DetectIntentResponse doInBackground(Void... voids) {
        try{
            DetectIntentRequest detectIntentRequest =
                    DetectIntentRequest.newBuilder()
                            .setSession(session.toString())
                            .setQueryInput(queryInput)
                            .build();
            return sessionsClient.detectIntent(detectIntentRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(DetectIntentResponse response) {
        CBFragment.callback(response);
    }

}
