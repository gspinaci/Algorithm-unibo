/**
 * 
 * 
 * Gianmarco 
 * Spinaci
 * 0000691241
 * gianmarco.spinaci@studio.unibo.it
 * 
 * REPORT:
 * 
 * DATA -> 6/05/15
 * 
 * funziona solo con il primo input, non ha conoscenza del futuro. non può fare scelte ottimali, approccio greedy
 * 
 * ----------------------------
 * 
 * DATA -> 7/05/15
 * 
 * trovato algoritmo per approccio dinamico
 * 
 * 
 * il primo metodo che ho trovato era un metodo poco efficiente di O(n*m^2)
 * 
 * valutava per ogni obbligazione( [t][j] ) il valore massimo tra i valori dati dalle combinazioni 
 * tra tutte obbligazioni t-1 
 * 
 * il metodo efficiente che ho pensato è invece O(n*m)
 * 
 * non è necessario andare a valutare tutte le combinazioni dell'obbligazione( [t][j]) 
 * con le obbligazioni del mese t-1, ma basta valutarla solo con il valore massimo del mese t-1
 * nel nostro caso c'era la possibilità di avere una penale se da un mese all'altro si cambiava obbligazione
 *
 * basta gestire il caso grazie al massimo tra i due parametri
 * 
 * CONCLUSO
 * 
 */

import java.io.*;

class SoluzioneEsericizio2{
	
	//inizializzaione variabili
	BufferedReader reader;
	int numeroMesi, numeroObbligazioni;
	double penale;
	
	/*
	 * creazione di 3 matrici
	 * una contiene i valori delle obbligazioni dati da input 
	 * una che uso per la programmazione dinamica
	 * l'ultima che uso per tener traccia del percorso di scelta che faccio nel tempo
	 */
	double R[][];
	double S[][];
	int X[][];
	
	SoluzioneEsericizio2(String nomeFile){
		
		try{
			
			reader = new BufferedReader(new FileReader(new File(nomeFile)));
			
			penale = Double.parseDouble(reader.readLine());
			numeroMesi = Integer.parseInt(reader.readLine());
			numeroObbligazioni = Integer.parseInt(reader.readLine());
			
			R = new double[numeroMesi][numeroObbligazioni];
			S = new double[numeroMesi][numeroObbligazioni];
			X = new int[numeroMesi][numeroObbligazioni];
			
			//popolamento matrici
			for(int i = 0;i<numeroMesi;i++){
				
				String[] row = reader.readLine().split("\\s+");
				
				for(int j=0;j<numeroObbligazioni;j++){
					
					R[i][j] = Double.parseDouble(row[j]);
					S[i][j] = 0;
					X[i][j] = 0;
				}
			}
			
			//salvo la somma del valore massimo che posso avere con queste obbligazioni in input
			//il metodo richiamato è effettivamente il codice dell'algoritmo dell'esercizio
			//double somma = getMaxBondValue();
			
			//trovo il percorso delle obbligazioni che mi porta, attraverso i mesi t,
			//ad arrivare al valore somma che mi sono calcolato prima
			//int[] tmp = getPath();
		}
		
		catch(IOException e){
			
			System.out.println(e);
		}
	}

	/**
	 * 
	 * metodo che accetta un vettore di N elementi double in input
	 * e ritorna l'indice dell'elemento maggiore
	 * 
	 * O(n)
	 * 
	 */
	private int getMaxValueIndex(double ... row){
		
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

	/**
	 * 
	 * metodo che accetta un vettore di N elementi double in input
	 * e ritorna il valore double maggiore
	 * 
	 * O(n)
	 * 
	 */
	private double getMaxValue(double ... row){
		
		double max = row[0];
		
		for(int i = 1;i<row.length;i++){
			
			if(row[i]>max){
				
				max = row[i];
			}
		}
		
		return max;
		
	}
		
	
	/**
	 * 
	 * algoritmo principale 
	 * 
	 * sfruttando la programmazione dinamica sono riuscito ad utilizzare una matrice dei risultati
	 * che vado a popolare dinamicamente
	 * ad ogni mese (tranne il primo) valuta la combinazione migliore di obbligazioni
	 * 
	 * versioni algoritmo [vedi report]
	 * 
	 * versione corrente :
	 * 
	 * costo O( [n*m]+[n*m] ) => O(2[n*m]) => O(n*m)
	 * 
	 * 
	 */
	public double getMaxBondValue(){
		
		//inizializzo la prima riga
		for(int i=0; i<numeroObbligazioni; i++){
			
			S[0][i] = R[0][i];
			X[0][i] = -1;
		}
		
		//setto l'indice dell'elemento di valore massimo, del mese t=0
		int currentIndex = getMaxValueIndex(S[0]);
				
		for(int t=1;t<numeroMesi;t++){
			
			for(int j=0; j<numeroObbligazioni; j++){
				
				
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
				S[t][j] = R[t][j] + getMaxValue( S[t-1][currentIndex]-penale, S[t-1][j] );
				
				
				//popolo la matrice che mi serve per tenere conto del percorso 
				if ( S[t-1][currentIndex]-penale > S[t-1][j] )
					
					X[t][j] = currentIndex;
				
				else
					
					X[t][j] = j;
			}
			
			//salvo l'indice dell'elemento maggiore del mese corrente
			//O(n)
			currentIndex = getMaxValueIndex(S[t]);
		}
		
		int[] soluzioni = getPath();
		
		for(int s : soluzioni)
			System.out.println(s);
		
		//ritorna il valore massimo dell'ultimo mese
		//cioè il massimo valore che si può ottenere con il giusto utilizzo delle obbligazioni
		return getMaxValue(S[S.length-1]);
	}

	/**
	 * 
	 * metodo che ricostruisce il percorso ideale nello scegliere le obbligazioni,
	 * leggendo la matrice al contario
	 * 
	 * O(m) 
	 * 
	 */
	private int[] getPath(){
		
		int[] ris = new int[numeroMesi];
		
		ris[numeroMesi-1]  = getMaxValueIndex(S[S.length-1]);
		
		for(int t=numeroMesi-2; t>=0; t--){
			
			ris[t] =  X[t+1][ris[t+1]];
		}
		
		return ris;
	}
}

public class Esercizio2 {
	
	public static void main(String[] args) {
		
		String nomeFile = args[0];
		
		System.out.println(new SoluzioneEsericizio2(nomeFile).getMaxBondValue());
	}
}