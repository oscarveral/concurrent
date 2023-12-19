package ejercicio4;

import java.util.Random;
import java.util.Scanner;

/**
 * Hilo principal de ejecución.
 * 
 * @author Óscar Vera López.
 */
public class Programa 
{
	public static void main(String[] args) 
	{
		/* Leemos cual es nuestro cupo de hilos. */
		Scanner scan = new Scanner(System.in);
		System.out.print("Introduce el valor del cupo de hilos (menor de 40): ");
		int cupo = scan.nextInt();
		scan.close();
		System.out.println();
		
		/* Creamos el controlador. */
		Controlador controler = new Controlador(cupo);
		
		Random rng = new Random();
		/* Generamos cuantos lectores y escritores queremos. */
		int numeroLectores= rng.nextInt(1, Controlador.MAXIMO_HILOS);
		int numeroEscritores = Controlador.MAXIMO_HILOS - numeroLectores;
		/* Creamos los arrays. */
		LectorEscritor lectores[] = new LectorEscritor[numeroLectores];
		LectorEscritor escritores[] = new LectorEscritor[numeroEscritores];
		
		/* Rellenamos los arrays de los hilos. */
		for (int i = 0; i < numeroLectores; i++)
		{
			lectores[i] = new LectorEscritor(i, controler, TipoHilo.LECTOR);
		}
		for (int i = 0; i < numeroEscritores; i++)
		{
			escritores[i] = new LectorEscritor(i + numeroLectores, controler, TipoHilo.ESCRITOR);
		}
		
		/* Empezamos todos los hilos. */
		controler.start();
		for (LectorEscritor lector : lectores) lector.start();
		for (LectorEscritor escritor : escritores) escritor.start();
		
	}
}
