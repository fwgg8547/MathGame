package com.fwgg8547.mathgame;
import java.security.*;
import java.util.*;

public class MathEngine
{
	private static final String TAG = "MathEngine";
	private static final int BIG_FIELD1[] = {6,7,8,11,13,16,17,18};
	private static final int SMALL_FIELD1[] = {0,1,2,3,4,5,9,10,14,15,19,20,21,22,23,24};
	private static final int TIMEINIT = 30;
	private static MathEngine instance;
	private Random mRand;
	private GameTask mTask;
	private Timer mTimer;
	private NumberGenerator mNg;
	private int mSelectedState;
	private int mClock;
	private int mDownClock;
	private int mCounter;
	
	public static MathEngine getInstance(){
		if(instance == null){
			instance = new MathEngine();
		}
		
		return instance;
	}
	
	private MathEngine(){
		create();
	}
	
	public void initialize(){
		
	}
	
	public void create(){
		mRand = new Random();
		mClock = TIMEINIT;
		mCounter = 0;
		mNg = new NumberGenerator();
		mNg.initialize();
	}
	
	public void start(){
		mTask = new GameTask();
	}

	public void stop(){
		mTimer.cancel();
		mTask = null;
	}
	
	public void resume(){
		mTask = new GameTask();
		mTimer = new Timer();
		mTimer.schedule(mTask, 1500, 1000);
	}
	
	public void pause(){
		mTimer.cancel();
		mTask = null;
	}
	
	public int getNewNumber(){
		
		return mNg.getNewNumber();
	}
	
	
	
	public class GameTask extends TimerTask
	{
		private boolean isStarted =false;
		private int mTaskId;
		
		
		public GameTask(){
			super();
			mTaskId = 0;
			mCounter = 0;
		}

		public void clearCounter(){
			mCounter = 0;
		}
		
		@Override
		public void run()
		{
			
			//Lg.w(TAG, "run");
			if (!isStarted){
				createFiled1();
				isStarted = true;
				
			} else {
				do1();
				/*
				if (mCounter % 15 == 0) {
						do15();
				}
				*/
				if(mCounter != 0 && mCounter % TIMEINIT == 0){
					do30();
				}
			}
			
			mCounter++;
		}
		
		private void do1(){
			clockdown();
		}
		
		private void do15(){
			Lg.w(TAG, "do15");
			addBigNum();
			clockClear();
		}
		
		private void do30(){
			Lg.w(TAG, "do30");
			addBigNum();
			clockClear();
		}
	}
	
	public void clockup(){
		mClock++;
		MathModel.getInstance().clockUpdate(mClock);
	}
	
	public void clockdown(){
		mClock--;
		MathModel.getInstance().clockUpdate(mClock);
	}
	
	public void clockClear(){
		Lg.w(TAG, "Clock Clear");
		mClock = TIMEINIT;
		MathModel.getInstance().clockUpdate(mClock);
	}
	
	public void createFiled1(){
		mNg.remakeCard();
		int nm;
		MathModel mm = MathModel.getInstance();
		
		for(int indx: BIG_FIELD1){
			nm = mNg.getBigNumber();
			mm.modify(indx, nm);
		}
		
		for(int indx: SMALL_FIELD1){
			nm = mNg.getSmallNumber();
			mm.modify(indx, nm);
		}
	}
	
	public void createFilee2(){
		mNg.remakeCard();
		int nm;
		int i = 0;
		while(i < 25){
			if(i==12){
				i++;
				continue;
			}
			nm = getNewNumber();
			MathModel.getInstance().modify(i, nm);
			i++;
		}
	}
	
	public void createSelected(){
		createFiled1();
		clockClear();
		//updateFiled();
	}
	
	public void updateSelected(){
		//createFiled1();
		updateFiled();
	}
	
	public void updateFiled(){
		int nm;
		MathModel mm = MathModel.getInstance();
		
		for(int indx: SMALL_FIELD1){
			nm = mNg.getSmallNumber();
			mm.modify(indx, nm);
		}
	}
	
	public void addBigNum(){
		MathModel mm = MathModel.getInstance();
		if(mm.getClearedCount() > 0){
			Lg.w(TAG, "add new big num");
			MathModel.Card cd = mm.getClearedCard();
			mm.modify(cd.mPos, mNg.getBigNumber());
			mm.removeClearedCard(cd.mPos);
		}
	}
	
	private boolean isSmallSelected(int pos){
		boolean ret = false;
		for(int indx: SMALL_FIELD1){
			if (indx == pos){
				ret = true;
				break;
			}
		}
		
		return ret;
	}
	
	private boolean divideBigBySmall(int bigindx){
		boolean ret = false;
		List<MathModel.Card> cds = MathModel.getInstance().getSelectedCards();
		MathModel.Card cd = cds.get(0);
		int small = cd.mNum;
		int big = MathModel.getInstance().getNumberOfCard(bigindx);
		int newBig = 0;
		if(big % small == 0){
			newBig = big/small;
			clockClear();
			MathModel.getInstance().modify(bigindx, newBig);
			MathModel.getInstance().clearAllSelectedCards();
			ret = true;
		}
		return ret;
	}
	
	public void cardSelected(Integer pos){
		if(mSelectedState == 0 && isSmallSelected(pos)){
			if(MathModel.getInstance().isSelected(pos)){
				MathModel.getInstance().unselected(pos);
			} else {
				MathModel.getInstance().selected(pos);
				mSelectedState = 1;
			}
			
		} else if(mSelectedState == 1 && !isSmallSelected(pos)) {
			if(divideBigBySmall(pos)){
				mTask.clearCounter();
				mSelectedState = 0;
				MathModel.getInstance().cleared(pos);
			}
		} else if(mSelectedState == 1){
			if(MathModel.getInstance().isSelected(pos)){
				MathModel.getInstance().unselected(pos);
				mSelectedState = 0;
			} 
		}
		
	}
	
	
	public void cardSelected1(Integer pos){
		MathModel.getInstance().selected(pos);
		int sel = MathModel.getInstance().getSelectedCount();

		if(sel >= 3){
			Lg.w(TAG, "calc");
			if(checkCalc()){
				MathModel.getInstance().clearAllSelectedCards();
				//int nm = getNewNumber();
				//MathModel.getInstance().modify(pos, nm);
			}
			//MathModel.getInstance().clearSelectedAll();

		}

	}
	
	private boolean checkCalc(){
		List<MathModel.Card> cdlist = MathModel.getInstance().getSelectedCards();
		
		int cdArray[] = new int[3];
		int i = 0;
		Iterator ite = cdlist.iterator();
		while(ite.hasNext() && i < 3){
			MathModel.Card cd = (MathModel.Card)ite.next();
			cdArray[i] = cd.mNum;
			i++;
		}
		
		if(cdArray[0] * cdArray[1] == cdArray[2]){
			Lg.w(TAG, "hit");
			return true;
		}
		
		return false;
	}
}
