/**
 * This file was auto-generated by the Titanium Module SDK helper for Android
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2010 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 *
 */
package de.appwerft.a2dp;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiApplication;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import de.appwerft.a2dp.utils.KrollCallbacks;

@Kroll.module(parentModule = A2dpModule.class)
public class DiscoveryNearbyDevicesModule extends KrollModule {
	final static int REQUEST_CODE = 2;
	final static String LCAT = A2dpModule.LCAT;
	private Context ctx = TiApplication.getInstance().getApplicationContext();
	BluetoothAdapter btAdapter;
	KrollCallbacks callbacks;

	private final BroadcastReceiver discoveryResult = new BroadcastReceiver() {

		@Override
		public void onReceive(Context ctx, Intent intent) {
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				try {
					BluetoothDevice device = intent
							.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,
							Short.MIN_VALUE);

					Log.d(LCAT, "parcelname=" + device.getName());
					Log.d(LCAT, "address=" + device.getAddress());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	};

	@Kroll.method
	public void start(KrollDict opts) {
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		if (btAdapter == null || !btAdapter.isEnabled()) {
			Log.e(LCAT, "btAdapter null or disabled");
			return;
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		filter.addAction(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		ctx.registerReceiver(discoveryResult, new IntentFilter(
				BluetoothDevice.ACTION_FOUND));
		if (!btAdapter.isDiscovering()) {
			btAdapter.startDiscovery();
			Log.d(LCAT, "discovery nearby started");
		}
	}

	@Kroll.method
	public void stop() {
		tearDown();
	}

	@Override
	public void onPause(Activity activity) {
		tearDown();
		super.onPause(activity);
	}

	@Override
	public void onStop(Activity activity) {
		tearDown();
		super.onStop(activity);
	}

	private void tearDown() {
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		if (discoveryResult != null)
			ctx.unregisterReceiver(discoveryResult);
		if (btAdapter != null && btAdapter.isDiscovering())
			btAdapter.cancelDiscovery();
	}

}
