/**
 * 
 * 
 * Gianmarco Spinaci
 * 0000691241
 * gianmarco.spinaci@studio.unibo.it
 * 
 * 
 * REPORT:
 * 
 * DATA -> 11/05/15
 * 
 * Completata la prima parte, con strade normali
 * 
 * Problema principale PriorityQueue 
 * Devo valutare se questo algoritmo di Dijkstra è effettivamente O(m log n)
 * 
 * DATA -> 12/05/15
 * 
 * Completato l'esercizio
 * 
 * Per l'algoritmo di dijkstra ho utilizzato la struttura dati PriorityQueue di Java
 * 
 * Ho utilizzato il metodo poll() che rimuove l'elemento in testa, con un costo di O(1)
 * Ogni inserimento con il metodo add() costa O(log n) perchè esegue il sorting dei nodi grazie al loro peso
 * 
 * Ho differenziato i due algoritmi di Dijkstra, il primo non accetta archi con costi > 0
 * 
 * Ho creato la classe Nodo con i vari paramentri
 * per non dover ricorrere all'iterazione dei suoi archi per trovare (di nuovo) l'arco 
 * che lega il nodo al nodo precedente
 * 
 * COMPLETATO
 * 
 */

import java.io.*;
import java.util.*;

/**
 * 
 * Classe Arco
 * 
 * Descrive le caratteristiche essenziali di ogni arco
 *
 */
class Arco {
	
	String tipo;
	int dst;
	double w;
	double c;
	
	Arco(String tipo, int dst,double w, double c){
		this.tipo = tipo;this.w = w; this.dst = dst; this.c = c;
	}
}
/**
 * 
 * Classe Nodo
 * 
 * questa classe permette l'utilizzo della struttura dati PriorityQueue
 *
 */
class Nodo implements Comparable<Nodo>{
	
	int index;
	int pred;
	double weight;
	double cost;
	
	Arco mainArc;

	Nodo(int index){ this.index = index; }
	
	/*
	 * Arraylist di archi apparteneti al nodo. 
	 * Necessario lo scorrimento O(n) e l'inserimento O(1)
	 */
	ArrayList<Arco> archi = new ArrayList<>(); 

	/*
	 * Creo un arco che collega questo nodo al nodo "dst", di peso "w" e di costo "c"
	 */
	protected void addArc(String tipo, int dst, double w, double c){
		
		this.archi.add(new Arco(tipo,dst,w,c));
	}

	/*
	 * Necessario per comparare i nodi all'interno della PriorityQueue
	 * Nodo con peso minore viene messo all'inidice 0 ecc.
	 */
	public int compareTo(Nodo nodo)
    {
        return Double.compare(weight, nodo.weight);
    }
}

class SoluzioneEsericizio1{
	
	BufferedReader reader;
	
	int numeroNodi, numeroArchi;
	double w,c;
	
	String tipo;
	int src, dst;
	
	Nodo[] grafo;
	
	SoluzioneEsericizio1(String nomeFile){
		
		try{
			
			//buffered* utilizzate per lettura file
			reader = new BufferedReader(new FileReader(new File(nomeFile)));
			
			//n = numero nodi; m = numero archi
			numeroNodi = Integer.parseInt(reader.readLine());
			numeroArchi = Integer.parseInt(reader.readLine());
			
			//utilizzo un array di nodi come grafo
			grafo = new Nodo[numeroNodi];
			
			//inizializzazione grafo con i vari n nodi O(n)
			for(int i=0; i<numeroNodi; i++){
				
				grafo[i] = new Nodo(i);
			}
			
			//lettura, creazione e inserimento nel grafo degli archi 
			for(int i=0; i<numeroArchi; i++){
				
				String row = reader.readLine();
				
				String[] tmp = row.split("\\s+");
				
				tipo = tmp[0];
				src = Integer.parseInt(tmp[1]);
				dst = Integer.parseInt(tmp[2]);
				
				w = Double.parseDouble(tmp[3]);
				
				if(tmp.length==5)
					c = Double.parseDouble(tmp[4]);
				else
					c = 0;
				
				grafo[src].addArc(tipo, dst, w, c);
			}
		}
		
		catch(IOException e){
			
			System.out.println(e);
		}
	}

	/**
	 * Algoritmo di Dijkstra su un array di Nodi
	 * 
	 * dijkstraN -> dijkstra solo su strade "n"
	 * dijkstraA -> dijkstra con tutti i tipi di strade
	 * 
	 * Inizializzazione O(n)
	 * Setto ogni peso a +Infinity, ogni predecessore a -1
	 * 
	 * Creo una PriorityQueue (MinHeap) che contenga i nodi,
	 * 
	 * poll() O(1)
	 * add()  O(log n)
	 * 
	 * non uso array di appoggio, ma solo variabili negli oggetti creati
	 * 
	 * @param grafo array di oggetti Nodo
	 * @return peso totale del cammino minimo con C=0
	 */
	public double dijkstraN(){
		
		
		//inizializzazione dei pesi e dei predecessori O(n)
		for(int i=0; i<grafo.length; i++){
			
			grafo[i].weight = Double.POSITIVE_INFINITY;
			grafo[i].pred = -1;
			
		}
		
		//implemento una coda di priorità di archi (max heap) ordinata per distanza minore
		grafo[0].weight = 0;
		PriorityQueue<Nodo> Q = new PriorityQueue<>();
		Q.add(grafo[0]);
		
		/*
		 * Inizio l'iterazione della coda di priorità
		 * 
		 * Rimuovo l'elemento Nodo con .weight minore
		 * e guardo se per ogni arco con origine in U 
		 * il costo è = 0
		 * 
		 * in caso affermativo continuo l'algoritmo
		 * valutando se è necessario l'aggiornamento 
		 * del peso degli archi da 0 al nodo V 
		 * 
		 * salvo ogni dato nel nodo
		 */
		while( !Q.isEmpty() ){
			
			Nodo u = Q.poll();
			
			for( Arco arco : u.archi){
				
				if( arco.c == 0 ){
					
					//caso in cui il nodo di destinazione dell'arco non è ancora stato scoperto
					if( grafo[arco.dst].weight == Double.POSITIVE_INFINITY){
						
						
						/*
						 * con queste istruzioni mi salvo:
						 * 
						 * peso totale dal nodo 0 al nodo corrente
						 * nodo da cui parte, e l'arco minore per arrivare al nodo corrente
						 * 
						 */
						grafo[arco.dst].weight = u.weight + arco.w;
						grafo[arco.dst].pred = u.index;
						grafo[arco.dst].mainArc = arco;
						
						Q.add(grafo[arco.dst]);
					}
					
					//caso in cui si scopre un nuovo percorso minimo
					else if( u.weight + arco.w < grafo[arco.dst].weight ){
						
						grafo[arco.dst].weight = u.weight + arco.w;
						grafo[arco.dst].pred = u.index;
						grafo[arco.dst].mainArc = arco;
					}
				}
			}
		}
		
		//ulteriore pezzo di codice che serve per la scrittura del percorso
		ArrayList<String> result1 = printPath(grafo);
		
		for(int i=result1.size()-1; i>=0; i--){
			
			System.out.println(result1.get(i));
		}

		return grafo[grafo.length-1].weight;
	}

	public double[] dijkstraA(){
		
		double[] r = new double[2];
		
		for(int i=0; i<grafo.length; i++){
			
			grafo[i].weight = Double.POSITIVE_INFINITY;
			grafo[i].pred = -1;
			
		}
		
		grafo[0].weight = 0;
		PriorityQueue<Nodo> Q = new PriorityQueue<>();
		Q.add(grafo[0]);
		
		while( !Q.isEmpty() ){
			
			Nodo u = Q.poll();
			
			for( Arco arco : u.archi){
				
				if( grafo[arco.dst].weight == Double.POSITIVE_INFINITY){
					
					grafo[arco.dst].weight = u.weight + arco.w;
					grafo[arco.dst].pred = u.index;
					grafo[arco.dst].mainArc = arco;
					
					//salvo la somma totale di costi dal nodo 0 al nodo corrente
					grafo[arco.dst].cost = u.cost + arco.c;
					
					Q.add(grafo[arco.dst]);
				}
				
				else if( u.weight + arco.w < grafo[arco.dst].weight ){
					
					grafo[arco.dst].weight = u.weight + arco.w;
					grafo[arco.dst].pred = u.index;
					grafo[arco.dst].mainArc = arco;
					
					grafo[arco.dst].cost = u.cost + arco.c;
				}
			}
		}
		
		ArrayList<String> result2 = printPath(grafo);
		
		for(int i=result2.size()-1; i>=0; i--){
			
			System.out.println(result2.get(i));
		}
		
		r[0] = grafo[grafo.length-1].weight;
		r[1] = grafo[grafo.length-1].cost;

		return r;
	}

	/*
	 * Metodo che accetta un grafo in forma di array di oggetti Nodo
	 * ritornando l'output in String di un arraylist in fomato:
	 * 
	 * tipo src dst
	 */
	private ArrayList<String> printPath(Nodo[] grafo){
		
		ArrayList<String> result = new ArrayList<>();
		
		int currentIndex = grafo.length-1;
		
		while(currentIndex != 0){
						
			result.add(grafo[currentIndex].mainArc.tipo+" "+grafo[currentIndex].pred+" "+currentIndex);
			
			currentIndex = grafo[currentIndex].pred;
		}
		
		return result;
	}
}

public class Esercizio1 {

	public static void main(String[] args) {
		
		String nomeFile = args[0];

		SoluzioneEsericizio1 soluzione = new SoluzioneEsericizio1(nomeFile);
		
		/*
		 * richiamo l'algoritmo di dijkstra solo per le strade normali
		 * inoltre predispongo il percorso minimo per l'algoritmo printPath
		 */
		System.out.println(soluzione.dijkstraN());
		
		double[] risultato = soluzione.dijkstraA();
		
		for(double r : risultato)
			System.out.println(r);
	}
}