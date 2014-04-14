package de.tegwan;

/**
	IStepTrackerListener
	
	Stubinterface der Activity f�r live Updates der Daten.
*/
interface IStepTrackerListener {
	void onOrientation(float orientation, float adaptedOrientation);
	void onStep(int steps);
}