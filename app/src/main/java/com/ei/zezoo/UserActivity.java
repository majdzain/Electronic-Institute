package com.ei.zezoo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.VpnService;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.Handler;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.downloader.Progress;
import com.downloader.request.DownloadRequest;
import com.downloader.request.DownloadRequestBuilder;
import com.ei.zezoo.model.Server;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.ExpandableBadgeDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import cn.hiroz.uninstallfeedback.FeedbackUtils;
import de.blinkt.openvpn.OpenVpnApi;
import de.blinkt.openvpn.core.OpenVPNService;
import de.blinkt.openvpn.core.OpenVPNThread;
import de.blinkt.openvpn.core.VpnStatus;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class UserActivity extends AppCompatActivity {
    final int person = 1, notif = 2, sales = 3, free = 9, ques = 10, cale = 11, sign = 12, excer = 13, cale1 = 14, calc = 15, sign_course = 16, bill = 17, settings = 18, excellent = 19, contact = 20, star = 21, down = 22;
    Resources res;
    Toolbar toolbar;
    private AccountHeader headerResult = null;
    Drawer result = null;
    ActionBarDrawerToggle actionBar;
    Context context = UserActivity.this;
    private static final int ADD_POINT = 100000;
    private static final int EDIT_POINTS = 100001;
    UserSQLDatabaseHandler dbu;
    NotifSQLDatabaseHandler dbn;
    CommentSQLDatabaseHandler dbc;
    private FirebaseUser currentuser;
    private FirebaseAuth mauth;
    private DatabaseReference ref;
    IProfile[] profiles;
    private Server server;
    OpenVPNService vpnService = new OpenVPNService();
    private SharedPreference preference;
    OpenVPNThread vpnThread = new OpenVPNThread();
    UserListChildItem u;
    private String userId;
    private StorageReference userprofileimagereference;
    private Bitmap my_image;
    private MenuItem menuConnected;
    public FragmentCommunicator fragmentCommunicator;
    Handler handStopVpn;
    Runnable runStopVpn;
    boolean isStartAnotherActivity = false;
    private boolean isHandRunning = false;
    private SharedPreferences pref;
    boolean isPicImages = false;
    LessonSQLDatabaseHandler dbl;
    ExpandableBadgeDrawerItem[] groupList;
    boolean isCommentChange = false;
    private ProgressDialog Loading;
    private ValueEventListener userListener, groupListener, curUserList, curGroupList;
    int selection = 1;
    MovableFloatingActionButton downloadBtn;
    NotificationManager mManager;
    DownloadSQLDatabaseHandler dbd;
    ArrayList<Integer> arrayIds;
    private AnimationDrawable animation;
    MaterialSearchView searchView;
    FrameLayout frameLayout;
    Dialog d;
    NumberProgressBar progressBar;
    private void createDownloadProgressDialog() {
        d = new Dialog(this);
        d.setContentView(R.layout.dialog_download_database);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressBar = (NumberProgressBar) d.findViewById(R.id.progress);
        progressBar.setMax(100);
        d.setCanceledOnTouchOutside(false);
        d.setCancelable(false);
        d.show();
    }

    public static int isLoginable(String str) {
        boolean is = true;
        if (str == null) {
            is = false;
        }
        int length = str.length();
        if (length == 0) {
            is = false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                is = false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                is = false;
            }
        }
        if (is) {
            return Integer.valueOf(str);
        } else
            return 2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        forceRTLIfSupported();
        res = getResources();
        mauth = FirebaseAuth.getInstance();
        currentuser = mauth.getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference();
        preference = new SharedPreference(this);
        server = preference.getServer();
        Loading = new ProgressDialog(UserActivity.this);
        downloadBtn = (MovableFloatingActionButton) findViewById(R.id.fab_download);
        animation = (AnimationDrawable) res.getDrawable(android.R.drawable.stat_sys_download);
        downloadBtn.setImageDrawable(animation);
        mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        pref = context.getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        frameLayout = (FrameLayout) findViewById(R.id.toolbar_container);
        handStopVpn = new Handler();
        runStopVpn = new Runnable() {
            public void run() {
                stopVpn();
                isHandRunning = true;
            }
        };
        userId = currentuser.getUid();
        arrayIds = new ArrayList<>();
        dbu = new UserSQLDatabaseHandler(context);
        u = dbu.getCurrentUser();
        dbn = new NotifSQLDatabaseHandler(context);
        dbc = new CommentSQLDatabaseHandler(context);
        dbl = new LessonSQLDatabaseHandler(context);
        dbd = new DownloadSQLDatabaseHandler(context);
        dbd.allDownloads();
        dbc.allComments();
        dbn.allNotifs();
        if (u.isBanned()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (u.isBanned() && !vpnService.getStatus().matches("CONNECTED"))
                        showBanDialog();
                }
            }, 9000);
        }
        // dbu.allUsers();
        //Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.aaaa);
        // dbc.addComment(new CommentListChildItem(0,1653,true,"", "أ.رامي تكريتي","هذا درسنا لليوم نتمنى لكم التوفيق...",getTime(),getDate(),null,null,null,null,null,null,null,null,null,null,0,""));
        //dbc.addComment(new CommentListChildItem(1,1653,false, user, "سعيد حكم","في المتتالية الهندسية هل يمكن أن نستعمل قانون Sn",getTime(),getDate(),null,null,null,null,null,null,null,null,null,null,0,""));
        // dbc.addComment(new CommentListChildItem(2,1653,true, user, "أ.رامي تكريتي","نعم القانون المخصص لها",getTime(),getDate(),null,null,null,null,null,null,null,null,null,null,0,""));
        //   ArrayList<String> studies = new ArrayList<>();
        // dbu.addUser(new UserListChildItem(0, "", "", 0, "", "", "", "", "", "", "", "", "", "", getTime(), getDate(), "", "", studies, BitmapFactory.decodeResource(res, R.drawable.profile), false));
        // dbn.addNotif(new NotifListChildItem(0, 1000010, 7, 7, "أهلاً وسهلاً بكم في معهدنا الالكتروني", 0, false, getTime(), getDate()));
        // dbn.addNotif(new NotifListChildItem(1, 1000009, 5, 5, "صدور جدول توزيع المواد الالكترونية اليومي للمواد", 0, true, getTime(), getDate()));
        //  dbn.addNotif(new NotifListChildItem(2, 1000000, 0, 1650, "الدرس الأول: مقاومة الهواء", 3, false, getTime(), getDate()));
        //  dbn.addNotif(new NotifListChildItem(3, 1000001, 0, 1650, "الدرس الثاني: النواس الثقلي", 2, false, getTime(), getDate()));
        //   dbn.addNotif(new NotifListChildItem(4, 1000002, 0, 1650, "الدرس الثالث: النواس الثقلي المركب", 1, true, getTime(), getDate()));
        //  dbn.addNotif(new NotifListChildItem(5, 1000003, 0, 1653, "الدرس الأول: المتتاليات", 2, false, getTime(), getDate()));
        //  dbn.addNotif(new NotifListChildItem(6, 1000004, 0, 1653, "الدرس الثاني: نهاية متتالية", 1, true, getTime(), getDate()));
        ///   dbn.addNotif(new NotifListChildItem(7, 1000005, 0, 1651, "الدرس الأول: الكيمياء النووية", 2, false, getTime(), getDate()));
        //  dbn.addNotif(new NotifListChildItem(8, 1000006, 0, 1651, "الدرس الثاني: الكيمياء الحرارية", 1, true, getTime(), getDate()));
        //  dbn.addNotif(new NotifListChildItem(9, 1000007, 0, 1654, "الدرس الأول: مراجعة عامة", 3, false, getTime(), getDate()));
        //  dbn.addNotif(new NotifListChildItem(10, 1000008, 0, 1654, "الدرس الثاني: Present Perfect", 1, true, getTime(), getDate()));
        toolbar = (Toolbar) findViewById(R.id.mainToolbar);
        toolbar.setTitle("Bee Academy");

        toolbar.setTitleTextColor(res.getColor(R.color.white));
        toolbar.setBackgroundColor(res.getColor(R.color.brown));
        changeToolbarFont(toolbar, this);
        setSupportActionBar(toolbar);
        int ide = 900, x = 1;
        profiles = new IProfile[dbu.allUsers().size() + 2];
        for (int i = 0; i < dbu.allUsers().size(); i++) {
            UserListChildItem user1 = dbu.allUsers().get(i);
            boolean b = user1.getUserId().matches(dbu.getCurrentUser().getUserId());
//Toast.makeText(this,String.valueOf(i) + (b?" true":" false"),Toast.LENGTH_LONG).show();
            profiles[b ? 0 : x] = new ProfileDrawerItem().withSetSelected(b).withName(user1.getName() + "(" + ElecUtils.getStudy(user1.getType(), user1.getStudy()) + ")").withEmail(user1.getUser()).withIcon(user1.getProfile()).withIdentifier(ide).withSelectedTextColorRes(R.color.spec_black).withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf")).withTag(user1).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                @Override
                public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                    result.closeDrawer();
                    u = (UserListChildItem) drawerItem.getTag();
                    if (!u.isCurrent()) {
                        final Dialog d1 = new Dialog(context);
                        d1.setContentView(R.layout.dialog_profile_change);
                        d1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        TextView txtProfile = d1.findViewById(R.id.txtPrfile);
                        Button btnYes = d1.findViewById(R.id.btnYes);
                        Button btnNo = d1.findViewById(R.id.btnNo);
                        txtProfile.setText(txtProfile.getText() + user1.getUser() + "/" + user1.getName());
                        btnYes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                d1.cancel();
                                createDownloadProgressDialog();
                                mauth.signInWithEmailAndPassword(u.getUser(), u.getPassword())
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    mauth = FirebaseAuth.getInstance();
                                                    currentuser = mauth.getCurrentUser();
                                                    ref = FirebaseDatabase.getInstance().getReference();
                                                    ref.child("Users").child(dbu.getCurrentUser().getUserId()).child("isNow").setValue("0");
                                                    ref.child("Users").child(dbu.getCurrentUser().getUserId()).child("status").setValue(getDate() + "|" + getTime());
                                                    if (currentuser != null) {
                                                        userId = currentuser.getUid();
                                                        userprofileimagereference = FirebaseStorage.getInstance().getReference().child("Profile Images");
                                                        progressBar.setProgress(20);
                                                        ref.child("Users").child(userId).addListenerForSingleValueEvent(userListener);
                                                    }

                                                } else {

                                                    String message = task.getException().toString();
                                                    if (message.contains("!DOCTYPE") || message.contains("Timedout"))
                                                        Toast.makeText(UserActivity.this, "تحقق من الاتصال", Toast.LENGTH_LONG).show();
                                                    else if (message.contains("password is invalid"))
                                                        Toast.makeText(UserActivity.this, "اسم المستخدم وكلمة السر غير متطابقان", Toast.LENGTH_LONG).show();
                                                    else if (message.contains("no user record"))
                                                        Toast.makeText(UserActivity.this, "لا يوجد اسم مستخدم مطابق", Toast.LENGTH_LONG).show();
                                                    else if (message.contains("blocked all requests"))
                                                        Toast.makeText(UserActivity.this, "تم الحظر بسبب محاولات تسجيل الدخول الخاطئة", Toast.LENGTH_LONG).show();
                                                    else
                                                        Toast.makeText(UserActivity.this, "خطأ " + message, Toast.LENGTH_LONG).show();
                                                    d.cancel();
                                                }
                                            }
                                        });
                            }
                        });
                        btnNo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                d1.cancel();
                            }
                        });
                        d1.setCanceledOnTouchOutside(true);
                        d1.show();
                    }
                    return false;
                }
            });
            if (!b)
                x++;
            ide++;
        }
        profiles[dbu.allUsers().size()] = new ProfileSettingDrawerItem().withName("إضافة حساب").withIcon(R.drawable.add_account).withIdentifier(ADD_POINT).withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf"));
        profiles[dbu.allUsers().size() + 1] = new ProfileSettingDrawerItem().withName("تسجيل الخروج").withIcon(R.drawable.logout).withIdentifier(EDIT_POINTS).withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf"));


        UserListChildItem user = dbu.getCurrentUser();
//Toast.makeText(this,currentuser.getUid(),Toast.LENGTH_LONG).show();
//Toast.makeText(this,dbu.getUser(0).getUserId(),Toast.LENGTH_LONG).show();
        // Toast.makeText(this,String.valueOf(dbu.allUsers().size()),Toast.LENGTH_LONG).show();

        // Create the AccountHeader
        // Tie DrawerLayout events to the ActionBarToggle
        headerResult = new AccountHeaderBuilder()
                .withActivity(this).withHeaderBackground(R.drawable.dr_background_4)
                .withTranslucentStatusBar(true).withTextColorRes(R.color.white).withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf"))
                .addProfiles(profiles)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        //sample usage of the onProfileChanged listener
                        //if the clicked item has the identifier 1 add a new profile ;)
                        if (profile instanceof IDrawerItem && profile.getIdentifier() == ADD_POINT) {
                            /** int count = 100 + headerResult.getProfiles().size() + 1;
                             IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName("Batman" + count).withEmail("batman" + count + "@gmail.com").withIcon(R.drawable.profile).withIdentifier(count);
                             if (headerResult.getProfiles() != null) {
                             //we know that there are 2 setting elements. set the new profile above them ;)
                             headerResult.addProfile(newProfile, headerResult.getProfiles().size() - 2);
                             } else {
                             headerResult.addProfiles(newProfile);
                             }**/
                            isStartAnotherActivity = true;
                            Intent ii = new Intent(UserActivity.this, LoginActivity.class);
                            ii.putExtra("userId",userId);
                            ii.putExtra("isNewLogin", true);
                            startActivity(ii);
                        } else if (profile instanceof IDrawerItem && profile.getIdentifier() == EDIT_POINTS) {
                            ref.child("Users").child(userId).child("isNow").setValue("0");
                            ref.child("Users").child(userId).child("status").setValue(getDate() + "|" + getTime());
                            logOut();
                        }

                        //false if you have not consumed the event and it should close the drawer
                        return false;

                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

//create the drawer and remember the `Drawer` result object
        result = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("الملف الشخصي").withIcon(R.drawable.person).withIdentifier(person).withSelectedTextColorRes(R.color.brown).withSelectedIconColorRes(R.color.orangeS).withIconColorRes(R.color.orangeS).withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf")),
                        new PrimaryDrawerItem().withName("الإشعارات").withIcon(R.drawable.notifications).withIdentifier(notif).withSelectedTextColorRes(R.color.brown).withSelectedIconColorRes(R.color.orangeS).withIconColorRes(R.color.orangeS).withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf")),
                        new PrimaryDrawerItem().withName("التحميلات").withIcon(R.drawable.downloads).withIdentifier(down).withSelectedTextColorRes(R.color.brown).withSelectedIconColorRes(R.color.orangeS).withIconColorRes(R.color.orangeS).withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf")),
                        new SectionDrawerItem().withName("الدروس الإلكترونية").withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf")))
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {

                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        //creating fragment object
                        Fragment fragment = null;
                        //initializing the fragment object which is selected
                        switch ((int) drawerItem.getIdentifier()) {
                            case person:
                                fragment = new PersonFragment();
                                //  menuConnected = ((PersonFragment) fragment).menuConnected;
                                try {
                                    fragmentCommunicator.stopConnectAnimation();
                                } catch (Exception e) {

                                }
                                fragmentCommunicator = (PersonFragment) fragment;
                                toolbar.setTitle("الملف الشخصي");
                                break;
                            case notif:
                                fragment = new NotifFragment();
                                fragmentCommunicator = (NotifFragment) fragment;
                                toolbar.setTitle("الإشعارات");
                                break;
                            case down:
                                fragment = new DownloadFragment();
                                fragmentCommunicator = (DownloadFragment) fragment;
                                toolbar.setTitle("التحميلات");
                                break;
                            case calc:
                                fragment = new CalcFragment();
                                toolbar.setTitle("الآلة الحاسبة");
                                break;
                            case ques:
                                fragment = new QuesFragment();
                                fragmentCommunicator = (QuesFragment) fragment;
                                toolbar.setTitle("الأسئلة العامة");
                                break;
                            case cale:
                                /*fragment = new ControlActivity();
                                toolbar.setBackgroundColor(res.getColor(R.color.orange));
                                toolbar.setTitle("Accountant");*/
                                break;
                            case sign:
                                /*fragment = new ControlActivity();
                                toolbar.setBackgroundColor(res.getColor(R.color.orange));
                                toolbar.setTitle("Accountant");*/
                                break;
                            case excer:
                                /*fragment = new ControlActivity();
                                toolbar.setBackgroundColor(res.getColor(R.color.orange));
                                toolbar.setTitle("Accountant");*/
                                break;
                            case cale1:
                                /*fragment = new ControlActivity();
                                toolbar.setBackgroundColor(res.getColor(R.color.orange));
                                toolbar.setTitle("Accountant");*/
                                break;
                            case sign_course:
                                /*fragment = new ControlActivity();
                                toolbar.setBackgroundColor(res.getColor(R.color.orange));
                                toolbar.setTitle("Accountant");*/
                                break;
                            case bill:
                                /*fragment = new ControlActivity();
                                toolbar.setBackgroundColor(res.getColor(R.color.orange));
                                toolbar.setTitle("Accountant");*/
                                break;
                            case settings:
                                /*fragment = new ControlActivity();
                                toolbar.setBackgroundColor(res.getColor(R.color.orange));
                                toolbar.setTitle("Accountant");*/
                                break;
                            case excellent:
                                /*fragment = new ControlActivity();
                                toolbar.setBackgroundColor(res.getColor(R.color.orange));
                                toolbar.setTitle("Accountant");*/
                                break;
                            case contact:
                                /*fragment = new ControlActivity();
                                toolbar.setBackgroundColor(res.getColor(R.color.orange));
                                toolbar.setTitle("Accountant");*/
                                break;
                            case star:
                                /*fragment = new ControlActivity();
                                toolbar.setBackgroundColor(res.getColor(R.color.orange));
                                toolbar.setTitle("Accountant");*/
                                break;
                        }
                        //replacing the fragment
                        if (fragment != null) {

                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.fragmentMain, fragment);
                            ft.commit();
                            result.closeDrawer();
                        }
                        return true;
                    }
                }).build();
        //    Toast.makeText(this,"Length" + String.valueOf(dbl.allLessons().size()),Toast.LENGTH_LONG).show();
        ArrayList<ExpandableBadgeDrawerItem> group = new ArrayList<>();
        // int ex =0;
        // try {
        //     ex = getIntent().getExtras().getInt("id", 0);
        //  }catch(Exception e){

        //  }

        for (int i = 0; i < dbl.allSubjects(dbu.getCurrentUser().getType(), dbu.getCurrentUser().getStudy()).size(); i++) {
            String subject = dbl.allSubjects(dbu.getCurrentUser().getType(), dbu.getCurrentUser().getStudy()).get(i);
            boolean isExist = false;
            for (int m = 0; m < dbu.getCurrentUser().getStudies().size(); m++) {
                if (dbu.getCurrentUser().getStudies().get(m) == ElecUtils.getSubject(user.getType(), user.getStudy(), subject))
                    isExist = true;
            }
            if (isExist) {
                final int g = i;

                ExpandableBadgeDrawerItem exp = new ExpandableBadgeDrawerItem().withName(subject).withSelectedTextColorRes(R.color.brown).withIcon(ElecUtils.getResourceFromSubject(user.getType(), user.getStudy(), ElecUtils.getSubject(user.getType(), user.getStudy(), subject))).withTag(subject).withSelectable(false).withIconColorRes(R.color.orangeS).withIdentifier(i + 3).withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.blue)).withBadge(String.valueOf(dbl.allLessons(user.getType(), user.getStudy(), subject).size())).withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf"));
                SecondaryDrawerItem[] list = new SecondaryDrawerItem[dbl.allLessons(user.getType(), user.getStudy(), subject).size()];
                for (int j = 0; j < dbl.allLessons(user.getType(), user.getStudy(), subject).size(); j++) {
                    final int h = j;
                    LessonListChildItem l = dbl.allLessons(user.getType(), user.getStudy(), subject).get(j);
                    //  if(ex != 0 && ex == l.getDownloadId())
                    //    selection = Integer.valueOf(String.valueOf(l.getSubject()) + String.valueOf(l.getLesson()));
                    SecondaryDrawerItem s = new SecondaryDrawerItem().withTag((LessonListChildItem) l).withName("الدرس " + String.valueOf(l.getLesson() - 100 + 1) + " : " + l.getLessonN()).withLevel(2).withIdentifier(Long.valueOf(String.valueOf(l.getSubject() + String.valueOf(l.getLesson())))).withSelectedTextColorRes(R.color.brown).withSelectedIconColorRes(R.color.blue).withIconColorRes(R.color.blue).withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf")).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                            Fragment fragment = null;
                            LessonListChildItem lesson = dbl.getLesson(((LessonListChildItem) drawerItem.getTag()).getColumn());
                            String str = String.valueOf(drawerItem.getIdentifier());
                            String sub = str.substring(0, 3);
                            String les = str.substring(3, 6);
                            //  Toast.makeText(UserActivity.this,sub,Toast.LENGTH_LONG).show();
                            //  Toast.makeText(UserActivity.this,les,Toast.LENGTH_LONG).show();
                            fragment = new LessonFragment(lesson, (SecondaryDrawerItem) drawerItem);
                            try {
                                fragmentCommunicator.stopConnectAnimation();
                            } catch (Exception e) {

                            }
                            fragmentCommunicator = (LessonFragment) fragment;
                            //  menuConnected = ((LessonFragment) fragment).menuConnected;
                            toolbar.setTitle("الدرس " + String.valueOf(lesson.getLesson() - 100 + 1) + " : " + lesson.getLessonN());
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.fragmentMain, fragment);
                            ft.commit();
                            result.closeDrawer();
                            return true;
                        }
                    });
                    s.withTag(l);
                    if (l.isNew())
                        s.withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.orangeS)).withBadge("ج");
                    if (l.getDownload() == 1)
                        s.withIcon(R.drawable.download);
                    else if (l.getDownload() == 2)
                        s.withIcon(R.drawable.downloaded);
                    else if (l.getDownload() == 3)
                        s.withIcon(R.drawable.seen);
                    list[j] = s;
                }
                exp.withSubItems(list);
                group.add(exp);
            }
        }
        groupList = new ExpandableBadgeDrawerItem[group.size()];
        for (int i = 0; i < group.size(); i++) {
            groupList[i] = group.get(i);
        }
        result.addItems(groupList);
        result.addItems(
                new PrimaryDrawerItem().withName("الأسئلة العامة").withIcon(R.drawable.questions).withIdentifier(ques).withSelectedTextColorRes(R.color.brown).withSelectedIconColorRes(R.color.orangeS).withIconColorRes(R.color.orangeS).withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf")),
                new PrimaryDrawerItem().withName("جدول الدروس").withIcon(R.drawable.caleander).withIdentifier(cale).withSelectedTextColorRes(R.color.brown).withSelectedIconColorRes(R.color.orangeS).withIconColorRes(R.color.orangeS).withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf")),
                new SectionDrawerItem().withName("الاختبارات").withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf")),
                new PrimaryDrawerItem().withName("التسجيل في الاختبارات").withIcon(R.drawable.sign_tasks).withIdentifier(sign).withSelectedTextColorRes(R.color.brown).withSelectedIconColorRes(R.color.orangeS).withIconColorRes(R.color.orangeS).withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf")),
                new ExpandableBadgeDrawerItem().withName("التدريبات الإلكترونية").withSelectedTextColorRes(R.color.brown).withIcon(FontAwesome.Icon.faw_tasks).withSelectable(false).withIconColorRes(R.color.orangeS).withIdentifier(excer).withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.blue)).withBadge("0").withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf")),
                new PrimaryDrawerItem().withName("جدول الإختبارات النظرية").withIcon(FontAwesome.Icon.faw_calendar_check2).withIdentifier(cale1).withSelectedTextColorRes(R.color.brown).withSelectedIconColorRes(R.color.orangeS).withIconColorRes(R.color.orangeS).withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf")),
                new SectionDrawerItem().withName("الأدوات والدعم").withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf")),
                new PrimaryDrawerItem().withName("الآلة حاسبة").withIcon(R.drawable.calc).withIdentifier(calc).withSelectedTextColorRes(R.color.brown).withSelectedIconColorRes(R.color.orangeS).withIconColorRes(R.color.orangeS).withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf")),
                new PrimaryDrawerItem().withName("التسجيل في الدورات").withIcon(R.drawable.sign_course).withIdentifier(sign_course).withSelectedTextColorRes(R.color.brown).withSelectedIconColorRes(R.color.orangeS).withIconColorRes(R.color.orangeS).withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf")),
                new PrimaryDrawerItem().withName("كشف الحساب").withIcon(R.drawable.cashes).withIdentifier(bill).withSelectedTextColorRes(R.color.brown).withSelectedIconColorRes(R.color.orangeS).withIconColorRes(R.color.orangeS).withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf")),
                new PrimaryDrawerItem().withName("الإعدادات").withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(settings).withSelectedTextColorRes(R.color.brown).withSelectedIconColorRes(R.color.orangeS).withIconColorRes(R.color.orangeS).withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf")),
                new PrimaryDrawerItem().withName("المتفوقين في المعهد").withIcon(R.drawable.excellent).withIdentifier(excellent).withSelectedTextColorRes(R.color.brown).withSelectedIconColorRes(R.color.orangeS).withIconColorRes(R.color.orangeS).withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf")),
                new PrimaryDrawerItem().withName("اتصل بنا").withIcon(GoogleMaterial.Icon.gmd_copyright).withIdentifier(contact).withSelectedTextColorRes(R.color.brown).withSelectedIconColorRes(R.color.orangeS).withIconColorRes(R.color.orangeS).withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf")),
                new PrimaryDrawerItem().withName("تقييم المعهد").withIcon(GoogleMaterial.Icon.gmd_star).withIdentifier(star).withSelectedTextColorRes(R.color.brown).withSelectedIconColorRes(R.color.orangeS).withIconColorRes(R.color.orangeS).withTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_1.ttf")));
        result.setSelection(1);
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (result.getCurrentSelection() != down)
                    result.setSelection(down);
            }
        });
        actionBar = result.getActionBarDrawerToggle();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        actionBar.setDrawerIndicatorEnabled(true);
        actionBar.setDrawerSlideAnimationEnabled(true);
        for (int i = 0; i < dbd.allDownloads().size(); i++) {
            if (dbd.allDownloads().get(i).getDownload() == DownloadListChildItem.PAUSED || dbd.allDownloads().get(i).getDownload() == DownloadListChildItem.RUNNING || dbd.allDownloads().get(i).getDownload() == DownloadListChildItem.STARTED) {
                downloadBtn.setVisibility(VISIBLE);
                break;
            }
        }
        //  Toast.makeText(this,ElecUtils.getType(dbu.getCurrentUser().getType()),Toast.LENGTH_LONG).show();
        // Toast.makeText(this,ElecUtils.getStudy(dbu.getCurrentUser().getStudy()),Toast.LENGTH_LONG).show();
        //    RetrieveGroups(dbu.getCurrentUser());
        userListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (getApplicationContext().getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isCommentChange", true)) {
                    if (dataSnapshot.exists()) {
                        progressBar.setProgress(45);
                        String snow = dataSnapshot.child("isNow").getValue().toString();
                        String sbirthday = dataSnapshot.child("birthday").getValue().toString();
                        String sdate = dataSnapshot.child("date").getValue().toString();
                        String stime = dataSnapshot.child("time").getValue().toString();
                        String semail = dataSnapshot.child("email").getValue().toString();
                        String sfather = dataSnapshot.child("father").getValue().toString();
                        String smother = dataSnapshot.child("mother").getValue().toString();
                        String sfrom = dataSnapshot.child("from").getValue().toString();
                        String sid = dataSnapshot.child("id").getValue().toString();
                        String simage = dataSnapshot.child("image").getValue().toString();
                        String sname = dataSnapshot.child("name").getValue().toString();
                        String snum = dataSnapshot.child("num").getValue().toString();
                        String snum_ = dataSnapshot.child("num_").getValue().toString();
                        String snumcomments = dataSnapshot.child("numcomments").getValue().toString();
                        String splace = dataSnapshot.child("place").getValue().toString();
                        String sstudy = dataSnapshot.child("study").getValue().toString();
                        String suid = dataSnapshot.child("uid").getValue().toString();
                        String sbill = dataSnapshot.child("bill").getValue().toString();
                        String steacher = dataSnapshot.child("isTeacher").getValue().toString();
                        String sstatus = dataSnapshot.child("status").getValue().toString();
                        String stype = dataSnapshot.child("type").getValue().toString();
                        String spass = dataSnapshot.child("pass").getValue().toString();
                        String sban = dataSnapshot.child("isBanned").getValue().toString();
                        ArrayList<Integer> studyList = new ArrayList<>();
                        String studies = dataSnapshot.child("studies").getValue().toString();
                        if (!TextUtils.isEmpty(studies)) {
                            for (int i = 0; i < studies.length(); i += 3) {
                                studyList.add(Integer.valueOf(studies.substring(i, i + 3)));
                            }
                        }
                        u.setStudies(studyList);
                        u.setType(Integer.valueOf(stype));
                        u.setName(sname);
                        u.setFather(sfather);
                        u.setMother(smother);
                        u.setId(sid);
                        u.setFrom(sfrom);
                        u.setPlace(splace);
                        u.setBirthday(sbirthday);
                        u.setPhone(snum);
                        u.setTelephone(snum_);
                        u.setEmail(semail);
                        u.setUser(semail);
                        u.setStudy(Integer.valueOf(sstudy));
                        u.setDate(sdate);
                        u.setTime(stime);
                        u.setBill(sbill);
                        u.setBills(sbill);
                        u.setUserId(suid);
                        u.setCurrent(true);
                        u.setNumComments(Integer.valueOf(snumcomments));
                        u.setPassword(spass);
                        u.setStatus(sstatus);
                        if (steacher.contains("1"))
                            u.setTeacher(true);
                        else
                            u.setTeacher(false);
                        if (sban.contains("1"))
                            u.setBanned(true);
                        else
                            u.setBanned(false);
                        if (snow.matches("0") && !u.isBanned()) {
                            ref.child("Users").child(suid).child("isNow").setValue("1");
                            if (!simage.matches("uri")) {
                                StorageReference refe = FirebaseStorage.getInstance().getReference().child("Profile Images/" + suid + ".jpg");
                                try {
                                    final File localFile = File.createTempFile("Images", "jpg");
                                    refe.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                            progressBar.setProgress(70);
                                           // ref.child("Users").child(userId).removeEventListener(userListener);
                                            my_image = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                            u.setProfile(my_image);
                                            dbu.updateUser(u);
                                            groupref = FirebaseDatabase.getInstance().getReference().child(ElecUtils.getType(u.getType())).child(ElecUtils.getStudy(u.getType(), u.getStudy()));
                                            groupref.addListenerForSingleValueEvent(groupListener);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressBar.setProgress(70);
                                          //  ref.child("Users").child(userId).removeEventListener(userListener);
                                            //  Loading.dismiss();
                                            // Toast.makeText(UserActivity.this, "لم يتم تسجيل الدخول, يرجى إعادة المحاولة!", Toast.LENGTH_LONG).show();
                                            Toast.makeText(UserActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                            u.setProfile(BitmapFactory.decodeResource(getResources(), R.drawable.profile_image));
                                            dbu.updateUser(u);
                                            groupref = FirebaseDatabase.getInstance().getReference().child(ElecUtils.getType(u.getType())).child(ElecUtils.getStudy(u.getType(), u.getStudy()));
                                            groupref.addListenerForSingleValueEvent(groupListener);
                                        }
                                    });
                                } catch (IOException e) {
                                    progressBar.setProgress(70);
                                    //  Loading.dismiss();
                                    // Toast.makeText(UserActivity.this, "لم يتم تسجيل الدخول, يرجى إعادة المحاولة!", Toast.LENGTH_LONG).show();
                                    Toast.makeText(UserActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                    u.setProfile(BitmapFactory.decodeResource(getResources(), R.drawable.profile_image));
                                    dbu.updateUser(u);
                                    groupref = FirebaseDatabase.getInstance().getReference().child(ElecUtils.getType(u.getType())).child(ElecUtils.getStudy(u.getType(), u.getStudy()));
                                    groupref.addListenerForSingleValueEvent(groupListener);
                                }
                            } else {
                                progressBar.setProgress(70);
                                u.setProfile(BitmapFactory.decodeResource(getResources(), R.drawable.profile_image));
                                dbu.updateUser(u);
                                groupref = FirebaseDatabase.getInstance().getReference().child(ElecUtils.getType(u.getType())).child(ElecUtils.getStudy(u.getType(), u.getStudy()));
                                groupref.addListenerForSingleValueEvent(groupListener);
                            }
                        } else {
                            d.cancel();
                           // ref.child("Users").child(userId).removeEventListener(this);
                            if (snow.matches("1"))
                                Toast.makeText(UserActivity.this, "الحساب متصل مسبقاً!", Toast.LENGTH_LONG).show();
                            else if (u.isBanned())
                                Toast.makeText(UserActivity.this, "يرجى التواصل مع المعهد, الحساب محظور!", Toast.LENGTH_LONG).show();
                            logOut();
                        }
                    } else {
                        d.cancel();
//                    name.setVisibility(View.VISIBLE);
                      //  ref.child("Users").child(userId).removeEventListener(this);
                        Toast.makeText(UserActivity.this, "تم حذف حسابك يرجى مراجعة المعهد!", Toast.LENGTH_LONG).show();
                        logOut();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        groupListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //          Toast.makeText(LoginActivity.this,"datachange",Toast.LENGTH_LONG).show();
                // ArrayList<String> set=new HashSet<>();
                Iterator iterator = dataSnapshot.getChildren().iterator();
                //     Toast.makeText(UserActivity.this,iterator.toString(),Toast.LENGTH_LONG).show();
                boolean hasNext = false;
                while (iterator.hasNext()) {

                    hasNext = true;
                    //  set.add(((DataSnapshot)iterator.next()).getKey());
                    final String subject = ((DataSnapshot) iterator.next()).getKey();
                    //     Toast.makeText(LoginActivity.this,subject,Toast.LENGTH_LONG).show();
                    boolean isSigned = false;
                    for (int i = 0; i < u.getStudies().size(); i++) {
                        //  Toast.makeText(LoginActivity.this,String.valueOf(user.getStudies().get(i)),Toast.LENGTH_SHORT).show();
                        if (u.getStudies().get(i) == ElecUtils.getSubject(u.getType(), u.getStudy(), subject))
                            isSigned = true;
                    }
                    if (!iterator.hasNext())
                        isLastSubject = true;
                    //  Toast.makeText(UserActivity.this,subject,Toast.LENGTH_LONG).show();
                    if (isSigned) {
                        final DatabaseReference le = FirebaseDatabase.getInstance().getReference().child(ElecUtils.getType(u.getType())).child(ElecUtils.getStudy(u.getType(), u.getStudy())).child(subject);
                        le.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Iterator iterator1 = snapshot.getChildren().iterator();
                                boolean hasNext1 = false;
                                while (iterator1.hasNext()) {
                                    hasNext1 = true;
                                    int Type = u.getType();
                                    int Study = u.getStudy();
                                    DataSnapshot data = (DataSnapshot) iterator1.next();
                                    String Lesson = data.getKey();
                                    if (!Lesson.matches("الأسئلة")) {
                                        // int Lesson = (int)data.getValue();
                                        LessonListChildItem les = new LessonListChildItem(dbl.allLessons().size(), Type, Study, ElecUtils.getSubject(Type, Study, subject), Integer.valueOf(Lesson.substring(Lesson.indexOf("|") + 1)), 0, subject, Lesson.substring(0, Lesson.indexOf("|")), "", true, 1, getTime(), getDate());
                                        //  Toast.makeText(UserActivity.this, String.valueOf(les.getLesson()), Toast.LENGTH_LONG).show();
                                        //    Toast.makeText(LoginActivity.this, String.valueOf(les.getLessonN()), Toast.LENGTH_LONG).show();
                                        boolean isExist = false;
                                        for (int i = 0; i < dbl.allLessons().size(); i++) {
                                            if (dbl.allLessons().get(i).getLesson() == les.getLesson() && dbl.allLessons().get(i).getType() == les.getType() && dbl.allLessons().get(i).getStudy() == les.getStudy() && dbl.allLessons().get(i).getSubject() == les.getSubject())
                                                isExist = true;
                                        }
                                        if (!isExist) {
                                            dbl.addLesson(les);
                                            //        Toast.makeText(UserActivity.this, "إدخال", Toast.LENGTH_LONG).show();
                                        }
                                    } else {

                                    }

                                }
                                if (isLastSubject) {
                                    progressBar.setProgress(100);
                                   // ref.child("Users").child(userId).removeEventListener(userListener);
                                   // groupref.removeEventListener(groupListener);
                                    FirebaseDatabase.getInstance().getReference().child(ElecUtils.getType(u.getType())).child(ElecUtils.getStudy(u.getType(), u.getStudy())).child(subject).removeEventListener(this);
                                    Intent main = new Intent(UserActivity.this, UserActivity.class);
                                    main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    isStartAnotherActivity = true;
                                    LocalBroadcastManager.getInstance(UserActivity.this).unregisterReceiver(broadcastReceiver);
                                    startActivity(main);
                                    finish();
                                    Toast.makeText(UserActivity.this, "تم تسجيل الدخول بنجاح...", Toast.LENGTH_LONG).show();
                                    Toast.makeText(UserActivity.this, "مرحبا بك في المعهد الإلكتروني...", Toast.LENGTH_LONG).show();
                               d.cancel();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
                if (!hasNext) {
                    progressBar.setProgress(100);
                   // ref.child("Users").child(userId).removeEventListener(userListener);
                   // groupref.removeEventListener(groupListener);
                    Intent main = new Intent(UserActivity.this, UserActivity.class);
                    main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    isStartAnotherActivity = true;
                    LocalBroadcastManager.getInstance(UserActivity.this).unregisterReceiver(broadcastReceiver);
                    startActivity(main);
                    finish();
                    Toast.makeText(UserActivity.this, "تم تسجيل الدخول بنجاح...", Toast.LENGTH_LONG).show();
                    Toast.makeText(UserActivity.this, "مرحبا بك في المعهد الإلكتروني...", Toast.LENGTH_LONG).show();
              d.cancel();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        curUserList = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!isCommentChange) {
                    if (dataSnapshot.exists()) {
                        String sbirthday = dataSnapshot.child("birthday").getValue().toString();
                        String sdate = dataSnapshot.child("date").getValue().toString();
                        String stime = dataSnapshot.child("time").getValue().toString();
                        String semail = dataSnapshot.child("email").getValue().toString();
                        String sfather = dataSnapshot.child("father").getValue().toString();
                        String smother = dataSnapshot.child("mother").getValue().toString();
                        String sfrom = dataSnapshot.child("from").getValue().toString();
                        String sid = dataSnapshot.child("id").getValue().toString();
                        String simage = dataSnapshot.child("image").getValue().toString();
                        String sname = dataSnapshot.child("name").getValue().toString();
                        String snum = dataSnapshot.child("num").getValue().toString();
                        String snum_ = dataSnapshot.child("num_").getValue().toString();
                        String splace = dataSnapshot.child("place").getValue().toString();
                        String sstudy = dataSnapshot.child("study").getValue().toString();
                        String suid = dataSnapshot.child("uid").getValue().toString();
                        String sbill = dataSnapshot.child("bill").getValue().toString();
                        String steacher = dataSnapshot.child("isTeacher").getValue().toString();
                        String snumcomments = dataSnapshot.child("numcomments").getValue().toString();
                        String stype = dataSnapshot.child("type").getValue().toString();
                        String spass = dataSnapshot.child("pass").getValue().toString();
                        String sstatus = dataSnapshot.child("status").getValue().toString();
                        ArrayList<Integer> studyList = new ArrayList<>();
                        String studies = dataSnapshot.child("studies").getValue().toString();
                        String sban = dataSnapshot.child("isBanned").getValue().toString();
                        if (!TextUtils.isEmpty(studies)) {
                            for (int i = 0; i < studies.length(); i += 3) {
                                studyList.add(Integer.valueOf(studies.substring(i, i + 3)));
                            }
                        }
                        u.setColumn(dbu.getCurrentUser().getColumn());
                        u.setStudies(studyList);
                        u.setType(Integer.valueOf(stype));
                        u.setName(sname);
                        u.setFather(sfather);
                        u.setMother(smother);
                        u.setId(sid);
                        u.setFrom(sfrom);
                        u.setPlace(splace);
                        u.setBirthday(sbirthday);
                        u.setPhone(snum);
                        u.setTelephone(snum_);
                        u.setEmail(semail);
                        u.setUser(semail);
                        u.setStudy(Integer.valueOf(sstudy));
                        u.setDate(sdate);
                        u.setTime(stime);
                        u.setBill(sbill);
                        u.setBills(sbill);
                        u.setUserId(suid);
                        u.setPassword(spass);
                        u.setNumComments(Integer.valueOf(snumcomments));
                        if (steacher.contains("1"))
                            u.setTeacher(true);
                        else
                            u.setTeacher(false);
                        if (sban.contains("1"))
                            u.setBanned(true);
                        else
                            u.setBanned(false);
                        u.setStatus(sstatus);
                        if (!simage.matches("uri")) {
                            StorageReference refe = FirebaseStorage.getInstance().getReference().child("Profile Images/" + suid + ".jpg");
                            try {
                                final File localFile = File.createTempFile("Images", "jpg");
                                refe.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        my_image = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                        u.setProfile(my_image);
                                        dbu.updateUser(u);
                                     //   ref.child("Users").child(userId).removeEventListener(curUserList);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                     //   ref.child("Users").child(userId).removeEventListener(curUserList);
                                        u.setProfile(dbu.getCurrentUser().getProfile());
                                        dbu.updateUser(u);

                                        //    Toast.makeText(UserActivity.this, "لم يتم تسجيل الدخول, يرجى إعادة فتح البرنامج!", Toast.LENGTH_LONG).show();
                                        //  Toast.makeText(UserActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                        //    closeApplication();
                                    }
                                });
                            } catch (IOException e) {
                              //  ref.child("Users").child(userId).removeEventListener(curUserList);
                                u.setProfile(dbu.getCurrentUser().getProfile());
                                dbu.updateUser(u);

                                //    Toast.makeText(UserActivity.this, "لم يتم تسجيل الدخول, يرجى إعادة فتح البرنامج!", Toast.LENGTH_LONG).show();
                                //  Toast.makeText(UserActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                //     closeApplication();
                            }
                        } else {
                            u.setProfile(BitmapFactory.decodeResource(res, R.drawable.profile_image));
                            dbu.updateUser(u);
                         //   ref.child("Users").child(userId).removeEventListener(curUserList);

                        }
                    } else {
//                    name.setVisibility(View.VISIBLE);
                      //  ref.child("Users").child(userId).removeEventListener(this);
                        Toast.makeText(UserActivity.this, "تم حذف حسابك يرجى مراجعة المعهد!", Toast.LENGTH_LONG).show();
                        logOut();
                    }
                } else {
                    isCommentChange = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        curGroupList = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //          Toast.makeText(LoginActivity.this,"datachange",Toast.LENGTH_LONG).show();
                // ArrayList<String> set=new HashSet<>();
                Iterator iterator = dataSnapshot.getChildren().iterator();
                //     Toast.makeText(UserActivity.this,iterator.toString(),Toast.LENGTH_LONG).show();
                boolean hasNext = false;
                while (iterator.hasNext()) {

                    hasNext = true;
                    //  set.add(((DataSnapshot)iterator.next()).getKey());
                    final String subject = ((DataSnapshot) iterator.next()).getKey();
                    //     Toast.makeText(LoginActivity.this,subject,Toast.LENGTH_LONG).show();
                    boolean isSigned = false;
                    for (int i = 0; i < user.getStudies().size(); i++) {
                        //  Toast.makeText(LoginActivity.this,String.valueOf(user.getStudies().get(i)),Toast.LENGTH_SHORT).show();
                        if (user.getStudies().get(i) == ElecUtils.getSubject(user.getType(), user.getStudy(), subject))
                            isSigned = true;
                    }
                    if (!iterator.hasNext())
                        isLastSubject = true;
                    //  Toast.makeText(UserActivity.this,subject,Toast.LENGTH_LONG).show();
                    if (isSigned) {
                        FirebaseDatabase.getInstance().getReference().child(ElecUtils.getType(user.getType())).child(ElecUtils.getStudy(user.getType(), user.getStudy())).child(subject).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Iterator iterator1 = snapshot.getChildren().iterator();
                                boolean hasNext1 = false;
                                while (iterator1.hasNext()) {
                                    hasNext1 = true;
                                    int Type = user.getType();
                                    int Study = user.getStudy();
                                    DataSnapshot data = (DataSnapshot) iterator1.next();
                                    String Lesson = data.getKey();
                                    // int Lesson = (int)data.getValue();
                                    if (!Lesson.matches("الأسئلة")) {
                                        LessonListChildItem les = new LessonListChildItem(dbl.allLessons().size(), Type, Study, ElecUtils.getSubject(Type, Study, subject), Integer.valueOf(Lesson.substring(Lesson.indexOf("|") + 1)), 0, subject, Lesson.substring(0, Lesson.indexOf("|")), "", true, 1, getTime(), getDate());
                                        //  Toast.makeText(UserActivity.this, String.valueOf(les.getLesson()), Toast.LENGTH_LONG).show();
                                        //    Toast.makeText(LoginActivity.this, String.valueOf(les.getLessonN()), Toast.LENGTH_LONG).show();
                                        boolean isExist = false;
                                        for (int i = 0; i < dbl.allLessons().size(); i++) {
                                            if (dbl.allLessons().get(i).getLesson() == les.getLesson() && dbl.allLessons().get(i).getSubject() == les.getSubject())
                                                isExist = true;
                                        }
                                        if (!isExist) {
                                            dbl.addLesson(les);
                                            //        Toast.makeText(UserActivity.this, "إدخال", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                                if (isLastSubject) {
                                    groupref.removeEventListener(curGroupList);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
                if (!hasNext) {
                    groupref.removeEventListener(curGroupList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        setDownloadSettings();
    }

    private void showBanDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("الحساب محظور!");
        builder.setIcon(R.drawable.close);
        builder.setMessage("تم حظر الحساب, يرجى مراجعة المعهد");
        builder.setCancelable(false);
        // builder.setMessage(R.string.cancel_connection_query);
        // builder.setNegativeButton("إلغاء", this);
        builder.setPositiveButton("موافق", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                closeApplication();
            }
        });
        //builder.setOnCancelListener(this);
        builder.show();
    }

    void setDownloadSettings() {
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .setReadTimeout(60_000)
                .setConnectTimeout(60_000)
                .build();
        PRDownloader.initialize(context, config);
    }


    long lastNtime = 0;

    void addDownload(LessonListChildItem Lesson, DownloadListChildItem Download, boolean isExist) {
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
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference dateRef = storageRef.child("/Videos/" + ElecUtils.getVideoDir(Lesson.getType(), Lesson.getStudy(), Lesson.getSubject(), Lesson.getLesson(), false));
        dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri downloadUrl) {
                //Toast.makeText(UserActivity.this,downloadUrl.toString(),Toast.LENGTH_LONG).show();
                // File file = new File("E101.mp4");
                //  File f = context.getFilesDir();
                try {
                    FileOutputStream fileOuputStream = context.openFileOutput(ElecUtils.getVideoDir(Lesson.getType(), Lesson.getStudy(), Lesson.getSubject(), Lesson.getLesson(), true), MODE_PRIVATE);
                } catch (FileNotFoundException e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }
                downloadBtn.setVisibility(VISIBLE);

                animation.start();
                animation.start();
                Download.setType(Lesson.getType());
                Download.setStudy(Lesson.getStudy());
                Download.setSubject(Lesson.getSubject());
                Download.setLesson(Lesson.getLesson());
                Download.setTitle(Lesson.getSubjectN());
                Download.setProgress(0);
                Download.setName("الدرس " + String.valueOf(Lesson.getLesson() - 100 + 1) + " : " + Lesson.getLessonN());
                Download.setUser(dbu.getCurrentUser().getUser());
                if (!isExist) {
                    Download.setColumn(dbd.allDownloads().size());
                    Download.setTime(getTime());
                    Download.setDate(getDate());
                }
                NotificationCompat.Builder notification = new NotificationCompat.Builder(context, "Elec").setSmallIcon(android.R.drawable.stat_sys_download).setContentTitle("الدرس " + String.valueOf(Lesson.getLesson() - 100 + 1) + " : " + Lesson.getLessonN()).setWhen(System.currentTimeMillis());
                RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_download_view);
                contentView.setProgressBar(R.id.notifProgress, 100, 0, false);
                contentView.setTextViewText(R.id.notifTitle, Lesson.getSubjectN());
                contentView.setTextViewText(R.id.notifName, "الدرس " + String.valueOf(Lesson.getLesson() - 100 + 1) + " : " + Lesson.getLessonN());
                contentView.setImageViewResource(R.id.notifImage, ElecUtils.getResourceFromSubject(Lesson.getType(), Lesson.getStudy(), Lesson.getSubject()));
                notification.setContent(contentView);
                final Intent intent = new Intent(context, UserActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(Intent.ACTION_MAIN);

                DownloadRequest download = PRDownloader.download(downloadUrl.toString(), context.getFilesDir().getPath(), ElecUtils.getVideoDir(Lesson.getType(), Lesson.getStudy(), Lesson.getSubject(), Lesson.getLesson(), true))
                        .build()
                        .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                            @Override
                            public void onStartOrResume() {
                                fragmentCommunicator.setIsDownloading(Lesson, true);
                                fragmentCommunicator.setIsResume(Lesson, true);
                                if (isExist)
                                    Download.setDownload(DownloadListChildItem.RESUMED);
                                else
                                    Download.setDownload(DownloadListChildItem.STARTED);
                                dbd.openDatabase();
                                dbd.updateDownloadP(Download);
                                // Intent notificationIntent = new Intent(context, NotificationHandler.class);
                                // PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
                                // notification.contentIntent = contentIntent;
                                contentView.setTextViewText(R.id.notifTxt, "جاري التحميل...");
                                notification.setOngoing(true);
                                notification.setSmallIcon(android.R.drawable.stat_sys_download);
                                mManager.notify(Lesson.getDownloadId(), notification.build());
                            }
                        })
                        .setOnPauseListener(new OnPauseListener() {
                            @Override
                            public void onPause() {
                                fragmentCommunicator.setIsDownloading(Lesson, false);
                                fragmentCommunicator.setIsResume(Lesson, true);
                                Download.setDownload(DownloadListChildItem.PAUSED);
                                contentView.setTextViewText(R.id.notifTxt, "ايقاف مؤقت");
                                notification.setOngoing(false);
                                notification.setSmallIcon(android.R.drawable.stat_sys_download_done);
                                mManager.notify(Lesson.getDownloadId(), notification.build());
                                dbd.updateDownloadP(Download);
                                dbd.closeDatabase();
                            }
                        })
                        .setOnCancelListener(new OnCancelListener() {
                            @Override
                            public void onCancel() {
                                fragmentCommunicator.setIsDownloading(Lesson, false);
                                fragmentCommunicator.setIsResume(Lesson, false);
                                fragmentCommunicator.setProgress(Lesson, 0);
                                contentView.setTextViewText(R.id.notifTxt, "تم ايقاف التحميل!");

                                notification.setOngoing(false);
                                notification.setSmallIcon(android.R.drawable.stat_sys_download_done);
                                mManager.notify(Lesson.getDownloadId(), notification.build());
                                Lesson.setDownloadId(0);
                                Download.setDownload(DownloadListChildItem.STOPED);
                                Download.setId(0);
                                dbd.updateDownloadP(Download);
                                dbd.closeDatabase();
                                dbl.updateLesson(Lesson);
                            }
                        })
                        .setOnProgressListener(new OnProgressListener() {
                            @Override
                            public void onProgress(Progress progress) {
                                long progressPercent = progress.currentBytes * 100 / progress.totalBytes;
                                fragmentCommunicator.setProgress(Lesson, (int) progressPercent);
                                //contentView.set
                                long currentNTime = System.currentTimeMillis();
                                if (currentNTime > lastNtime + 1500) {
                                    contentView.setTextViewText(R.id.notifTxt, "جاري التحميل..." + "(" + String.valueOf((int) progressPercent) + "%)");
                                    contentView.setProgressBar(R.id.notifProgress, 100, (int) progressPercent, false);
                                    mManager.notify(Lesson.getDownloadId(), notification.build());
                                    lastNtime = currentNTime;
                                    Download.setProgress((int) progressPercent);
                                    Download.setDownload(DownloadListChildItem.RUNNING);
                                    dbd.updateDownloadP(Download);
                                }

                            }
                        });
                download.start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        fragmentCommunicator.setIsDownloading(Lesson, false);
                        fragmentCommunicator.setTextDownload(Lesson, "تم التحميل");
                        fragmentCommunicator.setButtonDownloadV(Lesson, GONE);
                        fragmentCommunicator.setButtonStopV(Lesson, GONE);
                        fragmentCommunicator.setButtonPauseV(Lesson, GONE);
                        fragmentCommunicator.setButtonWatchV(Lesson, VISIBLE);
                        File f = new File(context.getFilesDir(), ElecUtils.getVideoDir(Lesson.getType(), Lesson.getStudy(), Lesson.getSubject(), Lesson.getLesson(), true));
                        notification.setOngoing(false);
                        Lesson.setDownload(2);
                        if (Notif != null) {
                            Notif.setDownload(2);
                            dbn.updateNotif(Notif);
                        }
                        List<NotifListChildItem> list = dbn.allNotifs(dbu.getCurrentUser().getType(), dbu.getCurrentUser().getStudy());
                        for (int i = 0; i < list.size(); i++) {
                            NotifListChildItem n = list.get(i);
                            if (n.getSubject() == Lesson.getSubject() && n.getName().contains(":") && n.getName().substring(n.getName().indexOf(":") + 2).matches(Lesson.getLessonN())) {
                                n.setDownload(2);
                                dbn.updateNotif(n);
                                break;
                            }
                        }
                        Lesson.setFile(f.getPath());
                        dbl.updateLesson(Lesson);
                        //  SecondaryDrawerItem DrawerItem = (SecondaryDrawerItem) result.getDrawerItem(Long.valueOf(String.valueOf(Lesson.getSubject() + String.valueOf(Lesson.getLesson()))));
                        //    DrawerItem.withIcon(R.drawable.downloaded);
                        //   DrawerItem.withTag(Lesson);
                        //   result.updateItem(DrawerItem);
                        contentView.setTextViewText(R.id.notifTxt, "تم التحميل ,اضغط للمشاهدة");
                        contentView.setProgressBar(R.id.notifProgress, 100, 100, false);
                        notification.setSmallIcon(android.R.drawable.stat_sys_download_done);
                        mManager.notify(Lesson.getDownloadId(), notification.build());

                        Download.setDownload(DownloadListChildItem.COMPLETED);
                        Download.setProgress(100);
                        dbd.updateDownloadP(Download);
                        dbd.closeDatabase();
                        //    th.stop();
                    }

                    @Override
                    public void onError(Error error) {
                        fragmentCommunicator.setIsDownloading(Lesson, false);
                        fragmentCommunicator.setIsResume(Lesson, false);
                        fragmentCommunicator.setTextDownload(Lesson, "لم يتم التحميل ,حدث خطأ!");
                        fragmentCommunicator.setButtonDownloadV(Lesson, VISIBLE);
                        fragmentCommunicator.setButtonStopV(Lesson, GONE);
                        fragmentCommunicator.setButtonPauseV(Lesson, GONE);
                        fragmentCommunicator.setButtonWatchV(Lesson, GONE);
                        notification.setOngoing(false);
                        notification.setSmallIcon(android.R.drawable.stat_sys_download_done);
                        contentView.setTextViewText(R.id.notifTxt, "لم يتم التحميل ,حدث خطأ!");
                        mManager.notify(Lesson.getDownloadId(), notification.build());
                        Lesson.setDownloadId(0);
                        Download.setDownload(DownloadListChildItem.ERROR);
                        Download.setId(0);
                        dbd.updateDownloadP(Download);
                        dbd.closeDatabase();
                        dbl.updateLesson(Lesson);


                        //  th.stop();
                        if (!isConnectingToInternet(context))
                            Toast.makeText(context, "لا يوجد اتصال بالإنترنت!", Toast.LENGTH_LONG).show();
                    }

                });
                Lesson.setDownloadId(download.getDownloadId());
                Download.setId(download.getDownloadId());
                //  lastNtime.put(Lesson.getDownloadId(),Long.valueOf(0));
                //  intent.putExtra("id",Lesson.getDownloadId());
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);
                //    notification.setContentIntent(contentIntent);
                dbl.updateLesson(Lesson);
                if (isExist)
                    dbd.updateDownload(Download);
                else
                    dbd.addDownload(Download);
                boolean isIdExist = false;
                for (int i = 0; i < arrayIds.size(); i++) {
                    if (Lesson.getDownloadId() == arrayIds.get(i))
                        isIdExist = true;
                }
                if (!isIdExist)
                    arrayIds.add(Lesson.getDownloadId());
            }
        });

    }

    void resumeDownload(LessonListChildItem Lesson) {
        PRDownloader.resume(Lesson.getDownloadId());
        for (int i = 0; i < dbd.allDownloads().size(); i++) {
            DownloadListChildItem Download = dbd.allDownloads().get(i);
            if (dbd.allDownloads().get(i).getId() == Lesson.getDownloadId()) {
                Download.setDownload(DownloadListChildItem.RESUMED);
                dbd.updateDownload(Download);
            }
        }
    }

    void stopDownload(LessonListChildItem Lesson) {
        PRDownloader.cancel(Lesson.getDownloadId());
        boolean isAll = true;
        for (int i = 0; i < dbd.allDownloads().size(); i++) {
            DownloadListChildItem Download = dbd.allDownloads().get(i);
            if (dbd.allDownloads().get(i).getId() == Lesson.getDownloadId()) {
                Download.setDownload(DownloadListChildItem.STOPED);
                dbd.updateDownload(Download);
            }
            if (dbd.allDownloads().get(i).getDownload() != DownloadListChildItem.STOPED && dbd.allDownloads().get(i).getDownload() != DownloadListChildItem.UNKNOWN)
                isAll = false;
        }
        if (isAll)
            downloadBtn.setVisibility(VISIBLE);
        // Lesson.setDownloadId(0);
        //dbl.updateLesson(Lesson);
    }

    void pauseDownload(LessonListChildItem Lesson) {
        PRDownloader.pause(Lesson.getDownloadId());
        for (int i = 0; i < dbd.allDownloads().size(); i++) {
            DownloadListChildItem Download = dbd.allDownloads().get(i);
            if (dbd.allDownloads().get(i).getId() == Lesson.getDownloadId()) {
                Download.setDownload(DownloadListChildItem.PAUSED);
                dbd.updateDownload(Download);
            }
        }
    }

    void logOut() {
        Loading.setTitle("تسجيل الخروج...");
        Loading.setMessage("يرجى الإنتظار...");
        Loading.setCanceledOnTouchOutside(false);
        Loading.show();

        mauth.signOut();
        if (dbu.allUsers().size() != 1) {
            for (int i = 0; i < dbu.allUsers().size(); i++) {
                if (!dbu.allUsers().get(i).getUserId().matches(dbu.getCurrentUser().getUserId())) {
                    final int k = i;
                   Loading.dismiss();
                   createDownloadProgressDialog();
                    mauth.signInWithEmailAndPassword(dbu.allUsers().get(i).getUser(), dbu.allUsers().get(i).getPassword())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        progressBar.setProgress(20);
                                        //  dbu.addUser(new UserListChildItem(dbu.allUsers().size(), "", userId,0, 0, "", "", "", "", "", "", "", "", "", "", getTime(), getDate(), "", "", studies, BitmapFactory.decodeResource(getResources(), R.drawable.profile_image), false, true, 0,true));
                                        u = dbu.allUsers().get(k);

                                        mauth = FirebaseAuth.getInstance();
                                        currentuser = mauth.getCurrentUser();
                                        ref = FirebaseDatabase.getInstance().getReference();
                                        if (currentuser != null) {
                                            userId = currentuser.getUid();
                                            userprofileimagereference = FirebaseStorage.getInstance().getReference().child("Profile Images");
                                            ref.child("Users").child(userId).addListenerForSingleValueEvent(userListener);
                                        }

                                    } else {

                                        String message = task.getException().toString();
                                        if (message.contains("!DOCTYPE") || message.contains("Timedout"))
                                            Toast.makeText(UserActivity.this, "تحقق من الاتصال", Toast.LENGTH_LONG).show();
                                        else if (message.contains("password is invalid"))
                                            Toast.makeText(UserActivity.this, "اسم المستخدم وكلمة السر غير متطابقان", Toast.LENGTH_LONG).show();
                                        else if (message.contains("no user record"))
                                            Toast.makeText(UserActivity.this, "لا يوجد اسم مستخدم مطابق", Toast.LENGTH_LONG).show();
                                        else if (message.contains("blocked all requests"))
                                            Toast.makeText(UserActivity.this, "تم الحظر بسبب محاولات تسجيل الدخول الخاطئة", Toast.LENGTH_LONG).show();
                                        else
                                            Toast.makeText(UserActivity.this, "خطأ " + message, Toast.LENGTH_LONG).show();
                                        d.cancel();
                                        closeApplication();
                                    }
                                }
                            });
                    break;
                }
            }
        } else {
            Loading.dismiss();
            Intent iii = new Intent(UserActivity.this, LoginActivity.class);
            iii.putExtra("isOnBackClose", true);
            startActivity(iii);
        }
        dbu.deleteUser(dbu.getCurrentUser());
        isCommentChange = true;


    }

    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void setVpnProxy() {

        connection = new CheckInternetConnection();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("connectionState"));
        setStatus(vpnService.getStatus());
        //  VpnStatus.initLogCache(getCacheDir());
    }

    boolean isProfileUpdated = false;

    public void setStatus(String connectionState) {
        if (connectionState != null)
            switch (connectionState) {
                case "DISCONNECTED":
                    //      status("connect");
                    vpnService.setDefaultStatus();
                    preference.saveVpnStarted(false);
                    //  FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    try {
                        fragmentCommunicator.setConnectButton(false);
                        if (fragmentCommunicator.isConnectAnimationStarted())
                            fragmentCommunicator.stopConnectAnimation();
                    } catch (Exception e) {
                    }
                    Toast.makeText(this, "غير متصل", Toast.LENGTH_SHORT).show();
                    //   binding.logTv.setText("");
                    break;
                case "CONNECTED":
                    // it will use after restart this activity
                    //    status("connected");
                    //  binding.logTv.setText("");
                    Toast.makeText(this, "متصل", Toast.LENGTH_SHORT).show();
                    try {
                        fragmentCommunicator.stopConnectAnimation();
                        fragmentCommunicator.setConnectButton(true);
                    } catch (Exception e) {
                    }
                    if (!isProfileUpdated) {
                        if (currentuser != null) {
                            new DownloadProfileData().execute();
                            isProfileUpdated = true;
                        }
                    }
                    break;
                case "WAIT":
                    try {
                        fragmentCommunicator.setConnectButton(false);
                        if (!fragmentCommunicator.isConnectAnimationStarted()) {
                            fragmentCommunicator.startConnectAnimation();
                        }
                    } catch (Exception e) {

                    }
                    //   binding.logTv.setText("waiting for server connection!!");
                    //   Toast.makeText(this,"waiting for server connection!!",Toast.LENGTH_SHORT).show();
                    break;
                case "AUTH":
                    try {
                        fragmentCommunicator.setConnectButton(false);
                        if (!fragmentCommunicator.isConnectAnimationStarted()) {
                            fragmentCommunicator.startConnectAnimation();
                        }
                    } catch (Exception e) {

                    }
                    //     binding.logTv.setText("server authenticating!!");
                    //    Toast.makeText(this,"server authenticating!!",Toast.LENGTH_SHORT).show();
                    break;
                case "RECONNECTING":
                    try {
                        fragmentCommunicator.setConnectButton(false);
                        if (!fragmentCommunicator.isConnectAnimationStarted()) {
                            fragmentCommunicator.startConnectAnimation();
                        }
                    } catch (Exception e) {

                    }
                    //status("connecting");
                    //       Toast.makeText(this,"RECONNECTING",Toast.LENGTH_SHORT).show();
                    break;
                case "NONETWORK":
                    try {
                        fragmentCommunicator.stopConnectAnimation();
                        fragmentCommunicator.setConnectButton(false);
                        preference.saveVpnStarted(false);
                    } catch (Exception e) {

                    }
                    //      binding.logTv.setText("No network connection");
                    //  Toast.makeText(this,"لا يوجد ",Toast.LENGTH_SHORT).show();
                    break;
            }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                setStatus(intent.getStringExtra("state"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public class DownloadProfileData extends AsyncTask<Void, Void, String> {


        DownloadProfileData() {

        }

        @Override
        protected String doInBackground(Void... params) {
            String s = "";
            try {
                userId = currentuser.getUid();
                userprofileimagereference = FirebaseStorage.getInstance().getReference().child("Profile Images");
                RetrieveUserInfo();
                RetrieveGroups(dbu.getCurrentUser());
            } catch (Exception e) {
                s = e.getMessage();
            }
            return s;
        }

        @Override
        protected void onPostExecute(final String token) {

        }

    }

    private DatabaseReference groupref;
    boolean isLastSubject = false;

    private void RetrieveGroups(UserListChildItem user) {
        groupref = FirebaseDatabase.getInstance().getReference().child(ElecUtils.getType(user.getType())).child(ElecUtils.getStudy(user.getType(), user.getStudy()));
        groupref.addListenerForSingleValueEvent(curGroupList);
    }

    boolean isSecondTime = false;

    private void RetrieveUserInfo() {
        ref.child("Users").child(userId).child("isBanned").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String isban = (String) snapshot.getValue();
                    if (isban.contains("1")) {
                        u.setBanned(true);
                        dbu.updateUser(u);
                        showBanDialog();
                    } else {
                        isSecondTime = true;
                        u.setBanned(false);
                        dbu.updateUser(u);
                        ref.child("Users").child(userId).child("status").setValue("1");
                        ref.child("Users").child(userId).child("isNow").setValue("1");
                        ref.child("Users").child(userId).addListenerForSingleValueEvent(curUserList);
                    }
                } else {
                    Toast.makeText(UserActivity.this, "تم حذف حسابك يرجى مراجعة المعهد!", Toast.LENGTH_LONG).show();
                    logOut();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    String status = "2";

    @Override
    protected void onStart() {
        if (isSecondTime) {
            ref.child("Users").child(userId).child("isBanned").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String isban = (String) snapshot.getValue();
                        if (isban.contains("1")) {
                            u.setBanned(true);
                            dbu.updateUser(u);
                            showBanDialog();
                        } else {
                            ref.child("Users").child(userId).child("status").setValue("1");
                            u.setBanned(false);
                            dbu.updateUser(u);
                            //  ref.child("Users").child(userId).addValueEventListener(curUserList);
                        }
                    } else {
                        Toast.makeText(UserActivity.this, "تم حذف حسابك يرجى مراجعة المعهد!", Toast.LENGTH_LONG).show();
                        logOut();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        super.onStart();
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void forceRTLIfSupported() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }

    public static void changeToolbarFont(Toolbar toolbar, Activity context) {
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                if (tv.getText().equals(toolbar.getTitle())) {
                    applyFont(tv, context);
                    break;
                }
            }
        }
    }

    public static void applyFont(TextView tv, Activity context) {
        tv.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/font_1.ttf"));
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

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            closeApplication();
            return;
        } else {
            fragmentCommunicator.exitVideoLesson();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "اضغط مرة أخرى للخروج", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public void closeApplication() {
        finish();
        finishAffinity();
        moveTaskToBack(true);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    @Override
    public void onResume() {
        if (server == null) {
            server = preference.getServer();
        }
        handStopVpn.removeCallbacks(runStopVpn);
        isHandRunning = false;
        setVpnProxy();
        if (!preference.isVpnStarted()) {
            // Toast.makeText(this,"resu",Toast.LENGTH_LONG).show();
            prepareVpn();
        }
        super.onResume();
    }

    public void prepareVpn() {
        if (getInternetStatus()) {
            // Checking permission for network monitor
            Intent intent = VpnService.prepare(this);
            if (intent != null) {
                startActivityForResult(intent, 1);
            } else startVpn();//have already permission

            // Update confection status
            //status("connecting");

        } else {

            // No internet connection available
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }

    private CheckInternetConnection connection;

    public boolean getInternetStatus() {

        return connection.netCheck(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                //Permission granted, start the VPN
                startVpn();
            } else {
                Toast.makeText(this, "يرجى السماح للبرنامج بالاتصال", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startVpn() {
        try {

            if (vpnService.getStatus().matches("DISCONNECTED") || !vpnService.isConnected()) {
                if (!isPicImages) {
                    preference.saveVpnStarted(true);
                    // .ovpn file
                    InputStream conf = getAssets().open(server.getOvpn());
                    InputStreamReader isr = new InputStreamReader(conf);
                    BufferedReader br = new BufferedReader(isr);
                    String config = "";
                    String line;

                    while (true) {
                        line = br.readLine();
                        if (line == null) break;
                        config += line + "\n";
                    }
                    br.readLine();
                    OpenVpnApi.startVpn(this, config, server.getCountry(), server.getOvpnUserName(), server.getOvpnUserPassword());

                    // Update log
                    Toast.makeText(this, "جاري الاتصال...", Toast.LENGTH_SHORT).show();
                    //    binding.logTv.setText("جاري الاتصال...");
                }
            }
        } catch (IOException | RemoteException e) {
            e.printStackTrace();
        }
    }

    public boolean stopVpn() {
        try {
            preference.saveVpnStarted(false);
            vpnThread.stop();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Save current selected server on local shared preference
     */
    boolean isEditStatus = true;

    @Override
    public void onStop() {

        ref.child("Users").child(userId).child("status").setValue(getDate() + "|" + getTime());
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        if (server != null) {
            preference.saveServer(server);
        }
        if (!isHandRunning) {
            handStopVpn.postDelayed(runStopVpn, 400000);
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        for (int i = 0; i < dbd.allDownloads().size(); i++) {
            if (dbd.allDownloads().get(i).getDownload() == DownloadListChildItem.RUNNING) {
                DownloadListChildItem Download = dbd.allDownloads().get(i);
                PRDownloader.pause(Download.getId());
                Download.setDownload(DownloadListChildItem.PAUSED);
                dbd.updateDownload(Download);
                mManager.cancel(Download.getId());
            }

        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        if (!isStartAnotherActivity) {
            preference.saveVpnStarted(false);
            handStopVpn.removeCallbacks(runStopVpn);
            stopVpn();
        }
        super.onDestroy();
    }

    XmlClickable someFragment;

    //...onCreate, etc. instantiating your fragments casting to your interface.
    public void onClick(View v) {
        someFragment.onClick(v);
    }
}