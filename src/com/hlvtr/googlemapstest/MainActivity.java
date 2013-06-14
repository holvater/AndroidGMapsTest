package com.hlvtr.googlemapstest;



import java.util.ArrayList;

import org.w3c.dom.Document;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;



/**
 *
 * @author VIJAYAKUMAR M
 *This class for display route current location to hotel location on google map V2
 */
public class MainActivity extends FragmentActivity  {

      LocationManager locManager;
      Drawable drawable;
      Document document;
      GMapV2GetRouteDirection v2GetRouteDirection;
      LatLng fromPosition;
      LatLng toPosition;
      GoogleMap mGoogleMap;
      MarkerOptions markerOptions;
      Location location ;
      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            v2GetRouteDirection = new GMapV2GetRouteDirection();
            SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
            		.findFragmentById(R.id.map);
            mGoogleMap = supportMapFragment.getMap();

            // Enabling MyLocation in Google Map
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
            mGoogleMap.getUiSettings().setCompassEnabled(true);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
            mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
            mGoogleMap.setTrafficEnabled(true);
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
            OnMapLongClickListener omlcl = new OnMapLongClickListener() {
    			
    			@Override
    			public void onMapLongClick(LatLng arg0) {
    	            double lat = mGoogleMap.getMyLocation().getLatitude();
    	            double lng = mGoogleMap.getMyLocation().getLongitude();
    	            fromPosition = new LatLng(lat, lng); 
    	            toPosition = arg0;
    	            GetRouteTask getRoute = new GetRouteTask();
    	            getRoute.execute();
    			}
    		};
    		mGoogleMap.setOnMapLongClickListener(omlcl);
            markerOptions = new MarkerOptions();
            
//            double lat = mGoogleMap.getMyLocation().getLatitude();
//            double lng = mGoogleMap.getMyLocation().getLongitude();
//            fromPosition = new LatLng(lat, lng); //
            fromPosition = new LatLng(19.326229,-99.182448);
            toPosition = new LatLng(19.389635,-99.166485);
            GetRouteTask getRoute = new GetRouteTask();
            getRoute.execute();
      }
      /**
       *
       * @author VIJAYAKUMAR M
       * This class Get Route on the map
       *
       */
      private class GetRouteTask extends AsyncTask<String, Void, String> {
           
            private ProgressDialog Dialog;
            String response = "";
            @Override
            protected void onPreExecute() {
                  Dialog = new ProgressDialog(MainActivity.this);
                  Dialog.setMessage("Loading route...");
                  Dialog.show();
            }

            @Override
            protected String doInBackground(String... urls) {
                  //Get All Route values
                        document = v2GetRouteDirection.getDocument(fromPosition, toPosition, GMapV2GetRouteDirection.MODE_WALKING);
                        response = "Success";
                  return response;

            }

            @Override
            protected void onPostExecute(String result) {
                  mGoogleMap.clear();
                  if(response.equalsIgnoreCase("Success")){
	                  ArrayList<LatLng> directionPoint = v2GetRouteDirection.getDirection(document);
	                  PolylineOptions rectLine = new PolylineOptions().width(10).color(
	                              Color.RED);
	
	                  for (int i = 0; i < directionPoint.size(); i++) {
	                        rectLine.add(directionPoint.get(i));
	                  }
	                  // Adding route on the map
	                  mGoogleMap.addPolyline(rectLine);
	                  markerOptions.position(toPosition);
	                  markerOptions.draggable(true);
	                  mGoogleMap.addMarker(markerOptions);

                  }
                  mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fromPosition,12));
                  Dialog.dismiss();
            }
      }
      @Override
      protected void onStop() {
            super.onStop();
            finish();
      }
}
