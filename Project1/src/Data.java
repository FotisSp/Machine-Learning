public class Data {
	private int category ;
	private double value1;
	private double value2;
	private double predictedCategory;
	private double predictedOutput;
	private double distance;
	
	public Data(String[] data) {
		value1 = Double.parseDouble(data[0]);
		if(data[1].isEmpty())
			value2 = Double.parseDouble(data[2]);
		else
			value2 = Double.parseDouble(data[1]);
		category = Integer.parseInt(data[data.length-1]);
	}
	
	public String getDataLine() {
		return value1+" "+value2+" "+category;
	}
	
	public int getCategory()
	{
		return category;
	}
	
	public double getValue1() {
		return value1;
	}

	public double getValue2() {
		return value2;
	}

	public double getPredictedCategory() {
		return predictedCategory;
	}
	
	public double getDistance() {
		return distance;
	}

	public void setPredictedCategory(double calculatedK) {
		this.predictedCategory = calculatedK;
	}
	
	public double getPredictedOutput() {
		return predictedOutput;
	}

	public void setPredictedOutput(double calculatedOutput) {
		this.predictedOutput = calculatedOutput;
	}
	
	public void setDistance(double distance) {
		this.distance = distance;
	}
}
