package com.dfrobot.angelo.blunobasicdemo;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.content.Intent;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import mehdi.sakout.fancybuttons.FancyButton;

public class MainActivity extends BlunoLibrary implements TextToSpeech.OnInitListener {
//	private Button buttonScan;
//	private Button buttonSerialSend;
//	private EditText serialSendText;
//	private TextView serialReceivedText;

	private FancyButton btnConnectDisconnect;
	private FancyButton BtnSilence;
	TextToSpeech TTS;

	// key numbers for save/load global data
	final static String KEY_SILENCE = "1";

	@Override
	public void onInit(int status) {
		Log.d("MainActivity","OnInit");
		if (status == TextToSpeech.SUCCESS) {
			Log.d("MainActivity","TTS setLanguage");
			TTS.setLanguage(Locale.KOREAN);

			if (!GlobalData.getSilence() && !TTS.isSpeaking())
				TTS.speak("화면 상단을 눌러 블루투스 연결을 시작하십시오. 화면 하단을 눌러 소리를 끄고 켤 수 있습니다."
						, TextToSpeech.QUEUE_ADD, null);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        onCreateProcess();														//onCreate Process by BlunoLibrary

		TTS = new TextToSpeech(this,this);
		TTS.setLanguage(Locale.KOREAN);
		Log.d("MainActivity","TTS created");

		// global data 불러오기
		if (savedInstanceState != null) {
			Log.d("data", "loading...");
			GlobalData.setStringSilence(savedInstanceState.getString(KEY_SILENCE));
		}
		//GlobalData.setStringSilence(getPreferences(Context.MODE_PRIVATE).getString(KEY_SILENCE,null));
		Log.d("MainActivity", "Silence: " + GlobalData.getStringSilence());

		Log.d("MainActivity", isExternalStorageWritable() + "");
		Log.d("MainActivity", isExternalStorageReadable() + "");

        serialBegin(115200);													//set the Uart Baudrate on BLE chip to 115200

		btnConnectDisconnect=(FancyButton) findViewById(R.id.btn_select);
		btnConnectDisconnect.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!GlobalData.getSilence() && !TTS.isSpeaking())
					TTS.speak("블루투스 연결을 시작합니다.", TextToSpeech.QUEUE_ADD, null);
				buttonScanOnClickProcess();
			}
		});

		BtnSilence = (FancyButton)findViewById(R.id.btn_silence);
		BtnSilence.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				GlobalData.switchSilence();
				if(GlobalData.getSilence()){
					//무음
					BtnSilence.setIconResource("\uf028");
					BtnSilence.setBackgroundColor(Color.parseColor("#af0029"));
					BtnSilence.setFocusBackgroundColor(Color.parseColor("#d6112f"));

				}else{
					if (!TTS.isSpeaking()) TTS.speak("음성을 활성화합니다.", TextToSpeech.QUEUE_ADD, null);
					BtnSilence.setIconResource("\uf026");
					BtnSilence.setBackgroundColor(Color.parseColor("#0093b8"));
					BtnSilence.setFocusBackgroundColor(Color.parseColor("#02aed9"));
				}
				Log.d("MainActivity", "Silence: "+ GlobalData.getStringSilence());
			}
		});

//        serialReceivedText=(TextView) findViewById(R.id.serialReveicedText);	//initial the EditText of the received data
//        serialSendText=(EditText) findViewById(R.id.serialSendText);			//initial the EditText of the sending data
//
//        buttonSerialSend = (Button) findViewById(R.id.buttonSerialSend);		//initial the button for sending the data
//        buttonSerialSend.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//
//				serialSend(serialSendText.getText().toString());				//send the data to the BLUNO
//			}
//		});
//
//        buttonScan = (Button) findViewById(R.id.buttonScan);					//initial the button for scanning the BLE device
//        buttonScan.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//
//				buttonScanOnClickProcess();										//Alert Dialog for selecting the BLE device
//			}
//		});
	}

	protected void onResume(){
		super.onResume();
		System.out.println("BlUNOActivity onResume");
		onResumeProcess();														//onResume Process by BlunoLibrary

	}

	public void callTTS() {
		if (!(GlobalData.getSilence())) {
			new Thread() {
				public void run() {
					try {
						sleep(1500); // 즉시 실행 시 onInit이 실행되기 전에 TTS 호출 -> 음성안내가 이루어지지 않음
						TTS.speak("블루투스 승인을 요청합니다. 블루투스 연결을 활성화하려면 오른쪽 중앙의 예 버튼을 클릭하세요."
								, TextToSpeech.QUEUE_ADD, null);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}.start();
		}
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		onActivityResultProcess(requestCode, resultCode, data);					//onActivityResult Process by BlunoLibrary
		super.onActivityResult(requestCode, resultCode, data);
	}
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        onPauseProcess();														//onPause Process by BlunoLibrary
//    }
//
//	protected void onStop() {
//		super.onStop();
//		onStopProcess();														//onStop Process by BlunoLibrary
//	}
//

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		Log.d("data", "saving...");
		outState.putString(KEY_SILENCE, GlobalData.getStringSilence());
	}


	@Override
	protected void onStop() {
		super.onStop();

		SharedPreferences.Editor SPEditor = getPreferences(Context.MODE_PRIVATE).edit();

		SPEditor.putString(KEY_SILENCE, GlobalData.getStringSilence());

		SPEditor.commit();
	}




	@Override
    protected void onDestroy() {
        super.onDestroy();	
        onDestroyProcess();														//onDestroy Process by BlunoLibrary
		TTS.shutdown();
    }

	@Override
	public void onConectionStateChange(connectionStateEnum theConnectionState) {//Once connection state changes, this function will be called
		switch (theConnectionState) {											//Four connection state
		case isConnected:
			btnConnectDisconnect.setText("Disconnect");
			startActivity(new Intent(MainActivity.this, FileListActivity.class));

//			buttonScan.setText("Connected");
			break;
		case isConnecting:
			btnConnectDisconnect.setText("Connecting");

//			buttonScan.setText("Connecting");
			break;
		case isToScan:
			btnConnectDisconnect.setText("Connect");

//			buttonScan.setText("Scan");
			break;
		case isScanning:
			btnConnectDisconnect.setText("Scanning");

//			buttonScan.setText("Scanning");
			break;
		case isDisconnecting:
			btnConnectDisconnect.setText("XXX");
//			buttonScan.setText("isDisconnecting");
			break;
		default:
			break;
		}
	}

	@Override
	public void onSerialReceived(String theString) {							//Once connection data received, this function will be called
		// TODO Auto-generated method stub

		Log.d("asd", "received : "+theString);
		if(theString.length()>1) return;

		FileReadActivity.setFilePointer(Integer.parseInt(theString)-1);

//		serialReceivedText.append(theString);							//append the text into the EditText
//		//The Serial data from the BLUNO may be sub-packaged, so using a buffer to hold the String is a good choice.
//		((ScrollView)serialReceivedText.getParent()).fullScroll(View.FOCUS_DOWN);
	}


	/* Checks if external storage is available for read and write */
	boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	/* Checks if external storage is available to at least read */
	boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state) ||
				Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}


}