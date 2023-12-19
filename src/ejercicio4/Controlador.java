package ejercicio4;

import messagepassing.MailBox;
import messagepassing.Selector;

/**
 * Clase que representa al controlador del recurso que van a utilizar los diferentes hilos lectores y escritores. 
 * 
 * @author Óscar Vera López
 */
public class Controlador extends Thread
{
	/* Constante con la máxima cantidad de hilos que hacen uso del controlador. Especificada en el enunciado. */
	public static final int MAXIMO_HILOS = 40;
	
	/* Almacena a que tipo de hilo le pertenece el uso del recurso compartido. */
	private TipoHilo turno; 
	
	/* Contadores de lectores actuales y totales y escritores totales. */
	private int numeroLectores;
	private int contadorHilosLectura;
	private int contadorHilosEscritura;
	
	/* Booleano que determina el estado de escritura. */
	private boolean enEscritura;
	
	/* Buzones que usarán los hilos lectores y escritores para comunicarse con el controlador. */
	private MailBox peticionLectura;
	private MailBox peticionEscritura;
	private MailBox finalizacionLectura;
	private MailBox finalizacionEscritura;
	
	/* Array de buzones que usarán los hilos para recibir las respuestas a sus peticiones. */
	private MailBox buzonesRespuesta[];
	
	/* Variable que almacena cada cuantos hilos servidos se cambia de turno. */
	private int c;
	
	/* Selecctor que usaremos para simular la sentencia select. */
	private Selector selector;
	
	/**
	 * Constructor del controlador. Establece todas las variables iniciales.
	 * 
	 * @param c Entero que representa cada cuantas peticiones servidas se cambia de turno.
	 */
	public Controlador(int c) 
	{
		this.c = c;
		this.numeroLectores = 0;
		this.contadorHilosLectura = 0;
		this.contadorHilosEscritura = 0;
		this.enEscritura = false;
		/* Al principio el turno es de cualquiera, el primer hilo que llege lo asigna a su tipo. */
		this.turno = TipoHilo.CUALQUIERA;
		/* Creamos todos los buzones. */
		this.peticionLectura = new MailBox();
		this.peticionEscritura = new MailBox();
		this.finalizacionLectura = new MailBox();
		this.finalizacionEscritura = new MailBox();
		/* Creamos y rellenamos el array de buzones de respuesta. */
		this.buzonesRespuesta = new MailBox[MAXIMO_HILOS];
		for (int i = 0; i < MAXIMO_HILOS; i++)
		{
			this.buzonesRespuesta[i] = new MailBox();
		}
		/* Creamos el selector y  añadimos los diferentes buzones que actuarán en él. */
		this.selector = new Selector();
		this.selector.addSelectable(this.peticionLectura, false); // 1
		this.selector.addSelectable(this.finalizacionLectura, false); // 2
		this.selector.addSelectable(this.peticionEscritura, false); // 3
		this.selector.addSelectable(this.finalizacionEscritura, false); // 4
	}
	
	/**
	 * Instrucciones de ejecución del hilo del controlador. Evalua todo el tiempo las peticiones encontradas y actua en consecuencia.
	 */
	@Override
	public void run() 
	{
		/* Bucle de ejecución del controlador. */
		while (true)
		{
			/* Establecemos las guardas de los buzones. */
			
			/* Para la petición de lectura. Solo si es nuestro turno y hemos contado menos peticiones de lectura que lo permitido podemos procesarla. */
			this.peticionLectura.setGuardValue((this.turno == TipoHilo.LECTOR || this.turno == TipoHilo.CUALQUIERA) && this.contadorHilosLectura < this.c);
			/* Podemos recibir una finalización de lectura en cualquier momento. */
			this.finalizacionLectura.setGuardValue(true);
			/* Para la petición de escritura. Solo podemos escribir si no hay nadie escribiendo, es nuestro turno y la cantidad de hilos que han escrito no llega a nuestro límite. */
			this.peticionEscritura.setGuardValue(this.enEscritura == false && (this.turno == TipoHilo.ESCRITOR || this.turno == TipoHilo.CUALQUIERA) && this.contadorHilosEscritura < this.c);
			/* Siempre podemos finalizar la escritura. */
			this.finalizacionEscritura.setGuardValue(true);
			
			/* Hacemos el select y actuamos en consecuencia. */
			switch (this.selector.selectOrBlock())			
			{
				/* Recibimos petición de lectura. */
				case 1:
					/* Recogemos el id del hilo que ha solicitado la lectura. */
					int idHiloLec = (Integer) this.peticionLectura.receive();
					/* Damos el turno a los lectores. */
					this.turno = TipoHilo.LECTOR;
					/* Incremento de contadores. */
					this.numeroLectores++;
					this.contadorHilosLectura++;
					/* Mandamos la respuesta por el buzón correspondiente.*/
					this.buzonesRespuesta[idHiloLec].send(this.buildConfirmationMessage());
					break;
				/* Recibimos finalización de lectura. */
				case 2:
					/* Sacamos el mensaje del buzón. */
					this.finalizacionLectura.receive();
					/* Decrementamos los lectores y si ya no hay lectores y se llego al tope de hilos pasamos el turno. */
					this.numeroLectores--;
					if (this.numeroLectores <= 0 && this.contadorHilosLectura >= this.c)
					{
						this.turno = TipoHilo.ESCRITOR;
						this.contadorHilosLectura = 0;
					}
					break;
				/* Recibimos petición de escritura. */
				case 3:
					/* Recogemos el id del hilo que ha solicitado la escritura. */
					int idHiloEsc = (Integer) this.peticionEscritura.receive();
					/* Damos el turno a los escritores. */
					this.turno = TipoHilo.ESCRITOR;
					/* Incremento de contador y establecimiento de la variable de escritura. */
					this.contadorHilosEscritura++;
					this.enEscritura = true;
					/* Mandamos la respuesta por el buzón correspondiente.*/
					this.buzonesRespuesta[idHiloEsc].send(this.buildConfirmationMessage());
					break;
				/* Recibimos finalización de escritura. */	
				case 4:
					this.finalizacionEscritura.receive();
					/* Establecemos que no se está escribiendo y si ya hemos llegado al cupo de hilos pasamos el turno. */
					this.enEscritura = false;
					if (this.contadorHilosEscritura >= this.c)
					{
						this.turno = TipoHilo.LECTOR;
						this.contadorHilosEscritura = 0;
					}
					break;
			}
		}
	}
	
	/**
	 * Función que construye el mensaje de confirmación que se envia como respuesta a las peticiones.
	 * @return Cadena de texto con el mensaje.
	 */
	private String buildConfirmationMessage ()
	{
		StringBuffer sb = new StringBuffer();
		/* Añadimos los lectores actuales y escritores separados por espacio. */
		sb.append(this.numeroLectores + " ");
		if (this.enEscritura) sb.append(1);
		else sb.append(0);
		/* Devolvemos la cadena. */
		return sb.toString();
	}	
	
	/**
	 * Método que usarán los lectores para obtener el buzon al que tienen que hacer las peticiones.
	 * 
	 * @return Buzón al que mandar las peticiones de lectura.
	 */
	public MailBox getBuzonPeticionLectura()
	{
		return this.peticionLectura;
	}
	
	/**
	 * Método que usarán los lectores para obtener el buzon donde hacer las finalizaciones.
	 * 
	 * @return Buzón al que mandar las finalizaciones de lectura.
	 */
	public MailBox getBuzonFinalizacionLectura()
	{
		return this.finalizacionLectura;
	}
	
	/**
	 * Método que usarán los escritores para obtener el buzon al que tienen que hacer las peticiones.
	 * 
	 * @return Buzón al que mandar las peticiones de escritura.
	 */
	public MailBox getBuzonPeticionEscritura()
	{
		return this.peticionEscritura;
	}
	
	/**
	 * Método que usarán los escritores para obtener el buzon donde hacer las finalizaciones.
	 * 
	 * @return Buzón al que mandar las finalizaciones de lectura.
	 */
	public MailBox getBuzonFinalizacionEscritura()
	{
		return this.finalizacionEscritura;
	}
	
	/**
	 * Método para que los hilos obtengan el buzon al que les va a responder el controlador.
	 * 
	 * @param id Id del hilo. Se usa para determinar el buzon que se devolverá.
	 * @return Buzón donde el controlador mandará respuestas de peticiones.
	 */
	public MailBox getBuzonRespuesta (int id)
	{
		return this.buzonesRespuesta[id];
	}
}
