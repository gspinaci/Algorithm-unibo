/**
 * 
 * 
 * Gianmarco Spinaci
 * 0000691241
 * gianmarco.spinaci@studio.unibo.it
 * 
 * 
 * 6/05/15
 * 
 * funziona solo con il primo input, non ha conoscenza del futuro. non può fare scelte ottimali, approccio greedy
 * 
 */



package esercizi;

import java.io.*;

public class Esercizio2 {
	
	public static void main(String[] args) {
		
		//inizializzaione variabili
		BufferedReader reader;
		//BufferedWriter writer;
		int numeroMesi, numeroObbligazioni;
		double penale;
		
		double R[][];
		double S[][];
		

		//prevenzione FileNotFoundException
		try{
			
			//buffered* utilizzate per lettura e scrittura file
			reader = new BufferedReader(new FileReader(new File("src/Esercizio2-3.in")));
			//writer = new BufferedWriter(new FileWriter(new File("src/Esercizio2-1.out")));
			
			//lettura file e settaggio variabili
			penale = Double.parseDouble(reader.readLine());
			numeroMesi = Integer.parseInt(reader.readLine());
			numeroObbligazioni = Integer.parseInt(reader.readLine());
			
			//inizializzazione matrice contenente le obbligazioni divise per mese
			R = new double[numeroMesi][numeroObbligazioni];
			S = new double[numeroMesi][numeroObbligazioni];
			
			//popolamento matrice
			for(int i = 0;i<numeroMesi;i++){
				
				String[] row = reader.readLine().split("\\s+");
				
				for(int j=0;j<numeroObbligazioni;j++){
					
					R[i][j] = Double.parseDouble(row[j]);
					S[i][j] = 0;
				}
			}
			
			
			//creazione dell'oggetto che serve per semplificare la comprensione del codice
			Finanziaria fin = new Finanziaria(R,S,numeroObbligazioni,numeroMesi,penale);
			
			System.out.println(fin.getMassimoValore());
			
		}
		
		catch(IOException e){
			
			System.out.println(e);
		}
	}
}

class Finanziaria{
	
	double[][] R;
	double[][] S;
	int n,m;
	double p;
	
	
	public Finanziaria(double[][] R, double[][] S, int n, int m, double p){
		
		this.R = R;
		this.n = n;
		this.m = m;
		this.p = p;
		this.S = S;
	}
	
	public int getMaxValueIndex(double[] row){
		
		double max = row[0];
		int index = 0;
		
		for(int i = 1;i<row.length;i++){
			
			if(row[i]>max){
				
				index = i;
				max = row[i];
			}
		}
		
		return index;
	}

	public double getMaxValue(double ... row){
		
		double max = row[0];
		
		for(int i = 1;i<row.length;i++){
			
			if(row[i]>max){
				
				max = row[i];
			}
		}
		
		return max;
		
	}
		
	public double getMassimoValore(){
		
		//inizializzo la prima riga
		for(int i=0; i<n; i++){
			
			S[0][i] = R[0][i];
			
		}
		
		
		//setto l'indice dell'elemento di valore massimo, del mese t=0
		int currentIndex = getMaxValueIndex(S[0]);
				
		for(int t=1;t<m;t++){
			
			for(int j=0; j<n; j++){
				
				
				/*
				 * per ogni cella della matrice, valuto quale valore devo inserire
				 * scelgo il massimo tra i valori : 
				 * 
				 * 1)	somma tra valore del titolo nel mese t e il massimo titolo del mese t-1 togliendo la penale
				 * 2)	somma tra valore del titolo nel mese t e il valore dello stesso titolo nel mese t-1
				 * 
				 * la matrice delle soluzioni viene popolata, quindi, con il valore massimo tra questi due valori.
				 * 
				 */
				S[t][j] = R[t][j] + getMaxValue( S[t-1][currentIndex]-p, S[t-1][j] );
				
				
			}
			
			//salvo l'indice dell'elemento maggiore del mese corrente
			currentIndex = getMaxValueIndex(S[t]);
		}
		
		//ritorna il valore massimo dell'ultimo mese
		//cioè il massimo valore che si può ottenere con il giusto utilizzo delle obbligazioni
		return getMaxValue(S[S.length-1]);
	}
	
}