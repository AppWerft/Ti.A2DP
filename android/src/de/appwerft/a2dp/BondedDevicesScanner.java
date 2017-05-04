package de.appwerft.a2dp;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.common.Log;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;

public final class BondedDevicesScanner implements
		BluetoothProfile.ServiceListener {
	BluetoothA2dp bluetoothSpeaker;
	private A2dpModule module;
	private final String LCAT = A2dpModule.LCAT;
	int lastHashcode;

	public BondedDevicesScanner(A2dpModule module) {
		this.module = module;
	}

	@Override
	public void onServiceConnected(int profile, BluetoothProfile proxy) {
		KrollDict result = new KrollDict();
		if (profile == BluetoothProfile.A2DP) {
			// first variant:
			final int[] states = new int[] { BluetoothProfile.STATE_CONNECTED,
					BluetoothProfile.STATE_CONNECTING };
			List<BluetoothDevice> connectedDevices = new ArrayList<BluetoothDevice>();
			if (profile == BluetoothProfile.A2DP) {
				connectedDevices.addAll(proxy
						.getDevicesMatchingConnectionStates(states));
			}
			// second variant:
			bluetoothSpeaker = (BluetoothA2dp) proxy;
			List<BluetoothDevice> connectedA2dpDevices = bluetoothSpeaker
					.getConnectedDevices();
			for (BluetoothDevice dev : connectedA2dpDevices) {
				// Log.d(LCAT, dev.getAddress() + "  " + dev.getName());
			}

			// get all bonded:
			Set<BluetoothDevice> devices = module.btAdapter.getBondedDevices();
			List<PairedDevice> pairedDevices = new ArrayList<PairedDevice>();
			for (BluetoothDevice device : devices) {
				if (device.getType() == module.type
						&& !device.getBluetoothClass().toString()
								.equals("1f00")) {
					boolean found = false;
					for (BluetoothDevice connectedDevice : connectedDevices) {
						if (connectedDevice.getAddress().equals(
								device.getAddress())) {
							found = true;
						}
					}
					// module.pairedDevices.setDevice(device, found);
					pairedDevices.add(new PairedDevice(device, found));
				}
			}
			module.pairedDevices.setDevices(pairedDevices);
			// http://stackoverflow.com/questions/5171248/programmatically-connect-to-paired-bluetooth-device
			// no devices are connected

		}
	}

	@Override
	public void onServiceDisconnected(int profile) {
		if (profile == BluetoothProfile.A2DP) {
			bluetoothSpeaker = null;
		}
	}
}