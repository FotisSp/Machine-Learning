package com.kmeans;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Point {
	private int category;
	private double x;
	private double y;
	private int predictedCenter = -20;
	
	public Point(String[] args) {
		this.category = Integer.parseInt(args[0]);
		this.x = Double.parseDouble(args[1]);
		this.y = Double.parseDouble(args[2]);
	}	
	
	public Point(double x, double y) {
		this.category = 0;
		this.x = x;
		this.y = y;
	}
	
	public void setPredictedCenter(int predictedCenter) {
		this.predictedCenter = predictedCenter;
	}
	
	public int getPredictedCenter() {
		return predictedCenter;
	}

	public int getCategory() {
		return category;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
	public boolean compareCenters(Point center)
	{
		double xDif = Math.abs(x - center.getX());
		double yDif = Math.abs(y - center.getY());
		
		if(xDif > 0.001 && yDif > 0.001)
			return false;
		return true;
	}
	
	private double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	@Override
	public String toString() {
		return round(x,4) + " " + round(y,4);
	}
}
