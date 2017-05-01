package de.appwerft.a2dp;

import java.util.ArrayList;
import java.util.List;

import android.bluetooth.BluetoothDevice;

public class PairedDevices extends ArrayList {
	private static PairedDevices instance = null;
	private final int NOTFOUND = -1;
	private List<BluetoothDevice> devices = new ArrayList<BluetoothDevice>();

	protected PairedDevices() {
	}

	public static PairedDevices getInstance() {
		if (instance == null) {
			instance = new PairedDevices();
		}
		return instance;
	}

	public void addDevice(BluetoothDevice device) {
		int ndx = getDeviceIndex(device);
		if (ndx != NOTFOUND) {
			devices.remove(ndx);
		}
		devices.add(device);
	}

	public void setConnected(BluetoothDevice device, boolean state) {
		int ndx = getDeviceIndex(device);

	}

	private int getDeviceIndex(BluetoothDevice device) {
		int ndx = NOTFOUND;
		int i = 0;
		for (BluetoothDevice d : devices) {
			if (d.getAddress().equals(device.getAddress())) {
				ndx = i;
				i++;
			}
		}
		return ndx;
	}
}
