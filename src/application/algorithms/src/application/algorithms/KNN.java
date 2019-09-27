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

import javax.xml.bind.ParseConversionEvent;

import org.xml.sax.Parser;

import application.functions.Functions;
import application.functions.Functions.NormalizedData;

public class KNN {
	
	 public Vector<List> 	 dados =  new Vector<List>();
	 public List<Double>	 media, desvio;
	 public NormalizedData   normalizedData;
	 public List<Classifier> classifiers = new ArrayList<Classifier>();
	 
	 
	 public KNN() {
		 loadData();
		 media = Functions.average(dados);
		 desvio = Functions.standardDeviation(dados, media);
		 
		 List<Double> input = new ArrayList<Double>();
		 input.add(1.75);
		 input.add(52.0);
		 input.add(null);
		 
		 normalizedData = Functions.zScore(dados, media, desvio, input);
		 
		 System.out.println(media);
		 System.out.println(desvio);
		 System.out.println(normalizedData.getNormalizedKNN());
		 System.out.println(normalizedData.getNormalizedInputData());
		 calculateKnn(normalizedData.getNormalizedInputData());
	 
	 }
	 
	public void loadData() {
		try {
			 BufferedReader br = new BufferedReader(new FileReader("c://Users//DELL//dados.txt"));
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
