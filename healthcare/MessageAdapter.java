package com.example.healthcare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter {

    private ArrayList<ChatModel> chatModelArrayList;
    private Context context;

    public MessageAdapter(ArrayList<ChatModel> chatModelArrayList, Context context) {
        this.chatModelArrayList = chatModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_sent_message, parent, false);
                return new UserViewHolder(view);

            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_received_message, parent, false);
                return new BotViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ChatModel chatModel = chatModelArrayList.get(position);
        switch (chatModel.getSender()) {
            case "user":
                ((UserViewHolder)holder).userMsg.setText(chatModel.getMessage());
                break;
            case "bot":
                ((BotViewHolder)holder).botMsg.setText(chatModel.getMessage());
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        switch (chatModelArrayList.get(position).getSender()) {
            case "user":
                return 0;
            case "bot":
                return 1;
            default:
                return -1;
        }
    }

    @Override
    public int getItemCount() {
        return chatModelArrayList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userMsg;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userMsg = itemView.findViewById(R.id.sentMessage);
        }

    }

    public static class BotViewHolder extends RecyclerView.ViewHolder {
        TextView botMsg;
        public BotViewHolder(@NonNull View itemView) {
            super(itemView);
            botMsg = itemView.findViewById(R.id.receivedMessage);
        }

    }

}
