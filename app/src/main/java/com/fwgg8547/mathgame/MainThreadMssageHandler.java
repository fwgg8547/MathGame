package com.fwgg8547.mathgame;

import android.os.Message;
import android.os.Handler;
import android.content.Context;
import android.app.Activity;
import android.widget.Toast;


public class MainThreadMssageHandler extends Handler
{
		private final static int SCREENREFRESH = 3;
		private final static int DISPLAYTOAST = 7;
		private final static int DISPLAYTOASTBYID = 8;
		private final static int CONTENTLOADED = 9;
		private final static int DATAREFRESH = 10;
		private final static int DATAUPDATE = 11;
		private final static int DOSOMETHING = 12;
	
		private final static String TAG = "handler";
  
		private static MainThreadMssageHandler instance;
		private static Activity mAct;
    
		public interface Callback {
			public void doCallback(Object arg);
		}
		
		public class MsgObject {
				public int id;
				public int mReqId;
				public boolean isConnected;
				public String mMessage;
				public int mMessageId;
				public Callback mCallback;
				public Object mArg;
		}
		
		public static MainThreadMssageHandler getInstance(/*Activity a*/) {
				if(instance == null){
						instance = new MainThreadMssageHandler(/*a*/);
				}
				return instance;
		}
  
		private MainThreadMssageHandler(/*Activity a*/){
		
		}
		
		public void setActivity(Activity act){
			mAct = act;
		}
		
		@Override
		public synchronized void handleMessage(Message msg)
    {
				super.handleMessage(msg);
				MsgObject o = (MsgObject)(msg.obj);
				switch (o.id) {
        
        case SCREENREFRESH:
						Lg.i(TAG, "refresh");
						break;
						
        case DISPLAYTOAST:
						Lg.i(TAG, "display toast");
						Toast.makeText(mAct, o.mMessage, Toast.LENGTH_LONG).show();
						break;
						
				case DISPLAYTOASTBYID:
						Lg.i(TAG, "display toast by id");
						Toast.makeText(mAct, mAct.getString(o.mMessageId), Toast.LENGTH_LONG).show();
						break;
						
				case DATAUPDATE:
						Lg.i(TAG, "data updated " + o.mReqId);
						break;
								
				case DOSOMETHING:
						Lg.w(TAG, "do something");
						o.mCallback.doCallback(o.mArg);
						Lg.w(TAG, "do somethig end");
						break;
        default:
						Lg.e(TAG, "unknown event");
				}
    }
  
		public synchronized void doSomething(Callback cb, Object arg){
			MsgObject o = new MsgObject();
			o.id = DOSOMETHING;
			o.mArg = arg;
			o.mCallback = cb;
			Message msg = Message.obtain();
			msg.obj = o;
			sendMessage(msg);
		}
		
		public  void notifyDataChanged(int pos){
        MsgObject o = new MsgObject();
        o.mReqId = pos;
        o.id = DATAUPDATE;
        Message msg = Message.obtain();
        msg.obj = o;
        sendMessage(msg);
    }

		public void notifySomethingForToast(String message) {
        MsgObject o = new MsgObject();
        o.id = DISPLAYTOAST;
				o.mMessage = message;
        Message msg = Message.obtain();
        msg.obj = o;
        sendMessage(msg);
		}
		
    public void notifySomethingIdForToast(int id) {
        MsgObject o = new MsgObject();
        o.id = DISPLAYTOASTBYID;
				o.mMessageId = id;
        Message msg = Message.obtain();
        msg.obj = o;
        sendMessage(msg);
		}
	
	
    public void notifyDataRefresh()
		{
				MsgObject o = new MsgObject();
				o.id = DATAREFRESH;
				Message msg = Message.obtain();
				msg.obj = o;
				sendMessage(msg);
		}
   
		
		public void notifyRefresh2(){
				MsgObject o = new MsgObject();
				o.id = SCREENREFRESH;
				Message msg = Message.obtain();
				msg.obj = o;
				sendMessage(msg);
		}

		public void notifyContentLoaded(int pos){
				MsgObject o = new MsgObject();
				o.id = CONTENTLOADED;
				o.mReqId = pos;
				Message msg = Message.obtain();
				msg.obj = o;
				sendMessage(msg);
		}
}
