package com.example.sandetest;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {

    private ImageView iv_home, iv_message, iv_me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv_home = findViewById(R.id.iv_home);
        iv_message = findViewById(R.id.iv_message);
        iv_me = findViewById(R.id.iv_me);

        iv_home.setOnClickListener(l);
        iv_message.setOnClickListener(l);
        iv_me.setOnClickListener(l);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment f = new HomeFragment();
        ft.replace(R.id.fragment,f);
        ft.commit();
    }

    View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment f = null;
            switch (v.getId()){
                case R.id.iv_home:
                    f = new HomeFragment();
                    break;
                case R.id.iv_message:
                    f = new MessageFragment();
                    break;
                case R.id.iv_me:
                    f = new MeFragment();
                    break;
                default:
                    break;
            }
            ft.replace(R.id.fragment,f);
            ft.commit();
        }
    };


}
