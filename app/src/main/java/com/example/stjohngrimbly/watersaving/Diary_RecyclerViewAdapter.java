package com.example.stjohngrimbly.watersaving;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by St John Grimbly on 8/3/2018.
 */

public class Diary_RecyclerViewAdapter extends RecyclerView.Adapter<Diary_RecyclerViewAdapter.ViewHolder>{

    public static final String TAG = "Diary Adapter";

    private ArrayList<String> mHeadings;
    private ArrayList<String> mInfo;
    private ArrayList<String> mTotals;
    private ArrayList<Drawable> mImages;
    private Context mContext;

    public Diary_RecyclerViewAdapter(Context mContext, ArrayList<Drawable> mImages, ArrayList<String> mHeadings, ArrayList<String> mInfo, ArrayList<String> mTotals) {
        this.mContext = mContext;
        this.mImages = mImages;
        this.mHeadings = mHeadings;
        this.mInfo = mInfo;
        this.mTotals = mTotals;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_diary_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.image.setImageDrawable(mImages.get(position));
        holder.heading.setText(mHeadings.get(position));
        holder.info.setText(mInfo.get(position));
        holder.total.setText(mTotals.get(position));
    }

    @Override
    public int getItemCount() {
        return mHeadings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView image;
        TextView heading;
        TextView info;
        TextView total;
        RelativeLayout diaryLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.diary_image);
            heading = itemView.findViewById(R.id.diary_heading);
            info = itemView.findViewById(R.id.diary_info);
            total = itemView.findViewById(R.id.diary_total);
            diaryLayout = itemView.findViewById(R.id.diary_layout);
        }
    }
}
