package de.appwerft.a2dp;

import java.util.HashMap;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;

@SuppressWarnings("serial")
public class PairedDevice extends HashMap<String, Object> {
	public PairedDevice(BluetoothDevice device, boolean connected) {
		super();
		BluetoothClass cl = device.getBluetoothClass();
		this.put("address", device.getAddress());
		this.put("name", device.getName());
		this.put("connected", connected);

	}

	public void setNearby(boolean nearby) {
		this.put("nearby", nearby);
	}

	public String getAddress() {
		return (String) (this.get("address"));
	}
}