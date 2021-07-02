package com.ttit.myapp.schedule.mvp.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ttit.myapp.schedule.BaseFragment;
import com.ttit.myapp.R;
import com.ttit.myapp.schedule.app.Cache;
import com.ttit.myapp.schedule.data.beanv2.CourseGroup;
import com.ttit.myapp.schedule.data.beanv2.UserWrapper;
import com.ttit.myapp.schedule.mvp.add.AddActivity;
import com.ttit.myapp.schedule.mvp.mg.MgActivity;
import com.ttit.myapp.schedule.utils.DialogHelper;
import com.ttit.myapp.schedule.utils.DialogListener;
import com.ttit.myapp.schedule.utils.RequestPermission;
import com.ttit.myapp.schedule.utils.ScreenUtils;
import com.ttit.myapp.schedule.utils.event.SignEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends BaseFragment implements HomeContract.View,
        View.OnClickListener, Toolbar.OnMenuItemClickListener {

    HomeContract.Presenter mPresenter;
    private int REQUEST_CODE_SCAN = 1;
    private View mViewShare;
    private DialogHelper mProgressDialog;
    private LinearLayout mLayoutUpload;
    private LinearLayout mLayoutOverwriteLocal;
    private Toolbar mToolbar;
    private CircleImageView mCivAvator;
    private View mLayoutSetting;
    private View mLayoutCourseMg;
    private TextView mTvUsername;
    private View mViewAdd;
    private View mLayoutFeedback;

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showMassage(String msg) {
        toast(msg);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_main;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void initView() {
        mToolbar = mRootView.findViewById(R.id.toolbar);
        mTvUsername = mRootView.findViewById(R.id.tv_username);
        mViewAdd = mRootView.findViewById(R.id.layout_add);
        mViewShare = mRootView.findViewById(R.id.layout_share);
      /*  mLayoutFeedback = mRootView.findViewById(R.id.layout_feedback);*/
        mLayoutCourseMg = mRootView.findViewById(R.id.layout_course_mg);
        mCivAvator = mRootView.findViewById(R.id.profile_image);
        initToolbar();
    }

    private void initToolbar() {
        backToolbar(mToolbar);
        mToolbar.inflateMenu(R.menu.toolbar_home);
        mToolbar.setOnMenuItemClickListener(this);
    }

    @Override
    public void initData() {
        new HomePresenter(this).start();
        showCacheData();
    }

    @Override
    public void initListener() {
        mViewAdd.setOnClickListener(this);
        mViewShare.setOnClickListener(this);
        mLayoutCourseMg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_add:
                addSelectDialog();
                break;
            case R.id.layout_share:
                Toast.makeText(activity, "邮箱：1535550295@qq.com", Toast.LENGTH_SHORT).show();
                break;
            case R.id.layout_course_mg:
                courseManage();
                break;
            default:
                break;
        }
    }

    private void addSelectDialog() {
        new DialogHelper().buildBottomListDialog(activity, new String[]{"手动添加"},
                new DialogListener() {
                    @Override
                    public void onItemClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch (which) {
                            case 0:
                                userAdd();
                                break;
                            default:
                                break;
                        }
                    }
                }).show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                addSelectDialog();
                break;
        }
        return false;
    }

    private void userAdd() {
        startActivity(new Intent(activity, AddActivity.class));
    }

    private void courseManage() {
        startActivity(new Intent(activity, MgActivity.class));
    }

    /**
     * Activity返回回调
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 权限回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        RequestPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 显示缓存的信息
     */
    @Override
    public void showCacheData() {
        String email = Cache.instance().getEmail();
        if (!TextUtils.isEmpty(email)) {
            UserWrapper.User user = new UserWrapper.User();
            user.setEmail(email);
            signInPage(user);
        }
    }

    @Override
    public void showLoading(String msg) {
        mProgressDialog = new DialogHelper();
        //TODO 弹出不可取消
        mProgressDialog.showProgressDialog(getContext(), "稍后", msg, false);
    }

    @Override
    public void stopLoading() {
        mProgressDialog.hideProgressDialog();
    }

    /**
     * 未登录模式
     */
    @Override
    public void noSignInPage() {
        if (!isActive()) {
            return;
        }
        hideSignOutMenu(false);
        mTvUsername.setText("加油");

        // default avator
        mCivAvator.setImageResource(R.drawable.ic_avator_black_24dp);
    }

    /**
     * 已登录模式
     */
    @Override
    public void signInPage(UserWrapper.User user) {
        if (!isActive()) {
            return;
        }
        hideSignOutMenu(true);
        mTvUsername.setOnClickListener(null);
        mTvUsername.setText(user.getEmail());
    }

    /**
     * 二维码生成成功
     */
    @Override
    public void createQRCodeSucceed(Bitmap bitmap) {
        if (!isActive()) {
            return;
        }

        DialogHelper dialogHelper = new DialogHelper();
        View dialogView = View.inflate(getContext(), R.layout.dialog_qr_code, null);

        ((ImageView) dialogView.findViewById(R.id.iv_qr_code)).setImageBitmap(bitmap);

        dialogHelper.showCustomDialog(Objects.requireNonNull(getContext()),
                dialogView, null, ScreenUtils.dp2px(220), null);
    }

    /**
     * 克隆云数据成功
     */
    @Override
    public void cloudToLocalSucceed() {
        startActivity(new Intent(activity, MgActivity.class));
        activity.finish();
    }

    /**
     * newInstance
     */
    public static com.ttit.myapp.schedule.mvp.home.HomeFragment newInstance() {

        Bundle args = new Bundle();

        com.ttit.myapp.schedule.mvp.home.HomeFragment fragment = new com.ttit.myapp.schedule.mvp.home.HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //登录事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginEvent(SignEvent event) {
        activity.recreate();
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    /**
     * 课表列表弹窗
     */
    @Override
    public void showGroupDialog(List<CourseGroup> groups) {
        DialogHelper helper = new DialogHelper();
        final String[] items = new String[groups.size()];
        final long[] ids = new long[items.length];

        for (int i = 0; i < groups.size(); i++) {
            items[i] = groups.get(i).getCgName();
            ids[i] = groups.get(i).getCgId();
        }
    }

    /**
     * 因此注销菜单
     */
    private void hideSignOutMenu(boolean hide) {
        if (TextUtils.isEmpty(Cache.instance().getEmail())) {
            mToolbar.getMenu().findItem(R.id.action_sign_out).setVisible(hide);
        }
    }

    /**
     * toolbar点击
     */
    @Override
    public void close() {
        super.close();
        activity.finish();
    }

    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
        //EvenBus
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 注销
     */
    private void signOut() {

        SignEvent event = new SignEvent().setSignOut(true);
        EventBus.getDefault().post(event);
    }
}
