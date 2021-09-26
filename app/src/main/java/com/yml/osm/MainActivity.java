package com.yml.osm;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.MapBoxTileSource;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.MapTileIndex;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.TilesOverlay;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private MapView  osmAndroidMap;
    private TextView loadMapbox;
    private TextView loadTianDi;
    private TextView loadAMap;
    private TextView loadGoogle;
    private TextView loadArcGis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        osmAndroidMap = findViewById(R.id.osm_android_map);

        loadMapbox = findViewById(R.id.tv_load_mapbox);
        loadMapbox.setOnClickListener(this);

        loadTianDi = findViewById(R.id.tv_load_td);
        loadTianDi.setOnClickListener(this);

        loadAMap = findViewById(R.id.tv_load_amap);
        loadAMap.setOnClickListener(this);

        loadGoogle = findViewById(R.id.tv_load_google);
        loadGoogle.setOnClickListener(this);

        loadArcGis = findViewById(R.id.tv_load_arcgis);
        loadArcGis.setOnClickListener(this);

//        loadMapboxTile();

        osmAndroidMap.getController().setZoom(16d);
        osmAndroidMap.getController().setCenter(new GeoPoint(39.977397, 116.371041));
    }

    /**
     * 加载Mapbox的Tileset
     */
    private void loadMapboxTile() {
        MapBoxTileSource mapBoxTileSource = new MapBoxTileSource();
        mapBoxTileSource.retrieveAccessToken(this);
        mapBoxTileSource.retrieveMapBoxMapId(this);
        Log.e(TAG, "MAPBOX_MAPID = " + mapBoxTileSource.getMapBoxMapId());
        Log.e(TAG, "MAPBOX_ACCESS_TOKEN = " + mapBoxTileSource.getAccessToken());
        osmAndroidMap.setTileSource(mapBoxTileSource);
    }

    /**
     * 加载天地图的Tileset
     */
    private void loadTianDiTile() {
        String[] tianDiBaseUrl = new String[]{
                "https://t0.tianditu.gov.cn/DataServer?T=vec_w"
//                "https://t0.tianditu.gov.cn/DataServer?T=cva_w"
        };
        OnlineTileSourceBase tianDiMapSource = new OnlineTileSourceBase("tiandi-vec-cva",
                0, 22,
                256, ".png", tianDiBaseUrl) {
            @Override
            public String getTileURLString(long pMapTileIndex) {
                StringBuilder url = new StringBuilder(getBaseUrl());
                url.append("&L=");
                url.append(MapTileIndex.getZoom(pMapTileIndex));
                url.append("&X=");
                url.append(MapTileIndex.getX(pMapTileIndex));
                url.append("&Y=");
                url.append(MapTileIndex.getY(pMapTileIndex));
                url.append("&tk=");
                url.append(Constants.TIANDI_MAP_KEY);
                Log.e(TAG, "天地图具体瓦片地址：" + url.toString());
                return url.toString();
            }
        };
        osmAndroidMap.setTileSource(tianDiMapSource);

        String[] tianDiZhujiUrl = new String[]{
                "https://t0.tianditu.gov.cn/DataServer?T=cva_w"
        };
        OnlineTileSourceBase tianDiZhujiSource = new OnlineTileSourceBase("tiandi-zhuji",
                0, 22,
                256, ".png", tianDiZhujiUrl) {
            @Override
            public String getTileURLString(long pMapTileIndex) {
                StringBuilder url = new StringBuilder(getBaseUrl());
                url.append("&L=");
                url.append(MapTileIndex.getZoom(pMapTileIndex));
                url.append("&X=");
                url.append(MapTileIndex.getX(pMapTileIndex));
                url.append("&Y=");
                url.append(MapTileIndex.getY(pMapTileIndex));
                url.append("&tk=");
                url.append(Constants.TIANDI_MAP_KEY);
                Log.e(TAG, "天地图具体瓦片地址：" + url.toString());
                return url.toString();
            }
        };
        MapTileProviderBasic basic = new MapTileProviderBasic(this, tianDiZhujiSource);
        TilesOverlay tilesOverlay = new TilesOverlay(basic, this);
        osmAndroidMap.getOverlayManager().add(tilesOverlay);
    }


    /**
     * 加载高德地图的Tileset
     */
    private void loadAMapTile() {
        OnlineTileSourceBase autoNaviVector = new XYTileSource("AutoNavi-Vector",
                0, 20, 256, ".png", new String[]{
                "https://wprd01.is.autonavi.com/appmaptile?",
                "https://wprd02.is.autonavi.com/appmaptile?",
                "https://wprd03.is.autonavi.com/appmaptile?",
                "https://wprd04.is.autonavi.com/appmaptile?",

        }) {
            @Override
            public String getTileURLString(long pMapTileIndex) {
                StringBuilder url = new StringBuilder(getBaseUrl());
                url.append("x=");
                url.append(MapTileIndex.getX(pMapTileIndex));
                url.append("&y=");
                url.append(MapTileIndex.getY(pMapTileIndex));
                url.append("&z=");
                url.append(MapTileIndex.getZoom(pMapTileIndex));
                url.append("&lang=zh_cn&size=1&scl=1&style=7&ltype=7");
                Log.e(TAG, "高德地图具体瓦片地址：" + url.toString());
                return url.toString();
            }
        };
        osmAndroidMap.setTileSource(autoNaviVector);
    }


    /**
     * 加载谷歌地图的Tileset
     * lyrs=y 谷歌卫星混合
     * lyrs=s 谷歌卫星
     * lyrs=m 谷歌地图
     * lyrs=t 谷歌地形
     * lyrs=p 谷歌地形带标注
     */
    private void loadGoogleMapTile() {
        OnlineTileSourceBase googleHybrid = new XYTileSource("Google-Hybrid",
                0, 19, 512, ".png", new String[]{
                "https://mt0.google.cn",
                "https://mt1.google.cn",
                "https://mt2.google.cn",
                "https://mt3.google.cn",

        }) {
            @Override
            public String getTileURLString(long pMapTileIndex) {
                String loadTileUrl = getBaseUrl()
                        + "/vt/lyrs=p&scale=2&hl=zh-CN&gl=CN&src=app&x="
                        + MapTileIndex.getX(pMapTileIndex) + "&y="
                        + MapTileIndex.getY(pMapTileIndex) + "&z="
                        + MapTileIndex.getZoom(pMapTileIndex);
                Log.d(TAG, "谷歌地图具体瓦片地址：" + loadTileUrl);
                return loadTileUrl;
            }
        };
        osmAndroidMap.setTileSource(googleHybrid);
    }


    /**
     * 加载ArcGis地图的Tileset
     */
    private void loadArcgisTile() {

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_load_mapbox) {
            loadMapboxTile();
        } else if (v.getId() == R.id.tv_load_td) {
            loadTianDiTile();
        } else if (v.getId() == R.id.tv_load_amap) {
            loadAMapTile();
        } else if (v.getId() == R.id.tv_load_google) {
            loadGoogleMapTile();
        } else if (v.getId() == R.id.tv_load_arcgis) {
            loadArcgisTile();
        }
    }

    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        osmAndroidMap.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        osmAndroidMap.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

}