package com.cebbank.partner.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cebbank.partner.R;

/**
 * @ClassName: Partner
 * @Description:
 * @Author Pjw
 * @date 2018/9/29 17:02
 */
public class ImageFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, null);
        return view;
    }
}
