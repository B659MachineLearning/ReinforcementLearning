package com.machinelearning.algorithms;

public class RL {
	public static void main(String args[]){
		QLearning ql = new QLearning();
		ql.solveQLearning();
		System.out.println("***********************************************");
		ValueIteration val = new ValueIteration();
		val.solveValueIteration();
	}
}
