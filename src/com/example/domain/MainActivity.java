package com.example.domain;

import android.app.Activity;
import android.content.Intent;

import java.util.Calendar;
import java.util.Random;

import android.os.Bundle;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;  

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {//安卓Activity界面的启动函数，在程序启动时就做预处理求得域上所有元素乘法和逆元
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		table = new int[256][256];//乘法表，8位2进制码的十进制形式，查找和存储乘法结果
		Nitable = new int[256];//逆表，同样以十进制形式查找和存储某元素的逆
		for (int i=0;i<256;i++)
			for (int j=0;j<256;j++){//求域上每两个元素的乘积：
				table[i][j] = 0;
				int[] ix = new int[8]; int p = i; for (int k=7;k>=0;k--) { ix[k] = p % 2; p = p / 2; }//乘数i变成二进制存在ix中
				int[] iy = new int[8]; p = j; for (int k=7;k>=0;k--) { iy[k] = p % 2; p = p / 2; }//乘数j变成二进制存在iy中
				int[] iz = new int[15];for (int ii=0;ii<15;ii++) iz[ii] = 0;//iz是ix与iy的乘积
				//-----------计算iz = ix * iy 有限域乘法，求和不进位---------------------
				for (int ii=0;ii<8;ii++){
					for (int jj=0;jj<8;jj++){
						iz[ii+jj] = (iz[ii+jj]+ix[ii]*iy[jj])%2;
				}}
				//-----------------------------------------------------------------------
				
				int[] gx = {1,0,0,0,1,1,0,1,1};//gx是模多项式
				
				//----------------对15位的iz取模，得到8位的结果iz[7...14]-----------------
				for (int ii=0;ii<=6;ii++){
					if (iz[ii]==1){
						for (int jj=0;jj<9;jj++){
							iz[ii+jj] = (iz[ii+jj] + gx[jj])%2;
				}}}
				//------------------------------------------------------------------------
				
				p = 1;
				for (int ii = 14;ii>=7;ii--){//将结果iz变为十进制，存在乘法表中
					table[i][j] += p*iz[ii];
					p = p *2;
				}
				if (table[i][j] == 1){ Nitable[i] = j; Nitable[j] = i; }//若ij乘积为1，则互为逆元，存在逆表中
			}
	}
	//-------------------------------------------------------------
	public void Add(View view){ //有限域加法：
		EditText editText1 =(EditText)findViewById(R.id.editText1);
		String s1=editText1.getText().toString(); //获取第一个被加数的String类型
		EditText editText2 =(EditText)findViewById(R.id.editText2);
		String s2=editText2.getText().toString(); //获取第二个被加数的String类型
		
		char[] ch1 = s1.toCharArray();//被加数1，转成CharArray类型
		char[] ch2 = s2.toCharArray();//被加数2，同上
		for (int i=0;i<8;i++){ ch1[i] = (char)(((ch1[i]-48)+(ch2[i]-48))%2+48);}//按位加，不进位
		
		EditText editText3 =(EditText)findViewById(R.id.editText3);
		editText3.setText(ch1, 0, 8);  //输出二进制结果
	}
	//--------------------------------------------------------------
	public void Cheng(View view){//有限域乘法：
		EditText editText1 =(EditText)findViewById(R.id.editText1);
		String s1=editText1.getText().toString(); 
		EditText editText2 =(EditText)findViewById(R.id.editText2);
		String s2=editText2.getText().toString();
		char[] chx = s1.toCharArray();
		char[] chy = s2.toCharArray();
		int t1 = 0,t2 = 0,p = 1;
		for (int i=7;i>=0;i--) { t1 += (chx[i]-48)*p; t2 += (chy[i]-48)*p; p *=2; }//求两个被乘数的十进制表示
		int w = table[t1][t2];//利用两个被乘数的十进制表示查表得到乘积的十进制表示
		for (int i = 7;i>=0;i--){//十进制转二进制数组
			chx[i] =(char)( w % 2+48);
			w = w / 2;
		}
		EditText editText3 =(EditText)findViewById(R.id.editText3);
		editText3.setText(chx, 0, 8);  //输出二进制结果
	}
	//-------------------------------------------------------------
	public void Ni(View view){//有限域求逆元：
		EditText editText1 =(EditText)findViewById(R.id.editText1);
		String s1=editText1.getText().toString(); 
		char[] chx = s1.toCharArray();
		int p = 0,t = 1;
		for (int i=7;i>=0;i--){//二进制数组转十进制数：
			p += t*(chx[i]-48);
			t *= 2;
		}
		p = Nitable[p];//查表得逆元
		for (int i = 7;i>=0;i--){//十进制转二进制数组
			chx[i] =(char)( p % 2+48);
			p = p / 2;
		}
		EditText editText3 =(EditText)findViewById(R.id.editText3);
		editText3.setText(chx, 0, 8);  //输出二进制结果
	}
	//-------------------------------------------------------------
	public void wanCheng(View view){//万次乘法算时间：
		long sum = 0,t1,t2;
		int[] chx = new int[8];
		int[] chy = new int[8];
		long ii = 0;
		Random rand = new Random(25);
		while (ii<100000){
			for (int i=0;i<8;i++){//随机生成两个乘法输入
				chx[i] = rand.nextInt(2);
				chy[i] = rand.nextInt(2);
			}
			//--------------------------
			t1 = System.currentTimeMillis();//标记时间1
			int tt1 = 0,tt2 = 0,p = 1;
			for (int i=7;i>=0;i--) { tt1 += chx[i]*p; tt2 += chy[i]*p; p *=2; }
			int w = table[tt1][tt2];
			for (int i = 7;i>=0;i--){
				chx[i] = w % 2;
				w = w / 2;
			}
			t2 = System.currentTimeMillis();//标记时间2
			sum += t2-t1;//t2-t1得到一次乘法的时间，累加到sum上
			//---------------------------
			ii++;
		}
		String ss = Long.toString(sum);
		char[] chc = ss.toCharArray();
		EditText editText3 =(EditText)findViewById(R.id.editText3);
		editText3.setText(chc, 0, chc.length); //输出sum(ms)
	}
	//--------------------------------------------------------------------
	public void wanNi(View view){
		long sum = 0,t1,t2;
		int[] chx = new int[8];
		long ii = 0;
		Random rand = new Random(25);
		while (ii<100000){
			for (int i=0;i<8;i++){//随机生成一个求逆输入
				chx[i] = rand.nextInt(2);
			}
			//-------------------------------
			t1 = System.currentTimeMillis();
			int tt1 = 0,p = 1;
			for (int i=7;i>=0;i--) { tt1 += chx[i]*p; p *=2; }
			int w = Nitable[tt1];
			for (int i = 7;i>=0;i--){
				chx[i] = w % 2;
				w = w / 2;
			}
			t2 = System.currentTimeMillis();
			sum += t2-t1;//累加时间
			//--------------------------------
			ii++;
		}
		String ss = Long.toString(sum);
		char[] chc = ss.toCharArray();
		EditText editText3 =(EditText)findViewById(R.id.editText3);
		editText3.setText(chc, 0, chc.length); 	//输出时间
	}
	//-------------------------------------------------------------------
	public void NewAct(View view){//调用求线性分析的Activity
		startActivity(new Intent(MainActivity.this, LinearActivity.class));  
	}
	public int table[][];
	public int Nitable[];
	
};


