package de.indoorpos;


import java.text.DecimalFormat;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * StepTracker
 * 
 * Oberfläche des Koppelnavigationsservice.
 * Steuert den Service und zeigt die Werte an.
 *
 */


public class StepTracker extends Activity {

	// UI Components
	private TextView 	cmpSteps;
	private TextView	cmpOrientation;
	private TextView 	cmpStepLength;
	private TextView	cmpLowerThreshold;
	private TextView	cmpUpperThreshold;
	private TextView 	cmpStepLog;
	private TextView 	cmpDistLog;
	private TextView 	cmpAccerlation;
	private TextView	cmpDist;

	private Button 		cmpStartService;
	private Button		cmpExit;
	
	private SeekBar		cmpStepLengthSlider;
	private SeekBar		cmpUpperThresholdSlider;
	private SeekBar		cmpLowerThresholdSlider;
	
	private boolean recording = false;
	
	
	private IStepTrackerService mServiceStub;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        cmpStartService = (Button) findViewById(R.id.start_service_button);
        cmpStartService.setOnClickListener(onStartServiceClick);  
        
        
        cmpExit = (Button) findViewById(R.id.exit_button);
        cmpExit.setOnClickListener(onExitClick);
        
        cmpSteps = (TextView) findViewById(R.id.steps);
                        
        cmpOrientation 	 		= (TextView) findViewById(R.id.orientation); 
        cmpStepLength			= (TextView) findViewById(R.id.step_length_text);
        cmpLowerThreshold		= (TextView) findViewById(R.id.lower_threshold_text);
        cmpUpperThreshold		= (TextView) findViewById(R.id.upper_threshold_text);
        cmpAccerlation			= (TextView) findViewById(R.id.accerlation_text);
        cmpStepLog				= (TextView) findViewById(R.id.step_log_text);
        cmpDistLog				= (TextView) findViewById(R.id.dist_log_text);
        cmpDist					= (TextView) findViewById(R.id.dist_text);
        
        int initStepLength = 100;
        cmpStepLengthSlider	= (SeekBar) findViewById(R.id.step_length_slider);
        cmpStepLengthSlider.setOnSeekBarChangeListener(onSeekBarChange);
        cmpStepLengthSlider.setProgress(initStepLength);
        cmpStepLength.setText("Schrittlänge = " + Integer.toString(initStepLength)+" cm");
        
        int initUpperThreshold = 199;
        cmpUpperThresholdSlider	= (SeekBar) findViewById(R.id.upper_threshold_slider);
        cmpUpperThresholdSlider.setOnSeekBarChangeListener(onUpperThresholdChange);
        cmpUpperThresholdSlider.setProgress(initUpperThreshold);
        cmpUpperThreshold.setText("Oberer Grenzwert = " + initUpperThreshold/10.0);
        
        int initLowerThreshold = 1;
        cmpLowerThresholdSlider	= (SeekBar) findViewById(R.id.lower_threshold_slider);
        cmpLowerThresholdSlider.setOnSeekBarChangeListener(onLowerThresholdChange);
        cmpLowerThresholdSlider.setProgress(initLowerThreshold);
        cmpLowerThreshold.setText("Unterer Grenzwert = " + initLowerThreshold);
        
   }
    
    /**
     * mListener
     * 
     * RPC Stub der Activity. Empfängt live Updates vom Service.
     */
    private final IStepTrackerListener.Stub mListener = new IStepTrackerListener.Stub() {
		
		@Override
		public void onStep(int steps,float dist) throws RemoteException {
			cmpSteps.setText(Integer.toString(steps));
			DecimalFormat df = new DecimalFormat( "0.00" );
			String s = df.format( dist );
			cmpDist.setText(s);
		}

		@Override
		public void onOrientation(float orientation, float adaptedOrientation) {
			cmpOrientation.setText(Float.toString(orientation));
		}

		@Override
		public void onAccerlation(float value) throws RemoteException {
			cmpAccerlation.setText(Integer.toString((int)value));	
		}


		
	};
	
	/**
	 * mConnection
	 * 
	 * Service proxy. Beim Verbindungsaufbau wir die Activity beim Service registriert.
	 */
	private final ServiceConnection mConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mServiceStub = null;			
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mServiceStub = IStepTrackerService.Stub.asInterface(service);
			try {
				mServiceStub.registerListener(mListener);
				mServiceStub.setStepLength(cmpStepLengthSlider.getProgress());
				mServiceStub.setLowerThreshold(cmpLowerThresholdSlider.getProgress()/10);
				mServiceStub.setUpperThreshold(cmpUpperThresholdSlider.getProgress()/10);
				
				
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	};
	
	
	/**
	 * onStartServiceClick
	 * 
	 * Startet den Service.
	 */
	private final OnClickListener onStartServiceClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			if(!recording){
				startService(new Intent(IStepTrackerService.class.getName()));
				bindService(new Intent(IStepTrackerService.class.getName()), mConnection, Context.BIND_AUTO_CREATE);
				recording = true;
				cmpStartService.setText("Aufzeichnung beenden");	
				cmpSteps.setText("0.0");
				cmpDist.setText("0.0");
			}
			else{
				try {
					if (mServiceStub != null){
						mServiceStub.stopTracking();
					}
					mServiceStub.unregisterListener(mListener);
					
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				unbindFromService();
				stopService(new Intent(IStepTrackerService.class.getName()));
				
				recording=false;
				cmpStartService.setText("Aufzeichnung starten");
				cmpDistLog.setText("Zurückgelegte Strecke: " + cmpDist.getText() + " m");
				cmpStepLog.setText("Anzahl Schritte: " + cmpSteps.getText());
			}		
		}
	};
	
	
	/**
	 * onExitClick
	 * 
	 * Meldet die Activity beim Service ab und beendet die Activity.
	 */
	private final OnClickListener onExitClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			unbindFromService();
			finish();
		}
	};
	
	/**
	 * onSeekBarChange
	 * 
	 * Reagiert auf die Justierung der Schrittlänge. 
	 */
	private final OnSeekBarChangeListener onSeekBarChange = new OnSeekBarChangeListener() {
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			if (mServiceStub != null) {
				try {
					// send offset to service
					mServiceStub.setStepLength(seekBar.getProgress());
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}						
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}
		
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// show offset
			cmpStepLength.setText("Schrittlänge = " + Integer.toString(progress)+" cm");
			
			
		}
	};
	
	/**
	 * onSeekBarChange
	 * 
	 * Reagiert auf die Justierung des UppterThreshold
	 */
	private final OnSeekBarChangeListener onUpperThresholdChange = new OnSeekBarChangeListener() {
		
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			
			int newValue = seekBar.getProgress();
			if(newValue <  cmpLowerThresholdSlider.getProgress()){
				seekBar.setProgress(cmpLowerThresholdSlider.getProgress());
			}
			
			if (mServiceStub != null) {
				try {
					// send offset to service
					mServiceStub.setUpperThreshold(seekBar.getProgress()/10);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}						
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}
		
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			cmpUpperThreshold.setText("Oberer Grenzwert = " + seekBar.getProgress()/10.0);	
		}
	};
	
	private final OnSeekBarChangeListener onLowerThresholdChange = new OnSeekBarChangeListener() {
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			
			int newValue = seekBar.getProgress();
			if(newValue >  cmpUpperThresholdSlider.getProgress()){
				seekBar.setProgress(cmpUpperThresholdSlider.getProgress());
			}
			
			if (mServiceStub != null) {
				try {
					// send offset to service
					mServiceStub.setLowerThreshold(seekBar.getProgress()/10);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}						
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}
		
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			cmpLowerThreshold.setText("Unterer Grenzwert = " + seekBar.getProgress()/10.0);
		}
	};
	
	/**
	 * unbindFromService
	 * @throws RemoteException 
	 */
	private void unbindFromService() {
		if (mServiceStub != null) {
			try {
				mServiceStub.stopTracking();
				recording = false;
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			unbindService(mConnection);
			mServiceStub = null;
		}
	}
	
}