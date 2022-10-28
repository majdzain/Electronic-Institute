package com.ei.zezoo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BillFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuesItemFragment extends Fragment implements FragmentCommunicator{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Context context;
    View view;
    SharedPreferences pref;
    Resources res;
    AnimatedExpandableListView expListView;
    QuesGSQLDatabaseHandler dbqg;
    QuesCSQLDatabaseHandler dbqc;
    UserSQLDatabaseHandler dbu;
    LessonSQLDatabaseHandler dbl;
    UserListChildItem user;
    TextView person, date, line, subjectName, title, text;
    MyTextView num;
    ImageView subjectImage;
    RecyclerView recyclerView;
    EditText reply;
    CardView replyBtn;
    QuesListGroupItem Ques;
    ArrayList<QuesListChildItem> Replies;
    ReplyAdapter replyAdapter;
    ValueEventListener replyListener;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatabaseReference groupRef;
    private DatabaseReference replyRef;
    int type ;
    int study;
    int subject;
    String id;
    Menu men;
    SharedPreference preference;
    MenuItem menuConnected;
    boolean isConnectAnimationStarted = false;
    HashMap<String,ValueEventListener> replyListeners;
    private boolean isLikeDis = false;

    public QuesItemFragment(String ids,Context co) {
        context = co;
        dbqg = new QuesGSQLDatabaseHandler(context);
        dbqc = new QuesCSQLDatabaseHandler(context);
        dbu = new UserSQLDatabaseHandler(context);
        dbl = new LessonSQLDatabaseHandler(context);
        user = dbu.getCurrentUser();
        for (int i = 0; i < dbqg.allQuess(user.getType(), user.getStudy()).size(); i++) {
            QuesListGroupItem q = dbqg.allQuess(user.getType(), user.getStudy()).get(i);
            if (q.getId().matches(ids)) {
                Ques = q;
                break;
            }
        }
        Replies = new ArrayList<>();
        for (int i = 0; i < dbqc.allQuess(ids).size(); i++)
            Replies.add(dbqc.allQuess(ids).get(i));
        type = Ques.getType();
        study = Ques.getStudy();
        subject = Ques.getSubject();
        id = ids;
        // Required empty public constructor
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ques_item, container, false);
        context = view.getContext();
        pref = context.getSharedPreferences("PREFERENCE", MODE_PRIVATE);
       setHasOptionsMenu(true);
        res = getResources();

        person = (TextView) view.findViewById(R.id.personName);
        date = (TextView) view.findViewById(R.id.quesDate);
        title = (TextView) view.findViewById(R.id.quesTitle);
        text = (TextView) view.findViewById(R.id.quesTxt);
        line = (TextView) view.findViewById(R.id.quesLine);
        subjectName = (TextView) view.findViewById(R.id.subjectTitle);
        subjectImage = (ImageView) view.findViewById(R.id.subjectImage);
        num = (MyTextView) view.findViewById(R.id.quesReplyNumber);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewMessages);
        reply = (EditText) view.findViewById(R.id.editTextMessage);
        replyBtn = (CardView) view.findViewById(R.id.imageViewSend);
        replyListeners = new HashMap<>();
        replyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final HashMap<String, Object> h = new HashMap<>();
                h.put("numlike", "0");
                h.put("numdis", "0");
                h.put("id", Ques.getId() + String.valueOf(Ques.getNum() + 1));
                h.put("name", user.getName());
                h.put("user", user.getUser());
                h.put("text", reply.getText().toString());
                h.put("time", getTime());
                h.put("date", getDate());
                h.put("isteacher", String.valueOf(user.isTeacher() ? 1 : 0));
                replyRef.child(Ques.getId() + String.valueOf(Integer.valueOf(num.getText().toString()) + 1)).setValue(h);
                groupRef.child("num").setValue(String.valueOf(Integer.valueOf(num.getText().toString()) + 1));
                num.setText(String.valueOf(Integer.valueOf(num.getText().toString())+1));
                reply.setText("");
            }
        });
        date.setText(Ques.getDate() + "|" + Ques.getTime());
        title.setText(Ques.getTitle());
        text.setText(Ques.getText());
        if (Ques.getUser().matches(user.getUser())) {
            person.setText("أنت");
            person.setTextColor(res.getColor(R.color.blue));
            line.setBackgroundColor(res.getColor(R.color.blue));
        } else if (Ques.isTeacher()) {
            person.setText(Ques.getName());
            person.setTextColor(res.getColor(R.color.orange));
            line.setBackgroundColor(res.getColor(R.color.orange));
        } else {
            person.setText(Ques.getName());
            person.setTextColor(res.getColor(R.color.purple));
            line.setBackgroundColor(res.getColor(R.color.purple));
        }
        subjectName.setText(ElecUtils.getSubject(Ques.getType(), Ques.getStudy(), Ques.getSubject()));
        subjectImage.setImageResource(ElecUtils.getResourceFromSubject(Ques.getType(), Ques.getStudy(), Ques.getSubject()));
        num.setText(String.valueOf(Ques.getNum()));
        final Animation animFadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        recyclerView.startAnimation(animFadeOut);
        replyAdapter = new ReplyAdapter(context, getActivity(), Replies,this);

        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(false);

        recyclerView.setAdapter(replyAdapter);
        groupRef = FirebaseDatabase.getInstance().getReference().child(ElecUtils.getType(user.getType())).child(ElecUtils.getStudy(user.getType(), user.getStudy())).child(ElecUtils.getSubject(user.getType(), user.getStudy(), Ques.getSubject())).child("الأسئلة").child(Ques.getId());
        replyRef = groupRef.child("replies");
        replyListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterator iteratorReply = snapshot.getChildren().iterator();
                while (iteratorReply.hasNext()) {
                    final String reply = ((DataSnapshot) iteratorReply.next()).getKey();
                    replyRef.child(reply).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot d) {
                            if (d.exists() ) {
                                if (!isLikeDis) {
                                    final QuesListChildItem c = new QuesListChildItem();
                                    c.setIdG(id);

                                    //    c.setTeacher(dbu.getCurrentUser().isTeacher());
                                    c.setDate(d.child("date").getValue().toString());
                                    c.setId(d.child("id").getValue().toString());
                                    c.setTeacher(Integer.valueOf(d.child("isteacher").getValue().toString()) > 0);
                                    c.setName(d.child("name").getValue().toString());
                                    int like = Integer.valueOf(d.child("numlike").getValue().toString());
                                    int dis = Integer.valueOf(d.child("numdis").getValue().toString());
                                    c.setNumDis(dis);
                                    c.setNumLike(like);
                                    c.setText(d.child("text").getValue().toString());
                                    c.setTime(d.child("time").getValue().toString());
                                    c.setUser(d.child("user").getValue().toString());
                                    boolean isReplyExist = false;
                                    boolean isReplyUpdate = false;
                                    boolean isLike = false;
                                    boolean isDis = false;
                                    int po = dbqc.allQuess().size();
                                    for (int i = 0; i < dbqc.allQuess(id).size(); i++) {
                                        if (reply.matches(dbqc.allQuess(id).get(i).getId())) {
                                            isReplyExist = true;
                                            if (dbqc.allQuess(id).get(i).getNumLike() != like || dbqc.allQuess(id).get(i).getNumDis() != dis) {
                                                isReplyUpdate = true;
                                                po = dbqc.allQuess(id).get(i).getColumn();
                                                isLike = dbqc.allQuess(id).get(i).isLike();
                                                isDis = dbqc.allQuess(id).get(i).isDis();
                                            }
                                        }
                                    }
                                    c.setColumn(po);
                                    c.setLike(isLike);
                                    c.setDis(isDis);
                                    if (!isReplyExist) {
                                        dbqc.addQues(c);
                                        replyAdapter.add(c);
                                        scrollToBottom();
                                    } else if (isReplyUpdate) {
                                        dbqc.updateQues(c);
                                        replyAdapter.update(c);
                                    }
                                }else{
                                    isLikeDis = false;
                                }
                            }
                                replyListeners.put(reply, this);
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        replyRef.addValueEventListener(replyListener);
        scrollToBottom();
        // Inflate the layout for this fragment
        return view;
    }

    public void likeDislikeReply(boolean like,String id){
        int li = 0;
        if(like){
            if(!dbqc.isLiked(Ques.getId(),id)) {
                QuesListChildItem q = new QuesListChildItem();
                for (int i = 0; i < dbqc.allQuess(Ques.getId()).size(); i++) {
                    if (dbqc.allQuess(Ques.getId()).get(i).getId().matches(id)) {
                        li = dbqc.allQuess(Ques.getId()).get(i).getNumLike() + 1;
                        q = dbqc.allQuess(Ques.getId()).get(i);
                        break;
                    }
                }
                q.setLike(true);
                dbqc.updateQues(q);
                replyRef.child(id).child("numlike").setValue(String.valueOf(li));
                isLikeDis = true;
            }
        }else{
            if(!dbqc.isDisLiked(Ques.getId(),id)) {
                QuesListChildItem q = new QuesListChildItem();
                for (int i = 0; i < dbqc.allQuess(Ques.getId()).size(); i++) {
                    if (dbqc.allQuess(Ques.getId()).get(i).getId().matches(id)) {
                        li = dbqc.allQuess(Ques.getId()).get(i).getNumDis() + 1;
                        q = dbqc.allQuess(Ques.getId()).get(i);
                        break;
                    }
                }
                q.setDis(true);
                dbqc.updateQues(q);
                replyRef.child(id).child("numdis").setValue(String.valueOf(li));
                isLikeDis = true;
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_lesson, menu);
        men = menu;
        menuConnected = menu.findItem(R.id.menu_connected);
        preference = new SharedPreference(context);
        if (((UserActivity) getActivity()).vpnService.getStatus().matches("CONNECTED")) {
            menuConnected.setIcon(R.drawable.connected);
            menuConnected.setTitle("متصل");
        } else if (((UserActivity) getActivity()).vpnService.getStatus().matches("DISCONNECTED") || ((UserActivity) getActivity()).vpnService.getStatus().matches("NONETWORK")) {
            menuConnected.setIcon(R.drawable.reconnect);
            menuConnected.setTitle("غير متصل");
            isConnectAnimationStarted = false;
        } else {
            startRotateAni(menuConnected);
        }
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
                if (!isConnectAnimationStarted && !preference.isVpnStarted()) {
                    startRotateAni(item);
                    if (!preference.isVpnStarted())
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
    public void setConnectButton(boolean isConnected) {
        //str is the string variable you pass from the Activity, it can be null...
        if (isConnected) {
            stopRotateAni();
            menuConnected.setIcon(R.drawable.connected);
            menuConnected.setTitle("متصل");
        } else {
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
        QuesFragment fragment = new QuesFragment();
        ((UserActivity) context).fragmentCommunicator = fragment;
        FragmentTransaction ft = ((UserActivity) context).getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentMain, fragment);
        ft.commit();
    }

    @Override
    public boolean isConnectAnimationStarted() {
        return isConnectAnimationStarted;
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isConnectAnimationStarted)
            stopRotateAni();
        replyRef.removeEventListener(replyListener);
        Iterator<String> i = replyListeners.keySet().iterator();
        while (i.hasNext()){
            String s = i.next();
            replyRef.child(s).removeEventListener(replyListeners.get(s));
        }
    }

    private String getDate() {
        String date = "";
        Date now = new Date();
        Date alsoNow = Calendar.getInstance().getTime();
        date = new SimpleDateFormat("yyyy/MM/dd").format(now);
        return date;
    }

    private String getTime() {
        String time = "";
        Calendar calander = Calendar.getInstance();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("HH:mm");
        time = simpledateformat.format(calander.getTime());
        return time;
    }
    private void scrollToBottom() {
        // scroll to last item to get the view of last item
        final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        final RecyclerView.Adapter adapter = recyclerView.getAdapter();
        final int lastItemPosition = adapter.getItemCount() - 1;

        layoutManager.scrollToPositionWithOffset(lastItemPosition, 0);
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                // then scroll to specific offset
                View target = layoutManager.findViewByPosition(lastItemPosition);
                if (target != null) {
                    int offset = recyclerView.getMeasuredHeight() - target.getMeasuredHeight();
                    layoutManager.scrollToPosition(lastItemPosition);
                }
            }
        });
    }
}