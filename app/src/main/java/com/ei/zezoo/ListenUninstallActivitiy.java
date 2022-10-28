package com.ei.zezoo;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

class ListenUninstallActivitiy extends Thread{
    boolean exit = false;
    ActivityManager am = null;
    Context context = null;
    ProgressDialog Loading;
   FirebaseAuth mauth;
    DatabaseReference ref;
    String userId;
    FirebaseUser currentuser;
    UserSQLDatabaseHandler dbu;
    public ListenUninstallActivitiy(Context con){
        context = con;
        am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    public void run(){

        Looper.prepare();

        while(!exit){

            // get the info from the currently running task
            List< ActivityManager.RunningTaskInfo > taskInfo = am.getRunningTasks(MAX_PRIORITY);

            String activityName = taskInfo.get(0).topActivity.getClassName();


            Log.d("topActivity", "CURRENT Activity ::"
                    + activityName);

            if (activityName.equals("com.android.packageinstaller.UninstallerActivity")) {
                // User has clicked on the Uninstall button under the Manage Apps settings

                //do whatever pre-uninstallation task you want to perform here
                // show dialogue or start another activity or database operations etc..etc..
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("يجب تسجيل الخروج من الحساب قبل حذف التطبيق");
                // builder.setMessage(R.string.cancel_connection_query);
                builder.setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Loading = new ProgressDialog(context);
                        Loading.setTitle("تسجيل الخروج...");
                        Loading.setMessage("يرجى الإنتظار...");
                        Loading.setCanceledOnTouchOutside(false);
                        Loading.show();
                        dbu = new UserSQLDatabaseHandler(context);
                        mauth = FirebaseAuth.getInstance();
                        currentuser = mauth.getCurrentUser();
                        ref = FirebaseDatabase.getInstance().getReference();
                        userId = currentuser.getUid();
                        ref.child("Users").child(userId).child("isNow").setValue("0");
                        mauth.signOut();
                        dbu.deleteUser(dbu.getCurrentUser());
                        Loading.dismiss();
                    }
                });
                builder.show();
                // context.startActivity(new Intent(context, MyPreUninstallationMsgActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                exit = true;
              //  Toast.makeText(context, "Done with preuninstallation tasks... Exiting Now", Toast.LENGTH_SHORT).show();
            } else if(activityName.equals("com.android.settings.ManageApplications")) {
                // back button was pressed and the user has been taken back to Manage Applications window
                // we should close the activity monitoring now
                exit=true;
            }
        }
        Looper.loop();
    }
}
