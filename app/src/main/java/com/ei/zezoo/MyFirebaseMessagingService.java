package com.ei.zezoo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.SystemClock;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyFirebaseMessagingService  extends FirebaseMessagingService {
UserSQLDatabaseHandler dbu;
    NotifSQLDatabaseHandler dbn;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        dbu = new UserSQLDatabaseHandler(this);
        dbn = new NotifSQLDatabaseHandler(this);
        String body = remoteMessage.getNotification().getBody();
        int id = Integer.valueOf(body.substring(0,7));
        int typeN = Integer.valueOf(body.substring(7, 8));
        int type = Integer.valueOf(body.substring(8, 11));
        int study = Integer.valueOf(body.substring(11, 14));

        if (type == dbu.getCurrentUser().getType() && study == dbu.getCurrentUser().getStudy()) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setClass(this, UserActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) SystemClock.uptimeMillis(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "Elec")
                    .setLargeIcon(largeIcon)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setTicker(getString(R.string.app_name))
                    .setWhen(System.currentTimeMillis())
                   // .setStyle(new NotificationCompat.InboxStyle())
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setAutoCancel(true);
            switch(typeN){
                case 0 :
             //       Toast.makeText(this,"000000000",Toast.LENGTH_LONG).show();
                    int subject = Integer.valueOf(body.substring(14, 17));
                    boolean isExist = false;
                    for(int i = 0; i < dbu.getCurrentUser().getStudies().size();i++){
                        if(subject == dbu.getCurrentUser().getStudies().get(i))
                            isExist = true;
                    }
                    if(isExist){
                        notificationBuilder.setContentTitle("مادة ال" + ElecUtils.getSubject(dbu.getCurrentUser().getType(),dbu.getCurrentUser().getStudy(),subject)+"(درس جديد)");
                        notificationBuilder.setContentText(body.substring(17));
                        notificationBuilder.setSmallIcon(ElecUtils.getResourceFromSubject(dbu.getCurrentUser().getType(),dbu.getCurrentUser().getStudy(),subject));
                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                        notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());
                        dbn.addNotif(new NotifListChildItem(dbn.allNotifs(type,study).size(),id,typeN,type,study,subject,body.substring(17),1,true,getTime(),getDate()));
                    }

                    break;
                case 1:

                    break;
                case 2:

                    break;
                case 3:

                    break;
                case 4:

                    break;
                case 5:

                    break;
                case 6:

                    break;
                case 7:
//                    Toast.makeText(this,"777777777",Toast.LENGTH_LONG).show();

                        notificationBuilder.setContentTitle("المعهد الإلكتروني");
                        notificationBuilder.setContentText(body.substring(14));
                        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
                        NotificationManager notificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                        notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());
                        dbn.addNotif(new NotifListChildItem(dbn.allNotifs().size(), id, typeN, type, study,typeN, body.substring(14), 0, true, getTime(), getDate()));

                    break;
            }

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