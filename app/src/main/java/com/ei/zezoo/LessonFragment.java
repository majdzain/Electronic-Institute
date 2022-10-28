package com.ei.zezoo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.capybaralabs.swipetoreply.ISwipeControllerActions;
import com.capybaralabs.swipetoreply.SwipeController;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class LessonFragment extends Fragment implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, VideoControllerView.MediaPlayerControlListener, MediaPlayer.OnVideoSizeChangedListener, MediaPlayer.OnCompletionListener, AudioRecordView.RecordingListener, View.OnClickListener, AttachmentOptionsListener, FragmentCommunicator {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private SecondaryDrawerItem DrawerItem;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Context context;
    View view;
    SharedPreferences pref;
    Resources res;
    ResizeSurfaceView mVideoSurface;
    MediaPlayer mMediaPlayer;
    VideoControllerView controller;
    public int mVideoWidth;
    public int mVideoHeight;
    private View mContentView;
    private View mLoadingView;
    private boolean mIsComplete;
    private CommentSQLDatabaseHandler dbc;
    private List<CommentListChildItem> listComments;
    UserSQLDatabaseHandler dbu;
    private AudioRecordView audioRecordView;
    private RecyclerView recyclerViewMessages;
    private CommentAdapter commentAdapter;
    private long time;
    private MediaRecorder myAudioRecorder;
    private String outputFile;
    private boolean isPaused;
    FrameLayout msgLayout;
    LinearLayout commentNewImages, layoutIconsReply;
    ImageView iconReply;
    TextView txtIconReply;
    FrameLayout layoutReply;
    TextView nameReply, textReply;
    CardView messageReply;
    ImageView closeReply;
    ArrayList<Uri> gridImagesArray;
    ArrayList<Integer> gridImagesNumberArray;
    private StorageReference userImagesReference;
    private ProgressBar loadingImages;
    private String idReply = "";
    private StorageReference userVoicesReference;
    ViewGroup parent;
    LinearLayout txtComments;
    DilatingDotsProgressBar loadingProgress;
    Menu men;
    SharedPreference preference;
    MenuItem menuConnected;
    boolean isConnectAnimationStarted = false;
    private DatabaseReference ref;
    private FirebaseAuth mauth;
    private FirebaseUser currentuser;
    ProgressBar videoProgress;
    NumberProgressBar downloadProgress;
    RelativeLayout videoDownloader;
    TextView txtDownload;
    Button btnDownload;
    Button btnWatch;
    Button btnCancel;
    Button btnPause;
    private FrameLayout videoWatcher;
    boolean isDownloading = false;
    private boolean isResume = false;
    private Handler handler = new Handler();
    LessonListChildItem Lesson;
    LessonSQLDatabaseHandler dbl;
    private boolean isMediaSet = false;
    private Runnable runCurrentVideo;
    Handler handVideo = new Handler();
    DownloadSQLDatabaseHandler dbd;
    NotifSQLDatabaseHandler dbn;
    MaterialSearchView searchView;
    LinearLayout linearVideo;
    ImageView btnHide;
    RecyclerView.SmoothScroller smoothScroller;
    private ProgressDialog Loadingbar;

    ChildEventListener childEventListener;
    private FirebaseAuth mAuth;
    @Nullable
    private DatabaseReference UserRef, GroupNameRef, GroupMessageKeyRef;
    @Nullable
    private String currentUserID, currentUserName, currentDate, currentTime;
    Thread th;
    boolean isVideoGone = false;
    Iterator iterator;
    String userId;
    private static SimpleDateFormat timeFormatter;
    HashMap<String, Object> messageInfoMap;
    boolean isSomeNotUpload = false;

    public LessonFragment(LessonListChildItem lesson, SecondaryDrawerItem drawerItem) {
        Lesson = lesson;
        DrawerItem = drawerItem;
    }

    // TODO: Rename and change types and number of parameters

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_lesson, container, false);
        parent = container;
        context = view.getContext();
        pref = context.getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        ref = FirebaseDatabase.getInstance().getReference();
        mauth = FirebaseAuth.getInstance();
        currentuser = mauth.getCurrentUser();
        userId = currentuser.getUid();
        setHasOptionsMenu(true);
        res = getResources();
        dbc = new CommentSQLDatabaseHandler(context);
        dbu = new UserSQLDatabaseHandler(context);
        dbl = new LessonSQLDatabaseHandler(context);
        dbd = new DownloadSQLDatabaseHandler(context);
        dbn = new NotifSQLDatabaseHandler(context);
        userImagesReference = FirebaseStorage.getInstance().getReference().child("Comment Images");
        userVoicesReference = FirebaseStorage.getInstance().getReference().child("Comment Voices");
        //listView = (ListView) view.findViewById(R.id.listComments);
        mVideoSurface = (ResizeSurfaceView) view.findViewById(R.id.videoSurface);
        mContentView = view.findViewById(R.id.video_container);
        mLoadingView = view.findViewById(R.id.loading);
        Loadingbar = new ProgressDialog(getActivity());
        msgLayout = view.findViewById(R.id.layoutMain);
        videoProgress = view.findViewById(R.id.bottom_progressbar);
        videoWatcher = (FrameLayout) view.findViewById(R.id.videoSurfaceContainer);
        videoDownloader = (RelativeLayout) view.findViewById(R.id.videoDownloader);
        downloadProgress = (NumberProgressBar) view.findViewById(R.id.number_progress_bar);
        txtDownload = (TextView) view.findViewById(R.id.txtDownload);
        btnDownload = (Button) view.findViewById(R.id.btnDownload);
        btnWatch = (Button) view.findViewById(R.id.btnWatch);
        btnCancel = (Button) view.findViewById(R.id.btnStop);
        btnPause = (Button) view.findViewById(R.id.btnPause);
        linearVideo = (LinearLayout) view.findViewById(R.id.layoutVideo);
        btnHide = (ImageView) view.findViewById(R.id.hideVideo);
        downloadProgress.setMax(100);
        Lesson.setNew(false);
        dbl.updateLesson(Lesson);
        NotifListChildItem N = null;
        for (int i = 0; i < dbn.allNotifs(dbu.getCurrentUser().getType(), dbu.getCurrentUser().getStudy()).size(); i++) {
            NotifListChildItem n = dbn.allNotifs(dbu.getCurrentUser().getType(), dbu.getCurrentUser().getStudy()).get(i);
            if (n.getTypeN() == 0 || n.getTypeN() == 1) {
                if (n.getSubject() == Lesson.getSubject() && n.getName().matches("الدرس " + String.valueOf(Lesson.getLesson() - 100 + 1) + " : " + Lesson.getLessonN())) {
                    N = n;
                }
            }
        }
        final NotifListChildItem Notif = N;
        if (Notif != null) {
            Notif.setNew(false);
            dbn.updateNotif(Notif);
        }
        DrawerItem.withTag(Lesson);
        ((UserActivity) getActivity()).result.updateItem(DrawerItem);

        btnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isVideoGone) {
                    Animation upBottom = AnimationUtils.loadAnimation(context, R.anim.view_video);
                    upBottom.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            btnHide.setImageResource(R.drawable.hide);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    linearVideo.startAnimation(upBottom);
                    linearVideo.setVisibility(VISIBLE);
                    if (isPaused) {
                        mMediaPlayer.start();
                        isPaused = false;
                    }
                    isVideoGone = false;
                } else {
                    Animation bottomUp = AnimationUtils.loadAnimation(context, R.anim.hide_video);
                    bottomUp.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                            btnHide.setImageResource(R.drawable.show);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    linearVideo.startAnimation(bottomUp);
                    linearVideo.setVisibility(View.GONE);
                    if (isMediaSet) {
                        if (mMediaPlayer.isPlaying()) {
                            mMediaPlayer.pause();
                            isPaused = true;
                        }
                    }

                    isVideoGone = true;
                }
            }
        });

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (isConnectingToInternet(context)) {
                    txtDownload.setVisibility(View.VISIBLE);
                    btnDownload.setVisibility(GONE);
                    btnCancel.setVisibility(View.VISIBLE);
                    btnWatch.setVisibility(GONE);
                    btnPause.setVisibility(View.VISIBLE);
                    txtDownload.setText("جاري التحميل");
                    isDownloading = true;
                    try {
                        th.start();
                    } catch (Exception e) {

                    }

                    DownloadListChildItem Download = new DownloadListChildItem();
                    boolean isExist = false;
                    for (int i = 0; i < dbd.allDownloads().size(); i++) {
                        DownloadListChildItem d = dbd.allDownloads().get(i);
                        if (d.getType() == Lesson.getType() && d.getStudy() == Lesson.getStudy() && d.getSubject() == Lesson.getSubject() && d.getLesson() == Lesson.getLesson()) {
                            Download = d;
                            isExist = true;
                        }
                    }
                    ((UserActivity) getActivity()).addDownload(Lesson, Download, isExist);
                } else {
                    txtDownload.setText("غير متصل بالإنترنت!");
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtDownload.setVisibility(GONE);
                btnDownload.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(GONE);
                btnWatch.setVisibility(GONE);
                btnPause.setVisibility(GONE);
                btnDownload.setText("تحميل الدرس");
                btnDownload.setCompoundDrawablesWithIntrinsicBounds(null, null, res.getDrawable(R.drawable.download_video), null);
                ((UserActivity) getActivity()).stopDownload(Lesson);
            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtDownload.setVisibility(GONE);
                btnDownload.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(GONE);
                btnWatch.setVisibility(GONE);
                btnPause.setVisibility(GONE);
                btnDownload.setText("استئناف التحميل");
                btnDownload.setCompoundDrawablesWithIntrinsicBounds(null, null, res.getDrawable(R.drawable.resume_download), null);
                //th.stop();
                ((UserActivity) getActivity()).pauseDownload(Lesson);
            }
        });
        btnWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoDownloader.setVisibility(GONE);
                videoWatcher.setVisibility(View.VISIBLE);
                Lesson.setDownload(3);
                List<NotifListChildItem> list = dbn.allNotifs(dbu.getCurrentUser().getType(), dbu.getCurrentUser().getStudy());
                if (Notif != null) {
                    Notif.setDownload(3);
                    dbn.updateNotif(Notif);
                }
                dbl.updateLesson(Lesson);
                DrawerItem.withIcon(R.drawable.seen);
                DrawerItem.withTag(Lesson);
                ((UserActivity) getActivity()).result.updateItem(DrawerItem);
                final SurfaceHolder videoHolder = mVideoSurface.getHolder();

                videoHolder.addCallback(LessonFragment.this);
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setOnVideoSizeChangedListener(LessonFragment.this);

                controller = new VideoControllerView.Builder(getActivity(), LessonFragment.this)
                        .withVideoTitle(Lesson.getLessonN())
                        .withVideoSurfaceView(mVideoSurface)//to enable toggle display controller view
                        .canControlBrightness(true)
                        .canControlVolume(true)
                        .canSeekVideo(true)
                        .exitIcon(R.drawable.video_top_back).setOutProgress(videoProgress)
                        .pauseIcon(R.drawable.ic_media_pause)
                        .playIcon(R.drawable.ic_media_play)
                        .shrinkIcon(R.drawable.ic_media_fullscreen_shrink)
                        .stretchIcon(R.drawable.ic_media_fullscreen_stretch)
                        .build(videoWatcher);//layout container that hold video play view

                mLoadingView.setVisibility(View.VISIBLE);
                controller.mRepeatButton.requestFocus();
                controller.mRepeatButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        resetPlayer();
                        mMediaPlayer = new MediaPlayer();
                        mMediaPlayer.setOnVideoSizeChangedListener(LessonFragment.this);
                        mLoadingView.setVisibility(View.VISIBLE);
                        setMediaPlayer(Lesson.getFile());
                        controller.setSeekProgress();
                    }
                });
                setMediaPlayer(Lesson.getFile());

                mVideoSurface.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (!controller.isShowing())
                            ((UserActivity) getActivity()).downloadBtn.setVisibility(GONE);
                        controller.toggleControllerView();
                        return false;
                    }
                });
            }
        });
        if (Lesson.getDownload() == 1) {
            th = new Thread(new Runnable() {
                public void run() {
                    while (isDownloading) {
                        handler.post(new Runnable() {
                            public void run() {
                                if (txtDownload.getText().toString().contains("..."))
                                    txtDownload.setText(txtDownload.getText().toString().replace("...", ""));
                                txtDownload.setText(txtDownload.getText() + ".");
                            }
                        });
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
            DownloadListChildItem down = new DownloadListChildItem();
            for (int i = 0; i < dbd.allDownloads().size(); i++) {
                DownloadListChildItem d = dbd.allDownloads().get(i);
                if (d.getType() == Lesson.getType() && d.getStudy() == Lesson.getStudy() && d.getSubject() == Lesson.getSubject() && d.getLesson() == Lesson.getLesson())
                    down = d;
            }
            txtDownload.setText("جاري التحميل");
            if (down.getDownload() == DownloadListChildItem.RUNNING) {
                txtDownload.setVisibility(View.VISIBLE);
                btnDownload.setVisibility(GONE);
                btnCancel.setVisibility(View.VISIBLE);
                btnWatch.setVisibility(GONE);
                btnPause.setVisibility(View.VISIBLE);
                isDownloading = true;
                try {
                    th.start();
                } catch (Exception e) {

                }
                downloadProgress.setProgress(down.getProgress());
            } else if (down.getDownload() == DownloadListChildItem.PAUSED) {
                txtDownload.setVisibility(GONE);
                btnDownload.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(GONE);
                btnWatch.setVisibility(GONE);
                btnPause.setVisibility(GONE);
                btnDownload.setText("استئناف التحميل");
                btnDownload.setCompoundDrawablesWithIntrinsicBounds(null, null, res.getDrawable(R.drawable.resume_download), null);
                isResume = true;
                downloadProgress.setProgress(down.getProgress());
            } else {
                videoDownloader.setVisibility(View.VISIBLE);
                videoWatcher.setVisibility(GONE);
                downloadProgress.setProgress(0);
                txtDownload.setText("");
                btnWatch.setVisibility(GONE);
                btnCancel.setVisibility(GONE);
                btnPause.setVisibility(GONE);
                btnDownload.setVisibility(View.VISIBLE);
            }
        } else if (Lesson.getDownload() == 2) {
            videoDownloader.setVisibility(View.VISIBLE);
            videoWatcher.setVisibility(GONE);
            downloadProgress.setProgress(100);
            txtDownload.setText("تم التحميل");
            btnWatch.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(GONE);
            btnPause.setVisibility(GONE);
            btnDownload.setVisibility(GONE);
        } else if (Lesson.getDownload() == 3) {
            videoDownloader.setVisibility(GONE);
            videoWatcher.setVisibility(View.VISIBLE);
            final SurfaceHolder videoHolder = mVideoSurface.getHolder();

            videoHolder.addCallback(LessonFragment.this);
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnVideoSizeChangedListener(LessonFragment.this);

            controller = new VideoControllerView.Builder(getActivity(), LessonFragment.this)
                    .withVideoTitle(Lesson.getLessonN())
                    .withVideoSurfaceView(mVideoSurface)//to enable toggle display controller view
                    .canControlBrightness(true)
                    .canControlVolume(true)
                    .canSeekVideo(true)
                    .exitIcon(R.drawable.video_top_back).setOutProgress(videoProgress)
                    .pauseIcon(R.drawable.ic_media_pause)
                    .playIcon(R.drawable.ic_media_play)
                    .shrinkIcon(R.drawable.ic_media_fullscreen_shrink)
                    .stretchIcon(R.drawable.ic_media_fullscreen_stretch)
                    .build(videoWatcher);//layout container that hold video play view

            mLoadingView.setVisibility(View.VISIBLE);
            controller.mRepeatButton.requestFocus();
            controller.mRepeatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    resetPlayer();
                    mMediaPlayer = new MediaPlayer();
                    mMediaPlayer.setOnVideoSizeChangedListener(LessonFragment.this);
                    mLoadingView.setVisibility(View.VISIBLE);
                    setMediaPlayer(Lesson.getFile());
                    //   mMediaPlayer.setDisplay(mVideoSurface.getHolder());
                    //   mMediaPlayer.prepareAsync();
                    controller.setSeekProgress();
                }
            });

            setMediaPlayer(Lesson.getFile());

            mVideoSurface.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (!controller.isShowing())
                        ((UserActivity) getActivity()).downloadBtn.setVisibility(GONE);
                    controller.toggleControllerView();
                    return false;
                }
            });
        }

        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.RECORD_AUDIO))
            permissionsNeeded.add("android.permission.RECORD_AUDIO");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("android.permission.WRITE_EXTERNAL_STORAGE");
        if (permissionsList.size() > 0)
            ActivityCompat.requestPermissions(getActivity(),
                    permissionsList.toArray(new String[permissionsList.size()]),
                    0);
        listComments = dbc.allCommentsAt(Lesson.getType(), Lesson.getStudy(), Lesson.getSubject(), Lesson.getLesson());
        initFirebase();

        createRList(listComments);
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    DisplayMessages(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    DisplayMessages(dataSnapshot);
                }
            }


            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        return view;
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission))
                return false;
        }
        return true;
    }

    private void setMediaPlayer(String s) {
        try {
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            //  AssetFileDescriptor afd = context.getAssets().openFd(s);
            //mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mMediaPlayer.setDataSource(s);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            runCurrentVideo = new Runnable() {

                @Override
                public void run() {
                    try {
                        int position = controller.mMediaPlayerControlListener.getCurrentPosition();
                        int duration = controller.mMediaPlayerControlListener.getDuration();
                        if (duration > 0) {
                            // use long to avoid overflow
                            long pos = 1000L * position / duration;
                            videoProgress.setProgress((int) pos);
                        }
                        //get buffer percentage
                        int percent = controller.mMediaPlayerControlListener.getBufferPercentage();

                        //set buffer progress
                        videoProgress.setSecondaryProgress(percent * 10);
                        handVideo.postDelayed(this, 200);
                    } catch (IllegalStateException ed) {
                        ed.printStackTrace();
                    }
                }
            };

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mMediaPlayer.setDisplay(mVideoSurface.getHolder());
            mMediaPlayer.prepareAsync();
        } catch (Exception e) {

        }
        isMediaSet = true;
    }

    private void initFirebase() {
        //currentGroupName = Lesson.getLessonN();
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        GroupNameRef = FirebaseDatabase.getInstance().getReference().child(ElecUtils.getType(Lesson.getType())).child(ElecUtils.getStudy(Lesson.getType(), Lesson.getStudy())).child(Lesson.getSubjectN()).child(Lesson.getLessonN() + "|" + String.valueOf(Lesson.getLesson()));
        GetUserInfo();
    }

    @Override
    public void onStart() {
        GroupNameRef.addChildEventListener(childEventListener);
        super.onStart();
    }

    private void GetUserInfo() {
        UserRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    currentUserName = dataSnapshot.child("name").getValue().toString();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void DisplayMessages(DataSnapshot dataSnapshot) {
        iterator = dataSnapshot.getChildren().iterator();
        boolean isFirst = true;
        while (iterator.hasNext()) {
            String column = (String) ((DataSnapshot) iterator.next()).getValue();
            if (!column.matches("test")) {
                loadingProgress.hideNow();
                txtComments.setVisibility(GONE);
                final CommentListChildItem c = new CommentListChildItem();
                c.setStudyType(Lesson.getType());
                c.setStudy(Lesson.getStudy());
                c.setSubject(Lesson.getSubject());
                c.setLesson(Lesson.getLesson());
                c.setColumn(dbc.allComments().size());
                //    c.setTeacher(dbu.getCurrentUser().isTeacher());
                c.setDate((String) ((DataSnapshot) iterator.next()).getValue());
                c.setId((String) ((DataSnapshot) iterator.next()).getValue());
                c.setImage1((String) ((DataSnapshot) iterator.next()).getValue());
                c.setImage2((String) ((DataSnapshot) iterator.next()).getValue());
                c.setImage3((String) ((DataSnapshot) iterator.next()).getValue());
                c.setImage4((String) ((DataSnapshot) iterator.next()).getValue());
                c.setImage5((String) ((DataSnapshot) iterator.next()).getValue());
                c.setImage6((String) ((DataSnapshot) iterator.next()).getValue());
                c.setImage7((String) ((DataSnapshot) iterator.next()).getValue());
                c.setImage8((String) ((DataSnapshot) iterator.next()).getValue());
                c.setImage9((String) ((DataSnapshot) iterator.next()).getValue());
                c.setTeacher((boolean) ((DataSnapshot) iterator.next()).getValue());
                c.setComment((String) ((DataSnapshot) iterator.next()).getValue());
                c.setName((String) ((DataSnapshot) iterator.next()).getValue());
                c.setRecrordData((String) ((DataSnapshot) iterator.next()).getValue());
                c.setRecordTime(Integer.valueOf((String) ((DataSnapshot) iterator.next()).getValue()));
                c.setReply((String) ((DataSnapshot) iterator.next()).getValue());
                c.setTime((String) ((DataSnapshot) iterator.next()).getValue());
                c.setType((String) ((DataSnapshot) iterator.next()).getValue());
                c.setUser((String) ((DataSnapshot) iterator.next()).getValue());
                c.setColumn(dbc.allComments().size());
                boolean isCommentExist = false;
                List<String> list = dbc.allIds();
                for (int i = 0; i < list.size(); i++) {
                    if (c.getId().matches(list.get(i))) {
                        isCommentExist = true;
                    }
                }
                if (!isCommentExist) {
                    dbc.addComment(c);
                    commentAdapter.add(c);
                    commentAdapter.notifyDataSetChanged();
                    scrollToBottom();
                }
            } else if (!iterator.hasNext()) {
                loadingProgress.hideNow();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isMediaSet) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
                isPaused = true;
            } else
                isPaused = true;
        }
    }

    public static String getAudioTime(long time) {
        timeFormatter = new SimpleDateFormat("m:ss", Locale.getDefault());
        time *= 1000;
        timeFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return timeFormatter.format(new Date(time));
    }

    private void createRList(final List<CommentListChildItem> comments) {

        // Construct the data source
        audioRecordView = new AudioRecordView();
        // this is to make your layout the root of audio record view, root layout supposed to be empty..
        audioRecordView.initView((FrameLayout) view.findViewById(R.id.layoutMain));
        // this is to provide the container layout to the audio record view..
        View containerView = audioRecordView.setContainerView(R.layout.layout_chatting);
        audioRecordView.setRecordingListener(this);
        recyclerViewMessages = view.findViewById(R.id.recyclerViewMessages);
        commentNewImages = audioRecordView.gridImages;
        loadingImages = audioRecordView.loadingImages;
        layoutReply = audioRecordView.layoutReply;
        closeReply = audioRecordView.closeReply;
        messageReply = audioRecordView.messageReply;
        nameReply = audioRecordView.nameReply;
        textReply = audioRecordView.textReply;
        txtComments = audioRecordView.txtComments;
        loadingProgress = audioRecordView.loadingProgress;
        txtIconReply = audioRecordView.txtIconReply;
        layoutIconsReply = audioRecordView.layoutIconsReply;
        iconReply = audioRecordView.iconReply;
        loadingProgress.showNow();
        gridImagesArray = new ArrayList<Uri>();
        gridImagesNumberArray = new ArrayList<Integer>();

        final Animation animFadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        recyclerViewMessages.startAnimation(animFadeOut);
        recyclerViewMessages.setVisibility(View.VISIBLE);
        smoothScroller = new LinearSmoothScroller(context) {
            @Override
            protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };
        commentAdapter = new CommentAdapter(context, getActivity(), comments);

        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerViewMessages.setHasFixedSize(false);

        recyclerViewMessages.setAdapter(commentAdapter);
        if (commentAdapter.getItemCount() > 0) {
            txtComments.setVisibility(View.GONE);
            loadingProgress.hideNow();
        }

        recyclerViewMessages.setItemAnimator(new DefaultItemAnimator());
        SwipeController controller = new SwipeController(context, new ISwipeControllerActions() {
            @Override
            public void onSwipePerformed(int position) {
                layoutReply.setVisibility(View.VISIBLE);
                CommentListChildItem c = comments.get(position);
                if (!TextUtils.isEmpty(c.getComment()))
                    messageReply.setVisibility(View.VISIBLE);
                else
                    messageReply.setVisibility(GONE);
                if (c.getRecordTime() != 0) {
                    if (c.isTeacher()) {
                        nameReply.setText(c.getName());
                        nameReply.setTextColor(res.getColor(R.color.orange));
                    } else if (c.getUser().matches(dbu.getCurrentUser().getUser())) {
                        nameReply.setText("أنت");
                        nameReply.setTextColor(res.getColor(R.color.blue));
                    } else {
                        nameReply.setText(c.getName());
                        nameReply.setTextColor(res.getColor(R.color.purple));
                    }
                    layoutIconsReply.setVisibility(View.VISIBLE);
                    txtIconReply.setText("تسجيل صوتي" + "(" + getAudioTime(c.getRecordTime()) + ")");
                    iconReply.setImageResource(R.drawable.voice_comment);
                } else {

                    if (c.isTeacher()) {
                        messageReply.setCardBackgroundColor(res.getColor(R.color.orange));
                        nameReply.setText(c.getName());
                        nameReply.setTextColor(res.getColor(R.color.orange));
                    } else if (c.getUser().matches(dbu.getCurrentUser().getUser())) {
                        messageReply.setCardBackgroundColor(res.getColor(R.color.blue));
                        nameReply.setText("أنت");
                        nameReply.setTextColor(res.getColor(R.color.blue));
                    } else {
                        messageReply.setCardBackgroundColor(res.getColor(R.color.purple));
                        nameReply.setText(c.getName());
                        nameReply.setTextColor(res.getColor(R.color.purple));
                    }
                    textReply.setText(c.getComment());
                    int imgCount = 0;
                    ArrayList<String> images = new ArrayList<>();
                    if (!TextUtils.isEmpty(c.getImage1())) {
                        imgCount++;
                        images.add(c.getImage1());
                    }
                    if (!TextUtils.isEmpty(c.getImage2())) {
                        imgCount++;
                        images.add(c.getImage2());
                    }
                    if (!TextUtils.isEmpty(c.getImage3())) {
                        imgCount++;
                        images.add(c.getImage3());
                    }
                    if (!TextUtils.isEmpty(c.getImage4())) {
                        imgCount++;
                        images.add(c.getImage4());
                    }
                    if (!TextUtils.isEmpty(c.getImage5())) {
                        imgCount++;
                        images.add(c.getImage5());
                    }
                    if (!TextUtils.isEmpty(c.getImage6())) {
                        imgCount++;
                        images.add(c.getImage6());
                    }
                    if (!TextUtils.isEmpty(c.getImage7())) {
                        imgCount++;
                        images.add(c.getImage7());
                    }
                    if (!TextUtils.isEmpty(c.getImage8())) {
                        imgCount++;
                        images.add(c.getImage8());
                    }
                    if (!TextUtils.isEmpty(c.getImage9())) {
                        imgCount++;
                        images.add(c.getImage9());
                    }
                    if (imgCount == 0) {
                        layoutIconsReply.setVisibility(View.GONE);
                    } else if (imgCount == 1) {
                        layoutIconsReply.setVisibility(View.VISIBLE);
                        txtIconReply.setText("صورة");
                        iconReply.setImageResource(R.drawable.image);
                    } else {
                        layoutIconsReply.setVisibility(View.VISIBLE);
                        txtIconReply.setText("صور");
                        iconReply.setImageResource(R.drawable.images);
                    }
                }
                idReply = c.getId();
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(controller);
        itemTouchHelper.attachToRecyclerView(recyclerViewMessages);
        closeReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutReply.setVisibility(View.GONE);
                idReply = "";
            }
        });
        scrollToBottom();
        setListener();
        audioRecordView.getMessageView().requestFocus();
        audioRecordView.setAttachmentOptions(AttachmentOption.getDefaultList(), this);
        audioRecordView.removeAttachmentOptionAnimation(false);
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

        mVideoHeight = mp.getVideoHeight();
        mVideoWidth = mp.getVideoWidth();
        if (mVideoHeight > 0 && mVideoWidth > 0)
            mVideoSurface.adjustSize(mContentView.getWidth(), mContentView.getHeight(), mMediaPlayer.getVideoWidth(), mMediaPlayer.getVideoHeight());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mVideoWidth > 0 && mVideoHeight > 0)
            mVideoSurface.adjustSize(getDeviceWidth(context), getDeviceHeight(context), mVideoSurface.getWidth(), mVideoSurface.getHeight());
    }

    private void resetPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public static int getDeviceWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics.widthPixels;
    }

    public static int getDeviceHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics.heightPixels;
    }

    // Implement SurfaceHolder.Callback
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (isPaused) {
            mMediaPlayer.setDisplay(holder);
            mMediaPlayer.start();

            isPaused = false;
        } else {
            try {
                mMediaPlayer.setDisplay(holder);
                mMediaPlayer.prepareAsync();
            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //setup video controller view
        mLoadingView.setVisibility(View.GONE);
        mVideoSurface.setVisibility(View.VISIBLE);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
        handVideo.postDelayed(runCurrentVideo, 200);
        mIsComplete = false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (null != mMediaPlayer)
            return mMediaPlayer.getCurrentPosition();
        else
            return 0;
    }

    @Override
    public int getDuration() {
        if (null != mMediaPlayer)
            return mMediaPlayer.getDuration();
        else
            return 0;
    }

    @Override
    public boolean isPlaying() {
        if (null != mMediaPlayer)
            return mMediaPlayer.isPlaying();
        else
            return false;
    }

    @Override
    public boolean isComplete() {
        return mIsComplete;
    }

    @Override
    public void pause() {
        if (null != mMediaPlayer) {
            mMediaPlayer.pause();
        }

    }

    @Override
    public void seekTo(int i) {
        if (null != mMediaPlayer) {
            mMediaPlayer.seekTo(i);
        }
    }

    @Override
    public void start() {
        if (null != mMediaPlayer) {
            mMediaPlayer.start();
            mIsComplete = false;
        }
    }

    @Override
    public boolean isFullScreen() {
        return getActivity().getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE ? true : false;

    }

    @Override
    public void toggleFullScreen() {
        if (isFullScreen()) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            ((UserActivity) getActivity()).getSupportActionBar().show();
            ((UserActivity) getActivity()).frameLayout.setVisibility(VISIBLE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            ((UserActivity) getActivity()).getSupportActionBar().hide();
            ((UserActivity) getActivity()).frameLayout.setVisibility(GONE);
            ((UserActivity) getActivity()).result.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    public void exit() {
        resetPlayer();
        getActivity().finish();
    }

    @Override
    public void onStop() {
        GroupNameRef.removeEventListener(childEventListener);
        super.onStop();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mIsComplete = true;
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

    private void setListener() {
        audioRecordView.showAttachmentIcon(false);
        audioRecordView.showEmojiIcon(false);
        CardView send = (CardView) audioRecordView.getSendView();
        // send.setCardBackgroundColor(res.getColor(R.color.blue));
        audioRecordView.getEmojiView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioRecordView.hideAttachmentOptionView();
                showToast("Emoji Icon Clicked");
            }
        });

        audioRecordView.getCameraView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioRecordView.hideAttachmentOptionView();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                ((UserActivity) getActivity()).isStartAnotherActivity = true;
                ((UserActivity) getActivity()).isPicImages = true;
                startActivityForResult(intent, 32);
            }
        });

        audioRecordView.getSendView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg = audioRecordView.getMessageView().getText().toString().trim();
                audioRecordView.getMessageView().setText("");
                CommentListChildItem c = new CommentListChildItem(dbc.allComments().size(), dbu.getCurrentUser().getUserId() + "/" + String.valueOf(1000000 + dbu.getCurrentUser().getNumComments()), Lesson.getType(), Lesson.getStudy(), Lesson.getSubject(), Lesson.getLesson(), dbu.getCurrentUser().isTeacher(), dbu.getCurrentUser().getUser(), dbu.getCurrentUser().getName(), msg, getTime(), getDate(), null, null, null, null, null, null, null, null, null, null, 0, "", idReply, "msg");
                String messageKey = GroupNameRef.push().getKey();
                HashMap<String, Object> groupMessageKey = new HashMap<>();
                GroupNameRef.updateChildren(groupMessageKey);
                GroupMessageKeyRef = GroupNameRef.child(messageKey);
                messageInfoMap = new HashMap<>();
                messageInfoMap.put("column", String.valueOf(dbu.getCurrentUser().getNumComments() + 1));
                messageInfoMap.put("recordData", "");
                messageInfoMap.put("recordTime", "0");
                messageInfoMap.put("date", getDate());
                messageInfoMap.put("time", getTime());
                messageInfoMap.put("type", c.getType());
                messageInfoMap.put("message", msg);
                messageInfoMap.put("name", dbu.getCurrentUser().getName());
                messageInfoMap.put("isTeacher", c.isTeacher);

                messageInfoMap.put("user", c.getUser());
                messageInfoMap.put("id", c.getId());
                messageInfoMap.put("reply", idReply);
                if (gridImagesArray.size() == 0) {
                    for (int i = gridImagesArray.size() + 1; i < 10; i++) {
                        messageInfoMap.put("image" + String.valueOf(i), "");
                    }
                    GroupMessageKeyRef.updateChildren(messageInfoMap);
                    UserListChildItem u = dbu.getCurrentUser();
                    u.setNumComments(u.getNumComments() + 1);
                    dbu.updateUser(u);
                    ((UserActivity) getActivity()).isCommentChange = true;
                    ref.child("Users").child(userId).child("numcomments").setValue(String.valueOf(dbu.getCurrentUser().getNumComments()));
                    scrollToBottom();
                    layoutReply.setVisibility(View.GONE);
                    idReply = "";
                } else {
                    audioRecordView.imageViewAudio.setVisibility(GONE);
                    audioRecordView.imageViewSend.setVisibility(GONE);
                    audioRecordView.imageViewSend.animate().scaleX(0f).scaleY(0f).setDuration(100).setInterpolator(new LinearInterpolator()).start();
                }
                for (int i = 1; i < 10; i++) {
                    messageInfoMap.put("image" + String.valueOf(i), "");
                }
                final int imageS = pref.getInt("imagesNumber", 1000);
                for (int i = 0; i < gridImagesArray.size(); i++) {
                    FrameLayout f = (FrameLayout) commentNewImages.getChildAt(i);
                    ImageView close = f.findViewById(R.id.item_image_close);
                    FrameLayout f1 = f.findViewById(R.id.layout_download);
                    ProgressBar progress = f.findViewById(R.id.downloadingImage);
                    ImageView done = f.findViewById(R.id.done_image);
                    ImageView not_done = f.findViewById(R.id.not_done_image);
                    ImageView upload = f.findViewById(R.id.upload_image);
                    close.setVisibility(View.GONE);
                    f1.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.VISIBLE);
                    done.setVisibility(View.GONE);
                    not_done.setVisibility(View.GONE);
                    upload.setVisibility(View.GONE);
                    final int p = i;
                    final int imageNumber = gridImagesNumberArray.get(i);
                    StorageReference file = userImagesReference.child(dbu.getCurrentUser().getId() + "_Image_" + String.valueOf(imageNumber) + ".jpg");
                    Uri uri = gridImagesArray.get(i);
                    file.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                messageInfoMap.put("image" + String.valueOf(p + 1), dbu.getCurrentUser().getId() + "_Image_" + String.valueOf(imageNumber));
                                progress.setVisibility(View.GONE);
                                done.setVisibility(View.VISIBLE);
                                not_done.setVisibility(View.GONE);
                                upload.setVisibility(GONE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        commentNewImages.removeViewAt(gridImagesArray.indexOf(uri));
                                        gridImagesArray.remove(uri);
                                        boolean a = gridImagesNumberArray.remove((Integer) imageNumber);
                                    }
                                }, 800);
                                if (gridImagesArray.size() == 0) {
                                    audioRecordView.isImages = false;
                                    audioRecordView.showSend(false);
                                }
                            } else {
                                isSomeNotUpload = true;
                                progress.setVisibility(View.GONE);
                                done.setVisibility(View.GONE);
                                not_done.setVisibility(View.VISIBLE);
                                upload.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        uploadNotUploadedImage(uri, f, imageNumber);
                                    }
                                });
                                Toast.makeText(context, "لم يتم تحميل الصورة" + String.valueOf(p + 1), Toast.LENGTH_LONG).show();
                                String message = task.getException().toString();
                                Toast.makeText(context, "Error : " + message, Toast.LENGTH_LONG).show();
                            }
                            boolean isLast = true;
                            for (int o = 0; o < gridImagesArray.size(); o++) {
                                FrameLayout f3 = (FrameLayout) commentNewImages.getChildAt(o);
                                ProgressBar progress3 = f3.findViewById(R.id.downloadingImage);
                                if (progress3.getVisibility() == View.VISIBLE)
                                    isLast = false;
                            }
                            if (isLast) {
                                int num = 0;
                                for (int j = 1; j < 10; j++) {
                                    if (!TextUtils.isEmpty(messageInfoMap.get("image" + String.valueOf(j)).toString()))
                                        num++;
                                }
                                if (num > 0)
                                    c.setType("image" + String.valueOf(num));
                                messageInfoMap.put("type", c.getType());
                                GroupMessageKeyRef.updateChildren(messageInfoMap);
                                UserListChildItem u = dbu.getCurrentUser();
                                u.setNumComments(u.getNumComments() + 1);
                                dbu.updateUser(u);
                                pref.edit().putBoolean("isCommentChange", false).commit();
                                ref.child("Users").child(userId).child("numcomments").setValue(String.valueOf(dbu.getCurrentUser().getNumComments()));
                                scrollToBottom();
                                layoutReply.setVisibility(View.GONE);
                                idReply = "";
                                pref.edit().putBoolean("isCommentChange", true).commit();
                                audioRecordView.imageViewAudio.setVisibility(View.VISIBLE);
                                audioRecordView.isImages = false;
                                audioRecordView.showSend(false);
                                if (isSomeNotUpload) {
                                    for (int g = 0; g < gridImagesArray.size(); g++) {
                                        FrameLayout f = (FrameLayout) commentNewImages.getChildAt(g);
                                        ImageView close = f.findViewById(R.id.item_image_close);
                                        FrameLayout f1 = f.findViewById(R.id.layout_download);
                                        ProgressBar progress = f.findViewById(R.id.downloadingImage);
                                        ImageView done = f.findViewById(R.id.done_image);
                                        ImageView not_done = f.findViewById(R.id.not_done_image);
                                        ImageView upload = f.findViewById(R.id.upload_image);
                                        close.setVisibility(View.GONE);
                                        f1.setVisibility(View.VISIBLE);
                                        progress.setVisibility(View.GONE);
                                        done.setVisibility(View.GONE);
                                        not_done.setVisibility(View.GONE);
                                        upload.setVisibility(View.VISIBLE);

                                    }
                                }
                            }
                        }
                    });
                }

            }
        });
    }

    int p;

    private void uploadNotUploadedImage(Uri uri, FrameLayout f, int imageNumber) {
        ProgressBar progress = f.findViewById(R.id.downloadingImage);
        ImageView done = f.findViewById(R.id.done_image);
        ImageView not_done = f.findViewById(R.id.not_done_image);
        ImageView upload = f.findViewById(R.id.upload_image);
        upload.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        for (int i = 0; i < gridImagesArray.size(); i++) {
            if (gridImagesArray.get(i) == uri) {
                p = i;
            }
        }
        String msg = audioRecordView.getMessageView().getText().toString().trim();
        audioRecordView.getMessageView().setText("");
        CommentListChildItem c = new CommentListChildItem(dbc.allComments().size(), dbu.getCurrentUser().getUserId() + "/" + String.valueOf(1000000 + dbu.getCurrentUser().getNumComments()), Lesson.getType(), Lesson.getStudy(), Lesson.getSubject(), Lesson.getLesson(), dbu.getCurrentUser().isTeacher(), dbu.getCurrentUser().getUser(), dbu.getCurrentUser().getName(), msg, getTime(), getDate(), null, null, null, null, null, null, null, null, null, null, 0, "", idReply, "images");
        String messageKey = GroupNameRef.push().getKey();
        HashMap<String, Object> groupMessageKey = new HashMap<>();
        GroupNameRef.updateChildren(groupMessageKey);
        GroupMessageKeyRef = GroupNameRef.child(messageKey);
        messageInfoMap = new HashMap<>();
        messageInfoMap.put("column", String.valueOf(dbu.getCurrentUser().getNumComments() + 1));
        messageInfoMap.put("recordData", "");
        messageInfoMap.put("recordTime", "0");
        messageInfoMap.put("date", getDate());
        messageInfoMap.put("time", getTime());
        messageInfoMap.put("type", c.getType());
        messageInfoMap.put("message", msg);
        messageInfoMap.put("name", dbu.getCurrentUser().getName());
        messageInfoMap.put("isTeacher", c.isTeacher);
        messageInfoMap.put("user", c.getUser());
        messageInfoMap.put("id", c.getId());
        //    if (layoutReply.getVisibility() == View.VISIBLE)
        messageInfoMap.put("reply", idReply);
        for (int i = 1; i < 10; i++) {
            messageInfoMap.put("image" + String.valueOf(i), "");
        }
        audioRecordView.imageViewSend.setVisibility(GONE);
        audioRecordView.imageViewAudio.setVisibility(GONE);
        audioRecordView.imageViewSend.animate().scaleX(0f).scaleY(0f).setDuration(100).setInterpolator(new LinearInterpolator()).start();
        StorageReference file = userImagesReference.child(dbu.getCurrentUser().getId() + "_Image_" + String.valueOf(imageNumber) + ".jpg");
        file.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    messageInfoMap.put("image1", dbu.getCurrentUser().getId() + "_Image_" + String.valueOf(imageNumber));
                    progress.setVisibility(View.GONE);
                    done.setVisibility(View.VISIBLE);
                    not_done.setVisibility(View.GONE);
                    upload.setVisibility(GONE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            commentNewImages.removeViewAt(p);
                            gridImagesArray.remove(p);
                            boolean a = gridImagesNumberArray.remove((Integer) imageNumber);
                        }
                    }, 1000);
                    if (gridImagesArray.size() == 0) {
                        audioRecordView.isImages = false;
                        audioRecordView.showSend(false);
                    } else {
                        audioRecordView.isImages = true;
                        audioRecordView.imageViewAudio.setVisibility(View.VISIBLE);
                        audioRecordView.showSend(true);
                    }
                    GroupMessageKeyRef.updateChildren(messageInfoMap);
                    UserListChildItem u = dbu.getCurrentUser();
                    u.setNumComments(u.getNumComments() + 1);
                    dbu.updateUser(u);
                    pref.edit().putBoolean("isCommentChange", false).commit();
                    ref.child("Users").child(userId).child("numcomments").setValue(String.valueOf(dbu.getCurrentUser().getNumComments()));
                    scrollToBottom();
                    layoutReply.setVisibility(View.GONE);
                    idReply = "";
                    pref.edit().putBoolean("isCommentChange", true).commit();

                } else {
                    progress.setVisibility(View.GONE);
                    done.setVisibility(View.GONE);
                    not_done.setVisibility(View.GONE);
                    upload.setVisibility(View.VISIBLE);
                    upload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            uploadNotUploadedImage(gridImagesArray.get(p), f, imageNumber);
                        }
                    });
                    Toast.makeText(context, "لم يتم تحميل الصورة" + String.valueOf(p + 1), Toast.LENGTH_LONG).show();
                    String message = task.getException().toString();
                    Toast.makeText(context, "Error : " + message, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public static Bitmap getBitmapFormUri(Activity ac, Uri uri) throws FileNotFoundException, IOException {
        InputStream input = ac.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        //Image resolution is based on 480x800
        float hh = 800f;//The height is set as 800f here
        float ww = 480f;//Set the width here to 480f
        //Zoom ratio. Because it is a fixed scale, only one data of height or width is used for calculation
        int be = 1;//be=1 means no scaling
        if (originalWidth > originalHeight && originalWidth > ww) {//If the width is large, scale according to the fixed size of the width
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//If the height is high, scale according to the fixed size of the width
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //Proportional compression
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//Set scaling
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return bitmap;//Mass compression again
    }

    private void saveImage(final Uri uri, String fileName) {
        Bitmap bitmap = null;
        try {
            bitmap = getBitmapFormUri(getActivity(), uri);
        } catch (final IOException e) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/ElectronicInstitute/media/Images");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        File file = new File(myDir, fileName);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (final Exception e) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        }

    }

    public void backup(String inFileName, String outFileName) throws IOException {
        File dbFile = new File(inFileName);
        FileInputStream fis = new FileInputStream(dbFile);
        // Open the empty db as the output stream
        OutputStream output = new FileOutputStream(outFileName);
        // Transfer bytes from the input file to the output file
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }
        // Close the streams
        output.flush();
        output.close();
        fis.close();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //  ((UserActivity)getActivity()).fragmentCommunicator = this;

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
        try {
            if (controller.isShowing())
                controller.toggleControllerView();
        } catch (Exception e) {

        }
    }

    @Override
    public boolean isConnectAnimationStarted() {
        return isConnectAnimationStarted;
    }

    @Override
    public void setButtonDownloadV(LessonListChildItem lesson, int v) {
        try {
            if (Lesson.getType() == lesson.getType() && Lesson.getStudy() == lesson.getStudy() && Lesson.getSubject() == lesson.getSubject() && Lesson.getLesson() == lesson.getLesson())
                btnDownload.setVisibility(v);
        } catch (Exception e) {

        }
    }

    @Override
    public void setButtonStopV(LessonListChildItem lesson, int v) {
        try {
            if (Lesson.getType() == lesson.getType() && Lesson.getStudy() == lesson.getStudy() && Lesson.getSubject() == lesson.getSubject() && Lesson.getLesson() == lesson.getLesson())
                btnCancel.setVisibility(v);
        } catch (Exception e) {

        }
    }

    @Override
    public void setButtonPauseV(LessonListChildItem lesson, int v) {
        try {
            if (Lesson.getType() == lesson.getType() && Lesson.getStudy() == lesson.getStudy() && Lesson.getSubject() == lesson.getSubject() && Lesson.getLesson() == lesson.getLesson())
                btnPause.setVisibility(v);
        } catch (Exception e) {

        }
    }

    @Override
    public void setButtonWatchV(LessonListChildItem lesson, int v) {
        try {
            if (Lesson.getType() == lesson.getType() && Lesson.getStudy() == lesson.getStudy() && Lesson.getSubject() == lesson.getSubject() && Lesson.getLesson() == lesson.getLesson())
                btnWatch.setVisibility(v);
        } catch (Exception e) {

        }
    }

    @Override
    public void setTextDownload(LessonListChildItem lesson, String text) {
        try {
            if (Lesson.getType() == lesson.getType() && Lesson.getStudy() == lesson.getStudy() && Lesson.getSubject() == lesson.getSubject() && Lesson.getLesson() == lesson.getLesson())
                txtDownload.setText(text);
        } catch (Exception e) {

        }
    }

    @Override
    public void setProgress(LessonListChildItem lesson, int p) {
        try {
            if (Lesson.getType() == lesson.getType() && Lesson.getStudy() == lesson.getStudy() && Lesson.getSubject() == lesson.getSubject() && Lesson.getLesson() == lesson.getLesson())
                downloadProgress.setProgress(p);
        } catch (Exception e) {

        }
    }

    @Override
    public void setIsResume(LessonListChildItem lesson, boolean is) {
        if (Lesson.getType() == lesson.getType() && Lesson.getStudy() == lesson.getStudy() && Lesson.getSubject() == lesson.getSubject() && Lesson.getLesson() == lesson.getLesson())
            isResume = is;
    }

    @Override
    public void setIsDownloading(LessonListChildItem lesson, boolean is) {
        if (Lesson.getType() == lesson.getType() && Lesson.getStudy() == lesson.getStudy() && Lesson.getSubject() == lesson.getSubject() && Lesson.getLesson() == lesson.getLesson())
            isDownloading = is;
    }

    @SuppressLint("StaticFieldLeak")
    private class SaveImagesInStorage extends AsyncTask<Void, Void, String> {
        int urisLength;
        Uri[] uris;
        int[] urisNumber;

        SaveImagesInStorage(Uri... uriss) {
            urisLength = uriss.length;
            uris = uriss;
            urisNumber = new int[urisLength];
        }

        @Override
        protected String doInBackground(Void... params) {
            String s = "";
            for (int i = 0; i < urisLength; i++) {
                File f = new File(uris[i].getPath());
                int num = pref.getInt("imagesNumber", 1000);
                saveImage(uris[i], dbu.getCurrentUser().getId() + "_Image_" + String.valueOf(num) + ".jpg");
                urisNumber[i] = num;
                pref.edit().putInt("imagesNumber", num + 1).commit();
                //try {
                // backup(f.getPath(), Environment.getExternalStorageDirectory() + File.separator + "ElectronicInstituite" + File.separator + "media" + File.separator + "Images" + File.separator + dbu.getCurrentUser().getUser() + String.valueOf(pref.getInt("imagesNumber", 1000)) + ".jpg");

                //} catch (IOException e) {
                //   s = e.getMessage();ima
                //}
            }
            return s;
        }

        @Override
        protected void onPostExecute(final String token) {
            loadingImages.setVisibility(View.GONE);
            for (int i = 0; i < urisLength; i++) {
                final int p = i;
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                FrameLayout f = (FrameLayout) inflater.inflate(R.layout.row_grid1, parent, false);
                ImageView image = f.findViewById(R.id.item_image);
                ImageView close = f.findViewById(R.id.item_image_close);
                Bitmap b = null;
                try {
                    b = ImageRounded.getRoundedCornerBitmap(getResizedBitmap(Bitmap.createScaledBitmap(getBitmapFormUri(getActivity(), uris[i]), 200, 200, true), 250, 250), 10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                image.setImageBitmap(b);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        commentNewImages.removeViewAt(p);
                        gridImagesArray.remove(p);
                        gridImagesNumberArray.remove(p);
                        if (gridImagesArray.size() == 0) {
                            audioRecordView.isImages = false;
                            audioRecordView.showSend(false);
                        }
                    }
                });
                commentNewImages.addView(f, i);
                gridImagesArray.add(uris[i]);
                gridImagesNumberArray.add(urisNumber[i]);

            }
            audioRecordView.showSend(true);
            audioRecordView.isImages = true;
        }

    }

    public Bitmap reduceResolution(String filePath, int viewWidth, int viewHeight) {
        int reqHeight = viewHeight;
        int reqWidth = viewWidth;

        BitmapFactory.Options options = new BitmapFactory.Options();

        // First decode with inJustDecodeBounds=true to check dimensions
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        double viewAspectRatio = 1.0 * viewWidth / viewHeight;
        double bitmapAspectRatio = 1.0 * options.outWidth / options.outHeight;

        if (bitmapAspectRatio > viewAspectRatio)
            reqHeight = (int) (viewWidth / bitmapAspectRatio);
        else
            reqWidth = (int) (viewHeight * bitmapAspectRatio);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        System.gc();                                        // TODO - remove explicit gc calls
        return BitmapFactory.decodeFile(filePath, options);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return resizedBitmap;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 32) {
                if (data.getClipData() != null) {

                    ClipData mClipData = data.getClipData();
                    loadingImages.setVisibility(View.VISIBLE);
                    Uri[] uris = new Uri[mClipData.getItemCount()];
                    for (int i = 0; i < mClipData.getItemCount(); i++) {
                        ClipData.Item item = mClipData.getItemAt(i);
                        uris[i] = item.getUri();
                    }
                    new SaveImagesInStorage(uris).execute();

                } else {
                    Uri uri = data.getData();
                    // display your image
                    Toast.makeText(context, "null", Toast.LENGTH_LONG).show();
                }
                ((UserActivity) getActivity()).isStartAnotherActivity = false;
                ((UserActivity) getActivity()).isPicImages = false;
            }
        }
    }

    @Override
    public void onRecordingStarted() {
        if (!audioRecordView.isStarted) {
            //  showToast("started");
            debug("started");
            audioRecordView.getMessageView().requestFocus();
            time = System.currentTimeMillis() / (1000);
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/ElectronicInstitute/media/Voices");
            File mSaveBit = new File(myDir, dbu.getCurrentUser().getId() + "_Voice_" + String.valueOf(pref.getInt("voicesNumber", 1000)) + ".3gp");
            myDir.mkdirs();
            outputFile = mSaveBit.getPath();
            try {
                FileOutputStream fos = new FileOutputStream(outputFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            myAudioRecorder = new MediaRecorder();
            myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            myAudioRecorder.setOutputFile(outputFile);
            try {
                myAudioRecorder.prepare();
                myAudioRecorder.start();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onRecordingLocked() {
        // showToast("locked");
        debug("locked");
    }

    @Override
    public void onRecordingCompleted() {
        //  showToast("completed");
        debug("completed");

        int recordTime = (int) ((System.currentTimeMillis() / (1000)) - time);

        if (recordTime > 1) {

            String msg = "";
            audioRecordView.getMessageView().setText("");
            CommentListChildItem c = new CommentListChildItem(dbc.allComments().size(), dbu.getCurrentUser().getUserId() + "/" + String.valueOf(1000000 + dbu.getCurrentUser().getNumComments()), Lesson.getType(), Lesson.getStudy(), Lesson.getSubject(), Lesson.getLesson(), dbu.getCurrentUser().isTeacher(), dbu.getCurrentUser().getUser(), dbu.getCurrentUser().getName(), msg, getTime(), getDate(), null, null, null, null, null, null, null, null, null, null, 0, "", idReply, "voice");
            String messageKey = GroupNameRef.push().getKey();
            if (TextUtils.isEmpty(msg)) {
            }
            HashMap<String, Object> groupMessageKey = new HashMap<>();
            loadingImages.setVisibility(View.VISIBLE);
            GroupNameRef.updateChildren(groupMessageKey);
            GroupMessageKeyRef = GroupNameRef.child(messageKey);
            messageInfoMap = new HashMap<>();
            messageInfoMap.put("column", String.valueOf(dbu.getCurrentUser().getNumComments() + 1));
            messageInfoMap.put("recordTime", String.valueOf(recordTime));
            messageInfoMap.put("date", getDate());
            messageInfoMap.put("time", getTime());
            messageInfoMap.put("type", c.getType());
            messageInfoMap.put("message", msg);
            messageInfoMap.put("name", dbu.getCurrentUser().getName());
            messageInfoMap.put("isTeacher", c.isTeacher);

            messageInfoMap.put("user", c.getUser());
            messageInfoMap.put("id", c.getId());
            for (int i = 1; i < 10; i++) {
                messageInfoMap.put("image" + String.valueOf(i), "");
            }
            messageInfoMap.put("reply", idReply);

            StorageReference file = userVoicesReference.child(dbu.getCurrentUser().getId() + "_Voice_" + String.valueOf(pref.getInt("voiceNumber", 1000)) + ".3gp");
            File f = new File(outputFile);
            file.putFile(Uri.fromFile(f)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    loadingImages.setVisibility(GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "تم تحميل تسجيل الصوت", Toast.LENGTH_LONG).show();
                        final String downloadurl = task.getResult().getMetadata().getReference().getDownloadUrl().toString();
                        UserListChildItem u = dbu.getCurrentUser();
                        u.setNumComments(u.getNumComments() + 1);
                        dbu.updateUser(u);
                        messageInfoMap.put("recordData", dbu.getCurrentUser().getId() + "_Voice_" + String.valueOf(pref.getInt("voiceNumber", 1000)));
                        pref.edit().putBoolean("isCommentChange", false).commit();
                        ref.child("Users").child(userId).child("numcomments").setValue(String.valueOf(dbu.getCurrentUser().getNumComments()));

                        GroupMessageKeyRef.updateChildren(messageInfoMap);
                        pref.edit().putInt("voiceNumber", pref.getInt("voiceNumber", 1000) + 1).commit();

                        scrollToBottom();
                        pref.edit().putBoolean("isCommentChange", true).commit();
                    } else {
                        Toast.makeText(context, "لم يتم تحميل تسجيل الصوت, يرجى إعادة التسجيل", Toast.LENGTH_LONG).show();
                        String message = task.getException().toString();
                        Toast.makeText(context, "Error : " + message, Toast.LENGTH_LONG).show();

                    }

                }
            });
            myAudioRecorder.stop();
            myAudioRecorder.release();
        }

        myAudioRecorder = null;
    }

    @Override
    public void onRecordingCanceled() {
        // showToast("canceled");
        debug("canceled");
        myAudioRecorder.reset();
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void debug(String log) {
        Log.d("VarunJohn", log);
    }

    @Override
    public void onClick(View view) {
        showDialog();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Created by:\nVarun John\nvarunjohn1990@gmail.com\n\nCheck code on Github :\nhttps://github.com/varunjohn/Audio-Recording-Animation")
                .setCancelable(false)
                .setPositiveButton("Github", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String url = "https://github.com/varunjohn/Audio-Recording-Animation";
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setPackage("com.android.chrome");
                        try {
                            startActivity(i);
                        } catch (ActivityNotFoundException e) {
                            i.setPackage(null);
                            try {
                                startActivity(i);
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onClick(AttachmentOption attachmentOption) {
        switch (attachmentOption.getId()) {

            case AttachmentOption.DOCUMENT_ID:
                showToast("Document Clicked");
                break;
            case AttachmentOption.CAMERA_ID:
                showToast("Camera Clicked");
                break;
            case AttachmentOption.GALLERY_ID:
                showToast("Gallery Clicked");
                break;
            case AttachmentOption.AUDIO_ID:
                showToast("Audio Clicked");
                break;
            case AttachmentOption.LOCATION_ID:
                showToast("Location Clicked");
                break;
            case AttachmentOption.CONTACT_ID:
                showToast("Contact Clicked");
                break;
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
                if (!TextUtils.isEmpty(query)) {
                    if (!seq.matches(query))
                        seqN = 0;
                    FilterSe f = new FilterSe(query);
                    f.execute();
                } else {
                    commentAdapter.searchString = "";
                    commentAdapter.notifyDataSetChanged();
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
                if (Lesson.getDownload() == 3) {
                    videoWatcher.setVisibility(GONE);
                    if (isMediaSet) {
                        if (mMediaPlayer.isPlaying()) {
                            mMediaPlayer.pause();
                            isPaused = true;
                        }
                    }
                } else
                    videoDownloader.setVisibility(GONE);
                btnHide.setVisibility(GONE);
            }

            @Override
            public void onSearchViewClosed() {
                if (Lesson.getDownload() == 3) {
                    videoWatcher.setVisibility(VISIBLE);
                    if (isPaused) {
                        mMediaPlayer.start();
                        isPaused = false;
                    }
                } else
                    videoDownloader.setVisibility(VISIBLE);
                commentAdapter.searchString = "";
                commentAdapter.notifyDataSetChanged();
                btnHide.setVisibility(VISIBLE);
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
            if (commentAdapter.filter(sequenc)) {
                for (int i = seqN; i < commentAdapter.comments.size(); i++) {
                    if (commentAdapter.comments.get(i).getComment().toLowerCase(Locale.getDefault()).contains(sequenc.toString().toLowerCase(Locale.getDefault()))) {
                        smoothScroller.setTargetPosition(i);
                        ((LinearLayoutManager) recyclerViewMessages.getLayoutManager()).startSmoothScroll(smoothScroller);
                        seq = sequenc.toString();
                        seqN = i + 1;
//                           Toast.makeText(context, String.valueOf(i), Toast.LENGTH_LONG).show();
                        break;
                    } else if (!TextUtils.isEmpty(commentAdapter.comments.get(i).getReply())) {
                        CommentListChildItem c = new CommentListChildItem();
                        for (int j = 0; j < commentAdapter.comments.size(); j++) {
                            if (commentAdapter.comments.get(i).getReply().matches(commentAdapter.comments.get(j).getId()))
                                c = commentAdapter.comments.get(j);
                        }
                        if (c.getComment().toLowerCase(Locale.getDefault()).contains(sequenc.toString().toLowerCase(Locale.getDefault()))) {
                            smoothScroller.setTargetPosition(i);
                            ((LinearLayoutManager) recyclerViewMessages.getLayoutManager()).startSmoothScroll(smoothScroller);
                            seq = sequenc.toString();
                            seqN = i + 1;
                            break;
                        }
                    }
                }
            }
            return s;
        }

        @Override
        protected void onPostExecute(final String token) {
            progressSearch.dismiss();
            commentAdapter.notifyDataSetChanged();
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
    public void onDestroy() {
        super.onDestroy();
        if (isConnectAnimationStarted)
            stopRotateAni();

    }

    private void scrollToBottom() {
        // scroll to last item to get the view of last item
        final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerViewMessages.getLayoutManager();
        final RecyclerView.Adapter adapter = recyclerViewMessages.getAdapter();
        final int lastItemPosition = adapter.getItemCount() - 1;

        layoutManager.scrollToPositionWithOffset(lastItemPosition, 0);
        recyclerViewMessages.post(new Runnable() {
            @Override
            public void run() {
                // then scroll to specific offset
                View target = layoutManager.findViewByPosition(lastItemPosition);
                if (target != null) {
                    int offset = recyclerViewMessages.getMeasuredHeight() - target.getMeasuredHeight();
                    layoutManager.scrollToPosition(lastItemPosition);
                }
            }
        });
    }
}