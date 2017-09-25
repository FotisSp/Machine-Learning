import java.util.List;

public class LeastSquares {
	private double a;
	private double b;
	
	public void train(List<Data> trainSet) {
		double sumx = 0, sumy = 0, sumxy = 0;
		
		for(Data d : trainSet)
		{
			sumx += d.getValue1();
			sumy += d.getValue2();
			sumxy += d.getValue1() * d.getValue2();
		}
		
		double meanx = sumx / trainSet.size();
		double meany = sumy / trainSet.size();
		
		a = (sumxy - meany * sumx) / Math.pow(sumx,2) - meanx * sumx;
		b = (meany * Math.pow(sumx, 2) - meanx * sumxy) / Math.pow(sumx, 2) - meanx * sumx;
		
		for(Data d : trainSet)
		{
			double prediction = d.getValue1() * a + b;
			if(prediction >= 0) 
				d.setPredictedCategory(1);
			else
				d.setPredictedCategory(0);
		}
	}
	
	public void test(List<Data> testSet) {
		for(Data d : testSet)
		{
			double prediction = d.getValue1() * a + b;
			if(prediction >= 0) 
				d.setPredictedCategory(1);
			else
				d.setPredictedCategory(0);
		}
	}
}
