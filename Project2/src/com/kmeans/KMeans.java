package com.kmeans;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class KMeans {
	private static final int K = 3;
	private static ArrayList<String> filenames = new ArrayList<>();
	private static ArrayList<Point> setOfPoints = new ArrayList<>();
	private static ArrayList<Point> centers = new ArrayList<>();
	private static ArrayList<Integer> countPlus = new ArrayList<>();
	private static ArrayList<Integer> countMinus = new ArrayList<>();
	private static int sumPos = 0;
	private static int sumNeg = 0;
	
	public static void main(String[] args) throws IOException {
		if(args.length == 0)
		{
			System.err.println("Specify file names");
			System.exit(1);
		}
		filenames.addAll(Arrays.asList(args));
		
		for(String file : filenames)
		{
				System.out.println("*** File : " + file + " ***");
				readFile(file);
				assignCenters();
				kMeans();
				writeCentersToFile(file);
				purity();
				fMeasure();
				System.out.println("*** *** ***");
				System.out.println();
				centers.clear();
				setOfPoints.clear();
		}
	}
	
	private static void readFile(String filename) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		
		String line = null;
		while((line = reader.readLine()) != null)
		{
			String[] args = line.split(" ");
			Point p = new Point(args);
			setOfPoints.add(p);
			
			if(Integer.parseInt(args[0]) > 0)
				sumPos++;
			else 
				sumNeg++;
		}
	}
	
	private static void assignCenters()
	{
		Random rand = new Random();

		for(int i=0; i<K; i++)
		{
			int r = rand.nextInt(setOfPoints.size());
			Point p = new Point(setOfPoints.get(r).getX(), setOfPoints.get(r).getY());
			centers.add(p);
		}
	}
	
	private static void kMeans()
	{
		ArrayList<Double> sumX = new ArrayList<>();
		ArrayList<Double> sumY = new ArrayList<>();
		ArrayList<Integer> count = new ArrayList<>();
		ArrayList<Point> previousCenters = new ArrayList<>();
		double min;
		
		for (int i = 0; i < K; i++) {
			sumX.add(0.0);
			sumY.add(0.0);
			count.add(0);
		}
		
		whileloop:
		while (true) {
			for (Point p : setOfPoints) {
				min = 1000;
				for (int i = 0; i < K; i++) {
					double dist = euclideanDistance(p, centers.get(i));
					if (dist < min) {
						min = dist;
						p.setPredictedCenter(i);
					}
				}

				int i = p.getPredictedCenter();
				double x = sumX.get(i) + p.getX();
				sumX.set(i, x);
				double y = sumY.get(i) + p.getY();
				sumY.set(i, y);
				count.set(i, count.get(i) + 1);
			}
			
			previousCenters = new ArrayList<>(centers);
			
			for (int i = 0; i < K; i++) {
				Point p = new Point(sumX.get(i) / count.get(i), sumY.get(i) / count.get(i));
				centers.set(i, p);
			} 
			
			for (int i = 0; i < K; i++) {
				if(previousCenters.get(i).compareCenters(centers.get(i)))
					break whileloop;
			} 
		}
	}
	
	private static void writeCentersToFile(String file) throws IOException
	{
		String[] name = file.split("\\.");
		BufferedWriter writer = new BufferedWriter(new FileWriter(name[0] + "_centers.txt"));
		
		for(Point c : centers)
		{
			writer.write(c.toString() + "\n");
			writer.flush();
		}
		
		BufferedWriter writer2 = new BufferedWriter(new FileWriter(name[0] + "_data.txt"));
		
		for(Point d : setOfPoints)
		{
			writer2.write(d.toString() + "\n");
			writer2.flush();
		}
		
		writer.close();
		writer2.close();
	}
	
	private static double euclideanDistance(Point data, Point center)
	{
		double x = Math.pow((data.getX() - center.getX()),2);
		double y = Math.pow((data.getY() - center.getY()),2);
		return x + y;
	}
	
	public static void purity()
	{
		double sumMax = 0;
		for(int i=0; i<K; i++)
		{
			int tempPlus = 0;
			int tempMinus = 0;
			for(Point p : setOfPoints)
			{
				if(p.getPredictedCenter() == i)
				{
					if(p.getCategory() > 0)
						tempPlus++;
					else
						tempMinus++;
				}
			}
			if(tempPlus >= tempMinus)
				sumMax += tempPlus;
			else
				sumMax += tempMinus;
			
			countPlus.add(i,tempPlus);
			countMinus.add(i,tempMinus);
		}
		
		System.out.print("Purity is : " + sumMax/setOfPoints.size());
	}
	
	public static void fMeasure()
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
		
		System.out.println("\tF-measure is : " + f);
	}
}