package com.cebbank.partner;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.cebbank.partner.fragment.PartnerFragment;
import com.cebbank.partner.fragment.HomeFragment;
import com.cebbank.partner.fragment.MineFragment;
import com.cebbank.partner.fragment.MessageFragment;
import com.cebbank.partner.fragment.AttentionFragment;
import com.cebbank.partner.utils.BottomNavigationViewHelper;
import com.cebbank.partner.utils.SharedPreferencesKey;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    replaceFragment(new HomeFragment());
                    return true;
                case R.id.navigation_attention:
                    replaceFragment(new AttentionFragment());
                    return true;
                case R.id.navigation_message:
                    replaceFragment(new MessageFragment());
                    return true;
                case R.id.navigation_partner:
                    replaceFragment(new PartnerFragment());
                    return true;
                case R.id.navigation_mine:
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
        MyApplication.saveValue(SharedPreferencesKey.Token,"04327681cfe3450bbd4740e2cc02e1a8");
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.commit();
    }

    /**
     * @param keyCode
     * @param event
     * @return 双击返回键 退出程序
     * @see android.support.v4.app.FragmentActivity#onKeyDown(int,
     * android.view.KeyEvent)
     */
    int i = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (i == 0) {
                Toast.makeText(this, "再点击一次将退出程序", Toast.LENGTH_SHORT).show();
                i++;
            } else {
                this.finish();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
