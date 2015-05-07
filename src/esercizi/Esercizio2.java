/**
 * 
 * 
 * Gianmarco Spinaci
 * 0000691241
 * gianmarco.spinaci@studio.unibo.it
 * 
 * 
 */



package esercizi;

import java.io.*;

public class Esercizio2 {
	
	static double R[][];

	public static void main(String[] args) {
		
		BufferedReader reader;
		BufferedWriter writer;
		
		int j, n;
		
		double penale;
		
		String rows;
		
		try{
			reader = new BufferedReader(new FileReader(new File("src/Esercizio2-1.in")));
			writer = new BufferedWriter(new FileWriter(new File("src/Esercizio2-1.out")));
			
			int cnt=0,tmp=0;
			
			penale = Double.parseDouble(reader.readLine());
			j = Integer.parseInt(reader.readLine());
			
			System.out.println(penale+" "+j);
			
		}catch(IOException e){
			
			System.out.println(e);
		}

	}

	private static void createRow(int index,String row){
		
		//System.out.println(row);
		
		String[] elem = row.split(" ");
		
		for(String x : elem){
			
			x.replaceAll("\\s","").replace("\n", "").replace("\r", "");
			System.out.println(x);
		}
		
		/*
		for(int i=0; i<elem.length; i++){
			
			R[index][i] = Double.parseDouble(elem[i]);
		}*/
	}
}
