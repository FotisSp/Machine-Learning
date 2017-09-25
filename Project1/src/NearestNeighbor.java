
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class NearestNeighbor {
	public Data[] findKNearestNeighbors(Data[] trainingSet, Data testRecord,int K){
		int NumOfTrainingSet = trainingSet.length;
		
		Data[] neighbors = new Data[K];
		
		int index;
		for(index = 0; index < K; index++){
			trainingSet[index].setDistance(getDistance(trainingSet[index], testRecord));
			neighbors[index] = trainingSet[index];
		}
		
		for(index = K; index < NumOfTrainingSet; index ++){
			trainingSet[index].setDistance(getDistance(trainingSet[index], testRecord));
			
			int maxIndex = 0;
			for(int i = 1; i < K; i ++){
				if(neighbors[i].getDistance() > neighbors[maxIndex].getDistance())
					maxIndex = i;
			}
			
			if(neighbors[maxIndex].getDistance() > trainingSet[index].getDistance())
				neighbors[maxIndex] = trainingSet[index];
		}
		
		return neighbors;
	}
	
	public int classify(Data[] neighbors){
		HashMap<Integer, Double> map = new HashMap<Integer, Double>();
		int num = neighbors.length;
		
		for(int index = 0;index < num; index ++){
			Data temp = neighbors[index];
			int key = temp.getCategory();
		
			if(!map.containsKey(key))
				map.put(key, 1 / temp.getDistance());
			
			else{
				double value = map.get(key);
				value += 1 / temp.getDistance();
				map.put(key, value);
			}
		}	
		
		double maxSimilarity = 0;
		int returnLabel = -1;
		Set<Integer> labelSet = map.keySet();
		Iterator<Integer> it = labelSet.iterator();
		
		while(it.hasNext()){
			int label = it.next();
			double value = map.get(label);
			if(value > maxSimilarity){
				maxSimilarity = value;
				returnLabel = label;
			}
		}
		
		return returnLabel;
	}
	
	private static double getDistance(Data s, Data e) {
		double sumx = 0, sumy = 0;
		
		sumx = Math.pow(s.getValue1() - e.getValue1(), 2);
		sumy = Math.pow(s.getValue2() - e.getValue2(), 2);
		
		return Math.sqrt(sumx + sumy);
	}
}
