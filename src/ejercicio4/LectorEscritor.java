package ejercicio4;

import messagepassing.MailBox;

/**
 * Clase que representa a lectores y escritores. Un parámetro el su creación determinará si es lector o escritor. 
 * 
 * @author Óscar Vera López
 */
public class LectorEscritor extends Thread 
{
	private static final int TIEMPO_ESPERA = 1000;
	/* Buzón con el que se garantizará la esclusión mutua en el uso de la pantalla. Global a todos los hilos. Lo dejamos listo para que el primer hilo que llegue reciba el primer mensaje. */
	private static final MailBox BUZON_IMPRESION = new MailBox();
	static 
	{
		/* Mandamos un mensaje testigo por el buzón. */
		String testigo = "testigo";
		BUZON_IMPRESION.send(testigo);
	}
	
	/* Controlador con el que se comunicará cada hilo. */
	private Controlador controlador;
	/* Buzón que usará para recibir respuestas. */
	private MailBox buzonHilo;
	/* Buzones donde se realizarán las peticiones y finalizaciones. */
	private MailBox buzonPeticion;
	private MailBox buzonFinalizacion;
	/* Id que identificará al hilo. */
	private int id;
	/* Tipo de hilo que será el objeto de esta clase. */
	private TipoHilo tipo;
	
	/**
	 * Constructor de lectores y escritores.
	 * 
	 * @param id Entero con el que se identificará a este hilo.
	 * @param controlador Controlador al que este hilo solicitará el recurso compartido.
	 * @param tipo Tipo de hilo que estamos contruyendo (Lector o Escritor)
	 */
	public LectorEscritor(int id, Controlador controlador, TipoHilo tipo) throws IllegalArgumentException
	{
		if (tipo == TipoHilo.CUALQUIERA) throw new IllegalArgumentException("No se permiten hilos de tipo CUALQUIERA");
		
		/* Establecemos todas las propiedades. */
		this.id = id;
		this.controlador = controlador;
		this.tipo = tipo;
		/* Obtenemos todos los buzones que usaremos (segun el tipo de hilo). */
		this.buzonHilo = controlador.getBuzonRespuesta(this.id);
		if (this.tipo == TipoHilo.LECTOR)
		{
			this.buzonPeticion = this.controlador.getBuzonPeticionLectura();
			this.buzonFinalizacion = this.controlador.getBuzonFinalizacionLectura();
		}
		else if (this.tipo == TipoHilo.ESCRITOR)
		{
			this.buzonPeticion = this.controlador.getBuzonPeticionEscritura();
			this.buzonFinalizacion = this.controlador.getBuzonFinalizacionEscritura();
		}
	}
	
	/**
	 * Instrucciones de ejecución del hilo. Tanto lectores como escritores hacen lo mismo.
	 */
	@Override
	public void run()
	{
		/* Bucle infinito de ejecución. */
		while (true) 
		{
			/* Enviamos la petición y recibimos la confirmación (contiene nº de lectores y nº de escritores). */
			this.buzonPeticion.send(this.id);
			String informacionConfirmacion = (String) this.buzonHilo.receive();
			
			/* Recibimos el testigo, imprimimos y mandamos el testigo de vuelta al buzón de impresión. */
			String testigo = (String)BUZON_IMPRESION.receive();
			this.imprimirInfo(informacionConfirmacion);
			BUZON_IMPRESION.send(testigo);
			
			/* Dormimos el hilo. */
			try 
			{
				Thread.sleep(TIEMPO_ESPERA);
			} 
			catch (InterruptedException e) 
			{
				System.err.println("El hilo " + this.id + " fue interrumpido.");
			}
			
			/* Devolvemos el recurso. */
			String finalizacion = "finalizar";
			this.buzonFinalizacion.send(finalizacion);	
		}
	}
	
	/**
	 * Método que pone en pantalla la información pedida en el ejercicio.
	 *
	 * @param info Cadena de texto con la información de cuantos lectores y escritores hay en el recurso.
	 */
	private void imprimirInfo (String info)
	{
		if (this.tipo == TipoHilo.LECTOR)
			System.out.println("Lector ID: " + this.id);
		else if (this.tipo == TipoHilo.ESCRITOR)
			System.out.println("Escritor ID: " + this.id);
		
		/* Parseamos la cadena de texto para sacar los datos. */
		String numeros[] = info.split(" ");
		System.out.println("Número de lectores en el recurso: " + numeros[0]);
		System.out.println("Número de escritores en el recurso: " + numeros[1]);
		
		if (this.tipo == TipoHilo.LECTOR)
			System.out.println("Lector ID: " + this.id);
		else if (this.tipo == TipoHilo.ESCRITOR)
			System.out.println("Escritor ID: " + this.id);
		
		System.out.println();
	}
}
