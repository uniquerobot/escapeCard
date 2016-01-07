package com.xjtu26.Gou.escapecard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MenuActivity extends Activity{

	private Button mbutton_single=null;
	private Button mbutton_double=null;
	private Button mbutton_brief=null;
	private Button mbutton_author=null;
	
	private static final String TAG = "EscapeCard";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		mbutton_single=(Button) findViewById(R.id.button_single);
		mbutton_single.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//button_reverse(mbutton_single);
				Intent i=new Intent(MenuActivity.this, MainActivity.class);
				startActivity(i);
			}
		});
		
		mbutton_double=(Button) findViewById(R.id.button_double);
		mbutton_double.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				Toast.makeText(MenuActivity.this,  "Sorry!作者正在废寝忘食地加班XDDD",   Toast.LENGTH_SHORT).show();

			}
		});
		
		mbutton_brief=(Button) findViewById(R.id.button_brief);
		mbutton_brief.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//button_reverse(mbutton_brief);
				Intent i=new Intent(MenuActivity.this, BriefActivity.class);
				startActivity(i);
			}
		});
		
		mbutton_author=(Button) findViewById(R.id.button_author);
		mbutton_author.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//button_reverse(mbutton_author);
				Intent i=new Intent(MenuActivity.this, AuthorActivity.class);
				startActivity(i);			
			}
		});
	}

}
