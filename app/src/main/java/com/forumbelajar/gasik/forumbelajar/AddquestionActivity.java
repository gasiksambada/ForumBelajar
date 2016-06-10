package com.forumbelajar.gasik.forumbelajar;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Gasik on 6/10/2016.
 */
public class AddquestionActivity extends ActionBarActivity {
    private static final int RESULT_LOAD_IMAGE = 1;
    ImageView photo;
    Button upload_photo,submit_question;
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
}
