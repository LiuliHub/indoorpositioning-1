/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: Z:\\codebase\\android_workspace\\InertialNavigation\\src\\de\\indoorpos\\IStepTrackerListener.aidl
 */
package de.indoorpos;
/**
	IStepTrackerListener
	
	Stubinterface der Activity für live Updates der Daten.
*/
public interface IStepTrackerListener extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements de.indoorpos.IStepTrackerListener
{
private static final java.lang.String DESCRIPTOR = "de.indoorpos.IStepTrackerListener";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an de.indoorpos.IStepTrackerListener interface,
 * generating a proxy if needed.
 */
public static de.indoorpos.IStepTrackerListener asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof de.indoorpos.IStepTrackerListener))) {
return ((de.indoorpos.IStepTrackerListener)iin);
}
return new de.indoorpos.IStepTrackerListener.Stub.Proxy(obj);
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
case TRANSACTION_onOrientation:
{
data.enforceInterface(DESCRIPTOR);
float _arg0;
_arg0 = data.readFloat();
float _arg1;
_arg1 = data.readFloat();
this.onOrientation(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_onStep:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
float _arg1;
_arg1 = data.readFloat();
this.onStep(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_onAccerlation:
{
data.enforceInterface(DESCRIPTOR);
float _arg0;
_arg0 = data.readFloat();
this.onAccerlation(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements de.indoorpos.IStepTrackerListener
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
@Override public void onOrientation(float orientation, float adaptedOrientation) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeFloat(orientation);
_data.writeFloat(adaptedOrientation);
mRemote.transact(Stub.TRANSACTION_onOrientation, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onStep(int steps, float dist) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(steps);
_data.writeFloat(dist);
mRemote.transact(Stub.TRANSACTION_onStep, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onAccerlation(float value) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeFloat(value);
mRemote.transact(Stub.TRANSACTION_onAccerlation, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_onOrientation = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_onStep = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_onAccerlation = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
}
public void onOrientation(float orientation, float adaptedOrientation) throws android.os.RemoteException;
public void onStep(int steps, float dist) throws android.os.RemoteException;
public void onAccerlation(float value) throws android.os.RemoteException;
}
