package com.example.connecttaskdemomodel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.fro.util.FROTemHum;
import com.fro.util.StreamUtil;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

public class connectTask extends AsyncTask<Void, String, Void> {
	private Context context;
	private TextView info_tv;
    private TextView tem_tv;
    private TextView hum_tv;
	
	private boolean state=false;
	
	private Socket sunSocket;
	private byte[] date;
	private Float tem;
	
	
	public connectTask(Context context,TextView info_tv,TextView tem_tv,TextView hum_tv) {
		this.context=context;
		this.info_tv=info_tv;
		this.tem_tv=tem_tv;
		this.hum_tv=hum_tv;
	}
	
	
	@Override
	protected void onPreExecute() {
		info_tv.setText("正在连接 ......");
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		sunSocket=getSocket(Constant.IP,Constant.port);
		if(sunSocket!=null) {//当有多个Socket 时会管用
		while(state) {
			try {
				StreamUtil.writeCommand(sunSocket.getOutputStream(),Constant.TEMHUM_CHK);
			    date=StreamUtil.readData(sunSocket.getInputStream());
			    tem=FROTemHum.getTemData(Constant.TEMHUM_LEN,Constant.TEMHUM_NUM,date);
			    publishProgress(tem+"");
			    tem=FROTemHum.getHumData(Constant.TEMHUM_LEN,Constant.TEMHUM_NUM,date);
			    publishProgress(tem+"");
			    Thread.sleep(200);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		}
		
		
		
		return null;
		
	}
protected  void onProgressUpdate(String[] values) {
	tem_tv.setText(String.valueOf(values[0]));
	tem_tv.setText(String.valueOf(values[1]));
	
	
};
	public Socket getSocket(String ip,int port) {
	           Socket	socket=new Socket();
	InetSocketAddress	address=new InetSocketAddress(ip,port);
		try {
			socket.connect(address,3000);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(socket.isConnected()&&!socket.isClosed()) {
			info_tv.setText("连接成功");
			return socket;
		}else {
			info_tv.setText("连接失敗");
			return null;
		}
	}
	
	public  void closeSocket(){
		if(sunSocket!=null) {
			try {
				sunSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setState(boolean b) {
	state=b;
	}
	
	public boolean getState() {
		return state;
	}
}
