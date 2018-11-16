package com.bqt.test;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

public class EditActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout linearLayout = getContentView();
		setContentView(linearLayout);
		new Handler().postDelayed(this::showSoftInput, 100);
	}
	
	@NonNull
	private LinearLayout getContentView() {
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		
		EditText editText = new EditText(this);
		editText.setHint("请输入小工具中显示的内容");
		editText.setText(getIntent() != null ? getIntent().getStringExtra("text") : "");
		editText.setLines(3);
		editText.setGravity(Gravity.CENTER);
		editText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		linearLayout.addView(editText);
		
		Button button = new Button(this);
		button.setText("保存");
		button.setOnClickListener(v -> {
			updateAppWidget(editText.getText().toString());
			finish();
		});
		linearLayout.addView(button);
		
		return linearLayout;
	}
	
	private void updateAppWidget(String text) {
		ComponentName componentName = new ComponentName(this, MyWidget.class);
		getSharedPreferences("MyWidget", Context.MODE_PRIVATE).edit().putString("MyWidgetText", text).apply();
		RemoteViews remoteViews = Utils.getRemoteViews(this, text);
		AppWidgetManager.getInstance(this).updateAppWidget(componentName, remoteViews);
	}
	
	private void showSoftInput() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
		}
	}
}