// Generated code from Butter Knife. Do not modify!
package com.ttit.myapp.fragment;

import android.view.View;
import android.widget.ImageView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.ttit.myapp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MyFragment_ViewBinding implements Unbinder {
  private MyFragment target;

  private View view7f0900cb;

  private View view7f09014f;

  private View view7f09016a;

  private View view7f090151;

  private View view7f090150;

  @UiThread
  public MyFragment_ViewBinding(final MyFragment target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.img_header, "field 'imgHeader' and method 'onViewClicked'");
    target.imgHeader = Utils.castView(view, R.id.img_header, "field 'imgHeader'", ImageView.class);
    view7f0900cb = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.rl_collect, "method 'onViewClicked'");
    view7f09014f = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.shareapp, "method 'onViewClicked'");
    view7f09016a = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.rl_skin, "method 'onViewClicked'");
    view7f090151 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.rl_logout, "method 'onViewClicked'");
    view7f090150 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    MyFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.imgHeader = null;

    view7f0900cb.setOnClickListener(null);
    view7f0900cb = null;
    view7f09014f.setOnClickListener(null);
    view7f09014f = null;
    view7f09016a.setOnClickListener(null);
    view7f09016a = null;
    view7f090151.setOnClickListener(null);
    view7f090151 = null;
    view7f090150.setOnClickListener(null);
    view7f090150 = null;
  }
}
