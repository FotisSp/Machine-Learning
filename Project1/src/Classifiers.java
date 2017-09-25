import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Classifiers {

	private static int high;
	private static HashMap<Integer, List<Data>> data = new HashMap<Integer, List<Data>>();
	private final static int LOW = 0;


	public static void main(String[] args) throws IOException {
		//createDatasets("Synthetic.dat",10);	//First Dataset.
		createDatasets("clouds.dat",10);		//Second Dataset.
		
		System.out.println("Finished Creating datasets");
		
		runNearestNeighbor();
		runNaiveBayes();
		runLeastSquares();
		runGradientDescent();
	}

	private static void runLeastSquares() {
		LeastSquares ls = new LeastSquares();
		
		for(int i = 0;i<data.size()-1;i++)
		{
			ls.train(data.get(i));
		}
		ls.test(data.get(9));
		
		
		findResults("Least Squares");
	}

	private static void runGradientDescent() {
		GradientDescent gd = new GradientDescent();
		
		for(Integer i : data.keySet())
		{				
			gd.train(data.get(i));
			for(Integer j : data.keySet())
			{
				if(i!=j)
					gd.test(data.get(j));
			}
		}

		findResults("Gradient Descent");
	}

	private static void runNaiveBayes() {
		NaiveBayes nb = new NaiveBayes();
		
		for(Integer i : data.keySet())
		{
			nb.train(data.get(i));
			for(Integer j : data.keySet())
			{
				if(i!=j)
					nb.test(data.get(j));
			}
		}
		
		findResults("Naive Bayes");
	}
	
	public static void createDatasets(String filename, int numOfPartitions) throws IOException {
		BufferedReader in = null;
		Random r = new Random();
		int datasetNumber;
		high = numOfPartitions;

		try {
			in = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e) {
			System.out.println("File " + filename + " not found!");
			System.exit(0);
		}

		String line;
		while ((line = in.readLine()) != null) {
			String[] seperatedLine = line.trim().split(" ");
			datasetNumber = r.nextInt(high - LOW) + LOW;

			Data tmpData = new Data(seperatedLine);
			if (data.containsKey(datasetNumber)) {
				data.get(datasetNumber).add(tmpData);
			} else
				data.put(datasetNumber, new ArrayList<Data>(Arrays.asList(tmpData)));
		}
		in.close();
	}
	
	private static void runNearestNeighbor() {
		NearestNeighbor nn = new NearestNeighbor();
		
		System.out.println("\n*********** Nearest Neighbor ***********");
		for (int k = 1; k < 10; k++) {
			double globalAccuracy = 0;

			for (Integer i : data.keySet())
			{
				for (Integer j : data.keySet()) {
					if (i != j) {
						for (Data d : data.get(j)) {
							Data[] neighbors = nn.findKNearestNeighbors(toArray(data.get(i)), d, k);
							int classLabel = nn.classify(neighbors);
							d.setPredictedCategory(classLabel); //assign the predicted label to TestRecord
						}
					
						int correctPrediction = 0;
						for (Data d : data.get(j)) {
							if(d.getPredictedCategory() == d.getCategory())
								correctPrediction ++;
						}
						
						globalAccuracy += (double)correctPrediction / data.get(j).size();
					}
				}
			} 
			System.out.printf("The average accuracy for k = " + k + " is %.2f%%\n" , globalAccuracy / 90 * 100);
		}
		System.out.println("****************************************");
	}
	
	private static Data[] toArray(List<Data> d) {
		Data[] data = new Data[d.size()];
		for(int i=0; i<d.size(); i++) {
			data[i] = d.get(i);
		}
		
		return data;
	}
	
	private static void findResults(String title) {
		int numCorrect = 0;
		int numFalse = 0;
		String t = "**** " + title + " ****";
		
		for(List<Data> value : data.values()) 
		{
			for(Data d : value)
			{
				if(d.getCategory() == d.getPredictedCategory())
					numCorrect++;
				else
					numFalse++;
			}
		}
		int size = numCorrect + numFalse;
		System.out.println("\n**** " + title + " ****");
		System.out.printf("Correct : %.2f%%\n" , (double)(numCorrect)/size * 100 );
		System.out.printf("False   : %.2f%%\n" , (double)(numFalse)/size * 100 );
		for(int i =0; i<t.length(); i++) 
			System.out.print("*");
		System.out.println();
	}	
}