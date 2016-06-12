package com.forumbelajar.gasik.forumbelajar;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
public class FragProfile extends Fragment {
    Communicator comm;
    Firebase accountRef;
    ArrayList<String> list_question_arr;
    ArrayList<String> list_questionID_arr;
    String vUsername;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_profile,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        vUsername = comm.getSession();
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
//                        if(username.equals(vUsername)){
                            String title_question = child.child("title").getValue().toString();
                            String ID_question = child.getValue().toString();
                            list_question_arr.add(title_question);
                            list_questionID_arr.add(ID_question);
//                        }
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
                Toast.makeText(getActivity(), "Data klik "+selectionID, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
