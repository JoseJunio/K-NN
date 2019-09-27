package application.functions;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Functions {

	private static Double pow = 2.0;
	private static List<Double> auxiliar;

	public static NormalizedData zScore(Vector<List> dados, List<Double> averages, List<Double> standardDeviation, List<Double> inputData){
		
		Vector<List> normalizedKNN = new Vector<List>();
		List<Double> normalizedInputData = new ArrayList<Double>(); 
		
		
		for(int i=0; i<averages.size(); i++) {
			normalizedInputData.add((inputData.get(i) - averages.get(i))/standardDeviation.get(i));
		}
		
		List<Double> tmp = new ArrayList<Double>();
		
		for(int j=0; j < dados.size()-1; j++) {
			tmp = new ArrayList<Double>();
			for(int k=0; k<averages.size(); k++) {
				tmp.add(((Double)dados.get(j).get(k)- averages.get(k))/standardDeviation.get(k));
			}
			tmp.add((Double)dados.get(j).get(dados.get(j).size()-1));
			normalizedKNN.add(tmp);
		}
			
		return new NormalizedData(normalizedKNN, normalizedInputData);
	}
	
	public static Double euclidianDistance(List<Double> inputData, List<Double> sample) {
		
		Double cont = 0.0;
		
		for(int i=0; i<inputData.size()-1; i++) {
			cont += Math.pow(inputData.get(i) - sample.get(i), 2);
		}
		
		return Math.sqrt(cont);
	}
	
	public static List<Double> average(Vector<List> dados) {

		auxiliar = new ArrayList();
		
		for(int i=0; i < dados.size(); i++) {
			for(int j=0; j<dados.get(i).size()-1; j++) {
				
				if(i == 0) {
					auxiliar.add((Double)dados.get(i).get(j));
				} else {
					auxiliar.set(j, auxiliar.get(j) + (Double)dados.get(i).get(j));
				}
				
			}	
		}
		
		for(int i=0; i<auxiliar.size(); i++) {
			Double average = auxiliar.get(i);
			
			if(average != 0.0) {
				auxiliar.set(i, average / dados.size());
			}
		}
		
		return auxiliar;
		
		//return calculateAux(dados);
	}
	
	public static List<Double> variance(Vector<List> dados, List<Double> averages) {
		
		auxiliar = new ArrayList();
		
		for(int i=0; i < dados.size(); i++) {
			for(int j=0; j<dados.get(i).size()-1; j++) {
				
				if(i == 0) {
					auxiliar.add(Math.pow((Double)dados.get(i).get(j) - averages.get(j), pow));
				} else {
					auxiliar.set(j, auxiliar.get(j) + Math.pow((Double)dados.get(i).get(j) - averages.get(j), pow));
				}
			}	
		}
		
		return calculateAux(dados); 
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
		
		for(int i=0; i < dados.size(); i++) {
			List newList = new ArrayList();
			for(int j=0; j<dados.get(i).size(); j++) {
				newList.add(Double.parseDouble(dados.get(i).get(j).toString()));
			}
			tmp.add(newList);
		}
		
		return tmp;
	} 
	
	public static List<Double> calculateAux(Vector<List> dados){
		
		for(int i=0; i<auxiliar.size(); i++) {
			Double average = auxiliar.get(i);
			
			if(average != 0.0) {
				auxiliar.set(i, average / (dados.size()-1.0));
			}
		}
		
		return auxiliar;
		
	}

	public static class NormalizedData{
		private Vector<List> normalizedKNN;
		private List<Double> normalizedInputData;
		
		public NormalizedData(Vector<List> normalizedKNN, List<Double> normalizedInputData) {
			this.normalizedKNN = normalizedKNN;
			this.normalizedInputData = normalizedInputData;
		}
		
		public Vector<List> getNormalizedKNN() {
			return normalizedKNN;
		}
		
		public List<Double> getNormalizedInputData() {
			return normalizedInputData;
		}
		
	}

}
