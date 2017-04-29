/**
 * This file was auto-generated by the Titanium Module SDK helper for Android
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2010 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 *
 */
package de.appwerft.a2dp;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiApplication;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.media.AudioManager;

@Kroll.module(name = "A2dp", id = "de.appwerft.a2dp")
public class A2dpModule extends KrollModule {
	private static final String LCAT = "TiA2DP";
	private Context ctx = TiApplication.getInstance().getApplicationContext();
	BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
	@Kroll.constant
	final static int DEVICE_TYPE_CLASSIC = BluetoothDevice.DEVICE_TYPE_CLASSIC;
	@Kroll.constant
	final static int DEVICE_TYPE_DUAL = BluetoothDevice.DEVICE_TYPE_DUAL;
	@Kroll.constant
	final static int DEVICE_TYPE_LE = BluetoothDevice.DEVICE_TYPE_LE;
	@Kroll.constant
	final static int DEVICE_TYPE_UNKNOWN = BluetoothDevice.DEVICE_TYPE_UNKNOWN;
	@Kroll.constant
	final static int BOND_BONDED = BluetoothDevice.BOND_BONDED;
	@Kroll.constant
	final static int BOND_BONDING = BluetoothDevice.BOND_BONDING;
	@Kroll.constant
	final static int BOND_NONE = BluetoothDevice.BOND_NONE;
	private KrollFunction onReady;
	private int type = DEVICE_TYPE_UNKNOWN;

	public A2dpModule() {
		super();
	}

	@Kroll.onAppCreate
	public static void onAppCreate(TiApplication app) {
		Log.d(LCAT, "onAppCreate ====>");
	}

	private BluetoothProfile.ServiceListener profileListener = new BluetoothProfile.ServiceListener() {
		BluetoothA2dp mBluetoothSpeaker;

		public void onServiceConnected(int profile, BluetoothProfile proxy) {
			Log.d(LCAT, proxy.toString());
			if (profile == BluetoothProfile.A2DP) {
				// can be cast to a BluetoothA2dp instance
				mBluetoothSpeaker = (BluetoothA2dp) proxy;
				// Use reflection to acquire the connect(BluetoothDevice) method
				// on the proxy:
				try {
					Method connect = BluetoothA2dp.class.getDeclaredMethod(
							"connect", BluetoothDevice.class);
					Set<BluetoothDevice> devices = btAdapter.getBondedDevices();
					Log.d(LCAT,
							"======================== List of a2dp devices ============");
					KrollDict result = new KrollDict();
					List<KrollDict> list = new ArrayList<KrollDict>();
					for (BluetoothDevice device : devices) {
						if (device.getType() == type
								&& !device.getBluetoothClass().toString()
										.equals("1f00")) {
							KrollDict d = new KrollDict();
							d.put("name", device.getName());
							d.put("bondstate", device.getBondState());
							d.put("address", device.getAddress());
							d.put("type", device.getType());
							list.add(d);
						}
					}

					result.put("devices", list.toArray());
					if (hasListeners("ready")) {
						fireEvent("ready", result);
					}
					if (onReady != null) {
						Log.d(LCAT, "send back to JS");
						onReady.call(getKrollObject(), result);
					}
				} catch (NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
				// http://stackoverflow.com/questions/5171248/programmatically-connect-to-paired-bluetooth-device
				// no devices are connected
				List<BluetoothDevice> connectedDevices = mBluetoothSpeaker
						.getConnectedDevices();
			}
		}

		public void onServiceDisconnected(int profile) {
			if (profile == BluetoothProfile.A2DP) {
				mBluetoothSpeaker = null;
			}
		}
	};

	@Kroll.method
	public void startScan(KrollDict opts) {
		if (opts.containsKeyAndNotNull("type"))
			type = opts.getInt("type");

		if (opts.containsKeyAndNotNull("change"))
			onReady = (KrollFunction) opts.get("change");

		// Using this instance, get a profile proxy for A2DP
		btAdapter.getProfileProxy(ctx, profileListener, BluetoothProfile.A2DP);
	}

	@Kroll.method
	public void start(KrollDict opts) {
		startScan(opts);
	}

	@Kroll.method
	public boolean isActive() {
		AudioManager audioManager = (AudioManager) ctx
				.getSystemService(Context.AUDIO_SERVICE);
		return (audioManager.isBluetoothA2dpOn());
	}

	@Kroll.method
	public void stop() {
		btAdapter = BluetoothAdapter.getDefaultAdapter();
	}

	@Kroll.method
	public void connect(String name) {

	}

	@Kroll.method
	public void disconnect(String name) {

	}
}
