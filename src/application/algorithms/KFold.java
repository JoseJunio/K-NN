package application.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import application.functions.Functions;
import application.functions.Functions.NormalizedData;
import javafx.util.Pair;

public class KFold {
	
	public static int[] tamanhos;
	static List<Pair<Integer, Integer>> ranges;
	
	public static int getCluster(int idx) {
		for (int i= 0; i< ranges.size(); i++) {
			if(i >= ranges.get(i).getKey() && i<= ranges.get(i).getValue()) {
				return i;
			}
		}
		return 0;
	}
	
	public static void executeKfold(){
		
		Vector<List> tmp = KNN.normalizedData.getNormalizedKNN() ;
		
		Collections.shuffle(tmp);
		
		tamanhos = new int[KNN.KFold];// aloca vetor com os tamanhos de cada fold k
		ranges = new ArrayList<Pair<Integer, Integer>>();

        int tamPadrao = (int) Math.floor(KNN.samples / KNN.KFold);
        int restante = KNN.samples % KNN.KFold;
        System.out.println("Serão " + KNN.KFold + " folds contendo os seguintes tamanhos:\n");

        //Distribui as amostras de modo que cada fold fique com tamanho mais proximo um do outro
        for (int i = 0; i < KNN.KFold; i++) {
            tamanhos[i] = tamPadrao;
            if (restante > 0) {
                tamanhos[i]++;
                restante--;
            }
            System.out.println(tamanhos[i]);
		}
        
        //Distribui as amostras de modo que cada fold fique com tamanho mais proximo um do outro
        int ini= 0;
        for (int i = 0; i < KNN.KFold; i++) {
            ranges.add(new Pair<Integer, Integer>(ini, ini + tamanhos[i]-1));
            ini+= tamanhos[i];
		}
        
        for (int i= 0; i< KNN.samples; i++) {
        	for(int j= 0; j< KNN.samples; j++) {
        		Double classe= (Double) tmp.get(i).get(KNN.attributes);
        		List treino= tmp.get(i);
        		List amostra= tmp.get(j);
        		
        		//se j não for amostra do mesmo cluster de i
        		if(getCluster(i) != getCluster(j)) {
        			Double distance= Functions.euclidianDistance(treino, amostra);
        			Classifier c= new Classifier((Double)tmp.get(j).get(KNN.attributes), distance);	
            		KNN.classifiers.add(c);        			
        		}
        	}  
        	Collections.sort(KNN.classifiers);
        }        
	}	
}
