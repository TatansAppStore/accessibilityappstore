package net.accessiblility.app.store.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ListView;
import android.widget.TextView;


import net.accessiblility.app.store.R;
import net.accessiblility.app.store.adapter.ListViewAdapter;
import net.accessiblility.app.store.utils.AppUtils;
import net.accessiblility.app.store.utils.ListContantsUtil;
import net.accessiblility.app.store.utils.SortListUtil;
import net.accessiblility.app.store.view.MySideBar;
import net.tatans.coeus.network.tools.TatansToast;

public class ListViewIndexActivity extends BaseActivity implements MySideBar.OnTouchingLetterChangedListener {

    private ListView lvShow;
    private List<String> userInfos = new ArrayList<String>();
    private MySideBar myView;
        private TextView overlay, head;
    private View hintLayout = null;
    private ListViewAdapter adapter;

    String overlayName = null;
    private OverlayThread overlayThread = new OverlayThread();

    int beforeFirstVisiablePosition = 0;
    AlphaAnimation animation = null;
    private String mString;
    private String uninstall = "卸载";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle(uninstall);
        setTitle(uninstall);
        setContentView(R.layout.test_listview_index_layout);

        lvShow = (ListView) findViewById(R.id.lvShow);
        myView = (MySideBar) findViewById(R.id.myView);

        head = (TextView) findViewById(R.id.list_index_tv_head);
        hintLayout = findViewById(R.id.head_hint_layout);

        overlay = (TextView) findViewById(R.id.tvLetter);
        lvShow.setTextFilterEnabled(true);
        overlay.setVisibility(View.INVISIBLE);

        adapter = new ListViewAdapter(ListViewIndexActivity.this, myView, userInfos, head);

        lvShow.setAdapter(adapter);
        lvShow.setOnScrollListener(adapter);
        getUserInfos();

        myView.setOnTouchingLetterChangedListener(this);
        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("EEEEEEEEEE", v.getId() + "");
            }
        });
//		animation = new AlphaAnimation(1f, 0f);
//		animation.setDuration(1000);
//		animation.setAnimationListener(new AnimationListener() {
//			
//			@Override
//			public void onAnimationStart(Animation animation) {
//				
//			}
//			
//			@Override
//			public void onAnimationRepeat(Animation animation) {
//				
//			}
//			
//			@Override
//			public void onAnimationEnd(Animation animation) {
//				hintLayout.setVisibility(View.INVISIBLE);
//			}
//		});

    }

    @SuppressLint("NewApi")
    private void getUserInfos() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//				Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
//				if (cursor != null && cursor.moveToFirst()) {
//					do {
//						String string = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//						userInfos.add(string);
//					} while (cursor.moveToNext());
//					cursor.close();
//				}
                userInfos = AppUtils.getLocalAppName(ListViewIndexActivity.this);//耗时工作
                SortListUtil sortListUtil = new SortListUtil();
                Collections.sort(userInfos, sortListUtil);
                ListContantsUtil.putNameIndexToMap(userInfos, sortListUtil.getPinyinMap());
//                adapter.notifyDataSetChanged();
                adapter = new ListViewAdapter(ListViewIndexActivity.this, myView, userInfos, head);
                lvShow.setAdapter(adapter);
                lvShow.setOnScrollListener(adapter);
                myView.setIndexList(ListContantsUtil.AbcList, ListContantsUtil.indexPositionMap);
                myView.setColorWhenListViewScrolling(0);
            }
        },100);
    }

    private Handler handler = new Handler() {
    };

    private class OverlayThread implements Runnable {

        public void run() {
            overlay.setVisibility(View.GONE);
            TatansToast.showAndCancel(mString);
        }

    }

    @Override
    public void onTouchingLetterChanged(String s) {
        showSelectionToast(s);
        if (ListContantsUtil.indexPositionMap.containsKey(s)) {
            lvShow.setSelection(ListContantsUtil.indexPositionMap.get(s));
        }

    }

    public void showSelectionToast(String s) {
        mString = s;
//        head.setText(s);
        overlay.setText(s);
        overlay.setVisibility(View.VISIBLE);
        handler.removeCallbacks(overlayThread);
        handler.postDelayed(overlayThread, 100);
    }

    public void configHeadView(int offset, int height, int section) {
        int position = lvShow.getFirstVisiblePosition();
        if (offset <= 4 && beforeFirstVisiablePosition != position) {
            setHeadView(0, ListContantsUtil.AbcList.get(section));
        } else {
            setHeadView(-(height - offset), ListContantsUtil.AbcList.get(section - 1));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void setFirstVisiablePosition(int position) {

    }

    public void setHeadView(int offset, String string) {
        head.setText(string);
        hintLayout.setPadding(0, offset, 0, 0);
        setHeadViewVisibility(View.VISIBLE);
    }

    public void setHeadViewVisibility(int visibility) {
        if (visibility == View.INVISIBLE) {
//			hintLayout.startAnimation(animation);
        } else {
            hintLayout.setVisibility(visibility);
        }

    }
}