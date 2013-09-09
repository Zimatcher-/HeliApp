package com.zimatcher.helicopter;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Gravity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.VerticalSeekBar;
import com.zimatcher.helicopter.Bluetooth;

public class MainActivity extends Activity {

	ToggleButton connectButton, heliButton, lightsButton;
	int progress, resultBT;
	VerticalSeekBar seekBar;
	TextView seekBarValue; 
	BroadcastReceiver mReceiver;
	IntentFilter filter;
	BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
     
		seekBar = (VerticalSeekBar)findViewById(R.id.throtleBar);
		seekBarValue = (TextView)findViewById(R.id.throtleText);
		connectButton = ((ToggleButton) findViewById(R.id.connectButton));
		heliButton = ((ToggleButton) findViewById(R.id.heliButton));
		lightsButton = ((ToggleButton) findViewById(R.id.lightsButton));
		
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){ 

			   @Override 
			   public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { 
				   boolean on = ((ToggleButton) findViewById(R.id.heliButton)).isChecked();
				   if (on){
					   seekBarValue.setText("Throtle = " + String.valueOf(progress)); }
				   else{
					   seekBarValue.setText("Heli Not On");
				   }
			   } 
			   
			   @Override 
			   public void onStartTrackingTouch(SeekBar seekBar) { 
			    // TODO Auto-generated method stub 
			   } 

			   @Override 
			   public void onStopTrackingTouch(SeekBar seekBar) { 
			    // TODO Auto-generated method stub 
			   } 
			       }); 

	}
	
    public void Connect(View view) {
	    // Is the toggle on?
	    boolean on = ((ToggleButton) view).isChecked();
	    
	    if (on) {
	        // Connect To Heli
	    	ToastText("Trying to Connect");
	    	CheckBT();
	    } else {
	        // Disconnect From Heli
	    	heliButton.setChecked(false);
	    	lightsButton.setChecked(false);
	    	ToastText("Disconnecting");
	    	ToastText("Bluetooth Turning off");
	    	mBluetoothAdapter.disable();
	    }
	}

	public void Heli(View view) {
	    // Is the toggle on?
	    boolean on = ((ToggleButton) view).isChecked();
	    boolean connected = connectButton.isChecked();
	   
	    if (on) {
	    	if (connected){
	    		// Turn Heli On
		    	ToastText("Heli Turning On");
	    	} else {
	    		ToastText("Not Connected");
	    		heliButton.setChecked(false);
	    	}
	        
	    } else {
	        // Turn Heli Off
	    	ToastText("Heli Turning Off");
	    }
	}
	
	public void Lights(View view) {
	    // Is the toggle on?
	    boolean on = ((ToggleButton) view).isChecked();
	    boolean connected = connectButton.isChecked();
		   
	    if (on) {
	    	if (connected){
	    		// Turn Lights On
		    	ToastText("Lights Turning On");
	    	} else {
	    		ToastText("Not Connected");
	    		lightsButton.setChecked(false);
	    	}
	        
	    } else {
	        // Turn Lights Off
	    	ToastText("Lights Turning Off");
	    }
	}

	public void Connect() {
		
		ToastText("Connecting");
		
		mReceiver = new BroadcastReceiver() {
    	    public void onReceive(Context context, Intent intent) {
    	        String action = intent.getAction();
    	        // When discovery finds a device
    	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
    	            // Get the BluetoothDevice object from the Intent
    	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
    	            // Add the name and address to an array adapter to show in a ListView
    	            ToastText(device.getName());
    	            if (device.getName() == "HC-06"){
    	            	ToastText("Found Device");
    	            }else {
    	            	ToastText("not my device");
    	            }
    	        }
    	    }
    	};
    	
    	filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
    	registerReceiver(mReceiver, filter);
    
	}
	public void CheckBT() {
		//BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
	    	ToastText("Doesn't Support Bluetooth");
		}else {
			if (!mBluetoothAdapter.isEnabled()) {	
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, 0);
			} else {
				Connect();
			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  if (requestCode==0) {
	    if (resultCode == RESULT_OK) {
	      ToastText("Bluetooth turned ON");
	      Connect();
	    } 
	    else {
	      ToastText("You need Bluetooth ON");
	      connectButton.setChecked(false);
	    }
	  }
	}
	
	public void ToastText(String textToDisplay) {
		
		  Toast myMessage = Toast.makeText(getApplicationContext(), textToDisplay, Toast.LENGTH_SHORT);
		  myMessage.setGravity(Gravity.CENTER, 0, 0);
		  myMessage.show();
		}

	@Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
    }
}
