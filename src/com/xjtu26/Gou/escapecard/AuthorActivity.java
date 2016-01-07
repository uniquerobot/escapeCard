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
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_author);
		
		mtext=(TextView) findViewById(R.id.text_author);
		mtext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根				
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
		mtext.append("  本游戏由苟新超开发\n");	
		mtext.append("  西安交通大学自动化\n");
		
	}

}
