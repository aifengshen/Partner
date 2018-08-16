package com.cebbank.partner.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.cebbank.partner.R;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName: Omnipotent
 * @Description:
 * @Author Pjw
 * @date 2018/7/31 15:27
 */
public class AttentionFragment extends Fragment {

    private View view;
    private MaterialViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.AppThemes);
//        LayoutInflater Inflater = inflater.cloneInContext(contextThemeWrapper);
//        View v = Inflater.inflate(R.layout.activity_main, container, false);
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_attention, container, false);
        initView(view);
        initData();
        setClickListener();
        return view;
    }

    private void initView(View view){
        Log.e("1111","1111");
        mViewPager = (MaterialViewPager) view.findViewById(R.id.materialViewPager);
        ViewPager viewPager = mViewPager.getViewPager();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new MineFragment());
        fragments.add(new ThreeFragment());
        Log.e("2222",fragments.size()+"");
        AdapterFragment adapterFragment = new AdapterFragment(fragmentManager,fragments);
        viewPager.setAdapter(adapterFragment);

        //After set an adapter to the ViewPager
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
//        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
//            @Override
//            public HeaderDesign getHeaderDesign(int page) {
//                switch (page) {
//                    case 0:
//                        return HeaderDesign.fromColorResAndUrl(
//                                R.color.blue,
//                                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534440083067&di=a04d46600bd5cfa3cf5b09f39de42f23&imgtype=0&src=http%3A%2F%2Fp16.qhimg.com%2Fbdr%2F__%2Fd%2F_open360%2Fbeauty0311%2F16.jpg");
//                    case 1:
//                        return HeaderDesign.fromColorResAndUrl(
//                                R.color.green,
//                                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534440083067&di=e63cfc6a75ce3857d0d4eff60586bc01&imgtype=0&src=http%3A%2F%2Fimg.ph.126.net%2FijQNs4q86Q2fn5UI7ezxuQ%3D%3D%2F733523789408400623.jpg");
//                    case 2:
//                        return HeaderDesign.fromColorResAndUrl(
//                                R.color.cyan,
//                                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534440083067&di=83cf8d486adb35659ee5eb7038b85ac3&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F1%2F572afc324b7f1.jpg");
//                    case 3:
//                        return HeaderDesign.fromColorResAndUrl(
//                                R.color.red,
//                                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534440083067&di=ebcc5903ced82773e206a92806c23637&imgtype=0&src=http%3A%2F%2Fi8.download.fd.pchome.net%2Ft_600x1024%2Fg1%2FM00%2F07%2F18%2FoYYBAFNY5H2If_UQAA_d0sgFVhwAABfCAFKBJQAD93q339.jpg");
//                }
//
//                //execute others actions if needed (ex : modify your header logo)
//
//                return null;
//            }
//        });
    }

    private void initData(){

    }

    private void setClickListener(){

    }

    public class AdapterFragment extends FragmentPagerAdapter {
        private List<Fragment> mFragments;

        public AdapterFragment(FragmentManager fm, List<Fragment> mFragments) {
            super(fm);
            this.mFragments = mFragments;
        }

        @Override
        public Fragment getItem(int position) {//必须实现
            return mFragments.get(position);
        }

        @Override
        public int getCount() {//必须实现
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {//选择性实现
            return mFragments.get(position).getClass().getSimpleName();
        }
    }

}
