package com.babytree.apps.comm.net;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.view.View.OnClickListener;

import com.babytree.apps.comm.ctr.BabytreeController;
import com.babytree.apps.comm.util.BaseUtil;
import com.babytree.apps.comm.util.DataResult;
import com.babytree.apps.comm.util.ExceptionUtil;

/**
 * 联网异步操作 提供了设置loading字符串,联网成功失败的回调
 * 
 * @author wangbingqi
 * 
 */
public abstract class BabytreeAsyncTask extends
		AsyncTask<String, Integer, DataResult> {
	private ProgressDialog mDialog;
	private Context context;

	public BabytreeAsyncTask(Context context) {
		this.context = context;
	}

	@Override
	protected DataResult doInBackground(String... params) {
		DataResult ret = null;
		try {
			if (BaseUtil.hasNetwork(context)) {
				ret = toNet(params);
			} else {
				ret = new DataResult();
				ret.message = BabytreeController.NetworkExceptionMessage;
				ret.status = BabytreeController.NetworkExceptionCode;
			}
		} catch (Exception e) {
			ret = new DataResult();
			ret.message = BabytreeController.SystemExceptionMessage;
			ret.status = BabytreeController.SystemExceptionCode;
			ret.error = ExceptionUtil.printException(e).toString();
		}
		return ret;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		showDialog(null, getDialogMessage(), null, null, true, null, null);
	}

	@Override
	protected void onPostExecute(DataResult result) {
		super.onPostExecute(result);
		closeDialog();
		switch (result.status) {
		case BabytreeController.SUCCESS_CODE:
			success(result);
			break;
		default:
			failure(result);
			break;
		}
	}

	/**
	 * 关闭对话框
	 */
	public final void closeDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
	}

	/**
	 * 显示对话框
	 */
	private void showDialog(String title, String content, String okText,
			String cancleText, boolean cancelable, OnCancelListener btnCancle,
			OnClickListener btnOk) {
		if (content == null || content.equalsIgnoreCase(""))
			return;
		try {
			if (mDialog == null) {
				mDialog = new ProgressDialog(context);
			}
			mDialog.setTitle(title);
			mDialog.setMessage(content);
			mDialog.setCancelable(cancelable);
			mDialog.show();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 联网方法
	 * 
	 * @param params
	 * @return
	 */
	protected abstract DataResult toNet(String[] params);

	/**
	 * 联网成功
	 * 
	 * @param result
	 */
	protected abstract void success(DataResult result);

	/**
	 * 联网失败
	 * 
	 * @param result
	 */
	protected abstract void failure(DataResult result);

	/**
	 * 设置loading对话栏显示信息
	 * 
	 * @return
	 */
	protected String getDialogMessage() {
		return "提交中.....";
	}
}
