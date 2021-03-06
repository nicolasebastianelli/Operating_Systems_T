package esamejava20120628;

import java.util.Random;

public class Visitatori extends Thread{
	private Ponte p;
	private Random r1;
	private Random r2;
	private int id;

	public Visitatori(Ponte p, int id) {
		super();
		this.p = p;
		this.id=id;
	}

	public void run() {
		while (true) {
			r1= new Random(System.currentTimeMillis()*id);
			r2= new Random();
			if(r2.nextInt(2)==0)
			{
				
				try {
					p.daEstTrattoEInizio(id);
					Thread.sleep(r1.nextInt(3000));
					p.daEstTrattoEFine(id);
					Thread.sleep(r1.nextInt(3000));
					p.daEstTrattoOInizio(id);
					Thread.sleep(r1.nextInt(3000));
					p.daEstTrattoOFine(id);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}		
				
			}
			else{
				try {
					p.daOvestTrattoOInizio(id);
					Thread.sleep(r1.nextInt(3000));
					p.daOvestTrattoOFine(id);
					Thread.sleep(r1.nextInt(3000));
					p.daOvestTrattoEInizio(id);
					Thread.sleep(r1.nextInt(3000));
					p.daOvestTrattoEFine(id);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}	
			}
		}
	}
}
