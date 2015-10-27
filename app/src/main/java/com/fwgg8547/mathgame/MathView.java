package com.fwgg8547.mathgame;

import android.widget.*;
import android.content.Context;
import java.util.*;
import android.view.SurfaceHolder.*;
import android.util.DisplayMetrics;
import android.app.Activity;
import android.view.View.*;
import android.view.View;
import android.graphics.*;
import com.fwgg8547.mathgame.R;

public class MathView implements MathModel.Callback
{
	private static final String TAG = "MathView";
	private static final int KEY_ID= 1;
	private static final int CREATE_BTN_INDX = 0;
	private static final int UPDATE_BTN_INDX = 1;
	private static final int SUM_TEXT_INDX = 2;
	private static MathView instance;
	private GridLayout mGl;
	private LinearLayout mCon;
	private List<Integer> mNumList;
	private static DisplayMetrics mDisplayMetrics;
	private CardClickCallback mCbClick;
	private CreateBtnClickCallback mCbcreate;
	private UpdateBtnClickCallback mCbupdate;
	
	public class CreateBtnClickCallback implements View.OnClickListener
	{

		@Override
			public void onClick(View p1)
		{
			MathEngine.getInstance().createSelected();
		}

	}
	
	public class UpdateBtnClickCallback implements View.OnClickListener
	{

		@Override
			public void onClick(View p1)
		{
			MathEngine.getInstance().updateSelected();
		}

	}
	
	public class CardClickCallback implements View.OnClickListener
	{

		@Override
			public void onClick(View p1)
		{
			int id = (int)p1.getTag(R.string.key_id);
			Lg.w(TAG, "clicked id= "+ id);
			MathEngine.getInstance().cardSelected(id);
			updateTmpSum();
		}

	}
	
	public static MathView getInstance(){
		if(instance == null){
			instance = new MathView();
		}
		return instance;
	}
	
	private MathView(){
		mGl = null;
		mNumList = null;
	}
	
	public static int getDPI(int size){
		return (size * mDisplayMetrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;        
	}
	
	public void initialize(Activity ctx, GridLayout gl, LinearLayout ll){
		Lg.w(TAG, "initialize");
		mCon = ll;
		mGl = gl;
		mNumList = new ArrayList<Integer>();
		mDisplayMetrics = new DisplayMetrics();
		ctx.getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		
		mCbcreate = new CreateBtnClickCallback();
		mCbupdate = new UpdateBtnClickCallback();
		mCbClick = new CardClickCallback();
		
		Button btn1 = (Button)mCon.getChildAt(CREATE_BTN_INDX);
		btn1.setOnClickListener(mCbcreate);
		
		Button btn2 = (Button)mCon.getChildAt(UPDATE_BTN_INDX);
		btn2.setOnClickListener(mCbupdate);
		
		int i = 0;
		int j = 0;
		int id = 0;
		
		while(j < 5){
			while(i < 5){
			
				id = j*5 + i;
				Button btn = new Button(ctx);
				GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
				lp.width = getDPI(50);
				lp.columnSpec = GridLayout.spec(i);
				lp.rowSpec = GridLayout.spec(j);
				btn.setLayoutParams(lp);
				String text = new Integer(id).toString();
				btn.setText(text);
				btn.setTag(R.string.key_id, new Integer(id));
				if(id!=12){
					btn.setOnClickListener(mCbClick);
				}
				mGl.addView(btn);
				
				Lg.w(TAG, ""  + id);
				i++;
			}
			i=0;
			j++;
		}
		
		mGl.requestLayout();
	}
	
	public void addCard(int pos, int nm){
		
	}

	// model callback
	@Override
		public void updated(int pos)
	{
		Lg.i(TAG, "updated pos " + pos);
		MainThreadMssageHandler.getInstance().doSomething(new MainThreadMssageHandler.Callback(){
				public void doCallback(Object args){
					Lg.i(TAG, "doCallback");
					Integer nm = (Integer)args;
					doUpdate(nm);
				}
			}, 
			new Integer(pos));
	}

	@Override
		public void clockUpdate(int clock)
	{
		MainThreadMssageHandler.getInstance().doSomething(new MainThreadMssageHandler.Callback(){
				public void doCallback(Object args){
					Lg.w(TAG, "doCallback");
					Integer nm = (Integer)args;
					doClockUpdate(nm);
				}
			}, 
			new Integer(clock));
		
	}
	
	public void doClockUpdate(int clock){
		Lg.w(TAG, "doClockUpdate clock " + clock);
		Button bt = (Button)mGl.getChildAt(12);
		bt.setText(""+clock);
	}

	public void doUpdate(int pos)
	{
		Lg.w(TAG, "doUpdate pos " + pos);
		MathModel mm = MathModel.getInstance();
		int nm = mm.getNumberOfCard(pos);
		Button bt = (Button)mGl.getChildAt(pos);
		if(mm.isSelected(pos)){
			bt.setBackgroundColor(Color.BLACK);
			bt.setTextColor(Color.WHITE);
		} else {
			bt.setBackgroundColor(Color.WHITE);
			bt.setTextColor(Color.BLACK);
		}
		
		bt.setText("" + nm);
		updateTmpSum();
	}
	
	private void updateTmpSum(){
		MathModel mm = MathModel.getInstance();
		List<MathModel.Card> cds = mm.getSelectedCards();
		
		if(cds == null){
			return;
		}
		int sum = 1;
		Iterator ite = cds.iterator();
		while(ite.hasNext()){
			MathModel.Card cd = (MathModel.Card)ite.next();
			sum = sum * cd.mNum;
		}
		
		TextView tv = (TextView)mCon.getChildAt(SUM_TEXT_INDX);
		tv.setText(""+sum);
	}
}
