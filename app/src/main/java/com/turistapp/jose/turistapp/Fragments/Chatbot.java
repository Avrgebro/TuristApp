package com.turistapp.jose.turistapp.Fragments;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
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
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;
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
import com.google.gson.JsonElement;
import com.google.protobuf.ListValue;
import com.google.protobuf.Value;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

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
    private Map parameters = new HashMap();
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

                View botcontrols = (LinearLayout) view.findViewById(R.id.tagcontainer);
                botcontrols.setVisibility(View.GONE);

                View progress = (LinearLayout) view.findViewById(R.id.loadingplaces);
                progress.setVisibility(View.VISIBLE);
                //Run Model

                ListValue auxlist = ((Value)parameters.get("estado")).getListValue();
                String status = auxlist.getValues(0).getStringValue();
                String genre = ((Value)parameters.get("sexo")).getStringValue();
                Double age = ((Value)parameters.get("number")).getNumberValue();

                int agedata = age.intValue();
                int genredata = genre.equalsIgnoreCase("Hombre") ? 1 : 0;
                int statusdata = status.equalsIgnoreCase("Soltero") ? 0 : 1;


                try {
                    //Log.i("hola", "hola1");
                    getplacesfromMLkit(agedata,statusdata,genredata);
                } catch (FirebaseMLException e) {
                    //Log.i("hola", "hola2");
                    Log.e(TAG, e.getMessage() + " " + e.getCode());
                }

            }
        });

        initai();

        if(!((MainActivity)getActivity()).isNetworkAvailable()){
            customBotMsg("Hola, parece que no estas conectado a internet, " +
                    "selecciona la opcion 'Guardado' del menu inferior para visualizar las rutas que guardaste " +
                    "anteriormente. ");

            input.setEnabled(false);
        }



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

            Map<String, Value> xd = response.getQueryResult().getParameters().getFieldsMap();

            for (Map.Entry<String, Value> entry : xd.entrySet()) {
                parameters.put(entry.getKey(), xd.get(entry.getKey()));
            }


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

    public void customBotMsg(String text){
        Message m = addMessage("1", text);
        adapter.addToStart(m, true);
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

        FirebaseModelDownloadConditions.Builder conditionsBuilder = new FirebaseModelDownloadConditions.Builder().requireWifi();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            conditionsBuilder = conditionsBuilder
                    .requireCharging()
                    .requireDeviceIdle();
        }

        FirebaseModelDownloadConditions conditions = conditionsBuilder.build();

        FirebaseLocalModel localModel = new FirebaseLocalModel.Builder("local_places_recommend")
                .setAssetFilePath("recsys.tflite").build();

        FirebaseModelManager.getInstance().registerLocalModel(localModel);

        FirebaseRemoteModel cloudSource = new FirebaseRemoteModel.Builder("remote_places_recommend")
                .enableModelUpdates(true)
                .setInitialDownloadConditions(conditions)
                .setUpdatesDownloadConditions(conditions)
                .build();

        FirebaseModelManager.getInstance().registerRemoteModel(cloudSource);

        FirebaseModelOptions options = new FirebaseModelOptions.Builder()
                .setRemoteModelName("remote_places_recommend")
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
                                List<Integer> re = new ArrayList<>();

                                //Log.i("OUTPUT SUCCESS: ", result.toString());

                                float[][][] res = result.getOutput(0);
                                float[] res2 = res[0][0];

                                for(int i = 0; i < (res2.length)/2; i++){
                                    re.add(Math.round(res2[i]));
                                }

                                ProgressBar pb = (ProgressBar) view.findViewById(R.id.progress_places);
                                TextView tv = (TextView) view.findViewById(R.id.progress_text);

                                pb.setVisibility(View.GONE);
                                tv.setText("Tus lugares estan listos! puedes verlo en le pestaña Lugares");



                                ((MainActivity)getActivity()).placesCallback(re);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Log.i("OUTPUT FAILURE: ", e.toString() + " " + ((FirebaseMLException)e).getCode());
                            }
                        });

    }


    private MappedByteBuffer loadModelFile(Activity activity, String MODEL_FILE) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(MODEL_FILE);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
