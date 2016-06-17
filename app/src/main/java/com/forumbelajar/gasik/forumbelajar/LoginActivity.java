package com.forumbelajar.gasik.forumbelajar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by Gasik on 6/17/2016.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    TextView title;
    Firebase accountRef,photoRef,pointRef;
    String vUsername,vPassword,fPassword;
    Button bLogin;
    ImageView bButton;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    ProgressDialog progress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);

        setContentView(R.layout.frag_login);
        title = (TextView) findViewById(R.id.title);
        title.setText("LOGIN");

        bLogin = (Button) findViewById(R.id.submit_login);
        bLogin.setOnClickListener(this);

        bButton = (ImageView) findViewById(R.id.back_button);
        bButton.setVisibility(View.VISIBLE);
        bButton.setOnClickListener(this);

        Firebase.setAndroidContext(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit_login:
                loginData();
                break;
            case R.id.back_button:
                finish();
                break;
        }
    }

    private boolean loginData() {
        EditText iUsername = (EditText) findViewById(R.id.username);
        final EditText iPassword = (EditText) findViewById(R.id.password);
        vUsername = iUsername.getText().toString();
        vPassword = iPassword.getText().toString();
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

        accountRef = new Firebase("https://forum-belajar.firebaseio.com/accounts/"+vUsername);
        accountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            Boolean sLoading = loading(true);
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("password").getValue() != null) {
                    fPassword = dataSnapshot.child("password").getValue().toString();
                    if(fPassword.equals(vPassword)){
                        photoRef = new Firebase("https://forum-belajar.firebaseio.com/photos/"+vUsername);
                        photoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.child("profile_picture").getValue() != null){
                                    String vPpic = dataSnapshot.child("profile_picture").getValue().toString();
                                    if (vPpic != "") {
                                        createSession("profile_picture",vPpic);
                                    }
                                }
                                if(dataSnapshot.child("background_timeline").getValue() != null){
                                    String vTbackground = dataSnapshot.child("background_timeline").getValue().toString();
                                    if (vTbackground != "") {
                                        createSession("background_timeline",vTbackground);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                                alertBox("Error!");
                            }
                        });
                        pointRef = new Firebase("https://forum-belajar.firebaseio.com/points/"+vUsername);
                        pointRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.child("question").getValue() != null){
                                    String vPquestion = dataSnapshot.child("question").getValue().toString();
                                    if (vPquestion != "") {
                                        createSession("point_question",vPquestion);
                                    }
                                }
                                if(dataSnapshot.child("answer").getValue() != null){
                                    String vPanswer = dataSnapshot.child("answer").getValue().toString();
                                    if (vPanswer != "") {
                                        createSession("point_answer",vPanswer);
                                    }
                                }
                                if(dataSnapshot.child("right_answer").getValue() != null){
                                    String vPRanswer = dataSnapshot.child("right_answer").getValue().toString();
                                    if (vPRanswer != "") {
                                        createSession("point_right_answer",vPRanswer);
                                    }
                                }
                                if(dataSnapshot.child("score").getValue() != null){
                                    String vPscore = dataSnapshot.child("score").getValue().toString();
                                    if (vPscore != "") {
                                        createSession("point_score",vPscore);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                                alertBox("Error!");
                            }
                        });
                        loading(false);
                        createSession("username",vUsername);
                        Toast.makeText(LoginActivity.this, "Login success!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, SecondActivity.class);
                        startActivity(intent);
                    }else{
                        loading(false);
                        alertBox("Wrong password!");
                    }
                }else{
                    loading(false);
                    alertBox("Username not found!");
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

    public void createSession(String key, String Value) {
        sharedpreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, Value);
        editor.commit();
    }
}
