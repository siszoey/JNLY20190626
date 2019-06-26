package com.lib.bandaid.arcruntime.core;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;

import com.esri.arcgisruntime.geometry.EnvelopeBuilder;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.mapping.view.SketchCreationMode;
import com.esri.arcgisruntime.mapping.view.SketchEditor;
import com.esri.arcgisruntime.mapping.view.SketchGeometryChangedEvent;
import com.esri.arcgisruntime.mapping.view.SketchGeometryChangedListener;
import com.esri.arcgisruntime.mapping.view.SketchStyle;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.lib.bandaid.arcruntime.core.draw.DrawEvent;
import com.lib.bandaid.arcruntime.core.draw.DrawEventListener;
import com.lib.bandaid.arcruntime.core.draw.DrawSymbol;
import com.lib.bandaid.arcruntime.core.draw.DrawType;
import com.lib.bandaid.arcruntime.core.draw.GeometryChangedListener;
import com.lib.bandaid.arcruntime.core.draw.ValueCallback;
import com.lib.bandaid.arcruntime.event.EasyMapEvent;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

/**
 * Created by zy on 2019/6/5.
 */

public class SketchTool extends BaseContainer implements SketchGeometryChangedListener {

    private static String TAG = "DrawTool";

    private GraphicsOverlay mGraphicsLayerEditing;//绘制要素图层
    private GraphicsOverlay tempLayer;//零时图层，用于存储要素各个节点信息
    private int drawType;//当前要素绘制类型
    private boolean active;
    private EnvelopeBuilder envelopeBuilder;
    private DrawTouchListener drawListener;//绘图事件
    private Graphic drawGraphic;//当前绘制要素
    private Point startPoint;//当前绘制要素起点信息

    //=======================================================================================================
    private PointCollection pointCollection;

    public SketchEditor sketchEditor;
    private ValueCallback callBack;
    private SpatialReference defaultSpatial;

    public SketchTool() {

    }

    @Override
    public void create(ArcMap arcMap) {
        super.create(arcMap);
        this.drawListener = new DrawTouchListener();
        this.sketchEditor = mapView.getSketchEditor();
    }

    @Override
    public void ready(List<Layer> layers) {
        super.ready(layers);
        this.defaultSpatial = mapView.getSpatialReference();

        this.mGraphicsLayerEditing = new GraphicsOverlay();
        this.mapView.getGraphicsOverlays().add(this.mGraphicsLayerEditing);
        this.tempLayer = new GraphicsOverlay();
        this.mapView.getGraphicsOverlays().add(this.tempLayer);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void activate(int drawType) {
        if (this.mapView == null)
            return;

        this.deactivate();
        //申请地图事件占用
        arcMap.setEvent(drawListener);

        this.drawType = drawType;
        this.active = true;
        if (sketchEditor == null) {
            sketchEditor = new SketchEditor();

            SimpleMarkerSymbol mPointSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.SQUARE, 0xFFFF0000, 20);

            SketchStyle sketchStyle = new SketchStyle();
            sketchStyle.setFeedbackVertexSymbol(mPointSymbol);
            sketchStyle.setLineSymbol(DrawSymbol.mLineSymbol);
            sketchStyle.setFillSymbol(DrawSymbol.mFillSymbol);
            sketchEditor.setSketchStyle(sketchStyle);
            sketchEditor.addGeometryChangedListener(this);

            mapView.setSketchEditor(sketchEditor);
        }
        switch (this.drawType) {
            case DrawType.POINT:
                sketchEditor.start(SketchCreationMode.POINT);
                break;
            case DrawType.POLYGON:
                sketchEditor.start(SketchCreationMode.POLYGON);
                break;
            case DrawType.FREEHAND_POLYGON:
                sketchEditor.start(SketchCreationMode.FREEHAND_POLYGON);
                break;
            case DrawType.POLYLINE:
                sketchEditor.start(SketchCreationMode.POLYLINE);
                break;
            case DrawType.FREEHAND_POLYLINE:
                sketchEditor.start(SketchCreationMode.FREEHAND_LINE);
                break;
            case DrawType.CIRCLE:
                pointCollection = new PointCollection(mapView.getSpatialReference());
                break;
            case DrawType.ENVELOPE:
                envelopeBuilder = new EnvelopeBuilder(this.defaultSpatial);
                drawGraphic = new Graphic();
                drawGraphic.setGeometry(envelopeBuilder.toGeometry());
                drawGraphic.setSymbol(DrawSymbol.mFillSymbol);
                mGraphicsLayerEditing.getGraphics().add(drawGraphic);
                drawGraphic.getGraphicsOverlay().setOpacity(0.7f);
                break;
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    public void deactivate() {
        //取消事件
        if (sketchEditor != null) sketchEditor.stop();
        arcMap.setEvent(null);
        this.active = false;
        this.drawType = -1;
        this.drawGraphic = null;
        this.startPoint = null;

        this.mGraphicsLayerEditing.getGraphics().clear();
        this.tempLayer.getGraphics().clear();


    }


    public void sendDrawEndEvent() {
        if (drawType != DrawType.ENVELOPE) {
            drawGraphic.setGeometry(sketchEditor.getGeometry());
        }
        DrawEvent e = new DrawEvent(this, DrawEvent.DRAW_END, drawGraphic);
        this.notifyEvent(e);
        int type = this.drawType;
        this.deactivate();
        if (type == DrawType.POINT) {
            this.activate(type);
        }
    }


    /**
     * 刷新要素信息
     */
    @Override
    public void refresh() {
        if (tempLayer != null) {
            tempLayer.getGraphics().clear();
        }
    }

    boolean isGeoChanged = false;

    @Override
    public void geometryChanged(SketchGeometryChangedEvent sketchGeometryChangedEvent) {
        isGeoChanged = true;
    }


    /**
     * 扩展MapOnTouchListener，实现画图功能
     */
    class DrawTouchListener extends EasyMapEvent {

        @Override
        public boolean onTouchMoving(MotionEvent motionEvent) {
            isGeoChanged = false;
            return super.onTouchMoving(motionEvent);
        }

        @Override
        public boolean onTouchCancel(MotionEvent motionEvent) {
            if (drawType == DrawType.CIRCLE || drawType == DrawType.ENVELOPE) {
                if (drawGraphic != null && isMapTouch) {
                    if (callBack != null) callBack.onGeometry(drawGraphic.getGeometry());
                }
            } else {
                if (sketchEditor != null && isGeoChanged && sketchEditor.isSketchValid()) {
                    if (callBack != null) callBack.onGeometry(sketchEditor.getGeometry());
                }
            }
            return super.onTouchCancel(motionEvent);
        }

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            Point point = toMapPoint(event);
            if (active && (drawType == DrawType.ENVELOPE || drawType == DrawType.CIRCLE) && event.getAction() == MotionEvent.ACTION_DOWN) {
                switch (drawType) {
                    case DrawType.ENVELOPE:
                        startPoint = point;
                        envelopeBuilder.setXY(point.getX(), point.getY(), point.getX(), point.getY());
                        break;
                    case DrawType.CIRCLE:
                        startPoint = point;
                        break;
                }
            }
            return super.onTouch(view, event);
        }

        @Override
        public boolean onRotate(MotionEvent event, double rotationAngle) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent from, MotionEvent to, float velocityX, float velocityY) {
            Point point = toMapPoint(to);
            if (active && (drawType == DrawType.ENVELOPE || drawType == DrawType.CIRCLE)) {
                switch (drawType) {
                    case DrawType.ENVELOPE:
                        envelopeBuilder.setXMin(startPoint.getX() > point.getX() ? point
                                .getX() : startPoint.getX());
                        envelopeBuilder.setYMin(startPoint.getY() > point.getY() ? point
                                .getY() : startPoint.getY());
                        envelopeBuilder.setXMax(startPoint.getX() < point.getX() ? point
                                .getX() : startPoint.getX());
                        envelopeBuilder.setYMax(startPoint.getY() < point.getY() ? point
                                .getY() : startPoint.getY());

                        boolean flag = envelopeBuilder.isSketchValid();
                        if (flag) {
                            drawGraphic.setGeometry(envelopeBuilder.toGeometry());
                        }
                        break;

                    case DrawType.CIRCLE:
                        pointCollection.add(point);
                        if (pointCollection.size() > 1) {
                            double x = (point.getX() - startPoint.getX());
                            double y = (point.getY() - startPoint.getY());
                            double radius = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
                            getCircle(startPoint, radius);
                        }
                        break;
                }
                return true;
            }
            return super.onFling(from, to, velocityX, velocityY);
        }

    }

    private Point toMapPoint(MotionEvent event) {
        return mapView.screenToLocation(new android.graphics.Point((int) event.getX(), (int) event.getY()));
    }


    private void getCircle(Point point, double radius) {
        Point[] points = getPoints(point, radius);
        pointCollection.clear();
        pointCollection.addAll(Arrays.asList(points));
        Polygon polygon = new Polygon(pointCollection);

        SimpleMarkerSymbol simpleMarkerSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 10);
        Graphic pointGraphic = new Graphic(point, simpleMarkerSymbol);
        mGraphicsLayerEditing.getGraphics().add(pointGraphic);

        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.parseColor("#FC8145"), 2.0f);
        SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.TRANSPARENT, lineSymbol);
        mGraphicsLayerEditing.getGraphics().clear();
        Graphic graphic = new Graphic(polygon, simpleFillSymbol);
        mGraphicsLayerEditing.getGraphics().add(graphic);
    }

    /**
     * 通过中心点和半径计算得出圆形的边线点集合
     */
    private Point[] getPoints(Point center, double radius) {
        Point[] points = new Point[50];
        double sin;
        double cos;
        double x;
        double y;
        for (double i = 0; i < 50; i++) {
            sin = Math.sin(Math.PI * 2 * i / 50);
            cos = Math.cos(Math.PI * 2 * i / 50);
            x = center.getX() + radius * sin;
            y = center.getY() + radius * cos;
            points[(int) i] = new Point(x, y);
        }
        return points;
    }


    public ValueCallback getCallBack() {
        return callBack;
    }

    public void setCallBack(ValueCallback callBack) {
        this.callBack = callBack;
    }

    private Vector<DrawEventListener> repository = new Vector<>();

    // 添加监听
    public void addEventListener(DrawEventListener listener) {
        this.repository.addElement(listener);
    }

    // 移除监听
    public void removeEventListener(DrawEventListener listener) {
        this.repository.removeElement(listener);
    }

    // 向监听者派发消息
    public void notifyEvent(DrawEvent event) {
        Enumeration<DrawEventListener> en = this.repository.elements();
        while (en.hasMoreElements()) {
            DrawEventListener listener = en.nextElement();
            listener.handleDrawEvent(event);
        }
    }
}
