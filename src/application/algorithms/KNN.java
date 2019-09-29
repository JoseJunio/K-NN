package application.algorithms;

import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import application.functions.Functions;
import application.functions.Functions.NormalizedData;

public class KNN {

	 public Vector<List> 	 dados =  new Vector<List>();
	 public List<Double>	 media, desvio;
	 public static NormalizedData   normalizedData;
	 public static List<Classifier> classifiers = new ArrayList<Classifier>();
	 public static int 			 K;
	 public static int 			 KFold;
	 public List<Double> input = new ArrayList<Double>();

	 public static int 			 attributes = 0;
	 public static int 			 samples = 0;

	 public KNN() {
		 loadData();

		 System.out.println("media: " + media);
		 System.out.println("desvio: " + desvio);
		 System.out.println("Normalized KNN: " + normalizedData.getNormalizedKNN());
		 		 		 
		 //System.out.println("KNN: " + getKNN());
		 System.out.println("attribute: " + attributes);
		 System.out.println("sample: " + samples);
		 
		 application.algorithms.KFold.executeKfold();

	 }

	public void loadData() {

		InputStream input;
		String 		line;
		boolean		caminhoValido = false;
		int numeroAmostras = 0;
		Scanner scanner = new Scanner(System.in);

        while (!caminhoValido) {
            System.out.println("Entre com o caminho do arquivo contendo as amostras: ");
            String caminho = scanner.nextLine();
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
                //e.printStackTrace();
                System.out.println(">>>>>>>>>>>>>>>>>>>\nFalha na leitura do arquivo!");
                System.out.println("Caminho inválido. Tente novamente\n");
            }
        }
        
        media = Functions.average(dados);
		desvio = Functions.standardDeviation(dados, media);
        
        //Normalizar dados?
        boolean normalizarValido = false;
		while (!normalizarValido) {
            System.out.println("Deseja normalizar os dados (com o algoritmo z-score) antes de aplicar o classificador? (Y/N)\n");
            String str = scanner.nextLine();
           
            if (str.equals("Y") || str.equals("y")) {
       		 	normalizedData = Functions.zScore(dados, media, desvio);
            	normalizarValido = true;
            }
            else if(str.equals("N") || str.equals("n")) {
            	normalizedData = Functions.zScore(dados, media, desvio);//TODO remover normalizacao e retornar dados apenas
            	normalizarValido = true;
            }
            else {
                System.out.println(
                        "\n\n>>>>>>Opcao inválida. Responda com Y ou N.\n");
            }
            
        }

        // define K vizinhos
        boolean kValido = false;
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
        
        scanner.close();

    }

	public List<Double> getKNN(){
		List<Double> tmp = new ArrayList<>();

		for(int i=0; i < K; i++) {
			tmp.add(classifiers.get(i).getDistance());
		}

		return tmp;
	}

}
