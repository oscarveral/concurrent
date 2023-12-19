package ejercicio1;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Clase principal del ejercicio. Declara las películas e hilos que imprimirán sus créditos.
 * 
 * @author �scar Vera L�pez
 */
public class Programa 
{
	private static final List<HiloCreditosPelicula> HILOS = new LinkedList<HiloCreditosPelicula>();
	private static final List<Pelicula> PELICULAS = new LinkedList<Pelicula>();
	
	public static void main(String[] args) 
	{
		String pelicula1 = "Blade Runner 2049";
		List<String> equipoTecnico1 = Arrays.asList("Roger Deakins", "Joe Walker", "Dennis Gassner");
		List<String> productores1 = Arrays.asList("Yale Badik", "Dana Belcastro", "Bill Carraro");
		List<String> asistentesProduccion1 = Arrays.asList("Bud Yorkin", "Steven P. Wegner", "Donald Sparks");
		List<String> maquilladores1 = Arrays.asList("Toni Bisset", "Szandra Bíró", "Hanna Erkel");
		List<String> bandaSonora1 = Arrays.asList("2049 (Hans Zimmer)", "Flight to LAPD (Hans Zimmer)", "Sapper's tree (Hans Zimmer)");
	
		String pelicula2 = "La lista de Schindler";
		List<String> equipoTecnico2 = Arrays.asList("Janusz Kaminski", "Michael Kahn", "Allan Starski");
		List<String> productores2 = Arrays.asList("Steven Spielberg", "Kathleen Kennedy", "Branko Lustig");
		List<String> asistentesProduccion2 = Arrays.asList("Irving Glovin", "Lew Rywin", "Robert Raymond");
		List<String> maquilladores2 = Arrays.asList("Czeslawa Baldo", "Judith A. Cory", "Pauline Heys");
		List<String> bandaSonora2 = Arrays.asList("Schindler's List: Theme From Schindler's List (Itzhak Perlman)", "Schindler's Workforce (Boston Symphony Orchestra)", "Stolen Memories (Boston Symphony Orchestra)");
	
		String pelicula3 = "La vida es bella";
		List<String> equipoTecnico3 = Arrays.asList("Tonino Delli Colli", "Simona Paggi", "Danilo Donati");
		List<String> productores3 = Arrays.asList("Gialuigi Braschi", "Mario Cotone", "John M. Davis");
		List<String> asistentesProduccion3 = Arrays.asList("Elda Ferri", "Agnès Mentre", "John Rogers");
		List<String> maquilladores3 = Arrays.asList("Giusy Bovino", "Martina Cossu", "Walter Cossu");
		List<String> bandaSonora3 = Arrays.asList("La notte di fuga (Nicola Piovani)", "Buongiorno principessa (Nicola Piovani)", "La vita � bella (Nicola Piovani)");
	
		String pelicula4 = "Matrix";
		List<String> equipoTecnico4 = Arrays.asList("Bill Pope", "Zach Staenberg", "Owen Paterson");
		List<String> productores4 = Arrays.asList("Bruce Berman", "Dan Cracchiolo", "Carol Hughes");
		List<String> asistentesProduccion4 = Arrays.asList("Lilly Wachowski", "Lana Wachowski", "Erwin Stoff");
		List<String> maquilladores4 = Arrays.asList("Rick Connelly", "Kathy Courtney", "Nikki Gooley");
		List<String> bandaSonora4 = Arrays.asList("Rock is dead (Marilyn Manson)", "Bad Blood (Ministry)", "Mindfields (The Prodigy)");
	
		String pelicula5 = "La terminal";
		List<String> equipoTecnico5 = Arrays.asList("Janusz Kaminski", "Alex McDowell", "Anne Kuljian");
		List<String> productores5 = Arrays.asList("Steven Spielberg", "Laurie MacDonald", "Andrew Niccol");
		List<String> asistentesProduccion5 = Arrays.asList("Jason Hoffs", "Walter F. Parkes", "Patricia Whitcher");
		List<String> maquilladores5 = Arrays.asList("Mary Cooke", "Katalin Elek", "Zoltan Elek");
		List<String> bandaSonora5 = Arrays.asList("The Tale of Viktor Navorski (Hollywood Studio Symphon)", "A legend is born (Hollywood Studio Symphon)", "Dinner with Ammelia (Hollywood Studio Symphon)");
	
		PELICULAS.add(new Pelicula(pelicula1, equipoTecnico1, productores1, asistentesProduccion1, maquilladores1, bandaSonora1));
		PELICULAS.add(new Pelicula(pelicula2, equipoTecnico2, productores2, asistentesProduccion2, maquilladores2, bandaSonora2));
		PELICULAS.add(new Pelicula(pelicula3, equipoTecnico3, productores3, asistentesProduccion3, maquilladores3, bandaSonora3));
		PELICULAS.add(new Pelicula(pelicula4, equipoTecnico4, productores4, asistentesProduccion4, maquilladores4, bandaSonora4));
		PELICULAS.add(new Pelicula(pelicula5, equipoTecnico5, productores5, asistentesProduccion5, maquilladores5, bandaSonora5));
		
		for (Pelicula pelicula : PELICULAS)
		{
			HILOS.add(new HiloCreditosPelicula(pelicula));
		}
		
		/* Comenzamos todos los hilos. */
		for (HiloCreditosPelicula hilo : HILOS)
		{
			hilo.start();
		}
		
		/* Esperamos a que todos acaben. */
		for (HiloCreditosPelicula hilo : HILOS)
		{
			try 
			{
				hilo.join();
			} 
			catch (InterruptedException e) 
			{
				System.err.println("Se interrumpió la ejecución de uno de los hilos.");
			}
		}
		
		System.out.println("FIN DE LOS CRÉDITOS DE LAS PELÍCULAS");
	}
}
