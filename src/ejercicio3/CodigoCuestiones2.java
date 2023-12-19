package ejercicio3;

public class CodigoCuestiones2 
{
	public static class HiloHolaMundo extends Thread
	{
		private static String INFO = "Hola amigos del mundo";
		private static MonitorPantalla MONITOR_PANTALLA = new MonitorPantalla();
		
		public HiloHolaMundo() {}
		
		@Override
		public void run() 
		{
			MONITOR_PANTALLA.imprimirInformacion(INFO + " " + this.getName());
		}
	}
	
	public static class MonitorPantalla 
	{	
		public MonitorPantalla() {}
		
		public synchronized void imprimirInformacion(String info)
		{	
			/* Usando una variable booleana aquí dentro tendr�amos un wait condicionado por dicha variable y despues de imprimir un notify. */
			System.out.println(info);
		}
	}
	
	public static void main(String[] args) 
	{
		HiloHolaMundo hilos[] = new HiloHolaMundo[10];
		for (int i = 0; i < 10; i++)
		{
			hilos[i] = new HiloHolaMundo();
		}
		for (int i = 0; i < 10; i++)
		{
			hilos[i].run();
		}
	}
}
