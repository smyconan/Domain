package com.example.domain;

public class Domainclass {
	public Domainclass(){
		val = new int[8];
	}
	public void init(char[] ch){
		for (int i=0;i<8;i++){
			val[i] = ch[i] - '0';
		}
	}
	public void Dadd(Domainclass t){
		for (int i=0;i<8;i++){
			val[i] = (val[i]+t.val[i])%2;
		}
	}
	public void Dminu(Domainclass t){	
	}
	public char[] Out(){
		char[] ch;
		ch = new char[8];
		for (int i=0;i<8;i++){
			ch[i] = (char) (val[i]+48);
		}
		return ch;
	}
	public int[] val;
}