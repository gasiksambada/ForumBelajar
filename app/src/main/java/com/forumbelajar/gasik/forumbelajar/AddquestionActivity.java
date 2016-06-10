package com.forumbelajar.gasik.forumbelajar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gasik on 6/10/2016.
 */
public class AddquestionActivity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE = 1;
    ImageView photo;
    Button upload_photo,submit_question;
    String vTitleQuestion,vQuestion,vUsername;
    EditText iTitleQuestion,iQuestion;
    Firebase accountRef;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addquestion_activity);
        setTitle("Add Question");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        upload_photo = (Button) this.findViewById(R.id.upload_photo);
        upload_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        vUsername = sharedpreferences.getString("username", "kosong");//"No name defined" is the default value.

        iTitleQuestion = (EditText) this.findViewById(R.id.title_question);
        iQuestion = (EditText) this.findViewById(R.id.question);

        submit_question = (Button) this.findViewById(R.id.submit_question);
        submit_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vTitleQuestion = iTitleQuestion.getText().toString();
                vQuestion = iQuestion.getText().toString();
                accountRef = new Firebase("https://forum-belajar.firebaseio.com/questions/");

                Map<String, String> dataQuestion = new HashMap<>();
                dataQuestion.put("title", vTitleQuestion);
                dataQuestion.put("question", vQuestion);
                dataQuestion.put("username", vUsername);
                accountRef.push().setValue(dataQuestion);
                Toast.makeText(v.getContext(), "Success add question", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE &&  data != null){
            Uri selectedImage = data.getData();
            photo = (ImageView) this.findViewById(R.id.photo);
            photo.setImageURI(selectedImage);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
