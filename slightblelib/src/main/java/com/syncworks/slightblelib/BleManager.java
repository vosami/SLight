package com.syncworks.slightblelib;

import android.content.Context;

/**
 * Created by vosami on 2015-03-19.
 * Bluetooth Low Energy API Manager
 * 블루투스 사용을 간편하게 해주는 API 매니저
 */
public class BleManager {
    private final static Class TAG_C = BleManager.class;
    // 디버그용 태그
    private final static String TAG = BleManager.class.getSimpleName();
    // 기본 블루투스 검색 시간(10초)
    public final static long DEFAULT_SCAN_PERIOD = 1000;
    // 스마트라이트 Context
    private Context _context;
    // BleManager 를 하나만 만들기 위해 Static 설정
    protected static BleManager client = null;
    /*
    private final ConcurrentMap<BleConsumer, ConsumerInfo> consumers = new ConcurrentHashMap<>();

    private BluetoothLeService bluetoothLeService = null;

    protected BleNotifier bleNotifier = null;

    private static boolean sAndroidLScanningDisabled = false;
    private static boolean sManifestCheckingDisabled = false;

    public static BleManager getBleManager(Context context) {
        if (client == null) {
            Logger.d(TAG_C,"BleManager Creation");
            client = new BleManager(context);
        }
        return client;
    }

    // 블루투스 매니저 생성자 - getBleManager 메소드로 생성할 것
    protected BleManager(Context context) {
        _context = context;
    }
    // 해당 안드로이드 장치가 Ble 를 지원하는지 확인
    public boolean checkAvailability() {
        if (Build.VERSION.SDK_INT <18) {
            Logger.d(TAG_C, "Bluetooth LE not supported by this device");
        }
        if (!_context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Logger.d(TAG_C, "Bluetooth LE not supported by this device");
        } else if (((BluetoothManager)_context.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter().isEnabled()) {
            return true;
        }
        return false;
    }

    public void bind(BleConsumer consumer) {
        if (!checkAvailability()) {
            return;
        }
        synchronized (consumers) {
            ConsumerInfo consumerInfo = consumers.putIfAbsent(consumer, new ConsumerInfo());
            if (consumerInfo != null) {
                Logger.d(TAG, "This consumer is already bound");
            }
            else {
                Logger.d(TAG, "This consumer is not bound. binding:"+ consumer);
                Intent intent = new Intent(consumer.getApplicationContext(), BluetoothLeService.class);
                consumer.bindService(intent, bluetoothLeServiceConnection ,Context.BIND_AUTO_CREATE);
                Logger.d(TAG,"consumer count is now: "+consumers.size());

            }
        }
    }

    public void unbind(BleConsumer consumer) {
        if (!checkAvailability()) {
            return;
        }
        synchronized (consumers) {
            if (consumers.containsKey(consumer)) {
                Logger.d(TAG, "Unbind");
                consumer.unbindService(bluetoothLeServiceConnection);
                consumers.remove(consumer);
                if (consumers.size() == 0) {
                    bluetoothLeService = null;
                }
            }
            else {
                Logger.d(TAG,"This consumer is not bound to: "+consumer);
            }
        }
    }

    private ServiceConnection bluetoothLeServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!bluetoothLeService.initialize()) {
                Logger.e(TAG, "Unable to initialize Bluetooth");
                return;
            }
            Logger.d(TAG, "I get the SmartLight Service");
            synchronized (consumers) {
                Iterator<Map.Entry<BleConsumer,ConsumerInfo>> iterator = consumers.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<BleConsumer, ConsumerInfo> entry = iterator.next();
                    if (!entry.getValue().isConnected) {
                        entry.getKey().onBleServiceConnect();
                        entry.getValue().isConnected = true;
                    }
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Logger.d(TAG, "onServiceDisconnected");
            //bluetoothLeService = null;
        }
    };

    private final BroadcastReceiver bleUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            // 연결 완료 메시지를 받으면
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                Logger.d(TAG, "Connected");
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                Logger.d(TAG, "Disconnected");
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                Logger.d(TAG, "Service Discovered");
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                Logger.d(TAG, "Data Available");
            } else if (BluetoothLeService.ACTION_DATA_WRITE_COMPLETE.equals(action)) {
                Logger.d(TAG, "Data Write Complete");
            }
        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_WRITE_COMPLETE);

        return intentFilter;
    }

    private class ConsumerInfo {
        public boolean isConnected = false;
    }
*/

}
