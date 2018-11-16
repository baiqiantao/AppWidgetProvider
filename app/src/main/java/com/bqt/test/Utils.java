package com.bqt.test;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {
	
	public static RemoteViews getRemoteViews(Context context, String text) {
		SpannableString textSpannableString = Utils.getSpannableString(context, text, Color.WHITE, 15);
		String date = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.getDefault()).format(new Date());
		SpannableString dateSpannableString = Utils.getSpannableString(context, date, Color.DKGRAY, 12);
		
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.my_app_widget);
		remoteViews.setTextViewText(R.id.tv_data, dateSpannableString);//时间
		remoteViews.setTextViewText(R.id.tv_text, textSpannableString);//内容
		
		Intent intent = new Intent(context, EditActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
		intent.putExtra("text", text);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.iv_icon, pendingIntent);
		
		Intent actionIntent = new Intent(context, MyWidget.class);//显示意图
		actionIntent.setAction(MyWidget.ACTION_MYWIDGET_ONCLICK);
		//actionIntent.setPackage(context.getPackageName());//隐式意图必须设置Package，实际测试发现，如果使用隐式意图，在应用被杀掉时不响应广播
		PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.tv_text, pIntent);
		
		return remoteViews;
	}
	
	public static SpannableString getSpannableString(Context context, String source, int color, int size) {
		SpannableString mSpannableString = new SpannableString(source);
		int dpValue = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, context.getResources().getDisplayMetrics());
		String firstLine = source.contains("\n") ? source.substring(0, source.indexOf("\n")) : source;
		
		//第一行的样式
		ForegroundColorSpan colorSpan = new ForegroundColorSpan(color);//颜色
		mSpannableString.setSpan(colorSpan, 0, firstLine.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(dpValue);//大小
		mSpannableString.setSpan(absoluteSizeSpan, 0, firstLine.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		//其他行的样式
		if (source.contains("\n")) {
			String otherLine = source.substring(source.indexOf("\n"));
			if (otherLine.length() > 0) {
				ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(Color.YELLOW);//颜色
				mSpannableString.setSpan(colorSpan2, firstLine.length(), source.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				AbsoluteSizeSpan absoluteSizeSpan2 = new AbsoluteSizeSpan((int) (0.8f * dpValue));//大小
				mSpannableString.setSpan(absoluteSizeSpan2, firstLine.length(), source.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return mSpannableString;
	}
}
