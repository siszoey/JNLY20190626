package com.lib.bandaid.arcruntime.event;

import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.lib.bandaid.arcruntime.core.ArcMap;

/**
 * Created by zy on 2019/5/28.
 */

public class ArcMapEventDispatch extends DefaultMapViewOnTouchListener implements IArcMapExtendEvent {

    public ArcMapEventDispatch(ArcMap arcMap) {
        super(arcMap.getContext(), arcMap.getMapView());
    }


    private IArcMapEvent iMapEvent;


    public IArcMapEvent getCurIMapEvent() {
        return iMapEvent;
    }

    public void setCurIMapEvent(IArcMapEvent iMapEvent) {
        this.iMapEvent = iMapEvent;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if (iMapEvent != null)
            if (iMapEvent.onDoubleTap(e)) return true;
        return super.onDoubleTap(e);
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        if (iMapEvent != null)
            if (iMapEvent.onDoubleTapEvent(e)) return true;
        return super.onDoubleTapEvent(e);
    }

    @Override
    public boolean onDoubleTouchDrag(MotionEvent event) {
        if (iMapEvent != null)
            if (iMapEvent.onDoubleTouchDrag(event)) return true;
        return super.onDoubleTouchDrag(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        if (iMapEvent != null)
            if (iMapEvent.onDown(e)) return true;
        return super.onDown(e);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (iMapEvent != null)
            if (iMapEvent.onFling(e1, e2, velocityX, velocityY)) return true;
        return super.onFling(e1, e2, velocityX, velocityY);
    }

    @Override
    public boolean onMultiPointerTap(MotionEvent event) {
        if (iMapEvent != null)
            if (iMapEvent.onMultiPointerTap(event)) return true;
        return super.onMultiPointerTap(event);
    }

    @Override
    public boolean onRotate(MotionEvent event, double rotationAngle) {
        if (iMapEvent != null)
            if (iMapEvent.onRotate(event, rotationAngle)) return true;
        return super.onRotate(event, rotationAngle);
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        if (iMapEvent != null)
            if (iMapEvent.onScale(detector)) return true;
        return super.onScale(detector);
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        if (iMapEvent != null)
            if (iMapEvent.onScaleBegin(detector)) return true;
        return super.onScaleBegin(detector);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (iMapEvent != null)
            if (iMapEvent.onScroll(e1, e2, distanceX, distanceY)) return true;
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        if (iMapEvent != null)
            if (iMapEvent.onSingleTapConfirmed(e)) return true;
        return super.onSingleTapConfirmed(e);
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (iMapEvent != null)
            if (iMapEvent.onSingleTapUp(e)) return true;
        return super.onSingleTapUp(e);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (iMapEvent != null)
            if (iMapEvent.onTouch(view, event)) return true;
        return super.onTouch(view, event);
    }

    @Override
    public boolean onUp(MotionEvent e) {
        if (iMapEvent != null)
            if (iMapEvent.onUp(e)) return true;
        return super.onUp(e);
    }

    @Override
    public void onLongPress(MotionEvent e) {
        if (iMapEvent != null) iMapEvent.onLongPress(e);
        super.onLongPress(e);
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        if (iMapEvent != null) iMapEvent.onScaleEnd(detector);
        super.onScaleEnd(detector);
    }

    @Override
    public void onShowPress(MotionEvent e) {
        if (iMapEvent != null) iMapEvent.onShowPress(e);
        super.onShowPress(e);
    }

    @Override
    public boolean onTouchStart(MotionEvent motionEvent) {
        if (iMapEvent != null)
            if (iMapEvent.onTouchStart(motionEvent)) return true;
        return false;
    }

    @Override
    public boolean onTouchMoving(MotionEvent motionEvent) {
        if (iMapEvent != null)
            if (iMapEvent.onTouchMoving(motionEvent)) return true;
        return false;
    }

    @Override
    public boolean onTouchCancel(MotionEvent motionEvent) {
        if (iMapEvent != null)
            if (iMapEvent.onTouchCancel(motionEvent)) return true;
        return false;
    }
}
