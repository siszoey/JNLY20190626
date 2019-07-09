package com.lib.bandaid.arcruntime.core;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.layers.ArcGISMapImageLayer;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.loadable.LoadStatusChangedEvent;
import com.esri.arcgisruntime.loadable.LoadStatusChangedListener;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.LayerList;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.mapping.view.SketchEditor;
import com.lib.bandaid.R;
import com.lib.bandaid.arcruntime.event.ArcMapEventDispatch;
import com.lib.bandaid.arcruntime.event.IArcMapEvent;
import com.lib.bandaid.arcruntime.layer.utils.DataSourceRead;
import com.lib.bandaid.rw.file.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zy on 2019/5/9.
 */

public class ArcMap extends RelativeLayout implements LoadStatusChangedListener, IContainer {

    private boolean canRotate = false;
    private boolean canOffline = false;
    private Context context;
    /**
     * 卫星影像地图
     */
    private List<String> baseMapUrls = new ArrayList<>();
    private List<String> featureUrls = new ArrayList<>();
    private List<String> mapServerUrls = new ArrayList<>();
    private List<String> mapServerDesc = new ArrayList<>();
    private List<String> mapLocalUrls = new ArrayList<>();


    private TocContainer tocContainer = new TocContainer();
    private QueryContainer queryContainer = new QueryContainer();
    private SelectContainer selectContainer = new SelectContainer();
    private MapControl mapControl = new MapControl();
    private WidgetContainer widgetContainer = new WidgetContainer();

    private SketchTool sketchTool = new SketchTool();
    private SketchContainer sketchContainer = new SketchContainer();


    private GraphicContainer graphicContainer = new GraphicContainer();
    private ToolContainer toolContainer = new ToolContainer();

    private MapView mapView;

    private IMapReady iMapReady;

    //事件
    private ArcMapEventDispatch arcMapEventDispatch;

    public ArcMap(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ArcMap(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArcMap);
        canRotate = typedArray.getBoolean(R.styleable.ArcMap_canRotate, false);
        canOffline = typedArray.getBoolean(R.styleable.ArcMap_canOffline, false);
        typedArray.recycle();

        this.context = context;
        init();
    }

    public ArcMap(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    void init() {
        initMap();
        create(this);
    }

    void initMap() {
        if (!canOffline) {
            ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud8065403504,none,RP5X0H4AH7CLJ9HSX018");
        }
        this.removeAllViews();
        this.mapView = new MapView(context);
        this.mapView.setAttributionTextVisible(false);
        this.addView(mapView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.arcMapEventDispatch = new ArcMapEventDispatch(this);
        //注册地图事件
        this.mapView.setOnTouchListener(arcMapEventDispatch);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        IArcMapEvent iMapEvent = arcMapEventDispatch.getCurIMapEvent();
        boolean continueDispatch;
        if (iMapEvent == null) return super.dispatchTouchEvent(ev);
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            continueDispatch = iMapEvent.onTouchStart(ev);
            if (continueDispatch) return true;
        }
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            continueDispatch = iMapEvent.onTouchMoving(ev);
            if (continueDispatch) return true;
        }
        if (ev.getAction() == MotionEvent.ACTION_UP ||
                ev.getAction() == MotionEvent.ACTION_CANCEL) {
            continueDispatch = iMapEvent.onTouchCancel(ev);
            if (continueDispatch) return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    void setEvent(IArcMapEvent event) {
        arcMapEventDispatch.setCurIMapEvent(event);
        this.mapView.setOnTouchListener(arcMapEventDispatch);
    }

    public void mapLoad(IMapReady iMapReady) {
        this.iMapReady = iMapReady;

        //加载底图
        {
            ArcGISMap gisMap = new ArcGISMap();
            if (baseMapUrls == null || baseMapUrls.size() == 0) {
                gisMap.setBasemap(Basemap.createImagery());
            } else {
                ArcGISTiledLayer tiledLayer = new ArcGISTiledLayer(baseMapUrls.get(0));
                gisMap.setBasemap(new Basemap(tiledLayer));
            }
            gisMap.addLoadStatusChangedListener(this);
            mapView.setMap(gisMap);
        }

        //加载网络数据
        {
            if (mapServerUrls == null) return;
            ArcGISMapImageLayer mapImageLayer;
            String uri;
            for (int i = 0; i < mapServerUrls.size(); i++) {
                uri = mapServerUrls.get(i);
                mapImageLayer = new ArcGISMapImageLayer(uri);
                mapImageLayer.setDescription(uri);
                if (mapServerDesc != null && mapServerDesc.size() == mapServerUrls.size()) {
                    mapImageLayer.setName(mapServerDesc.get(i));
                }
                mapView.getMap().getOperationalLayers().add(mapImageLayer);
            }
        }

        //加载featureLayer
        {
            if (featureUrls == null) return;
            ServiceFeatureTable featureTable;
            FeatureLayer featureLayer;
            for (int i = 0; i < featureUrls.size(); i++) {
                featureTable = new ServiceFeatureTable(featureUrls.get(i));
                featureLayer = new FeatureLayer(featureTable);
                featureLayer.setId(featureUrls.get(i));
                mapView.getMap().getOperationalLayers().add(featureLayer);
            }
        }

        //加载本地数据
        {
            if (mapLocalUrls == null) return;
            Map<String, List<File>> localTree;
            LayerList list = mapView.getMap().getOperationalLayers();
            for (int i = 0; i < mapLocalUrls.size(); i++) {
                localTree = FileUtil.fileTrees(mapLocalUrls.get(i), "geodatabase");
                DataSourceRead.read(list, localTree);
            }
        }
        //加载本地数据
       /* {
            if (mapLocalUrls == null) return;
            String url;
            LayerList list = mapView.getMap().getOperationalLayers();
            for (int i = 0; i < mapLocalUrls.size(); i++) {
                url = mapLocalUrls.get(i);
                if (url == null) continue;
                if (url.toLowerCase().endsWith(".shape")) {
                    DataSourceRead.readShape(list, url);
                } else if (url.toLowerCase().endsWith(".geodatabase")) {
                    DataSourceRead.readGeoDatabase(list, url);
                } else {
                    //DataSourceRead.readGeoDatabase();
                }
            }
        }*/


    }

    @Override
    public void loadStatusChanged(LoadStatusChangedEvent loadStatusChangedEvent) {
        if (LoadStatus.LOADED == loadStatusChangedEvent.getNewLoadStatus()) {
            ready(mapView.getMap().getOperationalLayers());
            if (iMapReady != null) iMapReady.onMapReady();
        }
    }

    @Override
    public void create(ArcMap arcMap) {
        tocContainer.create(arcMap);
        queryContainer.create(arcMap);
        selectContainer.create(arcMap);
        mapControl.create(arcMap);
        widgetContainer.create(arcMap);
        sketchTool.create(arcMap);
        sketchContainer.create(arcMap);
        graphicContainer.create(arcMap);
        toolContainer.create(arcMap);
    }

    @Override
    public void ready(List<Layer> layers) {
        tocContainer.ready(layers);
        queryContainer.ready(layers);
        selectContainer.ready(layers);
        mapControl.ready(layers);
        widgetContainer.ready(layers);
        sketchTool.ready(layers);
        sketchContainer.ready(layers);
        graphicContainer.ready(layers);
        toolContainer.ready(layers);
    }

    @Override
    public void destroy() {
        if (tocContainer != null) tocContainer.destroy();
        if (queryContainer != null) queryContainer.destroy();
        if (selectContainer != null) selectContainer.destroy();
        if (mapControl != null) mapControl.destroy();
        if (widgetContainer != null) widgetContainer.destroy();
        if (sketchTool != null) sketchTool.destroy();
        if (sketchContainer != null) sketchContainer.destroy();
        if (graphicContainer != null) graphicContainer.destroy();
        if (toolContainer != null) toolContainer.destroy();
    }

    public void resume() {
        mapView.resume();
    }

    public void pause() {
        mapView.pause();
    }


    @Override
    public void destroyDrawingCache() {
        super.destroyDrawingCache();
        destroy();
    }

    public interface IMapReady {
        public void onMapReady();
    }

    public TocContainer getTocContainer() {
        return tocContainer;
    }

    public GraphicContainer getGraphicContainer() {
        return graphicContainer;
    }

    public QueryContainer getQueryContainer() {
        return queryContainer;
    }

    public SelectContainer getSelectContainer() {
        return selectContainer;
    }

    public MapControl getMapControl() {
        return mapControl;
    }

    public WidgetContainer getWidgetContainer() {
        return widgetContainer;
    }

    public SketchContainer getSketchContainer() {
        return sketchContainer;
    }

    public SketchTool getSketchTool() {
        return sketchTool;
    }

    public ToolContainer getToolContainer() {
        return toolContainer;
    }

    public ArcMap setBaseMapUrl(String... urls) {
        if (urls == null) return this;
        for (int i = 0; i < urls.length; i++) {
            baseMapUrls.add(urls[i]);
        }
        return this;
    }

    public ArcMap setFeatureLayerUrl(String... urls) {
        if (urls == null) return this;
        for (int i = 0; i < urls.length; i++) {
            featureUrls.add(urls[i]);
        }
        return this;
    }

    public ArcMap setMapServerUrl(String... urls) {
        if (urls == null) return this;
        for (int i = 0; i < urls.length; i++) {
            mapServerUrls.add(urls[i]);
        }
        return this;
    }

    public ArcMap setMapServerDesc(String... name) {
        if (name == null) return this;
        for (int i = 0; i < name.length; i++) {
            mapServerDesc.add(name[i]);
        }
        return this;
    }

    public ArcMap setMapLocalUrl(String... urls) {
        if (urls == null) return this;
        for (int i = 0; i < urls.length; i++) {
            mapLocalUrls.add(urls[i]);
        }
        return this;
    }

    public ArcMap setMapLocalUrl(List<String> urls) {
        if (urls == null) return this;
        for (int i = 0; i < urls.size(); i++) {
            mapLocalUrls.add(urls.get(i));
        }
        return this;
    }

    public MapView getMapView() {
        return mapView;
    }

    public Point screenToLocation(MotionEvent e) {
        return mapView.screenToLocation(new android.graphics.Point((int) e.getX(), (int) e.getY()));
    }

    /**
     * **************************************地图属性设置*******************************************
     */
    public boolean isCanRotate() {
        return canRotate;
    }

    public void setCanRotate(boolean canRotate) {
        this.canRotate = canRotate;
    }

    public boolean isCanOffline() {
        return canOffline;
    }

    public void setCanOffline(boolean canOffline) {
        this.canOffline = canOffline;
    }
}
