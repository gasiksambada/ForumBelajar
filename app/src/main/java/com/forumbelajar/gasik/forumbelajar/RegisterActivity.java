package com.forumbelajar.gasik.forumbelajar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Calendar;

/**
 * Created by Gasik on 6/17/2016.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    TextView title;
    Button bRegister;
    Firebase accountRef;
    String vUsername,vPassword,vIdLPIA,vTempatLahir,vNoTelp,vAlamat,vTanggalLahir;
    ProgressDialog progress;
    ImageView bButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);

        setContentView(R.layout.frag_register);
        title = (TextView) findViewById(R.id.title);
        title.setText("REGISTER");

        bRegister = (Button) findViewById(R.id.submit_register);
        bRegister.setOnClickListener(this);

        bButton = (ImageView) findViewById(R.id.back_button);
        bButton.setVisibility(View.VISIBLE);
        bButton.setOnClickListener(this);

        Firebase.setAndroidContext(this);

        final EditText iTanggalLahir = (EditText) findViewById(R.id.tanggal_lahir);
        if(iTanggalLahir != null){
            iTanggalLahir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar mcurrentDate= Calendar.getInstance();
                    int mYear = mcurrentDate.get(Calendar.YEAR);
                    int mMonth = mcurrentDate.get(Calendar.MONTH);
                    int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog mDatePicker=new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                            iTanggalLahir.setText(""+selectedday+"/"+selectedmonth+"/"+selectedyear);
                        }
                    },mYear, mMonth, mDay);
                    mDatePicker.getDatePicker().setCalendarViewShown(false);
                    mDatePicker.setTitle("Select date");
                    mDatePicker.show();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit_register:
                saveData();
                break;
            case R.id.back_button:
                finish();
                break;
        }
    }

    private boolean saveData(){
        accountRef = new Firebase("https://forum-belajar.firebaseio.com/accounts");

        EditText iUsername = (EditText) findViewById(R.id.username);
        EditText iPassword = (EditText) findViewById(R.id.password);
        EditText iIdLPIA = (EditText) findViewById(R.id.no_id);
        EditText iTempatLahir = (EditText) findViewById(R.id.tempat_lahir);
        EditText iNoTelp = (EditText) findViewById(R.id.no_telp);
        EditText iAlamat = (EditText) findViewById(R.id.alamat);
        EditText iTanggalLahir = (EditText) findViewById(R.id.tanggal_lahir);

        vUsername = iUsername.getText().toString();
        vPassword = iPassword.getText().toString();
        vIdLPIA = iIdLPIA.getText().toString();
        vTempatLahir = iTempatLahir.getText().toString();
        vNoTelp = iNoTelp.getText().toString();
        vAlamat = iAlamat.getText().toString();
        vTanggalLahir = iTanggalLahir.getText().toString();
        if(vUsername.equals("")){
            alertBox("Please input username!");
            iUsername.requestFocus();
            return false;
        }
        if(vPassword.equals("")){
            alertBox("Please input password!");
            iPassword.requestFocus();
            return false;
        }
        if(vIdLPIA.equals("")){
            alertBox("Please input Id LPIA");
            iIdLPIA.requestFocus();
            return false;
        }

        accountRef.child(vUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            Boolean sLoading = loading(true);
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){
                    Firebase accountData = accountRef.child(vUsername);
                    accountData.child("password").setValue(vPassword);
                    accountData.child("id_lpia").setValue(vIdLPIA);
                    accountData.child("tempat_lahir").setValue(vTempatLahir);
                    accountData.child("no_telp").setValue(vNoTelp);
                    accountData.child("alamat").setValue(vAlamat);
                    accountData.child("tanggal_lahir").setValue(vTanggalLahir);

                    loading(false);
                    Toast.makeText(RegisterActivity.this, "Success register account!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }else {
                    loading(false);
                    alertBox("Username "+dataSnapshot.getKey().toString()+" already register, please use another one!");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                alertBox("Error!");
            }
        });

        return false;
    }

    public void alertBox(String message) {
        AlertDialog.Builder alertBox = new AlertDialog.Builder(this);
        alertBox.setMessage(message);
        alertBox.setCancelable(true);

        alertBox.setNegativeButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = alertBox.create();
        alert.show();
    }

    public boolean loading(Boolean status) {
        if(status){
            progress = new ProgressDialog(this);
            progress.setCancelable(false);
            progress.setTitle("Loading");
            progress.setMessage("Wait while loading...");
            progress.show();
        }else{
            progress.dismiss();
        }

        return false;
    }
}
