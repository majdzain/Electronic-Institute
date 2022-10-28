package com.ei.zezoo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomNewGridViewAdapter extends ArrayAdapter<Uri> {
    Context context;
    int layoutResourceId;
    ArrayList<Uri> data = new ArrayList<Uri>();

    public CustomNewGridViewAdapter(Context context, int layoutResourceId,
                                 ArrayList<Uri> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RecordHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new RecordHolder();
            holder.imageView = (ImageView) row.findViewById(R.id.item_image);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }

        Uri item = data.get(position);
        holder.imageView.setImageURI(item);
        return row;

    }

    static class RecordHolder {
        ImageView imageView;

    }
}
