package com.example.chandru.miniprojectchatbot;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ai.api.AIListener;
import ai.api.AIServiceContext;
import ai.api.AIServiceContextBuilder;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.android.AIService;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    private AIService aiService;
    List<BaseMessage> messageList;
    BaseMessage message;
    EditText senderInput;
    Button send;

    private String uuid = UUID.randomUUID().toString();
    private final String CLIENT_ACCESS_TOKEN = "Enter_your_token_here";
    private AIServiceContext customAIServiceContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        senderInput = findViewById(R.id.edittext_chatbox);
        send = findViewById(R.id.button_chatbox_send);

        messageList = new ArrayList<BaseMessage>();

        final AIConfiguration config = new AIConfiguration(
                CLIENT_ACCESS_TOKEN, AIConfiguration.SupportedLanguages.English,AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(this, config);

        final AIDataService aiDataService = new AIDataService(this, config);
        customAIServiceContext = AIServiceContextBuilder.buildFromSessionId(uuid);
        final AIRequest aiRequest = new AIRequest();

        mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        mMessageAdapter = new MessageListAdapter(this, messageList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setStackFromEnd(true);
        mMessageRecycler.setLayoutManager(mLayoutManager);
        mMessageRecycler.setAdapter(mMessageAdapter);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display sent message, store in firebase or get response from chatbot
                Log.d("Main Activity","Send Clicked");
                if(!TextUtils.isEmpty(senderInput.getText().toString())) {
                    String input = senderInput.getText().toString();
                    input = input.trim();
                    message = new BaseMessage(input, 1);
                    messageList.add(message);
                    int position = messageList.size() - 1;
                    Log.d("Main Activity", String.valueOf(position));
                    mMessageAdapter.notifyItemInserted(position);
                    mMessageRecycler.scrollToPosition(position);


                    aiRequest.setQuery(input);

                    new AsyncTask<AIRequest,Void,AIResponse>() {
                        @Override
                        protected AIResponse doInBackground(AIRequest... aiRequests) {
                            final AIRequest request = aiRequests[0];
                            try {
                                final AIResponse response = aiDataService.request(aiRequest,customAIServiceContext);
                                Log.d("Main Activity Async", response.toString());
                                return response;
                            } catch (Exception e) {
                                Log.d("Exception",e.getMessage());
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(AIResponse aiResponse) {
                            if (aiResponse != null) {

                                Result result = aiResponse.getResult();
                                String reply = result.getFulfillment().getSpeech();
                                BaseMessage bm = new BaseMessage(reply,2);
                                Log.d("Main Activity Async",reply);
                                messageList.add(bm);
                                int position = messageList.size() - 1;
                                Log.d("Main Activity Async", String.valueOf(position));
                                mMessageAdapter.notifyItemInserted(position);
                                mMessageRecycler.scrollToPosition(position);
                            }
                        }
                    }.execute(aiRequest);


                    senderInput.setText("");
                }
            }
        });
    }


}
