package com.dev.stdev.njuskalonovosti;

import android.app.Application;

public class App extends Application {

	private static App application;

	@Override
	public void onCreate() {
		super.onCreate();
		application = this;
	}

	public static App getApplication() {
		return application;
	}
}
