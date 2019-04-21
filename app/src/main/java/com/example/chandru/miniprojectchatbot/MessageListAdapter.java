package com.example.chandru.miniprojectchatbot;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Chandru on 12-03-2019.
 */

public class MessageListAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_HUMAN = 1;
    private static final int VIEW_TYPE_MESSAGE_CHATBOT = 2;

    private Context mContext;
    private List<BaseMessage> mMessageList;

    public MessageListAdapter(Context context, List<BaseMessage> messageList) {
        mContext = context;
        mMessageList = messageList;
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        BaseMessage message = (BaseMessage) mMessageList.get(position);

        if (message.getSentBy() == 1) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_HUMAN;
        } else if (message.getSentBy() == 2) {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_CHATBOT;
        }
        return 2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        Log.d("MessageListAdapter.java","OnCreateViewHolder()");
        if (viewType == VIEW_TYPE_MESSAGE_HUMAN) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_CHATBOT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BaseMessage message = (BaseMessage) mMessageList.get(position);
        Log.d("MessageListAdapter.java","OnBindViewHolder()");
        Log.d("MessageListAdapter.java",message.getMessage());
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_HUMAN:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_CHATBOT:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
        }

        void bind(BaseMessage message) {
            messageText.setText(message.getMessage());

        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            nameText = (TextView) itemView.findViewById(R.id.text_message_name);
        }

        void bind(BaseMessage message) {
            messageText.setText(message.getMessage());
            nameText.setText("Kush");

        }
    }


}//end of adapter
