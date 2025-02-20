package com.example.mybledemo_morning;

import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

public class BluetoothLeClass {
	
	public  interface onServerDiscoverListener{
		 public void onServerDiscover(BluetoothGatt gatt);
	}
	public  interface onDataAvaliableListener{
		public void onCharacteristicRead(BluetoothGatt gatt,BluetoothGattCharacteristic character,int states);
		public void onCharacteristicWriter(BluetoothGatt gatt,BluetoothGattCharacteristic character);
	}
	private onServerDiscoverListener onServer;
	private onDataAvaliableListener onData;
	public void setOnServer(onServerDiscoverListener onSerer){
		this.onServer=onSerer;
	}
	public void setOnData(onDataAvaliableListener onData){
		this.onData=onData;
	}
	
	private Context context;
	private TextView info_tv;
	private BluetoothManager manager;
	public BluetoothAdapter adapter;
	public BluetoothGatt mgatt;
	
	public static final UUID CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID = UUID
			.fromString("00002902-0000-1000-8000-00805f9b34fb");
	
	
	public BluetoothLeClass( Context context,TextView info_tv) {
		this.context=context;
		this.info_tv=info_tv;
	}
	

	public boolean initData() {
		manager=(BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
		if(manager==null){
			return false;
		}
		adapter=manager.getAdapter();
		if(adapter==null){
			return false;
		}
		if(!adapter.enable()){
			Intent intent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			context.startActivity(intent);
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!adapter.isEnabled()){
			return false;
		}
		info_tv.setText("本机Ble已获得......");
		return true;
	}

	public boolean connect(String address) {
		
		if(mgatt!=null){
			if(!mgatt.connect()){
				return false;
			}else{
				return true;
			}
		}
		BluetoothDevice device=adapter.getRemoteDevice(address);
		if(device==null){
			return false;
		}
		mgatt=device.connectGatt(context, false, callback);
		return true;
	}

	//获取 Gatt 的回调方法
	private  BluetoothGattCallback callback=new BluetoothGattCallback() {
		//连接状态的改变
		public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
			if(newState==BluetoothProfile.STATE_CONNECTED){
				mgatt.discoverServices();
			}
		};
		
		//获取服务
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			if(status==BluetoothGatt.GATT_SUCCESS&&onServer!=null){
			onServer.onServerDiscover(gatt);
			}
		};
		
		public void onCharacteristicRead(BluetoothGatt gatt, android.bluetooth.BluetoothGattCharacteristic characteristic, int status) {
			if(onData!=null){
				onData.onCharacteristicRead(gatt, characteristic, status);
			}
			
			
		};
		
		public void onCharacteristicChanged(BluetoothGatt gatt, android.bluetooth.BluetoothGattCharacteristic characteristic) {
	if(onData!=null){
				onData.onCharacteristicWriter(gatt, characteristic);
			}
		
		};
	};
	
	
	
	

	public void disconnect() {
		
		
		
	}
	public void setCharacteristicNotification(BluetoothGattCharacteristic mCharacteristic, boolean b) {
		if(adapter==null&&mgatt==null){
			return;
		}
		if(b==true){
			mgatt.setCharacteristicNotification(mCharacteristic, true);
			BluetoothGattDescriptor descriptor=mCharacteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID);
			descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
			mgatt.writeDescriptor(descriptor);
			
		}else{
			mgatt.setCharacteristicNotification(mCharacteristic, false);
			BluetoothGattDescriptor descriptor=mCharacteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID);
			descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
			mgatt.writeDescriptor(descriptor);
			
		}
		
		
	}
	
	
	

}
