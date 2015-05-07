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
		

		//prevenzione FileNotFoundException
		try{
			
			//buffered* utilizzate per lettura e scrittura file
			reader = new BufferedReader(new FileReader(new File("src/Esercizio2-2.in")));
			//writer = new BufferedWriter(new FileWriter(new File("src/Esercizio2-1.out")));
			
			//lettura file e settaggio variabili
			penale = Double.parseDouble(reader.readLine());
			numeroMesi = Integer.parseInt(reader.readLine());
			numeroObbligazioni = Integer.parseInt(reader.readLine());
			
			//inizializzazione matrice contenente le obbligazioni divise per mese
			R = new double[numeroMesi][numeroObbligazioni];
			
			//popolamento matrice
			for(int i = 0;i<numeroMesi;i++){
				
				String[] row = reader.readLine().split("\\s+");
				
				for(int j=0;j<numeroObbligazioni;j++){
					
					R[i][j] = Double.parseDouble(row[j]);
				}
			}
			
			Finanziaria fin = new Finanziaria(R,numeroObbligazioni,numeroMesi,penale);
			
			
			
			System.out.println(fin.getSomma2());
			
			fin.printPath();
			
			//System.out.println(getFirst(R,numeroObbligazioni,numeroMesi,0));
				
			//double somma = fin.getSomma2();
			
			//System.out.println(fin.max(10, 9.99));
			
			
		}catch(IOException e){
			
			System.out.println(e);
		}
		
		
		

	}
}

class Finanziaria{
	
	double[][] valori;
	
	double[][] appoggio;
	
	int n,m;
	
	double p;
	
	
	public Finanziaria(double[][] valori,int n, int m, double p){
		
		this.valori = valori;
		this.n = n;
		this.m = m;
		this.p = p;
		
		appoggio = new double[m][n];
		init();
	}
	
	/**
	 * 
	 * @param row ritorna l'indice del numero più alto
	 * @return
	 */
	public int getMax(double[] row){
		
		double max = row[0];
		int index = 0;
		
		for(int i = 1;i<row.length;i++){
			
			if(row[i]>max){
				
				index = i;
			}
		}
		
		return index;
	}

	public double getMaxValue(double[] row){
		
		double max = row[0];
		
		for(int i = 1;i<row.length;i++){
			
			if(row[i]>max){
				
				max = row[i];
			}
		}
		
		return max;
		
	}
	
	public void init(){
		
		for(double[] row : valori){
			
			for(double element : row){
				
				element = 0;
			}
		}
	}
	
	/**
	 * 
	 * @param R effettua il dump della matrice
	 */
	public void print(){
		
		for(double[] row : valori){
			
			for(double element : row){
				
				System.out.print(element+" ");
			}
			System.out.println("");
		}
	}
	
	//to complete
	public void printPath(){
		
	}
	
	/**
	 * 
	 * @param R matrice del valore delle obbligazioni per mese
	 * @param numeroObbligazioni
	 * @param numeroMesi
	 * @param penale
	 * @return somma delle obbligazioni che hanno valore maggiore
	 */
	public double getSomma(){
		
		//trovo l'indice dell'obbligazione con vaore più alto nel primo mese
		int firstIndex = getMax(valori[0]);
		
		//array di appoggio per il metodo getMax
		double[] tmp = new double[n];
		
		//salvo l'indice del valore da trovare
		int currentIndex = firstIndex;
		
		System.out.println(currentIndex);
		
		//salva la somma della prima riga
		double somma = valori[0][firstIndex];
		
		for(int i=1;i<m-1;i++){
			
			for(int j=0;j<n;j++){
				
				// vede se bisogna aggiungere la penale oppure no
				if(j==currentIndex){
					tmp[j] = somma + valori[i][j];
				}
				else{
					tmp[j] = somma + valori[i][j] - p;
				}
			}
			
			//calcola l'obbligazione da prendere
			currentIndex = getMax(tmp);
			
			System.out.println(currentIndex);
			
			//salva la somma
			somma += valori[i][currentIndex];
		}
		
		return somma;
	}

	public double getSomma2(){
		
		
		for(int i=0; i<n; i++){
			
			appoggio[0][i] = valori[0][i];
			
		}
		
		double[] tmp = new double[n];
		
		for(int i=1; i<m; i++){
			
			for(int j=0; j<n; j++){
				
				for(int x=0; x<n; x++){
					
					if(x==j)
						tmp[x] = appoggio[i-1][x] + valori[i][j];
					else
						tmp[x] = appoggio[i-1][x] + valori[i][j] - p;
				}
				
				appoggio[i][j] = getMaxValue(tmp);
				
			}
		}
		
		return getMaxValue(appoggio[appoggio.length-1]);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}