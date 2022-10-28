package com.ei.zezoo;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.VpnService;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.TransitionManager;

import com.ei.zezoo.model.Server;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanks.htextview.evaporate.EvaporateTextView;
import com.transitionseverywhere.ChangeText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import de.blinkt.openvpn.OpenVpnApi;
import de.blinkt.openvpn.core.OpenVPNService;
import de.blinkt.openvpn.core.OpenVPNThread;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;


public class SplashActivity extends AppCompatActivity {
    RelativeLayout rl;
    EvaporateTextView logo;
    GifImageView img;
    ProgressBar progressBar;
    Resources res;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    int arcMotionCount = 0;
    private FirebaseAuth mauth;
    private DatabaseReference ref;
    private FirebaseUser currentuser;
    UserSQLDatabaseHandler dbu;

    Context context;
    private AtomicInteger mLocalHttpProxyPort;
    private boolean isAccountDeleted;
    private boolean isStartAnotherActivity = false;
    private String userId;
    LessonSQLDatabaseHandler dbl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        res = getResources();
        context = this;
        mauth= FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();
        currentuser=mauth.getCurrentUser();
        dbl = new LessonSQLDatabaseHandler(context);
        dbl.allLessons();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        logo = (EvaporateTextView) findViewById(R.id.logo);
        img = (GifImageView) findViewById(R.id.imageView);
        dbu = new UserSQLDatabaseHandler(context);
        dbu.allUsers();
       // ArrayList<Integer> studies = new ArrayList<>();
       // dbu.addUser(new UserListChildItem(0, "","", 0,0, "", "", "", "", "", "", "", "", "", "", getTime(), getDate(), "", "", studies, BitmapFactory.decodeResource(res, R.drawable.profile),false, true, 0, true));
      //  u = dbu.getCurrentUser();

        logo.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/baloo.ttf"));
        logo.animateText("Bee Academy");



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                logo.animateText("Be Special With Us");
                ((GifDrawable) img.getDrawable()).start();
            }
        }, 4000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                logo.setVisibility(View.VISIBLE);
                // Start long running operation in a background thread
                new Thread(new Runnable() {
                    public void run() {
                        while (progressStatus < 100) {
                            progressStatus += 1;
                            // Update the progress bar and display the
                            //current value in the text view
                            handler.post(new Runnable() {
                                public void run() {
                                    progressBar.setProgress(progressStatus);
                                }
                            });
                            try {
                                // Sleep for 200 milliseconds.
                                Thread.sleep(79);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        }, 200);
        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                isStartAnotherActivity = true;
                if(currentuser==null){
                    signupAsFirst();
                }else {
                    Toast.makeText(SplashActivity.this, "مرحبا بك في المعهد الإلكتروني...", Toast.LENGTH_LONG).show();
                    Intent mainIntent = new Intent(SplashActivity.this, UserActivity.class);
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();

                }
            }
        }, 8000);
        new StartVpn().execute();
        //Toast.makeText(SplashActivity.this,String.valueOf(dbu.allUsers().size()),Toast.LENGTH_LONG).show();
    }
    public class StartVpn extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... params) {
            String s = "";
            try {
                setVpnProxy();
            } catch (Exception e) {
                s = e.getMessage();
            }
            return s;
        }

        @Override
        protected void onPostExecute(final String token) {

        }

    }
    private Server server;
    private CheckInternetConnection connection;

    OpenVPNThread vpnThread = new OpenVPNThread();
    OpenVPNService vpnService = new OpenVPNService();
    private SharedPreference preference;
    private DatabaseReference groupref;



    private void setVpnProxy() {
        preference = new SharedPreference(this);
        server = preference.getServer();
        Server s = new Server("Japan",
                "aaa.ovpn",
                "",
                ""
        );
        preference.saveServer(s);
        server = preference.getServer();
        connection = new CheckInternetConnection();
        if(!vpnService.isConnected()){
            preference.saveVpnStarted(false);
        }
        if (!preference.isVpnStarted()){

      //     Toast.makeText(SplashActivity.this, "Vpn Started", Toast.LENGTH_LONG).show();
            prepareVpn();
        }
    }
    private void prepareVpn() {
            if (getInternetStatus()) {
                // Checking permission for network monitor
                Intent intent = VpnService.prepare(this);
                if (intent != null) {
                    startActivityForResult(intent, 1);
                } else startVpn();//have already permissio
            } else {
                // No internet connection available
                showToast("يجب الاتصال بالإنترنت!");
            }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //Permission granted, start the VPN
            startVpn();
        } else {
            showToast("Permission Deny !! ");
        }
    }
    public boolean getInternetStatus() {
        return connection.netCheck(this);
    }
    private void startVpn() {
        try {

            if (vpnService.getStatus().matches("DISCONNECTED") || !vpnService.isConnected()) {
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
        } catch (IOException | RemoteException e) {
            e.printStackTrace();
        }
    }
   // private void startVpn() {

     //       if (vpnService.getStatus().matches("DISCONNECTED") || !vpnService.isConnected()) {
       //         preference.saveVpnStarted(true);
                // .ovpn file
        //        String root = Environment.getExternalStorageDirectory().toString();
          //      File myDir = new File(root + "/ElectronicInstitute/media");
         //       final File mSaveBit = new File(myDir,"config" + ".ovpn");
          //      StorageReference refe = FirebaseStorage.getInstance().getReference().child("config" + ".ovpn");
          //      refe.getFile(mSaveBit).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
          //          @Override
          //          public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
          //              try {
           //             FileInputStream conf = new FileInputStream(mSaveBit);
           //             InputStreamReader isr = new InputStreamReader(conf);
            //            BufferedReader br = new BufferedReader(isr);
            //            String config = "";
           //             String line;

           //             while (true) {
           //                 line = br.readLine();
            //                if (line == null) break;
            //                config += line + "\n";
            //            }
           //             br.readLine();
           //             OpenVpnApi.startVpn(SplashActivity.this, config, server.getCountry(), server.getOvpnUserName(), server.getOvpnUserPassword());

                        // Update log
            //            Toast.makeText(SplashActivity.this, "Connecting...", Toast.LENGTH_SHORT).show();
          //              } catch (IOException | RemoteException e) {
         //                   e.printStackTrace();
          //              }
          //          }
        //        }).addOnFailureListener(new OnFailureListener() {
         //           @Override
         //           public void onFailure(@NonNull Exception e) {
          //              Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
          //          }
         //       });

                //    /
  // }



    /**
     * Show toast message
     * @param message: toast message
     */
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        if (server == null) {

        }
        super.onResume();
    }

    /**
     * Save current selected server on local shared preference
     */
    @Override
    public void onStop() {
        if (server != null) {
            preference.saveServer(server);
        }

        super.onStop();
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
    @Override
    protected void onDestroy() {
        if (!isStartAnotherActivity  && vpnService.getStatus().matches("CONNECTED")) {
            stopVpn();
        }
        super.onDestroy();
    }
    private void signupAsFirst() {
        /**Boolean isFirstRun = context.getSharedPreferences("PREFERENCE", MODE_PRIVATE)
         .getBoolean("isFirstRun", false);
         if (isFirstRun) {
         //show sign up activity
         context.startActivity(new Intent(context, LoginActivity.class));
         }**/
        Intent LoginIntent=new Intent(SplashActivity.this,LoginActivity.class);
        LoginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(LoginIntent);
        finish();
    }
    private void VerifyUserExistence()
    {
        final String UserId=mauth.getCurrentUser().getUid();
        ref.child("Users").child(UserId).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if((dataSnapshot.child("Users").child(UserId).child("name").exists()))
                {
                    //Toast.makeText(SplashActivity.this,"مرحبا بك في المعهد الإلكتروني...",Toast.LENGTH_LONG).show();     //User is previous user
                }
                else
                {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
