package de.appwerft.a2dp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.common.Log;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;

import com.kcoppock.bluetoothconnector.*;

public final class DeviceConnector implements BluetoothProfile.ServiceListener,
		BluetoothBroadcastReceiver.Callback, BluetoothA2DPRequester.Callback {
	BluetoothA2dp mBluetoothSpeaker;
	private A2dpModule module;
	private String deviceName;
	private String action;

	private final String LCAT = A2dpModule.LCAT;

	public DeviceConnector(A2dpModule module, String deviceName, String action) {
		this.action = action;
		Log.d(LCAT, "Device|" + action + " started with " + deviceName);
		this.module = module;
		this.deviceName = deviceName;
	}

	@Override
	public void onServiceConnected(int profile, BluetoothProfile proxy) {
		Log.d(LCAT,
				"onServiceConnected ===>>>>>>>>>>>>>>>>>>>>>==========================");
		KrollDict result = new KrollDict();
		if (profile == BluetoothProfile.A2DP) {
			// can be cast to a BluetoothA2dp instance
			mBluetoothSpeaker = (BluetoothA2dp) proxy;
			// Use reflection to acquire the connect(BluetoothDevice) method
			// on the proxy:
			try {
				Method method = BluetoothA2dp.class.getDeclaredMethod(action,
						BluetoothDevice.class);
				BluetoothDevice mydevice = null;
				Set<BluetoothDevice> devices = module.btAdapter
						.getBondedDevices();
				if (devices != null) {
					for (BluetoothDevice device : devices) {
						if (deviceName.equals(device.getName())) {
							mydevice = device;
							try {
								Log.d(LCAT, "device found, ready for " + action);
								method.setAccessible(true);
								method.invoke(proxy, mydevice);
							} catch (InvocationTargetException ex) {
								Log.e(LCAT,
										"Unable to invoke connect(BluetoothDevice) method on proxy. "
												+ ex.toString());
							} catch (IllegalAccessException ex) {
								Log.e(LCAT, "Illegal Access! " + ex.toString());
							}
							break;
						}
					}
				}

			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			// http://stackoverflow.com/questions/5171248/programmatically-connect-to-paired-bluetooth-device
			// no devices are connected
			List<BluetoothDevice> connectedDevices = mBluetoothSpeaker
					.getConnectedDevices();
			List<KrollDict> list = new ArrayList<KrollDict>();
			for (BluetoothDevice devive : connectedDevices) {
				KrollDict d = new KrollDict();
				list.add(d);
			}
		}
	}

	@Override
	public void onServiceDisconnected(int profile) {
		Log.d(LCAT,
				"onServiceDisconnected =====<<<<<<<<<<<<<<<<========================");
		if (profile == BluetoothProfile.A2DP) {
			mBluetoothSpeaker = null;
		}
	}

	@Override
	public void onA2DPProxyReceived(BluetoothA2dp proxy) {
		Log.d(LCAT, "onA2DPProxyReceived");
		Method connect = getConnectMethod();
		BluetoothDevice device = Utils.findBondedDeviceByName(module.btAdapter,
				deviceName);

		// If either is null, just return. The errors have already been logged
		if (connect == null || device == null) {
			return;
		}

		try {
			connect.setAccessible(true);
			connect.invoke(proxy, device);
		} catch (InvocationTargetException ex) {
			Log.e(LCAT,
					"Unable to invoke connect(BluetoothDevice) method on proxy. "
							+ ex.toString());
		} catch (IllegalAccessException ex) {
			Log.e(LCAT, "Illegal Access! " + ex.toString());
		}

	}

	@Override
	public void onBluetoothConnected() {
		Log.d(LCAT, "onBluetoothConnected");

	}

	@Override
	public void onBluetoothError() {
		// TODO Auto-generated method stub

	}

	public Method getConnectMethod() {
		try {
			return BluetoothA2dp.class.getDeclaredMethod("connect",
					BluetoothDevice.class);
		} catch (NoSuchMethodException ex) {

			return null;
		}
	}
}

// https://github.com/kcoppock/bluetooth-a2dp
// http://stackoverflow.com/questions/5171248/programmatically-connect-to-paired-bluetooth-device