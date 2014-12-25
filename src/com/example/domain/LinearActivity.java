package com.example.domain;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class LinearActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_linear);
		EditText et[] ={(EditText)findViewById(R.id.et1),
				        (EditText)findViewById(R.id.et2),
				        (EditText)findViewById(R.id.et3),
				        (EditText)findViewById(R.id.et4),
				        (EditText)findViewById(R.id.et5),
				        (EditText)findViewById(R.id.et6),
				        (EditText)findViewById(R.id.et7),
				        (EditText)findViewById(R.id.et8),
				        (EditText)findViewById(R.id.et9),
				        (EditText)findViewById(R.id.et10)};//前十个方程所要输出到的十个文本框控件
		int sbox[] = {99,124,119,123,242,107,111,197,48,1,103,43,254,215,171,118,
				      202,130,201,125,250,89,71,240,173,212,162,175,156,164,114,192,
				      183,253,147,38,54,63,247,204,52,165,229,241,113,216,49,21,
				      4,199,35,195,24,150,5,154,7,18,128,226,235,39,178,117,
				      9,131,44,26,27,110,90,160,82,59,214,179,41,227,47,132,
				      83,209,0,237,32,252,177,91,106,203,190,57,74,76,88,207,
				      208,239,170,251,67,77,51,133,69,249,2,127,80,60,159,168,
				      81,163,64,143,146,157,56,245,188,182,218,33,16,255,243,210,
				      205,12,19,236,95,151,68,23,196,167,126,61,100,93,25,115,
				      96,129,79,220,34,42,144,136,70,238,184,20,222,94,11,219,
				      224,50,58,10,73,6,36,92,194,211,172,98,145,149,228,121,
				      231,200,55,109,141,213,78,169,108,86,244,234,101,122,174,8,
				      186,120,37,46,28,166,180,198,232,221,116,31,75,189,139,138,
				      112,62,181,102,72,3,246,14,97,53,87,185,134,193,29,158,
				      225,248,152,17,105,217,142,148,155,30,135,233,206,85,40,223,
				      140,161,137,13,191,230,66,104,65,153,45,15,176,84,187,22};//输入和输出的对应关系，输入i，输出sbox[i]，都是十进制
		int num[] = new int[65536];//统计每个方程成立的次数(用十进制表示方程)
	    for (int i = 0; i<65536; i++) { num[i] = 0;}
		for (int ii=0;ii<256;ii++){//枚举输入
			int j,i;
			for (int k = 1;k<=65535;k++){//枚举方程
				int sum = 0,p = k,l = 0;
				j = sbox[ii]; //由输入得输出(查表定)
				i = ii;
				//----------利用输入输出判断方程是否成立-------------
				while (l < 8){
					l++;
					sum  = sum ^ ((p%2)*(j%2));
					p = p / 2;
					j = j / 2;
				}	
				while (l<16) {
					l++;
					sum = sum ^ ((p%2)*(i%2));
					p = p / 2;
					i = i / 2;
				}
				//---------------------------------------------------
				
				if ( sum== 0) { num[k]++; }//若方程成立，则成立次数加一
			}
		}
		
		double route[] = new double[65536];//route表示方程的成离率
		for (int i = 1;i<=65535; i++) { route[i] = Math.abs(num[i]/256.0-0.5); num[i] = i; }//计算成离率
		
		//--------------排序求前十个方程的十进制表示------------------------
		int l = 0;
		while (l<10){
			l++;
			for (int i = 65535; i>1; i--){
				if ( route[i] > route [i-1]){
					double temp = route[i]; int temp2 = num[i];
					route[i] = route[i-1]; num[i] = num[i-1];
					route[i-1] = temp; num[i-1] = temp2;
				}
			}
		}
		//-------------------------------------------------------------------
		
		//------------将十进制数转化成可视化的方程式输出---------------------
		char chc[] = new char[16];
		char cht[] = new char[49];
		for (int i=1; i<=10; i++){
			for (int j = 15; j>=0; j--){
				chc[j] = (char)(num[i] % 2+48);
				num[i] = num[i] / 2;
			}
			int len = 0;
			for (int j = 0; j<16; j++){
				if (chc[j] == '1'){ 
					len+=3;
				    cht[len-2] = (j < 8)? 'X':'Y';
				    cht[len-1] = (j < 8)? (char)(j+48):(char)(j-8+48);
				    cht[len] = '^';
				}
			}
			cht[len]= '=';
			len++;
			cht[len]= '0';
			et[i-1].setText(cht, 0, len+1);
		}
		//--------------------------------------------------------------------
		
	}
}

