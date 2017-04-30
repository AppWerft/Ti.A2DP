# Ti.A2DP

With this Appcelerator Titanium module you can connect with  previously paired A2DP Bluetooth Speaker.
   

## Permissions
```xml
<uses-permission android:name="android.permission.BLUETOOTH"/>
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
```
The second permission you only need, if you want enable BT without interaction with user.

## Usage

### Availibility

```javascript
var BT = require("de.appwerft.a2dp").Bluetooth;
var state = BT.getAvailibility();
```
The result can be:

- [x] BT_NOTAVAILABLE 
- [x] BT_DISABLED
- [x] BT_ENABLED 

In case two you can:
```javascript
var BT = require("de.appwerft.a2dp").Bluetooth.enableBluetooth(); // needs permission, without arguments
```
Or:
In case two you can:
```javascript
var BT = require("de.appwerft.a2dp").Bluetooth.enableBluetooth({
	onsuccess : function(){},
	onerror : function(){}
}); 
```

### A2DP active?


```javascript
var A2DP = require("de.appwerft.a2dp");
A2DP.isBluetoothA2dpOn();
```
If `true` the device is connected with least one device.

### List all devices:

```javascript
var A2DP = require("de.appwerft.a2dp");
if (A2DP.init() {
    A2DP.startScanPairedDevices({
    	repeat : true,
        onchanged : function(res) {
        }
    });
    A2DP.startScanNearbyDevices({
        onchanged : function(res) {
        }
    });

}

```
Typical answer:
```javascript
[{
"address" : "08:DF:1F:C6:58:01",
"name" : "Bose Mini II SoundLink",
"class" : "240414",
"type" : 1
}, {
"address" : "34:4D:F7:A7:5C:0C",
"name" : "G Watch 1887",
"class" : "a0704",
"type" : 3
}, {
"address" : "5C:EB:68:21:C1:4A",
"name" : "MAJOR II BLUETOOTH",
"class" : "240404",
"type" : 1
}, {
"address" : "80:E4:DA:72:74:44",
"name" : "f022cnRE",
"class" : "1f00",
"type" : 2
}, {
"address" : "40:EF:4C:C1:5D:DD",
"name" : "STANMORE Speaker",
"class" : "240414",
"type" : 1
}]
```
### Constants

#### Device types
- [x] DEVICE_TYPE_CLASSIC   Bluetooth device type, Classic - BR/EDR devices 
- [x] DEVICE_TYPE_DUAL      Bluetooth device type, Dual Mode - BR/EDR/LE 
- [x] DEVICE_TYPE_LE        Bluetooth device type, Low Energy - LE-only 
- [x] DEVICE_TYPE_UNKNOWN


#### Bond state
@Kroll.constant
- [x]  BOND_BONDED
- [x]  BOND_BONDING
- [x]  BOND_NONE

### Test if A2DP active
```javascript
A2DP.isActive();
```



### Connect

```javascript
A2DP.connectTo(DEVICE_NAME);
A2DP.disconnectFrom(DEVICE_NAME);

```
