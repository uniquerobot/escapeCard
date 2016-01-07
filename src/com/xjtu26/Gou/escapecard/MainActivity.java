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
 * ���ƣ������˿�
 * ���ߣ����³�
 * ���ڣ�2015.12.1
 * �汾v1.0��������ɽ��棬mvc�ֲ㣬����ģ�鲻����
 * 	  v1.1:����ʵ��AI�жϣ�����
 *    v1.2:����ʵ�ֵ�����Ϸ��AI����Ϊ������ֻ���Լ�����ʱ�����ơ�
 *    v1.3:���ӳɹ���ʾ���ܣ�������������ʾΪ����
 *    v1.4:����ʣ��������ʾUI����
 *    v1.5:�������win���ж����޶�2,3Ϊ������
 *    v1.6:���Ӳ˵�������淨���ܣ��������ߣ��Լ�������Ϸ�����һ�����֤�����ȶ���
 *    v1.7:������ť�����Ӱ�ѹЧ��
 *    v1.71:���ӳ��ƣ����ư�ť��ʾ����
 *    v1.8:�Ż�����
 *    v1.81:����AI�Զ�����ʱ�������ƣ����ƣ����Ƶ�������
 *    v1.82:��������ģʽ������toast��ʾ����
 *    v1.83:�Ľ�AI����ƵĹ��򣬷�ֹ������2��3
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

	// ����ȫ��ɾ��
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
		//����ģʽ
		System.out.println("���յ���isCheat="+isCheat);
		
		if (isCheat) {
			for (int i = 0; i < mAI_Hands.length; i++) {
				mAI_Hands[i].setImageDrawable(getResources().getDrawable((R.drawable.level_card)));
			}
		}
		
		// ��ʾ����
		update_All();

		// ���ü���
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
					check_select(i); // ����select
				}

				playCard.isOk(select, select.length); // �ı�size

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
			select[i] = lever; // ���ѡ���˴���
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
		update_played_desk(); // ��PlayedDesk�е���˳����ʾ���ѳ�����
		update_All(); // �������е���
		check_win();
	}

	private void update_played_desk() {
		ImageView imv = null;
		int size = playCard.getPlayedDesk().size();
		// ���played_Desk�������ʾ
		for (int i = 0; i < select.length; i++) {
			imv = mDesk[i];
			if (playCard.getPlayedDesk().isEmpty()) {
				imv.setVisibility(View.INVISIBLE);
			} else {
				imv.setVisibility(View.GONE);
			}
		}
		// ����played_Desk�������ʾ
		for (int i = 0; i < size; i++) {
			imv = mDesk[i];
			imv.setVisibility(View.VISIBLE);
			imv.getDrawable().setLevel(playCard.getPlayedDesk().get(i)); // ȡ��ѡ��״̬
		}
	}

	// �������е��ƣ���ı�level 
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

	// ����ĳһ�����
	private void update_Heap(int i, int type) {

		int tempHeapTopCard = 0;
		int size = 0;
		boolean isEmpty = false;
		ImageView imv = null;

		switch (type) {
		case 1:

			tempHeapTopCard = playCard.getPlayerDesk().get(i).getTopCard();
			size = playCard.getPlayerDesk().get(i).getSize(); // size������ʾ�����м���ͼ
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
			size = playCard.getAIDesk().get(i).getSize(); // size������ʾ�����м���ͼ
			isEmpty = playCard.isEmpty(playCard.getAIDesk());
			break;
		case 4:

			tempHeapTopCard = playCard.getAIHands().get(i).getTopCard();
			imv = mAI_Hands[i];
			isEmpty = playCard.isEmpty(playCard.getAIHands());
			break;
		default:

			Log.d(TAG, "update_Heap(int i, int type)type���ͳ���" + "i " + i + "type " + type);
			break;
		}
		if (tempHeapTopCard == 0) { // û�ƾ�ɾ��

			if (isEmpty) {
				imv.setVisibility(View.INVISIBLE); // ���һ������ɾҪռס�ռ�
			} else {
				imv.setVisibility(View.GONE); // ֻҪ�������һ�ž�ɾ��������֤��ͼ�Գ�
			}
			imv.getDrawable().setLevel(0); // ��ո�ֵ
		} else { // ������ʾ����

			imv.getDrawable().setLevel(tempHeapTopCard);
			if (type == 1 || type == 3) { // ������ʾ���м���
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
