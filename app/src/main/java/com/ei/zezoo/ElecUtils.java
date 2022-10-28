package com.ei.zezoo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ElecUtils {

    static String getType(int type) {
        String t = "";
        String ty = String.valueOf(type).substring(0, 2);
        int typ = Integer.valueOf(ty);
        switch (typ) {
            case 100:
                t = "مناهج التربوية السورية";
                break;
            case 101:
                t = "برمجة وتطوير";
                break;
            case 102:
                t = "التصميم والرسم";
                break;
            case 103:
                t = "اللغة الإنكليزية";
                break;
            case 104:
                t = "المهن والحرف";
                break;
            default:
                t = "مناهج التربوية السورية";
                break;
        }
        return t;
    }

    static int getType(String type) {
        int t = 0;
        if (type.matches("مناهج التربوية السورية"))
            t = 100;
        else if (type.matches("برمجة وتطوير"))
            t = 101;
        else if (type.matches("التصميم والرسم"))
            t = 102;
        else if (type.matches("اللغة الإنكليزية"))
            t = 103;
        else if (type.matches("المهن والحرف"))
            t = 104;
        return t;
    }

    static String getStudy(int type, int study) {
        String s = "";
        //    String ty = String.valueOf(study).substring(0,2);
        //      int typ = Integer.valueOf(ty);
//        String st = String.valueOf(study).substring(3,5);
        // int sty = Integer.valueOf(st);
        if (type == 100) {
            switch (study) {
                case 100:
                    s = "الصف السابع";
                    break;
                case 101:
                    s = "الصف الثامن";
                    break;
                case 102:
                    s = "الصف التاسع";
                    break;
                case 103:
                    s = "الصف العاشر علمي";
                    break;
                case 104:
                    s = "الصف العاشر أدبي";
                    break;
                case 105:
                    s = "الصف الحادي عشر علمي";
                    break;
                case 106:
                    s = "الصف الحادي عشر أدبي";
                    break;
                case 107:
                    s = "بكالوريا علمي";
                    break;
                case 108:
                    s = "بكالوريا أدبي";
                    break;
            }
        }
        return s;
    }

    static int getStudy(int type, String study) {
        int s = 0;
        if (type == 100) {
            if (study.matches("الصف السابع"))
                s = 100;
            else if (study.matches("الصف الثامن"))
                s = 101;
            else if (study.matches("الصف التاسع"))
                s = 102;
            else if (study.matches("الصف العاشر علمي"))
                s = 103;
            else if (study.matches("الصف العاشر أدبي"))
                s = 104;
            else if (study.matches("الصف الحادي عشر علمي"))
                s = 105;
            else if (study.matches("الصف الحادي عشر أدبي"))
                s = 106;
            else if (study.matches("بكالوريا علمي"))
                s = 107;
            else if (study.matches("بكالوريا أدبي"))
                s = 108;
        }
        return s;
    }

    static String getSubject(int type, int study, int subject) {
        String s = "";
        if (type == 100) {
            switch (subject) {
                case 7 :
                    s = "إعلان";
                    break;
                case 100:
                    s = "رياضيات";
                    break;
                case 101:
                    s = "هندسة";
                    break;
                case 102:
                    s = "جبر";
                    break;
                case 103:
                    s = "تحليل";
                    break;
                case 104:
                    s = "فيزياء وكيمياء";
                    break;
                case 105:
                    s = "فيزياء";
                    break;
                case 106:
                    s = "كيمياء";
                    break;
                case 107:
                    s = "علم الأحياء";
                    break;
                case 108:
                    s = "لغة عربية";
                    break;
                case 109:
                    s = "لغة إنكليزية";
                    break;
                case 110:
                    s = "لغة فرنسية";
                    break;
                case 111:
                    s = "أجتماعيات";
                    break;
                case 112:
                    s = "تاريخ";
                    break;
                case 113:
                    s = "جغرافية";
                    break;
                case 114:
                    s = "وطنية";
                    break;
                case 115:
                    s = "فلسفة";
                    break;
                case 116:
                    s = "ديانة";
                    break;
                case 117:
                    s = "هندسة وجبر";
                    break;
                case 118:
                    s = "الدروس التعريفية المجانية";
                    break;
            }
        }
        return s;
    }

    static int getSubject(int type, int st, String study) {
        int s = 0;
        if (type == 100) {
            if (study.matches("رياضيات"))
                s = 100;
            else if (study.matches("هندسة"))
                s = 101;
            else if (study.matches("جبر"))
                s = 102;
            else if (study.matches("تحليل"))
                s = 103;
            else if (study.matches("فيزياء وكيمياء"))
                s = 104;
            else if (study.matches("فيزياء"))
                s = 105;
            else if (study.matches("كيمياء"))
                s = 106;
            else if (study.matches("علم الأحياء"))
                s = 107;
            else if (study.matches("لغة عربية"))
                s = 108;
            else if (study.matches("لغة إنكليزية"))
                s = 109;
            else if (study.matches("لغة فرنسية"))
                s = 110;
            else if (study.matches("أجتماعيات"))
                s = 111;
            else if (study.matches("تاريخ"))
                s = 112;
            else if (study.matches("جغرافية"))
                s = 113;
            else if (study.matches("وطنية"))
                s = 114;
            else if (study.matches("فلسفة"))
                s = 115;
            else if (study.matches("ديانة"))
                s = 116;
            else if (study.matches("هندسة وجبر"))
                s = 117;
            else if (study.matches("الدروس التعريفية المجانية"))
                s = 118;
        }
        return s;
    }

    static String getNamefromTypeN(int type){
        String typ = "";
        switch (type){
            case 0:
                typ = "الدروس الالكترونية";
                break;
            case 1:
                typ = "الدروس التعريفية";
                break;
            case 2:
                typ = "الأسئلة العامة";
                break;
            case 3:
                typ = "الاختبارات الالكترونية";
                break;
            case 4:
                typ = "الاختبارات العملية";
                break;
            case 5:
                typ = "جدول الدروس";
                break;
            case 6:
                typ = "جدول الاختبارات النظرية";
                break;
            case 7:
                typ = "إعلانات";
                break;
        }
        return typ;
    }

    static String getVideoDir(int type, int study, int subject, int lesson, boolean isFile) {
        if (isFile)
            return String.valueOf(type) + String.valueOf(study) + String.valueOf(subject) + String.valueOf(lesson)+".mp4";
        else
            return getType(type) + "/" + getStudy(type, study) + "/" + getSubject(type, study, subject) + "/" + String.valueOf(lesson)+".mp4";
    }


    static int getResourceFromSubject(int type, int study, int subject) {
        int id = 0;
        if (type == 100) {
            if (subject >= 100) {
                switch (getSubject(type, study, subject)) {
                    case "فيزياء وكيمياء":
                        id = R.drawable.physics;
                        break;
                    case "فيزياء":
                        id = R.drawable.physics;
                        break;
                    case "رياضيات":
                        id = R.drawable.maths;
                        break;
                    case "هندسة وجبر":
                        id = R.drawable.maths;
                        break;
                    case "هندسة":
                        id = R.drawable.maths;
                        break;
                    case "جبر":
                        id = R.drawable.algebra;
                    case "تحليل":
                        id = R.drawable.anatomy;
                        break;
                    case "لغة إنكليزية":
                        id = R.drawable.english;
                        break;
                    case "لغة عربية":
                        id = R.drawable.arabic;
                        break;
                    case "كيمياء":
                        id = R.drawable.chemistry;
                        break;
                }
            } else if (subject == 5)
                id = R.drawable.caleander;
            else if (subject == 7)
                id = R.drawable.ic_launcher;
        }
        return id;
    }
    static int getResourceFromSubject(Context context,String sub) {
        int id = 0;
        UserSQLDatabaseHandler dbu = new UserSQLDatabaseHandler(context);
        int type = dbu.getCurrentUser().getType();
        int study = dbu.getCurrentUser().getStudy();
        int subject = getSubject(type,study,sub);
        if (type == 100) {
            if (subject >= 100) {
                switch (getSubject(type, study, subject)) {
                    case "فيزياء وكيمياء":
                        id = R.drawable.physics;
                        break;
                    case "فيزياء":
                        id = R.drawable.physics;
                        break;
                    case "رياضيات":
                        id = R.drawable.maths;
                        break;
                    case "هندسة وجبر":
                        id = R.drawable.maths;
                        break;
                    case "هندسة":
                        id = R.drawable.maths;
                        break;
                    case "جبر":
                        id = R.drawable.algebra;
                    case "تحليل":
                        id = R.drawable.anatomy;
                        break;
                    case "لغة إنكليزية":
                        id = R.drawable.english;
                        break;
                    case "كيمياء":
                        id = R.drawable.chemistry;
                        break;
                }
            } else if (subject == 5)
                id = R.drawable.caleander;
            else if (subject == 7)
                id = R.drawable.ic_launcher;
        }
        return id;
    }


}
