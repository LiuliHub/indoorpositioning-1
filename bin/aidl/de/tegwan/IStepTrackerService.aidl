package de.tegwan;

import de.tegwan.IStepTrackerListener;

/**
	IStepTrackerService
	
	Stubinterface des Service. Bietet die M�glichkeit einen
	Listener (die Activity) zu registrieren, um live Schritte
	und Ausrichtung �bertragen zu bekommen.
	Mit setOffset() kann die Ausrichtung des Kompasses justiert
	werden.
*/
interface IStepTrackerService {
	void registerListener(IStepTrackerListener listener);
	void unregisterListener(IStepTrackerListener listener);
	void setStepLength(double offset);
	void setLowerThreshold(float lowerT);
	void setUpperThreshold(float upperT);
	void stopTracking();
}