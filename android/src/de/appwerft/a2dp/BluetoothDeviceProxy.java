package de.appwerft.a2dp;

import java.util.HashMap;
import java.util.Map;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;

public class BluetoothDeviceProxy extends KrollProxy {
	private BluetoothDevice device;
	private boolean nearby;
	private boolean connected;
	private boolean bonded;

	public BluetoothDeviceProxy(BluetoothDevice device, boolean connected,
			boolean nearby, boolean bonded) {
		super();
		this.device = device;
		this.nearby = nearby;
		this.connected = connected;
		this.bonded = bonded;

	}

	public void setNearby(boolean nearby) {
		this.nearby = nearby;
	}

	@Kroll.method
	public String getAddress() {
		return device.getAddress();
	}

	@Kroll.method
	public String getName() {
		return device.getName();
	}

	@Kroll.method
	public boolean getNearby() {
		return nearby;
	}

	@Kroll.method
	public boolean getConnected() {
		return connected;
	}

	@Kroll.method
	public boolean getPaired() {
		return bonded;
	}

	@Kroll.method
	public KrollDict getBluetoothClass() {
		Map<Integer, String> c = new HashMap<Integer, String>();
		c.put(BluetoothClass.Device.AUDIO_VIDEO_CAMCORDER,
				"AUDIO_VIDEO_CAMCORDER");
		c.put(BluetoothClass.Device.AUDIO_VIDEO_CAR_AUDIO,
				"AUDIO_VIDEO_CAR_AUDIO");

		c.put(BluetoothClass.Device.AUDIO_VIDEO_HANDSFREE,
				"AUDIO_VIDEO_HANDSFREE");
		c.put(BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES,
				"AUDIO_VIDEO_HEADPHONES");
		c.put(BluetoothClass.Device.AUDIO_VIDEO_HIFI_AUDIO,
				"AUDIO_VIDEO_HIFI_AUDIO");
		c.put(BluetoothClass.Device.AUDIO_VIDEO_LOUDSPEAKER,
				"AUDIO_VIDEO_LOUDSPEAKER");
		c.put(BluetoothClass.Device.AUDIO_VIDEO_MICROPHONE,
				"AUDIO_VIDEO_MICROPHONE");
		c.put(BluetoothClass.Device.AUDIO_VIDEO_PORTABLE_AUDIO,
				"AUDIO_VIDEO_PORTABLE_AUDIO");
		c.put(BluetoothClass.Device.AUDIO_VIDEO_SET_TOP_BOX,
				"AUDIO_VIDEO_SET_TOP_BOX");
		c.put(BluetoothClass.Device.AUDIO_VIDEO_UNCATEGORIZED,
				"AUDIO_VIDEO_UNCATEGORIZED");
		c.put(BluetoothClass.Device.AUDIO_VIDEO_VCR, "AUDIO_VIDEO_VCR");
		c.put(BluetoothClass.Device.AUDIO_VIDEO_VIDEO_CAMERA,
				"AUDIO_VIDEO_VIDEO_CAMERA");
		c.put(BluetoothClass.Device.AUDIO_VIDEO_VIDEO_CONFERENCING,
				"AUDIO_VIDEO_VIDEO_CONFERENCING");
		c.put(BluetoothClass.Device.AUDIO_VIDEO_VIDEO_DISPLAY_AND_LOUDSPEAKER,
				"AUDIO_VIDEO_VIDEO_DISPLAY_AND_LOUDSPEAKER");
		c.put(BluetoothClass.Device.AUDIO_VIDEO_VIDEO_GAMING_TOY,
				"AUDIO_VIDEO_VIDEO_GAMING_TOY");
		c.put(BluetoothClass.Device.AUDIO_VIDEO_VIDEO_MONITOR,
				"AUDIO_VIDEO_VIDEO_MONITOR");
		c.put(BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET,
				"AUDIO_VIDEO_WEARABLE_HEADSET");
		c.put(BluetoothClass.Device.COMPUTER_DESKTOP, "COMPUTER_DESKTOP");
		c.put(BluetoothClass.Device.COMPUTER_HANDHELD_PC_PDA,
				"COMPUTER_HANDHELD_PC_PDA");
		c.put(BluetoothClass.Device.COMPUTER_LAPTOP, "COMPUTER_LAPTOP");
		c.put(BluetoothClass.Device.COMPUTER_PALM_SIZE_PC_PDA,
				"COMPUTER_PALM_SIZE_PC_PDA");
		c.put(BluetoothClass.Device.COMPUTER_SERVER, "COMPUTER_SERVER");
		c.put(BluetoothClass.Device.COMPUTER_UNCATEGORIZED,
				"COMPUTER_UNCATEGORIZED");
		c.put(BluetoothClass.Device.COMPUTER_WEARABLE, "COMPUTER_WEARABLE");
		c.put(BluetoothClass.Device.HEALTH_BLOOD_PRESSURE,
				"HEALTH_BLOOD_PRESSURE");
		c.put(BluetoothClass.Device.HEALTH_DATA_DISPLAY, "HEALTH_DATA_DISPLAY");
		c.put(BluetoothClass.Device.HEALTH_GLUCOSE, "HEALTH_GLUCOSE");
		c.put(BluetoothClass.Device.HEALTH_PULSE_OXIMETER,
				"HEALTH_PULSE_OXIMETER");
		c.put(BluetoothClass.Device.HEALTH_PULSE_RATE, "HEALTH_PULSE_RATE");
		c.put(BluetoothClass.Device.HEALTH_THERMOMETER, "HEALTH_THERMOMETER");
		c.put(BluetoothClass.Device.HEALTH_UNCATEGORIZED,
				"HEALTH_UNCATEGORIZED");
		c.put(BluetoothClass.Device.HEALTH_WEIGHING, "HEALTH_WEIGHING");
		c.put(BluetoothClass.Device.PHONE_CELLULAR, "PHONE_CELLULAR");
		c.put(BluetoothClass.Device.PHONE_CORDLESS, "PHONE_CORDLESS");
		c.put(BluetoothClass.Device.PHONE_ISDN, "PHONE_ISDN");
		c.put(BluetoothClass.Device.PHONE_MODEM_OR_GATEWAY,
				"PHONE_MODEM_OR_GATEWAY");
		c.put(BluetoothClass.Device.PHONE_SMART, "PHONE_SMART");
		c.put(BluetoothClass.Device.PHONE_UNCATEGORIZED, "PHONE_UNCATEGORIZED");
		c.put(BluetoothClass.Device.WEARABLE_GLASSES, "WEARABLE_GLASSES");
		c.put(BluetoothClass.Device.WEARABLE_HELMET, "WEARABLE_HELMET");
		c.put(BluetoothClass.Device.WEARABLE_JACKET, "WEARABLE_JACKET");
		c.put(BluetoothClass.Device.WEARABLE_PAGER, "WEARABLE_PAGER");
		c.put(BluetoothClass.Device.WEARABLE_UNCATEGORIZED,
				"WEARABLE_UNCATEGORIZED");
		c.put(BluetoothClass.Device.WEARABLE_WRIST_WATCH,
				"WEARABLE_WRIST_WATCH");
		KrollDict kd = new KrollDict();
		int major = device.getBluetoothClass().getMajorDeviceClass();
		kd.put("code", major);
		try {
			kd.put("majordeviceclass", (String) c.get(major));
		} catch (Exception e) {

			e.printStackTrace();
		}
		return kd;
	}
}
