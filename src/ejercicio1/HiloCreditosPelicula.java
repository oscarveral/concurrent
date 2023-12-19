package ejercicio1;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Clase que representa un hilo de ejecución encargado de la impresión de los créditos de una película por pantalla.
 * La idea de esta clase es que sólo un hilo pueda imprimir por pantalla sus créditos en un momento determinado.
 * Hasta que un hilo no acabe de imprimir sus créditos no podrá venir el siguiente a imprimir los suyos.
 * 
 * @author Óscar Vera López
 */
public class HiloCreditosPelicula extends Thread
{
	private final static String TITULO_CREDITOS = "Créditos de la pel�cula ";
	private final static String EQUIPO_TECNICO = "Equipo técnico ";
	private final static String PRODUCTORES = "Productores ";
	private final static String ASISTENTES_PRODUCCION = "Asistentes de producción ";
	private final static String MAQUILLADORES = "Maquilladores ";
	private final static String BANDA_SONORA = "Banda sonora ";
	private final static char DOBLE_PUNTO = ':';
	private final static char TABULACION = '\t';
	private final static long SLEEP_TIME = 1000;
	
	/* Cerrojo global que asegura que sólo un hilo imprime por pantalla su pel�cula en un determinado momento. */
	private final static ReentrantLock CERROJO_DE_IMPRESION = new ReentrantLock();
	
	private Pelicula pelicula;
	
	/**
	 * Método que se encarga de imprimir los cr�ditos de la película por pantalla esperando entre la impresión de cada línea.
	 */
	private void imprimeCreditos ()
	{
		try {
			System.out.println(TITULO_CREDITOS + this.pelicula.getNombre());
			Thread.sleep(SLEEP_TIME);
			
			System.out.println(EQUIPO_TECNICO + this.pelicula.getNombre() + DOBLE_PUNTO);
			Thread.sleep(SLEEP_TIME);
			for (String s : this.pelicula.getEquipoTecnico())
			{
				System.out.println(TABULACION + s);
				Thread.sleep(SLEEP_TIME);
			}
			
			System.out.println(PRODUCTORES + this.pelicula.getNombre() + DOBLE_PUNTO);
			Thread.sleep(SLEEP_TIME);
			for (String s : this.pelicula.getProductores())
			{
				System.out.println(TABULACION + s);
				Thread.sleep(SLEEP_TIME);
			}
			
			System.out.println(ASISTENTES_PRODUCCION + this.pelicula.getNombre() + DOBLE_PUNTO);
			Thread.sleep(SLEEP_TIME);
			for (String s : this.pelicula.getAsistentesProduccion())
			{
				System.out.println(TABULACION + s);
				Thread.sleep(SLEEP_TIME);
			}
			
			System.out.println(MAQUILLADORES + this.pelicula.getNombre() + DOBLE_PUNTO);
			Thread.sleep(SLEEP_TIME);
			for (String s : this.pelicula.getMaquilladores())
			{
				System.out.println(TABULACION + s);
				Thread.sleep(SLEEP_TIME);
			}
			
			System.out.println(BANDA_SONORA+ this.pelicula.getNombre() + DOBLE_PUNTO);
			Thread.sleep(SLEEP_TIME);
			for (String s : this.pelicula.getBandaSonora())
			{
				System.out.println(TABULACION + s);
				Thread.sleep(SLEEP_TIME);
			}
			
			System.out.println();
		} 
		catch (InterruptedException e) 
		{
			System.err.println("El hilo de la pelicula " + this.pelicula.getNombre() + " ha sido interrumpido.");
		}
		
	}
	
	/**
	 * Constructor de los hilos de impresión de los créditos.
	 * 
	 * @param pelicula Objeto de la clase pel�cula del que deseamos imprimir sus créditos.
	 */
	public HiloCreditosPelicula (Pelicula pelicula)
	{
		super();
		this.pelicula = pelicula;
	}
	
	/**
	 * Método con la rutina principal de hilo de impresión. 
	 * Contiene las las interacciones con el cerrojo que aseguran que s�lo un hilo ejecuta la impresión al mismo tiempo.
	 */
	@Override
	public void run () 
	{
		/* Esperamos a disponer del cerrojo para imprimir los créditos. */
		CERROJO_DE_IMPRESION.lock();
		
		/* Imprimimos los créditos. */
		this.imprimeCreditos();
		
		/* Libreamos el cerrojo para dejar que otro hilo imprima sus créditos. */
		CERROJO_DE_IMPRESION.unlock();
	}
}