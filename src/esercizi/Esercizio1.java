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
 * Ho utilizzato il metodo poll() che rimuove l'elemento in testa, con un costo di O(log n) perchè ribilancia 
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
 * Data -> 23/05/15
 * 
 * Perfezionamento codice, ho creato un solo metodo, in base ad un booleano decide il tipo di algoritmo
 * 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
	@Override
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
	
	//prepara il grafo 
	SoluzioneEsericizio1(String nomeFile){
		
		try{
			
			reader = new BufferedReader(new FileReader(new File(nomeFile)));
			
			numeroNodi = Integer.parseInt(reader.readLine());
			numeroArchi = Integer.parseInt(reader.readLine());
			
			grafo = new Nodo[numeroNodi];
			
			//inizializzazione grafo con i vari n nodi O(n)
			for(int i=0; i<numeroNodi; i++){
				
				grafo[i] = new Nodo(i);
			}
			
			//lettura, creazione e inserimento nel grafo degli archi 
			//O(m)
			for(int i=0; i<numeroArchi; i++){
				
				String row = reader.readLine();
				
				String[] tmp = row.split("\\s+");
				
				tipo = tmp[0];
				src = Integer.parseInt(tmp[1]);
				dst = Integer.parseInt(tmp[2]);
				
				w = Double.parseDouble(tmp[3]);
				
				if(tmp.length == 5)
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
	 * tipo true  -> dijkstra solo su strade "n"
	 * tipo false -> dijkstra con tutti i tipi di strade
	 * 
	 * Inizializzazione O(n)
	 * Setto ogni peso a +Infinity, ogni predecessore a -1
	 * 
	 * Creo una PriorityQueue (MinHeap) che contenga i nodi,
	 * 
	 * Da Oracle.Docs
	 *  this implementation provides O(log(n)) time for the enqueing and dequeing methods (offer, poll, remove() and add)
	 * 
	 * non uso array di appoggio, ma solo variabili negli oggetti creati
	 * 
	 * @param grafo array di oggetti Nodo
	 * 
	 * Genera l'output richiesto a console
	 */
	public void dijkstra(boolean tipo){
		
		
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
				
				//caso solo strade normali, quindi costo = 0
				if(tipo && arco.c == 0){
					
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
				
				//caso tutte le strade
				else if(!tipo){
					
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
		}
		
		//scrittura output
		
		ArrayList<String> result1 = printPath(grafo);
		
		for(int i=result1.size()-1; i>=0; i--){
			
			System.out.println(result1.get(i));
		}
		
		System.out.println(grafo[grafo.length-1].weight);
		
		if(!tipo)
			System.out.println(grafo[grafo.length-1].cost);

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
		
		//Solo strade N
		soluzione.dijkstra(true);
		
		//Tutte le strade
		soluzione.dijkstra(false);
	}
}