package de.appwerft.a2dp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.common.Log;

import android.bluetooth.BluetoothDevice;
import android.os.ParcelUuid;

public class PairedDevices extends ArrayList {
	private static PairedDevices instance = null;
	private final int NOTFOUND = -1;
	public static PairedDevices pairedDevices = new PairedDevices();
	private static List<BluetoothDevice> nearbyDevices = new ArrayList<BluetoothDevice>();

	private static int lastHashcode;
	private final static String LCAT = A2dpModule.LCAT;

	private static PairedDevicesChangedInterface callback;
	A2dpModule module;

	protected PairedDevices() {
	}

	public void setModule(A2dpModule module) {
		this.module = module;
	}

	public static PairedDevices getInstance() {
		if (instance == null) {
			instance = new PairedDevices();
		}
		return instance;
	}

	public static void resetList() {
		lastHashcode = 0;
		if (nearbyDevices != null)
			nearbyDevices.clear();

	}

	public void addNearbyDevice(BluetoothDevice device) {
		// resetList();
		nearbyDevices.add(device);
	}

	public void setDevices(List<PairedDevice> devices) {
		pairedDevices.clear();
		boolean found = false;
		if (devices != null) {
			for (PairedDevice device : devices) {
				found = false;
				if (nearbyDevices != null) {
					// Log.d(LCAT, nearbyDevices.toString());
					for (BluetoothDevice nearbyDevice : nearbyDevices) {
						if (nearbyDevice.getAddress().equals(
								device.getAddress()))
							found = true;
					}
					device.setNearby(found);
				} else {
					Log.w(LCAT, "nearbyDevice was null");
				}
				pairedDevices.add(device);
			}
		} else
			Log.e(LCAT, "devices was null");
		int hashCode = pairedDevices.hashCode();
		if (hashCode != lastHashcode && module.onReady != null) {
			KrollDict res = new KrollDict();
			res.put("devices", pairedDevices.toArray());
			module.onReady.call(module.getKrollObject(), res);
			lastHashcode = hashCode;
		}
	}

	public void setDevice(BluetoothDevice device, boolean connected) {
		/*
		 * Log.d(LCAT, device.getName()); PairedDevice pairedDevice = new
		 * PairedDevice(device, connected); int ndx = getDeviceIndex(device); if
		 * (ndx != NOTFOUND) { Log.d(LCAT, "device in list => remove");
		 * pairedDevices.remove(ndx); } // adding nearby field
		 * pairedDevice.setNearby(stringContainsItemFromList(
		 * pairedDevice.getAddress(), nearbyMacs));
		 * 
		 * pairedDevices.add(pairedDevice); Log.d(LCAT,
		 * pairedDevices.toString()); int hashCode = pairedDevices.hashCode();
		 * if (hashCode != lastHashcode && callback != null) {
		 * callback.devicesChanged(pairedDevices); }
		 */
	}

	public void setConnected(BluetoothDevice device, boolean state) {
		int ndx = getDeviceIndex(device);

	}

	// private helpers
	private int getDeviceIndex(BluetoothDevice device) {
		int ndx = NOTFOUND;
		int i = 0;
		for (Object d : pairedDevices) {
			if (((PairedDevice) d).getAddress().equals(device.getAddress())) {
				ndx = i;
				i++;
			}
		}
		return ndx;
	}

}
