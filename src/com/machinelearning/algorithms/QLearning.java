package com.machinelearning.algorithms;
/*
 * Authors : Aniket Bhosale and Mayur Tare
 * 
 * Description :
 * Class to implement Reinforcement learning using Q-Learning 
 */

import java.util.HashMap;

import com.utilities.Config;
import com.utilities.State;

public class QLearning {

	private static int m = Integer.parseInt(Config.readConfig("QgridSizeHeight"));
	private static int n = Integer.parseInt(Config.readConfig("QgridSizeLength"));
	private static State states[] = new State[m*n];
	private static int startX =Integer.parseInt(Config.readConfig("startX"));
	private static int startY =Integer.parseInt(Config.readConfig("startY"));
	private static int goalX =Integer.parseInt(Config.readConfig("goalX"));
	private static int goalY = Integer.parseInt(Config.readConfig("goalY"));
	private static int numberOfPossibleActions = Integer.parseInt(Config.readConfig("numberOfPossActions"));
	private static int QLearingIterations = Integer.parseInt(Config.readConfig("QLearingIterations"));
	private static int timeStepReward = Integer.parseInt(Config.readConfig("QtimeStepReward"));
	private static boolean epsilonReduceFlag = Boolean.parseBoolean(Config.readConfig("epsilonReduceFlag"));
	
	
	public static double alpha = 0.1;
	public static double gamma = 0.9;
	public static double epsilon = 0.5;
	public static int epsilonChangePolicy = 10;
	
	//Reward Matrix for given grid
	public static int[][] reward = new int[][]
			{{0 , 0 , 0 , 0 , 0},
			{0 , 0 , 0 , -50,0},
			{0 , 0 , 0 , 0, 0},
			{0 ,0 , 0 , 0, 10}};
	
	//HashMap to save the states and their Q values
	public static HashMap<State, double[]> Qvalues = new HashMap<State,double[]>();
	
	//Possible Actions
	public static String Actions[] = {"UP","DOWN","LEFT","RIGHT"};
	
	
	
	//Initializations
	public static void intialize (State states []){
		//Create n*n states
		for(int i=0; i<m*n; i++){
			states[i] = new State((Integer)i/m, (Integer)i%m);
			//Initialize Qvalues
			Qvalues.put(states[i], new double[4]);
		}			
	}
	
	//Check if goal state is reached or not
	public static boolean episodeEnd(State curState){
		return (curState.getXCord() == goalX && curState.getYCord() == goalY);
	}
	
	//Action Selection using Epsilon Greedy policy
	public static int selectAction(State currState){
		
		int selectedAction = -1;
		double maxQVal = -Double.MAX_VALUE; 
		
		//Random action
		if(Math.random() < epsilon){
			selectedAction = (int) (Math.random() * numberOfPossibleActions);
			
		}
		//Max QValue action
		else{
			for(int act = 0; act < numberOfPossibleActions; act++){
				double Qval = Qvalues.get(currState)[act];
				if(Qval > maxQVal){
					selectedAction = act;
					maxQVal = Qval;
				}
			}
		}
		
		return selectedAction;
	}
	
	//Get the co-ordinates of next state for the given action
	public static State getNextState(State cState, String a){
		State nState = cState;
		int xCordinate = cState.getXCord();
		int yCordinate = cState.getYCord();	
		
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
	
	//Get Maximum possible Q value for the given state
	public static double getMaxOfNewSate(State newS){
		double max = -Double.MAX_VALUE;
		for (int i = 0; i<4;i++){
			//System.out.println(newS.toString());
			 if(Qvalues.get(newS)[i] > max)
				 max = Qvalues.get(newS)[i];
				
		}
		return max;
	}
	
	
	public void solveQLearning(){ 
		
		//Set Initial Q values
		intialize(states);		
				
		//Compute Q values for 5000 epsilonChangePolicy
		for (int i = 0; i < QLearingIterations ; i++){
			//Set start state
			State startState = new State(startX,startY);
			State currentState = startState;
			boolean flag = false; 
			
			//Continue the episode until Q values for goal state have been computed
			while(!flag){
				State newState = null;
				double difference = 0.0;
				double [] finalQ = Qvalues.get(currentState);
				
				//Select action using epsilon greedy policy
				int actionIndex =  selectAction(currentState);
				
				//Get next state after executing selected action
				newState = getNextState(currentState,Actions[actionIndex]);
				
				//Check for Blocked states
				if((newState.getXCord() == 1 || newState.getXCord() == 3) && newState.getYCord() == 1){
					newState = currentState;
				}
				
				//Get Maximum possible Q value for the new state
				double QMax = getMaxOfNewSate(newState);
				
				//Calculate Q value for current state and selected action
				difference = alpha * ((reward[currentState.getXCord()][currentState.getYCord()] + timeStepReward) + (gamma * QMax) - Qvalues.get(currentState)[actionIndex]);
				finalQ[actionIndex] = Qvalues.get(currentState)[actionIndex] + difference;
				Qvalues.put(currentState, finalQ);
				
				//Check if the episode has ended or not
				if(episodeEnd(currentState))
					flag = true;
				
				currentState = newState;
				
			}
			
			//Epsilon can be updated after every 10 epsilonChangePolicy
			if(i%epsilonChangePolicy == 0 && i>1 && epsilonReduceFlag){
				epsilon = epsilon/(1+epsilon);
			}
		}
		
		displayResults();
	}
	
	//Print the results
	public static void displayResults(){
		System.out.println("Q-Learning output for "+5000+" Iterations\n");
		System.out.println("Configuration : ");
		System.out.println("gridSizeLength : "+n+"  gridSizeHeight : "+m);
		System.out.println("Alpha : "+alpha+"\tEpsilon : "+epsilon+"\tGamma : "+gamma+"\t TimeStepReward : "+timeStepReward+"\n");
		System.out.println("===========================================");
		System.out.format(" %-8s %-8s %-8s %-8s %-8s%n","State","UP","DOWN","LEFT","RIGHT");
		System.out.println("-------------------------------------------");
		for (int i = 0; i< m*n;i++){
			System.out.format(" %-8s %-8.2f %-8.2f %-8.2f %-8.2f%n",states[i].toString(), Qvalues.get(states[i])[0], 
					Qvalues.get(states[i])[1], Qvalues.get(states[i])[2], Qvalues.get(states[i])[3]);
			System.out.println("-------------------------------------------");
		}
		System.out.println("===========================================");
	}
}
