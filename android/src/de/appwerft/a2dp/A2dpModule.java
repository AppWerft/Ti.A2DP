/**
 * This file was auto-generated by the Titanium Module SDK helper for Android
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2010 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 *
 */
package de.appwerft.a2dp;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiApplication;

import android.app.Activity;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;

@Kroll.module(name = "A2dp", id = "de.appwerft.a2dp")
public class A2dpModule extends KrollModule {
	final static String sStarted = BluetoothAdapter.ACTION_DISCOVERY_STARTED;
	final static String sFinished = BluetoothAdapter.ACTION_DISCOVERY_FINISHED;
	final static String sFound = BluetoothDevice.ACTION_FOUND;
	final static int BT_NOTAVAILABLE = 0;
	final static int BT_DISABLED = 1;
	final static int BT_ENABLED = 2;

	public static final String LCAT = "TiA2DP";
	private Context ctx = TiApplication.getInstance().getApplicationContext();
	BluetoothAdapter btAdapter;

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
	public KrollFunction onReady;
	public int type = DEVICE_TYPE_UNKNOWN;

	Runnable cronJob;
	Handler handler = new Handler(Looper.getMainLooper());
	protected PairedDevices pairedDevices = PairedDevices.getInstance();
	protected DiscoveryNearbyDevicesModule discoveryNearbyDevices = new DiscoveryNearbyDevicesModule();

	/*
	 * class CallBackImpl implements CallBack { public void methodToCallBack() {
	 * System.out.println("I've been called back"); } }
	 */

	public A2dpModule() {
		super();
		pairedDevices.setModule(this);
		DiscoveryNearbyDevicesModule.setModule(this);
	}

	@Kroll.onAppCreate
	public static void onAppCreate(TiApplication app) {
	}

	// private BluetoothProfile.ServiceListener deviceScanner = new
	// DeviceScanner(
	// this);

	@Kroll.method
	public void startMonitorPairedDevices(KrollDict opts) {
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		if (btAdapter == null || !btAdapter.isEnabled())
			return;
		if (opts.containsKeyAndNotNull("type"))
			type = opts.getInt("type");

		if (opts.containsKeyAndNotNull("onchanged"))
			onReady = (KrollFunction) opts.get("onchanged");
		cronJob = new Runnable() {
			@Override
			public void run() {
				btAdapter.getProfileProxy(ctx, new BondedDevicesScanner(
						A2dpModule.this), BluetoothProfile.A2DP);
				handler.postDelayed(this, 12000);
			}
		};
		handler.post(cronJob);

		/*
		 * ctx.registerReceiver(MonitorBondedDevicesr, new IntentFilter(
		 * BluetoothDevice.ACTION_FOUND)); if (!btAdapter.isDiscovering()) {
		 * btAdapter.startDiscovery(); } else Log.w(LCAT,
		 * "was discovering, cannot start discovering");
		 */
	}

	@Kroll.method
	public void stopMonitorPairedDevices() {
		tearDown();
	}

	private void tearDown() {
		handler.removeCallbacks(cronJob);
		if (pairedDevices != null)
			PairedDevices.resetList();
		if (btAdapter != null && btAdapter.isDiscovering())
			btAdapter.cancelDiscovery();
	}

	@Kroll.method
	public void connectWith(String name) {
		Log.d(LCAT, "try to connect to " + name
				+ "##################################	");
		btAdapter.getProfileProxy(ctx, new DeviceConnector(this, name,
				"connect"), BluetoothProfile.A2DP);
	}

	@Kroll.method
	public void disconnectFrom(String name) {
		Log.d(LCAT, "try to disconnect from " + name
				+ "##################################	");
		btAdapter.getProfileProxy(ctx, new DeviceConnector(this, name,
				"disconnect"), BluetoothProfile.A2DP);
	}

	@Override
	public void onPause(Activity activity) {
		tearDown();
		super.onPause(activity);
	}

	@Override
	public void onResume(Activity activity) {
		super.onResume(activity);

	}

	@Override
	public void onStop(Activity activity) {
		tearDown();
		super.onStop(activity);
	}
	
	
	
	
	
	
	

	


}
