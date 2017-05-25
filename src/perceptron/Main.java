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
	private static int tamanhoLetra = 63;
	private static double[][] pesos = new double[tiposDeLetras][tamanhoLetra];
	private static double taxa = 0.1;

	public static double[][] inicializarPesos(double[][] pesos) {
		for (int i = 0; i < tiposDeLetras; i++) {
			for (int j = 0; j < pesos.length; j++) {
				pesos[i][j] = 0.0;
			}
		}
		return pesos;
	}
	
	public static void main(String[] args) {
		//inicializando cada letra com seu caractere, versão do caractere e saída esperada (por exemplo, A é 1000000
		for (int i = 0; i < qtLetras; i++) {
			letras[i] = new Letra(ordemDeLetras[i/variantesDeLetras], (i/tiposDeLetras)+1, saida[i/variantesDeLetras]);
		}
		pesos = inicializarPesos(pesos);
		for(int i = 0; i < qtLetras; i++) {
			double[] z = new double[tamanhoLetra];
			for(int j = 0; j < tamanhoLetra; j++) {
				z[j] = letras[i].getAmostras()[j] * pesos[i/3][j];
			}
			System.out.println(mostraLinha(i+1, z, letras[i].getSaida()));
		}
	}

	public static String mostraLinha(int linha, double[] valores, int[] saida) {
		String resultado = String.format("%2s", linha);
		resultado += " | ";
		DecimalFormat format = new DecimalFormat("#.00");
		for(double v: valores) {
			resultado += String.format("%5s", format.format(v));
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
