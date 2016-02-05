package jk.edges.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by janne on 05.02.2016.
 */
public abstract class BluetoothActivity extends Activity{

    public static final UUID BLUETOOTH_UUID = java.util.UUID.randomUUID();
    private BluetoothAdapter mBluetoothAdapter;
    private static final int BLUETOOTH_REQUEST = 45654;
    public static final String BLUETOOTH_NAME = "Edges";

    protected BluetoothAdapter getAdapter(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter==null) goBack();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, BLUETOOTH_REQUEST);
        }
        return mBluetoothAdapter;
    }

    protected ArrayList<BluetoothDevice> getPairedDevices(){
        ArrayList<BluetoothDevice> devices = new ArrayList<>();
        if(devices.addAll(mBluetoothAdapter.getBondedDevices())) {
            return devices;
        }else{
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==BLUETOOTH_REQUEST){
            if(resultCode==RESULT_CANCELED){
                goBack();
            }
        }
    }

    public void goBack(){
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }
}
