package com.fwgg8547.mathgame;

import java.util.*;

public class NumberGenerator
{
	
	private static final String TAG = "Number";
	private static final int BUFF_SIZE=8;
	public static final int[] PRIMITIVE ={
		1,2,3,5,7,11,13,17,19
	};
	
	private State mState;
	private Random mRand;
	private int mResultBuff[][];
	private List<Integer> mShuff1;
	private List<Integer> mShuff2;
	private List<Integer> mShuff3;
	private int mFirst;
	private int mSecond;
	
	public enum State {
		Idle,
		Make1,
		Make2,
		Make3
	}
	
	public NumberGenerator(){
		
	}
	
	public void initialize(){
		mRand = new Random();
		
		mResultBuff = new int[3][BUFF_SIZE];
		mShuff1 = new ArrayList<Integer>();
		mShuff2 = new ArrayList<Integer>();
		mShuff3 = new ArrayList<Integer>();
		
		makeShuffBuff();
		
		mState = State.Idle;
		mFirst = 0;
		mSecond =0;
	}
	
	private void dump(){
		for(int i=0; i<BUFF_SIZE; i++){
			Lg.w(TAG, ""+mShuff1.get(i));
		}
	}
	
	private void makeShuffBuff(){
		Lg.w(TAG, "makeshudff");
		mShuff1.clear();
		mShuff2.clear();
		mShuff3.clear();
		for(int i=0; i<BUFF_SIZE; i++){
			mShuff1.add(i);
			mShuff2.add(i);
			mShuff3.add(i);
		}
		Collections.shuffle(mShuff1);
		Collections.shuffle(mShuff2);
		Collections.shuffle(mShuff3);
	}
	
	private void makeResultBuff(){
		Lg.w(TAG, "makeresult");
		for(int i=0; i<BUFF_SIZE; i++){
			mResultBuff[0][i] = get10to19();
			mResultBuff[1][i] = get10to19();
			mResultBuff[2][i] = mResultBuff[0][i] * mResultBuff[1][i];
		}
	}
	
	public void remakeCard(){
		makeResultBuff();
		makeShuffBuff();
	}
	
	public int getBigNumber(){
		int cand =0;
		cand = getResult(3);
		
		if(cand == 0){
			remakeCard();
			cand = getResult(3);
		}
		return cand;
	}
	
	public int getSmallNumber(){
		int cand = 0;
		cand = get10to19();
		return cand;
	}
	
	public int getNewNumber(){
		int cand = 1;
		switch(mState){
			case Idle:
				cand = stateIdle();
				break;
			case Make1:
				cand = mFirst;
				mState = State.Make2;
				break;
			case Make2:
				cand = mSecond;
				mState = State.Idle;
				break;
			case Make3:
				break;
		}
		return cand;
	}
	
	private int get0to99(){
		return ((Math.abs(mRand.nextInt())) % 100);
	}
	
	private int get10to19(){
		return ((Math.abs(mRand.nextInt())) % 9) +10;
	}
	
	private int get0to9(){
		return ((Math.abs(mRand.nextInt())) % 9);
	}
	
	private int getResult(int type){
		boolean find = false;
		
		Integer indx = null;
		try{
		switch(type) {
			case 1:
			  indx = mShuff1.remove(0);
				break;
			case 2:
				indx = mShuff2.remove(0);
				break;
			case 3:
				indx = mShuff3.remove(0);
				break;
		}
		} catch(IndexOutOfBoundsException e){
			return 0;
		}
		if(indx == null){
			return 0;
		}
		return mResultBuff[type-1][indx];
	}
	
	private int stateIdle(){
		/*
		int seed = Math.abs(mRand.nextInt());
		seed = seed % 100;
		*/
		int seed = 49;
		int cand = 1;
		if(seed < 1){
			cand = 1;
		} else if(seed < 10){
			int prim = (Math.abs(mRand.nextInt()))% 9;
			cand = PRIMITIVE[prim];

		} else if(seed < 50) {
			mFirst = getResult(1);
			mSecond = getResult(2);
			cand = getResult(3);
			mState = State.Make1;
		} else {
			cand = (Math.abs(mRand.nextInt())% 19) +1;
		}
		
		return cand;
	}
}
