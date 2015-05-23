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
 * DATA -> 8/05/15
 * 
 * ho valutato un algoritmo di risoluzione che va con O(n^2)
 * 
 * creo una matrice NxN, che contiene le distanze tra tutte le città
 * essendo una matrice speculare mi basta lavorare su (N^2 - N)/2 elementi della matrice
 * 
 * creazione della matrice Θ( (N^2 - N)/2 )
 * 
 * Errato, devo lavorare su N^2 elementi 
 * 
 * ad ogni riga t aggiorno il corrispondente contatore, che indica le città adiacenti
 * e inserisco un identificatore formato da [identificativo città, numero città adiacenti] in una delle seguenti strutture:
 * 
 * 						inserimento		ricercaMaggiore		riordinamento
 * - max heap  			O(log n)		O(1)				O(log n)
 * - array ordinato 	O(1)			O(1)				O(n log n)
 * 
 * nota (per rendere competitivo l'uso di array ordinato, viene ordinato solo dopo aver inserito tutti gli elementi, con un quick sort)
 * 
 * 
 * elimino l'elemento massimo (con più città adiacenti non coperte) 
 * ne conosco le coordinate della matrice
 * 
 * DATA -> 21/05/15
 * 
 * Cambiato l'utilizzo di strutture dati,
 * 
 * Lettura iniziale del file Θ( (N^2 - N)/2 ) poichè distanza(A,B) = distanza(B,A) 
 * allora si utilizza la matrice come una diagonale superiore
 * 
 * Salvo ogni Citta dentro un array, e ogni città adiacente dentro una LinkedList<Citta> nell'oggetto
 * 
 * L'algoritmo itera finchè non tutte le città sono coperte
 * Ad ogni giro prendo la città con più città adiacenti non coperte
 * 
 * per ogni città adiacente setto la variabile boolean coperto a true
 * 
 * dopo di che faccio un aggiornamento totale di tutte le città inserite nell'array e ri-aggiorno
 * la variabile int che tiene conto delle città vicine
 * 
 * aggiungo un quick-sort alla fine dell'array, eliminando l'ultimo elemento dell'array
 * 
 * 
 */

import java.io.*;
import java.util.*;

/*
 * Class Citta
 * 
 * Usata per il metodo compareTo che ordina le città in base 
 * al numero di città adiacenti
 * 
 * La classe tiene conto di caratteristiche della città
 */
class Citta implements Comparable<Citta>{
	
	double x;
	double y;
	
	int index;
	boolean coperto;
	int numeroAdiacenti;
	
	LinkedList<Citta> adiacenti;
	
	Citta(int index, String ... cord){
		
		this.x = Double.parseDouble(cord[0]);
		this.y = Double.parseDouble(cord[1]);
		
		adiacenti = new LinkedList<>();
		//numeroAdiacenti = 0;
		
		this.index = index;
		this.coperto = false;
	}
	
	public int compareTo(Citta citta){
		
		if( Integer.compare(this.numeroAdiacenti, citta.numeroAdiacenti) == 0 )
			
			return Integer.compare(this.index, citta.index );
		
		
        return -Integer.compare(this.numeroAdiacenti, citta.numeroAdiacenti);
    }
}

class SoluzioneEsericizio4{
	
	BufferedReader reader;
	
	double raggio;
	int numeroCitta;
	Citta[] citta;
	/*
	 * Metodo costruttore
	 * 
	 * Accetta il nome del file
	 * e crea l'array 
	 */
	SoluzioneEsericizio4(String nomeFile){
		
		try{
			
			reader = new BufferedReader(new FileReader(new File(nomeFile)));
			
			raggio = Double.parseDouble(reader.readLine());
			numeroCitta = Integer.parseInt(reader.readLine());
			
			citta = new Citta[numeroCitta];
			
			//inizializzazione, O(n)
			for( int i = 0; i < numeroCitta; i++ ){
				
				String[] row = reader.readLine().split("\\s+");
				
				citta[i] = new Citta(i,row);
			}
			
			/*
			 * 
			 * Θ( (N^2 - N)/2 ) lettura di tutte le distanze
			 * 
			 * LinkedList
			 * O(1) inserimento
			 */
			for(int i=0; i<numeroCitta; i++){
				
				for(int j=i; j<numeroCitta; j++){
					
					double distanza = Math.sqrt( Math.pow(citta[i].x - citta[j].x,2) + Math.pow(citta[i].y - citta[j].y,2));
					
					//salvo la distanza solo se è minore del raggio
					if(distanza < raggio){
						
						citta[i].adiacenti.add(citta[j]);
						citta[i].numeroAdiacenti++;
						
						//e se la distanza non è data dalla distanza della stessa città
						//la aggiungo anche dall'altra parte
						if(distanza != 0){
							
							citta[j].adiacenti.add(citta[i]);
							citta[j].numeroAdiacenti++;
						}
					}
				}
			}
		}
		
		catch(IOException e){
			
			System.out.println(e);
		}
	}
	/*
	 * Algoritmo principale
	 * 
	 * Faccio un sorting sull'array
	 * Quick sort O(n log n)
	 * 
	 * continuo ad iterare finchè tutte le città non sono coperte
	 * 
	 * Ad ogni giro aggiorno il numero di città non ancora coperte
	 * sottraendo il numero di città adiacenti dalla città alla posizione 0
	 * 
	 * Setto a true ogni città adiacente alla città U
	 * 
	 * Stampo poi tutti i risultati
	 * 
	 */
	public void posizionaAntenne(){
		
		ArrayList<Integer> risultati = new ArrayList<>();
		
		int cittaNonCoperte = numeroCitta; 
		
		//O(n log n)
		Arrays.sort(citta);
		
		while(cittaNonCoperte > 0){
			
			//O(1)
			Citta u = citta[0];
			
			risultati.add(u.index);
			
			for(Citta cittaAdiacente : u.adiacenti){
				
				cittaAdiacente.coperto = true;
			}
			
			cittaNonCoperte -= u.numeroAdiacenti;
			
			refreshNumeri();
		}
		
		for(Integer ris : risultati){
			
			System.out.println(ris);
		}
		
	}

	/*
	 * Metodo che aggiorna il numero delle città adiacenti
	 * non coperte adiacenti
	 */
	private void refreshNumeri(){
		
		
		citta[0] = null;
		
		Citta[] tmp = new Citta[citta.length-1];
		
		for(int i=0, j=1; i<citta.length-1; i++, j++){
			
			tmp[i] = citta[j];
		}
		
		citta = null;
		
		//O(n^2)
		for(Citta c : tmp){
			
			c.numeroAdiacenti = 0;
			
			if(c.coperto) c.numeroAdiacenti = 0;
			
			for(Citta cittaAdiacente : c.adiacenti){
				
				if(!cittaAdiacente.coperto)
					
					c.numeroAdiacenti++;
			}
		}
		
		citta = tmp;
		//quicksort
		//O(n log n) 
		Arrays.sort(citta);
	}

}

public class Esercizio4 {

	public static void main(String[] args) {
		
		String nomeFile = args[0];
		
		new SoluzioneEsericizio4(nomeFile).posizionaAntenne();
	}
}