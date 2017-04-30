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
import java.util.Timer;
import java.util.TimerTask;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.TiC;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass.Device;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;

@Kroll.module(name = "A2dp", id = "de.appwerft.a2dp")
public class A2dpModule extends KrollModule {
	String sStarted = BluetoothAdapter.ACTION_DISCOVERY_STARTED;
	String sFinished = BluetoothAdapter.ACTION_DISCOVERY_FINISHED;
	String sFound = BluetoothDevice.ACTION_FOUND;

	// https://code.tutsplus.com/tutorials/create-a-bluetooth-scanner-with-androids-bluetooth-api--cms-24084
	private final class discoveryMonitor extends BroadcastReceiver {

		public discoveryMonitor() {
			Log.d(LCAT, "discoveryMonitor started");
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(LCAT, intent.getAction());
			if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

				BTDeviceProxy d = new BTDeviceProxy(device);
				d.put("rssi", intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,
						Short.MIN_VALUE));
				d.put("name", intent.getStringExtra(BluetoothDevice.EXTRA_NAME));
				d.put("uuid", intent.getStringExtra(BluetoothDevice.EXTRA_UUID));
				d.put("device",
						intent.getStringExtra(BluetoothDevice.EXTRA_DEVICE));
				Log.d(LCAT, d.toString());
			}
		}
	}

	private final BroadcastReceiver discoveryMonitor = new discoveryMonitor();
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
	List<KrollDict> pairedDevices = new ArrayList<KrollDict>();

	public A2dpModule() {
		super();
	}

	@Kroll.onAppCreate
	public static void onAppCreate(TiApplication app) {
		Log.d(LCAT, "onAppCreate ====>");
	}

	// private BluetoothProfile.ServiceListener deviceScanner = new
	// DeviceScanner(
	// this);

	@Kroll.method
	public boolean init() {
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		if (btAdapter == null) {
			Log.w(LCAT, "init: btAdapter == null");
			return false;
		}
		if (btAdapter.isEnabled()) {
			Log.d(LCAT, "init: isEnabled");
			return true;
		}
		Log.d(LCAT, "init: try to enable");
		// btAdapter.enable();
		return (btAdapter.isEnabled()) ? true : false;
	}

	Timer cron = new Timer();

	@Kroll.method
	public void startScanPairedDevices(KrollDict opts) {
		if (opts.containsKeyAndNotNull("type"))
			type = opts.getInt("type");

		if (opts.containsKeyAndNotNull("onchanged"))
			onReady = (KrollFunction) opts.get("onchanged");

		cron.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				// Using this instance, get a profile proxy for A2DP
				btAdapter.getProfileProxy(ctx, new DeviceScanner(
						A2dpModule.this), BluetoothProfile.A2DP);
			}
		}, 0, 2024);
		ctx.registerReceiver(discoveryMonitor, new IntentFilter(
				BluetoothDevice.ACTION_FOUND));
		if (btAdapter.isEnabled() && !btAdapter.isDiscovering()) {
			btAdapter.startDiscovery();
		} else
			Log.w(LCAT, "BT disabled or discovering");
	}

	@Kroll.method
	public void getPairedDevices(KrollDict opts) {
		Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
		Log.d(LCAT, pairedDevices.toString());
	}

	@Kroll.method
	public boolean isBluetoothEnabled() {
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		if (bluetoothAdapter == null) {
			return false;
		} else {
			return bluetoothAdapter.isEnabled();
		}
	}

	@Kroll.method
	public boolean isBluetoothA2dpOn() {
		AudioManager audioManager = (AudioManager) ctx
				.getSystemService(Context.AUDIO_SERVICE);
		boolean available = audioManager.isBluetoothA2dpOn();
		Log.d(LCAT, "isBTActiv=" + available);
		return available;
	}

	@Kroll.method
	public void stopScanPairedDevices() {
		cron.cancel();
		ctx.unregisterReceiver(discoveryMonitor);
	}

	@Kroll.method
	public void connectWidth(String name) {
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

}
