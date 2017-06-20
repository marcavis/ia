package genetico;

import java.util.ArrayList;
import java.util.Random;

public class Main {

	//Configurações
	public static int tamPopulacao = 1000;
	public static double taxaDeCrossover = 0.60;
	public static boolean usarElitismo = true;
	public static double probabilidadeDeMutacao = 0.005; //chance de um cromossomo sofrer alteracoes
	public static double forcaDeMutacao = 0.1; //chance de algum bit específico ser alterado num cromossomo mutante
	public static boolean usarLimiteDeGeracoes = false;
	public static int limiteDeGeracoes = 100;
	public static boolean usarLimiteDeEstabilizacao = true;
	public static double limiteDeEstabilizacao = 0.005; //se em 5 gerações o fitness acumulado não crescer nessa proporção, parar
	
	public static int tamCromossomo = 36;
	public static Random gerador = new Random();
	
	public static void main(String[] args) {
		//int[] b = geraBotoes();
		//int[][] populacao = new int[tamPopulacao][tamCromossomo];
		ArrayList<int[]> populacao = new ArrayList<int[]>();
		//int[] b = {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
		//System.out.println(mostraBotoes(b));
		//System.out.println(aptidao(b));
		for(int i = 0; i < tamPopulacao; i++) {
			populacao.add(geraBotoes());
		}
		//for (int[] is : populacao) {
		//	System.out.println(mostraBotoes(is) + " " + aptidao(is));
		//}
		ArrayList<Integer> somasDeFitness = new ArrayList<Integer>();
		int geracao = 0;
		do {
			if(usarElitismo) {
				populacao = mutacao(crossoverElitista(populacao));
			} else {
				populacao = mutacao(crossoverRoleta(populacao));
			}
			int[] aptidoes = new int[populacao.size()];
			for(int i = 0; i < aptidoes.length; i++) {
				aptidoes[i] = aptidao(populacao.get(i));
			}
			somasDeFitness.add(soma(aptidoes));
			geracao++;
		} while ((usarLimiteDeGeracoes && geracao < limiteDeGeracoes) || 
				(usarLimiteDeEstabilizacao && !detectarEstabilizacao(somasDeFitness, limiteDeEstabilizacao)));
		//for (int[] is : populacao) {
		//	System.out.println(aptidao(is));
		//}
	}
	
	public static int[] geraBotoes() {
		int[] botoes = new int[tamCromossomo];
		Random gerador = new Random();
		for(int i = 0; i < tamCromossomo; i++) {
			botoes[i] = gerador.nextInt(2);
		}
		return botoes;
	}

	public static ArrayList<int[]> mutacao(ArrayList<int[]> pop) {
		for (int i = 0; i < pop.size(); i++) {
			if(gerador.nextDouble() < probabilidadeDeMutacao) {
				int[] mutante = pop.get(i);
				//System.out.println(mostraBotoes(mutante));
				for(int j = 0; j < mutante.length; j++) {
					if(gerador.nextDouble() < forcaDeMutacao) {
						mutante[j] = (mutante[j] + 1) % 2;
					}
				}
				pop.set(i, mutante);
				//System.out.println(mostraBotoes(mutante));
			}
		}
		return pop;
	}
	
	public static ArrayList<int[]> crossoverRoleta(ArrayList<int[]> pop) {
		ArrayList<int[]> novaPop = new ArrayList<int[]>();
		int[] aptidoes = new int[pop.size()];
		for(int i = 0; i < aptidoes.length; i++) {
			aptidoes[i] = aptidao(pop.get(i));
		}
		for(int i = 0; i < pop.size();) {
			if(gerador.nextDouble() < taxaDeCrossover) {
				int[] pai1 = pop.get(escolherRoleta(aptidoes, gerador.nextInt(soma(aptidoes))));
				int[] pai2 = pop.get(escolherRoleta(aptidoes, gerador.nextInt(soma(aptidoes))));
				int pontoDeCorte = 1 + gerador.nextInt(tamCromossomo - 2); //evitar pontos de corte nos extremos
				novaPop.add(cruzar(pai1, pai2, pontoDeCorte));
				novaPop.add(cruzar(pai2, pai1, pontoDeCorte));
				i += 2;
			}
		}
		System.out.println(soma(aptidoes));
		return novaPop;
	}
	
	public static ArrayList<int[]> crossoverElitista(ArrayList<int[]> pop) {
		ArrayList<int[]> novaPop = new ArrayList<int[]>();
		//colocar os cromossomos em ordem decrescente de acordo com a função de aptidão
		pop.sort((o1, o2) -> aptidao(o2).compareTo(aptidao(o1)));
		int[] aptidoes = new int[pop.size()];
		for(int i = 0; i < aptidoes.length; i++) {
			aptidoes[i] = aptidao(pop.get(i));
		}
		for(int i = 0; i < pop.size();) {
			if(gerador.nextDouble() < taxaDeCrossover) {
				int[] pai1 = pop.get(gerador.nextInt(pop.size()/2));
				int[] pai2 = pop.get(gerador.nextInt(pop.size()/2));
				int pontoDeCorte = 1 + gerador.nextInt(tamCromossomo - 2); //evitar pontos de corte nos extremos
				novaPop.add(cruzar(pai1, pai2, pontoDeCorte));
				novaPop.add(cruzar(pai2, pai1, pontoDeCorte));
				i += 2;
			}
		}
		System.out.println(soma(aptidoes));
		return novaPop;
	}
	
	//escolher o cromossomo apropriado conforme a frequência acumulada
	public static int escolherRoleta(int[] aptidoes, int valorAleatorio) {
		int somaDeAptidoesPercorridas = 0;
		for (int i = 0; i < aptidoes.length; i++) {
			if(somaDeAptidoesPercorridas + aptidoes[i] > valorAleatorio)
				return i;
			somaDeAptidoesPercorridas += aptidoes[i];
		}
		return aptidoes.length - 1;
	}
	
	//retorna o primeiro dos filhos; chame novamente com os pais invertidos
	//e mesmo ponto de corte para obter o segundo filho
	public static int[] cruzar(int[] pai1, int[] pai2, int corte) {
		int[] filho = new int[pai1.length];
		for(int i = 0; i < pai1.length; i++) {
			if(i < corte)
				filho[i] = pai1[i];
			else
				filho[i] = pai2[i];
		}
		return filho;
	}
	
	public static int soma(int[] vetor) {
		int resultado = 0;
		for (int i : vetor) {
			resultado += i;
		}
		return resultado;
	}
	
	public static Integer aptidao(int[] b) {
		return 9 + b[1] * b[4] - b[22] * b[13] + b[23] * b[3] - b[20] * b[9] + b[35] * b[14] 
				- b[10] * b[25] + b[15] * b[16] + b[2] * b[32] + b[27] * b[18] + b[11] * b[33] 
				- b[30] * b[31] - b[21] * b[24] + b[34] * b[26] - b[28] * b[6] + b[7] * b[12]
				- b[5] * b[8] + b[17] * b[19] - b[0] * b[29] + b[22] * b[3] + b[20] * b[14]
				+ b[25] * b[15] + b[30] * b[11] + b[24] * b[18] + b[6] * b[7] + b[8] * b[17]
				+ b[0] * b[32];
	}
	
	public static String mostraBotoes(int[] b) {
		String resultado = "[";
		int i;
		for(i = 0; i < tamCromossomo - 4; i += 4)
			resultado += (b[i] * 8 + b[i+1] * 4 + b[i+2] * 2 + b[i+3]) + ", ";
		resultado += (b[i] * 8 + b[i+1] * 4 + b[i+2] * 2 + b[i+3]) + "]";
		return resultado;
	}
	
	public static boolean detectarEstabilizacao(ArrayList<Integer> somasDeFitness, double limiteDeEstabilizacao) {
		if(somasDeFitness.size() < 5) {
			return false;
		} else {
			boolean resultado = true;
			int quantidadeDeGeracoes = somasDeFitness.size();
			int ultimo = somasDeFitness.get(quantidadeDeGeracoes - 1);
			for(int i = 1; i <= 5; i++) {
				if(Math.abs(1 - (double) ultimo / somasDeFitness.get(quantidadeDeGeracoes - i) ) > limiteDeEstabilizacao) {
					return false;
				}
			}
			return resultado;
		}
	}

}
