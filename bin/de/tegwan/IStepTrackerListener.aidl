package de.tegwan;

/**
	IStepTrackerListener
	
	Stubinterface der Activity für live Updates der Daten.
*/
interface IStepTrackerListener {
	void onOrientation(float orientation, float adaptedOrientation);
	void onStep(int steps);
}