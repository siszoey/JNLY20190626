package com.lib.bandaid.arcruntime.core;

import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.SketchCreationMode;
import com.esri.arcgisruntime.mapping.view.SketchEditor;
import com.esri.arcgisruntime.mapping.view.SketchStyle;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;

import java.util.List;

/**
 * Created by zy on 2019/5/10.
 */

public class SketchContainer extends BaseContainer {

    SketchEditor sketchEditor;

    @Override
    public void create(ArcMap arcMap) {
        super.create(arcMap);
        init();
    }

    @Override
    public void ready(List<Layer> layers) {
        super.ready(layers);
        //activate(DrawType.POLYGON);
    }

    private void init() {
        if (sketchEditor == null) {
            sketchEditor = new SketchEditor();
            SimpleMarkerSymbol pointSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.SQUARE, 0xFFFF0000, 20);
            SimpleLineSymbol lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.DASH, 0xFFFF0000, 2);
            SimpleFillSymbol fillSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.BACKWARD_DIAGONAL, 0xFFFF0000, lineSymbol);
            SketchStyle sketchStyle = new SketchStyle();
            sketchStyle.setFeedbackVertexSymbol(pointSymbol);
            sketchStyle.setLineSymbol(lineSymbol);
            sketchStyle.setFillSymbol(fillSymbol);
            sketchEditor.setSketchStyle(sketchStyle);
            mapView.setSketchEditor(sketchEditor);
        }
    }

    /**
     * 添加地图中点
     */
    public void addDefaultPoint() {
        int left = mapView.getLeft();
        int top = mapView.getTop();
        int width = mapView.getWidth();
        int high = mapView.getHeight();

        android.graphics.Point screenP = new android.graphics.Point(width / 2 + left, high / 2 + top);
        Point point = mapView.screenToLocation(screenP);
        addPoint(point);
    }

    /**
     * 添加指定点
     *
     * @param point
     */
    public void addPoint(Point point) {
        sketchEditor.insertVertexAfterSelectedVertex(point);
    }

    public void tryRedo() {
        if (sketchEditor.canRedo()) sketchEditor.redo();
    }

    public void tryUndo() {
        if (sketchEditor.canUndo()) sketchEditor.undo();
    }

    public void clear() {
        sketchEditor.clearGeometry();
    }


    public void activate(int drawType) {
        this.deactivate();

       /* if (drawType == DrawType.POINT) {
            sketchEditor.start(SketchCreationMode.POINT);
        } else if (drawType == DrawType.POLYGON) {
            sketchEditor.start(SketchCreationMode.POLYGON);
        } else if (drawType == DrawType.FREEHAND_POLYGON) {
            sketchEditor.start(SketchCreationMode.FREEHAND_POLYGON);
        } else if (drawType == DrawType.ENVELOPE) {

        } else if (drawType == DrawType.POLYLINE) {
            sketchEditor.start(SketchCreationMode.POLYLINE);
        } else if (drawType == DrawType.FREEHAND_POLYLINE) {
            sketchEditor.start(SketchCreationMode.FREEHAND_LINE);
        }*/
    }

    public void deactivate() {
        mapView.setOnTouchListener(new DefaultMapViewOnTouchListener(context, mapView));
    }

    public Geometry getSketchGeo() {
        if (sketchEditor == null || !sketchEditor.isSketchValid()) return null;
        return sketchEditor.getGeometry();
    }
}
