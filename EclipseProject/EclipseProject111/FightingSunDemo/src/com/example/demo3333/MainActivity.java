package com.example.demo3333;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.example.demo3333.BluetoothLeClass.OnDataAvailableListener;
import com.example.demo3333.BluetoothLeClass.OnServiceDiscoverListener;
import com.fro.util.FROSun;
import com.fro.util.HexStrConvertUtil;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class MainActivity extends Activity {
	public static String TAG = "MainActivity";

	private final Timer timer = new Timer();
	private TimerTask task;
	private Handler handler;

	private static final String UUID_SERVICE = "0000fff0-0000-1000-8000-00805f9b34fb";
	private static final String UUID_CHAR6 = "0000fff6-0000-1000-8000-00805f9b34fb";
	private static final String UUID_DESC = "00002902-0000-1000-8000-00805f9b34fb";

	private ToggleButton button;
	private ListView listDevice;
	private TextView error_tv;
	private TextView sun_tv;

	// BLE 设备
	private BluetoothLeClass mBLE;
	// 接收的字段
	private StringBuffer str_sb;
	// 蓝牙列表适配器
	private mBleDevicelistAdapter mbledevicelistAdapter;
	// 蓝牙 Gatt 服务
	private BluetoothGattService mService;
	// 蓝牙 GATT数据描述符
	private BluetoothGattCharacteristic gattCharacteristic_char6;
	// 是否开启读写数据
	private boolean isReadTaskRun;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		bindView();
		initData();
		initEvent();

		mBLE = new BluetoothLeClass(this);
		if (mBLE.initialize()) {
			error_tv.setText("初始化成功");
		} else {
			sun_tv.setText("初始化失败");
		}
		
		
		// 发现BLE终端的Service时回调
		mBLE.setOnServiceDiscoverListener(mOnServiceDiscover);
		// 收到BLE终端数据交互的事件
		mBLE.setOnDataAvailableListener(mOnDataAvailable);

	}

	private void bindView() {
		button = (ToggleButton) findViewById(R.id.button);
		listDevice = (ListView) findViewById(R.id.list);
		error_tv = (TextView) findViewById(R.id.error_tv);
		sun_tv = (TextView) findViewById(R.id.sun_tv);

	}

	private void initData() {
		str_sb = new StringBuffer("");
		mbledevicelistAdapter = new mBleDevicelistAdapter(this);
		listDevice.setAdapter(mbledevicelistAdapter);

	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	private void initEvent() {
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					if (isReadTaskRun) {
						ReadBle();
					}
				}

			}
		};

		task = new TimerTask() {

			@Override
			public void run() {
				Message message = new Message();
				message.what = 1;
				handler.sendMessage(message);

			}
		};
		// 开始定时器
		timer.schedule(task, 100, 1000);

		button.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@SuppressLint("NewApi")
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					mbledevicelistAdapter.clear();
					// 开始扫描
					mBLE.getmBluetoothAdapter().startLeScan(mLeScanCallback);
					   error_tv.setText("正在扫描BLE 终端....");
				} else {
					mBLE.getmBluetoothAdapter().stopLeScan(mLeScanCallback);
					   error_tv.setText("已停止扫描BLE 终端..");
				}

			}
		});

		// Item 点击
		listDevice.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				button.setChecked(false);
				mBLE.getmBluetoothAdapter().stopLeScan(mLeScanCallback);
				final BluetoothDevice device = mbledevicelistAdapter.getDevice(position);
						
				if (device == null)
					return;

				isReadTaskRun = !isReadTaskRun;
				if (isReadTaskRun) {
					// 建立Gatt 连接
					error_tv.setText("正在连接 BEL终端...");
					if (!mBLE.connect(device.getAddress())) {
						error_tv.setText("连接失败！");
					} else {
						mBLE.disconnect();
						error_tv.setText("断开连接");
					}

				}

			}

		});

	}

	/*
	 * 扫描到设备Device时的回调事件
	 */
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

		@Override
		public void onLeScan(final BluetoothDevice device, int rssi,
				byte[] scanRecord) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					mbledevicelistAdapter.addDevice(device);
					mbledevicelistAdapter.notifyDataSetChanged();
				}
			});
		}
	};

	/*
	 * 搜索到 BLE 终端服务的监听事件
	 */
	private BluetoothLeClass.OnServiceDiscoverListener mOnServiceDiscover = new OnServiceDiscoverListener() {

		@Override
		public void onServiceDiscover(BluetoothGatt gatt) {
			mService = gatt.getService(UUID.fromString(UUID_SERVICE));
			if (mService != null) {
				// UUID_CHAR6是可以跟蓝牙模块串口通信的Characteristic
				gattCharacteristic_char6 = mService.getCharacteristic(UUID
						.fromString(UUID_CHAR6));
				if (gattCharacteristic_char6 != null) {
					// 设置设备可通知
					mBLE.setCharacteristicNotification(
							gattCharacteristic_char6, true);
					// 更新接受区界面显示
					runOnUiThread(new Runnable() {
						public void run() {
							error_tv.setText("连接成功！");
						}
					});
				} else {
					Log.i(TAG, "mCharacteristic=null");
				}
			} else {
				Log.i(TAG, "mService=null");
				runOnUiThread(new Runnable() {
					public void run() {
						error_tv.setText("设备失败！");
					}
				});
			}
		}

	};

	/*
	 * 收到BLE终端数据交互的监听事件
	 */
	private BluetoothLeClass.OnDataAvailableListener mOnDataAvailable = new OnDataAvailableListener() {

		/*
		 * 收到 BLE终端数据被读的事件
		 */
		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			Log.i(TAG,
					"onCharRead"
							+ gatt.getDevice().getName()
							+ "read"
							+ characteristic.getUuid().toString()
							+ "--->"
							+ HexStrConvertUtil.bytesToHexString(characteristic
									.getValue()));

		}

		/*
		 * 收到BLE 终端 写入数据回调
		 */
		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			Log.i(TAG, "characteristic.getUuid().toString()="
					+ characteristic.getUuid().toString());
			if (characteristic.getUuid().toString().equals(UUID_CHAR6)) {
				// 接受数据
				Float fData = FROSun.getData(Const.SUN_LEN, Const.NODE_NUM,
						characteristic.getValue());
				String value = String.valueOf((int) (float) fData + "Lux");

				Log.i(TAG, "onChatWrite" + gatt.getDevice().getName()
						+ "  value" + value);
				// 追加到接收字段
				str_sb.replace(0, str_sb.length(), value);
				// 更新接受区界面显示
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						updateDisplaySendInfo();

					}

				});

			}

		}
	};

	/*
	 * 更新接受显示区
	 */
	private void updateDisplaySendInfo() {
		sun_tv.setText(str_sb.toString());
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		timer.cancel();
		mBLE.getmBluetoothAdapter().stopLeScan(mLeScanCallback);
		mBLE.disconnect();
		mBLE.close();

	}

	/*
	 * 读取BLE 的数据
	 */

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	private void ReadBle() {
		if (mBLE.getmBluetoothGatt() != null
				&& gattCharacteristic_char6 != null) {
			// 获取发送命令
			String writeStr = Const.SUN_CMD;
			// 设置数据内容
			gattCharacteristic_char6.setValue(HexStrConvertUtil
					.hexStringToByte(writeStr));
			// 往蓝牙模块写入数据
			mBLE.getmBluetoothGatt().writeCharacteristic(
					gattCharacteristic_char6);
			Log.i(TAG, "读取数据....");
		} else {
			Log.i(TAG,
					"mBLE.getBluetoothGatt()=null  or  gattCharacteristic_char6=null");
		}

	}

}
