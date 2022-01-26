package com.derry.serialport;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.derry.serialportlibrary.T;
import com.derry.serialportlibrary.listener.OnOpenSerialPortListener;
import com.derry.serialportlibrary.listener.OnSerialPortDataListener;
import com.derry.serialportlibrary.Device;
import com.derry.serialportlibrary.SerialPortManager;

import java.io.File;
import java.util.Arrays;

public class SerialPortActivity extends AppCompatActivity implements OnOpenSerialPortListener {
    // private static final String TAG = SerialPortActivity.class.getSimpleName();
    public static final String DEVICE = "device"; // 接收串口文件的标识
    public static final String BOTELV = "botelv"; // 接收波特率的标识
    int botelv = -1; // 波特率

    private SerialPortManager mSerialPortManager; // 打开串口，关闭串口，发生串口数据 需用的关联类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_port);

        Device device = (Device) getIntent().getSerializableExtra(DEVICE); // 串口文件设备描述的JavaBean Device
        botelv = Integer.valueOf(getIntent().getStringExtra(BOTELV)); // 波特率
        Log.i(T.TAG, "SerialPortActivity onCreate: device = " + device); // device = Device{name='ttyS0', root='serial', file=/dev/ttyS0} 【0】
        if (null == device) { // 如果串口文件为null，就全部结束返回
            finish();
            return;
        }

        mSerialPortManager = new SerialPortManager();

        // 打开串口
        boolean openSerialPort = mSerialPortManager.setOnOpenSerialPortListener(this)
                .setOnSerialPortDataListener(new OnSerialPortDataListener() {
                    @Override
                    public void onDataReceived(byte[] bytes) { // 接收到串口数据的监听函数
                        Log.i(T.TAG, "SerialPortActivity onDataReceived [ byte[] ]: " + Arrays.toString(bytes));
                        Log.i(T.TAG, "SerialPortActivity onDataReceived [ String ]: " + new String(bytes));
                        final byte[] finalBytes = bytes;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String hexString = ArraysUtils.bytesToHexString(finalBytes);
//                                showToast(String.format("接收\n%s", new String(finalBytes)));
                                showToast(String.format("接收 = %s", hexString));
                                Log.i(T.TAG, "SerialPortActivity"+String.format(" 接收\n%s", hexString));
                            }
                        });
                    }

                    /**
                     * 开启发生消息线程startSendThread - 调用 - 数据发送
                     * @param bytes 发送的数据
                     */
                    @Override
                    public void onDataSent(byte[] bytes) { // 发送串口数据的监听函数
                        Log.i(T.TAG, "SerialPortActivity onDataSent [ byte[] ]: " + Arrays.toString(bytes)); // onDataSent [ byte[] ]: [97] 【发送2】
                        Log.i(T.TAG, "SerialPortActivity onDataSent [ String ]: " + new String(bytes)); // onDataSent [ String ]: a
                        final byte[] finalBytes = bytes;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast(String.format("发送\n%s", new String(finalBytes)));
                            }
                        });
                    }
                })
                .openSerialPort(device.getFile(), botelv); // 串口设备文件，波特率

        Log.i(T.TAG, "SerialPortActivity onCreate: openSerialPort = " + openSerialPort);
        // openSerialPort = true 【4】
    }

    @Override
    protected void onDestroy() {  // Activity销毁时，关闭串口
        if (null != mSerialPortManager) {
            mSerialPortManager.closeSerialPort(); // 关闭串口
            mSerialPortManager = null;
        }
        super.onDestroy();
    }

    /**
     * 打开串口 openSerialPort 调用的当前此函数-打开串口成功
     * @param device 串口文件
     */
    @Override
    public void onSuccess(File device) {
        Toast.makeText(getApplicationContext(), String.format("串口 [%s] 打开成功", device.getPath()), Toast.LENGTH_SHORT).show();
    }

    /**
     * 打开串口 openSerialPort 调用的当前此函数-打开串口失败
     * @param device 串口文件
     * @param status 失败：状态
     */
    @Override
    public void onFail(File device, Status status) {
        switch (status) {
            case NO_READ_WRITE_PERMISSION:
                showDialog(device.getPath(), "没有读写权限");
                break;
            case OPEN_FAIL:
            default:
                showDialog(device.getPath(), "串口打开失败");
                break;
        }
    }

    /**
     * 打开串口失败 调用 显示提示框
     * @param title   title
     * @param message message
     */
    private void showDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

    /**
     * 发送数据
     * @param view view 布局上的按钮点击事件
     */
    public void onSend(View view) {
        EditText editTextSendContent = (EditText) findViewById(R.id.et_send_content);
        if (null == editTextSendContent) {
            return;
        }
        String sendContent = editTextSendContent.getText().toString().trim();
        if (TextUtils.isEmpty(sendContent)) {
            Log.i(T.TAG, "SerialPortActivity onSend: 发送内容为 null");
            return;
        }

//        byte[] sendContentBytes = sendContent.getBytes();
        byte[] sendContentBytes = hexToBytes(sendContent);
        boolean sendBytes = mSerialPortManager.sendBytes(sendContentBytes);
        Log.i(T.TAG, "onSend: sendBytes = " + sendBytes); // sendBytes = true  【发送3】
        showToast(sendBytes ? "发送成功" : "发送失败"); // 提示：发送成功 发送失败
    }

    public static byte[] hexToBytes(String hexStr) {
        if (hexStr == null) return null;
        if (hexStr.length() == 1) {
            hexStr = "0" + hexStr;
        }
        int length = hexStr.length() / 2;
        byte[] result = new byte[length];

        for (int i = 0; i < length; i++) {
            result[i] = (byte) Integer.parseInt(hexStr.substring(i * 2, i * 2 + 2), 16);
        }

        return result;
    }

    private Toast mToast;

    /**
     * 打开串口 调用 Toast的工具函数
     * @param content content
     */
    private void showToast(String content) {
        if (null == mToast) {
            mToast = Toast.makeText(getApplicationContext(), null, Toast.LENGTH_SHORT);
        }
        mToast.setText(content);
        mToast.show();
    }
}
