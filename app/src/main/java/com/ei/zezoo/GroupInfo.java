package com.ei.zezoo;

/**
 * Used for holding information regarding the group.
 */
public class GroupInfo {
    boolean animating = false;
    boolean expanding = false;
    int firstChildPosition;

    /**
     * This variable contains the last known height value of the dummy view.
     * We save this information so that if the user collapses a group
     * before it fully expands, the collapse animation will start from the
     * CURRENT height of the dummy view and not from the full expanded
     * height.
     */
    int dummyHeight = -1;
}