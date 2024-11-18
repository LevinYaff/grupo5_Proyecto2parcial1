package com.proyect.cinegrupo5.data;

import java.util.List;

public class TicketsResponse {
	private List<TicketsInfo> items;
	public List<TicketsInfo> getItems() {
		return items;
	}
	public void setItems(List<TicketsInfo> items) {
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
