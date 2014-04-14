/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: Z:\\codebase\\android_workspace\\InertialNavigation\\src\\de\\indoorpos\\IStepTrackerService.aidl
 */
package de.indoorpos;
/**
	IStepTrackerService
	
	Stubinterface des Service. Bietet die Möglichkeit einen
	Listener (die Activity) zu registrieren, um live Schritte
	und Ausrichtung übertragen zu bekommen.
	Mit setOffset() kann die Ausrichtung des Kompasses justiert
	werden.
*/
public interface IStepTrackerService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements de.indoorpos.IStepTrackerService
{
private static final java.lang.String DESCRIPTOR = "de.indoorpos.IStepTrackerService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an de.indoorpos.IStepTrackerService interface,
 * generating a proxy if needed.
 */
public static de.indoorpos.IStepTrackerService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof de.indoorpos.IStepTrackerService))) {
return ((de.indoorpos.IStepTrackerService)iin);
}
return new de.indoorpos.IStepTrackerService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_registerListener:
{
data.enforceInterface(DESCRIPTOR);
de.indoorpos.IStepTrackerListener _arg0;
_arg0 = de.indoorpos.IStepTrackerListener.Stub.asInterface(data.readStrongBinder());
this.registerListener(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_unregisterListener:
{
data.enforceInterface(DESCRIPTOR);
de.indoorpos.IStepTrackerListener _arg0;
_arg0 = de.indoorpos.IStepTrackerListener.Stub.asInterface(data.readStrongBinder());
this.unregisterListener(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_setStepLength:
{
data.enforceInterface(DESCRIPTOR);
double _arg0;
_arg0 = data.readDouble();
this.setStepLength(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_setLowerThreshold:
{
data.enforceInterface(DESCRIPTOR);
float _arg0;
_arg0 = data.readFloat();
this.setLowerThreshold(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_setUpperThreshold:
{
data.enforceInterface(DESCRIPTOR);
float _arg0;
_arg0 = data.readFloat();
this.setUpperThreshold(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_stopTracking:
{
data.enforceInterface(DESCRIPTOR);
this.stopTracking();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements de.indoorpos.IStepTrackerService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void registerListener(de.indoorpos.IStepTrackerListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_registerListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void unregisterListener(de.indoorpos.IStepTrackerListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_unregisterListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setStepLength(double offset) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeDouble(offset);
mRemote.transact(Stub.TRANSACTION_setStepLength, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setLowerThreshold(float lowerT) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeFloat(lowerT);
mRemote.transact(Stub.TRANSACTION_setLowerThreshold, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setUpperThreshold(float upperT) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeFloat(upperT);
mRemote.transact(Stub.TRANSACTION_setUpperThreshold, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void stopTracking() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_stopTracking, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_registerListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_unregisterListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_setStepLength = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_setLowerThreshold = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_setUpperThreshold = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_stopTracking = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
}
public void registerListener(de.indoorpos.IStepTrackerListener listener) throws android.os.RemoteException;
public void unregisterListener(de.indoorpos.IStepTrackerListener listener) throws android.os.RemoteException;
public void setStepLength(double offset) throws android.os.RemoteException;
public void setLowerThreshold(float lowerT) throws android.os.RemoteException;
public void setUpperThreshold(float upperT) throws android.os.RemoteException;
public void stopTracking() throws android.os.RemoteException;
}
