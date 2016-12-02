package net.accessiblility.app.store.viewpage;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class ParentViewPager extends ViewPager {

	public ParentViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ParentViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	/**
	 *
	 */
	@Override
	protected boolean canScroll(View v, boolean b, int dx, int x, int y) {
		// TODO Auto-generated method stub
		if (v != this && v instanceof ViewPager) {//�ж��Ƿ�Ϊ�ڲ�ViewPager
			ViewPager vp = (ViewPager) v;
			int currentItem = vp.getCurrentItem();
			int countItem = vp.getAdapter().getCount();
			if (currentItem == (countItem - 1) && dx < 0 || currentItem == 0
					&& dx > 0) {//

				return false;
			}
			return true;
		}

		return super.canScroll(v, b, dx, x, y);
	}

}
