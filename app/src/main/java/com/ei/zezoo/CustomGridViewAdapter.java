package com.ei.zezoo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class CustomGridViewAdapter extends ArrayAdapter<String> {
    Context context;
    int layoutResourceId;
    ArrayList<String> data = new ArrayList<String>();

    public CustomGridViewAdapter(Context context, int layoutResourceId,
                                 ArrayList<String> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RecordHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new RecordHolder();
            holder.imageView = (ImageView) row.findViewById(R.id.item_image);
            holder.progressBar = (ProgressBar) row.findViewById(R.id.loadingImage);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/ElectronicInstitute/media/Images");
        final File mSaveBit = new File(myDir, data.get(position) + ".jpg");
        holder.progressBar.setVisibility(View.VISIBLE);
        holder.imageView.setVisibility(View.GONE);
        if(mSaveBit.exists()){
            GetImage g = new GetImage(holder.imageView,holder.progressBar);
            g.source = mSaveBit.getPath();
            g.execute();
        }else{
            myDir.mkdirs();
            try {
                FileOutputStream fos = new FileOutputStream(mSaveBit.getPath());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            StorageReference refe = FirebaseStorage.getInstance().getReference().child("Comment Images/" + data.get(position) + ".jpg");
            try {
                Toast.makeText(context, String.valueOf("image"), Toast.LENGTH_LONG).show();
                final File localFile = File.createTempFile("Voices", "3gp");
                final RecordHolder finalHolder = holder;
                refe.getFile(mSaveBit).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        GetImage g = new GetImage(finalHolder.imageView,finalHolder.progressBar);
                        g.source = mSaveBit.getPath();
                        g.execute();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "e" + "image"+ String.valueOf(position) + "  " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } catch (IOException e) {
                Toast.makeText(context, "e" + "image"+ String.valueOf(position) + "  " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
       // Bitmap item = data.get(position);
       // holder.imageView.setImageBitmap(item);
        return row;

    }
    static class RecordHolder {
        ImageView imageView;
        ProgressBar progressBar;
    }
}
