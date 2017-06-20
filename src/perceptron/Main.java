package perceptron;

import java.text.DecimalFormat;

public class Main {
	
	private static int tiposDeLetras = 7;
	private static int variantesDeLetras = 3;
	private static int qtLetras = tiposDeLetras * variantesDeLetras;
	private static Letra[] letras = new Letra[tiposDeLetras * variantesDeLetras];
	private static char[] ordemDeLetras = {'A', 'B', 'C', 'D', 'E', 'J', 'K'};
	private static int[][] saida = 
			{{1,-1,-1,-1,-1,-1,-1},
			{-1,1,-1,-1,-1,-1,-1},
			{-1,-1,1,-1,-1,-1,-1},
			{-1,-1,-1,1,-1,-1,-1},
			{-1,-1,-1,-1,1,-1,-1},
			{-1,-1,-1,-1,-1,1,-1},
			{-1,-1,-1,-1,-1,-1,1}};
	public static int tamanhoLetra = 63;
	private static double taxa = 0.200;
	private static double bias = 1.000;
	private static double[][] pesosPorLetra = new double[7][63];
	
	public static void main(String[] args) {
		//inicializando cada letra com seu caractere, versão do caractere e saída esperada (por exemplo, A é 1000000
		for (int i = 0; i < qtLetras; i++) {
			letras[i] = new Letra(ordemDeLetras[i/variantesDeLetras], (i%variantesDeLetras)+1, saida[i/variantesDeLetras]);
			//System.out.println(i/variantesDeLetras + "-" + (i%variantesDeLetras + 1) + "-" + i/variantesDeLetras);
		}
		
		for(int l = 0; l < pesosPorLetra.length; l++) {
			Letra.inicializarPesos();
			int ciclo = 0;
			int respostasErradas = 0;
			do {
				respostasErradas = 0;
				ciclo++;
				System.out.println("Treinamento da letra " + ordemDeLetras[l] + ", ciclo " + ciclo);
				for(int i = 0; i < qtLetras; i++) {
					int _rede = rede(letras[i], l);
					int esperado = letras[i].getSaida()[l];
					int erro = esperado - _rede;
					if(erro != 0)
						respostasErradas++;
					/*System.out.println("Testando se " + letras[i].getNome() + letras[i].getNumero()
							+ " é lido como um " + ordemDeLetras[l] + ", resultado = "
							+ preRede(letras[i])
							+ ", esperava-se " + esperado + " erro foi "+ erro);
					System.out.println(mostraLinha(i+1, letras[i], l, esperado));*/
					for (int k = 0; k< tamanhoLetra; k++) {
						Letra.pesos[k] += erro * taxa * letras[i].getAmostras()[k];
						pesosPorLetra[l] = Letra.pesos;
					}
					
				}
				System.out.println("Erradas: " + respostasErradas);
				System.out.println("");
				
			} while (respostasErradas > 0);
			
			
			
		}
		for(int l = 0; l < pesosPorLetra.length; l++)
			System.out.println(mostraPesos(l));
	}

	//método que combina as variações da mesma letra, para que pixels tenham representação variada nas
	//mudanças de valor dos pesos
	public static double[] mediaDeAmostras(char c) {
		double[] resultado = new double[tamanhoLetra];
		for(Letra l: letras) {
			for(int i = 0; i < tamanhoLetra; i++) {
				if(l.getNome() == c)
					resultado[i] += (double) l.getAmostras()[i] / variantesDeLetras;
			}
		}
		return resultado;
	}
	
	public static int rede(Letra letra, int letraEsperada) {
		double soma = bias;
		//double soma = 0;
		for(int i = 0; i < letra.getAmostras().length; i++) {
			soma += letra.getAmostras()[i] * pesosPorLetra[letraEsperada][i];
		}
		return soma>0.0?1:-1;
	}
	
	public static double preRede(Letra letra, int letraEsperada) {
		double soma = bias;
		//double soma = 0;
		for(int i = 0; i < letra.getAmostras().length; i++) {
			soma += letra.getAmostras()[i] * pesosPorLetra[letraEsperada][i];
		}
		return soma;
	}
	
	//mostra os pesos da letra 0 = A, por exemplo
	public static String mostraPesos(int letra) {
		String resultado = "" + ordemDeLetras[letra] + " | ";
		DecimalFormat format = new DecimalFormat("#.000");
		for(int i = 0; i < tamanhoLetra; i++) {
			resultado += String.format("%6s", format.format(pesosPorLetra[letra][i]));
			resultado += " ";
		}
		return resultado;
	}
	
	public static String mostraLinha(int linha, Letra letra, int letraEsperada, int resultadoEsperado) {
		String resultado = String.format("%2s", linha);
		DecimalFormat format = new DecimalFormat("#.000");
		resultado += "|";
		resultado += letra.getNome() + "" + letra.getNumero() + " == " + ordemDeLetras[letraEsperada] + "? | ";
		resultado += "resultado = " + String.format("%6s", format.format(preRede(letra, letraEsperada))) + "| ";
		for(int i = 0; i< letra.getAmostras().length; i++) {
			resultado += String.format("%6s", format.format(letra.getAmostras()[i] * pesosPorLetra[letraEsperada][i]));
			resultado += " ";
		}
		resultado += "|";
		resultado += String.format("%2s", resultadoEsperado);
		resultado += " ";
		return resultado;
	}
}
