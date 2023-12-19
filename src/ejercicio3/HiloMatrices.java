package ejercicio3;

import java.util.Random;

/**
 * Clase que representa un hilo de ejecución de cálculo de matrices.
 * 
 * @author Óscar Vera López
 */
public class HiloMatrices extends Thread
{
	private static final int UPPER_SIZE_LIMIT = 11;
	private static final int LOWER_SIZE_LIMIT = 2;
	private static final int MAX_NUMBER = 50;
	private static final int NUMERO_HILOS = 20;
	private static final String PREVIO = "Hilo con id ";
	private static final String POSTERIOR = "Fin hilo con id ";
	private static final String SALTO_LINEA = "\n";
	private static final String DECORADOR_MATRIZ = "|";
	private static final String SIGNO_SUMA = "+";
	private static final String SIGNO_IGUAL = "=";
	private static final String ESPACIO = " ";
	
	private int id;
	private int size;
	private int matrizA[][];
	private int matrizB[][];
	private int matrizC[][];
	
	/* Monitor que permitirá a los hilos usar paneles para imprimir. */
	private MonitorPaneles monitorPaneles;
	
	/**
	 * Constructor de los hilos. Asigna la ID del hilo y el monitor que usará el hilo para poder obtener paneles sobre los que escribir.
	 */
	public HiloMatrices(int id, MonitorPaneles monitorPaneles) 
	{
		super();
		this.id = id;
		this.monitorPaneles = monitorPaneles;
	}
	
	/**
	 * Método usado para generar las matrices que usar� el hilo.
	 */
	private void generarMatrices ()
	{
		/* Creamos un generador de número aleatorios. */
		Random rng = new Random(System.currentTimeMillis());
		
		/* Generamos el tama�o de la matriz. */
		this.size = rng.nextInt(LOWER_SIZE_LIMIT,UPPER_SIZE_LIMIT);
		/* Inicializamos las matrices. */
		this.matrizA = new int[this.size][this.size];
		this.matrizB = new int[this.size][this.size];
		this.matrizC = new int[this.size][this.size];
		/* Rellenamos las matrices. */
		for (int i = 0; i < this.size; i++)
		{
			for (int j = 0; j < this.size; j++)
			{
				this.matrizA[i][j] = rng.nextInt(MAX_NUMBER);
				this.matrizB[i][j] = rng.nextInt(MAX_NUMBER);
				this.matrizC[i][j] = this.matrizA[i][j] + this.matrizB[i][j];
			}
		}
	}
	
	/**
	 * Método usado por los hilos para convertir la operación realizada en una cadena de texto.
	 * 
	 * @return Devuelve la cadena de texto que se imprimir� en la pantalla.
	 */
	private String operacionToString()
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append(PREVIO + this.id + SALTO_LINEA);
		
		for (int i = 0; i < this.size; i++)
		{
			sb.append(DECORADOR_MATRIZ + ESPACIO);
			for (int j = 0; j < this.size; j++)
			{
				if (this.matrizA[i][j] >= 10)
					sb.append(this.matrizA[i][j] + ESPACIO);
				else
					sb.append(this.matrizA[i][j] + ESPACIO + ESPACIO);
			}
			sb.append(DECORADOR_MATRIZ + ESPACIO);
		
			if (i == this.size/2) sb.append(SIGNO_SUMA + ESPACIO);
			else sb.append(ESPACIO + ESPACIO);
			
			sb.append(DECORADOR_MATRIZ + ESPACIO);
			for (int j = 0; j < this.size; j++)
			{
				if (this.matrizB[i][j] >= 10)
					sb.append(this.matrizB[i][j] + ESPACIO);
				else
					sb.append(this.matrizB[i][j] + ESPACIO + ESPACIO);
			}
			sb.append(DECORADOR_MATRIZ + ESPACIO);
			
			if (i == this.size/2) sb.append(SIGNO_IGUAL + ESPACIO);
			else sb.append(ESPACIO + ESPACIO);
			
			sb.append(DECORADOR_MATRIZ + ESPACIO);
			for (int j = 0; j < this.size; j++)
			{
				if (this.matrizC[i][j] >= 10)
					sb.append(this.matrizC[i][j] + ESPACIO);
				else
					sb.append(this.matrizC[i][j] + ESPACIO + ESPACIO);
			}
			sb.append(DECORADOR_MATRIZ + SALTO_LINEA);
		}
		
		sb.append(POSTERIOR + this.id + SALTO_LINEA);
	
		return sb.toString(); 
	}
	
	/**
	 * Método principal de los hilos.
	 */
	@Override
	public void run() 
	{
		/* Hacemos 10 veces la impresión de matrices. */
		for (int i = 0; i < 10; i++)
		{
			this.generarMatrices();
			String resultado = this.operacionToString();
			
			/* Pedimos un panel. */
			Panel panelPedido = this.monitorPaneles.pedirPanel(this.size);
			/* Imprimimos. */
			panelPedido.escribir_mensaje(resultado);
			/* Devolvemos el panel. */
			this.monitorPaneles.liberarPanel(panelPedido);
		}
	}
	
	/**
	 * Método principal de ejecución del programa.
	 * 
	 * @param args Argumentos de ejecución.
	 */
	public static void main(String[] args) 
	{
		/* Creamos el monitor que usarán todos los hilos. */
		MonitorPaneles monitor = new MonitorPaneles();
		/* Creamos todos los hilos. */
		HiloMatrices hilos[] = new HiloMatrices[NUMERO_HILOS];
		for (int i = 0; i < NUMERO_HILOS; i++)
		{
			hilos[i] = new HiloMatrices(i, monitor);
		}
		
		/* Ejecutamos todos los hilos. */
		for (int i = 0; i < NUMERO_HILOS; i++)
		{
			hilos[i].start();
		}
		
		/* Esperamos a todos los hilos. */
		for (int i = 0; i < NUMERO_HILOS; i++)
		{
			try 
			{
				hilos[i].join();
			} 
			catch (InterruptedException e) 
			{
				System.err.println("Hilo interrumpido.");
			}
		}
	}
}
