# Ti.A2DP

With this Appcelerator Titanium module you can connect with  previously paired A2DP Bluetooth Speaker.
   

## Permissions
```xml
/* Manifest permissions */
<uses-permission android:name="android.permission.BLUETOOTH"/>
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
```

## Usage

```javascript
var A2DP = require("de.appwerft.a2dp");

A2DP.startScan();
A2DP.addEventListener("load",function(res){
    // print the list of availabe BT devices:
    console.log(res.devices);
});

A2DP.connect(DEVCENAME);
A2DP.disconnect(DEVCENAME);

```
