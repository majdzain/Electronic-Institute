package com.ei.zezoo;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.downloader.PRDownloader;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.view.View.GONE;
import static com.ei.zezoo.LessonFragment.isConnectingToInternet;

public class CustomDownloadListAdapter extends ArrayAdapter<DownloadListChildItem> {
    Context context;
    private int lastPosition = -1;
    LessonSQLDatabaseHandler dbl;
    Activity activity;
    public CustomDownloadListAdapter(Context context,Activity activity, ArrayList<DownloadListChildItem> downloads) {
        super(context, 0, downloads);
        this.context = context;
        this.activity = activity;
        dbl = new LessonSQLDatabaseHandler(context);
    }
    private static class ViewHolder {
        TextView downloadTitle;
        TextView downloadName;
        TextView downloadTxt;
        ProgressBar downloadProgress;
        ImageView downloadImage ;
        Button btnDownload;
        Button btnStop;
        Button btnPause;
        Button btnWatch;
        LinearLayout downloadLinear;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DownloadListChildItem memData = (DownloadListChildItem) getItem(position);
        ViewHolder viewHolder = new ViewHolder();
        final View result;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.download_list_item, parent, false);
            viewHolder.downloadTitle = (TextView) convertView.findViewById(R.id.downloadTitle);
            viewHolder.downloadName = (TextView) convertView.findViewById(R.id.downloadName);
            viewHolder.downloadTxt = (TextView) convertView.findViewById(R.id.downloadTxt);
            viewHolder.downloadImage = (ImageView) convertView.findViewById(R.id.downloadImage);
            viewHolder.downloadProgress = (ProgressBar) convertView.findViewById(R.id.downloadProgress);
            viewHolder.btnDownload = (Button) convertView.findViewById(R.id.btnDownload);
            viewHolder.btnStop = (Button) convertView.findViewById(R.id.btnStop);
            viewHolder.btnPause = (Button) convertView.findViewById(R.id.btnPause);
            viewHolder.btnWatch = (Button) convertView.findViewById(R.id.btnWatch);
            viewHolder.downloadLinear = (LinearLayout) convertView.findViewById(R.id.download_linear);

            result=convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }
        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;
       // Handler handler = viewHolder.hand;
        //TextView textView = viewHolder.downloadTxt;
        /**viewHolder.th = new Thread(new Runnable() {
            public void run() {
                while (memData.getDownload() == 0) {
                    handler.post(new Runnable() {
                        public void run() {
                            if (textView.getText().toString().contains("..."))
                                textView.setText(textView.getText().toString().replace("...", ""));
                            textView.setText(textView.getText() + ".");

                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });**/
        viewHolder.downloadTitle.setText(memData.getTitle());
        viewHolder.downloadName.setText(memData.getName());
        viewHolder.downloadProgress.setProgress(memData.getProgress());
        viewHolder.downloadImage.setImageResource(ElecUtils.getResourceFromSubject(context,memData.getTitle()));
        switch (memData.getDownload()){
            case DownloadListChildItem.UNKNOWN:
                viewHolder.btnDownload.setVisibility(View.VISIBLE);
                viewHolder.btnStop.setVisibility(View.GONE);
                viewHolder.btnPause.setVisibility(View.GONE);
                viewHolder.btnWatch.setVisibility(View.GONE);
                viewHolder.downloadTxt.setText("اضغط للتحميل");
                break;
            case DownloadListChildItem.STARTED:
                viewHolder.btnDownload.setVisibility(View.GONE);
                viewHolder.btnStop.setVisibility(View.VISIBLE);
                viewHolder.btnPause.setVisibility(View.VISIBLE);
                viewHolder.btnWatch.setVisibility(View.GONE);
                viewHolder.downloadTxt.setText("جاري التحميل..");
                break;
            case DownloadListChildItem.RESUMED:
                viewHolder.btnDownload.setVisibility(View.GONE);
                viewHolder.btnStop.setVisibility(View.VISIBLE);
                viewHolder.btnPause.setVisibility(View.VISIBLE);
                viewHolder.btnWatch.setVisibility(View.GONE);
                viewHolder.downloadTxt.setText("جاري التحميل..");
                break;
            case DownloadListChildItem.PAUSED:
                viewHolder.btnDownload.setVisibility(View.VISIBLE);
                viewHolder.btnStop.setVisibility(View.GONE);
                viewHolder.btnPause.setVisibility(View.GONE);
                viewHolder.btnWatch.setVisibility(GONE);
                viewHolder.btnDownload.setText("استئناف التحميل");
                viewHolder.btnDownload.setCompoundDrawablesWithIntrinsicBounds(null,null,context.getResources().getDrawable(R.drawable.resume_download),null);
                viewHolder.downloadTxt.setText("إيقاف مؤقت");
                break;
            case DownloadListChildItem.STOPED:
                viewHolder.btnDownload.setVisibility(View.VISIBLE);
                viewHolder.btnStop.setVisibility(View.GONE);
                viewHolder.btnPause.setVisibility(View.GONE);
                viewHolder.btnWatch.setVisibility(GONE);
                viewHolder.btnDownload.setText("تحميل الدرس");
                viewHolder.btnDownload.setCompoundDrawablesWithIntrinsicBounds(null,null,context.getResources().getDrawable(R.drawable.download_video),null);
                viewHolder.downloadTxt.setText("تم إيقاف التحميل");
                break;
            case DownloadListChildItem.RUNNING:
                viewHolder.btnDownload.setVisibility(View.GONE);
                viewHolder.btnStop.setVisibility(View.VISIBLE);
                viewHolder.btnPause.setVisibility(View.VISIBLE);
                viewHolder.btnWatch.setVisibility(GONE);
                viewHolder.downloadTxt.setText("جاري التحميل..." + "(" + String.valueOf(memData.getProgress()) + "%)");
                break;
            case DownloadListChildItem.COMPLETED:
                viewHolder.btnDownload.setVisibility(View.GONE);
                viewHolder.btnStop.setVisibility(View.GONE);
                viewHolder.btnPause.setVisibility(View.GONE);
                viewHolder.btnWatch.setVisibility(View.VISIBLE);
                viewHolder.downloadTxt.setText("تم التحميل ,اضغط للمشاهدة");
                break;
            case DownloadListChildItem.ERROR:
                viewHolder.btnDownload.setVisibility(View.VISIBLE);
                viewHolder.btnStop.setVisibility(View.GONE);
                viewHolder.btnPause.setVisibility(View.GONE);
                viewHolder.btnWatch.setVisibility(GONE);
                viewHolder.downloadTxt.setText("لم يتم التحميل ,حدث خطأ");
                break;
        }
        viewHolder.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView txtDownload = (TextView) result.findViewById(R.id.downloadTxt);
                Button btnDownload = (Button) result.findViewById(R.id.btnDownload);
                Button btnCancel = (Button) result.findViewById(R.id.btnStop);
                Button btnPause = (Button) result.findViewById(R.id.btnPause);
                Button btnWatch = (Button) result.findViewById(R.id.btnWatch);
                if(isConnectingToInternet(context)) {
                    txtDownload.setVisibility(View.VISIBLE);
                    btnDownload.setVisibility(GONE);
                    btnCancel.setVisibility(View.VISIBLE);
                    btnWatch.setVisibility(GONE);
                    btnPause.setVisibility(View.VISIBLE);
                    txtDownload.setText("جاري التحميل");
                        DownloadListChildItem Download = memData;
                        LessonListChildItem Lesson = new LessonListChildItem();
                        for(int i = 0; i < dbl.allLessons(Download.getType(),Download.getStudy(),Download.getTitle()).size();i++){
                            LessonListChildItem d = dbl.allLessons(Download.getType(),Download.getStudy(),Download.getTitle()).get(i);
                            if(Download.getType() == d.getType() && Download.getStudy() == d.getStudy() && d.getSubject() == Download.getSubject() && d.getLesson() == Download.getLesson()){
                                Lesson = d;
                            }
                        }
                        ((UserActivity) context).addDownload(Lesson,Download,true);
                }else{
                    txtDownload.setText("غير متصل بالإنترنت!");
                }
            }
        });
        viewHolder.btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView txtDownload = (TextView) result.findViewById(R.id.downloadTxt);
                Button btnDownload = (Button) result.findViewById(R.id.btnDownload);
                Button btnCancel = (Button) result.findViewById(R.id.btnStop);
                Button btnPause = (Button) result.findViewById(R.id.btnPause);
                Button btnWatch = (Button) result.findViewById(R.id.btnWatch);
                txtDownload.setText("تم إيقاف التحميل");
                btnDownload.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(GONE);
                btnWatch.setVisibility(GONE);
                btnPause.setVisibility(GONE);
                btnDownload.setText("تحميل الدرس");
                btnDownload.setCompoundDrawablesWithIntrinsicBounds(null,null,context.getResources().getDrawable(R.drawable.download_video),null);
                //  th.stop();
                DownloadListChildItem Download = memData;
                LessonListChildItem Lesson = new LessonListChildItem();
                for(int i = 0; i < dbl.allLessons(Download.getType(),Download.getStudy(),Download.getTitle()).size();i++){
                    LessonListChildItem d = dbl.allLessons(Download.getType(),Download.getStudy(),Download.getTitle()).get(i);
                    if(Download.getType() == d.getType() && Download.getStudy() == d.getStudy() && d.getSubject() == Download.getSubject() && d.getLesson() == Download.getLesson()){
                        Lesson = d;
                    }
                }
                ((UserActivity) context).stopDownload(Lesson);
            }
        });
        viewHolder.btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView txtDownload = (TextView) result.findViewById(R.id.downloadTxt);
                Button btnDownload = (Button) result.findViewById(R.id.btnDownload);
                Button btnCancel = (Button) result.findViewById(R.id.btnStop);
                Button btnPause = (Button) result.findViewById(R.id.btnPause);
                Button btnWatch = (Button) result.findViewById(R.id.btnWatch);
                txtDownload.setText("إيقاف مؤقت");
                btnDownload.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(GONE);
                btnWatch.setVisibility(GONE);
                btnPause.setVisibility(GONE);
                btnDownload.setText("استئناف التحميل");
                btnDownload.setCompoundDrawablesWithIntrinsicBounds(null,null,context.getResources().getDrawable(R.drawable.resume_download),null);
                //th.stop();
                DownloadListChildItem Download = memData;
                LessonListChildItem Lesson = new LessonListChildItem();
                for(int i = 0; i < dbl.allLessons(Download.getType(),Download.getStudy(),Download.getTitle()).size();i++){
                    LessonListChildItem d = dbl.allLessons(Download.getType(),Download.getStudy(),Download.getTitle()).get(i);
                    if(Download.getType() == d.getType() && Download.getStudy() == d.getStudy() && d.getSubject() == Download.getSubject() && d.getLesson() == Download.getLesson()){
                        Lesson = d;
                    }
                }

                ((UserActivity) context).pauseDownload(Lesson);
            }
        });
        viewHolder.btnWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadListChildItem Download =memData;
                LessonListChildItem Lesson = new LessonListChildItem();
                for(int i = 0; i < dbl.allLessons(Download.getType(),Download.getStudy(),Download.getTitle()).size();i++){
                    LessonListChildItem d = dbl.allLessons(Download.getType(),Download.getStudy(),Download.getTitle()).get(i);
                    if(Download.getType() == d.getType() && Download.getStudy() == d.getStudy() && d.getSubject() == Download.getSubject() && d.getLesson() == Download.getLesson()){
                        Lesson = d;
                    }
                }
                ((UserActivity) activity).result.setSelection(Long.valueOf(String.valueOf(Lesson.getSubject() + String.valueOf(Lesson.getLesson()))),true);
            }
        });
        return convertView;
    }
}
