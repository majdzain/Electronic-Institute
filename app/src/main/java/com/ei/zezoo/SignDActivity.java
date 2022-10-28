package com.ei.zezoo;

import android.graphics.Color;
import android.os.Bundle;

import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class SignDActivity extends AppCompatActivity {

    EditText edit_name,edit_num,edit_num_,edit_address,edit_email;
    Spinner spin_lang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_d);
        androidx.appcompat.app.ActionBar actionbar = getSupportActionBar();
        TextView textview = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        textview.setLayoutParams(layoutparams);
        textview.setText("دورات التصميم والرسم");
        textview.setTextColor(Color.WHITE);
        textview.setGravity(Gravity.RIGHT);
        textview.setTextSize(20);
        actionbar.setDisplayOptions(androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        actionbar.setCustomView(textview);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionSubmitSign);
        edit_name = (EditText) findViewById(R.id.edit_name);
        edit_num = (EditText) findViewById(R.id.edit_num_ph);
        edit_num_ = (EditText) findViewById(R.id.edit_num_ph_);
        edit_address = (EditText) findViewById(R.id.edit_place);
        edit_email = (EditText) findViewById(R.id.edit_email);
        spin_lang = (Spinner) findViewById(R.id.spin_lang);
        String study[] = {"Photoshop","Illistrator"};
        ArrayAdapter<CharSequence> spinnerCurrencyArrayAdapter = new ArrayAdapter<CharSequence>(SignDActivity.this, R.layout.spinner_item, study);
        spinnerCurrencyArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        spin_lang.setAdapter(spinnerCurrencyArrayAdapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                boolean isOk = true;
                if(!edit_name.toString().matches(".*[a-zA-Z0-9أ-ي]+.*")){
                    edit_name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                    isOk = false;
                }else  if(!edit_num.toString().matches(".*[a-zA-Z0-9أ-ي]+.*")){
                    edit_num.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                    isOk = false;
                }else  if(!edit_address.toString().matches(".*[a-zA-Z0-9أ-ي]+.*")){
                    edit_address.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_error, 0, 0, 0);
                    isOk = false;
                }
                if(isOk){

                }
            }
        });
    }

}
