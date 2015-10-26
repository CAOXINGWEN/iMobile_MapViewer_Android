package com.supermap.imb.base;

import java.io.File;
import java.io.FilenameFilter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.supermap.imb.appconfig.DefaultDataConfig;
import com.supermap.imb.appconfig.MyApplication;
import com.supermap.mapping.MapControl;

public class UserDatasourcePopup extends PopupWindow implements OnClickListener{

	private TextView mTvTitle = null;
	
	private static String[] mUDBs = null;
	static{
		File dir = new File(DefaultDataConfig.MapDataPath);
		FilenameFilter wks = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if(name.endsWith(".UDB")||name.endsWith(".udb")){
					return true;
				}
				return false;
			}
		};
		mUDBs = dir.list(wks);
	}
	
	private LayoutInflater mInflater = null; 
	
	private ListView     mListDatasource = null;
	private MapControl   mMapControl     = null;
	private View         mContentView    = null; 
	
	private UserDatasetPopup    mUserDatasetPopup    = null;
	private UserDatasourcePopup mUserDatasourcePopup = null;
	
	/**
	 * ���캯��
	 * @param mapControl   ��ͼ�ؼ�
	 */
	public UserDatasourcePopup(MapControl mapControl) {
		mMapControl = mapControl;
		mInflater = LayoutInflater.from(mapControl.getContext());

		initView();
		mUserDatasetPopup = new UserDatasetPopup(mapControl, this);
		mUserDatasourcePopup = this;
	}
	
	/**
	 * ��ʼ����ʾ������
	 */
	private void initView(){
		mContentView = mInflater.inflate(R.layout.activity_ds, null);
		setContentView(mContentView);
		
		mTvTitle = (TextView) mContentView.findViewById(R.id.common_title).findViewById(R.id.tv_title);
		mListDatasource = (ListView) mContentView.findViewById(R.id.list_datasource);
		mContentView.findViewById(R.id.common_title).findViewById(R.id.btn_back).setOnClickListener(this);

		mListDatasource.setAdapter(new DatasourceAdapter());
		mTvTitle.setText("������Դ");
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			dismiss();
			MainActivity.reset();
			break;
		default:
			break;
		}
	}
	
	
	private static class ViewHolder{
		TextView Name;
	}
	
	private class DatasourceAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mUDBs.length;
		}

		@Override
		public Object getItem(int arg0) {
			return mUDBs[arg0];
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(final int index, View convertView, ViewGroup arg2) {
			ViewHolder holder = null;
			if(convertView == null){
				convertView = mInflater.inflate(R.layout.listview_ds_item, null);
				holder = new ViewHolder();
				holder.Name = (TextView) convertView.findViewById(R.id.tv_ds_name);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			final String name = mUDBs[index];
			holder.Name.setText(name);
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					MyApplication.getInstance().getUserDataManager().openUDB(DefaultDataConfig.MapDataPath+mUDBs[index]);
					
					mUserDatasetPopup = new UserDatasetPopup(mMapControl, mUserDatasourcePopup);
					mUserDatasetPopup.show();
				}
			});
			return convertView;
		}
	}

	/**
	 * ��ʾ����Դ�б�
	 */
    public void show(){
		
		showAt(100, 140, 350, 480);
	}
    
	private void showAt(int x,int y, int width, int height)
	{
		setWidth(MyApplication.dp2px(width));
		setHeight(MyApplication.dp2px(height));
		showAtLocation(mMapControl.getRootView(), Gravity.LEFT|Gravity.TOP,MyApplication.dp2px(x), MyApplication.dp2px(y));
	}

	/**
	 * �ر�����Դ�б�
	 */
	public void dismiss(){
		
		super.dismiss();
		if(mUserDatasetPopup != null)
		    mUserDatasetPopup.dismiss();
	}
	
}
