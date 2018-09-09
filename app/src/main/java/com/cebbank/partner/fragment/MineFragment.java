package com.cebbank.partner.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cebbank.partner.R;
import com.cebbank.partner.interfaces.HttpCallbackListener;
import com.cebbank.partner.ui.BecomePartnerActivity;
import com.cebbank.partner.ui.MyApplyActivity;
import com.cebbank.partner.ui.PersonalDataActivity;
import com.cebbank.partner.utils.UrlPath;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.cebbank.partner.utils.HttpUtil.sendOkHttpRequestUpLoad;

/**
 * @ClassName: Omnipotent
 * @Description:
 * @Author Pjw
 * @date 2018/7/31 15:26
 */
public class MineFragment extends Fragment implements View.OnClickListener {

    private CircleImageView profile_image;
    private TextView tvUserName, tvEditProfile, tvBecomePartner, tvPersonalData, tvMyMaterial, tvBindingCard, tvProgress_Audit, tvFeedback;
    private RelativeLayout rlHelpCenter, rlOfficialTutorials, rlContactUs, rlLogout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        initView(view);
        initData();
        setClickListener();
        return view;
    }

    private void initView(View view) {
        profile_image = view.findViewById(R.id.profile_image);
        tvUserName = view.findViewById(R.id.tvUserName);
        tvEditProfile = view.findViewById(R.id.tvEditProfile);
        tvBecomePartner = view.findViewById(R.id.tvBecomePartner);
        tvPersonalData = view.findViewById(R.id.tvPersonalData);
        tvMyMaterial = view.findViewById(R.id.tvMyMaterial);
        tvBindingCard = view.findViewById(R.id.tvBindingCard);
        tvProgress_Audit = view.findViewById(R.id.tvProgress_Audit);
        tvFeedback = view.findViewById(R.id.tvFeedback);
        rlHelpCenter = view.findViewById(R.id.rlHelpCenter);
        rlOfficialTutorials = view.findViewById(R.id.rlOfficialTutorials);
        rlContactUs = view.findViewById(R.id.rlContactUs);
        rlLogout = view.findViewById(R.id.rlLogout);
    }

    private void initData() {

    }

    private void setClickListener() {
        profile_image.setOnClickListener(this);
        tvUserName.setOnClickListener(this);
        tvEditProfile.setOnClickListener(this);
        tvBecomePartner.setOnClickListener(this);
        tvPersonalData.setOnClickListener(this);
        tvMyMaterial.setOnClickListener(this);
        tvBindingCard.setOnClickListener(this);
        tvProgress_Audit.setOnClickListener(this);
        tvFeedback.setOnClickListener(this);
        rlHelpCenter.setOnClickListener(this);
        rlOfficialTutorials.setOnClickListener(this);
        rlContactUs.setOnClickListener(this);
        rlLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_image:
                /**
                 * 头像
                 */
                upload(Environment.getExternalStorageDirectory().getAbsolutePath()+"/aaa.jpg");
                break;
            case R.id.tvUserName:
                /**
                 * 姓名
                 */
                break;
            case R.id.tvEditProfile:
                /**
                 * 编辑资料
                 */

                break;
            case R.id.tvBecomePartner:
                /**
                 * 成为合伙人
                 */
                startActivity(new Intent(getActivity(), BecomePartnerActivity.class));
                break;
            case R.id.tvPersonalData:
                /**
                 * 个人数据
                 */
                startActivity(new Intent(getActivity(), PersonalDataActivity.class));
                break;
            case R.id.tvMyMaterial:
                /**
                 *我的素材
                 */

                break;
            case R.id.tvBindingCard:
                /**
                 *绑定银行卡
                 */
                break;
            case R.id.tvProgress_Audit:
                /**
                 *审核进度
                 */
                startActivity(new Intent(getActivity(), MyApplyActivity.class));
                break;
            case R.id.tvFeedback:
                /**
                 *意见反馈
                 */
                break;
            case R.id.rlHelpCenter:
                /**
                 *帮助中心
                 */
                break;
            case R.id.rlOfficialTutorials:
                /**
                 *官方教程
                 */
                break;
            case R.id.rlContactUs:
                /**
                 *联系我们
                 */
                break;
            case R.id.rlLogout:
                /**
                 *退出登录
                 */
                break;

        }
    }

    private void upload(String path){

        sendOkHttpRequestUpLoad(getActivity(), UrlPath.Upload, null, path, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject jsonObject = new JSONObject(response);
                String code = jsonObject.optString("code");
                String obj = jsonObject.optString("obj");
//                    Gson gson = new Gson();
//                    List<PartnerDynamicBean> partnerDynamicBeanList = gson.fromJson(obj, PartnerDynamicBean.class);
//                List<PartnerDynamicBean> dataList = new ArrayList<>();
//                for (int i = 0; i < 10; i++) {
//                    PartnerDynamicBean partnerDynamicBean = new PartnerDynamicBean();
//                    partnerDynamicBean.setTitle(i + "啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊");
//                    dataList.add(partnerDynamicBean);
//                }

            }

            @Override
            public void onFailure() {


            }
        });
    }
}
