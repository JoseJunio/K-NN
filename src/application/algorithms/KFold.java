package application.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import application.functions.Functions;
import application.functions.Functions.NormalizedData;
import javafx.util.Pair;

public class KFold {
	
	public static int[] tamanhos;
	static List<Pair<Integer, Integer>> ranges;
	
	public static int getCluster(int idx) {
		for (int i= 0; i< ranges.size(); i++) {
			if(idx >= ranges.get(i).getKey() && idx<= ranges.get(i).getValue()) {
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
        System.out.println("\nSerão " + KNN.KFold + " folds contendo os seguintes tamanhos:\n");

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
        
        int acertos= 0;
        
        for (int i= 0; i< KNN.samples; i++) {
        	Double classe= (Double) tmp.get(i).get(KNN.attributes);
        	for(int j= 0; j< KNN.samples; j++) {        		
        		List treino= tmp.get(i);
        		List amostra= tmp.get(j);
        		
        		//se j não for amostra do mesmo cluster de i
        		if(getCluster(i) != getCluster(j)) {
        			Double distance= Functions.euclidianDistance(treino, amostra);
        			Classifier c= new Classifier(classe, distance);	
            		KNN.classifiers.add(c);        			
        		}
        	}
        	Collections.sort(KNN.classifiers);//ordena lista baseado na distancia
        	
        	//pegar os k primeiros ver qual classe ganha
        	int kLocal= KNN.K; //Inicialmente K é o definido pelo usuario mas pode ser diminuido em caso de empate
        	boolean empate= true;
        	double novaClasse= -1;
        	    
        	while(empate) {
        		HashMap<Double, Integer> classeQtd = new HashMap<Double, Integer>();
	        	for(int k=0; k< kLocal; k++) {
	        		Double cls= KNN.classifiers.get(k).getClasses();
	        		System.out.println("distancia: "+ KNN.classifiers.get(k).getDistance() +", classse: " + KNN.classifiers.get(k).getClasses()+ "\n");
	        		if(classeQtd.containsKey(cls)) {
	        			int qtd= classeQtd.get(cls);
	        			classeQtd.put(cls, ++qtd);//incrementa qtd de ocorrencias da classe
	        		}else {
	        			classeQtd.put(cls, 1);//primeira ocorrencia da classe, quantidade 1
	        		}
	        	}
	        	
	        	Integer max = Collections.max(classeQtd.values());//descobre qual maior quantidade (da classe ganhadora)
	        	int repeticoes= 0;
	        	
	        	//itera no hashmap verificando qual a classe mais repetida (nova classe de i) e se ela é a única com aquela pontuacao
	        	for (Entry<Double, Integer> entry : classeQtd.entrySet()) {
        		    Double key = entry.getKey();
        		    Object value = entry.getValue();
        		    if(value == max) {
        		    	repeticoes++;
        		    	novaClasse= key;
        		    	break;
        		    }
        		}	        		        	        	    
	        		        	
	        	//empate? 
	        	if(repeticoes == 1) {// nao repete
	        		empate= false;	        		
	        	}
	        	else {// repete
	        		kLocal--;// decrementa K para esse treino e tenta de novo descobrir a vencedora
	        	}	        		        		        	
        	}
        	
        	//Saiu do while, já nap há repeticao de classe vencedora e a nova classe é unanime.
        	
        	//calcular taxa de acerto
        	if(novaClasse == classe) {
        		//acerto
        		acertos++;
        	}   	       	
        	
        	KNN.classifiers.clear(); //limpa a lista para ser usada no proximo cluster
        	
        }
        
        System.out.println("TAxa de acerto: "+ acertos/KNN.samples* 100+ "\n");
        
	}
}
