package com.ttit.myapp.fragment;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.MobSDK;
import com.ttit.myapp.MainActivity;
import com.ttit.myapp.R;
import com.ttit.myapp.activity.BaseActivity;
import com.ttit.myapp.activity.GuideActivity;
import com.ttit.myapp.activity.HomeActivity;
import com.ttit.myapp.activity.LoginActivity;
import com.ttit.myapp.activity.MyCollectActivity;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.onekeyshare.OnekeyShare;
import skin.support.SkinCompatManager;


public class MyFragment extends BaseFragment {

    @BindView(R.id.img_header)
    ImageView imgHeader;

    public MyFragment() {
    }

    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView() {


    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.img_header, R.id.rl_collect, R.id.shareapp,R.id.rl_skin, R.id.rl_logout})
    public void onViewClicked(View view) {
        MobSDK.submitPolicyGrantResult(true, null);
        switch (view.getId()) {
            case R.id.img_header:
                showToast("人世几回伤往事,山形依旧枕寒流。");
                break;
            case R.id.rl_collect:
                navigateTo(MyCollectActivity.class);
                break;
            case R.id.shareapp:
                OnekeyShare oks = new OnekeyShare();
// title标题，微信、QQ和QQ空间等平台使用
                oks.setTitle(getString(R.string.app_name));
// titleUrl QQ和QQ空间跳转链接
                oks.setTitleUrl("http://sharesdk.cn");
// text是分享文本，所有平台都需要这个字段
                oks.setText("我是分享文本");
// setImageUrl是网络图片的url
                oks.setImageUrl("https://hmls.hfbank.com.cn/hfapp-api/9.png");
// url在微信、Facebook等平台中使用
                oks.setUrl("http://sharesdk.cn");
// 启动分享GUI
                oks.show(MobSDK.getContext());
                break;
            case R.id.rl_skin:
                String skin = findByKey("skin");
                if (skin.equals("night")) {
                    // 恢复应用默认皮肤
                    SkinCompatManager.getInstance().restoreDefaultTheme();
                    insertVal("skin", "default");
                } else {
                    SkinCompatManager.getInstance().loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN); // 后缀加载
                    insertVal("skin", "night");
                }
                break;
            case R.id.rl_logout:
                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                builder.setTitle("不想学了？").setMessage("确定要退出吗？")
                        .setIcon(R.drawable.ic_qus)
                        .setPositiveButton("手滑了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showToast("好的");
                    }
                }).setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showToast("再见！");
                        removeByKey("token");
                        navigateToWithFlag(MainActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    }
                }).show();
                break;
        }
    }
}