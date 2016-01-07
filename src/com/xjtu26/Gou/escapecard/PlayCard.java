package com.xjtu26.Gou.escapecard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import android.util.Log;

public class PlayCard {

	public PlayCard() {
		shuffle();
		deal();
	}

	// ����ȫ��ɾ��
	private static final String TAG = "EscapeCard";

	private int[] Cards = new int[48];
	private Vector<HeapCard> AIHands = new Vector<HeapCard>();
	private Vector<HeapCard> AIDesk = new Vector<HeapCard>();
	private Vector<HeapCard> PlayerHands = new Vector<HeapCard>();
	private Vector<HeapCard> PlayerDesk = new Vector<HeapCard>();
	private Vector<Integer> SelectCards = new Vector<Integer>();
	private Vector<Integer> PlayedDesk = new Vector<Integer>();
	private Vector<Integer> AICards = new Vector<Integer>();
	private Vector<Integer> PlayerCards = new Vector<Integer>();
	private Vector<Integer> AISelect = new Vector<Integer>();
	private Vector<Integer> TempPlayedDesk = new Vector<Integer>();
	private int Type_AISelect = 0;
	private boolean Eat = false;

	public boolean isEat() {
		return Eat;
	}

	public Vector<HeapCard> getPlayerHands() {
		return PlayerHands;
	}

	public Vector<HeapCard> getPlayerDesk() {
		return PlayerDesk;
	}

	public Vector<Integer> getPlayedDesk() {
		return PlayedDesk;
	}

	public Vector<HeapCard> getAIDesk() {
		return AIDesk;
	}

	public Vector<HeapCard> getAIHands() {
		return AIHands;
	}

	// ϴ��
	private void shuffle() {
		for (int i = 0; i < 12; i++) {
			Cards[i] = i + 2;
			Cards[i + 12] = i + 15;
			Cards[i + 24] = i + 28;
			Cards[i + 36] = i + 41;
		}
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < Cards.length; i++) {
			list.add(Cards[i]);
		}
		// ����
		Collections.shuffle(list);
		Iterator<Integer> ite = list.iterator();

		int tempIndex = 0;
		while (ite.hasNext()) {
			Cards[tempIndex] = ite.next();
			tempIndex++;
		}
	}

	// ����
	private void deal() {
		// ������
		HeapCard singleHeap = null;
		for (int i = 0; i < 4; i++) {
			singleHeap = new HeapCard(Cards[i]);
			AIHands.add(singleHeap);
			singleHeap = new HeapCard(Cards[i + 4]);
			PlayerHands.add(singleHeap);
		}
		// ���������
		HeapCard tempHeap = null;
		int tempCards[] = new int[4];
		int tempCards2[] = new int[4];
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 4; j++) {
				tempCards[j] = Cards[8 + 4 * i + j];
				tempCards2[j] = Cards[28 + 4 * i + j];
			}
			tempHeap = new HeapCard(tempCards);
			AIDesk.add(tempHeap);
			tempHeap = new HeapCard(tempCards2);
			PlayerDesk.add(tempHeap);
		}
	}

	public boolean isEmpty(Vector<HeapCard> Cards) {
		// ĳ�����е��ƶ������򷵻�true�������Լ����е����ƶ����꣩
		boolean isEmpty = true;
		for (int i = 0; i < Cards.size(); i++) {
			if (Cards.get(i).getSize() > 0) {
				isEmpty = false;
				break;
			}
		}
		return isEmpty;
	}

	private int getValue(int Card) {
		int Value = 0;
		// ������
		if (Card != 0) {
			// �õ�Card����������
			Value = Card % 13;
			if (Value == 0) {
				Value = 13;
			}
			if (Value == 2 || Value == 3) { // �ı�2,3��weight
				Value = Value + 15; // �������Ƶ�Ȩ�ش�
			}
		}
		return Value;
	}

	// ����
	private void sortCards(Vector<Integer> Cards) {
		int size = Cards.size();
		int tempSeries[] = new int[size];

		Comparator<Integer> comp = Collections.reverseOrder();
		Collections.sort(Cards, comp);
		Enumeration<Integer> e = Cards.elements();
		while (e.hasMoreElements()) {
			tempSeries[size - 1] = (Integer) e.nextElement();
			size--;
		}
		// д��sendCards
		size = Cards.size();
		for (int i = 0; i < size; i++) {
			Cards.set(i, tempSeries[i]);
		}
	}

	// �õ�ѡ����Ƶ������С
	private void getSelectValueSort(int[] select, int len) {
		// ���������
		SelectCards.clear();
		TempPlayedDesk.clear();
		// ��ֵ
		for (int i = 0; i < len; i++) {
			if (select[i] > 0) { // ��ֵ��ת��level��������ѡ��������
				TempPlayedDesk.add((Integer) select[i] - 100);
				SelectCards.add((Integer) getValue(select[i] - 100));
			}
		}
		// ����SelectCards
		sortCards(SelectCards);
		// ����PlayedDesk
		int size = SelectCards.size();
		Vector<Integer> tempDesk = (Vector<Integer>) TempPlayedDesk.clone();
		for (int i = 0; i < size; i++) {
			int temp_card = SelectCards.get(i);
			for (int j = 0; j < size; j++) {
				if (getValue(tempDesk.get(j)) == temp_card) { // ���selectΪweight����Ҳ��Ҫ��getweight
					TempPlayedDesk.set(i, tempDesk.get(j));
					tempDesk.set(j, 0);
					break;
				}
			}
		}
	}

	private void update_HeapSize(int[] select, int len) {
		for (int i = 0; i < len; i++) {
			if (select[i] > 0) {
				if (i < 5) {
					PlayerDesk.get(i).setSize(PlayerDesk.get(i).getSize() - 1);
				} else {
					PlayerHands.get(i - 5).setSize(0);
				}
			}
		}
	}

	private void update_AI_HeapSize(int j) {
		if (j < 5) {
			AIDesk.get(j).setSize(AIDesk.get(j).getSize() - 1);
		} else if (j < 9) {
			AIHands.get(j - 5).setSize(0);
		} else {
			Log.d(TAG, "update_AI_HeapSize(int j) j is over 8!");
		}
	}

	// �ж��Ƿ�������ע�����ǰ���Ѿ����й�������
	private boolean isSeries(Vector<Integer> sendCards) {
		boolean isSeries = true;
		for (int i = 0; i < sendCards.size() - 1; i++) {
			if ((sendCards.get(i) + 1) != sendCards.get(i + 1)) {
				// ���������˳�
				isSeries = false;
				break;
			}
		}
		return isSeries;
	}

	private boolean gapRemainJudge(int gap, int weight) {
		boolean juge = true;
		int random = (int) Math.round(Math.random() * (2 * 24 + 14));
		if (weight >= getValue(2)) { // ��2��3��������
			if (gap > 8) {
				if (random < (gap + getAIRemainedCards() + getPlayerRemainedCards())) {
					return false;
				}
			}
		} else { // ��ͨ��
			if (gap > 6) {
				if (random < (gap + getAIRemainedCards() + getPlayerRemainedCards())) {
					return false;
				}
			}
		}
		return juge;
	}

	private int findBig(Vector<Integer> WeightCards, int weight) {
		// �ҵ��ȴ���ֵ�����С�Ǳ꣬�Ҳ���Ϊ-1
		int FoundMinIndex = -1;
		for (int i = 0; i < WeightCards.size(); i++) {
			if (WeightCards.get(i) > weight) {
				FoundMinIndex = i;
				break;
			}
		}
		return FoundMinIndex;
	}

	private int findRepeat(Vector<Integer> WeightCards, int weight, int RepeatTimes) {
		// �ҵ��ȴ���ֵ�����С���ƣ��������ƣ�
		int count = 0;
		int tempWeight = 0;
		int FoundMinRepeatIndex = -1;

		int findIndex = findBig(WeightCards, weight);
		if (findIndex < 0) {
			return -1;
		}

		for (int i = findIndex; i < WeightCards.size(); i++) {
			tempWeight = WeightCards.get(i);
			count = 1;
			for (int j = i + 1; j < WeightCards.size(); j++) {
				if (WeightCards.get(j) == tempWeight) {
					count = count + 1;
				}
			}
			if (count >= RepeatTimes) {
				FoundMinRepeatIndex = i;
				break;
			}
		}

		if (FoundMinRepeatIndex >= 0) {
			int gap = WeightCards.get(FoundMinRepeatIndex) - weight; // �ȶԷ�������
			if (!gapRemainJudge(gap, WeightCards.get(FoundMinRepeatIndex))) {
				return -1;
			}
		}

		return FoundMinRepeatIndex;
	}

	private boolean findValue(Vector<Integer> WeightCards, int weight) {
		boolean FoundIndex = false;
		for (int i = 0; i < WeightCards.size(); i++) {
			if (WeightCards.get(i) == weight) {
				return true;
			}
		}
		return FoundIndex;
	}

	private int findSeries(Vector<Integer> weightcard, int weight, int len) {
		int FoundIndex = -1;
		int minIndex = -1;
		int size = weightcard.size();

		minIndex = findBig(weightcard, weight);
		if (minIndex < 0) { // ������С���ƴ����ҷ�������
			return -1;
		}

		int tempValue = 0;
		for (int i = minIndex; i < size; i++) {
			tempValue = weightcard.get(i);
			FoundIndex = i;
			for (int j = 0; j < len - 1; j++) {
				tempValue = tempValue + 1;
				if (!findValue(weightcard, tempValue)) { // û�ҵ�
					FoundIndex = -1;
					break;
				}
			}
			if (FoundIndex >= 0) { // �ҵ���
				return FoundIndex;
			}
		}
		return FoundIndex;
	}

	private void AIPlaySingle(Vector<Integer> WeightCards) {
		int maxIndex = -1;
		if (PlayerCards.size() > 0) {
			maxIndex = findBig(WeightCards, getPlayerBigestCommonCard()); // ���ڶԷ���������ͨ��-1��
		}
		// TODO aicards
		if (maxIndex >= 0 && (WeightCards.get(maxIndex) < WeightCards.lastElement())
				&& (WeightCards.get(maxIndex) < getValue(2))) { // ��ͨ���ҵ��˴��ڶԷ������ͨ�Ƶ���
			AISelect.add(WeightCards.get(maxIndex));
			Type_AISelect = 1;
			return;
		} else {
			AISelect.add(WeightCards.get(0));
			Type_AISelect = 1;
		}
	}

	private void AIPlayRepeat(Vector<Integer> WeightCards) {
		// �ҵ��ȴ���ֵ�����С���ƣ��������ƣ�
		int count = 0;
		int maxCount = 0;
		int tempWeight = 0;
		int FoundMinRepeatIndex = -1;
		int size = WeightCards.size();

		for (int i = 0; i < size; i++) {
			tempWeight = WeightCards.get(i);
			count = 1;
			for (int j = i + 1; j < size; j++) {
				if (WeightCards.get(j) == tempWeight) {
					count = count + 1;
				} else {
					break;
				}
			}
			if (maxCount < count) {
				maxCount = count;
				FoundMinRepeatIndex = i;
			}
		}
		if (maxCount >= 2) {
			tempWeight = WeightCards.get(FoundMinRepeatIndex);
			if (tempWeight >= getValue(2)) { // ������2��3ʱ
				if (getAIRemainedCards() <= (maxCount + 1)) { // �ƺ��٣�������ˣ�����
					for (int i = 0; i < maxCount; i++) {
						AISelect.add(tempWeight);
					}
					Type_AISelect = maxCount;
				}
			} else {
				for (int i = 0; i < maxCount; i++) {
					AISelect.add(tempWeight);
				}
				Type_AISelect = maxCount;
			}
		}
	}

	private void AIPlaySeries(Vector<Integer> weightcard) {
		int FoundIndex = -1;
		int len = 0;
		int maxlen = 0;
		int size = weightcard.size();
		int tempValue = 0;

		for (int i = 0; i < size; i++) {
			tempValue = weightcard.get(i);
			len = 1;
			for (int j = 0; j < size; j++) {
				tempValue = tempValue + 1;
				if (findValue(weightcard, tempValue)) { // �ҵ�
					len = len + 1;
				} else {
					break;
				}
			}
			if (maxlen < len) { // ��ʷ��������
				maxlen = len;
				FoundIndex = i;
			}
		}

		if (maxlen >= 3) { // �ҵ���
			tempValue = weightcard.get(FoundIndex);
			for (int i = 0; i < maxlen; i++) {
				AISelect.add(tempValue);
				tempValue = tempValue + 1;
			}
			if (maxlen > 3) {
				Type_AISelect = 5;
			} else {
				Type_AISelect = 4;
			}
		}
	}

	private Vector<Integer> AIFindSingleExceptSeriesAndRepeat(Vector<Integer> weightcard) {
		// TODO S

		int FoundIndex = 0;
		int len = 0;
		int size = weightcard.size();
		int tempValue = 0;
		Vector<Integer> SingleCards = (Vector<Integer>) weightcard.clone();

		for (int i = 0; i < size; i++) {
			tempValue = weightcard.get(i);
			len = 1;
			for (int j = 0; j < size; j++) { // ѭ��������
				tempValue = tempValue + 1;
				if (findValue(weightcard, tempValue)) { // �ҵ�
					len = len + 1;
				} else {
					break;
				}
			}
			if (len >= 3) {// �ҵ�һ������
				if (FoundIndex > 0) {
					if (i > (FoundIndex + len)) {
						tempValue = weightcard.get(i);
						for (int k = 0; k < len; k++) {
							// TODO ����Ҫɾ��������
							SingleCards.removeElement(tempValue);
							tempValue = tempValue + 1;
						}
						FoundIndex = i;
					}
				} else { // ��һ��
					tempValue = weightcard.get(i);
					for (int k = 0; k < len; k++) {
						// TODO ����Ҫɾ��������
						SingleCards.removeElement(tempValue);
						tempValue = tempValue + 1;
					}
					FoundIndex = i;
				}
			}
		}
		System.out.println("SingleCards" + SingleCards.toString());
		return SingleCards;
	}

	private void AIPlay() {
		int size = AISelect.size();
		if (size > 0) {
			TempPlayedDesk.clear();
		}
		Vector<Integer> tempDesk = (Vector<Integer>) AICards.clone();
		for (int i = 0; i < size; i++) {
			int tempCard = AISelect.get(i);
			for (int j = 0; j < tempDesk.size(); j++) {
				if (getValue(tempDesk.get(j)) == tempCard) {
					TempPlayedDesk.add(tempDesk.get(j)); // ��ӽ�PlayedDesk
					update_AI_HeapSize(j); // ����size
					tempDesk.set(j, 0); // �����ֹ�ٱ�ƥ��
					break;
				}
			}
		}
	}

	private void getAIPlayedCard() {

		getAISelect(); // ��ղ������µ�AISelect

		AIPlay();
		if (AISelect.size() == 0) {
			Eat = false;
		} else {
			Eat = true;
		}
	}

	private void getAISelect() {
		// AISelect�õ��������������
		AISelect.clear();
		Type_AISelect = 0;
		getAICards(); // �õ�AICards
		getPlayerCardsValue(); // �õ�playerCards������weight

		Vector<Integer> weightCards = new Vector<Integer>();
		for (int i = 0; i < AICards.size(); i++) {
			weightCards.add(getValue(AICards.get(i)));
		}
		sortCards(weightCards);

		int index = -1;
		int tempWeight = 0;
		switch (SelectCards.size()) {
		case 0:
			break;
		case 1:
			// TODO test
			if (!getAIPlaySingle(AIFindSingleExceptSeriesAndRepeat(weightCards))) {
				getAIPlaySingle(weightCards);
			}
			break;
		case 2:
			index = findRepeat(weightCards, SelectCards.get(0), 2);
			if (index >= 0) {
				tempWeight = weightCards.get(index);
				AISelect.add(tempWeight);
				AISelect.add(tempWeight);
				Type_AISelect = 2;
			}
			break;
		case 3:
			if (SelectCards.get(0) == SelectCards.get(2)) {
				index = findRepeat(weightCards, SelectCards.get(0), 3);
				if (index >= 0) {
					tempWeight = weightCards.get(index);
					AISelect.add(tempWeight);
					AISelect.add(tempWeight);
					AISelect.add(tempWeight);
					Type_AISelect = 3;
				}
			} else {
				index = findSeries(weightCards, SelectCards.get(0), 3);
				if (index >= 0) {
					tempWeight = weightCards.get(index);
					AISelect.add(tempWeight);
					AISelect.add(tempWeight + 1);
					AISelect.add(tempWeight + 2);
					Type_AISelect = 4;
				}
			}
			break;
		default:
			index = findSeries(weightCards, SelectCards.get(0), SelectCards.size());
			if (index >= 0) {
				tempWeight = weightCards.get(index);
				for (int i = 0; i < SelectCards.size(); i++) {
					AISelect.add(tempWeight);
					tempWeight = tempWeight + 1;
				}
				Type_AISelect = 5;

				break;
			}

		}
	}

	private void getAICards() {

		AICards.clear();
		int tempCard = 0;
		for (int i = 0; i < AIDesk.size(); i++) {
			tempCard = AIDesk.get(i).getTopCard();
			if (tempCard >= 0) {
				AICards.add(tempCard);
			}
		}
		for (int i = 0; i < AIHands.size(); i++) {
			tempCard = AIHands.get(i).getTopCard();
			if (tempCard >= 0) {
				AICards.add(tempCard);
			}
		}
	}

	private int getPlayerBigestCommonCard() {
		int bigCard = 0;
		for (int i = PlayerCards.size() - 1; i >= 0; i--) {

			if (PlayerCards.get(i) < getValue(2)) { // ������ͨ��
				bigCard = PlayerCards.get(i);
				break;
			}
		}
		return bigCard - 1;
	}

	private void getPlayerCardsValue() {
		PlayerCards.clear();
		int tempCard = 0;
		for (int i = 0; i < PlayerDesk.size(); i++) {
			tempCard = PlayerDesk.get(i).getTopCard();
			if (tempCard >= 0) {
				PlayerCards.add(tempCard);
			}
		}
		for (int i = 0; i < PlayerHands.size(); i++) {
			tempCard = PlayerHands.get(i).getTopCard();
			if (tempCard >= 0) {
				PlayerCards.add(tempCard);
			}
		}

		Vector<Integer> weightCards = new Vector<Integer>();
		for (int i = 0; i < PlayerCards.size(); i++) {
			if (PlayerCards.get(i) > 0) {

				weightCards.add(getValue(PlayerCards.get(i)));
			}
		}
		sortCards(weightCards);
		PlayerCards.clear();
		PlayerCards = (Vector<Integer>) weightCards.clone();
	}

	private boolean getAIPlaySingle(Vector<Integer> weightcard) {

		boolean status = false;
		int index = findBig(weightcard, SelectCards.get(0));
		int goodIndex = -1;
		int maxIndex = -1;
		if (PlayerCards.size() > 0) {
			maxIndex = findBig(weightcard, PlayerCards.lastElement() - 1); // ���ڵ��ڶԷ�������
			goodIndex = findBig(weightcard, getPlayerBigestCommonCard()); // ���ڶԷ��������ͨ��-1������
		}

		if (index >= 0) { // �ҵ��ܴ�ס�Է�����
			if (getAIRemainedCards() == 2) { // ֻ��������
				if (maxIndex >= 0 && (weightcard.get(maxIndex) > SelectCards.get(0))) {
					AISelect.add(weightcard.get(maxIndex));
					Type_AISelect = 1;
					System.out.println("���������Ƶ�������Է����� " + SelectCards.get(0) + "�ҳ�����" + weightcard.get(maxIndex));
					return true;
				}
			} else { // ���������Ƶ����

				if (goodIndex >= 0 && (weightcard.get(goodIndex) < getValue(2))
						&& (weightcard.get(goodIndex) > SelectCards.get(0))) { // ��ͨ���ҵ��˴��ڶԷ������Ƶ���
					AISelect.add(weightcard.get(goodIndex));
					Type_AISelect = 1;
					System.out.println("���Ǵ��ڶԷ���ͨ�Ƶ�������Է����� " + SelectCards.get(0) + "�ҳ�����" + weightcard.get(goodIndex));
					return true;
				} else {
					AISelect.add(weightcard.get(index));
					Type_AISelect = 1;
					return true;
				}
			}

		}
		return status;
	}

	private int getAIRemainedCards() {
		int count = 0;
		for (int i = 0; i < 5; i++) {
			count += AIDesk.get(i).getSize();
			if (i < 4) {
				count += AIHands.get(i).getSize();
			}
		}
		return count;
	}

	private int getPlayerRemainedCards() {
		int count = 0;
		for (int i = 0; i < 5; i++) {
			count += PlayerDesk.get(i).getSize();
			if (i < 4) {
				count += PlayerHands.get(i).getSize();
			}
		}
		return count;
	}

	public void AIPlayCard() {

		TempPlayedDesk.clear();
		PlayedDesk.clear();
		AISelect.clear();
		getAICards();
		getPlayerCardsValue(); // �õ�playerCards������weight

		Vector<Integer> weightCards = new Vector<Integer>();
		for (int i = 0; i < AICards.size(); i++) {
			int tempValue = getValue(AICards.get(i));
			if (tempValue > 0) {
				weightCards.add(tempValue);
			}
		}
		sortCards(weightCards);

		// TODO �޸ļ�
		AIPlaySeries(weightCards); // ����AISelect ��TypeSelect
		if (AISelect.size() > 0) {
			AIPlay(); // ��AISelect ����heapsize��TempPlayedDesk
			PlayedDesk = (Vector<Integer>) TempPlayedDesk.clone();
			Eat = true;
			return;
		}

		AIPlayRepeat(weightCards);
		if (AISelect.size() > 0) {
			AIPlay(); // ��AISelect ����heapsize��TempPlayedDesk
			PlayedDesk = (Vector<Integer>) TempPlayedDesk.clone();
			Eat = true;
			return;
		}

		AIPlaySingle(weightCards);
		AIPlay(); // ��AISelect ����heapsize��TempPlayedDesk
		PlayedDesk = (Vector<Integer>) TempPlayedDesk.clone();
		Eat = true;
		return;

	}

	public void isOk(int[] select, int len) {
		getSelectValueSort(select, len); // ��պ����SelectCards����

		boolean isOk = false;
		int type = 0;
		switch (SelectCards.size()) {
		case 0:
			break;
		case 1:
			isOk = true;
			type = 1;
			break;
		case 2:
			if (SelectCards.get(0) == SelectCards.get(1)) {
				isOk = true;
				type = 2;

			}
			break;
		case 3:
			// ��Ϊ�Ѿ������������ֻ����ͷβ����
			if (SelectCards.get(0) == SelectCards.get(2)) {
				isOk = true;
				type = 3;
			}
			if (isSeries(SelectCards)) {
				isOk = true;
				type = 4;
			}
			break;
		default:
			if (isSeries(SelectCards)) {
				isOk = true;
				type = 5;
			}
			break;
		}
		// ����size��PlayedDesk
		if (isOk) {
			Log.d(TAG, "AISelect" + AISelect.toString());
			if (AISelect.size() == 0) {
				update_HeapSize(select, 9); // �Լ�������Ҫ����size
				getAIPlayedCard(); // AI������ҲҪ����size
				PlayedDesk = (Vector<Integer>) TempPlayedDesk.clone();

				Log.d(TAG, "SelectCards" + SelectCards.toString());
				Log.d(TAG, "AISelect" + AISelect.toString());
				Log.d(TAG, "PlayedDesk" + PlayedDesk.toString());
			} else {
				// AI�г���
				Log.d(TAG, "Type_AISelect " + Type_AISelect + " AISelect" + AISelect.toString() + "SelectCards"
						+ SelectCards.toString());

				if ((type == Type_AISelect) && (SelectCards.get(0) > AISelect.get(0))) {
					update_HeapSize(select, 9); // �Լ�������Ҫ����size
					getAIPlayedCard(); // AI������ҲҪ����size
					PlayedDesk = (Vector<Integer>) TempPlayedDesk.clone();
				} else {
					TempPlayedDesk.clear();
					Eat = true;
				}
			}
		} else {
			TempPlayedDesk.clear();
		}
	}
}
