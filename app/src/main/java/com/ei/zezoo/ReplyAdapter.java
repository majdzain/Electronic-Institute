package com.ei.zezoo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ReplyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<QuesListChildItem> replys;
    private static SimpleDateFormat timeFormatter;
    Context context;
    private int lastPosition = -1;

    private Activity activity;
    private UserSQLDatabaseHandler dbu;
    private QuesCSQLDatabaseHandler dbqc;
QuesItemFragment fr;

    public ReplyAdapter(Context c, Activity a, List<QuesListChildItem> co,QuesItemFragment fragment) {
        context = c;
        activity = a;
        replys = co;
        timeFormatter = new SimpleDateFormat("m:ss", Locale.getDefault());
        dbu = new UserSQLDatabaseHandler(c);
        dbqc = new QuesCSQLDatabaseHandler(c);
        fr = fragment;
    }

    public void add(QuesListChildItem reply) {
        replys.add(reply);
        notifyDataSetChanged();
        //  notifyItemInserted(replys.lastIndexOf(reply));
    }
    public void update(QuesListChildItem reply) {
        int po = 0;
        for(int i = 0;i<replys.size();i++){
            if(replys.get(i).getIdG().matches(reply.getIdG()) && replys.get(i).getId().matches(reply.getId()))
                po = i;
        }
        replys.set(po,reply);
        notifyDataSetChanged();
        //  notifyItemInserted(replys.lastIndexOf(reply));
    }

    @Override
    public int getItemViewType(int position) {
        QuesListChildItem reply = replys.get(position);
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        QuesListChildItem reply = replys.get(viewType);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reply_item, null);
        ReplyAdapter.Reply c = new ReplyAdapter.Reply(view);
        return c;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ReplyAdapter.Reply) holder).bind(replys.get(position), position);
        LinearLayout.LayoutParams paramsMsg = new LinearLayout.
                LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

        holder.itemView.setLayoutParams(paramsMsg);
    }

    @Override
    public int getItemCount() {
        return replys.size();
    }

    public class Reply extends RecyclerView.ViewHolder {

        TextView replyDate;
        TextView replyName;
        TextView replyText;
        LinearLayout replyLinear,replyLikeLinear;
        CardView replyMessage;
        boolean isDateShowed = false;
        MyTextView like,dislike;

        public Reply(View view) {
            super(view);
            replyText = itemView.findViewById(R.id.replyText);
            replyDate = itemView.findViewById(R.id.replyDate);
            replyName = itemView.findViewById(R.id.replyName);
            replyLinear = itemView.findViewById(R.id.reply_item);
            replyMessage = itemView.findViewById(R.id.replyMessage);
            replyLikeLinear = itemView.findViewById(R.id.linearL);
like = itemView.findViewById(R.id.btnLike);
            dislike = itemView.findViewById(R.id.btnDislike);
        }

        public void bind(final QuesListChildItem reply, int position) {
            Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
            replyLinear.startAnimation(animation);
            lastPosition = position;
            if (position != 0 && reply.getUser().matches(replys.get(position - 1).getUser()))
                replyName.setVisibility(View.GONE);
            else
                replyName.setVisibility(View.VISIBLE);
                replyText.setText(reply.getText());

            replyDate.setText(reply.getDate() + "|" + reply.getTime());

            if (reply.isTeacher()) {

                replyLinear.setGravity(Gravity.LEFT);
                replyName.setTextColor(context.getResources().getColor(R.color.orange));
                replyMessage.setCardBackgroundColor(context.getResources().getColor(R.color.orange));
                replyName.setText(reply.getName());
                replyLikeLinear.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            } else if (reply.getUser().matches(dbu.getCurrentUser().getUser())) {
                replyLinear.setGravity(Gravity.RIGHT);
                replyName.setTextColor(context.getResources().getColor(R.color.blue));
                replyMessage.setCardBackgroundColor(context.getResources().getColor(R.color.blue));
                replyName.setText("أنت");
            } else {
                replyLinear.setGravity(Gravity.RIGHT);
                replyName.setTextColor(context.getResources().getColor(R.color.purple));
                replyMessage.setCardBackgroundColor(context.getResources().getColor(R.color.purple));
                replyName.setText(reply.getName());
            }
            replyLinear.setTag(position);
            replyLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    if (!isDateShowed) {
                        isDateShowed = true;
                        Animation animation = AnimationUtils.loadAnimation(context, R.anim.down_from_top);
                        replyDate.startAnimation(animation);
                        replyDate.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isDateShowed = false;
                                Animation animation = AnimationUtils.loadAnimation(context, R.anim.up_from_bottom);
                                replyDate.startAnimation(animation);
                                replyDate.setVisibility(View.GONE);

                            }
                        }, 4000);
                    } else {
                        isDateShowed = false;
                        Animation animation = AnimationUtils.loadAnimation(context, R.anim.up_from_bottom);
                        replyDate.startAnimation(animation);
                        replyDate.setVisibility(View.GONE);
                    }
                }
            });
like.setText(String.valueOf(reply.getNumLike()));
            dislike.setText(String.valueOf(reply.getNumDis()));
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!dbqc.getQues(reply.getColumn()).isLike()) {
                        like.setText(String.valueOf(Integer.valueOf(like.getText().toString()) + 1));
                        fr.likeDislikeReply(true, reply.getId());
                    }
                }
            });
            dislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!dbqc.getQues(reply.getColumn()).isDis()) {
                        dislike.setText(String.valueOf(Integer.valueOf(dislike.getText().toString()) + 1));
                        fr.likeDislikeReply(false, reply.getId());
                    }
                }
            });
        }
    }
}
