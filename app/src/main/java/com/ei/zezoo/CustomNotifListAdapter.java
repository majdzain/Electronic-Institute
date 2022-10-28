package com.ei.zezoo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomNotifListAdapter extends ArrayAdapter<NotifListChildItem> {
    Context context;
    private int lastPosition = -1;
    public CustomNotifListAdapter(Context context, ArrayList<NotifListChildItem> notifs) {
        super(context, 0, notifs);
        this.context = context;
    }
    private static class ViewHolder {
        TextView notifSubject;
        TextView notifDate;
        TextView notifName;
        TextView notifNew;
        ImageView notifImage ;
        ImageView notifDownload;
        FrameLayout notifLinear;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        NotifListChildItem memData = (NotifListChildItem) getItem(position);
        ViewHolder viewHolder = new ViewHolder();
        final View result;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_notification_item, parent, false);
            viewHolder.notifSubject = (TextView) convertView.findViewById(R.id.notifSubject);
            viewHolder.notifDate = (TextView) convertView.findViewById(R.id.notifDate);
            viewHolder.notifName = (TextView) convertView.findViewById(R.id.notifName);
            viewHolder.notifNew = (TextView) convertView.findViewById(R.id.notifNew);
            viewHolder.notifImage = (ImageView) convertView.findViewById(R.id.notifImage);
            viewHolder.notifDownload = (ImageView) convertView.findViewById(R.id.notifDownload);
            viewHolder.notifLinear = (FrameLayout) convertView.findViewById(R.id.notif_linear);
            result=convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }
        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;
        viewHolder.notifSubject.setText(ElecUtils.getSubject(memData.getType(),memData.getStudy(),memData.getSubject()));
        viewHolder.notifDate.setText(memData.getDate() + "|" + memData.getTime());
        viewHolder.notifName.setText(memData.getName());
        viewHolder.notifImage.setImageResource(ElecUtils.getResourceFromSubject(memData.getType(),memData.getStudy(),memData.getSubject()));
        switch (memData.getDownload()){
            case 0:
                viewHolder.notifDownload.setImageBitmap(null);
                break;
            case 1:
                viewHolder.notifDownload.setImageResource(R.drawable.download);
                break;
            case 2:
                viewHolder.notifDownload.setImageResource(R.drawable.downloaded);
                break;
            case 3:
                viewHolder.notifDownload.setImageResource(R.drawable.seen);
                break;
        }

        if(memData.isNew())
            viewHolder.notifNew.setVisibility(View.VISIBLE);
        else
            viewHolder.notifNew.setVisibility(View.GONE);


        return convertView;
    }
}
