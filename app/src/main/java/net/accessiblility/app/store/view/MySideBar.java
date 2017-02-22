package net.accessiblility.app.store.view;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import net.accessiblility.app.store.utils.DisplayUtil;
import net.tatans.coeus.network.tools.TatansToast;


public class MySideBar extends View {

	// 首字母
	List<String> abcList = null;
	// 根据首字母获取对应的位置
	HashMap<String, Integer> indexPositionMap = new HashMap<String, Integer>();

	OnTouchingLetterChangedListener onTouchingLetterChangedListener;
	// 26个字母
	public String[] b = { "#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
			"S", "T", "U", "V", "W", "X", "Y", "Z" };

	static final String COLOR = "#3f3939";
	private boolean showBkg = false;
	int choose = -1;
	int scrollChoose = -1;
	Paint paint = new Paint();
	Paint rectPaint = new Paint();
	float rectWidth = 0f;
	float rectHeiht = 0f;

	float fontScale = 0f;

	int removeCount = 0;
	int charPosition = -1;

	public MySideBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		inits(context);
	}

	public MySideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		inits(context);
	}

	public MySideBar(Context context) {
		super(context);
		inits(context);
	}

	private void inits(Context context) {
		rectPaint.setColor(Color.parseColor("#000000"));
		rectWidth = paint.measureText("#");
		Rect rect = new Rect();
		paint.getTextBounds("#", 0, 1, rect);
		rectHeiht = rect.height();
		fontScale = DisplayUtil.sp2px(context, 13);

	}

	/**
	 * 去掉索引不要的字母,只支持去掉一个字母
	 * 
	 * @param charPosition
	 *            该字母所在的位置
	 */
	public void removeUselessChar(int charPosition) {
		this.charPosition = charPosition;
		this.removeCount = 1;
	}

	/**
	 * 重写这个方法
	 */
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (showBkg) {
			canvas.drawColor(Color.parseColor(COLOR));
		}

		int height = getHeight();
		int width = getWidth();
		int count = b.length;
		int singleHeight = height / (count - removeCount);
		float xRectPos = ((float) width - rectWidth) / 2.0f - rectWidth;
		float xRectPos2 = xRectPos + 3.0f * rectWidth;
		int remove_count = 0;
		for (int i = 0; i < count; i++) {
			if (charPosition == i) {
				remove_count++;
				continue;
			}
			paint.setFakeBoldText(true);
			paint.setAntiAlias(true);
			paint.setTextSize(fontScale);
			paint.setColor(Color.parseColor("#ffffff"));
			float xPos = ((float) width - paint.measureText(b[i])) / 2.0f;
			float yPos = singleHeight * (i - remove_count) + singleHeight;
			if (i == choose) {
				paint.setColor(Color.RED);
				canvas.drawRect(xRectPos, yPos - rectHeiht - 6.0f - rectHeiht / 2.0f, xRectPos2, yPos + rectHeiht / 2.0f, rectPaint);
			}
			canvas.drawText(b[i], xPos, yPos, paint);
			paint.reset();
		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	protected boolean dispatchHoverEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();
		final int oldChoose = choose;
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		final int c = (int) (y / getHeight() * b.length);
		Log.d("WWWWWWW", event.getAction() + "--------" + c);
		switch (action) {
			case MotionEvent.ACTION_HOVER_ENTER:

				showBkg = true;
				if (oldChoose != c && listener != null) {
					doOnActionDown(listener, c);
				}

				break;
			case MotionEvent.ACTION_HOVER_MOVE:  //鼠标在view上
				if (oldChoose != c && listener != null) {
					doOnActionDown(listener, c);
				}
				break;
			case MotionEvent.ACTION_HOVER_EXIT:  //鼠标离开view
				showBkg = false;
				// choose = -1;
				invalidate();
				break;
		}
		return super.onHoverEvent(event);
	}



		@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();
		final int oldChoose = choose;
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		final int c = (int) (y / getHeight() * b.length);
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			showBkg = true;
			if (oldChoose != c && listener != null) {
				doOnActionDown(listener, c);
			}

			break;
		case MotionEvent.ACTION_MOVE:

			if (oldChoose != c && listener != null) {
				doOnActionDown(listener, c);
			}
			break;
		case MotionEvent.ACTION_UP:
			showBkg = false;
			// choose = -1;
			invalidate();
			break;
		}
	return super.dispatchTouchEvent(event);
	}



//		@Override
//	public boolean onHoverEvent(MotionEvent event) {
//		final int action = event.getAction();
//		final float y = event.getY();
//		final int oldChoose = choose;
//		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
//		final int c = (int) (y / getHeight() * b.length);
//		Log.d("WWWWWWW", event.getAction() + "--------" + c);
//		switch (action) {
//			case MotionEvent.ACTION_HOVER_ENTER:
//
//				showBkg = true;
//				if (oldChoose != c && listener != null) {
//					doOnActionDown(listener, c);
//				}
//
//				break;
//			case MotionEvent.ACTION_HOVER_MOVE:  //鼠标在view上
//				if (oldChoose != c && listener != null) {
//					doOnActionDown(listener, c);
//				}
//				break;
//			case MotionEvent.ACTION_HOVER_EXIT:  //鼠标离开view
//				showBkg = false;
//				// choose = -1;
//				invalidate();
//				break;
//		}
//		return super.onHoverEvent(event);
//	}

	/**
	 * listview滚动时候设置
	 * 
	 * @param c
	 */
	public void setColorWhenListViewScrolling(int c) {
		if (scrollChoose != c) {
			scrollChoose = c;
			String string = abcList.get(c);
			for (int i = c; i < b.length; ++i) {
				if (string.equals(b[i])) {
					choose = i;
					invalidate();
					break;
				}
			}
		}
	}

	private void doOnActionDown(OnTouchingLetterChangedListener listener, int c) {
		if (c >= 0 && c < b.length) {
			if (indexPositionMap.containsKey(b[c])) {
				listener.onTouchingLetterChanged(b[c]);
				choose = c;
				invalidate();
			} else {
				c = c - 1;
				doOnActionDown(listener, c);
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	/**
	 * 向外公开的方法
	 * 
	 * @param onTouchingLetterChangedListener
	 */
	public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	/**
	 * 接口
	 * 
	 */
	public interface OnTouchingLetterChangedListener {
		public void onTouchingLetterChanged(String s);
	}

	/**
	 * 请调用其方法，目的是根据listview滑动而使其变化
	 * 
	 * @param abcList
	 * @param indexPositionMap
	 */
	public void setIndexList(List<String> abcList, HashMap<String, Integer> indexPositionMap) {
		this.indexPositionMap = indexPositionMap;
		this.abcList = abcList;
//		b = new String[abcList.size()];
//		abcList.toArray(b);
//		invalidate();
	}
}