package com.xjtu26.Gou.escapecard;

public class HeapCard {
	
	public HeapCard(int Cards[]) {
		// TODO Auto-generated constructor stub
		this.size=4;
		MyCards=new int[size];
		//有四张牌的堆
		for (int i = 0; i < 4; i++) {
			this.MyCards[i]=Cards[i];
		}
	}
	public HeapCard(int Card) {
		// TODO Auto-generated constructor stub
		this.size=1;
		//只有一张牌的堆
		MyCards=new int[size];
		MyCards[0]=Card;
	}
	
	private int size;
	private int[]MyCards=null;
	private int topCard;
	
	public void setSize(int size) {
		this.size = size;
	}
	public int getSize() {
		return size;
	}
	
	public int getTopCard() {
		topCard=0;
		if (size>=1) {
			topCard=MyCards[size-1];			
		}	
		return topCard;
	}
	
}
