package com.miu30.common.async;

public abstract class Callback<T> {
	public void onStart() {
	}

	abstract public void onHandle(T result);

}
