package com.ei.zezoo;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class CustomNotifExpandableListAdapter extends AnimatedExpandableListAdapter {
    private Context context;
    private List<String> listGroupTitle; // header titles
    private HashMap<String, List<NotifListChildItem>> listChildData;
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
    public CustomNotifExpandableListAdapter(Context context, List<String> listGroupTitle, HashMap<String, List<NotifListChildItem>> listChildData) {
        this.context = context;
        this.listGroupTitle = listGroupTitle;
        this.listChildData = listChildData;
    }



    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).get(expandedListPosition);
        return this.listChildData.get(this.listGroupTitle.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        NotifListChildItem memData = (NotifListChildItem) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_notification_item, null);
        }

        TextView notifSubject = (TextView) convertView.findViewById(R.id.notifSubject);
        TextView notifDate = (TextView) convertView.findViewById(R.id.notifDate);
        TextView notifName = (TextView) convertView.findViewById(R.id.notifName);
        TextView notifNew = (TextView) convertView.findViewById(R.id.notifNew);
        ImageView notifImage = (ImageView) convertView.findViewById(R.id.notifImage);
        ImageView notifDownload = (ImageView) convertView.findViewById(R.id.notifDownload);
        notifSubject.setText(ElecUtils.getSubject(memData.getType(),memData.getStudy(),memData.getSubject()));
        notifDate.setText(memData.getDate() + "|" + memData.getTime());
        notifName.setText(memData.getName());
        notifImage.setImageResource(ElecUtils.getResourceFromSubject(memData.getType(),memData.getStudy(),memData.getSubject()));
        switch (memData.getDownload()){
            case 0:
               notifDownload.setImageBitmap(null);
                break;
            case 1:
               notifDownload.setImageResource(R.drawable.download);
                break;
            case 2:
                notifDownload.setImageResource(R.drawable.downloaded);
                break;
            case 3:
                notifDownload.setImageResource(R.drawable.seen);
                break;
        }
        if(memData.isNew())
            notifNew.setVisibility(View.VISIBLE);
        else
            notifNew.setVisibility(View.GONE);
        return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return this.listChildData.get(this.listGroupTitle.get(groupPosition)).size();
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
        String listTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_notification_group_item, null);
        }
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.listNotifItemTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        /**Image view which you put in row_group_list.xml
         View ind = convertView.findViewById(R.id.groupNotifIndicator);
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
