package com.xjtu26.Gou.escapecard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class AuthorActivity extends Activity{

	private TextView mtext=null;
	private int count=0;
	private boolean isCheat=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_author);
		
		mtext=(TextView) findViewById(R.id.text_author);
		mtext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������				
				count=(count+1)%10;	
				if (count==0) {
					isCheat=true;
					Intent intent = new Intent(AuthorActivity.this, MainActivity.class);
					intent.putExtra("cheat", isCheat);
					Toast.makeText(AuthorActivity.this,  R.string.toast_cheat,   Toast.LENGTH_SHORT).show();
					startActivity(intent);
					AuthorActivity.this.finish();
				}else {
					isCheat=false;
				}
			}
			
		});
		mtext.append("  ����Ϸ�ɹ��³�����\n");	
		mtext.append("  ������ͨ��ѧ�Զ���\n");
		
	}

}
