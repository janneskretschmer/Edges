package jk.edges.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import jk.edges.R;

public class BTServerActivity extends BluetoothActivity {

    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btserver);
        bluetoothAdapter = getAdapter();
        Log.d("paired dev",getPairedDevices().size()+"");
    }
}
