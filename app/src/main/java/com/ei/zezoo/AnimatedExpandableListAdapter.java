package com.ei.zezoo;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

/**
 * A specialized adapter for use with the AnimatedExpandableListView. All
 * adapters used with AnimatedExpandableListView MUST extend this class.
 */
public abstract class AnimatedExpandableListAdapter extends BaseExpandableListAdapter {
    private SparseArray<GroupInfo> groupInfo = new SparseArray<>();
    private AnimatedExpandableListView parent;

    private static final int STATE_IDLE = 0;
    private static final int STATE_EXPANDING = 1;
    private static final int STATE_COLLAPSING = 2;

    void setParent(AnimatedExpandableListView parent) {
        this.parent = parent;
    }

    public int getRealChildType(int groupPosition, int childPosition) {
        return 0;
    }

    public int getRealChildTypeCount() {
        return 1;
    }

    public abstract View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent);

    public abstract int getRealChildrenCount(int groupPosition);

    private GroupInfo getGroupInfo(int groupPosition) {
        GroupInfo info = groupInfo.get(groupPosition);
        if (info == null) {
            info = new GroupInfo();
            groupInfo.put(groupPosition, info);
        }
        return info;
    }

    public void notifyGroupExpanded(int groupPosition) {
        GroupInfo info = getGroupInfo(groupPosition);
        info.dummyHeight = -1;
    }

    void startExpandAnimation(int groupPosition, int firstChildPosition) {
        GroupInfo info = getGroupInfo(groupPosition);
        info.animating = true;
        info.firstChildPosition = firstChildPosition;
        info.expanding = true;
    }

    void startCollapseAnimation(int groupPosition, int firstChildPosition) {
        GroupInfo info = getGroupInfo(groupPosition);
        info.animating = true;
        info.firstChildPosition = firstChildPosition;
        info.expanding = false;
    }

    private void stopAnimation(int groupPosition) {
        GroupInfo info = getGroupInfo(groupPosition);
        info.animating = false;
    }

    /**
     * Override {@link #getRealChildType(int, int)} instead.
     */
    @Override
    public final int getChildType(int groupPosition, int childPosition) {
        GroupInfo info = getGroupInfo(groupPosition);
        if (info.animating) {
            // If we are animating this group, then all of it's children
            // are going to be dummy views which we will say is type 0.
            return 0;
        } else {
            // If we are not animating this group, then we will add 1 to
            // the type it has so that no type id conflicts will occur
            // unless getRealChildType() returns MAX_INT
            return getRealChildType(groupPosition, childPosition) + 1;
        }
    }

    /**
     * Override {@link #getRealChildTypeCount()} instead.
     */
    @Override
    public final int getChildTypeCount() {
        // Return 1 more than the childTypeCount to account for DummyView
        return getRealChildTypeCount() + 1;
    }

    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 0);
    }

    /**
     * Override {@link #getChildView(int, int, boolean, View, ViewGroup)} instead.
     */
    @Override
    public final View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {
        final GroupInfo info = getGroupInfo(groupPosition);
        View view = convertView;
        if (info.animating) {
            return handleIfAnimating(info, groupPosition, view, childPosition);
        } else {
            return getRealChildView(groupPosition, childPosition, isLastChild, view, parent);
        }
    }

    private View handleIfAnimating(GroupInfo info, int groupPosition, View view, int childPosition) {
        // If this group is animating, return the a DummyView...
        View tempView = view;
        if (!(tempView instanceof DummyView)) {
            tempView = new DummyView(parent.getContext());
            tempView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 0));
        }

        if (childPosition < info.firstChildPosition) {
            // The reason why we do this is to support the collapse
            // this group when the group view is not visible but the
            // children of this group are. When notifyDataSetChanged
            // is called, the ExpandableListView tries to keep the
            // list position the same by saving the first visible item
            // and jumping back to that item after the views have been
            // refreshed. Now the problem is, if a group has 2 items
            // and the first visible item is the 2nd child of the group
            // and this group is collapsed, then the dummy view will be
            // used for the group. But now the group only has 1 item
            // which is the dummy view, thus when the ListView is trying
            // to restore the scroll position, it will try to jump to
            // the second item of the group. But this group no longer
            // has a second item, so it is forced to jump to the next
            // group. This will cause a very ugly visual glitch. So
            // the way that we counteract this is by creating as many
            // dummy views as we need to maintain the scroll position
            // of the ListView after notifyDataSetChanged has been
            // called.
            tempView.getLayoutParams().height = 0;
            return tempView;
        }

        final ExpandableListView listView = parent;

        final DummyView dummyView = (DummyView) tempView;

        // Clear the views that the dummy view draws.
        dummyView.clearViews();

        // Set the style of the divider
        dummyView.setDivider(listView.getDivider(), parent.getMeasuredWidth(), listView.getDividerHeight());

        // Make measure specs to measure child views
        final int measureSpecW = View.MeasureSpec.makeMeasureSpec(parent.getWidth(), View.MeasureSpec.EXACTLY);
        final int measureSpecH = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        int totalHeight = 0;
        int clipHeight = parent.getHeight();

        final int len = getRealChildrenCount(groupPosition);
        for (int i = info.firstChildPosition; i < len; i++) {
            View childView = getRealChildView(groupPosition, i, i == len - 1, null, parent);

            AbsListView.LayoutParams p = (AbsListView.LayoutParams) childView.getLayoutParams();
            if (p == null) {
                p = (AbsListView.LayoutParams) generateDefaultLayoutParams();
                childView.setLayoutParams(p);
            }

            int lpHeight = p.height;

            int childHeightSpec;
            if (lpHeight > 0) {
                childHeightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight, View.MeasureSpec.EXACTLY);
            } else {
                childHeightSpec = measureSpecH;
            }

            childView.measure(measureSpecW, childHeightSpec);
            totalHeight += childView.getMeasuredHeight();

            if (totalHeight < clipHeight) {
                // we only need to draw enough views to fool the user...
                dummyView.addFakeView(childView);
            } else {
                dummyView.addFakeView(childView);

                // if this group has too many views, we don't want to
                // calculate the height of everything... just do a light
                // approximation and break
                int averageHeight = totalHeight / (i + 1);
                totalHeight += (len - i - 1) * averageHeight;
                break;
            }
        }

        Object o;
        int state = (o = dummyView.getTag()) == null ? STATE_IDLE : (Integer) o;

        handleExpandingOrCollapsing(info, state, dummyView, totalHeight, groupPosition, listView);

        return tempView;
    }

    private void handleExpandingOrCollapsing(GroupInfo info, int state, DummyView dummyView, int totalHeight, int groupPosition, ExpandableListView listView) {
        if (info.expanding && state != STATE_EXPANDING) {
            handleExpanding(dummyView, totalHeight, info, groupPosition);
        } else if (!info.expanding && state != STATE_COLLAPSING) {
            handleCollapsing(dummyView, totalHeight, info, groupPosition, listView);
        }
    }

    private void handleCollapsing(final DummyView dummyView, int totalHeight, final GroupInfo info, final int groupPosition, final ExpandableListView listView) {
        if (info.dummyHeight == -1) {
            info.dummyHeight = totalHeight;
        }

        ExpandAnimation ani = new ExpandAnimation(dummyView, info.dummyHeight, 0, info);
        ani.setDuration(this.parent.getAnimationDuration());
        ani.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationEnd(Animation animation) {
                onAnimationEnded(groupPosition, listView, info, dummyView);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //
            }

            @Override
            public void onAnimationStart(Animation animation) {
                //
            }

        });
        dummyView.startAnimation(ani);
        dummyView.setTag(STATE_COLLAPSING);
    }

    private void onAnimationEnded(int groupPosition, ExpandableListView listView, GroupInfo info, DummyView dummyView) {
        stopAnimation(groupPosition);
        listView.collapseGroup(groupPosition);
        notifyDataSetChanged();
        info.dummyHeight = -1;
        dummyView.setTag(STATE_IDLE);
    }

    private void handleExpanding(final DummyView dummyView, int totalHeight, GroupInfo info, final int groupPosition) {
        ExpandAnimation ani = new ExpandAnimation(dummyView, 0, totalHeight, info);
        ani.setDuration(this.parent.getAnimationDuration());
        ani.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationEnd(Animation animation) {
                stopAnimation(groupPosition);
                notifyDataSetChanged();
                dummyView.setTag(STATE_IDLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //
            }

            @Override
            public void onAnimationStart(Animation animation) {
                //
            }

        });
        dummyView.startAnimation(ani);
        dummyView.setTag(STATE_EXPANDING);
    }

    @Override
    public final int getChildrenCount(int groupPosition) {
        GroupInfo info = getGroupInfo(groupPosition);
        if (info.animating) {
            return info.firstChildPosition + 1;
        } else {
            return getRealChildrenCount(groupPosition);
        }
    }

}