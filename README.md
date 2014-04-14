Indoorpositioning
=================

Android app for inertial indoor positioning

Records an inertial trace from a starting coordinate (which is the current version hard coded).
Threshold values indicate at which accerlation values a step is detected.
In detail, the accerlation value must go above and below the respective threshold to detect a step.
Futhermore, the user can set the step length to get the accurate length of the trace.

After pushing stop, it saves a gpx trace file (can be shown in Google Earth) and a GnuPlot file for showing the accerlation over time.
