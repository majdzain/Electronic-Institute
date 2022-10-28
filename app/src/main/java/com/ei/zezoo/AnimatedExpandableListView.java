package com.ei.zezoo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

/**
 * This class defines an ExpandableListView which supports animations for
 * collapsing and expanding groups.
 */
public class AnimatedExpandableListView extends ExpandableListView {

    // The duration of the expand/collapse animations
    private static final int ANIMATION_DURATION = 300;

    private AnimatedExpandableListAdapter adapter;

    public AnimatedExpandableListView(Context context) {
        super(context);
    }

    public AnimatedExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatedExpandableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * @see ExpandableListView#setAdapter(ExpandableListAdapter)
     */
    public void setAdapter(ExpandableListAdapter adapter) {
        super.setAdapter(adapter);

        // Make sure that the adapter extends AnimatedExpandableListAdapter
        if (adapter instanceof AnimatedExpandableListAdapter) {
            this.adapter = (AnimatedExpandableListAdapter) adapter;
            this.adapter.setParent(this);
        } else {
            throw new ClassCastException(adapter.toString() + " must implement AnimatedExpandableListAdapter");
        }
    }

    /**
     * Expands the given group with an animation.
     *
     * @param groupPos The position of the group to expand
     * @return Returns true if the group was expanded. False if the group was
     * already expanded.
     */
    @SuppressLint("NewApi")
    public boolean expandGroupWithAnimation(int groupPos) {
        boolean lastGroup = groupPos == adapter.getGroupCount() - 1;
        if (lastGroup && Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return expandGroup(groupPos, true);
        }
        int groupFlatPos = getFlatListPosition(getPackedPositionForGroup(groupPos));
        if (groupFlatPos != -1) {
            int childIndex = groupFlatPos - getFirstVisiblePosition();
            if (childIndex < getChildCount()) {
                // Get the view for the group is it is on screen...
                View v = getChildAt(childIndex);
                if (v.getBottom() >= getBottom()) {
                    // If the user is not going to be able to see the animation
                    // we just expand the group without an animation.
                    // This resolves the case where getChildView will not be
                    // called if the children of the group is not on screen

                    // We need to notify the adapter that the group was expanded
                    // without it's knowledge
                    adapter.notifyGroupExpanded(groupPos);
                    return expandGroup(groupPos);
                }
            }
        }

        // Let the adapter know that we are starting the animation...
        adapter.startExpandAnimation(groupPos, 0);
        // Finally call expandGroup (note that expandGroup will call
        // notifyDataSetChanged so we don't need to)
        return expandGroup(groupPos);
    }

    /**
     * Collapses the given group with an animation.
     *
     * @param groupPos The position of the group to collapse
     * @return Returns true if the group was collapsed. False if the group was
     * already collapsed.
     */
    public boolean collapseGroupWithAnimation(int groupPos) {
        int groupFlatPos = getFlatListPosition(getPackedPositionForGroup(groupPos));
        if (groupFlatPos != -1) {
            int childIndex = groupFlatPos - getFirstVisiblePosition();
            if (childIndex >= 0 && childIndex < getChildCount()) {
                // Get the view for the group is it is on screen...
                View v = getChildAt(childIndex);
                if (v.getBottom() >= getBottom()) {
                    // If the user is not going to be able to see the animation
                    // we just collapse the group without an animation.
                    // This resolves the case where getChildView will not be
                    // called if the children of the group is not on screen
                    return collapseGroup(groupPos);
                }
            } else {
                // If the group is offscreen, we can just collapse it without an
                // animation...
                return collapseGroup(groupPos);
            }
        }

        // Get the position of the firstChild visible from the top of the screen
        long packedPos = getExpandableListPosition(getFirstVisiblePosition());
        int firstChildPos = getPackedPositionChild(packedPos);
        int firstGroupPos = getPackedPositionGroup(packedPos);

        // If the first visible view on the screen is a child view AND it's a
        // child of the group we are trying to collapse, then set that
        // as the first child position of the group... see
        // {@link #startCollapseAnimation(int, int)} for why this is necessary
        firstChildPos = firstChildPos == -1 || firstGroupPos != groupPos ? 0 : firstChildPos;

        // Let the adapter know that we are going to start animating the
        // collapse animation.
        adapter.startCollapseAnimation(groupPos, firstChildPos);

        // Force the listview to refresh it's views
        adapter.notifyDataSetChanged();
        return isGroupExpanded(groupPos);
    }

    int getAnimationDuration() {
        return ANIMATION_DURATION;
    }

}
