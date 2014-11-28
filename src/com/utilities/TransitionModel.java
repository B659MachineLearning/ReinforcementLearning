package com.utilities;

public class TransitionModel {
	private double up;
	private double right;
	private double down;
	private double left;
	
	public TransitionModel(double u, double r, double d, double l){
		this.up = u;
		this.right = r;
		this.down = d;
		this.left = l;
	}
	
	public double getTranProb(String act){
		switch(act){
			case "up":
				return this.up;
			
			case "right":
				return this.right;
				
			case "down":
				return this.down;
				
			case "left":
				return this.left;
			
			default:
				return 0.0;
		}
	}
	
//	public double getUpProb(){
//		return this.up;
//	}
//	
//	public double getRightProb(){
//		return this.right;
//	}
//	
//	public double getDownProb(){
//		return this.down;
//	}
//	
//	public double getLeftProb(){
//		return this.left;
//	}
}
