package net.accessiblility.app.store.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import net.accessiblility.app.store.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 分享列表
 * Created by SiLiPing on 2016/6/17.
 */
public class ShareDialog {
	
	private AlertDialog dialog;
	private GridView gridView;
	private RelativeLayout cancelButton;
	private SimpleAdapter saImageItems;
	private int[] image={R.mipmap.sns_wechat, R.mipmap.sns_wechat_moments, R.mipmap.sns_sina,
			R.mipmap.sns_qq, R.mipmap.sns_qzone };
	private String[] name={"到微信","到微信朋友圈","到新浪微博","到QQ","到QQ空间"};

	public ShareDialog(Context context){
		dialog=new AlertDialog.Builder(context).create();
		dialog.setCancelable(false);
		dialog.show();
		Window window = dialog.getWindow();
		window.setContentView(R.layout.share_dialog);
		gridView=(GridView) window.findViewById(R.id.share_gridView);
		cancelButton=(RelativeLayout) window.findViewById(R.id.share_cancel);
		List<HashMap<String, Object>> shareList=new ArrayList<HashMap<String,Object>>();
		for(int i=0;i<image.length;i++){
			HashMap<String, Object> map = new HashMap<String, Object>();
	        map.put("ItemImage", image[i]);//添加图像资源的ID  
	        map.put("ItemText", name[i]);//按序号做ItemText  
	        shareList.add(map);
		}
		saImageItems = new SimpleAdapter(context, shareList,
				R.layout.share_item,
				new String[] {"ItemImage","ItemText"},
				new int[] {R.id.imageView1, R.id.textView1});
		gridView.setAdapter(saImageItems);
	}
	
	public void setCancelButtonOnClickListener(OnClickListener Listener){
		cancelButton.setOnClickListener(Listener);
	}
	
	public void setOnItemClickListener(OnItemClickListener listener){
		gridView.setOnItemClickListener(listener);
	}
			
	
	/**
	 * 关闭对话框
	 */
	public void dismiss() {
		dialog.dismiss();
	}
}