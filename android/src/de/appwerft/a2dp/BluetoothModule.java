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
import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiMessenger;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.util.TiActivityResultHandler;
import org.appcelerator.titanium.util.TiActivitySupport;

import de.appwerft.a2dp.utils.KrollCallbacks;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;

@Kroll.module(parentModule = A2dpModule.class)
public class BluetoothModule extends KrollModule {
	final static int REQUEST_CODE = 1;
	final static int BT_NOTAVAILABLE = 0;
	final static int BT_DISABLED = 1;
	final static int BT_ENABLED = 2;
	final static String LCAT = A2dpModule.LCAT;
	private Context ctx = TiApplication.getInstance().getApplicationContext();
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
			kd.put("name", btAdapter.getName());
			kd.put("address", btAdapter.getAddress());
			callbacks.call("onsuccess", kd);
		}
	}

	@Kroll.method
	public void disableBluetooth() {
		if (btAdapter != null)
			btAdapter.disable();
	}

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
	public int getAvailibility() {
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		if (btAdapter == null) {
			Log.d(LCAT, "getAvailibility btAdapter = null");
			return BT_NOTAVAILABLE;
		} else {
			Log.d(LCAT, "getAvailibility btAdapter = " + btAdapter.isEnabled());
			return (btAdapter.isEnabled()) ? BT_ENABLED : BT_DISABLED;
		}
	}
}
