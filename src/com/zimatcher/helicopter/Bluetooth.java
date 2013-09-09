package com.zimatcher.helicopter;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.view.Gravity;
import android.widget.Toast;

public class Bluetooth extends Activity {

	public void getBtAdapter() {
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
	    	ToastText("Doesn't Support Bluetooth");
		}else {
			ToastText("" + mBluetoothAdapter);
		}
	}
	
	public void ToastText(String textToDisplay) {
		  Toast myMessage = Toast.makeText(getApplicationContext(), textToDisplay, Toast.LENGTH_SHORT);
		  myMessage.setGravity(Gravity.CENTER, 0, 0);
		  myMessage.show();
		}
}
