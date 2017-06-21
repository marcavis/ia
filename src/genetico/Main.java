package genetico;

import java.util.ArrayList;
import java.util.Random;

public class Main {

	//Configurações
	public static int tamPopulacao = 1000;
	public static double taxaDeCrossover = 0.60;
	public static boolean usarElitismo = true;
	public static double probabilidadeDeMutacao = 0.010; //chance de um cromossomo sofrer alteracoes
	public static double forcaDeMutacao = 0.1; //chance de algum bit específico ser alterado num cromossomo mutante
	public static boolean usarLimiteDeGeracoes = true;
	public static int limiteDeGeracoes = 500;
	public static boolean usarLimiteDeEstabilizacao = true;
	public static double limiteDeEstabilizacao = 0.005; //se em 10 gerações o fitness acumulado não crescer nessa proporção, parar
	
	public static int tamCromossomo = 36;
	public static Random gerador = new Random();
	
	public static void main(String[] args) {
		ArrayList<int[]> populacao = new ArrayList<int[]>();
		for(int i = 0; i < tamPopulacao; i++) {
			populacao.add(geraBotoes());
		}
		
		ArrayList<Integer> somasDeFitness = new ArrayList<Integer>();
		int geracao = 1;
		int[] melhorCromossomo = populacao.get(0);
		if(!usarLimiteDeEstabilizacao && !usarLimiteDeGeracoes) {
			System.out.println("Habilite ao menos um método de limitar o processamento do programa!");
			return;
		}
		
		String cabecalho = "Resolvendo com populações de " + tamPopulacao + " cromossomos,\nusando o método "
				+ (usarElitismo ? "elitista" : "da roleta") + " para selecionar pares para cruzamento,\n"
				+ "usando taxa de crossover de " + (taxaDeCrossover*100) + "%,\n"
				+ "taxa de mutação de " + (probabilidadeDeMutacao*100) + "%, alterando "
				+ (forcaDeMutacao*tamCromossomo) + " genes em média.\n"
				+ "O processamento está limitado a" + (usarLimiteDeGeracoes ? " " + limiteDeGeracoes + " gerações":"")
				+ (usarLimiteDeGeracoes && usarLimiteDeEstabilizacao ? " ou":"")
				+ (usarLimiteDeEstabilizacao ? " convergência de " + (limiteDeEstabilizacao*100) + "% nas 10 últimas gerações.":".");
		System.out.println(cabecalho);
		do {
			if(usarElitismo) {
				populacao = mutacao(crossoverElitista(populacao));
			} else {
				populacao = mutacao(crossoverRoleta(populacao));
			}
			populacao.sort((o1, o2) -> aptidao(o2).compareTo(aptidao(o1)));
			
			//Ordenando novamente a população em ordem decrescente de aptidão, o 1º elemento
			//é o melhor da geração atual
			if(aptidao(populacao.get(0)) > aptidao(melhorCromossomo)) {
				melhorCromossomo = populacao.get(0);
			}
			
			int[] aptidoes = new int[populacao.size()];
			for(int i = 0; i < aptidoes.length; i++) {
				aptidoes[i] = aptidao(populacao.get(i));
			}
			int somaDeAptidoesDestaPopulacao = soma(aptidoes);
			System.out.println("Geração nº " + geracao + ": aptidão total: "
					+ somaDeAptidoesDestaPopulacao + "\n\tmelhor elemento: "
					+ mostraBotoes(melhorCromossomo) + ", com aptidão " + aptidao(melhorCromossomo));
			
			somasDeFitness.add(somaDeAptidoesDestaPopulacao);
			geracao++;
		} while (!(
					(usarLimiteDeGeracoes && geracao > limiteDeGeracoes) || 
					(usarLimiteDeEstabilizacao && detectarEstabilizacao(somasDeFitness, limiteDeEstabilizacao))));
		String melhorGenotipo = "";
		for (int i : melhorCromossomo) {
			melhorGenotipo += i;
		}
		System.out.println("Melhor cromossomo:\n"
				+ "Genótipo: " + melhorGenotipo + "\n"
				+ "Fenótipo: " + mostraBotoes(melhorCromossomo));
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
				for(int j = 0; j < mutante.length; j++) {
					if(gerador.nextDouble() < forcaDeMutacao) {
						mutante[j] = (mutante[j] + 1) % 2;
					}
				}
				pop.set(i, mutante);
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
				int somaDeAptidoes = soma(aptidoes);
				int[] pai1 = pop.get(escolherRoleta(aptidoes, gerador.nextInt(somaDeAptidoes)));
				int[] pai2 = pop.get(escolherRoleta(aptidoes, gerador.nextInt(somaDeAptidoes)));
				int pontoDeCorte = 1 + gerador.nextInt(tamCromossomo - 2); //evitar pontos de corte nos extremos
				novaPop.add(cruzar(pai1, pai2, pontoDeCorte));
				novaPop.add(cruzar(pai2, pai1, pontoDeCorte));
				i += 2;
			}
		}
		return novaPop;
	}
	
	public static ArrayList<int[]> crossoverElitista(ArrayList<int[]> pop) {
		ArrayList<int[]> novaPop = new ArrayList<int[]>();
		//colocar os cromossomos em ordem decrescente de acordo com a função de aptidão
		pop.sort((o1, o2) -> aptidao(o2).compareTo(aptidao(o1)));
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
		if(somasDeFitness.size() < 10) {
			return false;
		} else {
			boolean resultado = true;
			int quantidadeDeGeracoes = somasDeFitness.size();
			int ultimo = somasDeFitness.get(quantidadeDeGeracoes - 1);
			for(int i = 1; i <= 10; i++) {
				if(Math.abs(1 - (double) ultimo / somasDeFitness.get(quantidadeDeGeracoes - i) ) > limiteDeEstabilizacao) {
					return false;
				}
			}
			return resultado;
		}
	}

}
