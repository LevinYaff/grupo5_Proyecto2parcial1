package com.proyect.cinegrupo5.data;

import java.util.List;

public class SalasResponse {
	private List<SalasInfo> items;
	public List<SalasInfo> getItems() {
		return items;
	}
	public void setItems(List<SalasInfo> items) {
		this.items = items;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	private int count;
}
