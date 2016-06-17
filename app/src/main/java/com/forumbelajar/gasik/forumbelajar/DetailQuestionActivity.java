package com.forumbelajar.gasik.forumbelajar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailQuestionActivity extends AppCompatActivity implements View.OnClickListener {
    String IdQuestion, TitleQuestion, Question, Photo1, Photo2,sPhotoAnswer1,sPhotoAnswer2,vAnswer,vUsername,vPanswer,vPscore,vPRanswer,QcreatedBy,QRightAnswerId;
    Firebase questionRef,photoRef,answerRef,pointRef;
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;
    Bitmap decodedByte1,decodedByte2,decodedAnswerByte1,decodedAnswerByte2;
    ImageView photo_1,photo_2,expandedImageView,photo1,photo2,bButton;
    TextView title_question,question,title;
    Button SubmitAnswer;
    EditText Answer;
    private GoogleApiClient client;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    int photoid;
    private static final int RESULT_LOAD_IMAGE = 1;
    String[] list_answer_arr = null,list_answerID_arr = null;
    Bitmap[] list_answerPhoto1_arr = null,list_answerPhoto2_arr = null;
    int loop = 0,iPanswer,iPscore,iPRanswer;
    ProgressDialog progress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);

        setContentView(R.layout.detailquestion_activity);
        title = (TextView) findViewById(R.id.title);
        title.setText("DETAIL QUESTION");

        bButton = (ImageView) findViewById(R.id.back_button);
        bButton.setVisibility(View.VISIBLE);
        bButton.setOnClickListener(this);

        Bundle bQuestion = getIntent().getExtras();
        IdQuestion = "";
        if (bQuestion != null) {
            IdQuestion = bQuestion.getString("IdQuestion");
        }

        photo_1 = (ImageView) findViewById(R.id.showphoto1);
        photo_2 = (ImageView) findViewById(R.id.showphoto2);

        photo1 = (ImageView) this.findViewById(R.id.photo1);
        photo2 = (ImageView) this.findViewById(R.id.photo2);
        photo1.setOnClickListener(this);
        photo2.setOnClickListener(this);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        vUsername = sharedpreferences.getString("username", "");
        vPanswer = sharedpreferences.getString("point_answer", "");
        vPRanswer = sharedpreferences.getString("point_right_answer", "");
        vPscore = sharedpreferences.getString("point_score", "");

        Firebase.setAndroidContext(this);
        questionRef = new Firebase("https://forum-belajar.firebaseio.com/questions/" + IdQuestion);
        questionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean loadingRes = loading(true);
                if (dataSnapshot.getValue() != null) {
                    TitleQuestion = dataSnapshot.child("title").getValue().toString();
                    Question = dataSnapshot.child("question").getValue().toString();
                    QcreatedBy = dataSnapshot.child("username").getValue().toString();
                    if(dataSnapshot.child("right_answer_id").getValue() != null){
                        QRightAnswerId = dataSnapshot.child("right_answer_id").getValue().toString();
                    }else{
                        QRightAnswerId = "";
                    }
                    title_question = (TextView) findViewById(R.id.title_question);
                    question = (TextView) findViewById(R.id.question);
                    title_question.setText(TitleQuestion);
                    question.setText(Question);
                    photo_1.setVisibility(View.GONE);
                    photo_2.setVisibility(View.GONE);

                    photoRef = new Firebase("https://forum-belajar.firebaseio.com/photos/" + IdQuestion);
                    photoRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Photo1 = dataSnapshot.child("photo_1").getValue().toString();
                            Photo2 = dataSnapshot.child("photo_2").getValue().toString();

                            if (Photo1 != "") {
                                byte[] decodedString = Base64.decode(Photo1, Base64.DEFAULT);
                                decodedByte1 = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                photo_1.setImageBitmap(decodedByte1);
                                photo_1.setVisibility(View.VISIBLE);
                            }
                            if (Photo2 != "") {
                                byte[] decodedString = Base64.decode(Photo2, Base64.DEFAULT);
                                decodedByte2 = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                photo_2.setImageBitmap(decodedByte2);
                                photo_2.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            Toast.makeText(DetailQuestionActivity.this, "The read failed: " + firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    answerRef = new Firebase("https://forum-belajar.firebaseio.com/answers/"+IdQuestion);
                    answerRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                final int total_child = (int) dataSnapshot.getChildrenCount();
                                loop = 0;
                                list_answer_arr = new String[total_child];
                                list_answerID_arr = new String[total_child];
                                list_answerPhoto1_arr = new Bitmap[total_child];
                                list_answerPhoto2_arr = new Bitmap[total_child];
                                for (DataSnapshot child : dataSnapshot.getChildren()){
                                    String answer = child.child("answer").getValue().toString();
                                    String from_username = child.child("username").getValue().toString();
                                    String ID_answer = child.getKey().toString();
                                    list_answer_arr[loop] = "From "+from_username+" : "+System.getProperty("line.separator")+answer;
                                    list_answerID_arr[loop] = ID_answer;
                                    final int loop2 = loop;

                                    photoRef = new Firebase("https://forum-belajar.firebaseio.com/photos/" + ID_answer);
                                    photoRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            if(dataSnapshot.child("photo_1").getValue() != null){
                                                String Photo1 = dataSnapshot.child("photo_1").getValue().toString();
                                                if (Photo1 != "") {
                                                    byte[] decodedString = Base64.decode(Photo1, Base64.DEFAULT);
                                                    decodedAnswerByte1 = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                                }
                                            }

                                            if(dataSnapshot.child("photo_2").getValue() != null) {
                                                String Photo2 = dataSnapshot.child("photo_2").getValue().toString();
                                                if (Photo2 != "") {
                                                    byte[] decodedString = Base64.decode(Photo2, Base64.DEFAULT);
                                                    decodedAnswerByte2 = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                                }
                                            }

                                            list_answerPhoto1_arr[loop2] = (decodedAnswerByte1);
                                            list_answerPhoto2_arr[loop2] = (decodedAnswerByte2);

                                            if((loop2+1) == total_child){
                                                onFinishGetAnswer();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(FirebaseError firebaseError) {
                                            Toast.makeText(DetailQuestionActivity.this, "The read failed: " + firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    loop++;
                                }
//                                ArrayAdapter<String> adapter = new ArrayAdapter<>(DetailQuestionActivity.this, android.R.layout.simple_list_item_1,list_answer_arr);
//                                list_answer.setAdapter(adapter);
                            }else{
                                Boolean loadingRes = loading(false);
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            Toast.makeText(DetailQuestionActivity.this, "The read failed: " + firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(DetailQuestionActivity.this, "The read failed: " + firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        photo_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomImageFromThumb(photo_1, decodedByte1);
                if(decodedByte2 != null){
                    photo_2.setVisibility(View.GONE);
                }
            }
        });

        photo_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomImageFromThumb(photo_2, decodedByte2);
                if(decodedByte1 != null){
                    photo_1.setVisibility(View.GONE);
                }
            }
        });

        SubmitAnswer = (Button) findViewById(R.id.submit_answer);
        SubmitAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Answer = (EditText) findViewById(R.id.answer);
                if(Answer.getText().toString() != ""){
                    vAnswer = Answer.getText().toString();
                    Toast.makeText(v.getContext(), "Please wait...", Toast.LENGTH_LONG).show();
                    answerRef = new Firebase("https://forum-belajar.firebaseio.com/answers/"+IdQuestion);

                    Long dateCreated = System.currentTimeMillis()/1000;
                    Long Priority = 0-(dateCreated);

                    Map<String, String> dataAnswer = new HashMap<>();
                    dataAnswer.put("answer", vAnswer);
                    dataAnswer.put("username", vUsername);
                    dataAnswer.put("datecreated", dateCreated.toString());
                    Firebase answer = answerRef.push();
                    answer.setValue(dataAnswer);
                    answer.setPriority(Priority);

                    String IdAnswer = answer.getKey();
                    photoRef = new Firebase("https://forum-belajar.firebaseio.com/photos/"+IdAnswer);
                    sPhotoAnswer1 = convertImg(R.id.photo1);
                    photoRef.child("photo_1").setValue(sPhotoAnswer1);

                    sPhotoAnswer2 = convertImg(R.id.photo2);
                    photoRef.child("photo_2").setValue(sPhotoAnswer2);

                    Answer.setText("");
                    photo1.setImageDrawable(null);
                    photo2.setImageDrawable(null);

                    pointRef = new Firebase("https://forum-belajar.firebaseio.com/points/"+vUsername);

                    iPanswer = Integer.parseInt(vPanswer)+1;
                    vPanswer = Integer.toString(iPanswer);
                    pointRef.child("answer").setValue(iPanswer);
                    createSession("point_answer",vPanswer);

                    iPscore = Integer.parseInt(vPscore)+3;
                    vPscore = Integer.toString(iPscore);
                    pointRef.child("score").setValue(iPscore);
                    createSession("point_score",vPscore);

                    Toast.makeText(v.getContext(), "Success add answer", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(v.getContext(), "Please input your answer", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE &&  data != null){
            Uri selectedImage = data.getData();
            ImageView photo = (ImageView) this.findViewById(photoid);
            photo.setImageURI(selectedImage);
        }
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
            case R.id.back_button:
                finish();
                break;
            default:
                break;
        }
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    private void onFinishGetAnswer(){
        int loop_pos = 0,pos_answer_id = -1;
        for(String answerId : list_answerID_arr){
            if(answerId.equals(QRightAnswerId)){
                pos_answer_id = loop_pos;
                break;
            }
            loop_pos++;
        }

        ListView list_answer = (ListView) findViewById(R.id.list_answer);
        list_answer.setAdapter(new CustomAdapter(DetailQuestionActivity.this,pos_answer_id, list_answer_arr,list_answerPhoto1_arr,list_answerPhoto2_arr));
        setListViewHeightBasedOnChildren(list_answer);

        list_answer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                final String selectionID = list_answerID_arr[position];
                if(vUsername.equals(QcreatedBy)){
                    if(QRightAnswerId.equals("")){
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                            questionRef = new Firebase("https://forum-belajar.firebaseio.com/questions/" + IdQuestion);
                                            questionRef.child("right_answer_id").setValue(selectionID);
                                            RelativeLayout checkmark = (RelativeLayout) view.findViewById(R.id.right_answer_box);
                                            checkmark.setVisibility(View.VISIBLE);

                                            pointRef = new Firebase("https://forum-belajar.firebaseio.com/points/"+vUsername);

                                            iPRanswer = Integer.parseInt(vPRanswer)+1;
                                            vPRanswer = Integer.toString(iPRanswer);
                                            pointRef.child("right_answer").setValue(iPRanswer);
                                            createSession("point_right_answer",vPRanswer);

                                            iPscore = Integer.parseInt(vPscore)+5;
                                            vPscore = Integer.toString(iPscore);
                                            pointRef.child("score").setValue(iPscore);
                                            createSession("point_score",vPscore);
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setMessage("You want mark this as right answer?").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();
                    }
                }
            }
        });

        Boolean loadingRes = loading(false);
    }

    public String convertImg(int Resources) {
        ImageView iPhoto = (ImageView)findViewById(Resources);
        BitmapDrawable drawable = (BitmapDrawable) iPhoto.getDrawable();
        if(drawable != null){
            Bitmap bitmap = drawable.getBitmap();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,bos);
            byte[] bb = bos.toByteArray();
            String vPhoto = com.firebase.client.utilities.Base64.encodeBytes(bb);
            return vPhoto;
        }
        return "";
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        totalHeight += 600 * listAdapter.getCount();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void zoomImageFromThumb(final View thumbView, Bitmap imageBimap) {
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        expandedImageView = (ImageView) findViewById(R.id.expanded_image);
        expandedImageView.setImageBitmap(imageBimap);

        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container).getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        float startScale;
        if ((float) finalBounds.width() / finalBounds.height() > (float) startBounds.width() / startBounds.height()) {
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        if(decodedByte1 != null){
                            photo_1.setVisibility(View.VISIBLE);
                        }
                        if(decodedByte2 != null){
                            photo_2.setVisibility(View.VISIBLE);
                        }
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        if(decodedByte1 != null){
                            photo_1.setVisibility(View.VISIBLE);
                        }
                        if(decodedByte2 != null){
                            photo_2.setVisibility(View.VISIBLE);
                        }
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
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

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "DetailQuestion Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.forumbelajar.gasik.forumbelajar/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "DetailQuestion Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.forumbelajar.gasik.forumbelajar/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public void createSession(String key, String Value) {
        sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, Value);
        editor.commit();
    }
}
