package com.example.sandetest;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class HomeFragment extends Fragment {

    private Button btn_diagnose, btn_feedback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,null);
        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btn_diagnose = getActivity().findViewById(R.id.btn_diagnose);
        btn_diagnose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DiagnoseActivity.class);
                startActivity(intent);
                Toast.makeText(getActivity(), "进行诊断", Toast.LENGTH_SHORT).show();
            }
        });

        btn_feedback = getActivity().findViewById(R.id.btn_feedback);
        btn_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),FeedbackActivity.class);
                startActivity(intent);
                Toast.makeText(getActivity(), "进行反馈", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
