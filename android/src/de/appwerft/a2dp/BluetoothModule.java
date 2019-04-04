package de.appwerft.a2dp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiMessenger;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.util.TiActivityResultHandler;
import org.appcelerator.titanium.util.TiActivitySupport;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import de.appwerft.a2dp.utils.KrollCallbacks;

@Kroll.module(parentModule = A2dpModule.class)
public class BluetoothModule extends KrollModule {
	final static int REQUEST_CODE = 1;
	final static int NOTAVAILABLE = 0;
	final static int DISABLED = 1;
	final static int ENABLED = 2;
	final static String LCAT = A2dpModule.LCAT;
	BluetoothAdapter btAdapter;
	KrollCallbacks callbacks;

	private final class BTEnablerResultHandler implements
			TiActivityResultHandler {

		@Override
		public void onError(Activity arg0, int arg1, Exception arg2) {
			callbacks.call("onerror", new KrollDict());
		}

		@Override
		public void onResult(Activity arg0, int arg1, int arg2, Intent arg3) {
			KrollDict kd = new KrollDict();
		//	kd.put("name", btAdapter.getName());
		//	kd.put("address", btAdapter.getAddress());
			callbacks.call("onsuccess", kd);
		}
	}

	@Kroll.method
	public void disableBluetooth() {
		if (btAdapter != null)
			btAdapter.disable();
	}
/*
	@Kroll.method
	public Object[] getBoundedDevices() {
		ArrayList<HashMap> resList = new ArrayList<HashMap>();
		Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
		if (pairedDevices.size() > 0) {
			// There are paired devices. Get the name and address of each paired device.
			for (BluetoothDevice device : pairedDevices) {
				KrollDict dict = new KrollDict();
				dict.put("devicename", device.getName());
				dict.put("address", device.getAddress()); // MAC address
				dict.put("type", device.getType());
				dict.put("type", device.getType());
				resList.add(dict);
			}
		}
		return resList.toArray();
	}

	*/
	
	@Kroll.method
	public boolean enableBluetooth(
			@Kroll.argument(optional = true) KrollDict opts) {
		if (opts == null) {
			return btAdapter.enable();
		} else {
			callbacks = new KrollCallbacks(this, opts);
			final Intent intent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			final TiActivitySupport activitySupport = (TiActivitySupport) TiApplication
					.getInstance().getCurrentActivity();

			if (TiApplication.isUIThread()) {
				activitySupport.launchActivityForResult(intent, REQUEST_CODE,
						new BTEnablerResultHandler());
			} else {
				TiMessenger.postOnMain(new Runnable() {
					@Override
					public void run() {
						activitySupport.launchActivityForResult(intent,
								REQUEST_CODE, new BTEnablerResultHandler());
					}
				});
			}
			return true;
		}
	}

	@Kroll.method
	public boolean isEnabled() {
		return (getAvailibility() == ENABLED) ? true : false;
	}

	@Kroll.method
	public int getAvailibility() {
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		if (btAdapter == null) {
			Log.d(LCAT, "getAvailibility btAdapter = null");
			return NOTAVAILABLE;
		} else {
			Log.d(LCAT, "getAvailibility btAdapter = " + btAdapter.isEnabled());
			return (btAdapter.isEnabled()) ? ENABLED : DISABLED;
		}
	}
}
