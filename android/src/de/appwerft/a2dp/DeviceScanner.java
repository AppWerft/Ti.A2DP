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

public final class DeviceScanner implements BluetoothProfile.ServiceListener {
	BluetoothA2dp mBluetoothSpeaker;
	private A2dpModule module;
	private final String LCAT = A2dpModule.LCAT;

	public DeviceScanner(A2dpModule module) {
		this.module = module;
	}

	@Override
	public void onServiceConnected(int profile, BluetoothProfile proxy) {
		KrollDict result = new KrollDict();
		if (profile == BluetoothProfile.A2DP) {
			final int[] states = new int[] { BluetoothProfile.STATE_CONNECTED,
					BluetoothProfile.STATE_CONNECTING };
			List<BluetoothDevice> connectedAd2dpDevices = new ArrayList<BluetoothDevice>();
			if (profile == BluetoothProfile.A2DP) {
				connectedAd2dpDevices.addAll(proxy
						.getDevicesMatchingConnectionStates(states));
			}
			// can be cast to a BluetoothA2dp instance
			mBluetoothSpeaker = (BluetoothA2dp) proxy;
			// get all bonded:
			Set<BluetoothDevice> devices = module.btAdapter.getBondedDevices();
			List<KrollDict> list = new ArrayList<KrollDict>();
			for (BluetoothDevice device : devices) {
				if (device.getType() == module.type
						&& !device.getBluetoothClass().toString()
								.equals("1f00")) {
					KrollDict d = new KrollDict();
					String name = device.getName();
					d.put("name", name);
					d.put("bondstate", device.getBondState());
					d.put("address", device.getAddress());
					boolean found = false;
					for (BluetoothDevice connectedDevice : connectedAd2dpDevices) {
						if (connectedDevice.getName().equals(name)) {
							found = true;
						}
					}
					d.put("connected", found);
					list.add(d);
				}
			}

			result.put("devices", list.toArray());

			if (module.onReady != null) {

				module.onReady.call(module.getKrollObject(), result);
			}

			// http://stackoverflow.com/questions/5171248/programmatically-connect-to-paired-bluetooth-device
			// no devices are connected
			List<BluetoothDevice> connectedDevices = mBluetoothSpeaker
					.getConnectedDevices();
		}
	}

	@Override
	public void onServiceDisconnected(int profile) {
		if (profile == BluetoothProfile.A2DP) {
			mBluetoothSpeaker = null;
		}
	}
}