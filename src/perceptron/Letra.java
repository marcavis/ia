package perceptron;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Letra {
	private char nome;
	private int numero;
	private char[] caracteres;
	private int[] amostras;
	public static double[] pesos;
	//como é representado um resultado no perceptron; por ex., {1,0,0,0,0,0,0} é a letra A
	private int[] saida;
	
	public Letra(char nome, int numero, int[] saida) {
		setNome(nome);
		setNumero(numero);
		setSaida(saida);
		try {
			FileReader fr = new FileReader(new File("Letras/" + nome + "-" + numero + ".txt"));
			BufferedReader br = new BufferedReader(fr);
			String conteudo = "";
			String linha;
			while((linha=br.readLine())!=null) {
				conteudo += linha;
			}
			caracteres = conteudo.toCharArray();
			br.close();
			fr.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		amostras = new int[caracteres.length];
		for (int i = 0; i < caracteres.length; i++) {
			if(caracteres[i] == '#') {
				amostras[i] = 1;
			} else if (caracteres[i] == '-') {
				amostras[i] = -1;
			} else {
				amostras[i] = 0;
			}
			//System.out.println(caracteres[i]+"-"+amostras[i]);
		}
	}
	
	public static void inicializarPesos() {
		pesos = new double[Main.tamanhoLetra];
		for (int i = 0; i < Main.tamanhoLetra; i++) {
			pesos[i] = 0.0;
		}
	}
	
	public char getNome() {
		return nome;
	}
	
	public void setNome(char nome) {
		this.nome = nome;
	}
	
	public char[] getCaracteres() {
		return caracteres;
	}
	
	public void setCaracteres(char[] caracteres) {
		this.caracteres = caracteres;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public int[] getSaida() {
		return saida;
	}

	public void setSaida(int[] saida) {
		this.saida = saida;
	}

	public int[] getAmostras() {
		return amostras;
	}

	public void setAmostras(int[] amostras) {
		this.amostras = amostras;
	}
}
