import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GradientDescent {

	private List<Double> w1 = new ArrayList<>();
	private List<Double> w2 = new ArrayList<>();
	private static final double H = 0.001;	
	private static final int max = 1;
	private static final int min = -1;

	public void train(List<Data> trainSet) {
		Random rand = new Random();
		for (int i = 0; i < trainSet.size(); i++) {
			w1.add(rand.nextDouble() * (max - min) + min);
			w2.add(rand.nextDouble() * (max - min) + min);
		}
		
		double network_error = 1000;
        while(network_error > 0.236){
            computeOutput(trainSet);
            for(int j = 0; j <trainSet.size();j++) {
                double error = trainSet.get(j).getCategory() - trainSet.get(j).getPredictedCategory();
                network_error = sigmoidFunction(trainSet.get(j).getPredictedOutput()) * (1 - sigmoidFunction(trainSet.get(j).getPredictedOutput()));
                w1.add(j, H * error * network_error * trainSet.get(j).getValue1());
                w2.add(j, H * error * network_error * trainSet.get(j).getValue2());
            }
        }
    }
	
	private void computeOutput(List<Data> input) {
       for(int i = 0; i<input.size();i++){
            double input1 = input.get(i).getValue1() * w1.get(i);
            double input2 = input.get(i).getValue2() * w2.get(i);
            input.get(i).setPredictedOutput(sigmoidFunction(input1 + input2));
            input.get(i).setPredictedCategory(computeOutputLabel(sigmoidFunction(input1 + input2)));
        }
    }
	
	public void test(List<Data> testSet) {
        computeOutput(testSet);
    }
	
	private int computeOutputLabel(double output) {
        if (output >= 0.5) 
        	return 1;
        return 0;
    }
	
	 private double sigmoidFunction(double x) {
	        return 1 / (1 + Math.exp(-x));
	 }
}
