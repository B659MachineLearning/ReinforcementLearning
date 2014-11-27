package com.utilities;

public class State {
	private int x = -1;
	private int y = -1;
	private String stringVal;
	
	public State(int x, int y){
		this.x = x;
		this.y = y;
		this.stringVal = Integer.toString(x)+","+Integer.toString(y);
	}
	
	public int getXCord(){
		return this.x;
	}
	
	public int getYCord(){
		return this.y;
	}
	
	public String getStringVal(){
		return this.stringVal;
	}
	
	@Override
	public int hashCode()
	{
	    return stringVal.hashCode();
	}
	
	@Override
	public boolean equals(Object o)
	{
	    return this.stringVal.equals(o);
	}
	
	
}
