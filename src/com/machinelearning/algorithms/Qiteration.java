package com.machinelearning.algorithms;

import java.util.Arrays;
import java.util.HashMap;

import com.utilities.Config;
import com.utilities.State;

public class Qiteration {

	private static int m = Integer.parseInt(Config.readConfig("m"));;
	private static int n = Integer.parseInt(Config.readConfig("n"));;
	private static int startX =Integer.parseInt(Config.readConfig("startX"));
	private static int startY =Integer.parseInt(Config.readConfig("startY"));
	private static int goalX =Integer.parseInt(Config.readConfig("goalX"));
	private static int goalY = Integer.parseInt(Config.readConfig("goalY"));
	public static double alpha = 0.1;
	public static double gamma = 0.9;
	public static double epsilon = 0.5;
	public static int Iterations = 10;
	public static int[][] reward = new int[][]
			{{0 , 0 , 0 , 0 , 0},
			{0 , 0 , 0 , -50,0},
			{0 , 0 , 0 , 0, 0},
			{0 ,0 , 0 , 0, 10}};
	public static HashMap<State, double[]> Qvalues = new HashMap<State,double[]>();
	public static String Actions[] = {"UP","DOWN","LEFT","RIGHT"};
	
	
	
	//Initializations
	public static void intialize (State states []){
		//Create n*n states
				for(int i=0; i<m*n; i++){
						states[i] = new State((Integer)i/m, (Integer)i%m);
						//Initialize Qvalues
						//System.out.println(states[i].toString());
						Qvalues.put(states[i], new double[4]);
						//System.out.println(Qvalues.get(states[i])[0]);
				}
				
				
				
	}
	
	public static boolean episodeEnd(State curState){
		return (curState.getXCord() == goalX && curState.getYCord() == goalY);
	}
	
	//Action Selection using Epsilon Greedy policy
	public static int selectAction(State currState){
		
		int selectedAction = -1;
		int possibleActions = 4;
		double maxQVal = -Double.MAX_VALUE; 
		//State tempState = new State(2,3);
		//System.out.println(Qvalues.get(currState));
		if(Math.random() < epsilon){
			selectedAction = (int) (Math.random() * possibleActions);
			
		}
		else{
			for(int act = 0; act < possibleActions; act++){
				double Qval = Qvalues.get(currState)[act];
				if(Qval > maxQVal){
					selectedAction = act;
					maxQVal = Qval;
				}
			}
		}
		
		return selectedAction;
	}
	
	public static State getNextState(State cState, String a){
		
		State nState = cState;
		int xCordinate = cState.getXCord();
		int yCordinate = cState.getYCord();
		boolean block;
		
		
		if(a == "UP"){
			if(yCordinate < m-1){
				nState = new State(xCordinate,yCordinate+1);
			}
			else
				nState = cState;
		}
		else if(a == "DOWN"){
			if(yCordinate != 0){
				nState = new State(xCordinate,yCordinate-1);
			}
			else
				nState = cState;
		}
		else if(a == "RIGHT"){
			if(xCordinate < n-1){
				nState = new State(xCordinate + 1,yCordinate);
			}
			else
				nState = cState;
		}
		else if(a == "LEFT"){
			if(xCordinate != 0){
				nState = new State(xCordinate - 1,yCordinate);
			}
			else
				nState = cState;
		}
		
		return nState;
	}

	public static double getMaxOfNewSate(State newS){
		double max = -Double.MAX_VALUE;
		for (int i = 0; i<4;i++){
			//System.out.println(newS.toString());
			 if(Qvalues.get(newS)[i] > max)
				 max = Qvalues.get(newS)[i];
				
		}
		return max;
	}
	
	public static void main(String args[]){
		
		State states[] = new State[m*n]; 
		 
		intialize(states);		
		
		
		int count = 0;
		for (int i = 0; i < 5000 ; i++){
			State startState = new State(startX,startY);
			State currentState = startState;
			boolean flag = false; 
			while(!flag){
				State newState = null;
				double difference = 0.0;
				double [] finalQ = Qvalues.get(currentState);
				
				int actionIndex =  selectAction(startState);
				//System.out.println(currentState.toString());
				//System.out.println("Actions = "+actionIndex);
				newState = getNextState(currentState,Actions[actionIndex]);
				if((newState.getXCord() == 1 || newState.getXCord() == 3) && newState.getYCord() == 1)
					newState = currentState;
				
				double QMax = getMaxOfNewSate(newState);
				//System.out.println("Qmax =" +QMax);
				
				difference = alpha * ((reward[currentState.getXCord()][currentState.getYCord()]) + (gamma * QMax) - Qvalues.get(currentState)[actionIndex]);
				//System.out.println("difference = "+difference);
				finalQ[actionIndex] = Qvalues.get(currentState)[actionIndex] + difference;
				Qvalues.put(currentState, finalQ);
				//System.out.println(Qvalues.get(currentState)[0]+", "+Qvalues.get(currentState)[1]+", "+Qvalues.get(currentState)[2]+", "+Qvalues.get(currentState)[3]);
				if(episodeEnd(currentState))
					flag = true;
				currentState = newState;
				
			}
			//Epsilon can be updated after every 10 Iterations
			/*count++;
			
			if(count == 10){
				epsilon = epsilon/(1+epsilon);
				count = 0;
			}*/
			//break;
		}
		System.out.println("=======QValues========");
		for (int i = 0; i< m*n;i++){
			System.out.println(states[i].toString() +"    -    "+Qvalues.get(states[i])[0]+", "+Qvalues.get(states[i])[1]+", "+Qvalues.get(states[i])[2]+", "+Qvalues.get(states[i])[3]+", ");
		}
	}
}
