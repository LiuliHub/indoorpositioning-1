package de.indoorpos;

import de.indoorpos.IStepTrackerListener;
import de.indoorpos.IStepTrackerService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteException;
import android.util.Log;

/**
 * StepTrackerService
 * 
 * Serviceklasse für Koppelnavigation. Bestimmt ausgehend von
 * einer GPS-Koordinate mit dem Beschleunigungssensor und dem
 * Kompass die ungefähre Bewegungsrichtung des Mobiltelefons.
 *
 */
public class StepTrackerService extends Service {
	
	private static final double DEGREE_PER_METER = 0.00001345;
	private static final boolean LOGGING = true;
	private static final int ORIENTATION_OFFSET = 0;
	
	private double mStepLength = 1;
	private float mTotalStepLength = 0;
	
	private Stepometer mStepometer = new Stepometer();
	private int mSteps = 0;
		
	private double mLastLatitude  =   48.744858;
	private double mLastLongitude =    9.106450; 
	
	private float mOrientationX = 0.0f;
	
	private IStepTrackerListener mListener;
	private SensorManager mSensorManager;
	private Sensor mAcceleratorSensor;
	private Sensor mOrientationSensor;
	
	private SensorEventListener mAcceleratorListener = new AcceleratorListener();
	private SensorEventListener mOrientationListener = new OrientationListener();
	
	private FileLogger mAccelerationLogger; 
	private GpxLogger mGpxLogger; 
	
	private WakeLock wl;
	/**
	 * onCreate
	 * 
	 * Initialisiert den Service. Logger werden instanziert und die Sensoren angesprochen.
	 */
	@Override
	public void onCreate() {
		mAccelerationLogger = new FileLogger("acceleration.data");
		mGpxLogger = new GpxLogger("track.gpx", System.currentTimeMillis());
		 
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAcceleratorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mOrientationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		
		mSensorManager.registerListener(mAcceleratorListener, mAcceleratorSensor, SensorManager.SENSOR_DELAY_FASTEST);
		mSensorManager.registerListener(mOrientationListener, mOrientationSensor, SensorManager.SENSOR_DELAY_FASTEST);
		
		mGpxLogger.addTrackpoint(mLastLatitude, mLastLongitude, System.currentTimeMillis());
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
        wl.acquire();
	}
	
	/**
	 * onDestroy
	 * 
	 * Behandelt die Servicezerstörung. Schreibt unter anderem die .gpx Datei.
	 */
	@Override
	public void onDestroy() {
		
		mGpxLogger.writeToFile();
		mGpxLogger.close();
		mAccelerationLogger.close();
		
		wl.release();
	}

	/**
	 * onBind
	 * 
	 * Gibt den Service-Stub zurück.
	 */
	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}
	
	/**
	 * IStepTrackerService.Stub
	 * 
	 * RPC Service-Stub. Dient nur dem Marshalling von Variablen.
	 * 
	 */
	private final IStepTrackerService.Stub mBinder = new IStepTrackerService.Stub() {
		
		@Override
		public void unregisterListener(IStepTrackerListener listener)
				throws RemoteException {
			mListener = null;
		}
		
		public void stopTracking() throws RemoteException{
			mSensorManager.unregisterListener(mAcceleratorListener);
			mSensorManager.unregisterListener(mOrientationListener);
			mSteps = 0;
		}
		
		@Override
		public void registerListener(IStepTrackerListener listener)
				throws RemoteException {
			mListener = listener;
		}



		@Override
		public void setStepLength(double offset) throws RemoteException {
			mStepLength = offset/100;	
			Log.d("StepLength", "changed to " + mStepLength);
		}

		@Override
		public void setLowerThreshold(float lowerT) throws RemoteException {
			mStepometer.setLowerThreshold(lowerT);
		}

		@Override
		public void setUpperThreshold(float upperT) throws RemoteException {
			mStepometer.setUpperThreshold(upperT);
		}
	};
	
	/**
	 * AcceleratorListener
	 * 
	 * Behandelt Events des Beschleunigungssensors. Sucht nach Mustern,
	 * um Schritte zu erkennen.  
	 *
	 */
	private class AcceleratorListener implements SensorEventListener {

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}

		/**
		 * onSensorChanged
		 * 
		 * Behandelt ein Sensorevent des Beschleunigungssensors.
		 * Zunächst wird bestimmt, ob ein Schritt gemacht wurde.
		 * Ist das der Fall, wird eine neue Position mit den aktuellen
		 * Werten des Kompasses und der definierten Schrittlänge 
		 * berechnet und in das Logfile geschrieben.
		 * Ferner wird die Activity aktualisiert und ein Sensorlog
		 * geschrieben.
		 */
		@Override
		public void onSensorChanged(SensorEvent event) {
			boolean step = false;
			// use sum of y- and z-axis to determine acceleration
			Float sum = event.values[1] + event.values[2];
			
			// update activity
			if (mListener != null){
				try {	
					mListener.onAccerlation(sum);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			
			// feed stepometer with sensor value and handle step
			Log.d("StepTracker ", "sum is " + sum);
			if (mStepometer.feed(sum)) {
				Log.d("StepTracker ", "detected as step ");
				mSteps++;
				mTotalStepLength += mStepLength;
				step = true;
								
				// update activity
				if (mListener != null){
					try {	
						mListener.onStep(mSteps,mTotalStepLength);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}
			// gpx and sensor logging 
			if (LOGGING) {
				// acceleration sensor values
				String data = Long.toString(System.currentTimeMillis())
								+ " " + Float.toString(event.values[0])
								+ " " + Float.toString(event.values[1])
								+ " " + Float.toString(event.values[2]);
				if (sum > mStepometer.getUpperThreshold() || sum < mStepometer.getLowerThreshold()) {
					data += " " + Float.toString(sum);
				} else {
					data += " -";
				}
				if (step) {
					data += " 0 " + Float.toString(mOrientationX);
				} else {
					data += " - -";
				}
				mAccelerationLogger.logData(data);

				// gpx log				
				if (step) {
					// determine next coordinate
					
					mLastLatitude  += mStepLength * DEGREE_PER_METER * Math.cos((mOrientationX + (float)ORIENTATION_OFFSET) * Math.PI / 180);
					mLastLongitude += mStepLength * DEGREE_PER_METER * Math.sin((mOrientationX + (float)ORIENTATION_OFFSET) * Math.PI / 180);
					mGpxLogger.addTrackpoint(mLastLatitude, mLastLongitude, System.currentTimeMillis());
				}
			}
		}	
	}
		
	/**
	 * OrientationListener
	 * 
	 * Behandelt Kompassevents. Ausrichtungswinkel wird nur zwischen-
	 * gespeichert und bei der Schritterkennung verwendet.
	 * 
	 */
	private class OrientationListener implements SensorEventListener {

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			mOrientationX = event.values[0];
			if (mListener != null) {
				try {
					mListener.onOrientation(mOrientationX, (mOrientationX + (float)ORIENTATION_OFFSET) % 360);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

