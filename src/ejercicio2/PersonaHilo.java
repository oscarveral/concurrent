package ejercicio2;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * Clase única del problema que representa a una persona que va a ir al cine a elegir película y ponerse en cola y comprar su entrada.
 * 
 * @author Óscar Vera López
 */
public class PersonaHilo extends Thread
{
	private static final int NUMERO_PERSONAS = 50;
	private static final List<PersonaHilo> PERSONAS = new LinkedList<PersonaHilo>();

	/* Array que almacena la cantidad de personas esperando en cada cola. */
	private static final int[] COLAS = {0,0,0};
	
	/* Semáforo que controla que sólo un hilo pueda modificar en un momento determinado el array de las colas. */ 
	private static final Semaphore MUTEX_COLAS = new Semaphore(1);
	/* Lista de semáforos de las ventanillas individuales. Son semáforos de exclusión mutua. Sólo una persona puede comprar en un cualquier determinado. */
	private static final List<Semaphore> MUTEXES_TAQUILLAS = Arrays.asList(new Semaphore(1), new Semaphore(1), new Semaphore(1));
	
	/* Campos específicos de cada hilo. */
	private int id;
	private int X;
	private int Y;
	private int ventanilla;
	
	/**
	 * Constructor de los hilos de cada persona.
	 * 
	 * @param id Identificador que se desea que tenga la persona.
	 */
	public PersonaHilo (int id)
	{
		this.id = id;
		/* Generamos los tiempos de decisión y compra respectivamente. */
		this.X = new Random().nextInt(1000, 2000);
		this.Y = new Random().nextInt(1000, 2000);
		/* La ventanilla se pone negativa para indicar que todavía no se ha elegido ninguna. */
		this.ventanilla = -1;
	}
	
	/**
	 * Instrucciones de ejecución de cada uno de los hilos.
	 */
	@Override
	public void run() 
	{
		try 
		{
			/* Esperamos el tiempo de despera de elegir una película. */
			Thread.sleep(this.X);
			
			/* Calculamos la cola con menos personas, imprimimos los datos y nos sumamos a la cola en exclusión mutua con el resto de hilos. */
			MUTEX_COLAS.acquire();
			this.ventanilla = this.getVentanillaConMenosCola();
			this.imprimirDatos();
			COLAS[this.ventanilla]++;
			MUTEX_COLAS.release();

			/* Tomamos el semáforo para nuestra taquilla y esperamos para comprar, después decrementamos la cola y devolvemos el semáforo. */
			MUTEXES_TAQUILLAS.get(this.ventanilla).acquire();
			Thread.sleep(this.Y);
			MUTEX_COLAS.acquire();
			COLAS[this.ventanilla]--;
			MUTEX_COLAS.release();
			MUTEXES_TAQUILLAS.get(this.ventanilla).release();
		} 
		catch (InterruptedException e) 
		{
			System.err.println("Alguien ha interrumpido el hilo de id " + this.id);
		}
		
	}
	
	/**
	 * Método que imprime los datos pedidos por el problema en pantalla.
	 */
	private void imprimirDatos()
	{
		System.out.println("Cliente de id " + this.id + " ser� atendido en la ventanilla " + this.ventanilla + 1);
		System.out.println("Tiempo en decidir la pel�cula = " + this.X + "ms");
		System.out.println("Tiempo en comprar la pel�cula = " + this.Y + "ms");
		System.out.println("Numero de personas esperando en ventanilla 1 = " + COLAS[0] + ", ventanilla 2 = " + COLAS[1] + ", ventanilla 3 = " + COLAS[2]);
	}
	
	/**
	 * Método que devuelve el número de ventanilla con menos cola.
	 * @return índice del array de colas correspondiente a la ventanilla deseada
	 */
	private int getVentanillaConMenosCola()
	{
		int index = 0;
		int min = COLAS[index];
		
		for (int i = 0; i < COLAS.length; i++)
		{
			if (COLAS[i] < min)
			{
				min = COLAS[i];
				index = i;
			}
		}
		
		return index;
	}
	
	/**
	 * Hilo principal del programa.
	 * 
	 * @param args Argumentos
	 */
	public static void main(String[] args) 
	{
		/* Creamos los hilos de 50 personas. */
		for (int i = 0; i < NUMERO_PERSONAS; i++)
		{
			PERSONAS.add(new PersonaHilo(i));
		}
		
		/* Comenzamos los 50 hilos. */
		for (PersonaHilo p : PERSONAS)
		{
			p.start();
		}
		
		/* Esperamos a que acaben todos. */
		for (PersonaHilo p : PERSONAS)
		{
			try 
			{
				p.join();
			} 
			catch (InterruptedException e) 
			{
				System.err.println("Thread interrupted");
			}
		}
	}
}
