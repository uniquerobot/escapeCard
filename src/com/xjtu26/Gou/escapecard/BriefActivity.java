package com.xjtu26.Gou.escapecard;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class BriefActivity extends Activity{

	private TextView mtext=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_brief);
		
		mtext=(TextView) findViewById(R.id.text_brief);
		mtext.append("  该游戏玩法类似红四，规则如下\n");	
		mtext.append("  1.普通牌：4-k为普通牌\n");
		mtext.append("  2.主牌：2、3为主牌，主牌大于普通牌，3最大\n");
		mtext.append("  3.花色：任何时候均不考虑花色\n");
		mtext.append("  4.单牌：一张任何牌，按牌面比较大小\n");
		mtext.append("  5.对牌：两张或三张一样的任何牌，规则如上条\n");
		mtext.append("  6.连牌：三张及以上数字相连的普通牌\n");
		mtext.append("  7.桌牌：每人有5堆，每堆有4张牌，只能看到顶牌\n");
		mtext.append("  8.手牌：每人4张牌\n");
		mtext.append("  9.胜利条件：出光所有的牌！\n");
		
	}

}
