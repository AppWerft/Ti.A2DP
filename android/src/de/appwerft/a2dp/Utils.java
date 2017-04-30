package de.appwerft.a2dp;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

public class Utils {

	public static BluetoothDevice findBondedDeviceByName(
			BluetoothAdapter adapter, String name) {
		for (BluetoothDevice device : getBondedDevices(adapter)) {
			if (name.matches(device.getName())) {
				return device;
			}
		}
		return null;
	}

	/**
	 * Safety wrapper around BluetoothAdapter#getBondedDevices() that is
	 * guaranteed to return a non-null result
	 * 
	 * @param adapter
	 *            the BluetoothAdapter whose bonded devices should be obtained
	 * @return the set of all bonded devices to the adapter; an empty set if
	 *         there was an error
	 */
	public static Set<BluetoothDevice> getBondedDevices(BluetoothAdapter adapter) {
		Set<BluetoothDevice> results = adapter.getBondedDevices();
		if (results == null) {
			results = new HashSet<BluetoothDevice>();
		}
		return results;
	}

}
