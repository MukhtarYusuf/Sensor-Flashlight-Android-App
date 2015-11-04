package com.example.proximityflashlight;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity implements SensorEventListener {

	//Declare Variables
	private SensorManager sensorManager;
	private Sensor proximity;
	boolean lightIsOn = false;
	Camera camera;
	Parameters params;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Get Instance of Sensor Manager and Sensor
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
	    
	    //Check for Camera
  		PackageManager pm = this.getPackageManager();
	  		
  		if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
  			Log.e("err", "Oops! This Device Doesn't Have a Camera");
  			return;
  		}
	  		
  		camera = Camera.open();
  		params = camera.getParameters();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onResume() {
		// Register a Listener for the Sensor Using Sensor Manager
		super.onResume();
		sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		//Unregister the Listener Whenever the Activity Pauses
		super.onPause();
	    sensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		float distance = event.values[0];
		
		if(lightIsOn && distance < 3){
			params.setFlashMode(Parameters.FLASH_MODE_TORCH);
			camera.setParameters(params);
			camera.stopPreview();
			lightIsOn = false;
		}
		else{
			if(!lightIsOn && distance < 3){
				params.setFlashMode(Parameters.FLASH_MODE_TORCH);
				camera.setParameters(params);
				camera.startPreview();
				lightIsOn = true;
			}
		}
	}
	
	//Method to Start Sensor
	public void start(View view){
		//Register Sensor
		sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	//Method to Stop Sensor
	public void stop(View view){
		//Unregister Sensor
		sensorManager.unregisterListener(this);
	}
}
