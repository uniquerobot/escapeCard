package com.xjtu26.Gou.escapecard;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class BriefActivity extends Activity{

	private TextView mtext=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_brief);
		
		mtext=(TextView) findViewById(R.id.text_brief);
		mtext.append("  ����Ϸ�淨���ƺ��ģ���������\n");	
		mtext.append("  1.��ͨ�ƣ�4-kΪ��ͨ��\n");
		mtext.append("  2.���ƣ�2��3Ϊ���ƣ����ƴ�����ͨ�ƣ�3���\n");
		mtext.append("  3.��ɫ���κ�ʱ��������ǻ�ɫ\n");
		mtext.append("  4.���ƣ�һ���κ��ƣ�������Ƚϴ�С\n");
		mtext.append("  5.���ƣ����Ż�����һ�����κ��ƣ�����������\n");
		mtext.append("  6.���ƣ����ż�����������������ͨ��\n");
		mtext.append("  7.���ƣ�ÿ����5�ѣ�ÿ����4���ƣ�ֻ�ܿ�������\n");
		mtext.append("  8.���ƣ�ÿ��4����\n");
		mtext.append("  9.ʤ���������������е��ƣ�\n");
		
	}

}
