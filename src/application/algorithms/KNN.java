package application.algorithms;

import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
	 public int 			 attributes;
	 public int 			 samples;
	 public int 			 K;
	 public int 			 KFold;
	 public List<Double> input = new ArrayList<Double>();
	 
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
		 System.out.println("attribute: " + attributes);
		 System.out.println("sample: " + samples);
	 
	 }
	 
	public void loadData() {
		
		InputStream input;
		String 		line;
		boolean		caminhoValido = false;
		int numeroAmostras = 0;
		
		Scanner scanner = new Scanner(System.in);
		
        while (!caminhoValido) {
            System.out.println("Entre com o caminho do arquivo contendo as amostras: ");
            String caminho = "/Users/josejunio/eclipse-workspace/K-NN-bkp/src/dados.txt"; // myObj.nextLine();
            List tmp;                                                                                                     
            BufferedReader buff;                                                                               
            
            try {
                input = new FileInputStream(caminho);
                buff = new BufferedReader(new InputStreamReader(input));
                
                while (buff.ready()) {
                    line = buff.readLine();
                    tmp = (List) Arrays.asList(line.split("\\s+"));
                    
                    if (numeroAmostras > 0) {// pula primeira linha
                    	dados.add(tmp);
                    } else {
                    	samples = Integer.parseInt(tmp.get(0).toString());
                    	attributes = Integer.parseInt(tmp.get(1).toString());
                    }
                    
                    numeroAmostras++;// incrementa numero de amostras
                }
                caminhoValido = true;
                dados = Functions.convertStringToDouble(dados);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(">>>>>>>>>>>>>>>>>>>\nFalha na leitura do arquivo!");
                System.out.println("Caminho inválido. Tente novamente\n");
            } 
        }
        
        
        // define K vizinhos
        boolean kValido = false;
        Object myObj;
		while (!kValido) {
            System.out.println("Entre com o numero de vizinhos (k): ");
            String kStr = scanner.nextLine();
            try {
                K = Integer.parseInt(kStr);
                if (K <= samples && K >= 1) {
                    kValido = true;
                } else {
                    System.out.println(
                            "\n\n>>>>>>k precisa ser um número maior que zero e menor ou igual ao número de amostras ("
                                    + samples + "):\n");
                }
            } catch (Exception e) {
                System.out.println("\n\n>>>>>>k precisa ser um número inteiro. Tente novamente:\n");
            }
        }
		
        // define particoes do K-fold
        boolean kFoldValido = false;
        while (!kFoldValido) {
            System.out.println("Entre com o número de partições k-fold: ");
            String kStr = scanner.nextLine();
            try {
                KFold = Integer.parseInt(kStr);
                if (KFold <= samples) {
                    if (KFold >= 2) {
                        kFoldValido = true;
                    } else {
                        System.out.println("\n\n>>>>>>k precisa ser um número divisivel pelo numero de amostras "
                                + samples + ":\n");
                    }
                } else {
                    System.out.println("\n\n>>>>>>k precisa ser um número menor ou igual ao número de amostras ("
                            + samples + "):\n");
                }
            } catch (Exception e) {
                System.out.println("\n\n>>>>>>k precisa ser um número inteiro. Tente novamente:\n");
            }
        }
        
    } 

	public List<Double> getKNN(){
		List<Double> tmp = new ArrayList<>();
		
		for(int i=0; i < K; i++) {
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
