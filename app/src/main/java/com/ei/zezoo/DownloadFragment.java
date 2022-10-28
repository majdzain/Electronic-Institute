package com.ei.zezoo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DownloadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DownloadFragment extends Fragment implements FragmentCommunicator{
    Context context;
    View view;
    SharedPreferences pref;
    Resources res;
    ListView listView;
    private DownloadSQLDatabaseHandler dbd;
    ArrayList<DownloadListChildItem> listDownloads;
    CustomDownloadListAdapter listAdapter;
    Menu men;
    boolean isConnectAnimationStarted = false;
    SharedPreference preference;
    MenuItem menuConnected;
    UserSQLDatabaseHandler dbu ;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DownloadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DownloadFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DownloadFragment newInstance(String param1, String param2) {
        DownloadFragment fragment = new DownloadFragment();
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
        view = inflater.inflate(R.layout.fragment_download, container, false);
        context = view.getContext();
        pref = context.getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        setHasOptionsMenu(true);
        res = getResources();
        dbd = new DownloadSQLDatabaseHandler(context);
        dbu = new UserSQLDatabaseHandler(context);
        listView = (ListView) view.findViewById(R.id.downloadList);
        createList(dbd.allDownloads());
        // Inflate the layout for this fragment
        return view;
    }
    private void createList(List<DownloadListChildItem> downloads) {
        final Animation animFadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        listView.startAnimation(animFadeOut);
        // Construct the data source
        listDownloads = new ArrayList<DownloadListChildItem>();
        for (int i = 0; i < downloads.size(); i++) {
            if(downloads.get(i).getUser().matches(dbu.getCurrentUser().getUser()))
            listDownloads.add(downloads.get(i));
        }
// Create the adapter to convert the array to views
        listAdapter = new CustomDownloadListAdapter(context,getActivity(), listDownloads);
// Attach the adapter to a ListView
        listView.setAdapter(listAdapter);


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
    public void exitVideoLesson() {

    }

    @Override
    public boolean isConnectAnimationStarted() {
        return isConnectAnimationStarted;
    }

    @Override
    public void setButtonDownloadV(LessonListChildItem lesson, int v) {
        try {
            int vi = 0;
            for (int i = 0; i < dbd.allDownloads().size(); i++) {
                DownloadListChildItem d = dbd.allDownloads().get(i);
                if (d.getType() == lesson.getType() && d.getStudy() == lesson.getStudy() && d.getSubject() == lesson.getSubject() && d.getLesson() == lesson.getLesson()) {
                    vi = i;
                }
            }
            View l = listView.getChildAt(vi);
            (l.findViewById(R.id.btnDownload)).setVisibility(v);
        }catch(Exception e){

        }
    }

    @Override
    public void setButtonStopV(LessonListChildItem lesson, int v) {
        try{
        int vi = 0;
        for(int i =0;i<dbd.allDownloads().size();i++){
            DownloadListChildItem d = dbd.allDownloads().get(i);
            if(d.getType() == lesson.getType() && d.getStudy() == lesson.getStudy() && d.getSubject() == lesson.getSubject() && d.getLesson() == lesson.getLesson()){
                vi = i;
            }
        }
        View l = listView.getChildAt(vi);
        (l.findViewById(R.id.btnStop)).setVisibility(v);
    }catch(Exception e){

    }
    }

    @Override
    public void setButtonPauseV(LessonListChildItem lesson, int v) {
        try{
        int vi = 0;
        for(int i =0;i<dbd.allDownloads().size();i++){
            DownloadListChildItem d = dbd.allDownloads().get(i);
            if(d.getType() == lesson.getType() && d.getStudy() == lesson.getStudy() && d.getSubject() == lesson.getSubject() && d.getLesson() == lesson.getLesson()){
                vi = i;
            }
        }
        View l = listView.getChildAt(vi);
        (l.findViewById(R.id.btnPause)).setVisibility(v);
    }catch(Exception e){

    }
    }

    @Override
    public void setButtonWatchV(LessonListChildItem lesson, int v) {
        try {
            int vi = 0;
            for (int i = 0; i < dbd.allDownloads().size(); i++) {
                DownloadListChildItem d = dbd.allDownloads().get(i);
                if (d.getType() == lesson.getType() && d.getStudy() == lesson.getStudy() && d.getSubject() == lesson.getSubject() && d.getLesson() == lesson.getLesson()) {
                    vi = i;
                }
            }
            View l = listView.getChildAt(vi);
            (l.findViewById(R.id.btnWatch)).setVisibility(v);
        }catch(Exception e){

        }
    }

    @Override
    public void setTextDownload(LessonListChildItem lesson, String text) {
        try{
        int vi = 0;
        for(int i =0;i<dbd.allDownloads().size();i++){
            DownloadListChildItem d = dbd.allDownloads().get(i);
            if(d.getType() == lesson.getType() && d.getStudy() == lesson.getStudy() && d.getSubject() == lesson.getSubject() && d.getLesson() == lesson.getLesson()){
                vi = i;
            }
        }
        View l = listView.getChildAt(vi);
        ((TextView)l.findViewById(R.id.downloadTxt)).setText(text);
        }catch(Exception e){

        }
    }

    @Override
    public void setProgress(LessonListChildItem lesson, int p) {
        try{
        int vi = 0;
        for(int i =0;i<dbd.allDownloads().size();i++){
            DownloadListChildItem d = dbd.allDownloads().get(i);
            if(d.getType() == lesson.getType() && d.getStudy() == lesson.getStudy() && d.getSubject() == lesson.getSubject() && d.getLesson() == lesson.getLesson()){
                vi = i;
            }
           }
// DownloadListChildItem download = listDownloads.get(vi);
  // download.setProgress(p);
  // listDownloads.set(vi,download);
  // listAdapter.notifyDataSetChanged();
             View l = listView.getChildAt(vi);
            ((ProgressBar)l.findViewById(R.id.downloadProgress)).setProgress(p);
            ((TextView) l.findViewById(R.id.downloadTxt)).setText("جاري التحميل..." + "(" + String.valueOf(p) + "%)");
    }catch(Exception e){

    }
    }

    @Override
    public void setIsResume(LessonListChildItem lesson, boolean is) {

    }

    @Override
    public void setIsDownloading(LessonListChildItem lesson, boolean is) {

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_lesson, menu);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_connected:
                if(!isConnectAnimationStarted && !preference.isVpnStarted()){
                    startRotateAni(item);
                    if(!preference.isVpnStarted())
                        ((UserActivity) getActivity()).prepareVpn();
                }
                return true;
            case R.id.menu_settings:
                UserActivity m = (UserActivity) getActivity();
                m.result.setSelection(m.settings);
                return true;
            case R.id.menu_u_edit:
                //      createrDialogEdit();
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
    public void onDestroy() {
        super.onDestroy();
        if(isConnectAnimationStarted)
            stopRotateAni();

    }
}