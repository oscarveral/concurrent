package ejercicio3;

public class CodigoCuestiones1 {
	public static class Monitor1 
	{
		private volatile int compar;
		public Monitor1(int val){
			compar=val;
		}
		synchronized public void decrementar(int cantidad){
			while(cantidad>compar){
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			compar-=cantidad;
			System.out.println("variable="+compar);
		}
		synchronized public void incrementar(int cantidad){
			compar+=cantidad;
			notifyAll();
			System.out.println("variable="+compar);
		}
	}
	public static class Hilo extends Thread{
		private Monitor1 monitor;
		private int tipo;
		private int cantidad;
		public Hilo(Monitor1 _monitor,int _tipo,int _cantidad){
			monitor=_monitor;
			tipo=_tipo;
			cantidad=_cantidad;
		}
		public void run(){
			for(int i=0 ; i<100 ; i++)
			{
				if (tipo==2) monitor.incrementar(cantidad);
				else monitor.decrementar(cantidad);
			}
		}
	}
	public class Ejemplo {
		public static void main(String[] args){
			Monitor1 monitor= new Monitor1(0);
			Thread a=new Hilo(monitor,2,10); // tipo 2 incrementa
			Thread b=new Hilo(monitor,1,5); // tipo 1 decrementa
			Thread c=new Hilo(monitor,1,3);
			Thread d=new Hilo(monitor,1,2);
			/* Por alguna razon mi JVM ejecuta primero todas las sumas y luego hace las restas lo que impide un bloqueo unsado notify(). */
			a.start();
			b.start();
			c.start();
			d.start();
		}
	}
	
	public static void main(String[] args) {
		Ejemplo.main(args);
	}

}