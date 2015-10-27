package com.fwgg8547.mathgame;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.View.*;
import android.view.View;
public class MainActivity extends Activity 
{
	private final static String TAG = "MainActivity";
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
     super.onCreate(savedInstanceState);
		 
		 Lg.w(TAG, "onCreate");
     setContentView(R.layout.main);
		 
		 MainThreadMssageHandler.getInstance().setActivity(this);
		 
		 GridLayout gl = (GridLayout)findViewById(R.id.gridspace);
		 LinearLayout ll = (LinearLayout)findViewById(R.id.cosole);
		 MathView mv = MathView.getInstance();
		 mv.initialize(this, gl,ll);
		 
		 MathModel.getInstance().initialize(mv);
		MathEngine.getInstance().initialize();
	}

	@Override
	protected void onStart()
	{
		// TODO: Implement this method
		super.onStart();
		Lg.w(TAG, "onStart");
		startEngine();
	}

	@Override
	protected void onStop()
	{
 		// TODO: Implement this method
		super.onStop();
		Lg.w(TAG, "onStop");
		stopEngine();
	}

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
	}

	@Override
	protected void onResume()
	{
		// TODO: Implement this method
		super.onResume();
		Lg.w(TAG, "onResume");
		MathEngine.getInstance().resume();
	}

	@Override
	protected void onPause()
	{
		// TODO: Implement this method
		super.onPause();
		Lg.w(TAG, "onPause");
		MathEngine.getInstance().pause();
	}
	
	private void startEngine(){
		MathEngine.getInstance().start();
	}
	
	private void stopEngine(){
		
	}

}
