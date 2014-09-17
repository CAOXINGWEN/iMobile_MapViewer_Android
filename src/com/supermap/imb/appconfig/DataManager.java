package com.supermap.imb.appconfig;

import java.io.File;
import java.util.ArrayList;

import com.supermap.data.Datasets;
import com.supermap.data.Datasource;
import com.supermap.data.DatasourceConnectionInfo;
import com.supermap.data.EngineType;
import com.supermap.data.Workspace;
import com.supermap.data.WorkspaceConnectionInfo;
import com.supermap.data.WorkspaceType;

public class DataManager {

	private String mWorkspaceServer = "";
	
	private Datasets mOpenedDatasets = null;
	
	private String mDisplayMapName = "";
	
	protected Workspace mWorkspace = null;
	private int indexOfServer = -1;
	private boolean isDataOpen = false;
	private ArrayList <String> mWorkspaceServerList = null;
	private ArrayList <Workspace> mWorkspaceList = null;
	/**
	 * ���캯��
	 */
	public DataManager() {
		mWorkspace = new Workspace();
		mWorkspaceServerList = new ArrayList<String> ();
		mWorkspaceList = new ArrayList<Workspace> ();
	}
	
	/**
	 * ��ȡ�����ռ�
	 * @return
	 */
	public Workspace getWorkspace(){
		if(indexOfServer>-1)
			mWorkspace = mWorkspaceList.get(indexOfServer);
		return mWorkspace;
	}
	
	/**
	 * ������Դ
	 * @param udb
	 * @return
	 */
	public boolean openUDB(String udb){
		Datasource  ds = null;
		boolean hasOpened = false;
		//�ȿ�����û�������
		for(int i=mWorkspace.getDatasources().getCount()-1;i>=0;i--){
			ds  = mWorkspace.getDatasources().get(i);
			if(ds.getConnectionInfo().getServer().equals(udb)){
				hasOpened = true;
				break;
			}
		}
		if(!hasOpened){
			DatasourceConnectionInfo dsInfo = new DatasourceConnectionInfo();
			dsInfo.setServer(udb);
			String alias = udb.replace(".udb", "")+"_temp";
			dsInfo.setAlias(alias);
			dsInfo.setEngineType(EngineType.UDB);
			ds = mWorkspace.getDatasources().open(dsInfo);
		}
		if(ds==null){
			return false;
		}
		mOpenedDatasets = ds.getDatasets();
		return true;
	}
	
	/**
	 * ��ȡ���ݼ�����
	 * @return
	 */
	public Datasets getOpenedDatasets(){
		return mOpenedDatasets;
	}

	/**
	 * ��ȡ�����ռ��·��
	 * @return
	 */
	public String getWorkspaceServer() {
		return mWorkspaceServer;
	}

	/**
	 * ���ù����ռ��·��
	 * @param WorkspaceServer
	 */
	public void setWorkspaceServer(String WorkspaceServer) {
		if(!mWorkspaceServer.equals(WorkspaceServer)){
			this.mWorkspaceServer = WorkspaceServer;
			isDataOpen = false;
		}
	}

	/**
	 * ��ȡ��ǰ�򿪵Ĺ����ռ������
	 * @return
	 */
	public String getDisplayMapName() {
		if(isDataOpen){
			return mDisplayMapName;
		}
		return "Workspace unOpen";
	}

	/**
	 * ���ô򿪵Ĺ����ռ������
	 * @param DisplayMapName
	 */
	public void setDisplayMapName(String DisplayMapName) {
		if(isDataOpen){
			this.mDisplayMapName = DisplayMapName;
		}
	}
	
	/**
	 * �򿪹����ռ�
	 * @return
	 */
	public boolean open(){
		indexOfServer = mWorkspaceServerList.indexOf(mWorkspaceServer);
		if(indexOfServer>-1)
			return true;
		if(isDataOpen){
			return true;
		}
		File wksFile = new File(mWorkspaceServer);
		if(!wksFile.exists()){
			return false;
		}
		WorkspaceType type = null;
		if(mWorkspaceServer.endsWith(".SMWU")||mWorkspaceServer.endsWith(".smwu"))
		{
			type = WorkspaceType.SMWU;
		}else if(mWorkspaceServer.endsWith(".SXWU")||mWorkspaceServer.endsWith(".sxwu"))
		{
			type = WorkspaceType.SXWU;
		}
		WorkspaceConnectionInfo info = new WorkspaceConnectionInfo();
		info.setServer(mWorkspaceServer);
		info.setType(type);
		mWorkspace.close();
		isDataOpen = mWorkspace.open(info);
		if(isDataOpen){
			if(getMapCount()>=1){
				setDisplayMapName(getMapName(0));
			}
		}
		info.dispose();
		info = null;
		return isDataOpen;
	}
	
	/**
	 * ��⹤���ռ��Ƿ��
	 * @return
	 */
	public boolean isDataOpen()
	{
		return isDataOpen;
	}
	
	public void close(){
		mWorkspace.close();
		isDataOpen = false;
	}
	
	/**
	 * ��ȡ����Դ��Ŀ
	 * @return
	 */
	public int getDatasourceCount(){
		if(isDataOpen){
			return mWorkspace.getDatasources().getCount();
		}
		return 0;
	}
	
	/**
	 * ��ȡָ����ŵ�����Դ
	 * @param index
	 * @return
	 */
	public Datasource getDatasource(int index){
		if(isDataOpen){
			return mWorkspace.getDatasources().get(index);
		}
		return null;
	}
	
	/**
	 * ��ȡָ�����Ƶ�����Դ
	 * @param name
	 * @return
	 */
	public Datasource getDatasource(String name){
		if(isDataOpen){
			return mWorkspace.getDatasources().get(name);
		}
		return null;
	}
	
	/**
	 * ��ȡ��ͼ����
	 * @return
	 */
	public int getMapCount(){
		if(isDataOpen){
			return mWorkspace.getMaps().getCount();
		}
		return 0;
	}
	
	/**
	 * ��ȡָ����ŵ�����
	 * @param index
	 * @return
	 */
	public String getMapName(int index){
		if(isDataOpen){
			return mWorkspace.getMaps().get(index);
		}
		return null;
	}

	/**
	 * ��ʼ�������ռ��б�
	 * @param serverPath
	 */
	public void initWorkspace(String serverPath){
		if(serverPath == null)
			return;
		File wksFile = new File(serverPath);
		if(!wksFile.exists()){
			return;
		}
		if(mWorkspaceServerList.contains(serverPath)){
		   return;
		}
		 mWorkspaceServerList.add(serverPath);
		 Workspace workspace = openWorkspace(serverPath);
		 mWorkspaceList.add(workspace);
	}
	private Workspace openWorkspace(String serverPath){
		Workspace workspace = new Workspace();
		WorkspaceType type = null;
		if(serverPath.endsWith(".SMWU")||serverPath.endsWith(".smwu"))
		{
			type = WorkspaceType.SMWU;
		}else if(serverPath.endsWith(".SXWU")||serverPath.endsWith(".sxwu"))
		{
			type = WorkspaceType.SXWU;
		}
		WorkspaceConnectionInfo info = new WorkspaceConnectionInfo();
		info.setServer(serverPath);
		info.setType(type);
		boolean isOpened = workspace.open(info);				
		if(!isOpened){
			workspace.dispose();
			return null;
		}
		info.dispose();
		info = null;
		return workspace;
	}
}
