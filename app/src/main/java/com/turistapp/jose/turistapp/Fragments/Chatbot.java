package com.turistapp.jose.turistapp.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import com.google.cloud.dialogflow.v2.TextInput;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.turistapp.jose.turistapp.Async.DFRequest;
import com.turistapp.jose.turistapp.Model.Author;
import com.turistapp.jose.turistapp.Model.Message;
import com.turistapp.jose.turistapp.R;

import java.io.InputStream;
import java.util.Date;
import java.util.UUID;


public class Chatbot extends Fragment{
    private static final String TAG = "Chatbot Fragment";

    private OnFragmentInteractionListener mListener;

    //Random session id
    private String uuid = UUID.randomUUID().toString();

    //Client session metadata
    private SessionsClient sessionsClient;
    private SessionName session;

    //Chat data
    private MessagesList messagesList;
    private MessagesListAdapter<Message> adapter;
    private MessageInput input;
    protected final String senderId = "0";
    private Author author;

    View view;

    public Chatbot() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chatbot, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        messagesList = (MessagesList) view.findViewById(R.id.messagesList);
        input = (MessageInput) view.findViewById(R.id.input);

        adapter = new MessagesListAdapter<>(senderId, null);
        messagesList.setAdapter(adapter);

        input.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                Message m = addMessage("0", input.toString());
                adapter.addToStart(m, true);
                sendMessage(m.getText());
                return true;
            }
        });

        initai();


    }

    public void initai() {
        try {
            InputStream stream = getResources().openRawResource(R.raw.dialogflow_credentials);
            GoogleCredentials credentials = GoogleCredentials.fromStream(stream);
            String projectId = ((ServiceAccountCredentials)credentials).getProjectId();

            SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
            SessionsSettings sessionsSettings = settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
            sessionsClient = SessionsClient.create(sessionsSettings);
            session = SessionName.of(projectId, uuid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String text) {
        if (text.trim().isEmpty()) {
            Toast.makeText(getActivity(), "Please enter your query!", Toast.LENGTH_LONG).show();
        } else {

            QueryInput queryInput = QueryInput.newBuilder().setText(TextInput.newBuilder().setText(text).setLanguageCode("es_US")).build();
            new DFRequest(this, session, sessionsClient, queryInput).execute();
        }
    }

    public void callback(DetectIntentResponse response) {
        if (response != null) {
            String reply = response.getQueryResult().getFulfillmentText();
            adapter.addToStart(addMessage("1", reply), true);

        } else {
            Log.d(TAG, "Bot Reply: Null");

        }
    }

    private Message addMessage(String userId, String text){
        String name = "";
        if(userId.equals("0")) {
            name = "User";
        } else {
            name = "Trippy";
        }
        author = new Author(userId,name,null);
        return new Message(userId,author,text,new Date());
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
