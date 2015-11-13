package com.spontaneous.android.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.spontaneous.android.R;
import com.spontaneous.android.fragment.HomeFragment;
import com.spontaneous.android.util.Logger;

/**
 * Created by Eidan on 4/25/2015.
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.i("MainActivity: onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            //toolbar.inflateMenu(R.menu.main);
        }

        moveToScreen(HomeFragment.class, null, true, true);
    }
}
