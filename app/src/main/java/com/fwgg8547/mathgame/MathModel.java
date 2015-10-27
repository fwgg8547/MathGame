package com.fwgg8547.mathgame;

import java.util.*;

public class MathModel
{
	private static final String TAG = "Model";
	private static MathModel instance;
	private HashMap<Integer, Card> mCards;
	private ArrayList<Card> mSelectedCards;
	private ArrayList<Card> mClearedCards;
	private Callback mCb;
	
	public interface Callback{
		public void updated(int pos);
		public void clockUpdate(int clock);
	}
	
	public class Card{
		public int mNum;
		public boolean mIsSelected;
		public int mPos;
		public Card(int nm, int pos){
			mNum = nm;
			mPos = pos;
			mIsSelected = false;
		}
	}
	
	public static MathModel getInstance(){
		if(instance == null){
			instance = new MathModel();
		}
		return instance;
	}
	
	private MathModel(){
		mCards = null;
		mSelectedCards = null;
	}
	
	public void initialize(Callback cb){
		mCards = new HashMap<Integer, Card>();
		mSelectedCards = new ArrayList<Card>();
		mClearedCards = new ArrayList<Card>();
		mCb = cb;
		int i = 0;
		while(i < 25){
			if(i==12){
				i++;
				continue;
			}
			add(i, i);
			i++;
		}
	}
	
	public void clockUpdate(int c){
		mCb.clockUpdate(c);
	}
	
	public void clockClear(){
		clockUpdate(0);
	}
	
	public void add(Integer pos, Integer nm){
		mCards.put(pos, new Card(nm, pos));
		mCb.updated(pos);
	}
	
	public void modify(Integer pos, Integer nm){
		Lg.w(TAG, "mod" + pos);
		Card cd = mCards.get(pos);
		cd.mNum = nm;
		mCb.updated(pos);
	}
	
	public int getNumberOfCard(Integer pos){
		Card cd = mCards.get(pos);
		return cd.mNum;
	}
	
	public void selected(Integer pos){
		
		Card cd = mCards.get(pos);
		Lg.w(TAG, "selected " + pos +";" + cd.mNum);
		cd.mIsSelected = true;
		mSelectedCards.add(cd);
		mCb.updated(pos);
	}
	
	
	public void unselected(int pos){
		
		Card cd = mCards.get(pos);
		Lg.w(TAG, "unselected " +pos+";"+cd.mNum);
		cd.mIsSelected = false;
		if(mSelectedCards.remove(cd)){
			Lg.w(TAG, "unselected remove");
		} else {
			Lg.w(TAG, "not found");
		}
		mCb.updated(pos);
	}
	
	public void cleared(int pos){
		Lg.w(TAG, "cleared"+pos);
		Card cd = mCards.get(pos);
		mClearedCards.add(cd);
	}
	
	public int getClearedCount(){
		return mClearedCards.size();
	}
	
	public Card getClearedCard(){
		return mClearedCards.get(0);
	}
	
	public void removeClearedCard(int pos){
		Card cd = mCards.get(pos);
		mClearedCards.remove(cd);
	}
	
	public boolean isSelected(int pos){
		boolean ret =false;
		Card cd = mCards.get(pos);
		if(cd.mIsSelected){
			ret = true;
		}
		
		return ret;
	}
	
	public List<Card> getSelectedCards(){
		return mSelectedCards;
	}
	
	public void clearAllSelectedCards(){
		Lg.w(TAG, "clear all selected cards");
		Iterator ite = mSelectedCards.iterator();
		while(ite.hasNext()){
			Card cd = (Card)ite.next();
			cd.mIsSelected = false;
			if(cd.mNum !=0){
				Lg.w(TAG, "update " +cd.mPos);
				mCb.updated(cd.mPos);
				cd.mNum = 0;
			} else {
				Lg.w(TAG, "update num is 0 " +cd.mPos);
			}
		}
		mSelectedCards.clear();
	}
	
	public int getSelectedCount(){
		return mSelectedCards.size();
	}
	
	
}
