package com.hac;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class HierarchicalClustering {
	private final static int K = 2;
	private final static int N = 500;
	private final static int D = 2;
	private static double[][] C = new double[N][D];
	private static int[] category = new int[N];
	private static int[] I = new int[N];
	private static HashMap<Integer,ArrayList<Integer>> clusters = new HashMap<>();
	private static ArrayList<Integer> countPlus = null;
	private static ArrayList<Integer> countMinus = null;
	private static int sumPos;
	private static int sumNeg;

	public static void main(String[] args) throws IOException {
		if(args.length == 0){
			System.out.println("Missing input files");
			System.exit(1);
		}
		
		for (String file : args) {
			readFile(file);
			hc();
		}
	}
	
	private static void readFile(String filename) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		
		String line = null;
		int counter = 0;
		while((line = reader.readLine()) != null)
		{
			String[] args = line.split(" ");
			C[counter][0] = Double.parseDouble(args[1]);
			C[counter][1] = Double.parseDouble(args[2]);
			category[counter] = Integer.parseInt(args[0]);
			I[counter] = 1;
			counter++;	
			
			if(Integer.parseInt(args[0]) > 0)
				sumPos++;
			else 
				sumNeg++;
		}
	}
	
	private static void hc() {
		int remainingClusters = N;
		
		while (remainingClusters >= K) {
			int minPosI = -1;
			int minPosJ = -1;
			double minDist = 1000;
			for (int i = 0; i < N; i++) {
				if (I[i] == 1) {
					for (int j = i + 1; j < N; j++) {
						if (I[j] == 1) {
							double dist = euclideanDistance(i, j);
							if (dist < minDist) {
								minDist = dist;
								minPosI = i;
								minPosJ = j;
							}
						}
					}
				}
			}
			
			ArrayList<Integer> tmp = new ArrayList<>();
			if(clusters.containsKey(minPosJ)) {
				tmp.addAll(clusters.get(minPosJ));
				tmp.add(minPosJ);
				if(clusters.containsKey(minPosI))
					tmp.addAll(clusters.get(minPosI));
			} else {
				tmp.add(minPosJ);
				if(clusters.containsKey(minPosI)) 
					tmp.addAll(clusters.get(minPosI));
			}
			
			clusters.put(minPosI, tmp);
			
			if(remainingClusters <= 4 && remainingClusters >= 2)
			{
				System.out.println("Number of Clusters : " + remainingClusters);
				purity();
				fMeasure();
			}
			
			clusters.remove(minPosJ);
			I[minPosJ] = 0;
			remainingClusters--;
		}
	}
	
	private static double euclideanDistance(int i, int j)
	{
		double x = Math.pow((C[i][0] - C[j][0]),2);
		double y = Math.pow((C[i][1] - C[j][1]),2);
		return x + y;
	}
	
	private static void purity()
	{
		double sumMax = 0;
		int i=0;
		
		countPlus = new ArrayList<>();
		countMinus = new ArrayList<>();
		
		for(ArrayList<Integer> list : clusters.values())
		{
			int tempPlus = 0;
			int tempMinus = 0;
			
			for(Integer key : list)
			{
				if(category[key] > 0)
					tempPlus++;
				else
					tempMinus++;
			}
			
			if(tempPlus >= tempMinus)
				sumMax += tempPlus;
			else
				sumMax += tempMinus;
			
			countPlus.add(i,tempPlus);
			countMinus.add(i,tempMinus);
			i++;
		}
		
		System.out.println("Purity is : " + sumMax/N);
	}
	
	private static void fMeasure()
	{
		double f = 0;
		
		for (int i = 0; i < K; i++) 
		{
			double precision = 0;
			double recall = 0;
			
			if (countPlus.get(i) > countMinus.get(i)) {
				precision = countPlus.get(i) / (double)(countPlus.get(i) + countMinus.get(i));
				recall = countPlus.get(i) / (double)sumPos;
			} else {
				precision = countMinus.get(i) / (double)(countPlus.get(i) + countMinus.get(i));
				recall = countMinus.get(i) / (double)sumNeg;
			}
			
			f += 2 / ((1 / precision) + (1 / recall));
		}
		
		System.out.println("F-measure is : " + f);
	}

}
