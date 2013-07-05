package com.babytree.apps.comm.listener;

import com.babytree.apps.comm.util.ButtomClickUtil;

import android.view.View;
import android.view.View.OnClickListener;

public abstract class BabytreeOnClickListenner implements OnClickListener {

	@Override
	public void onClick(View v) {
		if (ButtomClickUtil.isFastDoubleClick()) {
			return;
		}

	}

}
