package com.lib.bandaid.widget.drag;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.lib.bandaid.utils.ViewTreeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jacob-wj on 2015/4/14.
 */
public class DragLayoutV1 extends RelativeLayout {
    private static final String TAG = "DragLayout";
    /**
     *
     */
    private Context context;
    /**
     * 当前移动的View
     */
    private int index;
    /**
     *
     */
    private List<View> views;
    /**
     *
     */
    private List<ViewMoveHolder> viewMoveHolders;

    private LongClickRunnable runnable;

    public DragLayoutV1(Context context) {
        this(context, null);
        init(context);
    }

    public DragLayoutV1(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context);
    }

    public DragLayoutV1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        viewMoveHolders = new ArrayList<>();
        this.context = context;
        //runnable = new LongClickRunnable();
    }

    private class ViewDragCallBack extends ViewDragHelper.Callback {

        int id;

        public ViewDragCallBack(int id) {
            this.id = id;
        }

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return id == child.getId();
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return getMeasuredWidth() - child.getMeasuredWidth();
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return getMeasuredHeight() - child.getMeasuredHeight();
        }

        /**
         * 处理水平方向上的拖动
         *
         * @param child 拖动的View
         * @param left  移动到x轴的距离
         * @param dx    建议的移动的x距离
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            //Log.e(TAG, "left:" + left + "++++dx:" + dx);
            //两个if主要是让view在ViewGroup中
            if (left < getPaddingLeft()) {
                return getPaddingLeft();
            }

            if (left > getWidth() - child.getMeasuredWidth()) {
                return getWidth() - child.getMeasuredWidth();
            }
            return left;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            //Log.e(TAG, "top:" + top + "++++dy:" + dy);
            //两个if主要是让view在ViewGroup中
            if (top < getPaddingTop()) {
                return getPaddingTop();
            }

            if (top > getHeight() - child.getMeasuredHeight() - getPaddingBottom()) {
                return getHeight() - child.getMeasuredHeight() - getPaddingBottom();
            }
            return top;
        }

        @Override
        public void onViewDragStateChanged(int state) {
            switch (state) {
                case ViewDragHelper.STATE_DRAGGING://正在拖动过程中
                    break;
                case ViewDragHelper.STATE_IDLE://view没有被拖动，或者正在进行fling
                    break;
                case ViewDragHelper.STATE_SETTLING://fling完毕后被放置到一个位置
                    break;
            }
            super.onViewDragStateChanged(state);
        }
    }

    private int TOUCH_SLOP = 20;
    private int lastDownX;
    private int lastDownY;
    private boolean isMoved;
    private boolean isReleased;
    private MotionEvent disPatchMotionEvent;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        /*int x = (int) ev.getX();
        int y = (int) ev.getY();
        disPatchMotionEvent = ev;*/
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                System.out.println(TAG + "事件分发》》》按下");
                /*lastDownX = x;
                lastDownY = y;
                isMoved = false;
                isReleased = false;
                postDelayed(runnable, 0);*/
                index = getView(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                System.out.println(TAG + "事件分发》》》移动");
               /* if (isMoved) {
                    break;
                }
                if (Math.abs(x - lastDownX) > TOUCH_SLOP || (Math.abs(y - lastDownY) > TOUCH_SLOP)) {
                    isMoved = true;
                }*/
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                System.out.println(TAG + "事件分发》》》抬起");
                //isReleased = true;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (index == -1) {
            /**
             * 不拦截事件 让mapView消费事件
             */
            System.out.println(TAG + "原生的事件阻拦" + index);
            return super.onInterceptTouchEvent(ev);
        } else {
            System.out.println(TAG + "V4的事件阻拦" + index);
            ViewMoveHolder viewMoveHelper = viewMoveHolders.get(index);
            ViewDragHelper viewDragHelper = viewMoveHelper.getViewDragHelper();
            switch (ev.getAction()) {
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    viewMoveHelper.getViewDragHelper().cancel();
                    break;
            }
            return viewDragHelper.shouldInterceptTouchEvent(ev);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (index == -1) {
            /**
             * 用户没有触碰到可移动控件，事件有mapView消费
             */
            System.out.println(TAG + "原生的事件消费");
            return false;
        } else {
            System.out.println(TAG + "V4的事件消费");
            ViewMoveHolder viewMoveHelper = viewMoveHolders.get(index);
            ViewDragHelper viewDragHelper = viewMoveHelper.getViewDragHelper();
            viewDragHelper.processTouchEvent(ev);
            return true;
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        getMoveViewReady();
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        getMoveViewReady();
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        getMoveViewReady();
    }

    /**
     * 遍历查询可移动控件
     */
    private void getMoveViewReady() {
        ViewDragCallBack viewDragCallBack;
        ViewMoveHolder viewMoveHelper;
        ViewDragHelper viewDragHelper;
        views = ViewTreeUtil.find(this, "move");
        for (int i = 0; i < views.size(); i++) {
            boolean flag = false;
            for (int j = 0; j < viewMoveHolders.size(); j++) {
                viewMoveHelper = viewMoveHolders.get(j);
                View view = viewMoveHelper.getView();
                if (view == views.get(i)) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                continue;
            }
            viewDragCallBack = new ViewDragCallBack(views.get(i).getId());
            viewDragHelper = ViewDragHelper.create(this, 1f, viewDragCallBack);
            viewMoveHelper = new ViewMoveHolder();
            viewMoveHelper.setView(views.get(i));
            viewMoveHelper.setViewDragHelper(viewDragHelper);
            viewMoveHolders.add(viewMoveHelper);
        }
        viewMoveHolders.size();
    }

    /**
     * 输出最后一个（显示在最上面的）
     *
     * @param ev
     * @return
     */
    private int getView(MotionEvent ev) {
        float x = ev.getX();
        float y = ev.getY();
        int p = -1;
        for (int i = 0; i < views.size(); i++) {
            View view = views.get(i);
            int w = view.getWidth();
            int h = view.getHeight();
            int left = view.getLeft();
            int top = view.getTop();
            if (x < left + w && x > left && y > top && y < top + h) {
                p = i;
            }
        }
        return p;
    }

    private int getView1(View view) {
        int p = -1;
        for (int i = 0; i < views.size(); i++) {
            View _view = views.get(i);
            if (view == _view) {
                p = i;
                break;
            }
        }
        return p;
    }

    class ViewMoveHolder {
        private View view;
        private ViewDragHelper viewDragHelper;

        public ViewMoveHolder() {

        }

        public View getView() {
            return view;
        }

        public void setView(View view) {
            this.view = view;
        }

        public ViewDragHelper getViewDragHelper() {
            return viewDragHelper;
        }

        public void setViewDragHelper(ViewDragHelper viewDragHelper) {
            this.viewDragHelper = viewDragHelper;
        }
    }

    private class LongClickRunnable implements Runnable {

        @Override
        public void run() {
            if (isReleased || isMoved) {
                index = -1;
                System.out.println("长按释放index：" + index);
            } else {
                //VibratorUtil.Vibrate(context, 200);
                index = getView(disPatchMotionEvent);
                System.out.println("长按index：" + index);
            }
        }
    }
}
