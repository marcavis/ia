package perceptron;

import java.text.DecimalFormat;

public class Main {
	
	private static int tiposDeLetras = 7;
	private static int variantesDeLetras = 1;
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
	/*private static int[][] saida = 
		{{1,0,0,0,0,0,0},
		{0,1,0,0,0,0,0},
		{0,0,1,0,0,0,0},
		{0,0,0,1,0,0,0},
		{0,0,0,0,1,0,0},
		{0,0,0,0,0,1,0},
		{0,0,0,0,0,0,1}};*/
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
		
		for(int l = 0; l < pesosPorLetra.length; l++) {
			Letra.inicializarPesos();
			int ciclo = 0;
			while (ciclo < 4) {
				int respostasCertas = 0;
				int respostasErradas = 0;
				ciclo++;
				System.out.println("Ciclo: " + ciclo);
				/*
				for(int i = 0; i < qtLetras; i++) {
					for(int h = 0; h < tiposDeLetras; h++) {
	
						//double erro = achaErro(testaLinha(letras[i]), letras[i].getSaida()[h]);
						//if(erro >)
						int _rede = rede(letras[i]);
						int esperado = letras[i].getSaida()[h];
						int erro = esperado - _rede;
						System.out.println(erro);
						//System.out.println(erro);
						//System.out.println(testaLinha(letras[i]));
						System.out.println("Testando se " + letras[i].getNome() + letras[i].getNumero()
								+ " é lido como um " + ordemDeLetras[h] + ", resultado = "
								+ rede(letras[i])
								+ ", esperava-se " + letras[i].getSaida()[h] + " erro foi "+ erro);
						System.out.println(mostraLinha(i+1, letras[i], letras[i].getSaida()));
						//System.out.println(letras[i].getSaida()[h]);
						
						if(_rede == esperado) {
							respostasCertas++;
							//System.out.println("sucesso para letra " + letras[i].getNome() + letras[i].getNumero());
						} else {
							respostasErradas++;
							//System.out.println("fracasso");
							for(int j = 0; j < tamanhoLetra; j++) {
								for(Letra l: letras) {
									//alterar os pesos de todos os A ao mesmo tempo, ou todos os J, etc.
									if(l.getNome() == letras[i].getNome()) {
										l.pesos[j] = l.pesos[j] + taxa * mediaDeAmostras(l.getNome())[j] * (erro+2);
										//System.out.println(l.pesos[j] + "X" + taxa + "X" + mediaDeAmostras(l.getNome())[j] + "X" + erro + "X" + bias);
										//bias = bias + taxa * esperado;
									}
								}
							}
						}
					}
				}
				System.out.println("Certas: " + respostasCertas);
				System.out.println("Erradas: " + respostasErradas);
				System.out.println("");
			}
			*/
				for(int i = 0; i < qtLetras; i++) {
					int _rede = rede(letras[i]);
					int esperado = letras[i].getSaida()[l];
					int erro = esperado - _rede;
					if(erro == 0)
						respostasCertas++;
					else
						respostasErradas++;
							
					for (int k = 0; k< tamanhoLetra; k++) {
						Letra.pesos[k] += erro * taxa * letras[i].getAmostras()[k];
						pesosPorLetra[l] = Letra.pesos;
					}
					//System.out.println("Testando se " + letras[i].getNome() + letras[i].getNumero()
					//		+ " é lido como um " + ordemDeLetras[l] + ", resultado = "
					//		+ rede(letras[i])
					//		+ ", esperava-se " + esperado + " erro foi "+ erro);
					//System.out.println(mostraLinha(i+1, letras[i], letras[i].getSaida()));
				}
				
			}
			
			System.out.println(mostraPesos(ordemDeLetras[l]));
			
		}
		
	}

	//método que combina as variaçõerede(s da mesma letra, para que pixels tenham representação variada nas
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
	
	public static double achaErro(double resultado, int esperado) {
		return resultado - esperado;
		/*if(resultado > esperado)
			return 1;
		else if (resultado < esperado)
			return -1;
		else
			return 0;*/
	}
	
	//public static boolean isPesosCorretos(Letra letra, int resultadoDaFuncaoRede) {
	//	
	//}
	
//	public static int rede(double saida) {
//		if(saida > 0.0)
//			return 1;
//		else 
//			return -1;
//	}
	
	public static int rede(Letra letra) {
		double soma = bias;
		//double soma = 0;
		for(int i = 0; i < letra.getAmostras().length; i++) {
			soma += letra.getAmostras()[i] * Letra.pesos[i];
		}
		return soma>0.0?1:-1;
	}
	
	public static String mostraPesos(char letraDoPeso) {
		String resultado = "" + letraDoPeso;
		DecimalFormat format = new DecimalFormat("#.000");
		for(int i = 0; i < tamanhoLetra; i++) {
			resultado += String.format("%6s", format.format(Letra.pesos[i]));
			resultado += " ";
		}
		return resultado;
	}
	
	public static String mostraLinha(int linha, Letra letra, int[] saida) {
		String resultado = String.format("%2s", linha);
		resultado += " |";
		resultado += letra.getNome() + "-" + letra.getNumero() + "| ";
		DecimalFormat format = new DecimalFormat("#.000");
		for(int i = 0; i< letra.getAmostras().length; i++) {
			resultado += String.format("%6s", format.format(letra.getAmostras()[i] * Letra.pesos[i]));
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
