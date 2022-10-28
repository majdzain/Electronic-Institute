package com.ei.zezoo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotifFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotifFragment extends Fragment implements FragmentCommunicator{
    Context context;
    View view;
    FloatingActionButton fabS, fabT, fabD, fab;
    FloatingActionMenu fabM;
    SharedPreferences pref;
    Resources res;
    AnimatedExpandableListView expListView;
    ListView listView;
    private NotifSQLDatabaseHandler db;
    UserSQLDatabaseHandler dbu;
    ArrayList<String> listGroupTitles;
    ArrayList<NotifListChildItem> listNotifs;
    HashMap<String, List<NotifListChildItem>> listChildData;
    CustomNotifExpandableListAdapter expandableListAdapter;
    CustomNotifListAdapter listAdapter;
    int currentColumn;
    int currentId;
    String currentType;
    String currentSubject;
    String currentName;
    int currentDownload;
    boolean currentNew;
    String currentTime;
    String currentDate;
    private int lastExpandedPosition = -1;
    UserListChildItem user;
    boolean isConnectAnimationStarted = false;
    SharedPreference preference;
    MenuItem menuConnected;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotifFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotifFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotifFragment newInstance(String param1, String param2) {
        NotifFragment fragment = new NotifFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notif, container, false);
        context = view.getContext();
        pref = context.getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        setHasOptionsMenu(true);
        res = getResources();
        db = new NotifSQLDatabaseHandler(context);
        dbu = new UserSQLDatabaseHandler(context);
        user = dbu.getCurrentUser();
        fab = (FloatingActionButton) view.findViewById(R.id.floatingActionNon);
        fabS = (FloatingActionButton) view.findViewById(R.id.floatingActionSubject);
        fabT = (FloatingActionButton) view.findViewById(R.id.floatingActionType);
        fabD = (FloatingActionButton) view.findViewById(R.id.floatingActionDate);
        fabM = (FloatingActionMenu) view.findViewById(R.id.floatingActionMenu);
        expListView = (AnimatedExpandableListView) view.findViewById(R.id.notifExpList);
        listView = (ListView) view.findViewById(R.id.notifList);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;

        expListView.setIndicatorBounds(width - GetPixelFromDips(360), width - GetPixelFromDips(320));
        fab.setColorNormal(getResources().getColor(R.color.orange));
        fab.setColorPressed(getResources().getColor(R.color.orange));
        fab.setLabelColors(getResources().getColor(R.color.orange),getResources().getColor(R.color.orange),getResources().getColor(R.color.floating_light));
        fabS.setColorNormal(getResources().getColor(R.color.blue));
        fabS.setColorPressed(getResources().getColor(R.color.blue));
        fabS.setLabelColors(getResources().getColor(R.color.floating_light),getResources().getColor(R.color.floating_pressed),getResources().getColor(R.color.floating_light));
        fabT.setColorNormal(getResources().getColor(R.color.blue));
        fabT.setColorPressed(getResources().getColor(R.color.blue));
        fabT.setLabelColors(getResources().getColor(R.color.floating_light),getResources().getColor(R.color.floating_pressed),getResources().getColor(R.color.floating_light));
        fabD.setColorNormal(getResources().getColor(R.color.blue));
        fabD.setColorPressed(getResources().getColor(R.color.blue));
        fabD.setLabelColors(getResources().getColor(R.color.floating_light),getResources().getColor(R.color.floating_pressed),getResources().getColor(R.color.floating_light));
        createListFromSQL(0);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabM.close(true);
                fab.setColorNormal(getResources().getColor(R.color.orange));
                fab.setColorPressed(getResources().getColor(R.color.orange));
                fab.setLabelColors(getResources().getColor(R.color.orange),getResources().getColor(R.color.orange),getResources().getColor(R.color.floating_light));
                fabS.setColorNormal(getResources().getColor(R.color.blue));
                fabS.setColorPressed(getResources().getColor(R.color.blue));
                fabS.setLabelColors(getResources().getColor(R.color.floating_light),getResources().getColor(R.color.floating_pressed),getResources().getColor(R.color.floating_light));
                fabT.setColorNormal(getResources().getColor(R.color.blue));
                fabT.setColorPressed(getResources().getColor(R.color.blue));
                fabT.setLabelColors(getResources().getColor(R.color.floating_light),getResources().getColor(R.color.floating_pressed),getResources().getColor(R.color.floating_light));
                fabD.setColorNormal(getResources().getColor(R.color.blue));
                fabD.setColorPressed(getResources().getColor(R.color.blue));
                fabD.setLabelColors(getResources().getColor(R.color.floating_light),getResources().getColor(R.color.floating_pressed),getResources().getColor(R.color.floating_light));
                createListFromSQL(0);
            }
        });
        fabS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabM.close(true);
                fabS.setColorNormal(getResources().getColor(R.color.orange));
                fabS.setColorPressed(getResources().getColor(R.color.orange));
                fabS.setLabelColors(getResources().getColor(R.color.orange),getResources().getColor(R.color.orange),getResources().getColor(R.color.floating_light));
                fab.setColorNormal(getResources().getColor(R.color.blue));
                fab.setColorPressed(getResources().getColor(R.color.blue));
                fab.setLabelColors(getResources().getColor(R.color.floating_light),getResources().getColor(R.color.floating_pressed),getResources().getColor(R.color.floating_light));
                fabT.setColorNormal(getResources().getColor(R.color.blue));
                fabT.setColorPressed(getResources().getColor(R.color.blue));
                fabT.setLabelColors(getResources().getColor(R.color.floating_light),getResources().getColor(R.color.floating_pressed),getResources().getColor(R.color.floating_light));
                fabD.setColorNormal(getResources().getColor(R.color.blue));
                fabD.setColorPressed(getResources().getColor(R.color.blue));
                fabD.setLabelColors(getResources().getColor(R.color.floating_light),getResources().getColor(R.color.floating_pressed),getResources().getColor(R.color.floating_light));
                createListFromSQL(1);
            }
        });
        fabT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabM.close(true);
                fabT.setColorNormal(getResources().getColor(R.color.orange));
                fabT.setColorPressed(getResources().getColor(R.color.orange));
                fabT.setLabelColors(getResources().getColor(R.color.orange),getResources().getColor(R.color.orange),getResources().getColor(R.color.floating_light));
                fabS.setColorNormal(getResources().getColor(R.color.blue));
                fabS.setColorPressed(getResources().getColor(R.color.blue));
                fabS.setLabelColors(getResources().getColor(R.color.floating_light),getResources().getColor(R.color.floating_pressed),getResources().getColor(R.color.floating_light));
                fab.setColorNormal(getResources().getColor(R.color.blue));
                fab.setColorPressed(getResources().getColor(R.color.blue));
                fab.setLabelColors(getResources().getColor(R.color.floating_light),getResources().getColor(R.color.floating_pressed),getResources().getColor(R.color.floating_light));
                fabD.setColorNormal(getResources().getColor(R.color.blue));
                fabD.setColorPressed(getResources().getColor(R.color.blue));
                fabD.setLabelColors(getResources().getColor(R.color.floating_light),getResources().getColor(R.color.floating_pressed),getResources().getColor(R.color.floating_light));
                createListFromSQL(2);
            }
        });
        fabD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabM.close(true);
                fabD.setColorNormal(getResources().getColor(R.color.orange));
                fabD.setColorPressed(getResources().getColor(R.color.orange));
                fabD.setLabelColors(getResources().getColor(R.color.orange),getResources().getColor(R.color.orange),getResources().getColor(R.color.floating_light));
                fabS.setColorNormal(getResources().getColor(R.color.blue));
                fabS.setColorPressed(getResources().getColor(R.color.blue));
                fabS.setLabelColors(getResources().getColor(R.color.floating_light),getResources().getColor(R.color.floating_pressed),getResources().getColor(R.color.floating_light));
                fabT.setColorNormal(getResources().getColor(R.color.blue));
                fabT.setColorPressed(getResources().getColor(R.color.blue));
                fabT.setLabelColors(getResources().getColor(R.color.floating_light),getResources().getColor(R.color.floating_pressed),getResources().getColor(R.color.floating_light));
                fab.setColorNormal(getResources().getColor(R.color.blue));
                fab.setColorPressed(getResources().getColor(R.color.blue));
                fab.setLabelColors(getResources().getColor(R.color.floating_light),getResources().getColor(R.color.floating_pressed),getResources().getColor(R.color.floating_light));
                createListFromSQL(3);
            }
        });
        for(int i = 0; i < db.allNotifs(user.getType(),user.getStudy()).size();i++){
            NotifListChildItem n = db.allNotifs(user.getType(),user.getStudy()).get(i);
            if(n.getTypeN() != 0 && n.getTypeN() != 1){
                n.setNew(false);
                db.updateNotif(n);
            }
        }
        return view;
    }

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    private void createSQLDatabase() {
        // create our sqlite helper class

    }

    private void createListFromSQL(int type) {
        // list all notifs
        List<NotifListChildItem> notifs = db.allNotifs(user.getType(),user.getStudy());
        List<String> folders = null;
        if (type == 1)
            folders = db.allSubjects(user.getType(),user.getStudy());
        else if (type == 2)
            folders = db.allTypes(user.getType(),user.getStudy());
        else if (type == 3)
            folders = db.allDates(user.getType(),user.getStudy());
        if (notifs != null) {
            if (type != 0)
                createExpList(folders, notifs, type);
            else
                createList(notifs);
        }
    }

    private void createList(List<NotifListChildItem> notifs) {
        final Animation animFadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        expListView.setVisibility(View.GONE);
        listView.startAnimation(animFadeOut);
        listView.setVisibility(View.VISIBLE);
        // Construct the data source
        listNotifs = new ArrayList<NotifListChildItem>();
        for (int i = 0; i < notifs.size(); i++) {
            listNotifs.add(notifs.get(i));
        }
     //   Toast.makeText(context,ElecUtils.getSubject(notifs.get(1).getType(),notifs.get(1).getStudy(),notifs.get(1).getSubject()),Toast.LENGTH_LONG).show();
// Create the adapter to convert the array to views
        listAdapter = new CustomNotifListAdapter(context, listNotifs);
// Attach the adapter to a ListView
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentColumn = listNotifs.get(i).getColumn();
                currentId = listNotifs.get(i).getId();
                currentType = ElecUtils.getNamefromTypeN(listNotifs.get(i).getTypeN());
                currentSubject = ElecUtils.getSubject(listNotifs.get(i).getType(),listNotifs.get(i).getStudy(),listNotifs.get(i).getSubject());
                currentName = listNotifs.get(i).getName();
                currentDownload = listNotifs.get(i).getDownload();
                currentNew = listNotifs.get(i).isNew();
                currentTime = listNotifs.get(i).getTime();
                currentDate = listNotifs.get(i).getDate();
            }
        });

    }

    private void createExpList(List<String> folders, List<NotifListChildItem> notifs, int type) {
        listView.setVisibility(View.GONE);
        final Animation animFadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        expListView.startAnimation(animFadeOut);
        expListView.setVisibility(View.VISIBLE);
        // Get the expandable list
        expListView = (AnimatedExpandableListView) view.findViewById(R.id.notifExpList);
        String[] itemsFolders = new String[folders.size()];
        for (int i = 0; i < folders.size(); i++) {
            itemsFolders[i] = folders.get(i);
        }
        int[] itemsNotifsColumns = new int[notifs.size()];
        for (int i = 0; i < notifs.size(); i++) {
            itemsNotifsColumns[i] = notifs.get(i).getColumn();
        }
        int[] itemsNotifsIds = new int[notifs.size()];
        for (int i = 0; i < notifs.size(); i++) {
            itemsNotifsIds[i] = notifs.get(i).getId();
        }
        int[] itemsNotifsTypeNs = new int[notifs.size()];
        for (int i = 0; i < notifs.size(); i++) {
            itemsNotifsTypeNs[i] = notifs.get(i).getTypeN();
        }
        int[] itemsNotifsTypes = new int[notifs.size()];
        for (int i = 0; i < notifs.size(); i++) {
            itemsNotifsTypes[i] = notifs.get(i).getType();
        }
        int[] itemsNotifsStudys = new int[notifs.size()];
        for (int i = 0; i < notifs.size(); i++) {
            itemsNotifsStudys[i] = notifs.get(i).getStudy();
        }
        int[] itemsNotifsSubjects = new int[notifs.size()];
        for (int i = 0; i < notifs.size(); i++) {
            itemsNotifsSubjects[i] = notifs.get(i).getSubject();
        }
        String[] itemsNotifsNames = new String[notifs.size()];
        for (int i = 0; i < notifs.size(); i++) {
            itemsNotifsNames[i] = notifs.get(i).getName();
        }
        int[] itemsNotifsDownloads = new int[notifs.size()];
        for (int i = 0; i < notifs.size(); i++) {
            itemsNotifsDownloads[i] = notifs.get(i).getDownload();
        }
        boolean[] itemsNotifsNews = new boolean[notifs.size()];
        for (int i = 0; i < notifs.size(); i++) {
            itemsNotifsNews[i] = notifs.get(i).isNew();
        }
        String[] itemsNotifsTimes = new String[notifs.size()];
        for (int i = 0; i < notifs.size(); i++) {
            itemsNotifsTimes[i] = notifs.get(i).getTime();
        }
        String[] itemsNotifsDates = new String[notifs.size()];
        for (int i = 0; i < notifs.size(); i++) {
            itemsNotifsDates[i] = notifs.get(i).getDate();
        }
        // Setting up list
        listGroupTitles = new ArrayList<String>(Arrays.asList(itemsFolders));
        listChildData = new HashMap<String, List<NotifListChildItem>>();
        // Adding district names and number of population as children
        for (int i1 = 0; i1 < listGroupTitles.size(); i1++) {
            String folder = itemsFolders[i1];
            List<NotifListChildItem> pDistricts = pDistricts = new ArrayList<NotifListChildItem>();
            for (int i = 0; i < notifs.size(); i++) {
                if (type == 1&&(notifs.get(i).getTypeN() == 0 || notifs.get(i).getTypeN() == 1 )&& ElecUtils.getSubject(notifs.get(i).getType(),notifs.get(i).getStudy(),notifs.get(i).getSubject()).matches(folder)) {
                    pDistricts.add(new NotifListChildItem(itemsNotifsColumns[i], itemsNotifsIds[i], itemsNotifsTypeNs[i], itemsNotifsTypes[i], itemsNotifsStudys[i], notifs.get(i).getSubject(), itemsNotifsNames[i], itemsNotifsDownloads[i], itemsNotifsNews[i], itemsNotifsTimes[i], itemsNotifsDates[i]));
                } else if (type == 2 && ElecUtils.getNamefromTypeN(notifs.get(i).getTypeN()).matches(folder)) {
                    pDistricts.add(new NotifListChildItem(itemsNotifsColumns[i], itemsNotifsIds[i], itemsNotifsTypeNs[i], itemsNotifsTypes[i], itemsNotifsStudys[i], notifs.get(i).getSubject(), itemsNotifsNames[i], itemsNotifsDownloads[i], itemsNotifsNews[i], itemsNotifsTimes[i], itemsNotifsDates[i]));
                } else if (type == 3 && notifs.get(i).getDate().matches(folder)) {
                    pDistricts.add(new NotifListChildItem(itemsNotifsColumns[i], itemsNotifsIds[i], itemsNotifsTypeNs[i], itemsNotifsTypes[i], itemsNotifsStudys[i], notifs.get(i).getSubject(), itemsNotifsNames[i], itemsNotifsDownloads[i], itemsNotifsNews[i], itemsNotifsTimes[i], itemsNotifsDates[i]));
                }
            }
            listChildData.put(folder, pDistricts);
        }
        expandableListAdapter = new CustomNotifExpandableListAdapter(context, listGroupTitles, listChildData);
        // Setting list adapter
        expListView.setAdapter(expandableListAdapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
                currentColumn = listChildData.get(listGroupTitles.get(groupPosition)).get(childPosition).getColumn();
                currentId = listChildData.get(listGroupTitles.get(groupPosition)).get(childPosition).getId();
                currentType = ElecUtils.getNamefromTypeN(listChildData.get(listGroupTitles.get(groupPosition)).get(childPosition).getTypeN());
                currentSubject = ElecUtils.getSubject(listChildData.get(listGroupTitles.get(groupPosition)).get(childPosition).getType(),listChildData.get(listGroupTitles.get(groupPosition)).get(childPosition).getStudy(),listChildData.get(listGroupTitles.get(groupPosition)).get(childPosition).getSubject());
                currentName = listChildData.get(listGroupTitles.get(groupPosition)).get(childPosition).getName();
                currentDownload = listChildData.get(listGroupTitles.get(groupPosition)).get(childPosition).getDownload();
                currentNew = listChildData.get(listGroupTitles.get(groupPosition)).get(childPosition).isNew();
                currentTime = listChildData.get(listGroupTitles.get(groupPosition)).get(childPosition).getTime();
                currentDate = listChildData.get(listGroupTitles.get(groupPosition)).get(childPosition).getDate();
                //createNotifDialog(true);
                return true;
            }
        });

        expListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ExpandableListView listView = (ExpandableListView) parent;
                long pos = listView.getExpandableListPosition(position);

                // get type and correct positions
                int itemType = ExpandableListView.getPackedPositionType(pos);
                int groupPos = ExpandableListView.getPackedPositionGroup(pos);
                int childPos = ExpandableListView.getPackedPositionChild(pos);

                // if child is long-clicked
                if (itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    NotifListChildItem CLCI = (NotifListChildItem) expandableListAdapter.getChild(groupPos, childPos);
                    //createPopupChildItemMenu(view, CLCI);
                } else if (itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    //createPopupGroupItemMenu(view, groupPos);
                }
                return true;
            }
        });
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (expListView.isGroupExpanded(groupPosition)) {
                    expListView.collapseGroupWithAnimation(groupPosition);
                } else {
                    expListView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }
        });
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                if (lastExpandedPosition != -1
                        && i != lastExpandedPosition) {
                    expListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = i;
            }
        });
    }
    Menu men;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_notif, menu);
        men = menu;
        menuConnected = menu.findItem(R.id.menu_connected);
        preference = new SharedPreference(context);
        if(((UserActivity) getActivity()).vpnService.getStatus().matches("CONNECTED")) {
            menuConnected.setIcon(R.drawable.connected);
            menuConnected.setTitle("متصل");
        }else if(((UserActivity) getActivity()).vpnService.getStatus().matches("DISCONNECTED") || ((UserActivity) getActivity()).vpnService.getStatus().matches("NONETWORK") ) {
            menuConnected.setIcon(R.drawable.reconnect);
            menuConnected.setTitle("غير متصل");
            isConnectAnimationStarted = false;
        }else{
            startRotateAni(menuConnected);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_connected:
                if(!isConnectAnimationStarted && !preference.isVpnStarted()){
                    startRotateAni(item);
                    if(!preference.isVpnStarted())
                        ((UserActivity) getActivity()).prepareVpn();
                }
                return true;
            case R.id.menu_u_sort:
                fabM.open(true);
                return true;
            case R.id.menu_u_sign:

                return true;
            case R.id.menu_u_about:

                return true;
            case R.id.menu_u_exit:
                ((UserActivity) getActivity()).closeApplication();
return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setConnectButton(boolean isConnected) {
        //str is the string variable you pass from the Activity, it can be null...
        if(isConnected) {
            stopRotateAni();
            menuConnected.setIcon(R.drawable.connected);
            menuConnected.setTitle("متصل");
        }else {
            menuConnected.setIcon(R.drawable.reconnect);
            menuConnected.setTitle("غير متصل");
        }
    }

    @Override
    public void startConnectAnimation() {
        startRotateAni(menuConnected);
    }

    @Override
    public void stopConnectAnimation() {
        stopRotateAni();
    }


    @Override
    public boolean isConnectAnimationStarted() {
        return isConnectAnimationStarted;
    }

    @Override
    public void exitVideoLesson() {

    }


    @Override
    public void setButtonDownloadV(LessonListChildItem lesson, int v) {

    }

    @Override
    public void setButtonStopV(LessonListChildItem lesson, int v) {

    }

    @Override
    public void setButtonPauseV(LessonListChildItem lesson, int v) {

    }

    @Override
    public void setButtonWatchV(LessonListChildItem lesson, int v) {

    }

    @Override
    public void setTextDownload(LessonListChildItem lesson, String text) {

    }

    @Override
    public void setProgress(LessonListChildItem lesson, int p) {

    }

    @Override
    public void setIsResume(LessonListChildItem lesson, boolean is) {

    }

    @Override
    public void setIsDownloading(LessonListChildItem lesson, boolean is) {

    }
    private void startRotateAni(MenuItem item) {
        menuConnected = item;

        //This uses an ImageView to set the ActionView of the MenuItem so that we can use this ImageView to display the rotation animation.
        ImageView refreshActionView = (ImageView) getLayoutInflater().inflate(R.layout.iv_connect, null);
        refreshActionView.setImageResource(R.drawable.reconnect_);
        menuConnected.setActionView(refreshActionView);

        Animation rotateAni = AnimationUtils.loadAnimation(context,
                R.anim.rotate_connect);
        LinearInterpolator lin = new LinearInterpolator();
        rotateAni.setInterpolator(lin);
        isConnectAnimationStarted = true;
        item.setCheckable(false);// Do not accept any click events while scanning
        refreshActionView.startAnimation(rotateAni);

    }

    private void stopRotateAni() {
        if (menuConnected != null) {
            menuConnected.setCheckable(true);
            View view = menuConnected.getActionView();
            if (view != null) {
                view.clearAnimation();
                menuConnected.setActionView(null);
            }
            isConnectAnimationStarted = false;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(isConnectAnimationStarted)
            stopRotateAni();

    }
}