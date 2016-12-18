/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 mob.com. All rights reserved.
 */

package cn.sharesdk.onekeyshare.themes.classic.port;

import android.content.Context;

import com.mob.tools.utils.R;

import java.lang.reflect.Field;
import java.util.ArrayList;

import cn.sharesdk.onekeyshare.themes.classic.PlatformPage;
import cn.sharesdk.onekeyshare.themes.classic.PlatformPageAdapter;

/**
 * 竖屏的九宫格页面适配器
 */
public class PlatformPageAdapterPort extends PlatformPageAdapter {
    private static final int DESIGN_SCREEN_WIDTH_P = 720;
    private static final int DESIGN_SEP_LINE_WIDTH = 1;
    private static final int DESIGN_LOGO_HEIGHT = 76;
    private static final int DESIGN_PADDING_TOP = 20;
    private static  int PAGE_SIZE_P = 4;
    private static final int LINE_SIZE_P = 1;

    public PlatformPageAdapterPort(PlatformPage page, ArrayList<Object> cells) {
        super(page, cells);
    }

//	protected void calculateSize(Context context, ArrayList<Object> plats) {
//		int screenWidth = R.getScreenWidth(context);
//		lineSize = LINE_SIZE_P;
//		Log.d("QQQQQQQ",lineSize+"FFFFFFFFFFFFFF");
//		float ratio = ((float) screenWidth) / DESIGN_SCREEN_WIDTH_P;
//		sepLineWidth = (int) (DESIGN_SEP_LINE_WIDTH * ratio);
//		sepLineWidth = sepLineWidth < 1 ? 1 : sepLineWidth;
//		logoHeight = (int) (DESIGN_LOGO_HEIGHT * ratio);
//		paddingTop = (int) (DESIGN_PADDING_TOP * ratio);
//		bottomHeight = (int) (DESIGN_BOTTOM_HEIGHT * ratio);
//		cellHeight = (screenWidth - sepLineWidth * 3) / 4;
//		if (plats.size() <= lineSize) {
//			panelHeight = cellHeight + sepLineWidth;
//		} else if (plats.size() <= PAGE_SIZE_P - lineSize) {
//			panelHeight = (cellHeight + sepLineWidth) * 2;
//		} else {
//			panelHeight = (cellHeight + sepLineWidth) * 3;
//		}
//	}

    protected void calculateSize(Context context, ArrayList<Object> plats) {
        int screenWidth = R.getScreenWidth(context);
        int screenHight = R.getScreenHeight(context);
        lineSize = LINE_SIZE_P;
        if(plats.size()>10){
            PAGE_SIZE_P=10;
        }else{
            PAGE_SIZE_P = plats.size();
        }
        float ratio = ((float) screenWidth) / DESIGN_SCREEN_WIDTH_P;
        sepLineWidth = (int) (DESIGN_SEP_LINE_WIDTH * ratio);
        sepLineWidth = sepLineWidth < 1 ? 1 : sepLineWidth;
        logoHeight = (int) (DESIGN_LOGO_HEIGHT * ratio);
        paddingTop = (int) (DESIGN_PADDING_TOP * ratio);
        bottomHeight = (int) (DESIGN_BOTTOM_HEIGHT * ratio);
        cellHeight = (screenHight - getStatusBarHeight(context)) / PAGE_SIZE_P;
        panelHeight = screenHight;
    }

    private int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

    protected void collectCells(ArrayList<Object> plats) {
        int count = plats.size();
        if (count < PAGE_SIZE_P) {
            int lineCount = (count / lineSize);
            if (count % lineSize != 0) {
                lineCount++;
            }
            cells = new Object[1][lineCount * lineSize];
        } else {
            int pageCount = (count / PAGE_SIZE_P);
            if (count % PAGE_SIZE_P != 0) {
                pageCount++;
            }
            cells = new Object[pageCount][PAGE_SIZE_P];
        }

        for (int i = 0; i < count; i++) {
            int p = i / PAGE_SIZE_P;
            cells[p][i - PAGE_SIZE_P * p] = plats.get(i);
        }
    }

}
