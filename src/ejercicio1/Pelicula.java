package ejercicio1;

import java.util.LinkedList;
import java.util.List;

/**
 * Clase Pelicula. Representa una pel�cula cuya información se puede obtener en cadenas de texto.
 * 
 * @author Óscar Vera López
 */
public class Pelicula
{
	private String nombre;
	private List<String> equipoTecnico;
	private List<String> productores;
	private List<String> asistentesProduccion;
	private List<String> maquilladores;
	private List<String> bandaSonora;
	
	/**
	 * Constructor de la clase película.
	 * 
	 * @param nombre Nombre de la pelicula.
	 * @param equipoTecnico Listado de componentes del equipo técnico.
	 * @param productores Listado de productores.
	 * @param asistentesProduccion Listado de los asistentes de producción.
	 * @param maquilladores Listado de los maquilladores.
	 * @param bandaSonora Listado de piezas que compone la banda sonora.
	 * 
	 * @attention Aunque el ejercicio pide que las listas tengan al menos 3 componentes no lo compruebo 
	 * 			  ya que no cambia nada el funcionamiento del programa la cantidad de elementos en las listas.
	 */
	public Pelicula
	(
			String nombre, 
			List<String> equipoTecnico, 
			List<String> productores,
			List<String> asistentesProduccion,
			List<String> maquilladores,
			List<String> bandaSonora
	)
	{
		this.nombre = nombre;
		this.equipoTecnico = new LinkedList<String>(equipoTecnico);
		this.productores = new LinkedList<String>(productores);
		this.asistentesProduccion = new LinkedList<String>(asistentesProduccion);
		this.maquilladores = new LinkedList<String>(maquilladores);
		this.bandaSonora = new LinkedList<String>(bandaSonora);
	}
	
	/**
	 * Método para obtener el nombre de la película.
	 * 
	 * @return Devuelve la cadena de texto con el nombre de la película.
	 */
	public String getNombre ()
	{
		return this.nombre;
	}
	
	/**
	 * Método para obtener el listado de componentes del equipo técnico.
	 * 
	 * @return Devuelve una copia de la lista del equipo técnico.
	 */
	public List<String> getEquipoTecnico ()
	{
		return new LinkedList<String>(this.equipoTecnico);
	}
	
	/**
	 * Método para obtener el listado de productores.
	 * 
	 * @return Devuelve una copia de la lista de productores.
	 */
	public List<String> getProductores ()
	{
		return new LinkedList<String>(this.productores);
	}
	
	/**
	 * Método para obtener el listado de asistentes de producción.
	 * 
	 * @return Devuelve una copia de la lista de asistentes de producción.
	 */
	public List<String> getAsistentesProduccion ()
	{
		return new LinkedList<String>(this.asistentesProduccion);
	}
	
	/**
	 * Método para obtener el listado de maquilladores.
	 * 
	 * @return Devuelve una copia de la lista de maquilladores.
	 */
	public List<String> getMaquilladores ()
	{
		return new LinkedList<String>(this.maquilladores);
	}
	
	/**
	 * Método para obtener el listado de piezas de la banda sonora.
	 * 
	 * @return Devuelve una copia de la lista de piezas de la banda sonora.
	 */
	public List<String> getBandaSonora ()
	{
		return new LinkedList<String>(this.bandaSonora);
	}
}
