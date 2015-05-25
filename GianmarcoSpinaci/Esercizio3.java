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
 * DATA -> 12/05/2015
 * 
 * In questo caso sarebbe preferibile trasformare ogni cella in un nodo di un grafo
 * ogni nodo può avere 2,3 o 4 archi
 * 
 * il numero degli archi è dato da 
 * 
 * archiAngoli  = 4 * 2 					i 4 nodi agli angoli possono avere solo 2 archi
 * archiSpigoli = 3 * ( ( n-2 ) + ( m-2 ) * 2) i nodi sugli spigoli della matrice hanno solo 3 archi
 * archiInterni = 4 * ( ( n-2 ) * ( m-2 ) ) tutti i restanti nodi interni hanno 4 archi
 * 
 * 
 * Con l'algoritmo di Dijkstra il costo è O(m log n)
 * 
 * Quindi trasformo ogni cella in un a nodo e gli attribuisco i giusti archi
 * dopo aver creato il grafo utilizzo l'algoritmo di Dijkstra, vedo se è corretto 
 * 
 * Algoritmo di Dijkstra funziona solo con archi di peso > 0 in questo caso la distanza euclidea non darà mai un risultato negativo
 * 
 * 
 * Completato esercizio, avevo un problema con l'input e l'output ma è stato aggiornato.
 * 
 * 
 * 
 */


import java.io.*;
import java.util.ArrayList;
import java.util.PriorityQueue;


/**
 * 
 * Classe Nodo, usata per tenere conto degli oggetti per ogni cella della matrice
 * 
 * Creo un Nodo per ogni cella della matrice, passando le coordinate e l'altezza
 * E' comparabile in base al peso corrispondente, 
 * posso inserire ogni singolo nodo nella PriorityQueue nella posizione esatta
 */
class NodoE3 implements Comparable<NodoE3>{
	
	//Salvo le coordinate del Nodo corrente e le coordinate del Nodo precedente
	Coord cord;
	Coord pred;

	double h;
	double w;
	
	ArrayList<ArcoE3> archi = new ArrayList<>();
	
	/*
	 * Costruttore della classe nodo
	 * serve per tenere conto dell'altezza di ogni singolo nodo
	 * e delle coordinate sulla matrice 
	 * 
	 */
	NodoE3(int x, int y, double h){
		
		this.cord = new Coord(x,y);
		this.h = h;
	}

	/*
	 * metodo che mi crea un nuovo arco partendo dal nodo corrente
	 * che ha come destinazione il nodo di Coordinate dst e peso w
	 */
	public void addArc(Coord dst, double w){
		
		archi.add(new ArcoE3(dst,w));
	}
	
	/*
	 * Metodo che viene utilizzato unicamente in aggiunta alla struttura PriorityQueue
	 */
	@Override
	public int compareTo(NodoE3 nodo)
    {
        return Double.compare(w, nodo.w);
    }

	public String toString(){
		
		return h+" "+w;
	}
}


/**
 * Classi Arco e Coord utilizzate solo per la dot notation  *
 */
class ArcoE3{
	 
	Coord dst;
	double w;
	
	ArcoE3(Coord dst, double w){
		
		this.dst = dst;
		this.w = w;
	}
	
	public String toString(){
		
		return dst+" "+w;
	}
}

class Coord{
	
	int x, y;
	
	Coord(int x, int y){
		this.x = x; this.y = y;
	}
	
	public String toString(){
		
		return x+" "+y;
	}
}

/**
 * Classe Soluzione
 * 
 * Accetta nel costruttore il nome del file di input e crea la matrice di Nodi,
 * Per ogni nodo crea i suoi archi
 * 
 * Non ho ancora trovato un pattern per rendere più elegante la creazione di archi
 * 
 * AGGIORNAMENTO 22/05 pattern trovato
 * 
 * Ritorna il risultato dell'algoritmo di Dijkstra come numero double
 * e scrive il percorso con costo minore per la creazione della strada
 * sotto forma di 
 * 
 * 		src dst
 * 
 * dove:
 * 	src => nodo sorgente
 *	dst => nodo destinazione
 *
 */
class SoluzioneEsericizio3{
	
	BufferedReader reader;
	
	double cCell, cHeight;
	int rowNum, colNum;
	
	NodoE3[][] H;
	
	/*
	 * Accetta il file di input da leggere e popola la matrice di nodi
	 * rowNum * colNum = n (numero nodi)
	 * 
	 * il popolamento della matrice NxM è O(n)
	 */
	SoluzioneEsericizio3(String nomeFile){
		
		try{
			
			reader = new BufferedReader(new FileReader(new File(nomeFile)));
			
			cCell = Double.parseDouble(reader.readLine());
			cHeight = Double.parseDouble(reader.readLine());
			
			rowNum = Integer.parseInt(reader.readLine());
			colNum = Integer.parseInt(reader.readLine());
			
			H = new NodoE3[rowNum][colNum];
						
			//popolamento matrice 
			for(int i=0; i<rowNum; i++){
				
				String tmp = reader.readLine();
				String[] row = tmp.split("\\s+");
				
				for(int j=0; j<colNum; j++){
					
					H[i][j] = new NodoE3(i,j,Double.parseDouble(row[j]));
				}
			}

			creaArchi();
		}
		
		catch(IOException e){
			
			System.out.println(e);
		}
	}
	
	/*
	 * Metodo di creazione degli archi V2
	 * 
	 * V1:
	 * hard-coded e non elegante
	 * 
	 * i 4 nodi agli angoli della matrice hanno 2 archi
	 * gli [(n-2)+(m-2)] * 2 nodi che si trovano ai bordi hanno 3 archi
	 * tutti gli altri (n-2)*(m-2) nodi interni hanno 4 archi
	 * 
	 * Valutazione per ogni singolo caso
	 * 
	 * V2:
	 * Ho trovato un pattern, creo due archi per ogni nodo corrente
	 * e creo gli archi speculari partenti dai nodi di destinazione
	 * 
	 * Dopo il for ho tutta la matrice creata con tutti gli archi, 
	 * tranne che per l'ultima riga e colonna
	 * 
	 * In questo caso scorro semplicemente la riga e la colonna e creo gli archi
	 * 
	 * COSTO O(n)
	 * n -> numero di nodi
	 */
	private void creaArchi(){
		
		int lastRow = rowNum-1;
		int lastCol = colNum-1;
		
		//creo tutti gli archi della matrice H[0...n-1;0...m-1]
		for(int i=0; i<lastRow; i++){
			
			for(int j=0; j<lastCol; j++){
				
				double w1 = cHeight * Math.pow( H[i][j].h - H[i][j+1].h ,2);
				double w2 = cHeight * Math.pow( H[i][j].h - H[i+1][j].h, 2);
				//nodo vs. destro
				H[i][j].addArc(new Coord(i,j+1), w1);
				
				//nodo vs. inferiore
				H[i][j].addArc(new Coord(i+1,j), w2);
				
				//nodo destro vs. nodo
				H[i][j+1].addArc(new Coord(i,j), w1);
				
				//nodo inferiore vs. nodo
				H[i+1][j].addArc(new Coord(i,j), w2);
			}
		}
		
		//creo gli archi dell'ultima riga
		//ultima riga
		for(int j=0; j<lastCol;j++){
			
			double w = cHeight *  Math.pow( H[lastRow][j].h - H[lastRow][j+1].h , 2 );
			
			//arco vs. nodo destro
			H[lastRow][j].addArc(new Coord(lastRow,j+1),w);
			H[lastRow][j+1].addArc(new Coord(lastRow,j),w);
		}
		
		//ultima colonna		
		for(int i=0; i<lastRow; i++){
			
			double w = cHeight * Math.pow( H[i][lastCol].h - H[i+1][lastCol].h , 2 );
			
			H[i][lastCol].addArc(new Coord(i+1,lastCol),w);
			H[i+1][lastCol].addArc(new Coord(i,lastCol),w);
		}		
	}

	/*
	 * Richiamo l'algoritmo di Dijkstra che mi serve per trovare il cammino minimo
	 * tra il nodo di coordinate 0;0 e quello di coordinate N-1;M-1 nella matrice
	 * 
	 * Nota:
	 * Ogni nodo ha M = 2,3,4 archi, verso altri nodi adiacenti
	 */
	public void dijkstra(){
		
		NodoE3 tmp;
		
		/*
		 * Inizializzazione dell'algoritmo costo di O(n)
		 * con n = numero nodi, ed è dato da numRow * numCol
		 */
		for(NodoE3[] row : H){
			
			for(NodoE3 element : row){
				
				element.w = Double.POSITIVE_INFINITY;
				element.pred = new Coord(-1,-1);
			}
		}
		
		
		//creazione del MinHeap di oggetti di tipo Nodo
		PriorityQueue<NodoE3> Q = new PriorityQueue<>();
		
		H[0][0].w = 0;
		
		Q.add(H[0][0]);
		
		while( !Q.isEmpty() ){
			
			NodoE3 u = Q.poll();
			
			
			for(ArcoE3 arco : u.archi){
				
				NodoE3 nodoDestinazione = H[arco.dst.x][arco.dst.y];
				
				if(nodoDestinazione.w == Double.POSITIVE_INFINITY){
					
					nodoDestinazione.w = u.w + arco.w;
					nodoDestinazione.pred = u.cord;
					
					//O(log n)
					Q.add(nodoDestinazione);
				}
				
				else if(u.w + arco.w < nodoDestinazione.w){
					
					nodoDestinazione.w = u.w + arco.w;
					nodoDestinazione.pred = u.cord;
					
					//equivalente del decrease key
					//O(log n)
					if( (tmp=Q.poll()) != null)
						Q.add(tmp);
				}
			}
		}
		
		//scrittura output 
		
		ArrayList<Coord> ris = getPath();
				
		for(int i=ris.size()-1;i>0;i--){
						
			System.out.println(ris.get(i));
		}

		System.out.println( ( rowNum-1 ) + " " + ( colNum-1 ) );
		System.out.println((H[rowNum-1][colNum-1].w + cCell * ris.size()));
	}
	/*
	 * Metodo che ricrea il percorso, partendo dal nodo destinazione
	 * e salva le coordinate del percorso minimo in un arraylist<string>
	 */
	private ArrayList<Coord> getPath(){
		
		ArrayList<Coord> result = new ArrayList<>();
		
		Coord currentIndex = new Coord(rowNum-1,colNum-1);
		
		while(currentIndex.x != -1 && currentIndex.y != -1){
						
			result.add(currentIndex);
			
			currentIndex = H[currentIndex.x][currentIndex.y].pred;
		}
		
		return result;
	}
}

public class Esercizio3 {

	public static void main(String[] args) {

		new SoluzioneEsericizio3(args[0]).dijkstra();
	}
}
