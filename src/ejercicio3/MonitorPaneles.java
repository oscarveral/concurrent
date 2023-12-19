package ejercicio3;

import java.util.LinkedList;
import java.util.List;

/**
 * Clase que implementa un monitor sobre paneles que se usarán por diferentes
 * hilos. Los paneles son propios del monitor. Dos monitores diferentes tendrán
 * paneles diferentes.
 * 
 * @author Óscar Vera López
 */
public class MonitorPaneles 
{
	private static final int NUMERO_PANELES = 4;
	private static final int SIZE_LIMIT = 3;

	private static final String DECORADOR = "Panel ";

	/* Lista de paneles que contiene el monitor. */
	private final List<Panel> paneles;
	/* Array de booleanos que contiene el estado de ocupación de un panel. */
	private final boolean ocupados[] = {false,false,false,false};

	/* Objetos que usaremos como cerrojo para el panel rápido y los paneles lentos. */
	private final Object cerrojoPanelRapido = new Object();
	private final Object cerrojoPanelesLentos = new Object();
	
	/**
	 * Constructor de objetos monitores.
	 */
	public MonitorPaneles() 
	{
		/* Inicializamos la lista de paneles del monitor. */
		this.paneles = new LinkedList<Panel>();

		/* Añadimos 4 paneles y ponemos su estado como desocupados. */
		for (int i = 0; i < NUMERO_PANELES; i++) 
		{
			Panel p = new Panel(DECORADOR + (i + 1), 0, 0);
			this.paneles.add(p);
		}
	}

	/**
	 * Método que reserva un panel para que un hilo pueda usarlo para imprimir.
	 * La sincronización en este método la hacemos mediante el uso de dos objetos cerrojo en bloques
	 * de código sincronizado para separar la asignación y uso de los paneles 1, 2 y 3 del 4 según pide el problema.
	 * De esta forma conseguimos que se ejecute todo de forma concurrente sin bloquear todo cuando un hilo que quiere reservar
	 * el panel 4 está esperando a uno que ha reservado el panel 3 por ejemplo.
	 * 
	 * @param size Tamaño de la matriz que se va a manejar para saber que panel hay que devolver.
	 * @return Devuelve el panel que se ha reservado para el hilo.
	 */
	public Panel pedirPanel(int size) 
	{
		/* Para matrices pequeñas pedimos el cuarto panel. */
		if (size <= SIZE_LIMIT) 
		{
			/* Sincronizamos usando el cerrojo del panel rápido. */
			synchronized (this.cerrojoPanelRapido) 
			{
				/* Mientras está ocupado el panel vamos a esperar a que se desocupe. */
				while (this.ocupados[3]) 
				{
					try 
					{
						/* Esperamos en el cerrojo. */
						this.cerrojoPanelRapido.wait();
					} 
					catch (InterruptedException e) 
					{
						System.err.println("Hilo interrumpido");
					}
				}
				/* Ponemos el panel como ocupado y lo devolvemos. */
				this.ocupados[3] = true;
				return this.paneles.get(3);
			}
		}
		/* Caso de matrices grandes. */
		else
		{
			/* Sincronizamos usando el cerrojo de los paneles lentos. */
			synchronized (this.cerrojoPanelesLentos) 
			{
				/* Mientras todos los paneles están ocupados nos mantenemos esperando. */
				while (this.ocupados[0] && this.ocupados[1] && this.ocupados[2])
				{
					try 
					{
						/* Esperamos en el cerrojo. */
						this.cerrojoPanelesLentos.wait();
					}
					catch (InterruptedException e) 
					{
						System.err.println("Hilo interrumpido");
					}
				}
				/* Buscamos que panel ha sido liberado. */
				int index = 0;
				while (this.ocupados[index])
				{
					index = (index + 1) % 3;
				}
				/* Establecemos el panel buscado como ocupado y lo devolvemos. */
				this.ocupados[index] = true;
				return this.paneles.get(index);
			}
		}
	}
	
	/**
	 * Método para liberar un panel reservado. Utiliza la misma forma de sincronización que la función de reserva del panel.
	 * 
	 * @param panelALiberar panel que se reservó y se desea liberar.
	 */
	public void liberarPanel (Panel panelALiberar)
	{
		/* Obtenemos el índice del panel en la lista de paneles. */
		int index = this.paneles.indexOf(panelALiberar);
		
		/* Según el índice obtenido tratamos que sea el panel de matrices pequeñas o grandes. */
		if (index == 3)
		{
			/* Sincronizamos usando el cerrojo del panel rápido. */
			synchronized (this.cerrojoPanelRapido) 
			{
				/* Establecemos que el panel ya no está ocupado. */
				this.ocupados[index] = false;
				/* Despertamos a al siguiente hilo para que pueda coger el panel. */
				this.cerrojoPanelRapido.notify();
			}
		}
		else
		{
			/* Sincronizamos usando el cerrojo de los paneles lentos. */
			synchronized (this.cerrojoPanelesLentos) 
			{
				/* Establecemos que el panel ya no está ocupado. */
				this.ocupados[index] = false;
				/* Despertamos a al siguiente hilo para que pueda tomar el panel. */
				this.cerrojoPanelesLentos.notify();
			}
		}
	}
}
