package com.ei.zezoo;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikepenz.materialdrawer.model.ExpandableBadgeDrawerItem;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PersonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonFragment extends Fragment implements FragmentCommunicator, DatePickerDialog.OnDateSetListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Context context;
    Resources res;
    View view;
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    SharedPreferences pref;
    TextView txtDate, txtName, bill, physics, chemis, maths, english, name, father, mother, place, date, id, from, phone, telephone, email, user, study, profileName, dialogT;
    EditText nameE;
    EditText fatherE;
    EditText motherE;
    EditText placeE;
    TextView dateE;
    EditText idE;
    EditText fromE;
    EditText phoneE;
    EditText telephoneE;
    EditText emailE;
    EditText userE;
    Button btn_edit;
    CircleImageView gProfile, profileImage, profileE;
    ImageView profileEdit;
    Typeface tf;
    UserSQLDatabaseHandler dbu;
    UserListChildItem u;
    private FirebaseAuth mauth;
    private DatabaseReference ref;
    private FirebaseUser currentuser;
    private String userId;
    private StorageReference userprofileimagereference;
    ProgressBar loadingProfile;
    FrameLayout loadingFrame;
    AsyncTask<Void, Void, String> setUserInfo;
    boolean isConnectAnimationStarted = false;
    Menu men;
    SharedPreference preference;
    MenuItem menuConnected;
    private ProgressDialog Loading;
    TableLayout tableSubjects;
    ViewGroup parent;
    LessonSQLDatabaseHandler dbl;
    LinearLayout dateBE;
    private boolean isImageSelected = false;
    private Uri imageUri;
    private boolean isEdit = false;

    // TODO: Rename and change types and number of parameters
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
     //   ((UserActivity) getActivity()).fragmentCommunicator = this;

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
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy", Locale.US);
        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        dateE.setText(simpleDateFormat.format(calendar.getTime()));
    }

    public static PersonFragment newInstance(String param1, String param2) {
        PersonFragment fragment = new PersonFragment();
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
        view = inflater.inflate(R.layout.fragment_person, container, false);
        context = view.getContext();
        res = getResources();
        parent = container;
        mauth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();
        currentuser = mauth.getCurrentUser();
        userId = currentuser.getUid();
        Loading = new ProgressDialog(context);
        userprofileimagereference = FirebaseStorage.getInstance().getReference().child("Profile Images");
        setHasOptionsMenu(true);
        pref = context.getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        dbu = new UserSQLDatabaseHandler(context);
        dbl = new LessonSQLDatabaseHandler(context);
        u = dbu.getCurrentUser();
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_settings);
        tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/font_1.ttf");
        loadingFrame = (FrameLayout) view.findViewById(R.id.loadingFrame);
        TabItem tab1 = view.findViewById(R.id.person_tab1);
        TabItem tab2 = view.findViewById(R.id.person_tab2);
        TabItem tab3 = view.findViewById(R.id.person_tab3);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (ViewPager) view.findViewById(R.id.pager_settings);
        adapter = new ViewPagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        setUserInfo = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... urls) {
                String s = "";
                u = dbu.getCurrentUser();
                try {
                    if (viewPager.getCurrentItem() == 0) {
                        txtName.setText(u.getName());
                        user.setText(u.getUser());
                        study.setText(ElecUtils.getStudy(u.getType(),u.getStudy()));
                        txtDate.setText(u.getDate());
                        bill.setText(u.getBill());
                        gProfile.setImageBitmap(u.getProfile());
                    } else if (viewPager.getCurrentItem() == 1) {
                        name.setText(u.getName());
                        father.setText(u.getFather());
                        mother.setText(u.getMother());
                        place.setText(u.getPlace());
                        date.setText(u.getDate());
                        id.setText(u.getId());
                        from.setText(u.getFrom());
                    } else {
                        phone.setText(u.getPhone());
                        telephone.setText(u.getTelephone());
                        email.setText(u.getEmail());
                    }
                } catch (Exception e) {
                    s = e.getMessage();
                }
                return "";
            }

            @Override
            protected void onPostExecute(String result) {
                loadingFrame.setVisibility(View.GONE);
            }
        };
        setUserInfo.execute();
        return view;
    }

    String sname;
    String simage;
    private Bitmap my_image;

    /**
     * private void RetrieveUserInfo() {
     * ref.child("Users").child(userId).addValueEventListener(new ValueEventListener() {
     *
     * @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
     * if (dataSnapshot.exists()) {
     * String sbirthday = dataSnapshot.child("birthday").getValue().toString();
     * String sdate = dataSnapshot.child("date").getValue().toString();
     * String stime = dataSnapshot.child("time").getValue().toString();
     * String semail = dataSnapshot.child("email").getValue().toString();
     * String sfather = dataSnapshot.child("father").getValue().toString();
     * String smother = dataSnapshot.child("mother").getValue().toString();
     * String sfrom = dataSnapshot.child("from").getValue().toString();
     * String sid = dataSnapshot.child("id").getValue().toString();
     * //simage = dataSnapshot.child("image").getValue().toString();
     * sname = dataSnapshot.child("name").getValue().toString();
     * String snum = dataSnapshot.child("num").getValue().toString();
     * String snum_ = dataSnapshot.child("num_").getValue().toString();
     * String splace = dataSnapshot.child("place").getValue().toString();
     * String sstudy = dataSnapshot.child("study").getValue().toString();
     * String suid = dataSnapshot.child("uid").getValue().toString();
     * String sbill = dataSnapshot.child("bill").getValue().toString();
     * ArrayList<String> studyList = new ArrayList<>();
     * String studies = dataSnapshot.child("studies").getValue().toString();
     * if (!studies.contains("-")) {
     * studyList.add(sn.getBNameFromSubject(Integer.valueOf(studies)));
     * } else {
     * while (studies.contains("-") || studies.contains("&")) {
     * String a = "";
     * if (studies.contains("-"))
     * a = studies.substring(0, studies.indexOf("-") - 1);
     * else
     * a = studies.substring(0, studies.indexOf("&") - 1);
     * studyList.add(sn.getBNameFromSubject(Integer.valueOf(a)));
     * if (studies.contains("-"))
     * studies = studies.substring(studies.indexOf("-") + 1);
     * else
     * studies = "";
     * }
     * }
     * u.setStudies(studyList);
     * u.setName(sname);
     * u.setFather(sfather);
     * u.setMother(smother);
     * u.setId(sid);
     * u.setFrom(sfrom);
     * u.setPlace(splace);
     * u.setBirthday(sbirthday);
     * u.setPhone(snum);
     * u.setTelephone(snum_);
     * u.setEmail(semail);
     * u.setUser(semail);
     * u.setStudy(Integer.valueOf(sstudy));
     * u.setDate(sdate);
     * u.setTime(stime);
     * u.setBill(sbill);
     * u.setBills(sbill);
     * if(viewPager.getCurrentItem() == 0)
     * loadingProfile.setVisibility(View.VISIBLE);
     * StorageReference refe = FirebaseStorage.getInstance().getReference().child("Profile Images/" + suid + ".jpg");
     * try {
     * final File localFile = File.createTempFile("Images", "jpg");
     * refe.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
     * @Override public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
     * my_image = BitmapFactory.decodeFile(localFile.getAbsolutePath());
     * u.setProfile(my_image);
     * if (viewPager.getCurrentItem() == 0) {
     * gProfile.setImageBitmap(my_image);
     * loadingProfile.setVisibility(View.GONE);
     * }
     * dbu.updateUser(u);
     * }
     * }).addOnFailureListener(new OnFailureListener() {
     * @Override public void onFailure(@NonNull Exception e) {
     * Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
     * loadingProfile.setVisibility(View.GONE);
     * }
     * });
     * try{
     * txtName.setText(u.getName());
     * user.setText(u.getUser());
     * study.setText(sn.getNameFromSubject(u.getStudy()));
     * txtDate.setText(u.getDate());
     * bill.setText(u.getBill());
     * }catch(Exception e){
     * <p>
     * }
     * try{
     * name.setText(u.getName());
     * father.setText(u.getFather());
     * mother.setText(u.getMother());
     * place.setText(u.getPlace());
     * date.setText(u.getDate());
     * id.setText(u.getId());
     * from.setText(u.getFrom());
     * }catch(Exception e){
     * <p>
     * }
     * try{
     * phone.setText(u.getPhone());
     * telephone.setText(u.getTelephone());
     * email.setText(u.getEmail());
     * }catch(Exception e){
     * <p>
     * }
     * } catch (IOException e) {
     * e.printStackTrace();
     * }
     * dbu.updateUser(u);
     * loadingFrame.setVisibility(View.GONE);
     * } else {
     * //                    name.setVisibility(View.VISIBLE);
     * Toast.makeText(context, "لا يوجد معلومات يرجى مراجعة المعهد!", Toast.LENGTH_LONG).show();
     * ((UserActivity) getActivity()).closeApplication();
     * <p>
     * }
     * }
     * @Override public void onCancelled(@NonNull DatabaseError databaseError) {
     * <p>
     * }
     * });
     * }
     **/

    class ViewPagerAdapter extends PagerAdapter {
        private int currentPage = 0;


        public final int getCurrentPage() {
            return currentPage;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);

        }

        @Override
        public void finishUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == ((View) arg1);

        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public Parcelable saveState() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object instantiateItem(View collection, int position) {

            LayoutInflater inflater = (LayoutInflater) collection.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            int resId = 0;
            switch (position) {
                case 0:
                    resId = R.layout.fragment_person_instiute;
                    View view = inflater.inflate(resId, null);
                    ScrollView li = (ScrollView) view.findViewById(R.id.scrollPersonInstiute);
                    pref = context.getSharedPreferences("PREFERENCE", MODE_PRIVATE);
                    txtName = (TextView) li.findViewById(R.id.txt_name);
                    gProfile = (CircleImageView) li.findViewById(R.id.set_profile_image);
                    user = (TextView) li.findViewById(R.id.txtUser);
                    study = (TextView) li.findViewById(R.id.txtStudy);
                    txtDate = (TextView) li.findViewById(R.id.txtDateS);
                    tableSubjects = (TableLayout) li.findViewById(R.id.subjectsTable);
                    // physics = (TextView) li.findViewById(R.id.txtPhysics);
                    //  chemis = (TextView) li.findViewById(R.id.txtChemis);
                    //  maths = (TextView) li.findViewById(R.id.txtMaths);
                    //  english = (TextView) li.findViewById(R.id.txtEnglish);
                    bill = (TextView) li.findViewById(R.id.txtBill);
                    loadingProfile = (ProgressBar) li.findViewById(R.id.loading);
                    txtName.setTypeface(tf);
                    txtName.setText(u.getName());
                    gProfile.setImageBitmap(u.getProfile());
                    user.setText(u.getUser());
                    study.setText(ElecUtils.getStudy(u.getType(), u.getStudy()));
                    txtDate.setText(u.getDate());
                    bill.setText(u.getBill());
                    ArrayList<Integer> subjects = u.getStudies();
                    for (int i = 0; i < subjects.size(); i++) {
                                   final int g = i;
                        LayoutInflater inflater1 = ((Activity) context).getLayoutInflater();
                        TableRow r = (TableRow) inflater1.inflate(R.layout.row_subject, parent, false);
                        ImageView rowImage = r.findViewById(R.id.rowImage);
                        TextView rowName = r.findViewById(R.id.rowName);
                        TextView rowNumber = r.findViewById(R.id.rowNumber);
                        rowImage.setImageResource(ElecUtils.getResourceFromSubject(u.getType(), u.getStudy(), subjects.get(i)));
                        rowName.setText(ElecUtils.getSubject(u.getType(), u.getStudy(), subjects.get(i)));
                        int numLessons = dbl.allLessons(u.getType(), u.getStudy(), ElecUtils.getSubject(u.getType(), u.getStudy(), subjects.get(i))).size();
                        if (numLessons == 0)
                            rowNumber.setText("لا يوجد دروس");
                        else if (numLessons == 1)
                            rowNumber.setText("درس");
                        else if (numLessons == 2)
                            rowNumber.setText("درسين");
                        else if (numLessons > 2)
                            rowNumber.setText(String.valueOf(numLessons) + " " + "دروس");
                        r.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ((UserActivity) getActivity()).result.openDrawer();
                            //   ExpandableBadgeDrawerItem exp= (ExpandableBadgeDrawerItem)((UserActivity) getActivity()).result.getDrawerItem((String)ElecUtils.getSubject(u.getType(), u.getStudy(), subjects.get(g)));
                          //  exp.withIsExpanded(true);
                            }
                        });
                        tableSubjects.addView(r,i);

                    }
                    gProfile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final Dialog d = new Dialog(context);
                            d.setContentView(R.layout.dialog_profile_image);
                            d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            profileName = (TextView) d.findViewById(R.id.txtProfilePhoto);
                            profileImage = (CircleImageView) d.findViewById(R.id.set_profile_image_);
                            profileEdit = (ImageView) d.findViewById(R.id.imgProfileEdit);
                            profileName.setText(sname);
                            profileImage.setImageBitmap(u.getProfile());
                            profileName.setTypeface(tf);
                            profileEdit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ((UserActivity) getActivity()).isStartAnotherActivity = true;
                                    ((UserActivity) getActivity()).isPicImages = true;
                                    isEdit = false;
                                    CropImage.activity()
                                            .setGuidelines(CropImageView.Guidelines.ON)
                                            .setAspectRatio(1, 1)
                                            .start(getActivity());
                                }
                            });
                            d.setCanceledOnTouchOutside(true);
                            d.show();
                        }
                    });

                    ((ViewPager) collection).addView(view, 0);
                    currentPage = 0;
                    if (setUserInfo.isCancelled())
                        setUserInfo.execute();
                    return view;
                case 1:
                    resId = R.layout.fragment_person_details;
                    View view_ = inflater.inflate(resId, null);
                    ScrollView lt = (ScrollView) view_.findViewById(R.id.scrollPersonDetails);
                    pref = context.getSharedPreferences("PREFERENCE", MODE_PRIVATE);
                    name = (TextView) lt.findViewById(R.id.txtName);
                    father = (TextView) lt.findViewById(R.id.txtFather);
                    mother = (TextView) lt.findViewById(R.id.txtMother);
                    place = (TextView) lt.findViewById(R.id.txtPlace);
                    date = (TextView) lt.findViewById(R.id.txtDate);
                    id = (TextView) lt.findViewById(R.id.txtId);
                    from = (TextView) lt.findViewById(R.id.txtFrom);
                    name.setText(u.getName());
                    father.setText(u.getFather());
                    mother.setText(u.getMother());
                    place.setText(u.getPlace());
                    date.setText(u.getDate());
                    id.setText(u.getId());
                    from.setText(u.getFrom());

                    ((ViewPager) collection).addView(view_, 0);
                    currentPage = 1;
                    if (setUserInfo.isCancelled())
                        setUserInfo.execute();
                    return view_;
                case 2:
                    resId = R.layout.fragment_person_contact;
                    View view1 = inflater.inflate(resId, null);
                    ScrollView lp = (ScrollView) view1.findViewById(R.id.scrollPersonContact);
                    pref = context.getSharedPreferences("PREFERENCE", MODE_PRIVATE);
                    phone = (TextView) lp.findViewById(R.id.txtPhone);
                    telephone = (TextView) lp.findViewById(R.id.txtTelephone);
                    email = (TextView) lp.findViewById(R.id.txtEmail);
                    phone.setText(u.getPhone());
                    telephone.setText(u.getTelephone());
                    email.setText(u.getEmail());
                    ((ViewPager) collection).addView(view1, 0);
                    currentPage = 2;
                    if (setUserInfo.isCancelled())
                        setUserInfo.execute();
                    return view1;
            }
            return view;
        }
    }

    public static boolean isRTL() {
        return isRTL(Locale.getDefault());
    }

    public static boolean isRTL(Locale locale) {
        final int directionality = Character.getDirectionality(locale.getDisplayName().charAt(0));
        return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT ||
                directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                isImageSelected = true;
                Uri imageUri = result.getUri();
                this.imageUri = imageUri;
                Picasso.get().load(imageUri).into(gProfile);
                Picasso.get().load(imageUri).into(profileImage);
                ((UserActivity) getActivity()).isStartAnotherActivity = false;
                ((UserActivity) getActivity()).isPicImages = false;
                if (!isEdit) {
                    StorageReference file = userprofileimagereference.child(userId + ".jpg");
                    file.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                //Toast.makeText(SignActivity.this, "الصورة الشخصية تم تحديدها بنجاح", Toast.LENGTH_LONG).show();
                                final String downloadurl = task.getResult().getMetadata().getReference().getDownloadUrl().toString();
                                /**ref.child("Users").child(userId).child("image").setValue(downloadurl)
                                 .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                //   Toast.makeText(SignActivity.this, "Image saved in database,successfully...", Toast.LENGTH_LONG).show();

                                } else {
                                String message = task.getException().toString();
                                Toast.makeText(SignActivity.this, "Error : " + message, Toast.LENGTH_LONG).show();
                                }
                                }
                                });**/
                                ref.child("Users").child(userId).child("image").setValue(userId);
                            } else {
                                String message = task.getException().toString();
                                Toast.makeText(context, "Error : " + message, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_user, menu);
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
        item.setCheckable(false);// Do not accept any click events while scanning
        refreshActionView.startAnimation(rotateAni);
        isConnectAnimationStarted = true;
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
                        ((UserActivity) getActivity()).prepareVpn();
                }
                return true;
            case R.id.menu_settings:
                UserActivity m = (UserActivity) getActivity();
                m.result.setSelection(m.settings);
                return true;
            case R.id.menu_logout:
                ref.child("Users").child(userId).child("isNow").setValue("0");
                ref.child("Users").child(userId).child("status").setValue(getDate() + "|" + getTime());
                ((UserActivity) getActivity()).logOut();
                return true;
            case R.id.menu_u_edit:
                createrDialogEdit();
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



    private void createrDialogEdit() {
        final Dialog d = new Dialog(context);
        d.setContentView(R.layout.dialog_profile_edit);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        profileE = (CircleImageView) d.findViewById(R.id.set_profile_image__);
        nameE = (EditText) d.findViewById(R.id.txtName);
        fatherE = (EditText) d.findViewById(R.id.txtFather);
        motherE = (EditText) d.findViewById(R.id.txtMother);
        placeE = (EditText) d.findViewById(R.id.txtPlace);
        dateE = (TextView) d.findViewById(R.id.txtDate);
        dateBE = (LinearLayout) d.findViewById(R.id.btnDate);
        idE = (EditText) d.findViewById(R.id.txtId);
        fromE = (EditText) d.findViewById(R.id.txtFrom);
        phoneE = (EditText) d.findViewById(R.id.txtPhone);
        telephoneE = (EditText) d.findViewById(R.id.txtTelephone);
        emailE = (EditText) d.findViewById(R.id.txtEmail);
        btn_edit = (Button) d.findViewById(R.id.btn_edit);
        btn_edit.setTypeface(tf);
        nameE.setText(u.getName());
        fatherE.setText(u.getFather());
        motherE.setText(u.getMother());
        placeE.setText(u.getPlace());
        dateE.setText(u.getDate());
        idE.setText(u.getId());
        fromE.setText(u.getFrom());
        phoneE.setText(u.getPhone());
        telephoneE.setText(u.getTelephone());
        emailE.setText(u.getEmail());
        profileE.setImageBitmap(u.getProfile());
        dateBE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SpinnerDatePickerDialogBuilder()
                        .context(context)
                        .callback(PersonFragment.this)
                        .spinnerTheme(R.style.NumberPickerStyle)
                        .showTitle(true)
                        .showDaySpinner(true)
                        .defaultDate(2017, 0, 1)
                        .maxDate(2020, 0, 1)
                        .minDate(2000, 0, 1)
                        .build()
                        .show();
            }
        });
        profileE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((UserActivity) getActivity()).isStartAnotherActivity = true;
                ((UserActivity) getActivity()).isPicImages = true;
                isEdit = true;
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(getActivity());
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref.child("Users").child(userId).child("name").setValue(nameE.getText().toString());
                ref.child("Users").child(userId).child("father").setValue(fatherE.getText().toString());
                ref.child("Users").child(userId).child("mother").setValue(motherE.getText().toString());
                ref.child("Users").child(userId).child("place").setValue(placeE.getText().toString());
                ref.child("Users").child(userId).child("id").setValue(idE.getText().toString());
                ref.child("Users").child(userId).child("birthday").setValue(dateE.getText().toString());
                ref.child("Users").child(userId).child("from").setValue(fromE.getText().toString());
                ref.child("Users").child(userId).child("num").setValue(phoneE.getText().toString());
                ref.child("Users").child(userId).child("num_").setValue(telephoneE.getText().toString());
                ref.child("Users").child(userId).child("email").setValue(emailE.getText().toString());
                if (isImageSelected) {
                    StorageReference file = userprofileimagereference.child(userId + ".jpg");
                    file.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                //Toast.makeText(SignActivity.this, "الصورة الشخصية تم تحديدها بنجاح", Toast.LENGTH_LONG).show();
                                final String downloadurl = task.getResult().getMetadata().getReference().getDownloadUrl().toString();
                                /**ref.child("Users").child(userId).child("image").setValue(downloadurl)
                                 .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                //   Toast.makeText(SignActivity.this, "Image saved in database,successfully...", Toast.LENGTH_LONG).show();

                                } else {
                                String message = task.getException().toString();
                                Toast.makeText(SignActivity.this, "Error : " + message, Toast.LENGTH_LONG).show();
                                }
                                }
                                });**/
                                ref.child("Users").child(userId).child("image").setValue(userId);
                            } else {
                                String message = task.getException().toString();
                                Toast.makeText(context, "Error : " + message, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

            }
        });
        d.setCanceledOnTouchOutside(false);
        d.show();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(isConnectAnimationStarted)
            stopRotateAni();

    }
}