package com.zimatcher.helicopter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
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


public class MainActivity extends Activity {

	ToggleButton connectButton, heliButton, lightsButton;
	int progress, resultBT;
	VerticalSeekBar seekBar;
	TextView seekBarValue; 
	BroadcastReceiver mReceiver;
	IntentFilter filter;
	String sendLetter;
	public static BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	static Context context ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = getApplicationContext();
     
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

	public void Heli(View view) throws IOException {
	    // Is the toggle on?
	    boolean on = ((ToggleButton) view).isChecked();
	    boolean connected = connectButton.isChecked();
	   
	    if (on) {
	    	if (connected){
	    		// Turn Heli On
		    	sendLetter = "4";
		    	sendData();
	    	} else {
	    		ToastText("Not Connected");
	    		heliButton.setChecked(false);
	    	}
	        
	    } else {
	        // Turn Heli Off
	    	sendLetter = "4";
	    	sendData();
	    }
	}
	
	public void Lights(View view) throws IOException {
	    // Is the toggle on?
	    boolean on = ((ToggleButton) view).isChecked();
	    boolean connected = connectButton.isChecked();
		   
	    if (on) {
	    	if (connected){
	    		// Turn Lights On
		    	sendLetter = "1";
		    	sendData();
	    	} else {
	    		ToastText("Not Connected");
	    		lightsButton.setChecked(false);
	    	}
	        
	    } else {
	        // Turn Lights Off
	    	sendLetter = "2";
	    	sendData();
	    }
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
	
	public void Connect() {
		
		ToastText("Finding Heli");
		
		mBluetoothAdapter.startDiscovery();
		mReceiver = new BroadcastReceiver() {
			
    	    public void onReceive(Context context, Intent intent) {
    	        String action = intent.getAction();
    	        // When discovery finds a device
    	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
    	            // Get the BluetoothDevice object from the Intent
    	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
    	            // Add the name and address to an array adapter to show in a ListView
    	            if (device.getName().equals("HC-06")){
    	                ToastText("Connecting to Heli");
    	                unregisterReceiver(mReceiver);
    	                Thread connectBT = new ConnectThread(device);
    	                //Connect to the the device in a new thread
    	                new Thread(connectBT).start();
    	            }else {
    	            }
    	        }
    	    }
    	};
    	
    	filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
    	registerReceiver(mReceiver, filter);
    
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
	
	public static void ToastText(String textToDisplay) {
		  Toast myMessage = Toast.makeText(context, textToDisplay, Toast.LENGTH_SHORT);
		  myMessage.setGravity(Gravity.CENTER, 0, 0);
		  myMessage.show();
		}
	
	void sendData() throws IOException {
		  ConnectedThread.mmOutStream.write(sendLetter.getBytes());
		}
}

class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
 
    public ConnectThread(BluetoothDevice device) {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        BluetoothSocket tmp = null;
        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = device.createRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) { }
        mmSocket = tmp;
    }
 
    public void run() {
        // Cancel discovery because it will slow down the connection
        MainActivity.mBluetoothAdapter.cancelDiscovery();
 
        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
            try {
                mmSocket.close();
            } catch (IOException closeException) { }
            return;
        }
 
        // Do work to manage the connection (in a separate thread)
        Thread connectedBT = new ConnectedThread(mmSocket);
        //Connect to the the device in a new thread
        new Thread(connectedBT).start();
    }
 
    /** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }

}

class ConnectedThread extends Thread {
    
	static int MESSAGE_WRITE ;
	static int MESSAGE_READ ;
	private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    public static OutputStream mmOutStream;
 
    public ConnectedThread(BluetoothSocket socket) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
 
        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) { }
 
        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }
 
    public void run() {
        byte[] buffer = new byte[1024];  // buffer store for the stream
        int bytes; // bytes returned from read()
 
        // Keep listening to the InputStream until an exception occurs
        while (true) {
            try {
                // Read from the InputStream
                bytes = mmInStream.read(buffer);
                // Send the obtained bytes to the UI activity
                //mHandler.handleMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget();
            } catch (IOException e) {
                break;
            }
        }
    }
 
    /* Call this from the main activity to send data to the remote device */
    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) { }
    }
 
    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
}