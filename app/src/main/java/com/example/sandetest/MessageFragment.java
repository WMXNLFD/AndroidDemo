package com.example.sandetest;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class MessageFragment extends Fragment {
    
    private Button btn_message, btn_report;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message,null);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        btn_message = getActivity().findViewById(R.id.btn_message);
        btn_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "显示推文", Toast.LENGTH_SHORT).show();
            }
        });
        
        btn_report = getActivity().findViewById(R.id.btn_report);
        btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "显示报告", Toast.LENGTH_SHORT).show();
                //从信息页面进入报告页面
                Intent intent = new Intent(getActivity(), ReportActivity.class);
                intent.putExtra("messageToReport", 2);
                startActivity(intent);
                //其实应该在这里调用数据库，，，后面记得优化！！
            }
        });
        
    }
}
