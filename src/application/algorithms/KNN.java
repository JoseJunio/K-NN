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
	 public NormalizedData  normalizedData;
	 public List<classifier> classifiers = new ArrayList<classifier>();
	 
	 
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
 			//classifiers.add(new classifier(Functions.euclidianDistance(input, tmp.get(i)), tmp.get(i).get(tmp.get(i).size()-1)));
		}
		
		Collections.sort((List<KNN.classifier>) classifiers);
	}
	
	public abstract class classifier implements Comparable<classifier>{
		public int classes;
		public Double distance;
		
		public classifier(int classes, double distance) {
			super();
			this.classes = classes;
			this.distance = distance;
		}
		
		public int getClasses() {
			return classes;
		}
		
		public Double getDistance() {
			return distance;
		}
		
		/*public int compareTo(classifier anotherInstance) {
	        return (Integer) this.classe - anotherInstance.distance;
	    }*/
		
	}
	
}
