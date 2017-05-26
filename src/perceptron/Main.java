package perceptron;

import java.text.DecimalFormat;

public class Main {
	
	private static int tiposDeLetras = 7;
	private static int variantesDeLetras = 3;
	private static int qtLetras = tiposDeLetras * variantesDeLetras;
	private static Letra[] letras = new Letra[tiposDeLetras * variantesDeLetras];
	private static String[] ordemDeLetras = {"A", "B", "C", "D", "E", "J", "K"};
	private static int[][] saida = 
			{{1,0,0,0,0,0,0},
			{0,1,0,0,0,0,0},
			{0,0,1,0,0,0,0},
			{0,0,0,1,0,0,0},
			{0,0,0,0,1,0,0},
			{0,0,0,0,0,1,0},
			{0,0,0,0,0,0,1}};
	public static int tamanhoLetra = 63;
	private static double taxa = 0.1;

	public static double[][] inicializarPesos(double[][] pesos) {
		for (int i = 0; i < qtLetras; i++) {
			letras[i].inicializarPesos();
		}
		return pesos;
	}
	
	public static void main(String[] args) {
		//inicializando cada letra com seu caractere, versão do caractere e saída esperada (por exemplo, A é 1000000
		for (int i = 0; i < qtLetras; i++) {
			letras[i] = new Letra(ordemDeLetras[i/variantesDeLetras], (i%variantesDeLetras)+1, saida[i/variantesDeLetras]);
			//System.out.println(i/variantesDeLetras + "-" + (i%variantesDeLetras + 1) + "-" + i/variantesDeLetras);
		}
		for (Letra l : letras) {
			l.inicializarPesos();
		}
		
		
		//double[][] z = new double[qtLetras][tamanhoLetra];
//		for(int i = 0; i < tiposDeLetras; i++) {
//			
//			for(int j = 0; j < tamanhoLetra; j++) {
//				z[i*3][j] = letras[i*3].getAmostras()[j] * pesos[i][j];
//				z[i*3+1][j] = letras[i*3+1].getAmostras()[j] * pesos[i][j];
//				z[i*3+2][j] = letras[i*3+2].getAmostras()[j] * pesos[i][j];
//				//System.out.println(letras[i*3].getNome() + letras[i*3].getNumero());
//				//System.out.println(letras[i*3+1].getNome() + letras[i*3+1].getNumero());
//				//System.out.println(letras[i*3+2].getNome() + letras[i*3+2].getNumero());
//			}
//			//System.out.println(letras[i].getNome() + letras[i].getNumero());
//			//System.out.println(mostraLinha(i+1, z, letras[i].getSaida()));
//		}
		
		
		for(int i = 0; i < qtLetras; i++) {
			System.out.println(mostraLinha(i+1, letras[i], letras[i].getSaida()));
		}
	}

	public static String mostraLinha(int linha, Letra letra, int[] saida) {
		String resultado = String.format("%2s", linha);
		resultado += " |";
		resultado += letra.getNome() + "-" + letra.getNumero() + "| ";
		DecimalFormat format = new DecimalFormat("#.00");
		for(int i = 0; i< letra.getAmostras().length; i++) {
			resultado += String.format("%5s", format.format(letra.getAmostras()[i] * letra.getPesos()[i]));
			resultado += " ";
		}
		resultado += "|";
		for(int i: saida) {
			resultado += String.format("%2s", i);
			resultado += " ";
		}
		return resultado;
	}
}
