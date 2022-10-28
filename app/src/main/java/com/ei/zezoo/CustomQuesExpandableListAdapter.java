package com.ei.zezoo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CustomQuesExpandableListAdapter extends AnimatedExpandableListAdapter {
    private Context context;
    List<QuesListGroupItem> listGroupTitle; // header titles
    private HashMap<String, List<QuesListChildItem>> listChildData;
    private static final int[] EMPTY_STATE_SET = {};
    private static final int[] GROUP_EXPANDED_STATE_SET = {android.R.attr.state_expanded};
    private static final int[][] GROUP_STATE_SETS = {EMPTY_STATE_SET, // 0
            GROUP_EXPANDED_STATE_SET // 1
    };
    private static final int STATE_IDLE = 0;
    private static final int STATE_EXPANDING = 1;
    private static final int STATE_COLLAPSING = 2;
    private int lastPosition = -1;
    private int lastGroupPosition = -1;
    UserSQLDatabaseHandler dbu;
    UserListChildItem user;
    Resources res;
    String searchString = "";
    QuesFragment fr;
    QuesCSQLDatabaseHandler dbqc;
    public CustomQuesExpandableListAdapter(Context context, List<QuesListGroupItem> listGroupTitle, HashMap<String, List<QuesListChildItem>> listChildData,QuesFragment fragment) {
        this.context = context;
        this.listGroupTitle = listGroupTitle;
        this.listChildData = listChildData;
        dbu = new UserSQLDatabaseHandler(context);
        dbqc = new QuesCSQLDatabaseHandler(context);
        user = dbu.getCurrentUser();
        res = context.getResources();
        fr = fragment;
    }

    public void add(QuesListGroupItem g, List<QuesListChildItem> r){
           listGroupTitle.add(0,g);
        HashMap<String, List<QuesListChildItem>> temp = listChildData;
        listChildData = new HashMap<String, List<QuesListChildItem>>();
        listChildData.put(g.getId(), r);
        listChildData.putAll(temp);
           notifyDataSetChanged();
    }
    public void update(QuesListGroupItem g,List<QuesListChildItem> r){
        int p = 0;
        for(int i =0;i<listGroupTitle.size();i++){
            if(listGroupTitle.get(i).getId().matches(g.getId())) {
                if(listGroupTitle.get(i).getNum() != g.getNum()){
                    p = i;
                }
                break;
            }
        }
        listGroupTitle.set(p,g);
        listChildData.put(g.getId(),r);
        notifyDataSetChanged();
    }
    public void updateChild(QuesListGroupItem g,QuesListChildItem c){
        List<QuesListChildItem> replies = listChildData.get(g.getId());
        for(int i = 0;i<replies.size();i++){
            if(replies.get(i).getId().matches(c.getId()))
                replies.set(i,c);
        }
        listChildData.put(g.getId(),replies);
        notifyDataSetChanged();
    }
    public boolean filter(CharSequence sequence) {
        boolean isExist = false;
        if (!TextUtils.isEmpty(sequence)) {
            for (QuesListGroupItem group : listGroupTitle) {
                if (group.getTitle().toLowerCase(Locale.getDefault()).contains(sequence.toString().toLowerCase(Locale.getDefault())) || group.getText().toLowerCase(Locale.getDefault()).contains(sequence.toString().toLowerCase(Locale.getDefault()))) {
                    searchString = sequence.toString().toLowerCase(Locale.getDefault());
                    isExist = true;
                }
            }

        } else {
            searchString = "";
        }
        return isExist;
    }


    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).get(expandedListPosition);
        return this.listChildData.get(this.listGroupTitle.get(groupPosition).getId()).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        QuesListChildItem memData = (QuesListChildItem) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_ques_item, null);
        }

        TextView quesDate = (TextView) convertView.findViewById(R.id.quesDate);
        TextView personName = (TextView) convertView.findViewById(R.id.personName);
        TextView quesLine = (TextView) convertView.findViewById(R.id.quesLine);
        TextView quesTxt = (TextView) convertView.findViewById(R.id.quesTxt);
        MyTextView btnLike = (MyTextView) convertView.findViewById(R.id.btnLike);
        MyTextView btnDis = (MyTextView) convertView.findViewById(R.id.btnDislike);
        quesDate.setText(memData.getDate() + "|" + memData.getTime());
        if(memData.getUser().matches(user.getUser())){
            personName.setText("أنت");
            personName.setTextColor(res.getColor(R.color.blue));
            quesLine.setBackgroundColor(res.getColor(R.color.blue));
        }else if(memData.isTeacher()){
            personName.setText(memData.getName());
            personName.setTextColor(res.getColor(R.color.orange));
            quesLine.setBackgroundColor(res.getColor(R.color.orange));
        }else{
            personName.setText(memData.getName());
            personName.setTextColor(res.getColor(R.color.purple));
            quesLine.setBackgroundColor(res.getColor(R.color.purple));
        }
        quesTxt.setText(memData.getText());
        btnLike.setText(String.valueOf(memData.getNumLike()));
        btnDis.setText(String.valueOf(memData.getNumDis()));
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!dbqc.getQues(memData.getColumn()).isLike()) {
                    btnLike.setText(String.valueOf(Integer.valueOf(btnLike.getText().toString()) + 1));
                    fr.likeDislikeReply(true, listGroupTitle.get(groupPosition).getSubject(), listGroupTitle.get(groupPosition).getId(), memData.getId());
                }
            }
        });
        btnDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!dbqc.getQues(memData.getColumn()).isDis()) {
                    btnDis.setText(String.valueOf(Integer.valueOf(btnDis.getText().toString()) + 1));
                    fr.likeDislikeReply(false, listGroupTitle.get(groupPosition).getSubject(), listGroupTitle.get(groupPosition).getId(), memData.getId());
                }
            }
        });
        return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return this.listChildData.get(this.listGroupTitle.get(groupPosition).getId()).size();
    }


    @Override
    public Object getGroup(int groupPosition) {
        return this.listGroupTitle.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listGroupTitle.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        QuesListGroupItem g = (QuesListGroupItem) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_ques_group_item, null);
        }
        CardView card = (CardView) convertView.findViewById(R.id.groupCard);
        TextView quesDate = (TextView) convertView.findViewById(R.id.quesDate);
        TextView personName = (TextView) convertView.findViewById(R.id.personName);
        TextView quesLine = (TextView) convertView.findViewById(R.id.quesLine);
        TextView subjectTitle = (TextView) convertView.findViewById(R.id.subjectTitle);
        ImageView subjectImage = (ImageView) convertView.findViewById(R.id.subjectImage);
        MyTextView quesReplyNumber = (MyTextView) convertView.findViewById(R.id.quesReplyNumber);
        TextView quesTxt = (TextView) convertView.findViewById(R.id.quesTxt);
        TextView quesTitle = (TextView) convertView.findViewById(R.id.quesTitle);
        Button btnView = (Button) convertView.findViewById(R.id.btnView);
        Button btnViewR = (Button) convertView.findViewById(R.id.btnViewR);
        quesDate.setText(g.getDate() + "|" + g.getTime());
        if(g.getUser().matches(user.getUser())){
            personName.setText("أنت");
            personName.setTextColor(res.getColor(R.color.blue));
            quesLine.setBackgroundColor(res.getColor(R.color.blue));
        }else if(g.isTeacher()){
            personName.setText(g.getName());
            personName.setTextColor(res.getColor(R.color.orange));
            quesLine.setBackgroundColor(res.getColor(R.color.orange));
        }else{
            personName.setText(g.getName());
            personName.setTextColor(res.getColor(R.color.purple));
            quesLine.setBackgroundColor(res.getColor(R.color.purple));
        }
        subjectTitle.setText(ElecUtils.getSubject(g.getType(),g.getStudy(),g.getSubject()));
        subjectImage.setImageResource(ElecUtils.getResourceFromSubject(g.getType(),g.getStudy(),g.getSubject()));
        quesReplyNumber.setText(String.valueOf(g.getNum()));
        if (g.getTitle().toLowerCase(Locale.getDefault()).contains(searchString)) {

            int startPos = g.getTitle().toLowerCase(Locale.getDefault()).indexOf(searchString);
            int endPos = startPos + searchString.length();

            Spannable spanString = Spannable.Factory.getInstance().newSpannable(g.getTitle());
            spanString.setSpan(new BackgroundColorSpan(Color.YELLOW), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            quesTitle.setText(spanString);
        } else
        quesTitle.setText(g.getTitle());
        if (g.getText().toLowerCase(Locale.getDefault()).contains(searchString)) {

            int startPos = g.getText().toLowerCase(Locale.getDefault()).indexOf(searchString);
            int endPos = startPos + searchString.length();

            Spannable spanString = Spannable.Factory.getInstance().newSpannable(g.getText());
            spanString.setSpan(new BackgroundColorSpan(Color.YELLOW), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            quesTxt.setText(spanString);
        } else
        quesTxt.setText(g.getText());
        if(g.getNum() > 0)
            btnViewR.setVisibility(View.VISIBLE);
        else
            btnViewR.setVisibility(View.GONE);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fr.colExp(groupPosition,btnViewR);
            }
        });
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuesItemFragment fragment = new QuesItemFragment(g.getId(),context);
                ((UserActivity) context).fragmentCommunicator = fragment;
                FragmentTransaction ft = ((UserActivity) context).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentMain, fragment);
                ft.commit();
            }
        });
        btnViewR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fr.colExp(groupPosition,(Button)view);
            }
        });
        /**Image view which you put in row_group_list.xml
         View ind = convertView.findViewById(R.id.groupQuesIndicator);
         if (ind != null) {
         ImageView indicator = (ImageView) ind;
         indicator.setVisibility(View.VISIBLE);
         int stateSetIndex = (isExpanded ? 1 : 0);
         Drawable drawable = indicator.getDrawable();
         drawable.setState(GROUP_STATE_SETS[stateSetIndex]);
         }**/
        return convertView;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}
