// Generated code from Butter Knife. Do not modify!
package com.ttit.myapp.fragment;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.ttit.myapp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class NewsFragment_ViewBinding implements Unbinder {
  private NewsFragment target;

  private View view7f0900b0;

  @UiThread
  public NewsFragment_ViewBinding(final NewsFragment target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.fab, "method 'onViewClicked'");
    view7f0900b0 = view;
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
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    target = null;


    view7f0900b0.setOnClickListener(null);
    view7f0900b0 = null;
  }
}
