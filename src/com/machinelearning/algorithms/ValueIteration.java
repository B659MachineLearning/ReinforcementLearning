package com.machinelearning.algorithms;

/*
 * Authors : Aniket Bhosale and Mayur Tare
 * 
 * Description :
 * Class to implement Reinforcement learning using Value Iteration 
 */

import com.utilities.Config;
import com.utilities.TransitionModel;

public class ValueIteration {
	private static int gridSizeLength;
	private static int gridSizeHeight;
	private static int numberOfPossActions;
	private static int timeStepReward;
	
	private static int count = 0;
	
	//Grid
	private static double states[][];
	//Possible actions
	private static String[] actions = {"up", "right", "down", "left"}; 
	
	private static double delta;
	private static double gamma;
	
	//Rewards
	private static int reward[][] = {
									{0,	  0,	0,	 10},
									{0, -50,    0,	  0},
									{0,	  0,	0,	  0},
									{0,   0,	0,	  0},
									{0,	  0,	0,	  0},	
									};
	
	//Transition Model
										  //   up  right down left
	private static double upTransit[] 		= {0.8, 0.0, 0.0, 0.2};
	private static double rightTransit[] 	= {0.0, 0.8, 0.2, 0.0};
	private static double downTransit[] 	= {0.0, 0.0, 1.0, 0.0};
	private static double leftTransit[] 	= {0.0, 0.0, 0.0, 1.0};	
	
	
	public void solveValueIteration(){
		//Get size of the grid
		gridSizeLength = Integer.parseInt(Config.readConfig("gridSizeLength"));
		gridSizeHeight = Integer.parseInt(Config.readConfig("gridSizeHeight"));
		numberOfPossActions = Integer.parseInt(Config.readConfig("numberOfPossActions"));
		timeStepReward = Integer.parseInt(Config.readConfig("timeStepReward"));
		
		delta = Double.parseDouble(Config.readConfig("delta"));
		//epsilon = Integer.parseInt(Config.readConfig("epsilon"));
		gamma = Double.parseDouble(Config.readConfig("gamma"));

		states = new double[gridSizeHeight][gridSizeLength];		
		
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
		
		//Repeat until utility values don't change more than delta
		while(!isConverged){
			isConverged = true;
			count++;
			
			//For each State
			for(int i=0; i<gridSizeHeight; i++){
				for(int j=0; j<gridSizeLength; j++){
					
					//If Blocked state ship the computation
					if((j == 1 || j == 3) && (i == 3)){
						continue;
					}
					
					//Calculate Utility Value
					utilVal = getUtilVal(i ,j);
					
					//Get Current utility value for state and action
					currUtilVal = states[i][j];
					
					//Check for Error threshold
					if(Math.abs(currUtilVal - utilVal) > delta){
						isConverged = false;
						states[i][j] = utilVal;
					}
				}
			}
		}
		displayResults();
	}
	
	//Calculate Utility Value using Bellman Equation
	public static double getUtilVal(int x, int y){
		double stateReward = reward[x][y] + timeStepReward;
		double maxVal = Double.NEGATIVE_INFINITY;
		double uVal = 0.0;
		double transVal = 0.0;
		
		//Compute for all possible actions
		for(int j=0; j<numberOfPossActions; j++){
			 TransitionModel trn = getTransitionModel(actions[j]);
			 transVal = 0.0;
			 
			 //Sum over the all possible values according to Transition Model
			 for(int k=0; k<numberOfPossActions; k++){
				 int nextX = getNextXcord(x, actions[k]);
				 int nextY = getNextYcord(y, actions[k]);
				 
				 //Check for blocked states
				 if((nextY == 1 || nextY == 3) && (nextX == 3)){
					 transVal += trn.getTranProb(actions[k])*states[x][y];
				 }
				 else{
					 transVal += trn.getTranProb(actions[k])*states[nextX][nextY];
				 }
				 
			 }
			 
			 //Calculate utility value using discount factor
			 uVal = stateReward + (gamma * transVal);
			 
			 //select the maximum value
			 if(uVal > maxVal){
				 maxVal = uVal;
			 }
		}
		
		return maxVal;
		
	}
	
	//Get X coordinate of next state for given action
	public static int getNextXcord(int x, String action){
		if(action.equalsIgnoreCase("up")){
			if(x-1 >= 0){
				return x-1;
			}
			else{
				return x;
			}
		}
		else if(action.equalsIgnoreCase("down")){
			if(x+1 <= gridSizeHeight-1){
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
	
	//Get Y coordinate of next state for given action
	public static int getNextYcord(int y, String action){
		if(action.equalsIgnoreCase("left")){
			if(y-1 >= 0){
				return y-1;
			}
			else{
				return y;
			}
		}
		else if(action.equalsIgnoreCase("right")){
			if(y+1 <= gridSizeLength-1){
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
	
	//Get the transitional model for given action
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
	
	//Print results
	public static void displayResults(){
		System.out.println("ValueIteration Output after "+count+" iterations : \n");
		System.out.println("Configuration : ");
		System.out.println("gridSizeLength : "+gridSizeLength+"  gridSizeHeight : "+gridSizeHeight);
		System.out.println("Delta : "+delta+"\tGamma(Discount Factor) : "+gamma+"\tTimeStepReward : "+timeStepReward);
		System.out.println("========================================");
		for(int i=0; i<gridSizeHeight; i++){
			for(int j=0; j<gridSizeLength; j++){
				System.out.format(" %8.2f",states[i][j]);
			}
			System.out.println();
		}
		System.out.println("========================================");
	}
}
