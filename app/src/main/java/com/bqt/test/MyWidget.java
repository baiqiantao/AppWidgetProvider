package com.bqt.test;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Arrays;

public class MyWidget extends AppWidgetProvider {
	public static final String ACTION_MYWIDGET_ONCLICK = "com.bqt.test.mywidget.onclick";
	
	@SuppressLint("UnsafeProtectedBroadcastReceiver")
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		Log.i("bqt", "【onReceive，其他所有回调方法都是由它调用的】");
		//这里判断是自己的action，做自己的事情，比如小工具被点击了要干啥
		if (ACTION_MYWIDGET_ONCLICK.equals(intent.getAction())) {
			Toast.makeText(context, "什么是最重要的呢？\n         时间！争分夺秒！", Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		//根据 updatePeriodMillis 定义的时间定期调用该函数，此外当用户添加 Widget 时也会调用该函数，可以在这里进行必要的初始化操作。
		Log.i("bqt", "【onUpdate，当插件内容更新函数时调用，最重要的方法】" + Arrays.toString(appWidgetIds));
		for (int appWidgetId : appWidgetIds) {
			String text = context.getSharedPreferences("MyWidget", Context.MODE_PRIVATE).getString("MyWidgetText", "");
			RemoteViews remoteViews = Utils.getRemoteViews(context, text);
			appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
		}
	}
	
	@Override
	public void onEnabled(Context context) {
		Log.i("bqt", "【onEnabled，当 Widget 第一次被添加时调用】");
		//例如用户添加了两个你的 Widget，那么只有在添加第一个 Widget 时该方法会被调用，该方法适合执行你所有 Widgets 只需进行一次的操作
	}
	
	@Override
	public void onDisabled(Context context) {
		Log.i("bqt", "【onDisabled，当你的最后一个 Widget 被删除时调用】");//该方法适合用来清理之前在 onEnabled() 中进行的操作
	}
	
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		Log.i("bqt", "【onDeleted，当 Widget 被删除时调用】" + Arrays.toString(appWidgetIds));
	}
	
	@Override
	public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
		super.onRestored(context, oldWidgetIds, newWidgetIds);
		Log.i("bqt", "【onRestored，被还原是调用】旧" + Arrays.toString(oldWidgetIds) + "，新" + Arrays.toString(newWidgetIds));
	}
	
	@Override
	public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
		super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
		Log.i("bqt", "【onAppWidgetOptionsChanged，当 Widget 第一次被添加或者大小发生变化时调用】");
	}
}