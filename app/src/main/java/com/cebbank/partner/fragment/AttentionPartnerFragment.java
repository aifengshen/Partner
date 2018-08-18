package com.cebbank.partner.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cebbank.partner.R;

/**
 *关注的合伙人
 */
public class AttentionPartnerFragment extends Fragment {
    public static final String ARG_OBJECT = "object";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(
                R.layout.fragment_attention_partner, container, false);
        Bundle args = getArguments();
//        ((TextView) rootView.findViewById(R.id.tvText)).setText(
//                Integer.toString(args.getInt(ARG_OBJECT)));
        return rootView;
    }
}
