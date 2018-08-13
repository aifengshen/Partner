package com.cebbank.partner;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.cebbank.partner.fragment.HomeFragment;
import com.cebbank.partner.fragment.MineFragment;
import com.cebbank.partner.fragment.ThreeFragment;
import com.cebbank.partner.fragment.TwoFragment;
import com.cebbank.partner.utils.BottomNavigationViewHelper;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    replaceFragment(new HomeFragment());
                    return true;
                case R.id.navigation_dashboard:
                    replaceFragment(new TwoFragment());
                    return true;
                case R.id.navigation_three:
                    replaceFragment(new ThreeFragment());
                    return true;
                case R.id.navigation_notifications:
                    replaceFragment(new MineFragment());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);
//        navigation.setItemTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        BottomNavigationViewHelper.disableShiftMode(navigation);
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment,fragment);
        transaction.commit();
    }

}
