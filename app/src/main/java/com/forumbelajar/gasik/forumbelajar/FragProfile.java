package com.forumbelajar.gasik.forumbelajar;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.utilities.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by Gasik on 6/8/2016.
 */
public class FragProfile extends Fragment implements View.OnClickListener {
    Communicator comm;
    private static final int RESULT_LOAD_IMAGE = 1;
    Firebase accountRef,photoRef,pointRef;
    ArrayList<String> list_question_arr;
    ArrayList<String> list_questionID_arr;
    String vUsername,vPpic,vTbackground,vPquestion,vPanswer,vPRanswer,vPscore,fPquestion,fPanswer,fPRanswer,fPscore;
    ImageView Ppic,Tbackground;
//    TextView Tbackground;
    int photoid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_profile,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Tbackground = (ImageView) getActivity().findViewById(R.id.TimelineBackground);
        Ppic = (ImageView) getActivity().findViewById(R.id.ProfilePicture);
        Tbackground.setOnClickListener(this);
        Ppic.setOnClickListener(this);

        vUsername = SecondActivity.getCurrentUsername();
        vPpic = SecondActivity.getCurrentPpic();
        vTbackground = SecondActivity.getCurrentTbackground();

//        vPquestion = SecondActivity.getCurrentPquestion();
//        vPanswer = SecondActivity.getCurrentPanswer();
//        vPRanswer = SecondActivity.getCurrentPRanswer();
//        vPscore = SecondActivity.getCurrentPscore();
//
//        TextView point_question = (TextView) getActivity().findViewById(R.id.point_question);
//        TextView point_answer = (TextView) getActivity().findViewById(R.id.point_answer);
//        TextView point_right_answer = (TextView) getActivity().findViewById(R.id.point_right_answer);
//        TextView point_score = (TextView) getActivity().findViewById(R.id.point_score);
//
//        point_question.setText(vPquestion);
//        point_answer.setText(vPanswer);
//        point_right_answer.setText(vPRanswer);
//        point_score.setText(vPscore);

        pointRef = new Firebase("https://forum-belajar.firebaseio.com/points/"+vUsername);
        pointRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView point_question = (TextView) getActivity().findViewById(R.id.point_question);
                TextView point_answer = (TextView) getActivity().findViewById(R.id.point_answer);
                TextView point_right_answer = (TextView) getActivity().findViewById(R.id.point_right_answer);
                TextView point_score = (TextView) getActivity().findViewById(R.id.point_score);
                if(dataSnapshot.child("question").getValue() != null){
                    fPquestion = dataSnapshot.child("question").getValue().toString();
                    if (fPquestion != "") {
                        point_question.setText(fPquestion);
//                        comm.createSession("point_question",fPquestion);
                    }
                }
                if(dataSnapshot.child("answer").getValue() != null){
                    fPanswer = dataSnapshot.child("answer").getValue().toString();
                    if (fPanswer != "") {
                        point_answer.setText(fPanswer);
//                        comm.createSession("point_answer",fPanswer);
                    }
                }
                if(dataSnapshot.child("right_answer").getValue() != null){
                    fPRanswer = dataSnapshot.child("right_answer").getValue().toString();
                    if (fPRanswer != "") {
                        point_right_answer.setText(fPRanswer);
//                        comm.createSession("point_right_answer",fPRanswer);
                    }
                }
                if(dataSnapshot.child("score").getValue() != null){
                    fPscore = dataSnapshot.child("score").getValue().toString();
                    if (fPscore != "") {
                        point_score.setText(fPscore);
//                        comm.createSession("point_score",fPscore);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Log.d("From Profile : ",vPpic);

        if (vPpic != null && vPpic != "") {
            byte[] decPpic = android.util.Base64.decode(vPpic, android.util.Base64.DEFAULT);
            Bitmap bmpPpic = BitmapFactory.decodeByteArray(decPpic, 0, decPpic.length);
            BitmapDrawable bdPpic = new BitmapDrawable(getActivity().getResources(), bmpPpic);
            ImageView iPpic = (ImageView) getActivity().findViewById(R.id.ProfilePicture);
            iPpic.setImageDrawable(bdPpic);
        }

        if (vTbackground != null && vPpic != "") {
            byte[] decTbackground = android.util.Base64.decode(vTbackground, android.util.Base64.DEFAULT);
            Bitmap bmpTbackground = BitmapFactory.decodeByteArray(decTbackground, 0, decTbackground.length);
            BitmapDrawable bdTbackground = new BitmapDrawable(getActivity().getResources(), bmpTbackground);
            ImageView iTbackground = (ImageView) getActivity().findViewById(R.id.TimelineBackground);
            iTbackground.setBackgroundDrawable(bdTbackground);
        }

        Firebase.setAndroidContext(this.getActivity());
        accountRef = new Firebase("https://forum-belajar.firebaseio.com/questions");
        accountRef.orderByPriority().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    list_question_arr = new ArrayList<>();
                    list_questionID_arr = new ArrayList<>();

                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        String username = child.child("username").getValue().toString();
                        if(username.equals(vUsername)){
                            String title_question = child.child("title").getValue().toString();
                            String ID_question = child.getKey().toString();
                            list_question_arr.add(title_question);
                            list_questionID_arr.add(ID_question);
                        }
                    }

                    ListView list_question = (ListView) getActivity().findViewById(R.id.list_your_question);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,list_question_arr);
                    list_question.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(getActivity(), "The read failed: " + firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        ListView list_question = (ListView) getActivity().findViewById(R.id.list_your_question);
        list_question.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectionID = list_questionID_arr.get(position);
                Intent intent = new Intent(getActivity(), DetailQuestionActivity.class);
                Bundle bQuestion = new Bundle();
                bQuestion.putString("IdQuestion", selectionID);
                intent.putExtras(bQuestion);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE &&  data != null){
            Uri selectedImage = data.getData();
            String picturePath = getRealPathFromURI(selectedImage);
            Bitmap bmp = BitmapFactory.decodeFile(picturePath, null);
            BitmapDrawable bd = new BitmapDrawable(getActivity().getResources(), bmp);
            Toast.makeText(getActivity(), "Please wait...", Toast.LENGTH_LONG).show();
            photoRef = new Firebase("https://forum-belajar.firebaseio.com/photos/"+vUsername);

            switch (photoid) {
                case R.id.TimelineBackground:
                    ImageView Background = (ImageView) getActivity().findViewById(photoid);
                    Background.setBackgroundDrawable(bd);

                    vTbackground = convertImg(bd);
                    photoRef.child("background_timeline").setValue(vTbackground);

                    break;
                case R.id.ProfilePicture:
                    ImageView photo = (ImageView) getActivity().findViewById(photoid);
                    photo.setImageURI(selectedImage);

                    vPpic = convertImg(bd);
                    photoRef.child("profile_picture").setValue(vPpic);
                    break;
                default:
                    break;
            }

            Toast.makeText(getActivity(), "Success change profile picture", Toast.LENGTH_LONG).show();
        }
    }

    public String convertImg(BitmapDrawable drawable) {
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

    public String getRealPathFromURI (Uri contentUri) {
        String path = null;
        String[] proj = { MediaStore.MediaColumns.DATA };
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }

    @Override
    public void onClick(View v) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        switch (v.getId() /*to get clicked view id**/) {
            case R.id.TimelineBackground:
                photoid = R.id.TimelineBackground;
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                break;
            case R.id.ProfilePicture:
                photoid = R.id.ProfilePicture;
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                break;
            default:
                break;
        }
    }
}
