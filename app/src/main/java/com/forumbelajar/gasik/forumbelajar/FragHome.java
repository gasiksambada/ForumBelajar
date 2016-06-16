package com.forumbelajar.gasik.forumbelajar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import java.util.ArrayList;

/**
 * Created by Gasik on 6/8/2016.
 */
public class FragHome extends Fragment {
    Communicator comm;
    Firebase questionRef;
    ArrayList<String> list_question_arr;
    ArrayList<String> list_questionID_arr;
    ListView list_question;
    ArrayAdapter<String> adapter;
    ProgressDialog progress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_home,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Firebase.setAndroidContext(this.getActivity());
        questionRef = new Firebase("https://forum-belajar.firebaseio.com/questions");
        questionRef.orderByPriority().addValueEventListener(new ValueEventListener() {
            Boolean loadingRes = loading(true);
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    list_question_arr = new ArrayList<>();
                    list_questionID_arr = new ArrayList<>();

                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        String title_question = child.child("title").getValue().toString();
                        String ID_question = child.getKey().toString();
                        list_question_arr.add(title_question);
                        list_questionID_arr.add(ID_question);
                    }

                    ListView list_question = (ListView) getActivity().findViewById(R.id.list_question);
                    adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,list_question_arr);
                    list_question.setAdapter(adapter);
                }
                Boolean loadingRes2 = loading(false);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(getActivity(), "The read failed: " + firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Button add_question = (Button) getActivity().findViewById(R.id.add_question);
        add_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comm = (Communicator) getActivity();
                comm.goTo("AddquestionActivity");
            }
        });

        list_question = (ListView) getActivity().findViewById(R.id.list_question);
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

        EditText SearchBox = (EditText) getActivity().findViewById(R.id.searchbox);
        TextWatcher fieldValidatorTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("Search :",s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.getFilter().filter(s);
            }
        };
        SearchBox.addTextChangedListener(fieldValidatorTextWatcher);
    }

    public boolean loading(Boolean status) {
        if(status){
            progress = new ProgressDialog(getActivity());
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
