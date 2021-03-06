package com.sur.ultra.contacta.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sur.ultra.contacta.Interfaces.OnMessageSelectedListener;
import com.sur.ultra.contacta.Models.Message;
import com.sur.ultra.contacta.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexis on 6/2/16.
 */
public class MessageAdapter
        extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private static final String TAG = "MessageAdapter";
    private final List<Message> messages;
    private Context ctx;
    private OnMessageSelectedListener mCallback;

    public MessageAdapter(List<Message> messages, Context ctx, OnMessageSelectedListener mCallback) {
        this.messages = messages;
        this.ctx = ctx;
        this.mCallback = mCallback;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // Campos respectivos de un item
        public TextView messageSummary;
        public ImageView avatar;
        public TextView author;
        private List<Message> messages = new ArrayList<Message>();
        private Context ctx;
        private OnMessageSelectedListener mCallback;
        public ProgressBar progressBar;

        Button dismissButton;

        public ViewHolder(View v, Context ctx, List<Message> messages, OnMessageSelectedListener mCallback) {
            super(v);
            this.messages = messages;
            this.ctx = ctx;
            this.mCallback = mCallback;

            dismissButton = (Button) v.findViewById(R.id.dismissButton);

            dismissButton.setOnClickListener(this);
            v.setOnClickListener(this);

            messageSummary = (TextView) v.findViewById(R.id.text_messageSummary);
            author = (TextView) v.findViewById(R.id.text_author);
            avatar = (ImageView) v.findViewById(R.id.avatar);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            if (v.getId() == dismissButton.getId()){
                Toast.makeText(v.getContext(), "Marcar como leida = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
            } else {
                Message messageClicked = messages.get(position);
                mCallback.onMessageSelected(messageClicked.id, messageClicked.type );
            }
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_message_list, viewGroup, false);
        return new ViewHolder(v,this.ctx, this.messages, mCallback );
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        Message item = messages.get(position);
        viewHolder.messageSummary.setText(item.title);
        viewHolder.author.setText(item.name);


        ImageLoadingListener imageLoadingListener = new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                viewHolder.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                viewHolder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                viewHolder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                viewHolder.progressBar.setVisibility(View.GONE);
            }
        };

        // Then later, when you want to display image
        ImageLoader.getInstance().displayImage(messages.get(position).getAvatar(), viewHolder.avatar, imageLoadingListener);
    }

}


