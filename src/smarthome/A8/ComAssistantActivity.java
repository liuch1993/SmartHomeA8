package smarthome.A8;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.json.JSONException;
import org.json.JSONObject;

import smarthome.bean.AssistBean;
import smarthome.bean.ComBean;
import tools.NetWork;
import tools.Status;
import tools.URLValues;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.test.suitebuilder.annotation.Smoke;
import android.util.Base64;
import android.view.View;

import android.widget.Button;

import android.widget.CompoundButton;

import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ComAssistantActivity extends Activity {

	public  boolean kled=false,sled=false,aled=false,bled=false,safe=false,wind=false,fan=false;
	public  boolean sleep=false,home=false,smart=false;
	String smog, honwai, tem, hum, light;

	public String set = "";
	TextView editTexttem, editTexthum, editTextlight, editTexthongwai,
			editTextyanwu;
	Button fanOn, fanOff, kledOn, kledOff, sledOn, sledOff, aledOn, aledOff,
			bledOn, bledOff, windon, windoff, homeBT, sleepBT, safeOn, safeOff,smartOn,smartOff;

	SerialControl ComA;
	DispQueueThread DispQueue;// 刷新显示线程

	AssistBean AssistData;// 用于界面数据序列化和反序列化
	int iRecLines1 = 0;// 接收区行数
	int iRecLines2 = 0;// 接收区行数
	final int sendToastWhat = 0x101, comeDataResultWhat = 0x102,
			getControlWhat = 0x103, sendControlResultWhat = 0x104;
	final String sendToastKey = "sendToastKey",
			comeDataResultKey = "comeDataResultKey",
			getControlKey = "getControlKey",
			sendControlResultKey = "sendControlResultKey";
	/** Called when the activity is first created. */

	boolean flag = true;
	Handler handler = new Handler() {
		public void handleMessage(Message message) {
			switch (message.what) {
			case sendToastWhat:
				String toast = message.getData().getString(sendToastKey);
				Toast.makeText(ComAssistantActivity.this, toast,
						Toast.LENGTH_SHORT).show();
				break;
			case comeDataResultWhat:
				String content = message.getData().getString(comeDataResultKey);
				if (content.equals("") || content.contains("winon")
						|| content.contains("winoff")
						|| content.contains("windon")
						|| content.contains("windoff")
						|| content.contains("kledon")
						|| content.contains("kledoff")
						|| content.contains("sledon")
						|| content.contains("sledoff")
						|| content.contains("aledon")
						|| content.contains("aledoff")
						|| content.contains("bledon")
						|| content.contains("bledoff")
						|| content.contains("home")
						|| content.contains("sleep")) {
					return;
				}
//				sendToast(content);
				
				  //用电器测试
				/* if(content.contains("li")||content.contains("smog")||(
				  content.contains("h")&&!content.contains("hh"))){ return ; }*/
				
				if (content.contains("somebody")) {
					sendMonitor("hongwai", "somebody");
				}
				if (content.contains("ffon")) {
					kled = true;
					sendControl("kled", true);
				}
				if (content.contains("ffoff")) {
					kled = false;
					sendControl("kled", false);
				}
				if (content.contains("ccon")) {
					sled = true;
					sendControl("sled", true);
				}
				if (content.contains("ccoff")) {
					sled = false;
					sendControl("sled", false);
				}
				if (content.contains("ddon")) {
					aled = true;
					sendControl("aled", true);
				}
				if (content.contains("ddoff")) {

					aled = false;
					sendControl("aled", false);
				}
				if (content.contains("ggon")) {
					fan = true;
					sendControl("fan", true);
				}
				if (content.contains("ggoff")) {
					fan = false;
					sendControl("fan", false);
				}
				if (content.contains("eeon")) {
					bled = true;
					sendControl("bled", true);
				}
				if (content.contains("eeoff")) {
					bled = false;
					sendControl("bled", false);
				}
				if (content.contains("smog")) {
					smog = content.substring(4);
					editTextyanwu.setText(smog);
					sendMonitor("smog", smog);
				}
				if (content.contains("li")) {
					light = content.substring(2);
					editTextlight.setText(light);
					if(smart&&wind&&Double.parseDouble(light)<10){
						sendPortData(ComA, "winoff");
					}
					if(smart&&!wind&&(Double.parseDouble(light)>250)){
						sendPortData(ComA,"winon");
					}
					sendMonitor("light", light);
				}
				if (content.contains("h") && !content.contains("hh")) {
					tem = content.substring(3, 5);
					hum = content.substring(5, 7);
					editTexttem.setText(tem);
					editTexthum.setText(hum);
					sendMonitor("tem", tem);
					sendMonitor("hum", hum);
				}
				if (content.contains("hh")) {
					 wind = true;
					sendControl("win", true);
				}
				if (content.contains("ii")) {
					 wind = false;
					sendControl("win", false);
				}

				if (content.contains("aa")) {
					home = false;
				
					sendControl("home", false);
					
				}
				if (content.contains("bb")) {
					sleep = false;
					sendControl("sleep", false);
				}
				break;
			case getControlWhat:
				String control = message.getData().getString(getControlKey);
				try {
					JSONObject jsonObject = new JSONObject(control);
					boolean kledNow = jsonObject.getBoolean("kled");
					boolean sledNow = jsonObject.getBoolean("sled");
					boolean aledNow = jsonObject.getBoolean("aled");
					boolean bledNow = jsonObject.getBoolean("bled");
					boolean winNow = jsonObject.getBoolean("win");
					boolean fanNow = jsonObject.getBoolean("fan");
					boolean sleepNow = jsonObject.getBoolean("sleep");
					boolean safeNow = jsonObject.getBoolean("safe");
					boolean homeNow = jsonObject.getBoolean("home");
					boolean smartNow=jsonObject.getBoolean("smart");
					if (homeNow) {
						sendPortData(ComA, "home");
					}
					if (safe !=  safeNow) {
//						sendToast("安防模式"); 
						safe = safeNow;
					}
					if (sleepNow) {
						sendPortData(ComA, "sleep");
					}
					if (kled !=  kledNow) {
						if (kledNow) {
//							sendToast("开客厅灯");
							sendPortData(ComA, "kledon");
						} else {
//							sendToast("关客厅灯");
							sendPortData(ComA, "kledoff");
						}
					}

					if (sled !=  sledNow) {
						if (sledNow) {
//							sendToast("开书房灯");
							sendPortData(ComA, "sledon");
						} else {
//							sendToast("关书房灯");
							sendPortData(ComA, "sledoff");
						}
					}
					if (aled !=  aledNow) {
						if (aledNow) {
//							sendToast("开卧室一灯");
							sendPortData(ComA, "aledon");
						} else {
							//sendToast("关卧室一灯");
							sendPortData(ComA, "aledoff");
						}
					}
					if (bled !=  bledNow) {
						if (bledNow) {
							//sendToast("开卧室二灯");
							sendPortData(ComA, "bledon");
						} else {
							//sendToast("关卧室二灯");
							sendPortData(ComA, "bledoff");
						}
					}
					if (winNow !=  wind) {

						if (winNow) {
							//sendToast("开窗帘");
							sendPortData(ComA, "winon");
						} else {
							//sendToast("关窗帘");
							sendPortData(ComA, "winoff");
						}
					}
					if (fan !=  fanNow) {
						if (fanNow) {
							//sendToast("开风扇");
							sendPortData(ComA, "windon");
						} else {
							//sendToast("关风扇");
							sendPortData(ComA, "windoff");
						}
					}
					if(smart!=smartNow){
						//sendToast("智能模式已切换");
						smart=smartNow;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					sendToast("数据解析失败");
				}

				break;
			case sendControlResultWhat:
				String result = message.getData().getString(
						sendControlResultKey);
				if (result.equals("safe")) {
					 safe = ! safe;
				}
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		DispQueue = new DispQueueThread();
		DispQueue.start();
		ComA = new SerialControl();
		AssistData = getAssistData();
		setControls();
		ComA.setPort("/dev/ttySAC0");
		ComA.setBaudRate(115200);
		OpenComPort(ComA);
		GetControlThread getControlThread = new GetControlThread();
		getControlThread.start();
	}

	public void sendMonitor(final String key, final String value) {
		new Thread(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				NetWork netWork = new NetWork();
				Map<String, String> map = new HashMap<String, String>();
				map.put("key", key);
				map.put("value", value);
				netWork.doPost(map, URLValues.monitorURL);
			}
		}).start();
	}

	public void sendControl(final String key, final boolean value) {
		new Thread(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				NetWork netWork = new NetWork();
				Map<String, String> map = new HashMap<String, String>();
				map.put("key", key);
				map.put("value", Boolean.toString(value));
				map.put("device", 1 + "");
				String result = netWork.doPost(map, URLValues.putControlResult);
				if (result == null)
					sendToast("发送失败");
				else
					sendMessage(sendControlResultWhat, sendControlResultKey,
							result);
			}
		}).start();
	}

	public class GetControlThread extends Thread {
		public void run() {
			while (flag) {
				try {
					NetWork netWork = new NetWork();
					Map<String, String> map = new HashMap<String, String>();
					map.put("device", 1 + "");
					String result = netWork.doGet(map, URLValues.getControlURL);
					if (result != null)
						sendMessage(getControlWhat, getControlKey, result);
					sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onDestroy() {
		saveAssistData(AssistData);
		CloseComPort(ComA);
		flag = false;
		super.onDestroy();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		CloseComPort(ComA);

		setContentView(R.layout.main);
		setControls();
	}

	// ----------------------------------------------------
	private void setControls() {

		editTexttem = (TextView) findViewById(R.id.edittexttem);
		editTextlight = (TextView) findViewById(R.id.edittextlight);
		editTexthum = (TextView) findViewById(R.id.edittexthum);
		editTextyanwu = (TextView) findViewById(R.id.edittextyanwu);

		fanOn = (Button) findViewById(R.id.fan_on);
		fanOff = (Button) findViewById(R.id.fan_off);
		windon = (Button) findViewById(R.id.win_on);
		windoff = (Button) findViewById(R.id.win_off);
		kledOn = (Button) findViewById(R.id.kled);
		kledOff = (Button) findViewById(R.id.kled_off);
		sledOn = (Button) findViewById(R.id.sled);
		sledOff = (Button) findViewById(R.id.sled_off);
		aledOn = (Button) findViewById(R.id.aled);
		aledOff = (Button) findViewById(R.id.aled_off);
		bledOn = (Button) findViewById(R.id.bled);
		bledOff = (Button) findViewById(R.id.bled_off);

		homeBT = (Button) findViewById(R.id.home);
		sleepBT = (Button) findViewById(R.id.sleep);
		safeOn = (Button) findViewById(R.id.safe_on);
		safeOff = (Button) findViewById(R.id.safe_off);
		smartOn=(Button)findViewById(R.id.smart_on);
		smartOff=(Button)findViewById(R.id.smart_off);
		smartOn.setOnClickListener(new ButtonClickEvent());
		smartOff.setOnClickListener(new ButtonClickEvent());
		fanOn.setOnClickListener(new ButtonClickEvent());
		fanOff.setOnClickListener(new ButtonClickEvent());
		windon.setOnClickListener(new ButtonClickEvent());
		windoff.setOnClickListener(new ButtonClickEvent());
		kledOn.setOnClickListener(new ButtonClickEvent());
		kledOff.setOnClickListener(new ButtonClickEvent());
		sledOn.setOnClickListener(new ButtonClickEvent());
		sledOff.setOnClickListener(new ButtonClickEvent());
		aledOn.setOnClickListener(new ButtonClickEvent());
		aledOff.setOnClickListener(new ButtonClickEvent());
		bledOn.setOnClickListener(new ButtonClickEvent());
		bledOff.setOnClickListener(new ButtonClickEvent());
		homeBT.setOnClickListener(new ButtonClickEvent());
		sleepBT.setOnClickListener(new ButtonClickEvent());
		safeOn.setOnClickListener(new ButtonClickEvent());
		safeOff.setOnClickListener(new ButtonClickEvent());
	}

	// ----------------------------------------------------发送按钮
	class ButtonClickEvent implements View.OnClickListener {
		public void onClick(View v) {
			AssistData.setTxtMode(true);
			switch (v.getId()) {
			case R.id.kled:
				sendPortData(ComA, "kledon");
				break;
			case R.id.kled_off:
				sendPortData(ComA, "kledoff");
				break;
			case R.id.fan_on:
				sendPortData(ComA, "windon");
				break;
			case R.id.fan_off:
				sendPortData(ComA, "windoff");
				break;
			case R.id.win_on:
				sendPortData(ComA, "winon");
				break;
			case R.id.win_off:
				sendPortData(ComA, "winoff");
				break;

			case R.id.sled:
				sendPortData(ComA, "sledon");
				break;
			case R.id.sled_off:
				sendPortData(ComA,"sledoff");
				break;
			case R.id.aled:
				sendPortData(ComA, "aledon");
				break;
			case R.id.aled_off:
				sendPortData(ComA,"aledoff");
				break;
			case R.id.bled:
				sendPortData(ComA, "bledon");
				break;
			case R.id.bled_off:
				sendPortData(ComA,"bledoff");
				break;
			case R.id.home:
				sendPortData(ComA, "home");
				break;
			case R.id.sleep:
				sendPortData(ComA, "sleep");
				break;
			case R.id.safe_on:
				 safe=true;
				sendControl("safe", true);
				break;
			case R.id.safe_off:
				 safe=false;
				sendControl("safe", false);
				break;
			case R.id.smart_off:
				 smart=false;
				sendControl("smart",false);
			break;
			case R.id.smart_on:
				 smart=true;
				sendControl("smart",true);
				break;
			}
		}
	}

	/*
	 * // ----------------------------------------------------打开关闭串口 class
	 * ToggleButtonCheckedChangeEvent implements
	 * ToggleButton.OnCheckedChangeListener { public void
	 * onCheckedChanged(CompoundButton buttonView, boolean isChecked) { if
	 * (buttonView == toggleButtonCOMA) { if (isChecked) {
	 * ComA.setPort("/dev/ttySAC0"); ComA.setBaudRate(115200);
	 * OpenComPort(ComA);
	 * 
	 * } else { CloseComPort(ComA);
	 * 
	 * } } } }
	 */

	// ----------------------------------------------------串口控制类
	private class SerialControl extends SerialHelper {

		public SerialControl() {
		}

		@Override
		protected void onDataReceived(final ComBean ComRecData) {

			DispQueue.AddQueue(ComRecData);// 线程定时刷新显示(推荐)
		}
	}

	// ----------------------------------------------------刷新显示线程
	private class DispQueueThread extends Thread {
		private Queue<ComBean> QueueList = new LinkedList<ComBean>();

		@Override
		public void run() {
			super.run();
			while (!isInterrupted()) {
				final ComBean ComData;
				while ((ComData = QueueList.poll()) != null) {
					runOnUiThread(new Runnable() {
						public void run() {
							DispRecData(ComData);
						}
					});
					try {

						Thread.sleep(1000);// 显示性能高的话，可以把此数值调小。
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				}
			}
		}

		public synchronized void AddQueue(ComBean ComData) {
			QueueList.add(ComData);
		}
	}

	// ----------------------------------------------------保存、获取界面数据
	private void saveAssistData(AssistBean AssistData) {
		SharedPreferences msharedPreferences = getSharedPreferences(
				"ComAssistant", Context.MODE_PRIVATE);
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(AssistData);
			String sBase64 = new String(Base64.encode(baos.toByteArray(), 0));
			SharedPreferences.Editor editor = msharedPreferences.edit();
			editor.putString("AssistData", sBase64);
			editor.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ----------------------------------------------------
	private AssistBean getAssistData() {
		SharedPreferences msharedPreferences = getSharedPreferences(
				"ComAssistant", Context.MODE_PRIVATE);
		AssistBean AssistData = new AssistBean();
		try {
			String personBase64 = msharedPreferences
					.getString("AssistData", "");
			byte[] base64Bytes = Base64.decode(personBase64.getBytes(), 0);
			ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			AssistData = (AssistBean) ois.readObject();
			return AssistData;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return AssistData;
	}

	// ----------------------------------------------------显示接收数据
	private void DispRecData(ComBean ComRecData) {
		String string = new String(ComRecData.bRec);
		sendMessage(comeDataResultWhat, comeDataResultKey, string);
	}

	public void sendMessage(int what, String key, String value) {
		Message message = new Message();
		message.what = what;
		Bundle bundle = new Bundle();
		bundle.putString(key, value);
		message.setData(bundle);
		handler.sendMessage(message);
	}

	public void sendToast(String toast) {
		sendMessage(sendToastWhat, sendToastKey, toast);
	}

	// ----------------------------------------------------串口发送
	private void sendPortData(final SerialHelper ComPort, final String sOut) {
		if (ComPort != null && ComPort.isOpen()) {
			new Thread(new Runnable() {

				public void run() {
					// TODO Auto-generated method stub
					ComPort.sendTxt(sOut);
				}
			}).start();

		}
	}

	// ----------------------------------------------------关闭串口
	private void CloseComPort(SerialHelper ComPort) {
		if (ComPort != null) {
			ComPort.stopSend();
			ComPort.close();
		}
	}

	// ----------------------------------------------------打开串口
	private void OpenComPort(SerialHelper ComPort) {
		try {
			ComPort.open();
		} catch (SecurityException e) {
			ShowMessage("打开串口失败:没有串口读/写权限!");
		} catch (IOException e) {
			ShowMessage("打开串口失败:未知错误!");
		} catch (InvalidParameterException e) {
			ShowMessage("打开串口失败:参数错误!");
		}
	}

	// ------------------------------------------显示消息
	private void ShowMessage(String sMsg) {
		Toast.makeText(this, sMsg, Toast.LENGTH_SHORT).show();
	}

}
