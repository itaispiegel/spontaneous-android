package com.spontaneous.android.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.spontaneous.android.App;
import com.spontaneous.android.R;
import com.spontaneous.android.util.Logger;

/**
 * Created by Eidan on 4/25/2015.
 */
public abstract class BaseActivity extends ActionBarActivity {

    private boolean mBackStackIncreased;
    private String mCurrentFragmentKey;
    private App mApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApp = (App) getApplication();
    }

    public void moveToScreen(Class<? extends Fragment> clazz, Bundle args,
                             boolean addToBackStack, boolean cleanBackStack) {
        String nextScreen = clazz.getSimpleName();
        Fragment f = getFragmentManager().findFragmentByTag(nextScreen);
        if(f == null) {
            try {
                f = clazz.newInstance();
            } catch(InstantiationException e) {
                e.printStackTrace();
                return;
            } catch(IllegalAccessException e) {
                e.printStackTrace();
                return;
            }

        }

        if(args != null) {
            f.setArguments(args);
        }

        moveToFragment(f, nextScreen, addToBackStack, cleanBackStack);
    }

    private void moveToFragment(Fragment f, String nextScreen, boolean addToBackStack, boolean cleanBackStack) {
        Logger.i("moveToFragment");
        try {
            FragmentManager manager = getFragmentManager();
            FragmentTransaction trans = manager.beginTransaction();

            if(cleanBackStack) {
                manager.popBackStackImmediate(null,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else {
                trans.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right,
                    R.anim.slide_in_left, R.anim.slide_out_right);
            }

            trans.replace(R.id.fragment_container, f, nextScreen);

            if(addToBackStack) {
                mBackStackIncreased = true;
                trans.addToBackStack(nextScreen);
            }

            mCurrentFragmentKey = nextScreen;
            trans.commit();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }

    }
}
