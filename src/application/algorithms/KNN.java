package application.algorithms;

import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Vector;

import org.xml.sax.Parser;

import application.functions.Functions;
import application.functions.Functions.NormalizedData;

public class KNN {
	
	 public Vector<List> 	 dados =  new Vector<List>();
	 public List<Double>	 media, desvio;
	 public NormalizedData   normalizedData;
	 public List<Classifier> classifiers = new ArrayList<Classifier>();
	 
	 // Vai virar parametro de entrada - INICIO
	 public int 			 knn = 5;
	 public List<Double> input = new ArrayList<Double>();
	 // Vai virar parametro de entrada - FIM
	 
	 public KNN() {
		 loadData();
		 media = Functions.average(dados);
		 desvio = Functions.standardDeviation(dados, media);
		 
		 input.add(1.75);
		 input.add(52.0);
		 input.add(null);
		 
		 normalizedData = Functions.zScore(dados, media, desvio, input);
		 
		 System.out.println("media: " + media);
		 System.out.println("desvio: " + desvio);
		 System.out.println("Normalized KNN: " + normalizedData.getNormalizedKNN());
		 System.out.println("Normalized Input: " + normalizedData.getNormalizedInputData());
		 calculateKnn(normalizedData.getNormalizedInputData());
		 System.out.println("KNN: " + getKNN());
		 
	 
	 }
	 
	public void loadData() {
		try {
			 BufferedReader br = new BufferedReader(new FileReader("/Users/josejunio/eclipse-workspace/K-NN-bkp/src/dados.txt"));
			 while(br.ready()){
				 String linha = br.readLine();
				 List tmp = (List) Arrays.asList(linha.split("\\s+")); 
				 dados.add(tmp);
			}
			br.close();
			System.out.println(dados);
			dados = Functions.convertStringToDouble(dados);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	} 

	public List<Double> getKNN(){
		List<Double> tmp = new ArrayList<>();
		
		for(int i=0; i < knn; i++) {
			tmp.add(classifiers.get(i).getDistance());
		}
	
		return tmp;
	} 	
	
	public void calculateKnn(List<Double> input){
		
		Vector<List> tmp = normalizedData.getNormalizedKNN();
		
		for(int i=0; i<tmp.size(); i++) {
 			classifiers.add(new Classifier((Double)tmp.get(i).get(tmp.get(i).size()-1), 
 											Functions.euclidianDistance(tmp.get(i), input)));
		}
		
		Collections.sort((List<Classifier>) classifiers);

		for(Classifier c : classifiers) {
			System.out.println("classe: " + c.getClasses() + " distance: " + c.getDistance() + "\n");
		}
		
	}
	
	public class Classifier implements Comparable<Classifier>{
		public Double classes;
		public Double distance;
		
		public Classifier(double classes, double distance) {
			this.classes = classes;
			this.distance = distance;
		}
		
		public Double getClasses() {
			return classes;
		}
		
		public Double getDistance() {
			return distance;
		}
		
		public int compareTo(Classifier anotherInstance) {
			
			if (this.distance < anotherInstance.distance) return -1;
	        if (this.distance > anotherInstance.distance) return 1;
	        return 0;
	    }
		
	}
	
}
