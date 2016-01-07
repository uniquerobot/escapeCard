package com.xjtu26.Gou.escapecard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/*
 * 名称：逃亡扑克
 * 作者：苟新超
 * 日期：2015.12.1
 * 版本v1.0：基本完成界面，mvc分层，控制模块不完善
 * 	  v1.1:基本实现AI判断，出牌
 *    v1.2:基本实现单人游戏，AI还较为初级，只会自己出牌时出单牌。
 *    v1.3:增加成功提示功能，将对手手牌显示为背面
 *    v1.4:增加剩余牌数显示UI界面
 *    v1.5:修正最后win的判定，修订2,3为最大的牌
 *    v1.6:增加菜单并完成玩法介绍，关于作者，以及单人游戏，并且基本保证界面稳定。
 *    v1.7:美化按钮，增加按压效果
 *    v1.71:增加出牌，吃牌按钮提示功能
 *    v1.8:优化代码
 *    v1.81:增加AI自动出牌时，出连牌，对牌，单牌的能力。
 *    v1.82:增加作弊模式，增加toast提示功能
 *    v1.83:改进AI打对牌的规则，防止随便出对2对3
 *    
 */

class MyOnClickListener implements OnClickListener {
	int i = 0;

	public MyOnClickListener(int i) {
		this.i = i;
	}

	@Override
	public void onClick(View arg0) {
	}
}

public class MainActivity extends ActionBarActivity {

	private ImageView[] mAI_Desk = new ImageView[5];
	private ImageView[] mAI_Hands = new ImageView[4];
	private ImageView[] mPL_Desk = new ImageView[5];
	private ImageView[] mPL_Hands = new ImageView[4];
	private ImageView[] mDesk = new ImageView[9];

	private Button mbutton_play = null;
	private Button mbutton_pass = null;

	int[] select = new int[9];

	PlayCard playCard = new PlayCard();

	// 后期全部删除
	private static final String TAG = "EscapeCard";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mAI_Desk[0] = (ImageView) findViewById(R.id.AI_desk_1);
		mAI_Desk[1] = (ImageView) findViewById(R.id.AI_desk_2);
		mAI_Desk[2] = (ImageView) findViewById(R.id.AI_desk_3);
		mAI_Desk[3] = (ImageView) findViewById(R.id.AI_desk_4);
		mAI_Desk[4] = (ImageView) findViewById(R.id.AI_desk_5);
		mAI_Hands[0] = (ImageView) findViewById(R.id.AI_hand_1);
		mAI_Hands[1] = (ImageView) findViewById(R.id.AI_hand_2);
		mAI_Hands[2] = (ImageView) findViewById(R.id.AI_hand_3);
		mAI_Hands[3] = (ImageView) findViewById(R.id.AI_hand_4);

		mPL_Desk[0] = (ImageView) findViewById(R.id.PL_desk_1);
		mPL_Desk[1] = (ImageView) findViewById(R.id.PL_desk_2);
		mPL_Desk[2] = (ImageView) findViewById(R.id.PL_desk_3);
		mPL_Desk[3] = (ImageView) findViewById(R.id.PL_desk_4);
		mPL_Desk[4] = (ImageView) findViewById(R.id.PL_desk_5);
		mPL_Hands[0] = (ImageView) findViewById(R.id.PL_hand_1);
		mPL_Hands[1] = (ImageView) findViewById(R.id.PL_hand_2);
		mPL_Hands[2] = (ImageView) findViewById(R.id.PL_hand_3);
		mPL_Hands[3] = (ImageView) findViewById(R.id.PL_hand_4);

		mDesk[0] = (ImageView) findViewById(R.id.desk_1);
		mDesk[1] = (ImageView) findViewById(R.id.desk_2);
		mDesk[2] = (ImageView) findViewById(R.id.desk_3);
		mDesk[3] = (ImageView) findViewById(R.id.desk_4);
		mDesk[4] = (ImageView) findViewById(R.id.desk_5);
		mDesk[5] = (ImageView) findViewById(R.id.desk_6);
		mDesk[6] = (ImageView) findViewById(R.id.desk_7);
		mDesk[7] = (ImageView) findViewById(R.id.desk_8);
		mDesk[8] = (ImageView) findViewById(R.id.desk_9);

		Intent intent=this.getIntent();
		boolean isCheat=intent.getBooleanExtra("cheat", false);
		//作弊模式
		System.out.println("我收到的isCheat="+isCheat);
		
		if (isCheat) {
			for (int i = 0; i < mAI_Hands.length; i++) {
				mAI_Hands[i].setImageDrawable(getResources().getDrawable((R.drawable.level_card)));
			}
		}
		
		// 显示牌面
		update_All();

		// 设置监听
		for (int i = 0; i < 5; i++) {
			mPL_Desk[i].setOnClickListener(new MyOnClickListener(i) {

				@Override
				public void onClick(View arg0) {
					reverse(mPL_Desk[i]);

				}
			});
			if (i < 4) {
				mPL_Hands[i].setOnClickListener(new MyOnClickListener(i) {

					@Override
					public void onClick(View arg0) {
						reverse(mPL_Hands[i]);
					}
				});
			}
		}

		mbutton_play = (Button) findViewById(R.id.button_play);
		mbutton_play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO
				for (int i = 0; i < select.length; i++) {
					select[i] = 0; // clear_select
					check_select(i); // 产生select
				}

				playCard.isOk(select, select.length); // 改变size

				if (playCard.isEat()) {
					mbutton_play.setText(R.string.button_eat);
				} else {
					mbutton_play.setText(R.string.button_play);
				}

				update();
			}
		});
		mbutton_pass = (Button) findViewById(R.id.button_pass);
		mbutton_pass.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				playCard.AIPlayCard();
				mbutton_play.setText(R.string.button_eat);
				update();
			}
		});
	}

	private void check_select(int i) {
		ImageView imv = null;
		if (i < 5) {
			imv = mPL_Desk[i];
		} else {
			imv = mPL_Hands[i - 5];
		}

		int lever = imv.getDrawable().getLevel();
		if (lever > 100) {
			select[i] = lever; // 如果选择了此牌
		} else {
			select[i] = 0;
		}
	}

	private void check_win() {
		boolean isWin = true;
		for (int i = 0; i < 5; i++) {
			if (playCard.getPlayerDesk().get(i).getTopCard() != 0) {
				isWin = false;
				break;
			}
			if (i < 4) {
				if (playCard.getPlayerHands().get(i).getTopCard() != 0) {
					isWin = false;
					break;
				}
			}
		}
		if (isWin) {
			Toast.makeText(MainActivity.this, R.string.toast_win, Toast.LENGTH_SHORT).show();
			Intent i = new Intent(MainActivity.this, MainActivity.class);
			startActivity(i);
			this.finish();
			return;
		}

		isWin = true;
		for (int i = 0; i < 5; i++) {
			if (playCard.getAIDesk().get(i).getTopCard() != 0) {
				isWin = false;
				break;
			}
			if (i < 4) {
				if (playCard.getAIHands().get(i).getTopCard() != 0) {
					isWin = false;
					break;
				}
			}
		}
		if (isWin) {
			Toast.makeText(MainActivity.this, R.string.toast_lose, Toast.LENGTH_SHORT).show();
			Log.d(TAG, "lose");
			Intent i = new Intent(MainActivity.this, MainActivity.class);
			startActivity(i);
			this.finish();
		}
	}
	

	private void update() {
		update_played_desk(); // 将PlayedDesk中的牌顺序显示在已出牌中
		update_All(); // 更新所有的牌
		check_win();
	}

	private void update_played_desk() {
		ImageView imv = null;
		int size = playCard.getPlayedDesk().size();
		// 清空played_Desk桌面的显示
		for (int i = 0; i < select.length; i++) {
			imv = mDesk[i];
			if (playCard.getPlayedDesk().isEmpty()) {
				imv.setVisibility(View.INVISIBLE);
			} else {
				imv.setVisibility(View.GONE);
			}
		}
		// 更新played_Desk桌面的显示
		for (int i = 0; i < size; i++) {
			imv = mDesk[i];
			imv.setVisibility(View.VISIBLE);
			imv.getDrawable().setLevel(playCard.getPlayedDesk().get(i)); // 取消选择状态
		}
	}

	// 更新所有的牌，会改变level 
	private void update_All() {
		for (int i = 0; i < 5; i++) {
			update_Heap(i, 1);
			update_Heap(i, 3);
			if (i < 4) {
				update_Heap(i, 2);
				update_Heap(i, 4);
			}
		}
	}

	// 更新某一类的牌
	private void update_Heap(int i, int type) {

		int tempHeapTopCard = 0;
		int size = 0;
		boolean isEmpty = false;
		ImageView imv = null;

		switch (type) {
		case 1:

			tempHeapTopCard = playCard.getPlayerDesk().get(i).getTopCard();
			size = playCard.getPlayerDesk().get(i).getSize(); // size用来显示背景有几张图
			imv = mPL_Desk[i];
			isEmpty = playCard.isEmpty(playCard.getPlayerDesk());
			break;
		case 2:

			tempHeapTopCard = playCard.getPlayerHands().get(i).getTopCard();
			imv = mPL_Hands[i];
			isEmpty = playCard.isEmpty(playCard.getPlayerHands());
			break;
		case 3:

			tempHeapTopCard = playCard.getAIDesk().get(i).getTopCard();
			imv = mAI_Desk[i];
			size = playCard.getAIDesk().get(i).getSize(); // size用来显示背景有几张图
			isEmpty = playCard.isEmpty(playCard.getAIDesk());
			break;
		case 4:

			tempHeapTopCard = playCard.getAIHands().get(i).getTopCard();
			imv = mAI_Hands[i];
			isEmpty = playCard.isEmpty(playCard.getAIHands());
			break;
		default:

			Log.d(TAG, "update_Heap(int i, int type)type类型出错" + "i " + i + "type " + type);
			break;
		}
		if (tempHeapTopCard == 0) { // 没牌就删掉

			if (isEmpty) {
				imv.setVisibility(View.INVISIBLE); // 最后一个不能删要占住空间
			} else {
				imv.setVisibility(View.GONE); // 只要不是最后一张就删掉它，保证视图对称
			}
			imv.getDrawable().setLevel(0); // 清空赋值
		} else { // 有牌显示牌面

			imv.getDrawable().setLevel(tempHeapTopCard);
			if (type == 1 || type == 3) { // 桌牌显示还有几张
				imv.setBackground(getResources().getDrawable(R.drawable.level_back));
				imv.getBackground().setLevel(size);
			}
		}
	}

	private void reverse(ImageView imv) {
		int level = imv.getDrawable().getLevel();
		if (level < 100) {
			level = level + 100;
		} else {
			level = level - 100;
		}
		imv.getDrawable().setLevel(level);
	}


	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
