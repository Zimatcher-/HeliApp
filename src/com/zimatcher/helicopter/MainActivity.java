package com.zimatcher.helicopter;

import android.os.Bundle;
import android.app.Activity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.VerticalSeekBar;

public class MainActivity extends Activity {

	ToggleButton Heli, Lights;
	int progress;
	VerticalSeekBar seekBar;
	TextView seekBarValue ; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
     
		seekBar = (VerticalSeekBar)findViewById(R.id.throtleBar);
		seekBarValue = (TextView)findViewById(R.id.throtleText);
		
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
	
	//on screen touch function incase i need later
	
	/*public boolean onTouchEvent(MotionEvent e){
        int i = e.getAction();

        switch(i){

        case MotionEvent.ACTION_DOWN:
            ToastMaster("finderDown");

            break;

        case MotionEvent.ACTION_UP:
            //When your finger stop touching the screen

            break;

        case MotionEvent.ACTION_MOVE:
            //When your finger moves around the screen

            break;
        }

        return false;
    }
*/
	public void Heli(View view) {
	    // Is the toggle on?
	    boolean on = ((ToggleButton) view).isChecked();
	    
	    if (on) {
	        // Turn Heli On
	    	ToastMaster("Heli Turning On");
	    } else {
	        // Turn Heli Off
	    	ToastMaster("Heli Turning Off");
	    }
	}
	
	public void Lights(View view) {
	    // Is the toggle on?
	    boolean on = ((ToggleButton) view).isChecked();
	    
	    if (on) {
	    	
	        // Enable Lights
	    	ToastMaster("Lights Turning On");
	    } else {
	        // Disable Lights
	    	ToastMaster("Lights Turning Off");
	    }
	}
	
	
	public void ToastMaster(String textToDisplay) {
		  Toast myMessage = Toast.makeText(getApplicationContext(), 
		  textToDisplay, 
		  Toast.LENGTH_SHORT);
		  myMessage.setGravity(Gravity.CENTER, 0, 0);
		  myMessage.show();
		}
}
