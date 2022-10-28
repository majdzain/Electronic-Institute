package com.ei.zezoo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Varun John on 4 Dec, 2018
 * Github : https://github.com/varunjohn
 */
public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<CommentListChildItem> comments;
    private static SimpleDateFormat timeFormatter;
    Context context;
    private int lastPosition = -1;

    private Activity activity;
    private UserSQLDatabaseHandler dbu;
    private CommentSQLDatabaseHandler dbc;
    Handler handVoice = new Handler();
    Runnable runCurrentVoice;
    ImageView currentPlayImage;
    MediaPlayer currentMedia;
    String searchString = "";


    public CommentAdapter(Context c, Activity a, List<CommentListChildItem> co) {
        context = c;
        activity = a;
        comments = co;
        timeFormatter = new SimpleDateFormat("m:ss", Locale.getDefault());
        dbu = new UserSQLDatabaseHandler(c);
        dbc = new CommentSQLDatabaseHandler(c);
    }

    public void add(CommentListChildItem comment) {
        comments.add(comment);
        notifyDataSetChanged();
        //  notifyItemInserted(comments.lastIndexOf(comment));
    }

    @Override
    public int getItemViewType(int position) {
        CommentListChildItem comment = comments.get(position);
        int type = 10;
        if (!TextUtils.isEmpty(comment.getReply()))
            type = type + 10;
        switch (comment.getType()) {
            case "voice":
                type = type + 1;
                break;
            case "images":
                type = type + 2;
                break;
        }
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommentListChildItem comment = comments.get(viewType);
        String reply = comment.getReply();
        String type = comment.getType();
        if (TextUtils.isEmpty(reply)) {
            if (type.matches("msg")) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_comment_item, null);
                Comment c = new Comment(view);
                return c;
            } else if (type.matches("voice")) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_comment_voice_item, null);
                CommentVoice c = new CommentVoice(view);
                return c;
            } else if (type.contains("image")) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_comment_images_item, null);
                int n = Integer.valueOf(comment.getType().substring(type.indexOf("e") + 1));
                CommentImages c = new CommentImages(view, n);
                return c;
            }
        } else {
            if (type.matches("msg")) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_comment_reply, null);
                CommentReply c = new CommentReply(view);
                return c;
            } else if (type.matches("voice")) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_comment_voice_reply, null);
                CommentVoiceReply c = new CommentVoiceReply(view);
                return c;
            } else if (type.contains("image")) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_comment_images_reply, null);
                int n = Integer.valueOf(comment.getType().substring(type.indexOf("e") + 1));
                CommentImagesReply c = new CommentImagesReply(view, n);
                return c;
            }
        }

        /**if (viewType == 10) {
         View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_comment_item, null);
         return new Comment(view);
         } else if (viewType == 20) {
         View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_comment_reply, null);
         return new CommentReply(view);
         } else if (viewType == 11) {
         View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_comment_voice_item, null);
         return new CommentVoice(view);
         } else if (viewType == 21) {
         View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_comment_voice_reply, null);
         return new CommentVoiceReply(view);
         } else if (viewType == 12) {
         View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_comment_images_item, null);
         return new CommentImages(view);
         } else if (viewType == 22) {
         View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_comment_images_reply, null);
         return new CommentImagesReply(view);
         }**/
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof Comment)
            ((Comment) holder).bind(comments.get(position), position);
        else if (holder instanceof CommentReply)
            ((CommentReply) holder).bind(comments.get(position), position);
        else if (holder instanceof CommentVoice)
            ((CommentVoice) holder).bind(comments.get(position), position);
        else if (holder instanceof CommentVoiceReply)
            ((CommentVoiceReply) holder).bind(comments.get(position), position);
        else if (holder instanceof CommentImages)
            ((CommentImages) holder).bind(comments.get(position), position);
        else if (holder instanceof CommentImagesReply)
            ((CommentImagesReply) holder).bind(comments.get(position), position);
        LinearLayout.LayoutParams paramsMsg = new LinearLayout.
                LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

        holder.itemView.setLayoutParams(paramsMsg);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static String getAudioTime(long time) {
        time *= 1000;
        timeFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return timeFormatter.format(new Date(time));
    }

    public boolean filter(CharSequence sequence) {
           boolean isExist = false;
        if (!TextUtils.isEmpty(sequence)) {
            for (CommentListChildItem comment : comments) {
                if (comment.getComment().toLowerCase(Locale.getDefault()).contains(sequence.toString().toLowerCase(Locale.getDefault()))) {
                    searchString = sequence.toString().toLowerCase(Locale.getDefault());
                    isExist = true;
                }
            }

        } else {
            searchString = "";
        }
return isExist;
    }

    public class Comment extends RecyclerView.ViewHolder {

        TextView commentDate;
        TextView commentName;
        TextView commentText;
        LinearLayout commentLinear;
        CardView commentMessage;
        boolean isDateShowed = false;

        public Comment(View view) {
            super(view);
            commentName = itemView.findViewById(R.id.commentName);
            commentText = itemView.findViewById(R.id.commentText);
            commentDate = itemView.findViewById(R.id.commentDate);
            commentName = itemView.findViewById(R.id.commentName);
            commentLinear = itemView.findViewById(R.id.comment_item);
            commentMessage = itemView.findViewById(R.id.commentMessage);

        }

        public void bind(final CommentListChildItem comment, int position) {
            Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
            commentLinear.startAnimation(animation);
            lastPosition = position;
            if (position != 0 && comment.getUser().matches(comments.get(position - 1).getUser()))
                commentName.setVisibility(View.GONE);
            else
                commentName.setVisibility(View.VISIBLE);
            if (comment.getComment().toLowerCase(Locale.getDefault()).contains(searchString)) {

                int startPos = comment.getComment().toLowerCase(Locale.getDefault()).indexOf(searchString);
                int endPos = startPos + searchString.length();

                Spannable spanString = Spannable.Factory.getInstance().newSpannable(comment.getComment());
                spanString.setSpan(new BackgroundColorSpan(Color.YELLOW), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                commentText.setText(spanString);
            } else
                commentText.setText(comment.getComment());

            commentDate.setText(comment.getDate() + "|" + comment.getTime());

            if (comment.isTeacher()) {

                commentLinear.setGravity(Gravity.LEFT);
                commentName.setTextColor(context.getResources().getColor(R.color.orange));
                commentMessage.setCardBackgroundColor(context.getResources().getColor(R.color.orange));
                commentName.setText(comment.getName());
            } else if (comment.getUser().matches(dbu.getCurrentUser().getUser())) {
                commentLinear.setGravity(Gravity.RIGHT);
                commentName.setTextColor(context.getResources().getColor(R.color.blue));
                commentMessage.setCardBackgroundColor(context.getResources().getColor(R.color.blue));
                commentName.setText("أنت");
            } else {
                commentLinear.setGravity(Gravity.RIGHT);
                commentName.setTextColor(context.getResources().getColor(R.color.purple));
                commentMessage.setCardBackgroundColor(context.getResources().getColor(R.color.purple));
                commentName.setText(comment.getName());
            }
            /**if(position > 0 && getItem(position-1).getName().matches(getItem(position).getName())){
             commentName.setVisibility(View.GONE);
             }**/

            /**if (comment.getImage1() != null)
             gridArray.add(comment.getImage1());
             if (comment.getImage2() != null)
             gridArray.add(comment.getImage2());
             if (comment.getImage3() != null)
             gridArray.add(comment.getImage3());
             if (comment.getImage4() != null)
             gridArray.add(comment.getImage4());
             if (comment.getImage5() != null)
             gridArray.add(comment.getImage5());
             if (comment.getImage6() != null)
             gridArray.add(comment.getImage6());
             if (comment.getImage7() != null)
             gridArray.add(comment.getImage7());
             if (comment.getImage8() != null)
             gridArray.add(comment.getImage8());
             if (comment.getImage9() != null)
             gridArray.add(comment.getImage9());
             if (comment.getImage10() != null)
             gridArray.add(comment.getImage10());
             if (position == 0) {
             Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.aaaa);
             gridArray.add(bm);
             gridArray.add(bm);
             gridArray.add(bm);
             gridArray.add(bm);


             }**/


            commentLinear.setTag(position);
            commentLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    if (!isDateShowed) {
                        isDateShowed = true;
                        Animation animation = AnimationUtils.loadAnimation(context, R.anim.down_from_top);
                        commentDate.startAnimation(animation);
                        commentDate.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isDateShowed = false;
                                Animation animation = AnimationUtils.loadAnimation(context, R.anim.up_from_bottom);
                                commentDate.startAnimation(animation);
                                commentDate.setVisibility(View.GONE);

                            }
                        }, 4000);
                    } else {
                        isDateShowed = false;
                        Animation animation = AnimationUtils.loadAnimation(context, R.anim.up_from_bottom);
                        commentDate.startAnimation(animation);
                        commentDate.setVisibility(View.GONE);
                    }
                }
            });


        }
    }

    public class CommentReply extends RecyclerView.ViewHolder {

        TextView commentDate;
        TextView commentName;
        TextView commentText, commentReplyName, commentReplyText, commentReplyTextIcon;
        LinearLayout commentLinear, commentReplyIcons;
        CardView commentMessage, commentReply, commentReplyMessage;
        ImageView commentReplyColor, commentReplyIcon;
        private boolean isDateShowed = false;


        public CommentReply(View view) {
            super(view);
            commentName = itemView.findViewById(R.id.commentName);
            commentText = itemView.findViewById(R.id.commentText);
            commentDate = itemView.findViewById(R.id.commentDate);
            commentName = itemView.findViewById(R.id.commentName);
            commentLinear = itemView.findViewById(R.id.comment_reply);
            commentMessage = itemView.findViewById(R.id.commentMessage);
            commentReply = itemView.findViewById(R.id.commentReply);
            commentReplyIcons = itemView.findViewById(R.id.layoutIconsReply);
            commentReplyColor = itemView.findViewById(R.id.replyColor);
            commentReplyName = itemView.findViewById(R.id.replyName);
            commentReplyText = itemView.findViewById(R.id.replyText);
            commentReplyIcon = itemView.findViewById(R.id.replyIcon);
            commentReplyTextIcon = itemView.findViewById(R.id.txtIconReply);
            commentReplyMessage = itemView.findViewById(R.id.replyMessage);
        }

        public void bind(final CommentListChildItem comment, int position) {
            Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
            commentLinear.startAnimation(animation);
            lastPosition = position;
            if (position != 0 && comment.getUser().matches(comments.get(position - 1).getUser()))
                commentName.setVisibility(View.GONE);
            else
                commentName.setVisibility(View.VISIBLE);
            if (comment.getComment().toLowerCase(Locale.getDefault()).contains(searchString)) {

                int startPos = comment.getComment().toLowerCase(Locale.getDefault()).indexOf(searchString);
                int endPos = startPos + searchString.length();

                Spannable spanString = Spannable.Factory.getInstance().newSpannable(comment.getComment());
                spanString.setSpan(new BackgroundColorSpan(Color.YELLOW), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                commentText.setText(spanString);
            } else
                commentText.setText(comment.getComment());
            if (!TextUtils.isEmpty(comment.getReply())) {
                commentReply.setVisibility(View.VISIBLE);
                CommentListChildItem replyComment = new CommentListChildItem();
                for (int i = 0; i < dbc.allIds().size(); i++) {
                    if (dbc.allIds().get(i).matches(comment.getReply()))
                        replyComment = dbc.allComments().get(i);
                }
                if (replyComment.isTeacher()) {
                    commentReplyName.setText(replyComment.getName());
                    commentReplyColor.setImageResource(R.drawable.reply_t);
                    commentReplyName.setTextColor(context.getResources().getColor(R.color.orange));
                } else if (replyComment.getUser().matches(dbu.getCurrentUser().getUser())) {
                    commentReplyName.setText("أنت");
                    commentReplyColor.setImageResource(R.drawable.reply_s);
                    commentReplyName.setTextColor(context.getResources().getColor(R.color.blue));
                } else {
                    commentReplyName.setText(replyComment.getName());
                    commentReplyColor.setImageResource(R.drawable.reply);
                    commentReplyName.setTextColor(context.getResources().getColor(R.color.purple));
                }
                  if (!TextUtils.isEmpty(replyComment.getComment())) {
                    if (replyComment.getComment().toLowerCase(Locale.getDefault()).contains(searchString)) {

                        int startPos = replyComment.getComment().toLowerCase(Locale.getDefault()).indexOf(searchString);
                        int endPos = startPos + searchString.length();

                        Spannable spanString = Spannable.Factory.getInstance().newSpannable(replyComment.getComment());
                        spanString.setSpan(new BackgroundColorSpan(Color.YELLOW), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        commentReplyText.setText(spanString);
                    } else
                    commentReplyText.setText(replyComment.getComment());
                }else
                    commentReplyMessage.setVisibility(View.GONE);
                int imgCount = 0;
                ArrayList<String> images = new ArrayList<>();
                if (!TextUtils.isEmpty(replyComment.getImage1())) {
                    imgCount++;
                    images.add(replyComment.getImage1());
                }
                if (!TextUtils.isEmpty(replyComment.getImage2())) {
                    imgCount++;
                    images.add(replyComment.getImage2());
                }
                if (!TextUtils.isEmpty(replyComment.getImage3())) {
                    imgCount++;
                    images.add(replyComment.getImage3());
                }
                if (!TextUtils.isEmpty(replyComment.getImage4())) {
                    imgCount++;
                    images.add(replyComment.getImage4());
                }
                if (!TextUtils.isEmpty(replyComment.getImage5())) {
                    imgCount++;
                    images.add(replyComment.getImage5());
                }
                if (!TextUtils.isEmpty(replyComment.getImage6())) {
                    imgCount++;
                    images.add(replyComment.getImage6());
                }
                if (!TextUtils.isEmpty(replyComment.getImage7())) {
                    imgCount++;
                    images.add(replyComment.getImage7());
                }
                if (!TextUtils.isEmpty(replyComment.getImage8())) {
                    imgCount++;
                    images.add(replyComment.getImage8());
                }
                if (!TextUtils.isEmpty(replyComment.getImage9())) {
                    imgCount++;
                    images.add(replyComment.getImage9());
                }

                if (imgCount == 0) {
                    commentReplyIcons.setVisibility(View.GONE);
                } else if (imgCount == 1) {
                    commentReplyIcons.setVisibility(View.VISIBLE);
                    commentReplyTextIcon.setText("صورة");
                    commentReplyIcon.setImageResource(R.drawable.image);
                } else {
                    commentReplyIcons.setVisibility(View.VISIBLE);
                    commentReplyTextIcon.setText("صور");
                    commentReplyIcon.setImageResource(R.drawable.images);
                }
                if (replyComment.getRecordTime() > 0) {
                    commentReplyIcons.setVisibility(View.VISIBLE);
                    commentReplyTextIcon.setText("تسجيل صوتي" + "(" + getAudioTime(replyComment.getRecordTime()) + ")");
                    commentReplyIcon.setImageResource(R.drawable.voice_comment);
                }
            } else {
                commentReply.setVisibility(View.GONE);
            }
            commentDate.setText(comment.getDate() + "|" + comment.getTime());

            if (comment.isTeacher()) {

                commentLinear.setGravity(Gravity.LEFT);
                commentName.setTextColor(context.getResources().getColor(R.color.orange));
                commentMessage.setCardBackgroundColor(context.getResources().getColor(R.color.orange));
                commentName.setText(comment.getName());

            } else if (comment.getUser().matches(dbu.getCurrentUser().getUser())) {
                commentLinear.setGravity(Gravity.RIGHT);
                commentName.setTextColor(context.getResources().getColor(R.color.blue));
                commentMessage.setCardBackgroundColor(context.getResources().getColor(R.color.blue));
                commentName.setText("أنت");

            } else {
                commentLinear.setGravity(Gravity.RIGHT);
                commentName.setTextColor(context.getResources().getColor(R.color.purple));
                commentMessage.setCardBackgroundColor(context.getResources().getColor(R.color.purple));
                commentName.setText(comment.getName());
            }
            /**if(position > 0 && getItem(position-1).getName().matches(getItem(position).getName())){
             commentName.setVisibility(View.GONE);
             }**/

            /**if (comment.getImage1() != null)
             gridArray.add(comment.getImage1());
             if (comment.getImage2() != null)
             gridArray.add(comment.getImage2());
             if (comment.getImage3() != null)
             gridArray.add(comment.getImage3());
             if (comment.getImage4() != null)
             gridArray.add(comment.getImage4());
             if (comment.getImage5() != null)
             gridArray.add(comment.getImage5());
             if (comment.getImage6() != null)
             gridArray.add(comment.getImage6());
             if (comment.getImage7() != null)
             gridArray.add(comment.getImage7());
             if (comment.getImage8() != null)
             gridArray.add(comment.getImage8());
             if (comment.getImage9() != null)
             gridArray.add(comment.getImage9());
             if (comment.getImage10() != null)
             gridArray.add(comment.getImage10());
             if (position == 0) {
             Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.aaaa);
             gridArray.add(bm);
             gridArray.add(bm);
             gridArray.add(bm);
             gridArray.add(bm);


             }**/


            commentLinear.setTag(position);
            commentLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    if (!isDateShowed) {
                        isDateShowed = true;
                        Animation animation = AnimationUtils.loadAnimation(context, R.anim.down_from_top);
                        commentDate.startAnimation(animation);
                        commentDate.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isDateShowed = false;
                                Animation animation = AnimationUtils.loadAnimation(context, R.anim.up_from_bottom);
                                commentDate.startAnimation(animation);
                                commentDate.setVisibility(View.GONE);

                            }
                        }, 4000);
                    } else {
                        isDateShowed = false;
                        Animation animation = AnimationUtils.loadAnimation(context, R.anim.up_from_bottom);
                        commentDate.startAnimation(animation);
                        commentDate.setVisibility(View.GONE);
                    }
                }
            });


        }
    }

    public class CommentVoice extends RecyclerView.ViewHolder {

        TextView commentDate;
        TextView commentName;
        LinearLayout commentLinear;
        CardView commentAudio;
        ImageView commentPlay;
        TextView commentTime;
        SeekBar commentSeekbar;
        MediaPlayer mediaPlayer;
        ImageView commentVoiceImg;
        ProgressBar commentProgressV;
        private boolean isDateShowed = false;
        PrepareVoice prepareVoice;
        Handler handler = new Handler();
        Runnable runnable;


        public CommentVoice(View view) {
            super(view);
            commentName = itemView.findViewById(R.id.commentName);
            commentDate = itemView.findViewById(R.id.commentDate);
            commentLinear = itemView.findViewById(R.id.comment_voice_item);
            commentAudio = itemView.findViewById(R.id.commentAudio);
            commentPlay = itemView.findViewById(R.id.commentPlay);
            commentTime = itemView.findViewById(R.id.commentTime);
            commentSeekbar = itemView.findViewById(R.id.commentSeekbar);
            commentProgressV = itemView.findViewById(R.id.loadingVoice);
            commentVoiceImg = itemView.findViewById(R.id.commentVoiceImg);
            mediaPlayer = new MediaPlayer();
            prepareVoice = new PrepareVoice(mediaPlayer, commentVoiceImg, commentProgressV);

        }

        private void togglePausePlay() {

            if (mediaPlayer.isPlaying()) {
                commentPlay.setImageResource(R.drawable.pause);
            } else {
                commentPlay.setImageResource(R.drawable.play);
            }
        }

        public void bind(final CommentListChildItem comment, int position) {

            Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
            commentLinear.startAnimation(animation);
            lastPosition = position;
            if (position != 0 && comment.getUser().matches(comments.get(position - 1).getUser()))
                commentName.setVisibility(View.GONE);
            else
                commentName.setVisibility(View.VISIBLE);
            commentAudio.setVisibility(View.VISIBLE);
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/ElectronicInstitute/media/Voices");
            final File mSaveBit = new File(myDir, comment.getRecrordData() + ".3gp");
            commentProgressV.setVisibility(View.VISIBLE);
            commentVoiceImg.setVisibility(View.GONE);

            // commentPlay.setClickable(false);
            //commentSeekbar.setEnabled(false);
            //commentSeekbar.setClickable(false);
            //commentSeekbar.setFocusable(false);
            if (mSaveBit.exists()) {
                PrepareVoice p = new PrepareVoice(mediaPlayer, commentVoiceImg, commentProgressV);
                p.source = mSaveBit.getPath();
                p.execute();
            } else {
                myDir.mkdirs();
                try {
                    FileOutputStream fos = new FileOutputStream(mSaveBit.getPath());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                StorageReference refe = FirebaseStorage.getInstance().getReference().child("Comment Voices/" + comment.getRecrordData() + ".3gp");
                try {

                    final File localFile = File.createTempFile("Voices", "3gp");
                    refe.getFile(mSaveBit).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            prepareVoice.source = mSaveBit.getPath();
                            prepareVoice.execute();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "e" + "voice" + "  " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (IOException e) {
                    Toast.makeText(context, "e" + "voice" + "  " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            commentSeekbar.setMax(comment.getRecordTime() * 1000);
            commentSeekbar.setProgress(0);
            commentPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                                commentTime.setText(getAudioTime(mCurrentPosition));
                                commentSeekbar.setProgress(mediaPlayer.getCurrentPosition());
                                if (mediaPlayer.getCurrentPosition() == mediaPlayer.getDuration()) {
                                    togglePausePlay();
                                    commentTime.setText(getAudioTime(comment.getRecordTime()));
                                    commentSeekbar.setProgress(0);
                                }
                                handler.postDelayed(this, 200);
                            } catch (IllegalStateException ed) {
                                ed.printStackTrace();
                            }
                        }
                    };
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        handler.removeCallbacks(runnable);
                    } else {
                        //   mediaPlayer.start();
                        /** if(runCurrentVoice != null){
                         currentMedia.stop();
                         handVoice.removeCallbacks(runCurrentVoice);
                         if(currentPlayImage.getDrawable() == context.getResources().getDrawable(R.drawable.pause))
                         currentPlayImage.setImageDrawable(context.getResources().getDrawable(R.drawable.play));
                         }**/
                        //   currentMedia = mediaPlayer;
                        mediaPlayer.start();
                        //    currentMedia.seekTo(posi);
                        //     currentPlayImage = commentPlay;
                        /**   runCurrentVoice = new Runnable() {

                        @Override public void run() {
                        try {
                        int mCurrentPosition = currentMedia.getCurrentPosition() / 1000;
                        commentTime.setText(getAudioTime(mCurrentPosition));
                        commentSeekbar.setProgress((int) mCurrentPosition);
                        posi = currentMedia.getCurrentPosition();
                        if (currentMedia.getCurrentPosition() == currentMedia.getDuration()) {
                        togglePausePlay();
                        commentTime.setText(getAudioTime(comment.getRecordTime()));
                        commentSeekbar.setProgress(0);
                        posi = 0;
                        currentPlayImage = null;
                        runCurrentVoice = null;
                        currentMedia = null;
                        }else {
                        handVoice.postDelayed(this, 200);
                        }
                        } catch (IllegalStateException ed) {
                        ed.printStackTrace();
                        }
                        }
                        };
                         handVoice.postDelayed(runCurrentVoice, 200);**/
                        //Make sure you update Seekbar on UI thread

                        handler.postDelayed(runnable, 200);
                    }
                    togglePausePlay();

                    //Make sure you update Seekbar on UI thread


                    /**activity.runOnUiThread(new Runnable() {

                    @Override public void run() {
                    if(mediaPlayer != null){
                    int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    commentTime.setText(getAudioTime(mCurrentPosition));
                    commentSeekbar.setProgress(mCurrentPosition);
                    if(mediaPlayer.getCurrentPosition() == mediaPlayer.getDuration()){
                    togglePausePlay();
                    commentTime.setText(getAudioTime(comment.getRecordTime()));
                    commentSeekbar.setProgress(0);
                    }
                    }
                    mHandler.postDelayed(this, 100);
                    }
                    });**/
                    //int p = mediaPlayer.getCurrentPosition();
                    //int duration = mediaPlayer.getDuration();
                    //if (commentSeekbar != null) {
                    //if (duration > 0) {
                    // use long to avoid overflow
                    //long pos = 1000L * p / duration;
                    // commentSeekbar.setProgress((int) pos);
                    //}
                    //get buffer percentage
                    //int percent = mediaPlayer.getBufferPercentage();
                    //set buffer progress
                    //mSeekBar.setSecondaryProgress(percent * 10);
                    //}

                    /**if (mEndTime != null)
                     mEndTime.setText(stringToTime(duration));
                     if (mCurrentTime != null) {
                     Log.e(TAG, "position:" + position + " -> duration:" + duration);
                     mCurrentTime.setText(stringToTime(position));
                     if(mediaPlayer.isComplete()){
                     mCurrentTime.setText(stringToTime(duration));
                     }
                     }**/

                    Toast.makeText(context, "Playing Audio", Toast.LENGTH_LONG).show();

                }
            });
            commentSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (mediaPlayer != null && fromUser) {
                        mediaPlayer.seekTo(progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            commentTime.setText(getAudioTime(comment.RecordTime));
            commentDate.setText(comment.getDate() + "|" + comment.getTime());

            if (comment.isTeacher()) {

                commentLinear.setGravity(Gravity.LEFT);
                commentName.setTextColor(context.getResources().getColor(R.color.orange));
                commentAudio.setCardBackgroundColor(context.getResources().getColor(R.color.orange));
                commentName.setText(comment.getName());
            } else if (comment.getUser().matches(dbu.getCurrentUser().getUser())) {
                commentLinear.setGravity(Gravity.RIGHT);
                commentName.setTextColor(context.getResources().getColor(R.color.blue));
                commentAudio.setCardBackgroundColor(context.getResources().getColor(R.color.blue));
                commentName.setText("أنت");
            } else {
                commentLinear.setGravity(Gravity.RIGHT);
                commentName.setTextColor(context.getResources().getColor(R.color.purple));
                commentAudio.setCardBackgroundColor(context.getResources().getColor(R.color.purple));
                commentName.setText(comment.getName());
            }
            /**if(position > 0 && getItem(position-1).getName().matches(getItem(position).getName())){
             commentName.setVisibility(View.GONE);
             }**/

            /**if (comment.getImage1() != null)
             gridArray.add(comment.getImage1());
             if (comment.getImage2() != null)
             gridArray.add(comment.getImage2());
             if (comment.getImage3() != null)
             gridArray.add(comment.getImage3());
             if (comment.getImage4() != null)
             gridArray.add(comment.getImage4());
             if (comment.getImage5() != null)
             gridArray.add(comment.getImage5());
             if (comment.getImage6() != null)
             gridArray.add(comment.getImage6());
             if (comment.getImage7() != null)
             gridArray.add(comment.getImage7());
             if (comment.getImage8() != null)
             gridArray.add(comment.getImage8());
             if (comment.getImage9() != null)
             gridArray.add(comment.getImage9());
             if (comment.getImage10() != null)
             gridArray.add(comment.getImage10());
             if (position == 0) {
             Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.aaaa);
             gridArray.add(bm);
             gridArray.add(bm);
             gridArray.add(bm);
             gridArray.add(bm);


             }**/


            commentLinear.setTag(position);
            commentLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    if (!isDateShowed) {
                        isDateShowed = true;
                        Animation animation = AnimationUtils.loadAnimation(context, R.anim.down_from_top);
                        commentDate.startAnimation(animation);
                        commentDate.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isDateShowed = false;
                                Animation animation = AnimationUtils.loadAnimation(context, R.anim.up_from_bottom);
                                commentDate.startAnimation(animation);
                                commentDate.setVisibility(View.GONE);

                            }
                        }, 4000);
                    } else {
                        isDateShowed = false;
                        Animation animation = AnimationUtils.loadAnimation(context, R.anim.up_from_bottom);
                        commentDate.startAnimation(animation);
                        commentDate.setVisibility(View.GONE);
                    }
                }
            });


        }
    }

    public class CommentVoiceReply extends RecyclerView.ViewHolder {

        TextView commentDate;
        TextView commentName;
        TextView commentReplyName, commentReplyText, commentReplyTextIcon;
        LinearLayout commentLinear, commentReplyIcons;
        CardView commentAudio, commentReply, commentReplyMessage;
        ImageView commentPlay;
        TextView commentTime;
        SeekBar commentSeekbar;
        MediaPlayer mediaPlayer;
        ImageView commentVoiceImg, commentReplyColor, commentReplyIcon;
        ProgressBar commentProgressV;
        private boolean isDateShowed = false;
        PrepareVoice prepareVoice;


        public CommentVoiceReply(View view) {
            super(view);
            commentName = itemView.findViewById(R.id.commentName);
            commentDate = itemView.findViewById(R.id.commentDate);
            commentName = itemView.findViewById(R.id.commentName);
            commentLinear = itemView.findViewById(R.id.comment_voice_reply);
            commentAudio = itemView.findViewById(R.id.commentAudio);
            commentPlay = itemView.findViewById(R.id.commentPlay);
            commentTime = itemView.findViewById(R.id.commentTime);
            commentSeekbar = itemView.findViewById(R.id.commentSeekbar);
            commentProgressV = itemView.findViewById(R.id.loadingVoice);
            commentVoiceImg = itemView.findViewById(R.id.commentVoiceImg);
            commentReply = itemView.findViewById(R.id.commentReply);
            commentReplyIcons = itemView.findViewById(R.id.layoutIconsReply);
            commentReplyColor = itemView.findViewById(R.id.replyColor);
            commentReplyName = itemView.findViewById(R.id.replyName);
            commentReplyText = itemView.findViewById(R.id.replyText);
            commentReplyIcon = itemView.findViewById(R.id.replyIcon);
            commentReplyTextIcon = itemView.findViewById(R.id.txtIconReply);
            commentReplyMessage = itemView.findViewById(R.id.replyMessage);
            mediaPlayer = new MediaPlayer();
            prepareVoice = new PrepareVoice(mediaPlayer, commentVoiceImg, commentProgressV);
        }

        private void togglePausePlay() {

            if (mediaPlayer.isPlaying()) {
                commentPlay.setImageResource(R.drawable.pause);
            } else {
                commentPlay.setImageResource(R.drawable.play);
            }
        }

        public void bind(final CommentListChildItem comment, int position) {

            Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
            commentLinear.startAnimation(animation);
            lastPosition = position;
            if (position != 0 && comment.getUser().matches(comments.get(position - 1).getUser()))
                commentName.setVisibility(View.GONE);
            else
                commentName.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(comment.getReply())) {
                commentReply.setVisibility(View.VISIBLE);
                CommentListChildItem replyComment = new CommentListChildItem();
                for (int i = 0; i < dbc.allIds().size(); i++) {
                    if (dbc.allIds().get(i).matches(comment.getReply()))
                        replyComment = dbc.allComments().get(i);
                }
                if (replyComment.isTeacher()) {
                    commentReplyName.setText(replyComment.getName());
                    commentReplyColor.setImageResource(R.drawable.reply_t);
                    commentReplyName.setTextColor(context.getResources().getColor(R.color.orange));
                } else if (replyComment.getUser().matches(dbu.getCurrentUser().getUser())) {
                    commentReplyName.setText("أنت");
                    commentReplyColor.setImageResource(R.drawable.reply_s);
                    commentReplyName.setTextColor(context.getResources().getColor(R.color.blue));
                } else {
                    commentReplyName.setText(replyComment.getName());
                    commentReplyColor.setImageResource(R.drawable.reply);
                    commentReplyName.setTextColor(context.getResources().getColor(R.color.purple));
                }
                  if (!TextUtils.isEmpty(replyComment.getComment())) {
                    if (replyComment.getComment().toLowerCase(Locale.getDefault()).contains(searchString)) {

                        int startPos = replyComment.getComment().toLowerCase(Locale.getDefault()).indexOf(searchString);
                        int endPos = startPos + searchString.length();

                        Spannable spanString = Spannable.Factory.getInstance().newSpannable(replyComment.getComment());
                        spanString.setSpan(new BackgroundColorSpan(Color.YELLOW), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        commentReplyText.setText(spanString);
                    } else
                    commentReplyText.setText(replyComment.getComment());
                }else
                    commentReplyMessage.setVisibility(View.GONE);
                int imgCount = 0;
                ArrayList<String> images = new ArrayList<>();
                if (!TextUtils.isEmpty(replyComment.getImage1())) {
                    imgCount++;
                    images.add(replyComment.getImage1());
                }
                if (!TextUtils.isEmpty(replyComment.getImage2())) {
                    imgCount++;
                    images.add(replyComment.getImage2());
                }
                if (!TextUtils.isEmpty(replyComment.getImage3())) {
                    imgCount++;
                    images.add(replyComment.getImage3());
                }
                if (!TextUtils.isEmpty(replyComment.getImage4())) {
                    imgCount++;
                    images.add(replyComment.getImage4());
                }
                if (!TextUtils.isEmpty(replyComment.getImage5())) {
                    imgCount++;
                    images.add(replyComment.getImage5());
                }
                if (!TextUtils.isEmpty(replyComment.getImage6())) {
                    imgCount++;
                    images.add(replyComment.getImage6());
                }
                if (!TextUtils.isEmpty(replyComment.getImage7())) {
                    imgCount++;
                    images.add(replyComment.getImage7());
                }
                if (!TextUtils.isEmpty(replyComment.getImage8())) {
                    imgCount++;
                    images.add(replyComment.getImage8());
                }
                if (!TextUtils.isEmpty(replyComment.getImage9())) {
                    imgCount++;
                    images.add(replyComment.getImage9());
                }
                if (imgCount == 0) {
                    commentReplyIcons.setVisibility(View.GONE);
                } else if (imgCount == 1) {
                    commentReplyIcons.setVisibility(View.VISIBLE);
                    commentReplyTextIcon.setText("صورة");
                    commentReplyIcon.setImageResource(R.drawable.image);
                } else {
                    commentReplyIcons.setVisibility(View.VISIBLE);
                    commentReplyTextIcon.setText("صور");
                    commentReplyIcon.setImageResource(R.drawable.images);
                }
                if (replyComment.getRecordTime() > 0) {
                    commentReplyIcons.setVisibility(View.VISIBLE);
                    commentReplyTextIcon.setText("تسجيل صوتي" + "(" + getAudioTime(replyComment.getRecordTime()) + ")");
                    commentReplyIcon.setImageResource(R.drawable.voice_comment);
                }
            } else {
                commentReply.setVisibility(View.GONE);
            }
            commentAudio.setVisibility(View.VISIBLE);
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/ElectronicInstitute/media/Voices");
            final File mSaveBit = new File(myDir, comment.getRecrordData() + ".3gp");
            commentProgressV.setVisibility(View.VISIBLE);
            commentVoiceImg.setVisibility(View.GONE);
            // commentPlay.setClickable(false);
            //commentSeekbar.setEnabled(false);
            //commentSeekbar.setClickable(false);
            //commentSeekbar.setFocusable(false);
            if (mSaveBit.exists()) {
                prepareVoice.source = mSaveBit.getPath();
                prepareVoice.execute();
            } else {
                myDir.mkdirs();
                try {
                    FileOutputStream fos = new FileOutputStream(mSaveBit.getPath());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                StorageReference refe = FirebaseStorage.getInstance().getReference().child("Comment Voices/" + comment.getRecrordData() + ".3gp");
                try {

                    final File localFile = File.createTempFile("Voices", "3gp");
                    refe.getFile(mSaveBit).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            prepareVoice.source = mSaveBit.getPath();
                            prepareVoice.execute();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "e" + "voice" + "  " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (IOException e) {
                    Toast.makeText(context, "e" + "voice" + "  " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            commentPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    } else {
                        mediaPlayer.start();
                    }
                    togglePausePlay();
                    final Handler handler = new Handler();
                    //Make sure you update Seekbar on UI thread
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                                commentTime.setText(getAudioTime(mCurrentPosition));
                                commentSeekbar.setProgress((int) mCurrentPosition);
                                if (mediaPlayer.getCurrentPosition() == mediaPlayer.getDuration()) {
                                    togglePausePlay();
                                    commentTime.setText(getAudioTime(comment.getRecordTime()));
                                    commentSeekbar.setProgress(0);
                                }
                                handler.postDelayed(this, 1000);
                            } catch (IllegalStateException ed) {
                                ed.printStackTrace();
                            }
                        }
                    };
                    handler.postDelayed(runnable, 1000);
                    /**activity.runOnUiThread(new Runnable() {

                    @Override public void run() {
                    if(mediaPlayer != null){
                    int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                    commentTime.setText(getAudioTime(mCurrentPosition));
                    commentSeekbar.setProgress(mCurrentPosition);
                    if(mediaPlayer.getCurrentPosition() == mediaPlayer.getDuration()){
                    togglePausePlay();
                    commentTime.setText(getAudioTime(comment.getRecordTime()));
                    commentSeekbar.setProgress(0);
                    }
                    }
                    mHandler.postDelayed(this, 100);
                    }
                    });**/
                    //int p = mediaPlayer.getCurrentPosition();
                    //int duration = mediaPlayer.getDuration();
                    //if (commentSeekbar != null) {
                    //if (duration > 0) {
                    // use long to avoid overflow
                    //long pos = 1000L * p / duration;
                    // commentSeekbar.setProgress((int) pos);
                    //}
                    //get buffer percentage
                    //int percent = mediaPlayer.getBufferPercentage();
                    //set buffer progress
                    //mSeekBar.setSecondaryProgress(percent * 10);
                    //}

                    /**if (mEndTime != null)
                     mEndTime.setText(stringToTime(duration));
                     if (mCurrentTime != null) {
                     Log.e(TAG, "position:" + position + " -> duration:" + duration);
                     mCurrentTime.setText(stringToTime(position));
                     if(mediaPlayer.isComplete()){
                     mCurrentTime.setText(stringToTime(duration));
                     }
                     }**/

                    Toast.makeText(context, "Playing Audio", Toast.LENGTH_LONG).show();

                }
            });
            commentSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (mediaPlayer != null && fromUser) {
                        mediaPlayer.seekTo(progress * 1000);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            commentSeekbar.setMax(comment.RecordTime);
            commentSeekbar.setProgress(0);
            commentTime.setText(getAudioTime(comment.RecordTime));
            commentDate.setText(comment.getDate() + "|" + comment.getTime());

            if (comment.isTeacher()) {

                commentLinear.setGravity(Gravity.LEFT);
                commentName.setTextColor(context.getResources().getColor(R.color.orange));
                commentAudio.setCardBackgroundColor(context.getResources().getColor(R.color.orange));
                commentName.setText(comment.getName());

            } else if (comment.getUser().matches(dbu.getCurrentUser().getUser())) {
                commentLinear.setGravity(Gravity.RIGHT);
                commentName.setTextColor(context.getResources().getColor(R.color.blue));
                commentAudio.setCardBackgroundColor(context.getResources().getColor(R.color.blue));
                commentName.setText("أنت");

            } else {
                commentLinear.setGravity(Gravity.RIGHT);
                commentName.setTextColor(context.getResources().getColor(R.color.purple));
                commentAudio.setCardBackgroundColor(context.getResources().getColor(R.color.purple));
                commentName.setText(comment.getName());

            }
            /**if(position > 0 && getItem(position-1).getName().matches(getItem(position).getName())){
             commentName.setVisibility(View.GONE);
             }**/

            /**if (comment.getImage1() != null)
             gridArray.add(comment.getImage1());
             if (comment.getImage2() != null)
             gridArray.add(comment.getImage2());
             if (comment.getImage3() != null)
             gridArray.add(comment.getImage3());
             if (comment.getImage4() != null)
             gridArray.add(comment.getImage4());
             if (comment.getImage5() != null)
             gridArray.add(comment.getImage5());
             if (comment.getImage6() != null)
             gridArray.add(comment.getImage6());
             if (comment.getImage7() != null)
             gridArray.add(comment.getImage7());
             if (comment.getImage8() != null)
             gridArray.add(comment.getImage8());
             if (comment.getImage9() != null)
             gridArray.add(comment.getImage9());
             if (comment.getImage10() != null)
             gridArray.add(comment.getImage10());
             if (position == 0) {
             Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.aaaa);
             gridArray.add(bm);
             gridArray.add(bm);
             gridArray.add(bm);
             gridArray.add(bm);


             }**/


            commentLinear.setTag(position);
            commentLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    if (!isDateShowed) {
                        isDateShowed = true;
                        Animation animation = AnimationUtils.loadAnimation(context, R.anim.down_from_top);
                        commentDate.startAnimation(animation);
                        commentDate.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isDateShowed = false;
                                Animation animation = AnimationUtils.loadAnimation(context, R.anim.up_from_bottom);
                                commentDate.startAnimation(animation);
                                commentDate.setVisibility(View.GONE);

                            }
                        }, 4000);
                    } else {
                        isDateShowed = false;
                        Animation animation = AnimationUtils.loadAnimation(context, R.anim.up_from_bottom);
                        commentDate.startAnimation(animation);
                        commentDate.setVisibility(View.GONE);
                    }
                }
            });


        }
    }

    public class CommentImages extends RecyclerView.ViewHolder {

        TextView commentDate;
        TextView commentName;
        TextView commentText;
        MyGridView commentImages;
        ArrayList<String> gridArray;
        CustomGridViewAdapter customGridAdapter;
        LinearLayout commentLinear;
        CardView commentMessage;
        FrameLayout commentFrameImageOne;
        LinearLayout commentLinearImageTwo;
        LinearLayout commentLinearImageThree;
        ImageView commentImage, commentImage1, commentImage2, commentImage11, commentImage12, commentImage13;
        ProgressBar commentProgress, commentProgress1, commentProgress2, commentProgress11, commentProgress12, commentProgress13;
        private boolean isDateShowed = false;
        GetImage getImage, getImage1, getImage2, getImage11, getImage12, getImage13;
        int num;


        public CommentImages(View view, int n) {
            super(view);
            commentName = itemView.findViewById(R.id.commentName);
            commentText = itemView.findViewById(R.id.commentText);
            commentDate = itemView.findViewById(R.id.commentDate);
            commentName = itemView.findViewById(R.id.commentName);
            commentLinear = itemView.findViewById(R.id.comment_images_item);
            commentMessage = itemView.findViewById(R.id.commentMessage);
            num = n;
            if (n == 1) {
                commentFrameImageOne = itemView.findViewById(R.id.commentFrameImageOne);
                commentImage = itemView.findViewById(R.id.commentImage);
                commentProgress = itemView.findViewById(R.id.loadingImage);
                getImage = new GetImage(commentImage, commentProgress);
            } else if (n == 2) {
                commentLinearImageTwo = itemView.findViewById(R.id.commentLinearImageTwo);
                commentImage1 = itemView.findViewById(R.id.commentImage1);
                commentImage2 = itemView.findViewById(R.id.commentImage2);
                commentProgress1 = itemView.findViewById(R.id.loadingImage1);
                commentProgress2 = itemView.findViewById(R.id.loadingImage2);
                getImage1 = new GetImage(commentImage1, commentProgress1);
                getImage2 = new GetImage(commentImage2, commentProgress2);
            } else if (n == 3) {
                commentLinearImageThree = itemView.findViewById(R.id.commentLinearImageThree);
                commentImage11 = itemView.findViewById(R.id.commentImage11);
                commentImage12 = itemView.findViewById(R.id.commentImage12);
                commentImage13 = itemView.findViewById(R.id.commentImage13);
                commentProgress11 = itemView.findViewById(R.id.loadingImage11);
                commentProgress12 = itemView.findViewById(R.id.loadingImage12);
                commentProgress13 = itemView.findViewById(R.id.loadingImage13);
                getImage11 = new GetImage(commentImage11, commentProgress11);
                getImage12 = new GetImage(commentImage12, commentProgress12);
                getImage13 = new GetImage(commentImage13, commentProgress13);
            } else if (n > 3) {
                commentImages = itemView.findViewById(R.id.commentImages);
            }
        }


        public void bind(final CommentListChildItem comment, int position) {
            Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
            commentLinear.startAnimation(animation);
            lastPosition = position;
            if (position != 0 && comment.getUser().matches(comments.get(position - 1).getUser()))
                commentName.setVisibility(View.GONE);
            else
                commentName.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(comment.getComment())) {
                commentText.setVisibility(View.VISIBLE);
                if (comment.getComment().toLowerCase(Locale.getDefault()).contains(searchString)) {

                    int startPos = comment.getComment().toLowerCase(Locale.getDefault()).indexOf(searchString);
                    int endPos = startPos + searchString.length();

                    Spannable spanString = Spannable.Factory.getInstance().newSpannable(comment.getComment());
                    spanString.setSpan(new BackgroundColorSpan(Color.YELLOW), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    commentText.setText(spanString);
                } else
                    commentText.setText(comment.getComment());
            }
            commentDate.setText(comment.getDate() + "|" + comment.getTime());

            if (comment.isTeacher()) {

                commentLinear.setGravity(Gravity.LEFT);
                try {
                    commentImages.setGravity(Gravity.LEFT);
                } catch (Exception e) {

                }
                //      commentImages.setGravity(Gravity.LEFT);
                commentName.setTextColor(context.getResources().getColor(R.color.orange));
                commentMessage.setCardBackgroundColor(context.getResources().getColor(R.color.orange));
                commentName.setText(comment.getName());
            } else if (comment.getUser().matches(dbu.getCurrentUser().getUser())) {
                commentLinear.setGravity(Gravity.RIGHT);
                try {
                    commentImages.setGravity(Gravity.RIGHT);
                } catch (Exception e) {

                }
                commentName.setTextColor(context.getResources().getColor(R.color.blue));
                commentMessage.setCardBackgroundColor(context.getResources().getColor(R.color.blue));
                commentName.setText("أنت");
            } else {
                commentLinear.setGravity(Gravity.RIGHT);
                try {
                    commentImages.setGravity(Gravity.RIGHT);
                } catch (Exception e) {

                }
                commentName.setTextColor(context.getResources().getColor(R.color.purple));
                commentMessage.setCardBackgroundColor(context.getResources().getColor(R.color.purple));
                commentName.setText(comment.getName());
            }
            /**if(position > 0 && getItem(position-1).getName().matches(getItem(position).getName())){
             commentName.setVisibility(View.GONE);
             }**/
            int imgCount = 0;
            ArrayList<String> images = new ArrayList<>();
            if (!TextUtils.isEmpty(comment.getImage1())) {
                imgCount++;
                images.add(comment.getImage1());
            }
            if (!TextUtils.isEmpty(comment.getImage2())) {
                imgCount++;
                images.add(comment.getImage2());
            }
            if (!TextUtils.isEmpty(comment.getImage3())) {
                imgCount++;
                images.add(comment.getImage3());
            }
            if (!TextUtils.isEmpty(comment.getImage4())) {
                imgCount++;
                images.add(comment.getImage4());
            }
            if (!TextUtils.isEmpty(comment.getImage5())) {
                imgCount++;
                images.add(comment.getImage5());
            }
            if (!TextUtils.isEmpty(comment.getImage6())) {
                imgCount++;
                images.add(comment.getImage6());
            }
            if (!TextUtils.isEmpty(comment.getImage7())) {
                imgCount++;
                images.add(comment.getImage7());
            }
            if (!TextUtils.isEmpty(comment.getImage8())) {
                imgCount++;
                images.add(comment.getImage8());
            }
            if (!TextUtils.isEmpty(comment.getImage9())) {
                imgCount++;
                images.add(comment.getImage9());
            }
            if (num == 1) {
                commentFrameImageOne.setVisibility(View.VISIBLE);
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/ElectronicInstitute/media/Images");
                final File mSaveBit = new File(myDir, images.get(0) + ".jpg");
                commentProgress.setVisibility(View.VISIBLE);
                commentImage.setVisibility(View.GONE);
                if (mSaveBit.exists()) {
                    GetImage g = new GetImage(commentImage, commentProgress);
                    g.source = mSaveBit.getPath();
                    g.size = 600;
                    g.execute();
                    try {

                    } catch (Exception e) {
                        Bitmap b = null;
                        try {
                            b = ImageRounded.getRoundedCornerBitmap(BitmapFactory.decodeFile(mSaveBit.getPath()), 100);
                        } catch (Exception e1) {
                        }
                        commentProgress.setVisibility(View.GONE);
                        commentImage.setVisibility(View.VISIBLE);
                        commentImage.setImageBitmap(b);
                    }
                } else {
                    myDir.mkdirs();
                    try {
                        FileOutputStream fos = new FileOutputStream(mSaveBit.getPath());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    StorageReference refe = FirebaseStorage.getInstance().getReference().child("Comment Images/" + images.get(0) + ".jpg");
                    try {

                        final File localFile = File.createTempFile("Voices", "3gp");
                        refe.getFile(mSaveBit).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                getImage.source = mSaveBit.getPath();
                                getImage.size = 400;
                                getImage.execute();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "e" + "image" + "  " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (IOException e) {
                        Toast.makeText(context, "e" + "image" + "  " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            } else if (num == 2) {
                commentLinearImageTwo.setVisibility(View.VISIBLE);
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/ElectronicInstitute/media/Images");
                final File mSaveBit = new File(myDir, images.get(0) + ".jpg");
                commentProgress1.setVisibility(View.VISIBLE);
                commentImage1.setVisibility(View.GONE);
                if (mSaveBit.exists()) {
                    GetImage g = new GetImage(commentImage1, commentProgress1);
                    g.source = mSaveBit.getPath();
                    g.size = 300;
                    g.execute();
                } else {
                    myDir.mkdirs();
                    try {
                        FileOutputStream fos = new FileOutputStream(mSaveBit.getPath());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    StorageReference refe = FirebaseStorage.getInstance().getReference().child("Comment Images/" + images.get(0) + ".jpg");
                    try {
                        Toast.makeText(context, String.valueOf("image1"), Toast.LENGTH_LONG).show();
                        final File localFile = File.createTempFile("Voices", "3gp");
                        refe.getFile(mSaveBit).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                getImage1.source = mSaveBit.getPath();
                                getImage1.size = 300;
                                getImage1.execute();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "e" + "image1" + "  " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (IOException e) {
                        Toast.makeText(context, "e" + "image1" + "  " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                String root1 = Environment.getExternalStorageDirectory().toString();
                File myDir1 = new File(root1 + "/ElectronicInstitute/media/Images");
                final File mSaveBit1 = new File(myDir1, images.get(1) + ".jpg");
                commentProgress2.setVisibility(View.VISIBLE);
                commentImage2.setVisibility(View.GONE);
                if (mSaveBit1.exists()) {
                    GetImage g = new GetImage(commentImage2, commentProgress2);
                    g.source = mSaveBit1.getPath();
                    g.size = 300;
                    g.execute();
                } else {
                    myDir1.mkdirs();
                    try {
                        FileOutputStream fos = new FileOutputStream(mSaveBit1.getPath());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    StorageReference refe = FirebaseStorage.getInstance().getReference().child("Comment Images/" + images.get(1) + ".jpg");
                    try {
                        Toast.makeText(context, String.valueOf("image2"), Toast.LENGTH_LONG).show();
                        final File localFile = File.createTempFile("Voices", "3gp");
                        refe.getFile(mSaveBit1).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                getImage2.source = mSaveBit1.getPath();
                                getImage2.size = 300;
                                getImage2.execute();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "e" + "image2" + "  " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (IOException e) {
                        Toast.makeText(context, "e" + "image2" + "  " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            } else if (num == 3) {
                commentLinearImageThree.setVisibility(View.VISIBLE);
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/ElectronicInstitute/media/Images");
                final File mSaveBit = new File(myDir, images.get(0) + ".jpg");
                commentProgress11.setVisibility(View.VISIBLE);
                commentImage11.setVisibility(View.GONE);
                if (mSaveBit.exists()) {
                    GetImage g = new GetImage(commentImage11, commentProgress11);
                    g.source = mSaveBit.getPath();
                    g.size = 200;
                    g.execute();
                } else {
                    myDir.mkdirs();
                    try {
                        FileOutputStream fos = new FileOutputStream(mSaveBit.getPath());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    StorageReference refe = FirebaseStorage.getInstance().getReference().child("Comment Images/" + images.get(0) + ".jpg");
                    try {
                        Toast.makeText(context, String.valueOf("image11"), Toast.LENGTH_LONG).show();
                        final File localFile = File.createTempFile("Voices", "3gp");
                        refe.getFile(mSaveBit).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                getImage11.source = mSaveBit.getPath();
                                getImage11.size = 200;
                                getImage11.execute();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "e" + "image11" + "  " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (IOException e) {
                        Toast.makeText(context, "e" + "image11" + "  " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                String root1 = Environment.getExternalStorageDirectory().toString();
                File myDir1 = new File(root1 + "/ElectronicInstitute/media/Images");
                final File mSaveBit1 = new File(myDir1, images.get(1) + ".jpg");
                commentProgress12.setVisibility(View.VISIBLE);
                commentImage12.setVisibility(View.GONE);
                if (mSaveBit1.exists()) {
                    GetImage g = new GetImage(commentImage12, commentProgress12);
                    g.source = mSaveBit1.getPath();
                    g.size = 200;
                    g.execute();
                } else {
                    myDir1.mkdirs();
                    try {
                        FileOutputStream fos = new FileOutputStream(mSaveBit1.getPath());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    StorageReference refe = FirebaseStorage.getInstance().getReference().child("Comment Images/" + images.get(1) + ".jpg");
                    try {
                        Toast.makeText(context, String.valueOf("image12"), Toast.LENGTH_LONG).show();
                        final File localFile = File.createTempFile("Voices", "3gp");
                        refe.getFile(mSaveBit1).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                getImage12.source = mSaveBit1.getPath();
                                getImage12.size = 200;
                                getImage12.execute();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "e" + "image12" + "  " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (IOException e) {
                        Toast.makeText(context, "e" + "image12" + "  " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                String root2 = Environment.getExternalStorageDirectory().toString();
                File myDir2 = new File(root2 + "/ElectronicInstitute/media/Images");
                final File mSaveBit2 = new File(myDir2, images.get(2) + ".jpg");
                commentProgress13.setVisibility(View.VISIBLE);
                commentImage13.setVisibility(View.GONE);
                if (mSaveBit2.exists()) {
                    GetImage g = new GetImage(commentImage13, commentProgress13);
                    g.source = mSaveBit2.getPath();
                    g.size = 200;
                    g.execute();
                } else {
                    myDir2.mkdirs();
                    try {
                        FileOutputStream fos = new FileOutputStream(mSaveBit2.getPath());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    StorageReference refe = FirebaseStorage.getInstance().getReference().child("Comment Images/" + images.get(2) + ".jpg");
                    try {
                        Toast.makeText(context, String.valueOf("image13"), Toast.LENGTH_LONG).show();
                        final File localFile = File.createTempFile("Voices", "3gp");
                        refe.getFile(mSaveBit2).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                getImage13.source = mSaveBit2.getPath();
                                getImage13.size = 200;
                                getImage13.execute();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "e" + "image13" + "  " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (IOException e) {
                        Toast.makeText(context, "e" + "image13" + "  " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            } else if (num > 3) {
                gridArray = images;
                customGridAdapter = new CustomGridViewAdapter(context, R.layout.row_grid, gridArray);
                commentImages.setVisibility(View.VISIBLE);
                commentImages.setAdapter(customGridAdapter);
            }

            /**if (comment.getImage1() != null)
             gridArray.add(comment.getImage1());
             if (comment.getImage2() != null)
             gridArray.add(comment.getImage2());
             if (comment.getImage3() != null)
             gridArray.add(comment.getImage3());
             if (comment.getImage4() != null)
             gridArray.add(comment.getImage4());
             if (comment.getImage5() != null)
             gridArray.add(comment.getImage5());
             if (comment.getImage6() != null)
             gridArray.add(comment.getImage6());
             if (comment.getImage7() != null)
             gridArray.add(comment.getImage7());
             if (comment.getImage8() != null)
             gridArray.add(comment.getImage8());
             if (comment.getImage9() != null)
             gridArray.add(comment.getImage9());
             if (comment.getImage10() != null)
             gridArray.add(comment.getImage10());
             if (position == 0) {
             Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.aaaa);
             gridArray.add(bm);
             gridArray.add(bm);
             gridArray.add(bm);
             gridArray.add(bm);


             }**/


            commentLinear.setTag(position);
            commentLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    if (!isDateShowed) {
                        isDateShowed = true;
                        Animation animation = AnimationUtils.loadAnimation(context, R.anim.down_from_top);
                        commentDate.startAnimation(animation);
                        commentDate.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isDateShowed = false;
                                Animation animation = AnimationUtils.loadAnimation(context, R.anim.up_from_bottom);
                                commentDate.startAnimation(animation);
                                commentDate.setVisibility(View.GONE);

                            }
                        }, 4000);
                    } else {
                        isDateShowed = false;
                        Animation animation = AnimationUtils.loadAnimation(context, R.anim.up_from_bottom);
                        commentDate.startAnimation(animation);
                        commentDate.setVisibility(View.GONE);
                    }
                }
            });


        }
    }

    public class CommentImagesReply extends RecyclerView.ViewHolder {

        TextView commentDate;
        TextView commentName;
        TextView commentText, commentReplyName, commentReplyText, commentReplyTextIcon;
        MyGridView commentImages;
        ArrayList<String> gridArray;
        CustomGridViewAdapter customGridAdapter;
        LinearLayout commentLinear, commentReplyIcons;
        CardView commentMessage, commentReply, commentReplyMessage;

        FrameLayout commentFrameImageOne;
        LinearLayout commentLinearImageTwo;
        LinearLayout commentLinearImageThree;
        ImageView commentImage, commentImage1, commentImage2, commentImage11, commentImage12, commentImage13, commentReplyColor, commentReplyIcon;
        ProgressBar commentProgress, commentProgress1, commentProgress2, commentProgress11, commentProgress12, commentProgress13;
        private boolean isDateShowed = false;
        GetImage getImage, getImage1, getImage2, getImage11, getImage12, getImage13;
        int num;

        public CommentImagesReply(View view, int n) {
            super(view);
            commentName = itemView.findViewById(R.id.commentName);
            commentText = itemView.findViewById(R.id.commentText);
            commentDate = itemView.findViewById(R.id.commentDate);
            commentName = itemView.findViewById(R.id.commentName);
            commentLinear = itemView.findViewById(R.id.comment_images_reply);
            commentMessage = itemView.findViewById(R.id.commentMessage);
            commentReply = itemView.findViewById(R.id.commentReply);
            commentReplyIcons = itemView.findViewById(R.id.layoutIconsReply);
            commentReplyColor = itemView.findViewById(R.id.replyColor);
            commentReplyName = itemView.findViewById(R.id.replyName);
            commentReplyText = itemView.findViewById(R.id.replyText);
            commentReplyIcon = itemView.findViewById(R.id.replyIcon);
            commentReplyTextIcon = itemView.findViewById(R.id.txtIconReply);
            commentReplyMessage = itemView.findViewById(R.id.replyMessage);
            num = n;
            if (n == 1) {
                commentFrameImageOne = itemView.findViewById(R.id.commentFrameImageOne);
                commentImage = itemView.findViewById(R.id.commentImage);
                commentProgress = itemView.findViewById(R.id.loadingImage);
                getImage = new GetImage(commentImage, commentProgress);
            } else if (n == 2) {
                commentLinearImageTwo = itemView.findViewById(R.id.commentLinearImageTwo);
                commentImage1 = itemView.findViewById(R.id.commentImage1);
                commentImage2 = itemView.findViewById(R.id.commentImage2);
                commentProgress1 = itemView.findViewById(R.id.loadingImage1);
                commentProgress2 = itemView.findViewById(R.id.loadingImage2);
                getImage1 = new GetImage(commentImage1, commentProgress1);
                getImage2 = new GetImage(commentImage2, commentProgress2);
            } else if (n == 3) {
                commentLinearImageThree = itemView.findViewById(R.id.commentLinearImageThree);
                commentImage11 = itemView.findViewById(R.id.commentImage11);
                commentImage12 = itemView.findViewById(R.id.commentImage12);
                commentImage13 = itemView.findViewById(R.id.commentImage13);
                commentProgress11 = itemView.findViewById(R.id.loadingImage11);
                commentProgress12 = itemView.findViewById(R.id.loadingImage12);
                commentProgress13 = itemView.findViewById(R.id.loadingImage13);
                getImage11 = new GetImage(commentImage11, commentProgress11);
                getImage12 = new GetImage(commentImage12, commentProgress12);
                getImage13 = new GetImage(commentImage13, commentProgress13);
            } else if (n > 3) {
                commentImages = itemView.findViewById(R.id.commentImages);
            }
        }

        public void bind(final CommentListChildItem comment, int position) {
            Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
            commentLinear.startAnimation(animation);
            lastPosition = position;
            if (position != 0 && comment.getUser().matches(comments.get(position - 1).getUser()))
                commentName.setVisibility(View.GONE);
            else
                commentName.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(comment.getComment())) {
                commentMessage.setVisibility(View.VISIBLE);
                if (comment.getComment().toLowerCase(Locale.getDefault()).contains(searchString)) {

                    int startPos = comment.getComment().toLowerCase(Locale.getDefault()).indexOf(searchString);
                    int endPos = startPos + searchString.length();

                    Spannable spanString = Spannable.Factory.getInstance().newSpannable(comment.getComment());
                    spanString.setSpan(new BackgroundColorSpan(Color.YELLOW), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    commentText.setText(spanString);
                } else
                    commentText.setText(comment.getComment());
            }
            if (!TextUtils.isEmpty(comment.getReply())) {
                commentReply.setVisibility(View.VISIBLE);
                CommentListChildItem replyComment = new CommentListChildItem();
                for (int i = 0; i < dbc.allIds().size(); i++) {
                    if (dbc.allIds().get(i).matches(comment.getReply()))
                        replyComment = dbc.allComments().get(i);
                }
                if (replyComment.isTeacher()) {
                    commentReplyName.setText(replyComment.getName());
                    commentReplyColor.setImageResource(R.drawable.reply_t);
                    commentReplyName.setTextColor(context.getResources().getColor(R.color.orange));
                } else if (replyComment.getUser().matches(dbu.getCurrentUser().getUser())) {
                    commentReplyName.setText("أنت");
                    commentReplyColor.setImageResource(R.drawable.reply_s);
                    commentReplyName.setTextColor(context.getResources().getColor(R.color.blue));
                } else {
                    commentReplyName.setText(replyComment.getName());
                    commentReplyColor.setImageResource(R.drawable.reply);
                    commentReplyName.setTextColor(context.getResources().getColor(R.color.purple));
                }
                if (!TextUtils.isEmpty(replyComment.getComment())) {
                    if (replyComment.getComment().toLowerCase(Locale.getDefault()).contains(searchString)) {

                        int startPos = replyComment.getComment().toLowerCase(Locale.getDefault()).indexOf(searchString);
                        int endPos = startPos + searchString.length();

                        Spannable spanString = Spannable.Factory.getInstance().newSpannable(replyComment.getComment());
                        spanString.setSpan(new BackgroundColorSpan(Color.YELLOW), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        commentReplyText.setText(spanString);
                    } else
                    commentReplyText.setText(replyComment.getComment());
                }else
                    commentReplyMessage.setVisibility(View.GONE);
                int imgCount = 0;
                ArrayList<String> images = new ArrayList<>();
                if (!TextUtils.isEmpty(replyComment.getImage1())) {
                    imgCount++;
                    images.add(replyComment.getImage1());
                }
                if (!TextUtils.isEmpty(replyComment.getImage2())) {
                    imgCount++;
                    images.add(replyComment.getImage2());
                }
                if (!TextUtils.isEmpty(replyComment.getImage3())) {
                    imgCount++;
                    images.add(replyComment.getImage3());
                }
                if (!TextUtils.isEmpty(replyComment.getImage4())) {
                    imgCount++;
                    images.add(replyComment.getImage4());
                }
                if (!TextUtils.isEmpty(replyComment.getImage5())) {
                    imgCount++;
                    images.add(replyComment.getImage5());
                }
                if (!TextUtils.isEmpty(replyComment.getImage6())) {
                    imgCount++;
                    images.add(replyComment.getImage6());
                }
                if (!TextUtils.isEmpty(replyComment.getImage7())) {
                    imgCount++;
                    images.add(replyComment.getImage7());
                }
                if (!TextUtils.isEmpty(replyComment.getImage8())) {
                    imgCount++;
                    images.add(replyComment.getImage8());
                }
                if (!TextUtils.isEmpty(replyComment.getImage9())) {
                    imgCount++;
                    images.add(replyComment.getImage9());
                }
                if (imgCount == 0) {
                    commentReplyIcons.setVisibility(View.GONE);
                } else if (imgCount == 1) {
                    commentReplyIcons.setVisibility(View.VISIBLE);
                    commentReplyTextIcon.setText("صورة");
                    commentReplyIcon.setImageResource(R.drawable.image);
                } else {
                    commentReplyIcons.setVisibility(View.VISIBLE);
                    commentReplyTextIcon.setText("صور");
                    commentReplyIcon.setImageResource(R.drawable.images);
                }
                if (replyComment.getRecordTime() > 0) {
                    commentReplyIcons.setVisibility(View.VISIBLE);
                    commentReplyTextIcon.setText("تسجيل صوتي" + "(" + getAudioTime(replyComment.getRecordTime()) + ")");
                    commentReplyIcon.setImageResource(R.drawable.voice_comment);
                }
            } else {
                commentReply.setVisibility(View.GONE);
            }
            commentDate.setText(comment.getDate() + "|" + comment.getTime());

            if (comment.isTeacher()) {

                commentLinear.setGravity(Gravity.LEFT);
                try {
                    commentImages.setGravity(Gravity.LEFT);
                } catch (Exception e) {

                }
                commentName.setTextColor(context.getResources().getColor(R.color.orange));
                commentMessage.setCardBackgroundColor(context.getResources().getColor(R.color.orange));
                commentName.setText(comment.getName());

            } else if (comment.getUser().matches(dbu.getCurrentUser().getUser())) {
                commentLinear.setGravity(Gravity.RIGHT);
                try {
                    commentImages.setGravity(Gravity.RIGHT);
                } catch (Exception e) {

                }
                commentName.setTextColor(context.getResources().getColor(R.color.blue));
                commentMessage.setCardBackgroundColor(context.getResources().getColor(R.color.blue));
                commentName.setText("أنت");

            } else {
                commentLinear.setGravity(Gravity.RIGHT);
                try {
                    commentImages.setGravity(Gravity.RIGHT);
                } catch (Exception e) {

                }
                commentName.setTextColor(context.getResources().getColor(R.color.purple));
                commentMessage.setCardBackgroundColor(context.getResources().getColor(R.color.purple));
                commentName.setText(comment.getName());

            }
            /**if(position > 0 && getItem(position-1).getName().matches(getItem(position).getName())){
             commentName.setVisibility(View.GONE);
             }**/
            int imgCount = 0;
            ArrayList<String> images = new ArrayList<>();
            if (!TextUtils.isEmpty(comment.getImage1())) {
                imgCount++;
                images.add(comment.getImage1());
            }
            if (!TextUtils.isEmpty(comment.getImage2())) {
                imgCount++;
                images.add(comment.getImage2());
            }
            if (!TextUtils.isEmpty(comment.getImage3())) {
                imgCount++;
                images.add(comment.getImage3());
            }
            if (!TextUtils.isEmpty(comment.getImage4())) {
                imgCount++;
                images.add(comment.getImage4());
            }
            if (!TextUtils.isEmpty(comment.getImage5())) {
                imgCount++;
                images.add(comment.getImage5());
            }
            if (!TextUtils.isEmpty(comment.getImage6())) {
                imgCount++;
                images.add(comment.getImage6());
            }
            if (!TextUtils.isEmpty(comment.getImage7())) {
                imgCount++;
                images.add(comment.getImage7());
            }
            if (!TextUtils.isEmpty(comment.getImage8())) {
                imgCount++;
                images.add(comment.getImage8());
            }
            if (!TextUtils.isEmpty(comment.getImage9())) {
                imgCount++;
                images.add(comment.getImage9());
            }
            if (num == 1) {
                commentFrameImageOne.setVisibility(View.VISIBLE);
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/ElectronicInstitute/media/Images");
                final File mSaveBit = new File(myDir, images.get(0) + ".jpg");
                commentProgress.setVisibility(View.VISIBLE);
                commentImage.setVisibility(View.GONE);
                if (mSaveBit.exists()) {
                    GetImage g = new GetImage(commentImage, commentProgress);
                    g.source = mSaveBit.getPath();
                    g.size = 600;
                    g.execute();
                    try {

                    } catch (Exception e) {
                        Bitmap b = null;
                        try {
                            b = ImageRounded.getRoundedCornerBitmap(BitmapFactory.decodeFile(mSaveBit.getPath()), 100);
                        } catch (Exception e1) {
                        }
                        commentProgress.setVisibility(View.GONE);
                        commentImage.setVisibility(View.VISIBLE);
                        commentImage.setImageBitmap(b);
                    }
                } else {
                    myDir.mkdirs();
                    try {
                        FileOutputStream fos = new FileOutputStream(mSaveBit.getPath());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    StorageReference refe = FirebaseStorage.getInstance().getReference().child("Comment Images/" + images.get(0) + ".jpg");
                    try {

                        final File localFile = File.createTempFile("Voices", "3gp");
                        refe.getFile(mSaveBit).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                getImage.source = mSaveBit.getPath();
                                getImage.size = 400;
                                getImage.execute();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "e" + "image" + "  " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (IOException e) {
                        Toast.makeText(context, "e" + "image" + "  " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            } else if (num == 2) {
                commentLinearImageTwo.setVisibility(View.VISIBLE);
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/ElectronicInstitute/media/Images");
                final File mSaveBit = new File(myDir, images.get(0) + ".jpg");
                commentProgress1.setVisibility(View.VISIBLE);
                commentImage1.setVisibility(View.GONE);
                if (mSaveBit.exists()) {
                    GetImage g = new GetImage(commentImage1, commentProgress1);
                    g.source = mSaveBit.getPath();
                    g.size = 300;
                    g.execute();
                } else {
                    myDir.mkdirs();
                    try {
                        FileOutputStream fos = new FileOutputStream(mSaveBit.getPath());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    StorageReference refe = FirebaseStorage.getInstance().getReference().child("Comment Images/" + images.get(0) + ".jpg");
                    try {
                        Toast.makeText(context, String.valueOf("image1"), Toast.LENGTH_LONG).show();
                        final File localFile = File.createTempFile("Voices", "3gp");
                        refe.getFile(mSaveBit).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                getImage1.source = mSaveBit.getPath();
                                getImage1.size = 300;
                                getImage1.execute();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "e" + "image1" + "  " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (IOException e) {
                        Toast.makeText(context, "e" + "image1" + "  " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                String root1 = Environment.getExternalStorageDirectory().toString();
                File myDir1 = new File(root1 + "/ElectronicInstitute/media/Images");
                final File mSaveBit1 = new File(myDir1, images.get(1) + ".jpg");
                commentProgress2.setVisibility(View.VISIBLE);
                commentImage2.setVisibility(View.GONE);
                if (mSaveBit1.exists()) {
                    GetImage g = new GetImage(commentImage2, commentProgress2);
                    g.source = mSaveBit1.getPath();
                    g.size = 300;
                    g.execute();
                } else {
                    myDir1.mkdirs();
                    try {
                        FileOutputStream fos = new FileOutputStream(mSaveBit1.getPath());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    StorageReference refe = FirebaseStorage.getInstance().getReference().child("Comment Images/" + images.get(1) + ".jpg");
                    try {
                        Toast.makeText(context, String.valueOf("image2"), Toast.LENGTH_LONG).show();
                        final File localFile = File.createTempFile("Voices", "3gp");
                        refe.getFile(mSaveBit1).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                getImage2.source = mSaveBit1.getPath();
                                getImage2.size = 300;
                                getImage2.execute();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "e" + "image2" + "  " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (IOException e) {
                        Toast.makeText(context, "e" + "image2" + "  " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            } else if (num == 3) {
                commentLinearImageThree.setVisibility(View.VISIBLE);
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/ElectronicInstitute/media/Images");
                final File mSaveBit = new File(myDir, images.get(0) + ".jpg");
                commentProgress11.setVisibility(View.VISIBLE);
                commentImage11.setVisibility(View.GONE);
                if (mSaveBit.exists()) {
                    GetImage g = new GetImage(commentImage11, commentProgress11);
                    g.source = mSaveBit.getPath();
                    g.size = 200;
                    g.execute();
                } else {
                    myDir.mkdirs();
                    try {
                        FileOutputStream fos = new FileOutputStream(mSaveBit.getPath());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    StorageReference refe = FirebaseStorage.getInstance().getReference().child("Comment Images/" + images.get(0) + ".jpg");
                    try {
                        Toast.makeText(context, String.valueOf("image11"), Toast.LENGTH_LONG).show();
                        final File localFile = File.createTempFile("Voices", "3gp");
                        refe.getFile(mSaveBit).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                getImage11.source = mSaveBit.getPath();
                                getImage11.size = 200;
                                getImage11.execute();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "e" + "image11" + "  " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (IOException e) {
                        Toast.makeText(context, "e" + "image11" + "  " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                String root1 = Environment.getExternalStorageDirectory().toString();
                File myDir1 = new File(root1 + "/ElectronicInstitute/media/Images");
                final File mSaveBit1 = new File(myDir1, images.get(1) + ".jpg");
                commentProgress12.setVisibility(View.VISIBLE);
                commentImage12.setVisibility(View.GONE);
                if (mSaveBit1.exists()) {
                    GetImage g = new GetImage(commentImage12, commentProgress12);
                    g.source = mSaveBit1.getPath();
                    g.size = 200;
                    g.execute();
                } else {
                    myDir1.mkdirs();
                    try {
                        FileOutputStream fos = new FileOutputStream(mSaveBit1.getPath());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    StorageReference refe = FirebaseStorage.getInstance().getReference().child("Comment Images/" + images.get(1) + ".jpg");
                    try {
                        Toast.makeText(context, String.valueOf("image12"), Toast.LENGTH_LONG).show();
                        final File localFile = File.createTempFile("Voices", "3gp");
                        refe.getFile(mSaveBit1).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                getImage12.source = mSaveBit1.getPath();
                                getImage12.size = 200;
                                getImage12.execute();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "e" + "image12" + "  " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (IOException e) {
                        Toast.makeText(context, "e" + "image12" + "  " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                String root2 = Environment.getExternalStorageDirectory().toString();
                File myDir2 = new File(root2 + "/ElectronicInstitute/media/Images");
                final File mSaveBit2 = new File(myDir2, images.get(2) + ".jpg");
                commentProgress13.setVisibility(View.VISIBLE);
                commentImage13.setVisibility(View.GONE);
                if (mSaveBit2.exists()) {
                    GetImage g = new GetImage(commentImage13, commentProgress13);
                    g.source = mSaveBit2.getPath();
                    g.size = 200;
                    g.execute();
                } else {
                    myDir2.mkdirs();
                    try {
                        FileOutputStream fos = new FileOutputStream(mSaveBit2.getPath());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    StorageReference refe = FirebaseStorage.getInstance().getReference().child("Comment Images/" + images.get(2) + ".jpg");
                    try {
                        Toast.makeText(context, String.valueOf("image13"), Toast.LENGTH_LONG).show();
                        final File localFile = File.createTempFile("Voices", "3gp");
                        refe.getFile(mSaveBit2).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                getImage13.source = mSaveBit2.getPath();
                                getImage13.size = 200;
                                getImage13.execute();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "e" + "image13" + "  " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (IOException e) {
                        Toast.makeText(context, "e" + "image13" + "  " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            } else if (num > 3) {
                gridArray = images;
                customGridAdapter = new CustomGridViewAdapter(context, R.layout.row_grid, gridArray);
                commentImages.setVisibility(View.VISIBLE);
                commentImages.setAdapter(customGridAdapter);
            }

            /**if (comment.getImage1() != null)
             gridArray.add(comment.getImage1());
             if (comment.getImage2() != null)
             gridArray.add(comment.getImage2());
             if (comment.getImage3() != null)
             gridArray.add(comment.getImage3());
             if (comment.getImage4() != null)
             gridArray.add(comment.getImage4());
             if (comment.getImage5() != null)
             gridArray.add(comment.getImage5());
             if (comment.getImage6() != null)
             gridArray.add(comment.getImage6());
             if (comment.getImage7() != null)
             gridArray.add(comment.getImage7());
             if (comment.getImage8() != null)
             gridArray.add(comment.getImage8());
             if (comment.getImage9() != null)
             gridArray.add(comment.getImage9());
             if (comment.getImage10() != null)
             gridArray.add(comment.getImage10());
             if (position == 0) {
             Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.aaaa);
             gridArray.add(bm);
             gridArray.add(bm);
             gridArray.add(bm);
             gridArray.add(bm);


             }**/


            commentLinear.setTag(position);
            commentLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    if (!isDateShowed) {
                        isDateShowed = true;
                        Animation animation = AnimationUtils.loadAnimation(context, R.anim.down_from_top);
                        commentDate.startAnimation(animation);
                        commentDate.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isDateShowed = false;
                                Animation animation = AnimationUtils.loadAnimation(context, R.anim.up_from_bottom);
                                commentDate.startAnimation(animation);
                                commentDate.setVisibility(View.GONE);

                            }
                        }, 4000);
                    } else {
                        isDateShowed = false;
                        Animation animation = AnimationUtils.loadAnimation(context, R.anim.up_from_bottom);
                        commentDate.startAnimation(animation);
                        commentDate.setVisibility(View.GONE);
                    }
                }
            });


        }
    }
}