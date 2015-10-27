package com.fwgg8547.mathgame;

import android.content.Context;
import android.util.Log;

public class Lg
{
		final private static boolean isOutput = true;
		final private static int level = 4;
  
		public static void i(String tag, String s){
				if (isOutput && level <= 3) Log.i(tag, s);   
		}

		public static void w(String tag, String s){
				if (isOutput && level <= 4) Log.w(tag, s);   
		}

		public static void e(String tag, String s){
				if (isOutput && level <= 5) Log.e(tag, s);   
		}
  
}
