package de.indoorpos;

import android.util.Log;

/**
 * Stepometer
 * 
 * Bestimmt aus Beschleunigungssensorwerten Muster,
 * die einen Schritt darstellen. In dieser Implementierung
 * wird ein Wertepaar von Beschleunigungen gesucht, dessen
 * erster Wert über einem oberen Schwellenwert liegt und dessen
 * zweiter Wert unter einem Schwellenwert liegt. Dies ist die
 * typische Abfolge von Beschleunigungen bei Schritten.
 * Beim Abstoßen erfährt das Mobiltelefon eine positive 
 * Beschleunigung, beim Auftreten eine negative Beschleunigung.
 * Da ein Schritt innerhalb einer gewissen Zeit erfolgt, werden
 * nur solche Paare berücksichtigt, die einen begrenzten zeitlichen
 * Abstand haben.
 *
 */
public class Stepometer {
	
	private float mUpperThreshold = 130;//11.5f; // positive acceleration
	private float mLowerThreshold = 100;//10.5f; // negative acceleration
	private static final long TIME_THRESHOLD = 500; // step must be finished in half a second 
	
	private long mLastUpperThreshold = 0;
	
	public Stepometer() {
		
	}
	
	/**
	 * feed
	 * 
	 * Bestimmt, ob der aktuelle Sensorwert einen Schritt abschließt.
	 * 
	 * @param value Sensorwert
	 * @return true, wenn ein Schritt erkannt wurde
	 */
	public boolean feed(float value) {
		boolean step = false;
		
		if (value > mUpperThreshold) {
			mLastUpperThreshold = System.currentTimeMillis();
		} else if (value < mLowerThreshold) {
			if (System.currentTimeMillis() - mLastUpperThreshold < TIME_THRESHOLD) {
				step = true;
			}
			mLastUpperThreshold = 0;
		}
		
		return step;
	}

	public float getUpperThreshold() {
		return mUpperThreshold;
	}

	public float getLowerThreshold() {
		return mLowerThreshold;
	}

	public void setUpperThreshold(float mUpperThreshold) {
		this.mUpperThreshold = mUpperThreshold;
		Log.d("Stepometer", "Upper Threshold now: " +mUpperThreshold);
	}

	public void setLowerThreshold(float mLowerThreshold) {
		this.mLowerThreshold = mLowerThreshold;
		Log.d("Stepometer", "Lower Threshold now: " + mLowerThreshold);
	}
}
