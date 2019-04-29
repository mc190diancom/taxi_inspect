package com.miu360.taxi_check.common;

import java.util.List;

import com.wheel.wheel.adapter.AbstractWheelTextAdapter;

import android.content.Context;

public class CitySelectAdapter extends AbstractWheelTextAdapter {
	private List<City> cities;

	public CitySelectAdapter(Context context, List<City> cities) {
		super(context);
		this.cities = cities;
	}

	@Override
	public int getItemsCount() {
		return cities.size();
	}

	@Override
	protected CharSequence getItemText(int index) {
		if (index >= 0 && index < cities.size()) {
			return cities.get(index).getName();
		} else {
			return null;
		}
	}

}
