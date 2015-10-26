package com.supermap.imb.appconfig;


import com.supermap.data.Environment;
import com.supermap.imb.base.R;
import com.supermp.imb.file.MyAssetManager;
import com.supermp.imb.file.MySharedPreferences;

import android.app.Application;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class MyApplication extends Application {
	public static String SDCARD = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
	private static MyApplication sInstance = null;
	
	private DefaultDataManager mDefaultDataManager = null;
	
	private DataManager mUserDataManager = null;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		sInstance = this;
		
		//��һ���������û�����������ʼ����iMobile
		Environment.setLicensePath(DefaultDataConfig.LicPath);
		Environment.initialization(this);
		
		//��ʼ��ϵͳ��ص���
		MySharedPreferences.init(this);
		MyAssetManager.init(this);
		
		mDefaultDataManager = new DefaultDataManager();
		mUserDataManager = new DataManager();
		
//    	//��������
//		new DefaultDataConfig().autoConfig();
		
	}
	
	/**
	 * ��ȡ��ǰapplication
	 * @return
	 */
	public static MyApplication getInstance(){
		return sInstance;
	}
	
	/**
	 * ��ȡĬ�����ݶ���
	 * @return
	 */
	public DefaultDataManager getDefaultDataManager(){
		return mDefaultDataManager;
	}
	
	/**
	 * ��ȡ�û����ݶ���
	 * @return
	 */
	public DataManager getUserDataManager(){
		return mUserDataManager;
	}
	
	/**
	 * ��ʾ��Ϣ
	 * @param info ��Ҫ��ʾ����Ϣ
	 */
	public void ShowInfo(String info){
		Toast toast = Toast.makeText(sInstance, info, 500);
		toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.show();
	}
	
	/**
	 * ��ʾ������Ϣ
	 * @param err  ��Ҫ��ʾ�Ĵ�����Ϣ
	 */
	public void ShowError(String err){
		Toast toast = Toast.makeText(sInstance, "Error: "+err, 500);
		toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.getView().setBackgroundResource(R.drawable.red_round_rect);
		toast.show();
		Log.e(this.getClass().getName(), err);
	}
	
	public static int dp2px(int dp){
		return (int) (dp*sInstance.getResources().getDisplayMetrics().density);
	}
	
}
