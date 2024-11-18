package com.proyect.cinegrupo5.data;

import java.util.List;

public class ClientesResponse {
	private List<ClientesInfo> items;
	public List<ClientesInfo> getItems() {
		return items;
	}
	public void setItems(List<ClientesInfo> items) {
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
