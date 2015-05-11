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
 * DATA -> 12/05/15
 * 
 * Completata la prima parte, con strade normali
 * 
 * Problema principale PriorityQueue 
 * Devo valutare se questo algoritmo di Dijkstra è effettivamente O(m log n)
 * 
 * 
 */



package esercizi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.*;


public class Esercizio1 {

	public static void main(String[] args) {
		
		BufferedReader reader;
		
		int n, m;
		double w,c;
		
		String tipo;
		int src, dst;
		
		
		try{
			
			//buffered* utilizzate per lettura file
			reader = new BufferedReader(new FileReader(new File("src/Esercizio1-1.in")));
			
			//n = numero nodi; m = numero archi
			n = Integer.parseInt(reader.readLine());
			m = Integer.parseInt(reader.readLine());
			
			//utilizzo un array di nodi come grafo
			Nodo[] grafo = new Nodo[n];
			
			//inizializzazione grafo con i vari n nodi
			for(int i=0; i<n; i++){
				
				grafo[i] = new Nodo(i);
			}
			
			//lettura e creazione archi 
			for(int i=0; i<m; i++){
				
				String row = reader.readLine();
				
				String[] tmp = row.split("\\s+");
				
				tipo =  tmp[0];
				src = Integer.parseInt(tmp[1]);
				dst = Integer.parseInt(tmp[2]);
				
				w = Double.parseDouble(tmp[3]);
				
				if(tmp.length==5)
					c = Double.parseDouble(tmp[4]);
				else
					c = 0;
				
				
				grafo[src].addArc(tipo, dst, w, c);
			}
			
			/*
			 * richiamo l'algoritmo di dijkstra solo per le strade normali
			 * inoltre predispongo il percorso minimo per l'algoritmo printPath
			 */
			double l = dijkstraN(grafo);
			
			ArrayList<String> result = printPath(grafo);
			
			for(int i=result.size()-1; i>=0; i--){
				
				System.out.println("n "+result.get(i));
			}
			
			System.out.println(l);
		}
		
		catch(IOException e){
			
			System.out.println(e);
		}
	}
	
	/**
	 * Algoritmo di Dijkstra su un array di Nodi
	 * 
	 * Inizializzazione O(n)
	 * Setto ogni peso a +Infinity, ogni predecessore a -1
	 * 
	 * Creo una PriorityQueue (MinHeap) che contenga i nodi,
	 * non uso array di appoggio, ma solo variabili degli oggetti creati
	 * 
	 * @param grafo array di oggetti Nodo
	 * @return peso totale del cammino minimo con C=0
	 */
	private static double dijkstraN(Nodo[] grafo){
		
		
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
				
				/*
				 * Essendo il primo caso, solo se il costo è nullo posso prendere in considerazione l'arco
				 */
				if( arco.c == 0 ){
					
					if( grafo[arco.dst].weight == Double.POSITIVE_INFINITY){
						
						grafo[arco.dst].weight = u.weight + arco.w;
						grafo[arco.dst].pred = u.index;
						
						Q.add(grafo[arco.dst]);
					}
					
					else if( u.weight + arco.w < grafo[arco.dst].weight ){
						
						grafo[arco.dst].weight = u.weight + arco.w;
						grafo[arco.dst].pred = u.index;
					}
				}
			}
		}

		return grafo[grafo.length-1].weight;
	}

	/**
	 * 
	 * @param grafo array di oggetti Nodo
	 * @return ArrayList<String> contenente nodo sorgente e nodo destinazione di ogni arco del cammino minimo
	 */
	private static ArrayList<String> printPath(Nodo[] grafo){
		
		ArrayList<String> result = new ArrayList<>();
		
		int currentIndex = grafo.length-1;
		
		
		while(currentIndex != 0){
			
			result.add(grafo[currentIndex].pred+" "+currentIndex);
			
			currentIndex = grafo[currentIndex].pred;
		}
		
		return result;
	}
}


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
	
	public String toString(){
		
		return "["+w+","+dst+"]";
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

	public String toString(){
		
		String x = "";
		
		for(Arco arco : archi){
			
			x+= arco+";";
		}
		
		return x;
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

