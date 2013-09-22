package com.example.ahwmediaplayernfc;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private MediaPlayer mp;
	
	private int[] musicas = new int[]{R.raw.musica_a, R.raw.musica_b, 
			R.raw.musica_c, R.raw.musica_d, R.raw.musica_e, R.raw.musica_f, 
			R.raw.musica_g, R.raw.musica_h, R.raw.musica_i, R.raw.musica_j,
			R.raw.musica_l, R.raw.musica_m, R.raw.musica_n, R.raw.musica_o};
	
	private TextView textos[] = new TextView[14];
	
	private int index;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		textos[0] = (TextView) findViewById(R.id.m1);
		textos[1] = (TextView) findViewById(R.id.m2);
		textos[2] = (TextView) findViewById(R.id.m3);
		textos[3] = (TextView) findViewById(R.id.m4);
		textos[4] = (TextView) findViewById(R.id.m5);
		textos[5] = (TextView) findViewById(R.id.m6);
		textos[6] = (TextView) findViewById(R.id.m7);
		textos[7] = (TextView) findViewById(R.id.m8);
		textos[8] = (TextView) findViewById(R.id.m9);
		textos[9] = (TextView) findViewById(R.id.m10);
		textos[10] = (TextView) findViewById(R.id.m11);
		textos[11] = (TextView) findViewById(R.id.m12);
		textos[12] = (TextView) findViewById(R.id.m13);
		textos[13] = (TextView) findViewById(R.id.m14);
		
		textos[0].setTextColor(Color.RED);
		
		mp = MediaPlayer.create(this, musicas[index]);
        mp.start();
	}
	
	@Override
	protected void onResume() {
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		
		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
	    try {
	        ndef.addDataType("*/*");
	    } catch (MalformedMimeTypeException e) {
	        throw new RuntimeException("fail", e);
	    }

	    IntentFilter tech = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
	    try {
	        tech.addDataType("*/*");
	    } catch (MalformedMimeTypeException e) {
	        throw new RuntimeException("fail", e);
	    }

	    IntentFilter[] intentFiltersArray = new IntentFilter[] { ndef, tech };
	    
	    String[][] techList = new String[][] { new String[] { NfcA.class.getName(),
	            NfcB.class.getName(), NfcF.class.getName(),
	            NfcV.class.getName(), IsoDep.class.getName(),
	            MifareClassic.class.getName(),
	            MifareUltralight.class.getName(), Ndef.class.getName() } };
	    
	    NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
	    nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techList);
		super.onResume();
	}
	
	public void mudaCorTexto(int index, int newIndex){
		textos[index].setTextColor(Color.BLACK);
		textos[newIndex].setTextColor(Color.RED);
	}
	
	@Override
	public void onNewIntent(Intent intent) {
	    String action = intent.getAction();

	    if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
	        // reag TagTechnology object...
	    	Toast.makeText(this, "TAG DISCOVERED", 8000).show();
	    	
	    } else if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
	        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			if (rawMsgs != null) {
				NdefMessage[] msgs = new NdefMessage[rawMsgs.length];
				for (int i = 0; i < rawMsgs.length; i++) {
					msgs[i] = (NdefMessage) rawMsgs[i];
					NdefRecord[] records = msgs[i].getRecords();

					//tv.setText(new String(records[0].getPayload()));

					if (new String(records[0].getPayload()).endsWith("0001")) {
						if (index > 0){
				        	mudaCorTexto(index, index  - 1);
					    	index--;
					    	mp.stop();
					    	mp = MediaPlayer.create(this, musicas[index]);
					        mp.start();
				        }
					} else {
						if (index < musicas.length - 1){
				        	mudaCorTexto(index, index + 1);
					    	index++;
					    	mp.stop();
					    	mp = MediaPlayer.create(this, musicas[index]);
					        mp.start();
				        }
					}
					
				}
			}
	    } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
	    	Toast.makeText(this, "TECH DISCOVERED", 8000).show();
	    }
	}
	
}
