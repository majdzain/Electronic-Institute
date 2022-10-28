package com.ei.zezoo;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.transition.TransitionManager;

import android.os.Handler;
import android.text.TextUtils;
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
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.transitionseverywhere.ChangeText;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.TEXT_CLASSIFICATION_SERVICE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuesFragment extends Fragment implements FragmentCommunicator {

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
    UserListChildItem user;
    ArrayList<QuesListGroupItem> listGroupItems;
    HashMap<String, List<QuesListChildItem>> listChildItems;
    CustomQuesExpandableListAdapter expandableListAdapter;
    private int lastExpandedPosition = -1;
    FloatingActionButton addQues;
    MaterialSpinner spinner;
    CustomSpinnerSubjectAdapter customAdapter;
    LessonSQLDatabaseHandler dbl;
    EditText editTitle, editText;
    Button btnAdd;
    Menu men;
    SharedPreference preference;
    MenuItem menuConnected;
    boolean isConnectAnimationStarted = false;
    MaterialSearchView searchView;
    DilatingDotsProgressBar loadingProgress;
    ChildEventListener childEventListener;
    ValueEventListener subjectListener;
    DatabaseReference groupRef;
    boolean isLastSubject = false;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView imageSubject;
    HashMap<String,ValueEventListener> replyListeners;
    HashMap<String,ValueEventListener> quesListeners;
    private boolean isListening = true;

    public QuesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuesFragment newInstance(String param1, String param2) {
        QuesFragment fragment = new QuesFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ques, container, false);
        context = view.getContext();
        pref = context.getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        setHasOptionsMenu(true);
        res = getResources();
        dbqg = new QuesGSQLDatabaseHandler(context);
        dbqc = new QuesCSQLDatabaseHandler(context);
        dbu = new UserSQLDatabaseHandler(context);
        dbl = new LessonSQLDatabaseHandler(context);
        user = dbu.getCurrentUser();
        expListView = (AnimatedExpandableListView) view.findViewById(R.id.quesExpList);
        loadingProgress = (DilatingDotsProgressBar) view.findViewById(R.id.progress_loading);
        loadingProgress.showNow();
        addQues = (FloatingActionButton) view.findViewById(R.id.addQ);
//addQues.setLabelColors(getResources().getColor(R.color.floating_light),getResources().getColor(R.color.floating_pressed),getResources().getColor(R.color.floating_light));
        createExpList(dbqg.allQuess(user.getType(), user.getStudy()));
        addQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAddQuesDialog();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               isListening = false;
            }
        }, 10000);
        lastItems = new HashMap<>();
        quesListeners = new HashMap<>();
        replyListeners = new HashMap<>();
        groupRef = FirebaseDatabase.getInstance().getReference().child(ElecUtils.getType(user.getType())).child(ElecUtils.getStudy(user.getType(), user.getStudy()));
        subjectListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator iteratorSubject = dataSnapshot.getChildren().iterator();
                while (iteratorSubject.hasNext()&&isListening) {
                    final String subject = ((DataSnapshot) iteratorSubject.next()).getKey();
                    boolean isSigned = false;
                    for (int i = 0; i < user.getStudies().size(); i++) {
                        if (user.getStudies().get(i) == ElecUtils.getSubject(user.getType(), user.getStudy(), subject))
                            isSigned = true;
                    }
                    if (isSigned) {
                        lastItems.put(subject, 0);
                        if(addQues.getVisibility() == GONE)
                            addQues.setVisibility(VISIBLE);
                        final DatabaseReference questions = groupRef.child(subject).child("الأسئلة");
                        questions.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Iterator iteratorQuestions = snapshot.getChildren().iterator();
                                int i = 0;
                                while (iteratorQuestions.hasNext()&&isListening) {
                                    i = i + 1;
                                    DataSnapshot data = (DataSnapshot) iteratorQuestions.next();
                                    String Ques = data.getKey();
                                    if(!Ques.matches("num")) {
                                        questions.child(Ques).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists() && isListening) {
                                                    String sid = dataSnapshot.child("id").getValue().toString();
                                                    String snum = dataSnapshot.child("num").getValue().toString();
                                                    String stype = dataSnapshot.child("type").getValue().toString();
                                                    String sstudy = dataSnapshot.child("study").getValue().toString();
                                                    String ssubject = dataSnapshot.child("subject").getValue().toString();

                                                    String sname = dataSnapshot.child("name").getValue().toString();
                                                    String stitle = dataSnapshot.child("title").getValue().toString();
                                                    String suser = dataSnapshot.child("user").getValue().toString();
                                                    String stext = dataSnapshot.child("text").getValue().toString();
                                                    String stime = dataSnapshot.child("time").getValue().toString();
                                                    String sdate = dataSnapshot.child("date").getValue().toString();
                                                    String isteacher = dataSnapshot.child("isteacher").getValue().toString();
                                                    int type = Integer.valueOf(stype);
                                                    int study = Integer.valueOf(sstudy);
                                                    int subject = Integer.valueOf(ssubject);
                                                    int num = Integer.valueOf(snum);

                                                    boolean isAdd = true;
                                                    boolean isUpdate = false;
                                                    int k = dbqg.allQuess().size();
                                                    for (int i = 0; i < dbqg.allQuess(user.getType(), user.getStudy()).size(); i++) {
                                                        if (dbqg.allQuess(user.getType(), user.getStudy()).get(i).getId().matches(sid)) {
                                                            isAdd = false;
                                                            if (num != dbqg.allQuess(user.getType(), user.getStudy()).get(i).getNum()) {
                                                                isUpdate = true;
                                                                k = dbqg.allQuess(user.getType(), user.getStudy()).get(i).getColumn();
                                                            }
                                                            break;
                                                        }
                                                    }
                                                    final QuesListGroupItem q = new QuesListGroupItem(k, type, study, subject, num, sid, sname, stitle, suser, stext, stime, sdate, Integer.valueOf(isteacher) > 0);
                                                    if (num > 1) {
                                                        groupRef.child(ElecUtils.getSubject(type, study, subject)).child("الأسئلة").child(sid).child("replies").addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                Iterator iteratorReply = snapshot.getChildren().iterator();
                                                                while (iteratorReply.hasNext() && isListening) {
                                                                    final String reply = ((DataSnapshot) iteratorReply.next()).getKey();

                                                                    ArrayList<QuesListChildItem> replies = new ArrayList<>();
                                                                    groupRef.child(ElecUtils.getSubject(type, study, subject)).child("الأسئلة").child(sid).child("replies").child(reply).addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot d) {
                                                                            if (d.exists() && isListening) {
                                                                                final QuesListChildItem c = new QuesListChildItem();
                                                                                c.setIdG(sid);

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
                                                                                int po = dbqc.allQuess().size();
                                                                                boolean isLike = false;
                                                                                boolean isDis = false;
                                                                                for (int i = 0; i < dbqc.allQuess(sid).size(); i++) {
                                                                                    if (reply.matches(dbqc.allQuess(sid).get(i).getId())) {
                                                                                        isReplyExist = true;
                                                                                        if (dbqc.allQuess(sid).get(i).getNumLike() != like || dbqc.allQuess(sid).get(i).getNumDis() != dis) {
                                                                                            isReplyUpdate = true;
                                                                                            po = dbqc.allQuess(sid).get(i).getColumn();
                                                                                            isLike = dbqc.allQuess(sid).get(i).isLike();
                                                                                            isDis = dbqc.allQuess(sid).get(i).isDis();
                                                                                        }
                                                                                    }
                                                                                }

                                                                                c.setColumn(po);
                                                                                c.setLike(isLike);
                                                                                c.setDis(isDis);
                                                                                //       Toast.makeText(context,"reply",Toast.LENGTH_SHORT).show();
                                                                                if (!isReplyExist) {

                                                                                    dbqc.addQues(c);
                                                                                    replies.add(c);
                                                                                    if (replies.size() == q.getNum()) {
                                                                                        // listChildItems.put(q.getId(), replies);
                                                                                        expandableListAdapter.add(q, replies);
                                                                                    }
                                                                                } else if (isReplyUpdate) {
                                                                                    //    Toast.makeText(context,"update",Toast.LENGTH_SHORT).show();
                                                                                    dbqc.updateQues(c);
                                                                                    expandableListAdapter.updateChild(q, c);
                                                                                }
                                                                                questions.child(sid).child("replies").child(reply).removeEventListener(this);
                                                                            }
                                                                        }


                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                        }
                                                                    });
                                                                }
                                                                questions.child(Ques).child("replies").removeEventListener(this);
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });
                                                    }
                                                    if (isAdd) {
                                                        // Toast.makeText(context,String.valueOf(k),Toast.LENGTH_SHORT).show();
                                                        dbqg.addQues(q);
                                                        loadingProgress.hideNow();
                                                        //       listGroupItems.add(q);
                                                        //    listChildItems.put(q.getId(), new ArrayList<QuesListChildItem>());
                                                        expandableListAdapter.add(q, new ArrayList<QuesListChildItem>());
                                                    } else if (isUpdate) {
                                                        dbqg.updateQues(q);
                                                        //    Toast.makeText(context,String.valueOf(k),Toast.LENGTH_SHORT).show();
                                                        int p = 0;
                                                        for (int i = 0; i < listGroupItems.size(); i++) {
                                                            if (listGroupItems.get(i).getId().matches(q.getId()))
                                                                p = i;
                                                        }
                                                        //   listGroupItems.set(p, q);
                                                        loadingProgress.hideNow();
                                                        //    listChildItems.put(q.getId(), dbqc.allQuess(q.getId()));
                                                        expandableListAdapter.update(q, dbqc.allQuess(q.getId()));
                                                    }
                                                    questions.child(Ques).removeEventListener(this);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }else{
                                        lastItems.put(subject, Integer.valueOf((String) data.getValue()));
                                    }

                                }

                                quesListeners.put(subject,this);
                               // questions.removeEventListener(this);
                                    //  loadingProgress.hideNow();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
                //  if(!hasNext){
                // loadingProgress.hideNow();
                //  }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        groupRef.addValueEventListener(subjectListener);
        if (listGroupItems.size() > 1)
            loadingProgress.hideNow();
        // Inflate the layout for this fragment
        return view;
    }

    HashMap<String, Integer> lastItems;
    public void likeDislikeReply(boolean like,int subject,String idG,String id){
        int li = 0;
        if(like){
            if(!dbqc.isLiked(idG,id)) {
                QuesListChildItem q = new QuesListChildItem();
                for (int i = 0; i < dbqc.allQuess(idG).size(); i++) {
                    if (dbqc.allQuess(idG).get(i).getId().matches(id)) {
                        li = dbqc.allQuess(idG).get(i).getNumLike() + 1;
                        q = dbqc.allQuess(idG).get(i);
                        break;
                    }
                }
                q.setLike(true);
                dbqc.updateQues(q);
                groupRef.child(ElecUtils.getSubject(user.getType(), user.getStudy(), subject)).child("الأسئلة").child(idG).child("replies").child(id).child("numlike").setValue(String.valueOf(li));
            }
        }else{
            if(!dbqc.isDisLiked(idG,id)) {
                QuesListChildItem q = new QuesListChildItem();
                for (int i = 0; i < dbqc.allQuess(idG).size(); i++) {
                    if (dbqc.allQuess(idG).get(i).getId().matches(id)) {
                        li = dbqc.allQuess(idG).get(i).getNumDis() + 1;
                        q = dbqc.allQuess(idG).get(i);
                        break;
                    }
                }
                q.setDis(true);
                dbqc.updateQues(q);
                groupRef.child(ElecUtils.getSubject(user.getType(), user.getStudy(), subject)).child("الأسئلة").child(idG).child("replies").child(id).child("numdis").setValue(String.valueOf(li));
            }
        }
    }

    private void createAddQuesDialog() {
        final Dialog d = new Dialog(context);
        d.setContentView(R.layout.dialog_add_ques);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        editTitle = (EditText) d.findViewById(R.id.Title);
        editText = (EditText) d.findViewById(R.id.Text);
        btnAdd = (Button) d.findViewById(R.id.btn_edit);
        spinner = (MaterialSpinner) d.findViewById(R.id.spinner);
        imageSubject = (ImageView) d.findViewById(R.id.imageSubject);
        ArrayList<String> subjects = new ArrayList<>();
        ArrayList<Integer> images = new ArrayList<>();
        for (int i = 0; i < dbl.allSubjects(user.getType(), user.getStudy()).size(); i++) {
            String s = dbl.allSubjects(user.getType(), user.getStudy()).get(i);
            subjects.add(s);
            images.add(ElecUtils.getResourceFromSubject(user.getType(), user.getStudy(), ElecUtils.getSubject(user.getType(), user.getStudy(), s)));
        }

        customAdapter = new CustomSpinnerSubjectAdapter(context, images, subjects);
        imageSubject.setImageResource(images.get(0));
        final Animation animFadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        imageSubject.startAnimation(animFadeOut);
        spinner.setItems(subjects);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                imageSubject.setImageResource(images.get(position));
                final Animation animFadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_in);
                imageSubject.startAnimation(animFadeOut);

            }
        });
        //  spinner.setLabel("اختر مادة");
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setError(null);
                editTitle.setError(null);
                boolean isError = false;
                if (TextUtils.isEmpty(editTitle.getText().toString())) {
                    isError = true;
                    editTitle.setError("هذه الخانة مطلوبة");
                }
                if (TextUtils.isEmpty(editText.getText().toString())) {
                    isError = true;
                    editText.setError("هذه الخانة مطلوبة");
                }
                if(String.valueOf(lastItems.get(subjects.get(spinner.getSelectedIndex()))).matches("null")){
                    isError = true;
                    Toast.makeText(context,"حدث خطأ يرجى إعادة تحميل الصفحة",Toast.LENGTH_LONG).show();
                }
                if (!isError) {
                    int su = ElecUtils.getSubject(user.getType(), user.getStudy(), subjects.get(spinner.getSelectedIndex()));
                    QuesListGroupItem q = new QuesListGroupItem(dbqg.allQuess().size(),user.getType(),user.getStudy(),su,0,String.valueOf(user.getType()) + String.valueOf(user.getStudy()) + String.valueOf(su) + String.valueOf(lastItems.get(subjects.get(spinner.getSelectedIndex()))),user.getName(),editTitle.getText().toString(),user.getUser(),editText.getText().toString(),getTime(),getDate(),user.isTeacher());
                    final HashMap<String, Object> h = new HashMap<>();
                    h.put("type", String.valueOf(q.getType()));
                    h.put("study", String.valueOf(q.getStudy()));
                    h.put("subject", String.valueOf(su));
                    h.put("num", "0");
                    h.put("name", q.getName());
                    h.put("title", q.getTitle());
                    h.put("user", q.getUser());
                    h.put("text", q.getText());
                    h.put("time", getTime());
                    h.put("date", getDate());
                    h.put("isteacher", String.valueOf(q.isTeacher() ? 1 : 0));
                    h.put("id",q.getId() );
                    isListening = false;
                    dbqg.addQues(q);
                    expandableListAdapter.add(q,new ArrayList<QuesListChildItem>());
                    groupRef.child(subjects.get(spinner.getSelectedIndex())).child("الأسئلة").child(q.getId()).setValue(h);
                    groupRef.child(subjects.get(spinner.getSelectedIndex())).child("الأسئلة").child("num").setValue(String.valueOf(lastItems.get(subjects.get(spinner.getSelectedIndex()))+1));

                    d.cancel();
                }
            }
        });
        d.setCanceledOnTouchOutside(true);
        d.show();
    }

    private void createExpList(List<QuesListGroupItem> folders) {
        final Animation animFadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        expListView.startAnimation(animFadeOut);
        expListView.setVisibility(View.VISIBLE);
        // Get the expandable list
        expListView = (AnimatedExpandableListView) view.findViewById(R.id.quesExpList);
        QuesListGroupItem[] itemsFolders = new QuesListGroupItem[folders.size()];
        for (int i = 0; i < folders.size(); i++) {
            itemsFolders[i] = folders.get(i);
        }
        String[] itemsIds = new String[folders.size()];
        for (int i = 0; i < folders.size(); i++) {
            itemsIds[i] = folders.get(i).getId();
        }
        // Setting up list
        listGroupItems = new ArrayList<QuesListGroupItem>(Arrays.asList(itemsFolders));
        listChildItems = new HashMap<String, List<QuesListChildItem>>();
        Collections.reverse(listGroupItems);
        // Adding district names and number of population as children
        for (int i1 = 0; i1 < listGroupItems.size(); i1++) {
            String folder = itemsIds[i1];
            List<QuesListChildItem> pDistricts = dbqc.allQuess(folder);
            Collections.reverse(pDistricts);
            listChildItems.put(folder, pDistricts);
        }
        expandableListAdapter = new CustomQuesExpandableListAdapter(context, listGroupItems, listChildItems, this);
        // Setting list adapter
        expListView.setAdapter(expandableListAdapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
                // currentColumn = listChildData.get(listGroupTitles.get(groupPosition)).get(childPosition).getColumn();

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
                Button bt = (Button) view.findViewById(R.id.btnViewR);
                colExp(groupPosition, bt);

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

    void colExp(int groupPosition, Button bt) {
        if (expListView.isGroupExpanded(groupPosition)) {
            expListView.collapseGroupWithAnimation(groupPosition);
            bt.setVisibility(VISIBLE);
        } else {
            expListView.expandGroupWithAnimation(groupPosition);
            bt.setVisibility(GONE);
        }
    }

    ProgressDialog progressSearch;

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
        MenuItem item = menu.findItem(R.id.action_search);
        progressSearch = new ProgressDialog(context);
        progressSearch.setCanceledOnTouchOutside(false);
        searchView = ((UserActivity) getActivity()).searchView;
        searchView.setMenuItem(item);
        searchView.setVoiceSearch(false);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // searchView.showSearch();
                if (!TextUtils.isEmpty(query)) {
                    //     progressSearch.show();
                    //       searchView.hideKeyboard(searchView);
                    if (!seq.matches(query))
                        seqN = 0;
                    FilterSe f = new FilterSe(query);
                    f.execute();
                } else {
                    expandableListAdapter.searchString = "";
                    expandableListAdapter.notifyDataSetChanged();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                addQues.setVisibility(GONE);
            }

            @Override
            public void onSearchViewClosed() {
                addQues.setVisibility(VISIBLE);
                expandableListAdapter.searchString = "";
                expandableListAdapter.notifyDataSetChanged();
                seq = "";
                seqN = 0;
            }
        });


        super.onCreateOptionsMenu(menu, inflater);
    }

    String seq = "";
    int seqN = 0;

    public class FilterSe extends AsyncTask<Void, Void, String> {
        CharSequence sequenc;

        FilterSe(CharSequence sequence) {
            sequenc = sequence;
        }

        @Override
        protected String doInBackground(Void... params) {
            String s = "";
            if (expandableListAdapter.filter(sequenc)) {
                for (int i = seqN; i < expandableListAdapter.listGroupTitle.size(); i++) {
                    if (expandableListAdapter.listGroupTitle.get(i).getTitle().toLowerCase(Locale.getDefault()).contains(sequenc.toString().toLowerCase(Locale.getDefault())) || expandableListAdapter.listGroupTitle.get(i).getText().toLowerCase(Locale.getDefault()).contains(sequenc.toString().toLowerCase(Locale.getDefault()))) {
                        expListView.scrollTo(i, 0);
                        //   smoothScroller.setTargetPosition(i);
                        //   ((LinearLayoutManager) recyclerViewMessages.getLayoutManager()).startSmoothScroll(smoothScroller);
                        seq = sequenc.toString();
                        seqN = i + 1;
//                           Toast.makeText(context, String.valueOf(i), Toast.LENGTH_LONG).show();
                        break;
                    }
                }
            }
            return s;
        }

        @Override
        protected void onPostExecute(final String token) {
            progressSearch.dismiss();
            expandableListAdapter.notifyDataSetChanged();
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
        groupRef.removeEventListener(subjectListener);
        Iterator<String> i = quesListeners.keySet().iterator();
        while(i.hasNext()){
            String s = i.next();
            groupRef.child(s).child("الأسئلة").removeEventListener(quesListeners.get(s));
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
}