package com.handmark.pulltorefresh.library.internal;

import com.example.droidfusdklib.R;

import android.content.Context;
import android.graphics.Matrix;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * ListView的FooterView组件
 * @author gaierlin
 */
public class ListFooterView extends LinearLayout {
	public static final String FOOTER_VIEW_STYLE_KEY = "footer_view_style";
	/**
	 * 文本样式的Footer
	 */
	public static final int TEXT_FOOTER_VIEW_STYLE = 0;
	/**
	 * Button样式的Footer
	 */
	public static final int BUTTON_FOOTER_VIEW_STYLE = 1;
	private static final int FOOTER_TEXT_MIN_SIZE = 12;//PX
	private static final int ROTATION_ANIMATION_DURATION = 1200;
	
	private int mCurrentFooterStyle = TEXT_FOOTER_VIEW_STYLE;
	private LinearLayout textFooterViewLayout;
	private ImageView loadingImg;
	private TextView loadingTextTip;
	private Button loadNextPageBtn;
	
	private Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();
	private Animation mRotateAnimation;
	private Matrix mHeaderImageMatrix;
	private LoadNextPageDataListener mListener;
	private Context mContext;

	public ListFooterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		
		mHeaderImageMatrix = new Matrix();

		mRotateAnimation = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
		mRotateAnimation.setDuration(ROTATION_ANIMATION_DURATION);
		mRotateAnimation.setRepeatCount(Animation.INFINITE);
		mRotateAnimation.setRepeatMode(Animation.RESTART);
	}

	@Override
	protected void onFinishInflate() {
		textFooterViewLayout = (LinearLayout) findViewById(R.id.text_footer_view_layout);
		loadingImg = (ImageView) findViewById(R.id.loading_img);
		loadingImg.setImageMatrix(mHeaderImageMatrix);
		
		loadingTextTip = (TextView) findViewById(R.id.text_footer_view);
		loadNextPageBtn = (Button) findViewById(R.id.btn_next_page);
		loadNextPageBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mListener!=null){
					mListener.loadingNextPageData();
					showFooterView(TEXT_FOOTER_VIEW_STYLE);
				}
			}
		});
	}

	public void setFooterStyle(int footerStyle) {
		if (footerStyle == TEXT_FOOTER_VIEW_STYLE) {
			mCurrentFooterStyle = footerStyle;
		} else {
			mCurrentFooterStyle = BUTTON_FOOTER_VIEW_STYLE;
		}
		
		showFooterView();
	}

	public void showFooterView(int style){
		if (style == TEXT_FOOTER_VIEW_STYLE) {
			showTextFooter();
		} else {
			showButtonFooter();
		}
	}
	
	public void showFooterView() {
		if (mCurrentFooterStyle == TEXT_FOOTER_VIEW_STYLE) {
			showTextFooter();
		} else {
			showButtonFooter();
		}
	}
	
	private void showButtonFooter(){
		loadingImg.setAnimation(null);
		textFooterViewLayout.setVisibility(View.GONE);
		loadNextPageBtn.setVisibility(View.VISIBLE);
	}
	
	private void showTextFooter(){
		loadingImg.startAnimation(mRotateAnimation);
		textFooterViewLayout.setVisibility(View.VISIBLE);
		loadNextPageBtn.setVisibility(View.GONE);
	}

	public void hideFooterView() {
		textFooterViewLayout.setVisibility(View.GONE);
		loadNextPageBtn.setVisibility(View.GONE);
		loadingImg.setAnimation(null);
	}
	
	public void setFooterText(String lable){
		if(!TextUtils.isEmpty(lable))
			loadingTextTip.setText(lable);
	}
	
	public void setFooterTextSize(int size){
		if(size>=FOOTER_TEXT_MIN_SIZE)
			loadingTextTip.setTextSize(size);
	}
	
	public void setFooterTextColor(int color){
		loadingTextTip.setTextColor(color);
	}
	
	/**
	 * 设置旋转的图片
	 * @param resid
	 */
	public void setFooterTextLoadImageView(int resid){
		if(resid>0){
			loadingImg.setImageResource(resid);
		}
	}
	
	/**
	 * 为按钮加载数据提供回调数据接口
	 * @param linstener
	 */
	public void setLoadNextPageDataListener(LoadNextPageDataListener linstener){
		if(linstener!=null){
			this.mListener = linstener;
		}
	}
	
	public interface LoadNextPageDataListener{
		void loadingNextPageData();
	}
	
	@Override
	public void setVisibility(int visibility){
		super.setVisibility(visibility);
		if(visibility == View.VISIBLE){
			showFooterView();
		} else{
			hideFooterView();
		}
	}
}
