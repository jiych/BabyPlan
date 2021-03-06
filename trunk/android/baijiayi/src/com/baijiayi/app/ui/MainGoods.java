package com.baijiayi.app.ui;
import com.baijiayi.app.R;
import greendroid.widget.QuickActionWidget;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.baijiayi.app.AppContext;
import com.baijiayi.app.AppException;
import com.baijiayi.app.AppManager;
import com.baijiayi.app.adapter.ListViewGoodsAdapter;
import com.baijiayi.app.bean.AdResult;
import com.baijiayi.app.bean.GoodsItemList;
import com.baijiayi.app.bean.GoodsModel;
import com.baijiayi.app.bean.Notice;
import com.baijiayi.app.common.StringUtils;
import com.baijiayi.app.common.UIHelper;
import com.baijiayi.app.common.UpdateManager;
import com.baijiayi.app.widget.BadgeView;
import com.baijiayi.app.widget.NewDataToast;
import com.baijiayi.app.widget.PullToRefreshListView;

/**
 * 应用程序首页
 * @author liux 
 * @version 1.0
 * @created 2012-3-21
 */
public class MainGoods extends Activity {

	private RadioButton[] mButtons;
	private String[] mHeadTitles;
	private int mViewCount;
	private int mCurSel;
	
	private ProgressBar mHeadProgress;
	
	private ImageButton mHead_set;
	private Button mHeadPub_post;
	
	
	private int curGoodsCatalog = GoodsItemList.CATALOG_ALL;
	
	
	private PullToRefreshListView lvGoods;
	
	
	private ListViewGoodsAdapter lvGoodsAdapter;
	
	private List<GoodsModel> lvGoodsData = new ArrayList<GoodsModel>();
	
	private Handler lvGoodsHandler;
	
	
	private int lvGoodsSumData;
	
	private ImageView fbSetting;
	
	
	private Button framebtn_Goods_all;
	private Button framebtn_Goods_clothes;
	private Button framebtn_Goods_toys;
	private Button framebtn_Goods_others;
	
	private View lvGoods_footer;
	
	private TextView lvGoods_foot_more;
	
	private ProgressBar lvGoods_foot_progress;
	
	
	public static BadgeView bv_active;
	public static BadgeView bv_message;
	public static BadgeView bv_atme;
	public static BadgeView bv_review;
	
    private QuickActionWidget mGrid;//快捷栏控件
	
	
	private AppContext appContext;//全局Context
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_goods);
        
        AppManager.getAppManager().addActivity(this);
        
        appContext = (AppContext)getApplication();
        //网络连接判断
        if(!appContext.isNetworkConnected())
        	UIHelper.ToastMessage(this, R.string.network_not_connected);
        //初始化登录
        appContext.initLoginInfo();
		
		this.initHeadView();    
        this.initFrameButton();
        this.initFrameListView();
        
        //检查新版本
        UpdateManager.getUpdateManager().checkAppUpdate(this, false);
        
    }
 
    /**
     * 初始化所有ListView
     */
    private void initFrameListView()
    {
    	//初始化listview控件
		this.initGoodsListView();
		//加载listview数据
		this.initFrameListViewData();
    }
    /**
     * 初始化所有ListView数据
     */
    private void initFrameListViewData()
    {
        //初始化Handler 
        
    	lvGoodsHandler = this.getLvHandler(lvGoods, lvGoodsAdapter, lvGoods_foot_more, lvGoods_foot_progress, AppContext.PAGE_SIZE);
        //加载资讯数据
		if(lvGoodsData.isEmpty()) {
			loadLvGoodsData(curGoodsCatalog, 0, lvGoodsHandler, UIHelper.LISTVIEW_ACTION_INIT);
		}
    }

    private void initGoodsListView()
    {   
        lvGoodsAdapter = new ListViewGoodsAdapter(this, lvGoodsData, R.layout.goods_listitem);        
        lvGoods_footer = getLayoutInflater().inflate(R.layout.listview_footer, null);
        lvGoods_foot_more = (TextView)lvGoods_footer.findViewById(R.id.listview_foot_more);
        lvGoods_foot_progress = (ProgressBar)lvGoods_footer.findViewById(R.id.listview_foot_progress);
        lvGoods = (PullToRefreshListView)findViewById(R.id.frame_listview_goods);
        lvGoods.addFooterView(lvGoods_footer);//添加底部视图  必须在setAdapter前
        lvGoods.setAdapter(lvGoodsAdapter); 
        lvGoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		//点击头部、底部栏无效
        		if(position == 0 || view == lvGoods_footer) return;
        		
        		GoodsModel goods = null;	
        		//判断是否是TextView
        		if(view instanceof TextView){
        			goods = (GoodsModel)view.getTag();
        		}else{
        			TextView tv = (TextView)view.findViewById(R.id.goods_listitem_title);
        			goods = (GoodsModel)tv.getTag();
        		}
        		if(goods == null) return;        		
        		
        		//跳转到动弹详情&评论页面
        		UIHelper.showGoodsDetail(view.getContext(), goods.getId());
        	}        	
		});
        lvGoods.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				lvGoods.onScrollStateChanged(view, scrollState);
				
				//数据为空--不用继续下面代码了
				if(lvGoodsData.isEmpty()) return;
				
				//判断是否滚动到底部
				boolean scrollEnd = false;
				try {
					if(view.getPositionForView(lvGoods_footer) == view.getLastVisiblePosition())
						scrollEnd = true;
				} catch (Exception e) {
					scrollEnd = false;
				}
				
				int lvDataState = StringUtils.toInt(lvGoods.getTag());
				if(scrollEnd && lvDataState==UIHelper.LISTVIEW_DATA_MORE)
				{
					lvGoods_foot_more.setText(R.string.load_ing);
					lvGoods_foot_progress.setVisibility(View.VISIBLE);
					//当前pageIndex
					int pageIndex = lvGoodsSumData/AppContext.PAGE_SIZE;
					loadLvGoodsData(curGoodsCatalog, pageIndex, lvGoodsHandler, UIHelper.LISTVIEW_ACTION_SCROLL);
				}
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
				lvGoods.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
			}
		});
        lvGoods.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				//点击头部、底部栏无效
        		if(position == 0 || view == lvGoods_footer) return false;
				
        		GoodsModel _goods = null;
        		//判断是否是TextView
        		if(view instanceof TextView){
        			_goods = (GoodsModel)view.getTag();
        		}else{
    				TextView tv = (TextView)view.findViewById(R.id.tweet_listitem_username);
    				_goods = (GoodsModel)tv.getTag();
        		} 
        		if(_goods == null) return false;		
				
				return true;
			}        	
		});
        lvGoods.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
			public void onRefresh() {
            	loadLvGoodsData(curGoodsCatalog, 0, lvGoodsHandler, UIHelper.LISTVIEW_ACTION_REFRESH);
            }
        });			
    }
    
    private void initHeadView()
    {
    	
    	mHeadProgress = (ProgressBar)findViewById(R.id.main_head_progress);
    	
    	mHead_set = (ImageButton)findViewById(R.id.main_head_set);
    	mHeadPub_post = (Button)findViewById(R.id.main_head_pub_post);
    	
    	mHead_set.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//UIHelper.showQuestionPub(v.getContext());
				Intent intent = new Intent(MainGoods.this,MainSetting.class);
				startActivity(intent);
			}
		});
    	
    	mHeadPub_post.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
	    		//判断登录
				int uid = appContext.getLoginUid();
				if(uid == 0){
					UIHelper.showLoginDialog(MainGoods.this);
					return;
				}   
				AdResult result = appContext.TryLogin();
				if(result.OK())
				{
					UIHelper.showGoodsPub(v.getContext());
				}
				else
				{
					appContext.Logout();
					UIHelper.ToastMessage(v.getContext(), result.getErrorMessage());
					UIHelper.showLoginDialog(MainGoods.this);
					return;
				}
			}
		});
    }
   /**
     * 初始化各个主页的按钮(资讯、问答、动弹、动态、留言)
     */
    private void initFrameButton()
    {
    	framebtn_Goods_all= (Button)findViewById(R.id.frame_btn_goods_all);
    	framebtn_Goods_clothes= (Button)findViewById(R.id.frame_btn_goods_clothes);
    	framebtn_Goods_toys= (Button)findViewById(R.id.frame_btn_goods_toys);
    	framebtn_Goods_others= (Button)findViewById(R.id.frame_btn_goods_others);
    	
    	framebtn_Goods_all.setEnabled(false);
    	
    	framebtn_Goods_all.setOnClickListener(frameGoodsBtnClick(framebtn_Goods_all,GoodsItemList.CATALOG_ALL));
    	framebtn_Goods_clothes.setOnClickListener(frameGoodsBtnClick(framebtn_Goods_clothes,GoodsItemList.CATALOG_CLOTHES));
    	framebtn_Goods_toys.setOnClickListener(frameGoodsBtnClick(framebtn_Goods_toys,GoodsItemList.CATALOG_TOYS));
    	framebtn_Goods_others.setOnClickListener(frameGoodsBtnClick(framebtn_Goods_others,GoodsItemList.CATALOG_OHERS));
    	
    }
    
    private View.OnClickListener frameGoodsBtnClick(final Button btn,final int catalog){
    	return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
		    	if(btn == framebtn_Goods_all)
		    		framebtn_Goods_all.setEnabled(false);
		    	else
		    		framebtn_Goods_all.setEnabled(true);
		    	if(btn == framebtn_Goods_clothes)
		    		framebtn_Goods_clothes.setEnabled(false);
		    	else
		    		framebtn_Goods_clothes.setEnabled(true);
		    	if(btn == framebtn_Goods_toys)
		    		framebtn_Goods_toys.setEnabled(false);
		    	else
		    		framebtn_Goods_toys.setEnabled(true);	
		    	if(btn == framebtn_Goods_others)
		    		framebtn_Goods_others.setEnabled(false);
		    	else
		    		framebtn_Goods_others.setEnabled(true);	

		    	
				lvGoods_foot_more.setText(R.string.load_more);
				lvGoods_foot_progress.setVisibility(View.GONE);
				
				curGoodsCatalog = catalog;
				loadLvGoodsData(curGoodsCatalog, 0, lvGoodsHandler, UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG);		    	
			}
		};
    }
 
    /**
     * 获取listview的初始化Handler
     * @param lv
     * @param adapter
     * @return
     */
    private Handler getLvHandler(final PullToRefreshListView lv,final BaseAdapter adapter,final TextView more,final ProgressBar progress,final int pageSize){
    	return new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what >= 0){
					//listview数据处理
					Notice notice = handleLvData(msg.what, msg.obj, msg.arg2, msg.arg1);
					
					if(msg.what < pageSize){
						lv.setTag(UIHelper.LISTVIEW_DATA_FULL);
						adapter.notifyDataSetChanged();
						more.setText(R.string.load_full);
					}else if(msg.what == pageSize){
						lv.setTag(UIHelper.LISTVIEW_DATA_MORE);
						adapter.notifyDataSetChanged();
						more.setText(R.string.load_more);
					}
					//发送通知广播
					if(notice != null){
						UIHelper.sendBroadCast(lv.getContext(), notice);
					}
				}
				else if(msg.what == -1){
					//有异常--显示加载出错 & 弹出错误消息
					lv.setTag(UIHelper.LISTVIEW_DATA_MORE);
					more.setText(R.string.load_error);
					((AppException)msg.obj).makeToast(MainGoods.this);
				}
				if(adapter.getCount()==0){
					lv.setTag(UIHelper.LISTVIEW_DATA_EMPTY);
					more.setText(R.string.load_empty);
				}
				progress.setVisibility(View.GONE);
				mHeadProgress.setVisibility(View.GONE);
				if(msg.arg1 == UIHelper.LISTVIEW_ACTION_REFRESH){
					lv.onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
					lv.setSelection(0);
				}else if(msg.arg1 == UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG){
					lv.onRefreshComplete();
					lv.setSelection(0);
				}
			}
		};
    }
    /**
     * listview数据处理
     * @param what 数量
     * @param obj 数据
     * @param objtype 数据类型
     * @param actiontype 操作类型
     * @return notice 通知信息
     */
    private Notice handleLvData(int what,Object obj,int objtype,int actiontype){
    	Notice notice = null;
		switch (actiontype) {
			case UIHelper.LISTVIEW_ACTION_INIT:
			case UIHelper.LISTVIEW_ACTION_REFRESH:
			case UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG:
				int newdata = 0;//新加载数据-只有刷新动作才会使用到
				switch (objtype) {
					case UIHelper.LISTVIEW_DATATYPE_GOODS:
						GoodsItemList tlist = (GoodsItemList)obj;
						notice = tlist.getNotice();
						lvGoodsSumData = what;
						if(actiontype == UIHelper.LISTVIEW_ACTION_REFRESH){
							if(lvGoodsData.size() > 0){
								for(GoodsModel goods1 : tlist.getGoodslist()){
									boolean b = false;
									for(GoodsModel goods2 : lvGoodsData){
										if(goods1.getId() == goods2.getId()){
											b = true;
											break;
										}
									}
									if(!b) newdata++;
								}
							}else{
								newdata = what;
							}
						}
						lvGoodsData.clear();//先清除原有数据
						lvGoodsData.addAll(tlist.getGoodslist());
						break;
				}
				if(actiontype == UIHelper.LISTVIEW_ACTION_REFRESH){
					//提示新加载数据
					if(newdata >0)
						NewDataToast.makeText(this, getString(R.string.new_data_toast_message, newdata), true).show();
					else
						NewDataToast.makeText(this, getString(R.string.new_data_toast_none), false).show();
				}
				break;
			case UIHelper.LISTVIEW_ACTION_SCROLL:
				switch (objtype) {
					case UIHelper.LISTVIEW_DATATYPE_GOODS:
						GoodsItemList tlist = (GoodsItemList)obj;
						notice = tlist.getNotice();
						lvGoodsSumData += what;
						if(lvGoodsData.size() > 0){
							for(GoodsModel goods1 : tlist.getGoodslist()){
								boolean b = false;
								for(GoodsModel goods2 : lvGoodsData){
									if(goods1.getId() == goods2.getId()){
										b = true;
										break;
									}
								}
								if(!b) lvGoodsData.add(goods1);
							}
						}else{
							lvGoodsData.addAll(tlist.getGoodslist());
						}
						break;
				}
				break;
		}
		return notice;
    }
	
	private void loadLvGoodsData(final int catalog,final int pageIndex,final Handler handler,final int action){  
		mHeadProgress.setVisibility(View.VISIBLE);
		new Thread(){
			@Override
			public void run() {
				Message msg = new Message();
				boolean isRefresh = false;
				if(action == UIHelper.LISTVIEW_ACTION_REFRESH || action == UIHelper.LISTVIEW_ACTION_SCROLL)
					isRefresh = true;
				try {
					GoodsItemList list = appContext.getGoodsList(catalog, pageIndex, isRefresh);				
					msg.what = list.getPageSize();
					msg.obj = list;
	            } catch (AppException e) {
	            	e.printStackTrace();
	            	msg.what = -1;
	            	msg.obj = e;
	            }
				msg.arg1 = action;
				msg.arg2 = UIHelper.LISTVIEW_DATATYPE_GOODS;
				if(curGoodsCatalog == catalog)
					handler.sendMessage(msg);
			}
		}.start();
	}
	
	/**
	 * 创建menu TODO 停用原生菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
		//MenuInflater inflater = getMenuInflater();
		//inflater.inflate(R.menu.main_menu, menu);
		//return true;
	}
	
	/**
	 * 菜单被显示之前的事件
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		UIHelper.showMenuLoginOrLogout(this, menu);
		return true;
	}
	
	/**
	 * 监听返回--是否退出程序
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			//是否退出应用
			UIHelper.Exit(this);
		}else if(keyCode == KeyEvent.KEYCODE_MENU){
			//展示快捷栏&判断是否登录
			UIHelper.showSettingLoginOrLogout(MainGoods.this, mGrid.getQuickAction(0));
			mGrid.show(fbSetting, true);
		}else if(keyCode == KeyEvent.KEYCODE_SEARCH){
			//展示搜索页
			UIHelper.showSearch(MainGoods.this);
		}
		return true;
	}
}

