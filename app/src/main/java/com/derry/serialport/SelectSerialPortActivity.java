package com.derry.serialport;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.derry.serialport.adapter.DeviceAdapter;
import com.derry.serialportlibrary.Device;
import com.derry.serialportlibrary.SerialPortFinder;
import com.derry.serialportlibrary.T;

import java.util.ArrayList;

// 这个Activity要展示，所有可能打开的串口设备列表
public class SelectSerialPortActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    // private static final String TAG = SelectSerialPortActivity.class.getSimpleName();
    private DeviceAdapter mDeviceAdapter; // 显示串口列表的适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_serial_port);
        ListView listView = (ListView) findViewById(R.id.lv_devices); // 同学们：列出所有的串口
        ArrayList<Device> devices = new SerialPortFinder().getDevices(); // 获取所有的串口
        if (listView != null) {
            listView.setEmptyView(findViewById(R.id.tv_empty)); // 若没有串口，就显示tv_empty控件
            mDeviceAdapter = new DeviceAdapter(getApplicationContext(), devices); // 实例化适配器
            listView.setAdapter(mDeviceAdapter); // 把适配器设置到ListView中去
            listView.setOnItemClickListener(this); // 设置ListView item的监听
        }
        initPermission();
    }

//    @TargetApi(Build.VERSION_CODES.M)
    private void initPermission() {
        int i = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0x01);
        }
    }

    @Override // 点击ListView item 的监听触发的函数
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        EditText btlv = (EditText) findViewById(R.id.btlv); // 波特率
        Device device = mDeviceAdapter.getItem(position); // 获取具体的
        Log.d(T.TAG, "SelectSerialPortActivity onItemClick: " + device.toString());
        // Device{name='ttyS3', root='serial', file=/dev/ttyS3} 这个串口文件 不可用
        // Device{name='ttyS2', root='serial', file=/dev/ttyS2} 这个串口文件 不可用
        // Device{name='ttyS1', root='serial', file=/dev/ttyS1} 这个串口文件 是可以用的，有时候要重启电脑才能正常使用，毕竟是虚拟串口的
        // Device{name='ttyS0', root='serial', file=/dev/ttyS0} 这个串口文件 表面上是可以用的，实际上是一个坑货

        if (btlv.getText().toString().isEmpty()) { // 否则用户不填写波特率
            Toast.makeText(this, "你个货，没有设置波特率啊，我怎么对标频率呀，哎", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, SerialPortActivity.class); // SerialPortActivity串口读写操作
        intent.putExtra(SerialPortActivity.DEVICE, device); // 传递串口设备对象
        intent.putExtra(SerialPortActivity.BOTELV, btlv.getText().toString().trim()); // 传递波特率
        startActivity(intent); // 启动SerialPortActivity
    }
}
