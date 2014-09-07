package com.cn.android.zhengxun.app.widgets;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.cn.android.zhengxun.app.R;
import com.cn.android.zhengxun.app.util.AppHelper;
import com.cn.android.zhengxun.app.util.ScreenManager;



@SuppressLint("UseSparseArrays")
public class FootBarView extends FrameLayout implements OnClickListener,
		AnimationListener, Runnable
{

	public static final String TAG = FootBarView.class.getSimpleName();
	public static final int ANIM_DURATION = 300;
	private View homeImage, classifyImage,perCenterImage,
			moreImage;
	private ImageView currentImage;
	private OnClickMenuBarLisntener onClickMenuBarLisntener;
	private Map<Integer, View> viewMap;
	private int startXDelta = 0;
	private int footItemWidth = 0;
	private Handler handler = new Handler();
	private int currentIndex;

	public interface OnClickMenuBarLisntener
	{
		public void onClickMenuBar(int index);
	}

	public FootBarView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		viewMap = new HashMap<Integer, View>();
		selfInitView();
	}

	private void selfInitListener()
	{
		homeImage.setOnClickListener(this);
		classifyImage.setOnClickListener(this);
		perCenterImage.setOnClickListener(this);
		moreImage.setOnClickListener(this);
	}

	private void selfInitView()
	{
		LayoutInflater.from(getContext()).inflate(R.layout.foot_bar, this);
		footItemWidth = ScreenManager.getInstance().getScreenWidth() / 4;
	}

	@Override
	protected void onFinishInflate()
	{

		currentImage = (ImageView) findViewById(R.id.img_selected);
		LayoutParams layoutParams = (LayoutParams) currentImage
				.getLayoutParams();
		layoutParams.width = footItemWidth;
		currentImage.setLayoutParams(layoutParams);
		homeImage = (ImageView) findViewById(R.id.img_daily_work);
		classifyImage = (ImageView) findViewById(R.id.img_sell_assistant);
		perCenterImage = (ImageView) findViewById(R.id.img_personal_center);
		moreImage = findViewById(R.id.img_more);

		viewMap.put(Integer.parseInt(homeImage.getTag().toString()), homeImage);
		viewMap.put(Integer.parseInt(classifyImage.getTag().toString()),
				classifyImage);
		viewMap.put(Integer.parseInt(perCenterImage.getTag().toString()),
				perCenterImage);
		viewMap.put(Integer.parseInt(moreImage.getTag().toString()), moreImage);
		selfInitListener();
	}

	@Override
	public void onClick(View v)
	{
		if (onClickMenuBarLisntener == null)
		{
			throw new RuntimeException("not set OnClickFootBarLisntener");
		}
		if (AppHelper.FobbitMultClick.isMultClick())
		{
			// 0.5秒内多次点击只处理一次click事件
			return;
		}
		int selectIndex = Integer.parseInt(v.getTag().toString());
		// handler.postDelayed(this, Math.abs((selectIndex - currentIndex))
		// * ANIM_DURATION);
		if (selectIndex != 3)
		{
			handler.postDelayed(this, ANIM_DURATION);
			setSelectTab(selectIndex);
			currentIndex = selectIndex;
		} else
		{
			onClickMenuBarLisntener.onClickMenuBar(3);
		}
	}

	public void setOnClickMenuBarLisntener(
			OnClickMenuBarLisntener onClickMenuBarLisntener)
	{
		this.onClickMenuBarLisntener = onClickMenuBarLisntener;
	}

	/**
	 * 点击FootBar项处理
	 * 
	 * @param selectIndex
	 */

	public void setSelectTab(int selectIndex)
	{
		Animation transAnim = new TranslateAnimation(startXDelta, selectIndex
				* footItemWidth, 0, 0);
		// transAnim.setDuration(Math.abs((selectIndex - currentIndex))
		// * ANIM_DURATION); //速度均等
		transAnim.setDuration(ANIM_DURATION); // 时间均等
		transAnim.setFillEnabled(true);
		transAnim.setFillBefore(true);
		transAnim.setFillAfter(true);
		transAnim.setAnimationListener(this);
		currentImage.startAnimation(transAnim);
	}

	/**
	 * 设置高亮
	 */
	public void setCurrentHightlight()
	{
		if (!currentImage.isShown())
		{
			currentImage.setVisibility(View.VISIBLE);
		}
		currentImage.clearAnimation();
		setSelectTab(currentIndex);
	}

	/**
	 * 取消高亮
	 */
	public void cancelCurrentHightlight()
	{
		if (currentImage.isShown())
		{
			currentImage.setVisibility(View.GONE);
		}
		currentImage.clearAnimation();
	}

	/**
	 * 设置底部导航栏外边距
	 */
	public void setFootBarMargin(int left, int top, int right, int bottom)
	{
		MarginLayoutParams lp = (MarginLayoutParams) getLayoutParams();
		lp.setMargins(left, top, right, bottom);
		setLayoutParams(lp);
	}

	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		onClickMenuBarLisntener.onClickMenuBar(currentIndex);
	}

	@Override
	public void onAnimationStart(Animation animation)
	{
		// TODO Auto-generated method stub
		startXDelta = currentIndex * footItemWidth;
	}

	@Override
	public void onAnimationEnd(Animation animation)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationRepeat(Animation animation)
	{
		// TODO Auto-generated method stub

	}

	public View getMoreView()
	{
		return moreImage;
	}
}