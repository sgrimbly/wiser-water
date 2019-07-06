package com.example.stjohngrimbly.watersaving;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by St John Grimbly on 8/3/2018.
 */

public class AddEntry_RecyclerViewAdapter extends RecyclerView.Adapter<AddEntry_RecyclerViewAdapter.ViewHolder>{
    public static final String TAG = "AddEntry Adapter";

    private ArrayList<String> mHeadings = new ArrayList<>();
    private ArrayList<String> mInfo = new ArrayList<>();
    private ArrayList<String> mTotals = new ArrayList<>();
    private ArrayList<Drawable> mImages = new ArrayList<>();
    private Context mContext;
    private TextView totalHeading;

    public AddEntry_RecyclerViewAdapter(Context mContext, ArrayList<Drawable> mImages, ArrayList<String> mHeadings, ArrayList<String> mInfo, ArrayList<String> mTotals, TextView totalHeading) {
        this.mContext = mContext;
        this.mHeadings = mHeadings;
        this.mInfo = mInfo;
        this.mTotals = mTotals;
        this.mImages = mImages;
        this.totalHeading = totalHeading;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_add_entry_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.image.setImageDrawable(mImages.get(position));
        holder.heading.setText(mHeadings.get(position));
        holder.info.setText(mInfo.get(position));
        holder.total.setText(mTotals.get(position));

        holder.plus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTotals.set(position,"TOTAL: " + String.valueOf(Utilities.getValue((String) holder.total.getText()) + 1) + " litres");
                holder.total.setText(mTotals.get(position));
                totalHeading.setText("TOTAL: "+(Integer.parseInt(String.valueOf(totalHeading.getText()).substring(7))+1));
            }
        });
        holder.minus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utilities.getValue((String)holder.total.getText()) != 0) {
                    mTotals.set(position,"TOTAL: " + String.valueOf(Utilities.getValue((String) holder.total.getText()) - 1) + " litres");
                    holder.total.setText(mTotals.get(position));
                    totalHeading.setText("TOTAL: "+(Integer.parseInt(String.valueOf(totalHeading.getText()).substring(7))-1));
                }
            }
        });
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
        ImageButton plus_button;
        ImageButton minus_button;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.diary_image);
            heading = itemView.findViewById(R.id.diary_heading);
            info = itemView.findViewById(R.id.diary_info);
            total = itemView.findViewById(R.id.diary_total);

            plus_button = (ImageButton) itemView.findViewById(R.id.add_entry_plus);
            minus_button = (ImageButton) itemView.findViewById(R.id.add_entry_minus);

            diaryLayout = itemView.findViewById(R.id.diary_layout);

        }
    }

}
