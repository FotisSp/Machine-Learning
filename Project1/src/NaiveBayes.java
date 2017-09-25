import java.util.List;

public class NaiveBayes {
	
	private double meanCat0;
	private double stdDevCat0;
	private double meanCat1;
	private double stdDevCat1;

	public void train(List<Data> trainSet)
	{
		double sumCat0 = 0.0;
		int countCat0 = 0;
		
		double sumCat1 = 0.0;
		int countCat1 = 0;

		for(Data d : trainSet)
		{
			if(d.getCategory() == 0)
			{
				sumCat0 += d.getValue1();
				sumCat0 += d.getValue2();
				countCat0++;
			}
			else
			{
				sumCat1 += d.getValue1();
				sumCat1 += d.getValue2();
				countCat1++;
			}
		}
		meanCat0 = sumCat0 / countCat0;
		meanCat1 = sumCat1 / countCat1;

		double varCat0 = 0.0;
		double varCat1 = 0.0;
		
		for(Data d : trainSet)
		{
			if(d.getCategory() == 0)
			{
				varCat0 += Math.pow((d.getValue1()-meanCat0),2);
				varCat0 += Math.pow((d.getValue2()-meanCat0),2);
			}
			else
			{
				varCat1 += Math.pow((d.getValue1()-meanCat1),2);
				varCat1 += Math.pow((d.getValue2()-meanCat1),2);
			}
		}
		
		stdDevCat0 = Math.sqrt(varCat0/countCat0);
		stdDevCat1 = Math.sqrt(varCat1/countCat1);
		
		for(Data d : trainSet)
		{
			double probCat0Value1 = probabilityDensityFunction(meanCat0, stdDevCat0, d.getValue1());
			double probCat0Value2 = probabilityDensityFunction(meanCat0, stdDevCat0, d.getValue2());
			double probCat1Value1 = probabilityDensityFunction(meanCat1, stdDevCat1, d.getValue1());
			double probCat1Value2 = probabilityDensityFunction(meanCat1, stdDevCat1, d.getValue2());
			d.setPredictedCategory(findCategory(probCat1Value1, probCat0Value1, probCat1Value2, probCat0Value2));
		}
		
	}
	
	public void test(List<Data> testSet)
	{
		for(Data d : testSet)
		{
			double probCat0Value1 = probabilityDensityFunction(meanCat0, stdDevCat0, d.getValue1());
			double probCat0Value2 = probabilityDensityFunction(meanCat0, stdDevCat0, d.getValue2());
			double probCat1Value1 = probabilityDensityFunction(meanCat1, stdDevCat1, d.getValue1());
			double probCat1Value2 = probabilityDensityFunction(meanCat1, stdDevCat1, d.getValue2());
			d.setPredictedCategory(findCategory(probCat1Value1, probCat0Value1, probCat1Value2, probCat0Value2));
		}
	}
	
	private int findCategory(double probCat1Value1, double probCat0Value1,double probCat1Value2, double probCat0Value2) {
        double probabilityCat1 = probCat1Value1 * probCat1Value2;
        double probabilityCat0 = probCat0Value1 * probCat0Value2;

        if(probabilityCat1 > probabilityCat0)
            return 1;

        return 0;
    }
	
	 private double probabilityDensityFunction(double mean,double std, double value) {
		 return 1 / Math.sqrt(2 * Math.PI * std) * Math.exp(- Math.pow(value - mean, 2)/ (2 *Math.pow(std, 2)));
	 }

}
