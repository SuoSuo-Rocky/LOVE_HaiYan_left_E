/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.demo3333;

import java.util.List;
import java.util.UUID;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

/**
 * 
 * 閫氳繃锟�?锟�? 鎵樼鍦ㄤ竴涓粰瀹氱殑钃濈墮璁惧GATT鏈嶅姟鍣ㄤ负绠＄悊杩炴帴鍜屾暟鎹拷?锟戒俊鍋氭湇锟�?
 *
 * 
 * Service for managing connection and data communication with a GATT server
 * hosted on a given Bluetooth LE device.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
@SuppressLint("NewApi")
public class BluetoothLeClass {
	public static final String TAG = MainActivity.TAG;// BluetoothLeClass.class.getSimpleName();

	public static final UUID CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID = UUID
			.fromString("00002902-0000-1000-8000-00805f9b34fb");
	private BluetoothManager mBluetoothManager;
	private BluetoothAdapter mBluetoothAdapter;
	private String mBluetoothDeviceAddress;
	private BluetoothGatt mBluetoothGatt;

	public interface OnConnectListener {
		public void onConnect(BluetoothGatt gatt);
	}

	public interface OnDisconnectListener {
		public void onDisconnect(BluetoothGatt gatt);
	}

	public interface OnServiceDiscoverListener {
		public void onServiceDiscover(BluetoothGatt gatt);
	}

	public interface OnDataAvailableListener {
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status);

		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic);
	}

	private OnConnectListener mOnConnectListener;
	private OnDisconnectListener mOnDisconnectListener;
	private OnServiceDiscoverListener mOnServiceDiscoverListener;
	private OnDataAvailableListener mOnDataAvailableListener;
	private Context mContext;

	public void setOnConnectListener(OnConnectListener l) {
		mOnConnectListener = l;
	}

	public void setOnDisconnectListener(OnDisconnectListener l) {
		mOnDisconnectListener = l;
	}

	public void setOnServiceDiscoverListener(OnServiceDiscoverListener l) {
		mOnServiceDiscoverListener = l;
	}

	public void setOnDataAvailableListener(OnDataAvailableListener l) {
		mOnDataAvailableListener = l;
	}

	public BluetoothLeClass(Context c) {
		mContext = c;
	}

	/**
	 * 瀹炵幇鍥炶皟鏂规硶锛屽洜涓虹▼搴忛渶瑕佺敤鍒扮殑GATT浜嬩欢 渚嬪锛岃繛鎺ユ洿鏀瑰拰鍙戠幇鏈嶅姟绛変細瑙﹀彂鍒扮殑鏂规硶
	 * 
	 * Implements callback methods for GATT events that the app cares about. For
	 * example,connection change and services discovered.
	 */
	private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
		/**
		 * 褰撹皟鐢ㄤ簡杩炴帴鍑芥暟 mBluetoothGatt =
		 * bluetoothDevice.connectGatt(this.context, false,
		 * gattCallback);鎴栵拷?锟絚onnect锛堬級涔嬪悗锟�? 濡傛灉杩炴帴鎴愬姛灏变細 璧板埌 杩炴帴鐘讹拷?锟藉洖锟�?
		 */
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status,
				int newState) {
			if (newState == BluetoothProfile.STATE_CONNECTED) {
				if (mOnConnectListener != null)
					mOnConnectListener.onConnect(gatt);
				Log.i(TAG, "Connected to GATT server.");
				// Attempts to discover services after successful connection.
				Log.i(TAG, "Attempting to start service discovery:"
						+ mBluetoothGatt.discoverServices());

			} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
				if (mOnDisconnectListener != null)
					mOnDisconnectListener.onDisconnect(gatt);
				Log.i(TAG, "Disconnected from GATT server.");
			}
		}

		/**
		 * 褰撳垽鏂埌杩炴帴鎴愬姛涔嬪悗锛屼細鍘诲鎵炬湇鍔★紝 杩欎釜杩囩▼鏄紓姝ョ殑锛屼細鑰楃偣鏃堕棿锟�?
		 * 褰揹iscoverServices()瀵绘壘鍒版湇鍔′箣鍚庯紝浼氳蛋鍒板洖璋冿細
		 */
		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS
					&& mOnServiceDiscoverListener != null) {
				mOnServiceDiscoverListener.onServiceDiscover(gatt);
			} else {
				Log.i(TAG, "onServicesDiscovered received: " + status);
			}
		}

		/**
		 * 
		 * 濡傛灉mBluetoothGatt.readCharacteristic() 璇诲彇鏁版嵁锛堟垨鑰呰鍙栧叾浠栵拷?锟斤級鎴愬姛涔嬪悗
		 * 锛屼細鏉ュ埌 鍥炶皟锟�?
		 * 
		 */
		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			if (mOnDataAvailableListener != null)
				mOnDataAvailableListener.onCharacteristicRead(gatt,
						characteristic, status);
		}

		/**
		 * Notify(閫氱煡) notify 灏辨槸璁╄锟�?
		 * 鍙互鍙戯拷?锟斤拷?锟界煡缁欎綘锛屼篃鍙互璇翠笂鎶ワ拷?锟界粰浣狅紙鍙戯拷?锟藉懡浠ょ粰浣狅級
		 * 锟�?鏃﹁澶囬偅杈筺otify鏁版嵁缁欎綘锛屼綘浼氬湪鍥炶皟閲屾敹鍒帮細
		 */
		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			if (mOnDataAvailableListener != null)
				mOnDataAvailableListener.onCharacteristicWrite(gatt,
						characteristic);
		}
	};

	/**
	 * 鍒濆鍖栦竴涓弬鑰冩湰鍦拌摑鐗欙拷?锟介厤锟�? Initializes a reference to the local Bluetooth
	 * adapter.
	 * 
	 * 濡傛灉鍒濆鍖栨垚鍔燂紝杩斿洖true
	 * 
	 * @return Return true if the initialization is successful.
	 */
	public boolean initialize() {
		// Use this check to determine whether BLE is supported on the device.
		// Then you can
		// selectively disable BLE-related features.
		if (!mContext.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_BLUETOOTH_LE)) {
			Log.i(TAG, "Unable to initialize Bluetooth,do not has BLE system");
			return false;
		} else {
			Log.i(TAG, "initialize Bluetooth, has BLE system");
		}
		// For API level 18 and above, get a reference to BluetoothAdapter
		// through
		// BluetoothManager.
		if (mBluetoothManager == null) {
			mBluetoothManager = (BluetoothManager) mContext
					.getSystemService(Context.BLUETOOTH_SERVICE);
			if (mBluetoothManager == null) {
				Log.i(TAG, "Unable to initialize BluetoothManager.");
				return false;
			}
		}

		mBluetoothAdapter = mBluetoothManager.getAdapter();
		if (mBluetoothAdapter == null) {
			Log.i(TAG, "Unable to obtain a BluetoothAdapter.");
			return false;
		}

		// 鐩存帴鍚姩钃濈墮
		boolean isEnableBTA = mBluetoothAdapter.enable();
		if (isEnableBTA) {
			Log.i(TAG, "mBluetoothAdapter.enable");
		} else {
			Log.i(TAG, "mBluetoothAdapter.disable");
		}

		return isEnableBTA;
	}

	/**
	 * 杩炴帴鎵樼鍦ㄨ摑鐗橪E璁惧鐨凣ATT鏈嶅姟 Connects to the GATT server hosted on the
	 * Bluetooth LE device.
	 * 
	 * @param address
	 *            鐩爣璁惧鍦板潃 The device address of the destination device.
	 * 
	 * @return 
	 *         鑻ュ凡杩炴帴杩囩殑浼氳皟鐢╟onnect(),鑻ユ湭杩炴帴杩囩殑浼氳皟鐢╟onnectGatt(),濡傛灉杩炴帴鎴愬姛锛屽垯杩斿洖鐪燂拷
	 *         ?? 杩炴帴缁撴灉浼氬紓姝ワ拷?锟借繃鍥炶皟{ @浠ｇ爜bluetoothgattcallback #
	 *         onconnectionstatechange锛坅ndroid.bluetooth.bluetoothgatt锛宨nt锛宨nt锛墋
	 *         锟�? Return true if the connection is initiated successfully. The
	 *         connection result is reported asynchronously through the
	 *         {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
	 *         callback.
	 */
	public boolean connect(final String address) {
		if (mBluetoothAdapter == null || address == null) {
			Log.i(TAG,
					"BluetoothAdapter not initialized or unspecified address.");
			return false;
		}

		// Previously connected device. Try to reconnect.
		if (mBluetoothDeviceAddress != null
				&& address.equals(mBluetoothDeviceAddress)
				&& mBluetoothGatt != null) {
			Log.i(TAG,
					"Trying to use an existing mBluetoothGatt for connection.");
			if (mBluetoothGatt.connect()) {
				return true;
			} else {
				return false;
			}
		}

		final BluetoothDevice device = mBluetoothAdapter
				.getRemoteDevice(address);
		if (device == null) {
			Log.i(TAG, "Device not found.  Unable to connect.");
			return false;
		}
		// We want to directly connect to the device, so we are setting the
		// autoConnect
		// parameter to false.
		mBluetoothGatt = device.connectGatt(mContext, false, mGattCallback);
		Log.i(TAG, "Trying to create a new connection.");
		mBluetoothDeviceAddress = address;
		return true;
	}

	/**
	 * 鏂紑鐜版湁杩炴帴鎴栧彇娑堟寕璧风殑杩炴帴锟�? 杩欎釜鏂紑鐨勭粨鏋滀細寮傛閫氳繃鎶ュ憡鍥炶皟 { @浠ｇ爜bluetoothgattcallback
	 * # onconnectionstatechange锛坅ndroid.bluetooth.bluetoothgatt锛宨nt锛宨nt锛墋锟�?
	 * 
	 * Disconnects an existing connection or cancel a pending connection. The
	 * disconnection result is reported asynchronously through the
	 * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
	 * callback.
	 */
	public void disconnect() {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.i(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.disconnect();
	}

	/**
	 * 浣跨敤锟�?涓粰瀹氱殑BLE璁惧鍚庯紝搴旂敤绋嬪簭蹇呴』璋冪敤杩欎釜鏂规硶鏉ョ‘瀹氳祫婧愰噴鏀撅拷??
	 * 
	 * After using a given BLE device, the app must call this method to ensure
	 * resources are released properly.
	 */
	public void close() {
		if (mBluetoothGatt == null) {
			return;
		}
		mBluetoothGatt.close();
		mBluetoothGatt = null;
	}

	/**
	 * 瑕佹眰鍦ㄤ竴涓缁欏畾鐨剓 @浠ｇ爜bluetoothgattcharacteristic}锟�? 璇荤粨鏋滄槸閫氳繃寮傛鎶ュ憡鍥炶皟 { @浠ｇ爜bluetoothgattcallback
	 * # oncharacteristicread
	 * 锛坅ndroid.bluetooth.bluetoothgatt锛宎ndroid.bluetooth.
	 * bluetoothgattcharacteristic锛宨no锛墋
	 * 
	 * Request a read on a given {@code BluetoothGattCharacteristic}. The read
	 * result is reported asynchronously through the
	 * {@code BluetoothGattCallback#onCharacteristicRead
	 * (android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
	 * callback.
	 * 
	 * @param characteristic
	 *            The characteristic to read from.
	 */
	public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.i(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.readCharacteristic(characteristic);
	}

	/**
	 * 鎵撳紑鎴栧叧闂澶囩殑閫氱煡鍔熻兘锟�? 鍙傛暟 enable 灏辨槸鎵撳紑杩樻槸鍏抽棴锟�?
	 * characteristic灏辨槸浣犳兂璁剧疆BluetoothGattCharacteristic閲岀殑Descriptor锟�?
	 * 璁ヽharacteristic鍏锋湁閫氱煡鍔熻兘锟�?
	 * 
	 * Enables or disables notification on a give characteristic.
	 * 
	 * @param characteristic
	 *            Characteristic to act on.
	 * @param enabled
	 *            If true, enable notification. False otherwise.
	 */
	public void setCharacteristicNotification(
			BluetoothGattCharacteristic characteristic, boolean enabled) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.i(TAG, "BluetoothAdapter not initialized");
			return;
		}
		if (enabled == true) {
			Log.i(TAG, "Enable Notification");
			mBluetoothGatt.setCharacteristicNotification(characteristic, true);
			BluetoothGattDescriptor descriptor = characteristic
					.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID);
			descriptor
					.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
			mBluetoothGatt.writeDescriptor(descriptor);
		} else {
			Log.i(TAG, "Disable Notification");
			mBluetoothGatt.setCharacteristicNotification(characteristic, false);
			BluetoothGattDescriptor descriptor = characteristic
					.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID);
			descriptor
					.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
			mBluetoothGatt.writeDescriptor(descriptor);
		}

		// mBluetoothGatt.setCharacteristicNotification(characteristic,
		// enabled);
	}

	public void writeCharacteristic(BluetoothGattCharacteristic characteristic) {
		mBluetoothGatt.writeCharacteristic(characteristic);
	}

	/**
	 * 鑾峰緱鍦ㄨ宸茶繛鎺ョ殑璁惧鐨凣ATT鏈嶅姟鍒楄〃 杩欎簺鏈嶅姟閮芥槸鍦ㄨ皟鐢▄@code
	 * BluetoothGatt#discoverServices()} 鍚庢垚鍔熺殑鍙戠幇鐨勬湇鍔★拷??
	 * 
	 * Retrieves a list of supported GATT services on the connected device. This
	 * should be invoked only after {@code BluetoothGatt#discoverServices()}
	 * completes successfully.
	 * 
	 * @return A {@code List} of supported services.
	 */
	public List<BluetoothGattService> getSupportedGattServices() {
		if (mBluetoothGatt == null)
			return null;

		return mBluetoothGatt.getServices();
	}

	/**
	 * getter and setter
	 * 
	 * @return
	 */

	public BluetoothAdapter getmBluetoothAdapter() {
		return mBluetoothAdapter;
	}

	public void setmBluetoothAdapter(BluetoothAdapter mBluetoothAdapter) {
		this.mBluetoothAdapter = mBluetoothAdapter;
	}

	public BluetoothGatt getmBluetoothGatt() {
		return mBluetoothGatt;
	}

	public void setmBluetoothGatt(BluetoothGatt mBluetoothGatt) {
		this.mBluetoothGatt = mBluetoothGatt;
	}

}
