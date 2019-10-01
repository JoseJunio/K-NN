package application.functions;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import application.algorithms.KNN;

public class Functions {

	private static Double pow = 2.0;
	private static List<Double> auxiliar;
	private static List<Double> line;
	
	public static NormalizedData zScore(Vector<List> dados, List<Double> averages, List<Double> standardDeviation, boolean normalized){
		
		Vector<List> normalizedKNN = new Vector<List>();
						
		List<Double> tmp = new ArrayList<Double>();
		
		for(int j=0; j < KNN.samples; j++) {
			
			tmp = new ArrayList<Double>();
			line = dados.get(j);
			
			for(int k=0; k < averages.size(); k++) {
				tmp.add(((Double)line.get(k)- averages.get(k))/standardDeviation.get(k));
			}
			tmp.add((Double) line.get(line.size()-1));
			normalizedKNN.add(tmp);
		}
		
		return new NormalizedData(normalized ? normalizedKNN : dados);
	}
	
	public static Double euclidianDistance(List<Double> inputData, List<Double> sample) {
		
		Double cont = 0.0;
		
		for(int i=0; i < inputData.size()-1; i++) {
			cont += (Math.pow(inputData.get(i) - sample.get(i), 2));
		}
		
		return Math.sqrt(cont);
	}
	
	public static List<Double> average(Vector<List> dados) {

		auxiliar = new ArrayList();
		
		for(int i=0; i < KNN.samples; i++) {
			for(int j=0; j < KNN.attributes; j++) {
				line = dados.get(i);
				
				if(i == 0) {
					auxiliar.add((Double)line.get(j));
				} else {
					auxiliar.set(j, auxiliar.get(j) + (Double)line.get(j));
				}
				
			}	
		}
		
		return calculateAux(dados, false);
	}
	
	public static List<Double> variance(Vector<List> dados, List<Double> averages) {
		
		auxiliar = new ArrayList();
		
		for(int i=0; i < KNN.samples; i++) {
			for(int j=0; j < KNN.attributes; j++) {
				line = dados.get(i);
				
				if(i == 0) {
					auxiliar.add(Math.pow((Double) line.get(j) - averages.get(j), pow));
				} else {
					auxiliar.set(j, auxiliar.get(j) + Math.pow((Double)line.get(j) - averages.get(j), pow));
				}
			}	
		}
		
		return calculateAux(dados, true); 
	} 
	
	public static List<Double> standardDeviation(Vector<List> dados, List<Double> averages){
		List<Double> variances = variance(dados, averages);
		
		List<Double> standartDeviation = new ArrayList();
	
		for(int i=0; i < variances.size(); i++) {
			standartDeviation.add(Math.sqrt(variances.get(i)));
		}
		
		return standartDeviation;
	}
	
	public static Vector<List> convertStringToDouble(Vector<List> dados) {
		
		Vector<List> tmp = new Vector<List>();
		
		for(int i=0; i < KNN.samples; i++) {
			List newList = new ArrayList();
			for(int j=0; j < dados.get(i).size(); j++) {
				newList.add(Double.parseDouble(dados.get(i).get(j).toString()));
			}
			tmp.add(newList);
		}
		
		return tmp;
	} 
	
	public static List<Double> calculateAux(Vector<List> dados, boolean isVariance){
		
		for(int i=0; i<auxiliar.size(); i++) {
			Double average = auxiliar.get(i);
			
			if(average != 0.0) {
				if(isVariance) {
					auxiliar.set(i, average / (dados.size()-1.0));
				} else {
					auxiliar.set(i, average / dados.size());
				}
			}
		}
		return auxiliar;
	}

	public static class NormalizedData{
		private Vector<List> normalizedKNN;
		
		public NormalizedData(Vector<List> normalizedKNN) {
			this.normalizedKNN = normalizedKNN;
		}
		
		public Vector<List> getNormalizedKNN() {
			return normalizedKNN;
		}
		
	}

}
