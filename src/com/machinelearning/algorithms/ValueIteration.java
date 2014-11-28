package com.machinelearning.algorithms;

import com.utilities.Config;
import com.utilities.TransitionModel;

public class ValueIteration {
	private static int gridSizeLength;
	private static int gridSizeHeight;
	private static int numberOfPossActions;
	private static double utilValue;
	private static double states[][];
	private static String[] actions = {"up", "right", "down", "left"}; 
	
	private static double delta;
	//private static double epsilon;
	private static double gamma;
	
	private static int reward[][] = {
									{0,	  0,	0,	 10},
									{0,	-50,    0,	  0},
									{0,	  0,	0,	  0},
									{1,	  0,	0,	  0},	
									};
	
	private static double upTransit[] 		= {0.8, 0.0, 0.0, 0.2};
	private static double rightTransit[] 	= {0.0, 0.8, 0.2, 0.0};
	private static double downTransit[] 	= {0.0, 0.0, 1.0, 0.0};
	private static double leftTransit[] 	= {0.0, 0.0, 0.0, 1.0};
	
	
	public static void main(String args[]){
		//Get size of the grid
		gridSizeLength = Integer.parseInt(Config.readConfig("gridSizeLength"));
		gridSizeHeight = Integer.parseInt(Config.readConfig("gridSizeHeight"));
		numberOfPossActions = Integer.parseInt(Config.readConfig("numberOfPossActions"));
		
		delta = Integer.parseInt(Config.readConfig("delta"));
		//epsilon = Integer.parseInt(Config.readConfig("epsilon"));
		gamma = Integer.parseInt(Config.readConfig("gamma"));
		
		states = new double[gridSizeLength][gridSizeHeight]; 
		
		//Value iteration algorithm 
		/*
		 * 0 - up
		 * 1 - right
		 * 2 - down
		 * 3 - left
		*/
		double utilVal = 0.0f;
		double currUtilVal = 0.0f;
		boolean isConverged = false;
		
		while(!isConverged){
			for(int i=0; i<gridSizeHeight; i++){
				for(int j=0; j<gridSizeLength; j++){
					utilVal = getUtilVal(i,j);
					
					//Get Current utility value for state and action
					currUtilVal = states[i][j];
					
					//Check for Error threshold
					if(currUtilVal - utilVal > delta){
						isConverged = false;
						states[i][j] = utilVal;
					}
					else{
						isConverged = true;
					}
				}
			}
		}
		
		System.out.println("ValueIteration Output : ");
		System.out.println("=============================");
		for(int i=0; i<gridSizeHeight; i++){
			for(int j=0; j<gridSizeLength; j++){
				System.out.println(" | "+states[i][j]+" | ");
			}
			System.out.println();
		}
		System.out.println("=============================");
	}
	
	
	public static double getUtilVal(int x, int y){
		double stateReward = reward[x][y] - 1;
		double maxVal = 0.0;
		double uVal = 0.0;
		double transVal = 0.0;
		for(int j=0; j<numberOfPossActions; j++){
			 TransitionModel trn = getTransitionModel(actions[j]);
			 transVal = 0.0;
			 for(int k=0; k<numberOfPossActions; k++){
				 transVal += trn.getTranProb(actions[k])*states[getNextXcord(x, actions[k])][getNextYcord(y, actions[k])];
			 }
			 uVal = stateReward + (gamma * transVal);
			 
			 if(uVal > maxVal){
				 maxVal = uVal;
			 }
		}
		
		return maxVal;
		
	}
	
	public static int getNextYcord(int y, String action){
		if(action.equalsIgnoreCase("up")){
			if(y-1 >= 0){
				return y-1;
			}
			else{
				return y;
			}
		}
		else if(action.equalsIgnoreCase("down")){
			if(y+1 <= gridSizeHeight-1){
				return y+1;
			}
			else{
				return y;
			}
		}
		else{
			return y;
		}
	}
	
	public static int getNextXcord(int x, String action){
		if(action.equalsIgnoreCase("left")){
			if(x-1 >= 0){
				return x-1;
			}
			else{
				return x;
			}
		}
		else if(action.equalsIgnoreCase("right")){
			if(x+1 <= gridSizeLength-1){
				return x+1;
			}
			else{
				return x;
			}
		}
		else{
			return x;
		}
	}
	
	public static TransitionModel getTransitionModel(String actn){
		TransitionModel t = null;
		switch(actn){
			case "up":
				t = new TransitionModel(upTransit[0], upTransit[1], upTransit[2], upTransit[3]);
				return t;
				
			case "right":
				t = new TransitionModel(rightTransit[0], rightTransit[1], rightTransit[2], rightTransit[3]);
				return t;
				
			case "down":
				t = new TransitionModel(downTransit[0], downTransit[1], downTransit[2], downTransit[3]);
				return t;
			
			case "left":
				t = new TransitionModel(leftTransit[0], leftTransit[1], leftTransit[2], leftTransit[3]);
				return t;
				
			default:
				t = new TransitionModel(0.0, 0.0, 0.0, 0.0);
				return t;
		}
	}
}
