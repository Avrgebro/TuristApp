package com.turistapp.jose.turistapp.Fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.adroitandroid.chipcloud.ChipCloud;
import com.adroitandroid.chipcloud.ChipListener;
import com.adroitandroid.chipcloud.FlowLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import com.google.cloud.dialogflow.v2.TextInput;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.common.modeldownload.FirebaseLocalModel;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.common.modeldownload.FirebaseRemoteModel;
import com.google.firebase.ml.custom.FirebaseModelDataType;
import com.google.firebase.ml.custom.FirebaseModelInputOutputOptions;
import com.google.firebase.ml.custom.FirebaseModelInputs;
import com.google.firebase.ml.custom.FirebaseModelInterpreter;
import com.google.firebase.ml.custom.FirebaseModelOptions;
import com.google.firebase.ml.custom.FirebaseModelOutputs;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.turistapp.jose.turistapp.Async.DFRequest;
import com.turistapp.jose.turistapp.MainActivity;
import com.turistapp.jose.turistapp.Model.Author;
import com.turistapp.jose.turistapp.Model.Message;
import com.turistapp.jose.turistapp.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import java.util.Random;


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
    private ChipCloud taggroup;
    private LinearLayout tagcontainer;
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

        ImageView acceptprofile = (ImageView) view.findViewById(R.id.acceptprofile);
        acceptprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Run Model
                List<Integer> a = new ArrayList<>();

                for(int i = 1; i<=34; i++){
                    a.add(i);
                }

                Collections.shuffle(a);

                List<Integer> rec = a.subList(0, 15);

                ((MainActivity)getActivity()).placesCallback(rec);

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
            String[] msgs = reply.split("\\*");

            for(int i = 0; i < msgs.length; i++){
                String aux = msgs[i];
                if(!aux.startsWith("/")){
                    adapter.addToStart(addMessage("1", aux), true);
                }
            }

            if(msgs[msgs.length-1].startsWith("/")){

                /*try{
                    getplacesfromMLkit(25,1,1);
                } catch (FirebaseMLException e) {
                    Log.e("MLKIT: ", e.getMessage());
                }*/

                taggroup = (ChipCloud) view.findViewById(R.id.tags);
                tagcontainer = (LinearLayout) view.findViewById(R.id.tagcontainer);

                String[] labels = {"arte","historia","musica","naturaleza","vida nocturna","comida","shopping"};

                new ChipCloud.Configure()
                        .chipCloud(taggroup)
                        .selectedColor(Color.parseColor("#FF2371FA"))
                        .selectedFontColor(Color.parseColor("#FFFFFF"))
                        .deselectedColor(Color.parseColor("#e1e1e1"))
                        .deselectedFontColor(Color.parseColor("#333333"))
                        .selectTransitionMS(500)
                        .deselectTransitionMS(250)
                        .labels(labels)
                        .mode(ChipCloud.Mode.MULTI)
                        .allCaps(true)
                        .gravity(ChipCloud.Gravity.STAGGERED)
                        .textSize(getResources().getDimensionPixelSize(R.dimen.default_textsize))
                        .verticalSpacing(getResources().getDimensionPixelSize(R.dimen.vertical_spacing))
                        .minHorizontalSpacing(getResources().getDimensionPixelSize(R.dimen.min_horizontal_spacing))
                        .chipListener(new ChipListener() {
                            @Override
                            public void chipSelected(int index) {
                                //...
                            }
                            @Override
                            public void chipDeselected(int index) {
                                //...
                            }
                        })
                        .build();

                input.setVisibility(View.GONE);
                tagcontainer.setVisibility(View.VISIBLE);

            }

        } else {
            Log.d(TAG, "Bot Reply: Null");

        }
    }

    private Message addMessage(String userId, String text){
        String name = "";
        if(userId.equals("0")) {
            name = "User";
        } else {
            name = "Bot";
        }
        author = new Author(userId,name,null);
        return new Message(userId,author,text,new Date());
    }

    private ArrayList<String> getTags(){
        ArrayList<String> tags = new ArrayList<>();
        tags.add("Naturaleza");
        tags.add("Historia");
        tags.add("Arte");
        tags.add("Vida nocturna");
        tags.add("Comida");
        tags.add("Shopping");
        tags.add("Música");

        return tags;
    }

    private void getplacesfromMLkit(int age, int status, int genre) throws FirebaseMLException{

        /*FirebaseModelDownloadConditions.Builder conditionsBuilder = new FirebaseModelDownloadConditions.Builder().requireWifi();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            conditionsBuilder = conditionsBuilder
                    .requireCharging()
                    .requireDeviceIdle();
        }

        FirebaseModelDownloadConditions conditions = conditionsBuilder.build();*/

        FirebaseLocalModel localModel = new FirebaseLocalModel.Builder("local_places_recommend")
                .setAssetFilePath("recsys.tflite").build();

        FirebaseModelManager.getInstance().registerLocalModel(localModel);

        /*FirebaseRemoteModel cloudSource = new FirebaseRemoteModel.Builder("remote_places_recommend")
                .enableModelUpdates(true)
                .setInitialDownloadConditions(conditions)
                .setUpdatesDownloadConditions(conditions)
                .build();

        FirebaseModelManager.getInstance().registerRemoteModel(cloudSource);*/

        FirebaseModelOptions options = new FirebaseModelOptions.Builder()
                //.setRemoteModelName("remote_places_recommend")
                .setLocalModelName("local_places_recommend")
                .build();

        FirebaseModelInterpreter firebaseInterpreter = FirebaseModelInterpreter.getInstance(options);

        //float[][] inp = new float[1][3];
        //float[][] out = new float[1][34];

        FirebaseModelInputOutputOptions inputOutputOptions =
                new FirebaseModelInputOutputOptions.Builder()
                        .setInputFormat(0, FirebaseModelDataType.FLOAT32, new int[]{1, 3})
                        .setOutputFormat(0, FirebaseModelDataType.FLOAT32, new int[]{1, 1, 34})
                        .build();

        float[][] input = new float[1][3];

        input[0][0] = age;
        input[0][1] = status;
        input[0][2] = genre;


        FirebaseModelInputs inputs = new FirebaseModelInputs.Builder()
                .add(input)  // add() as many input arrays as your model requires
                .build();

        firebaseInterpreter.run(inputs, inputOutputOptions)
                .addOnSuccessListener(
                        new OnSuccessListener<FirebaseModelOutputs>() {
                            @Override
                            public void onSuccess(FirebaseModelOutputs result) {
                                //float[][] output = result.getOutput(0);

                                Log.i("OUTPUT: ", result.getOutput(0).toString());

                                //((MainActivity)getActivity()).routesCallback(response.body(), adapter.getOrigin(), waypoints);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i("OUTPUT: ", e.toString());
                            }
                        });

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
