package com.forumbelajar.gasik.forumbelajar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.utilities.Base64;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gasik on 6/10/2016.
 */
public class AddquestionActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int RESULT_LOAD_IMAGE = 1;
    ImageView photo1,photo2;
    Button submit_question;
    String vTitleQuestion,vQuestion,vUsername,vPhoto1,vPhoto2,vPquestion,vPscore;
    EditText iTitleQuestion,iQuestion;
    Firebase questionRef,photoRef,pointRef;
    int photoid,iPquestion,iPscore;

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addquestion_activity);
        setTitle("Add Question");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        photo1 = (ImageView) this.findViewById(R.id.photo1);
        photo2 = (ImageView) this.findViewById(R.id.photo2);
        photo1.setOnClickListener(this);
        photo2.setOnClickListener(this);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        vUsername = sharedpreferences.getString("username", "");
        vPquestion = sharedpreferences.getString("point_question", "");
        vPscore = sharedpreferences.getString("point_score", "");

        iTitleQuestion = (EditText) this.findViewById(R.id.title_question);
        iQuestion = (EditText) this.findViewById(R.id.question);

        submit_question = (Button) this.findViewById(R.id.submit_question);
        submit_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Please wait...", Toast.LENGTH_LONG).show();
                vTitleQuestion = iTitleQuestion.getText().toString();
                vQuestion = iQuestion.getText().toString();
                questionRef = new Firebase("https://forum-belajar.firebaseio.com/questions/");

                Long dateCreated = System.currentTimeMillis()/1000;
                Long Priority = 0-(dateCreated);

                Map<String, String> dataQuestion = new HashMap<>();
                dataQuestion.put("title", vTitleQuestion);
                dataQuestion.put("question", vQuestion);
                dataQuestion.put("username", vUsername);
                dataQuestion.put("datecreated", dateCreated.toString());
                Firebase question = questionRef.push();
                question.setValue(dataQuestion);
                question.setPriority(Priority);

                String IdQuestion = question.getKey();
                photoRef = new Firebase("https://forum-belajar.firebaseio.com/photos/"+IdQuestion);
                vPhoto1 = convertImg(R.id.photo1);
                photoRef.child("photo_1").setValue(vPhoto1);

                vPhoto2 = convertImg(R.id.photo2);
                photoRef.child("photo_2").setValue(vPhoto2);

                pointRef = new Firebase("https://forum-belajar.firebaseio.com/points/"+vUsername);

                iPquestion = Integer.parseInt(vPquestion)+1;
                vPquestion = Integer.toString(iPquestion);
                pointRef.child("question").setValue(iPquestion);
                createSession("point_question",vPquestion);

                iPscore = Integer.parseInt(vPscore)+2;
                vPscore = Integer.toString(iPscore);    
                pointRef.child("score").setValue(iPscore);
                createSession("point_score",vPscore);

                Toast.makeText(v.getContext(), "Success add question", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddquestionActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE &&  data != null){
            Uri selectedImage = data.getData();
            ImageView photo = (ImageView) this.findViewById(photoid);
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

    @Override
    public void onClick(View v) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        switch (v.getId() /*to get clicked view id**/) {
            case R.id.photo1:
                photoid = R.id.photo1;
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                break;
            case R.id.photo2:
                photoid = R.id.photo2;
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                break;
            default:
                break;
        }
    }

    public String convertImg(int Resources) {
        ImageView iPhoto = (ImageView)findViewById(Resources);
        BitmapDrawable drawable = (BitmapDrawable) iPhoto.getDrawable();
        if(drawable != null){
            Bitmap bitmap = drawable.getBitmap();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,bos);
            byte[] bb = bos.toByteArray();
            String vPhoto = Base64.encodeBytes(bb);
            return vPhoto;
        }
        return "";
    }

    public void createSession(String key, String Value) {
        sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, Value);
        editor.commit();
    }
}
