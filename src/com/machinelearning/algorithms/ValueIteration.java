package com.machinelearning.algorithms;

import java.awt.GridBagConstraints;

import com.utilities.Config;
import com.utilities.State;

public class ValueIteration {
	private static int gridSize;
	
	public static void main(String args[]){
		//Get size of the grid
		gridSize = Integer.parseInt(Config.readConfig("gridSize"));
		State states[] = new State[gridSize*gridSize]; 
		
		//Create n*n states
		for(int i=0; i<gridSize*gridSize; i++){
				states[i] = new State((Integer)i/gridSize, (Integer)i%gridSize);
		}
		
	}
	
}
