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
 * Aggiornamento
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
class Nodo implements Comparable<Nodo>{
	
	//Salvo le coordinate del Nodo corrente e le coordinate del Nodo precedente
	Coord cord;
	Coord pred;
	double h;

	double w;
	
	ArrayList<Arco> archi = new ArrayList<>();
	
	/*
	 * Costruttore della classe nodo
	 * serve per tenere conto dell'altezza di ogni singolo nodo
	 * e delle coordinate sulla matrice 
	 * 
	 */
	Nodo(int x, int y, double h){
		
		this.cord = new Coord(x,y);
		this.h = h;
	}

	/*
	 * metodo che mi crea un nuovo arco partendo dal nodo corrente
	 * che ha come destinazione il nodo di Coordinate dst e peso w
	 */
	public void addArc(Coord dst, double w){
		
		archi.add(new Arco(dst,w));
	}
	
	/*
	 * Metodo che viene utilizzato unicamente in aggiunta alla struttura PriorityQueue
	 */
	public int compareTo(Nodo nodo)
    {
        return Double.compare(w, nodo.w);
    }
}


/**
 * Classe Arco utilizzata solo per la dot notation 
 * sull'arraylist di archi nella classe Nodo
 * 
 * racchiude informazioni sule coordinate dell'arco di destinazione 
 * e sul peso dell'arco
 *
 */
class Arco{
	 
	Coord dst;
	double w;
	
	Arco(Coord dst, double w){
		
		this.dst = dst;
		this.w = w;
	}
}
/**
 * Classe Coord utilizzata solo per la dot notation
 * e semplificare l'aggiunta di coordinate
 * 
 */
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
	
	Nodo[][] H;
	
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
			
			H = new Nodo[rowNum][colNum];
						
			//popolamento matrice 
			for(int i=0; i<rowNum; i++){
				
				String tmp = reader.readLine();
				String[] row = tmp.split("\\s+");
				
				for(int j=0; j<colNum; j++){
					
					H[i][j] = new Nodo(i,j,Double.parseDouble(row[j]));
				}
			}
			
			//metodo che crea gli archi
			createArcs();
		}
		
		catch(IOException e){
			
			System.out.println(e);
		}
	}
	
	
	/*
	 * Metodo di creazione degli archi
	 * 
	 * Il metodo è da rifinire, non è elegante, è hard-coded e spaghettiCode
	 * 
	 * crea degli archi per ogni nodo, quindi O(n)
	 * 
	 * il numero di archi che crea è definito in base alle sue coordinate:
	 * 
	 * i 4 nodi agli angoli della matrice hanno 2 archi
	 * gli [(n-2)+(m-2)] * 2 nodi che si trovano ai bordi hanno 3 archi
	 * tutti gli altri (n-2)*(m-2) nodi interni hanno 4 archi
	 * 
	 */
	public void createArcs(){
		
		for(int i=0; i<rowNum; i++){
			
			for(int j=0; j<colNum; j++){
				
				//caso dei 4 angoli o dei bordi
				if( i==0 || j==0 || i==rowNum-1 || j==colNum-1 ){
					
					//top-left
					if(i == 0 && j == 0){
						
						H[i][j].addArc(new Coord(i,j+1),Math.pow( H[i][j].h - H[i][j+1].h , 2 ));
						H[i][j].addArc(new Coord(i+1,j),Math.pow( H[i][j].h - H[i+1][j].h , 2 ));
					}
					
					//top-right
					else if( i == 0 && j == colNum-1){
						
						H[i][j].addArc(new Coord(i,j-1),Math.pow( H[i][j].h - H[i][j-1].h , 2 ));
						H[i][j].addArc(new Coord(i+1,j),Math.pow( H[i][j].h - H[i+1][j].h , 2 ));
					}
					
					//bottom-left
					else if( i == rowNum-1 && j == 0 ){
						
						H[i][j].addArc(new Coord(i-1,j),Math.pow( H[i][j].h - H[i-1][j].h , 2 ));
						H[i][j].addArc(new Coord(i,j+1),Math.pow( H[i][j].h - H[i][j+1].h , 2 ));
					}
					
					//bottom-right
					else if( i == rowNum-1 && j == colNum-1 ){
						
						H[i][j].addArc(new Coord(i-1,j),Math.pow( H[i][j].h - H[i-1][j].h , 2 ));
						H[i][j].addArc(new Coord(i,j-1),Math.pow( H[i][j].h - H[i][j-1].h , 2 ));
					}
					
					
					//prima riga
					else if( i==0 ){
						
						H[i][j].addArc(new Coord(i,j-1),Math.pow( H[i][j].h - H[i][j-1].h , 2 ));
						H[i][j].addArc(new Coord(i+1,j),Math.pow( H[i][j].h - H[i+1][j].h , 2 ));
						H[i][j].addArc(new Coord(i,j+1),Math.pow( H[i][j].h - H[i][j+1].h , 2 ));
					}
					
					//ultima riga
					else if( i == rowNum-1 ){
						
						H[i][j].addArc(new Coord(i,j+1),Math.pow( H[i][j].h - H[i][j+1].h , 2 ));
						H[i][j].addArc(new Coord(i-1,j),Math.pow( H[i][j].h - H[i-1][j].h , 2 ));
						H[i][j].addArc(new Coord(i,j-1),Math.pow( H[i][j].h - H[i][j-1].h , 2 ));
					}
					
					//prima colonna
					else if( j==0 ){
						
						H[i][j].addArc(new Coord(i-1,j),Math.pow( H[i][j].h - H[i-1][j].h , 2 ));
						H[i][j].addArc(new Coord(i,j+1),Math.pow( H[i][j].h - H[i][j+1].h , 2 ));
						H[i][j].addArc(new Coord(i+1,j),Math.pow( H[i][j].h - H[i+1][j].h , 2 ));
					}
					
					//ultima colonna
					else if( j==colNum-1 ){
						
						H[i][j].addArc(new Coord(i-1,j),Math.pow( H[i][j].h - H[i-1][j].h , 2 ));
						H[i][j].addArc(new Coord(i,j-1),Math.pow( H[i][j].h - H[i][j-1].h , 2 ));
						H[i][j].addArc(new Coord(i+1,j),Math.pow( H[i][j].h - H[i+1][j].h , 2 ));
					}	
				}
				
				//caso dei nodi centrali
				else{
					
					//arco vs. nodo superiore
					H[i][j].addArc(new Coord(i-1,j),Math.pow( H[i][j].h - H[i-1][j].h , 2 ));
					
					//arco vs. nodo inferiore
					H[i][j].addArc(new Coord(i+1,j),Math.pow( H[i][j].h - H[i+1][j].h , 2 ));
					
					//arco vs. nodo sinistro
					H[i][j].addArc(new Coord(i,j-1),Math.pow( H[i][j].h - H[i][j-1].h , 2 ));
					
					//arco vs. nodo destro
					H[i][j].addArc(new Coord(i,j+1),Math.pow( H[i][j].h - H[i][j+1].h , 2 ));					
				}
			}
		}
	}

	/*
	 * Richiamo l'algoritmo di Dijkstra che mi serve per trovare il cammino minimo
	 * tra il nodo di coordinate 0;0 e quello di coordinate N-1;M-1 nella matrice
	 * 
	 * Nota:
	 * Ogni nodo ha M = 2,3,4 archi, verso altri nodi adiacenti
	 */
	public double dijkstra(){
		
		/*
		 * Inizializzazione dell'algoritmo costo di O(n)
		 * con n = numero nodi, ed è dato da numRow * numCol
		 */
		for(Nodo[] row : H){
			
			for(Nodo element : row){
				
				element.w = Double.POSITIVE_INFINITY;
				element.pred = new Coord(-1,-1);
			}
		}
		
		/*
		 * creazione del MinHeap di oggetti di tipo Nodo
		 */
		PriorityQueue<Nodo> Q = new PriorityQueue<>();
		
		H[0][0].w = 0;
		
		Q.add(H[0][0]);
		
		
		while( !Q.isEmpty() ){
			
			Nodo u = Q.poll();
			
			for( Arco arco : u.archi){
				
				Nodo archToNode = H[arco.dst.x][arco.dst.y];
				
				if( archToNode.w == Double.POSITIVE_INFINITY){
					
					/*
					 * 
					 * setto la nuova altezza,
					 * data dalla somma tra cHeight * quadrato del dislivello tra le celle,
					 * il costo cCell e il peso del nodo corrente
					 * 
					 */
					archToNode.w = u.w + cCell + ( arco.w * cHeight );
					
					archToNode.pred = u.cord;
					
					Q.add(archToNode);
				}
				
				else if( u.w + cCell + ( arco.w * cHeight ) < archToNode.w ){
					
					archToNode.w = u.w + cCell + ( arco.w * cHeight );
					archToNode.pred = u.cord;
				}
			}
		}
		
		//salvo le coordinate del percorso minimo
		ArrayList<Coord> ris = getPath();
		
		
		for(int i=ris.size()-1;i>0;i--){
			
			System.out.println(ris.get(i));
		}
		
		System.out.println( ( rowNum-1 ) + " " + ( colNum-1 ) );
		
		return H[rowNum-1][colNum-1].w;
	}
	/*
	 * Metodo che ricrea il percorso, partendo dal nodo destinazione
	 * e salve le coordinate del percorso minimo in un arraylist<string>
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

		String nomeFile = args[0];
		
		SoluzioneEsericizio3 soluzione = new SoluzioneEsericizio3(nomeFile);
		
		double ris = soluzione.dijkstra();
		
		System.out.println(ris);
	}

}
