package org.kymjs.blog.ui;

import org.kymjs.blog.R;
import org.kymjs.blog.ui.widget.dobmenu.CurtainItem.OnSwitchListener;
import org.kymjs.blog.ui.widget.dobmenu.CurtainItem.SlidingType;
import org.kymjs.blog.ui.widget.dobmenu.CurtainView;
import org.kymjs.blog.utils.KJAnimations;
import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.utils.KJLoger;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 应用Activity基类
 * 
 * @author kymjs (https://github.com/kymjs)
 * @since 2015-3
 */
public abstract class TitleBarActivity extends KJActivity {

    public ImageView mImgBack;
    public TextView mTvTitle;
    public ImageView mImgMenu;
    
    // Sliding menu object
    private CurtainView mCurtainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        try {
            mImgBack = (ImageView) findViewById(R.id.titlebar_img_back);
            mTvTitle = (TextView) findViewById(R.id.titlebar_text_title);
            mImgMenu = (ImageView) findViewById(R.id.titlebar_img_menu);
            mImgBack.setOnClickListener(this);
            mImgMenu.setOnClickListener(this);
            initCurtainView();
        } catch (NullPointerException e) {
            throw new NullPointerException(
                    "TitleBar Notfound from Activity layout");
        }
        super.onStart();
    }

    @Override
    public void widgetClick(View v) {
        super.widgetClick(v);
        switch (v.getId()) {
        case R.id.titlebar_img_back:
            onBackClick();
            break;
        case R.id.titlebar_img_menu:
            onMenuClick();
            break;
        default:
            break;
        }
    }

    protected void onBackClick() {}

    protected void onMenuClick() {}
    
    public void onCurtainPull(){
    }
    
    public void onCurtainPush(){}
    
    public CurtainView getCurtainView(){
    	return mCurtainView;
    }

    private void initCurtainView() {
        mCurtainView = new CurtainView(this, R.id.titlebar);
        mCurtainView.setSlidingView(R.layout.dob_sliding_menu);
        mCurtainView.setMaxDuration(1000);
        mCurtainView.setSlidingType(SlidingType.MOVE);
        
        mCurtainView.setOnSwitchListener(new OnSwitchListener() {
            @Override
            public void onCollapsed() {
            	onCurtainPush();
            }

            @Override
            public void onExpanded() {
            	onCurtainPull();
				mHandler.postDelayed(timerRunnable, 5000);
				 count++;
				 if (count >3){
					Toast.makeText(aty, "您也太无聊了吧", Toast.LENGTH_SHORT).show();
					count = 0;
				 }else if (count > 2){
					 mHandler.removeCallbacks(timerRunnable);
					 mHandler.postDelayed(timerRunnable, 5000);
				 }
            }
        });
        mCurtainView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				KJAnimations.shakeCurtain(mCurtainView.getContentView());
				mHandler.postDelayed(timerRunnable, 2000);
				 count++;
				 if (count >2){
					Toast.makeText(aty, "掉下来很累的，去看看博客吧", Toast.LENGTH_SHORT).show();
					count = 0;
				 }else if (count == 2){
					 mHandler.removeCallbacks(timerRunnable);
					 mHandler.postDelayed(timerRunnable, 2000);
				 }
			}
		});
    }
    
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private static int count = 0;
    
    private Runnable timerRunnable = new Runnable() {
		@Override
		public void run() {
			count = 0;
		}
	};
}