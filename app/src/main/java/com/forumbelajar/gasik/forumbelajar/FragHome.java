package com.forumbelajar.gasik.forumbelajar;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Gasik on 6/8/2016.
 */
public class FragHome extends Fragment {
    Communicator comm;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_home,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button add_question = (Button) getActivity().findViewById(R.id.add_question);
        add_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comm = (Communicator) getActivity();
                comm.goTo("AddquestionActivity");
            }
        });
    }
}
