package esamejava20110628;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Spogliatoio {
	private Lock lock = new ReentrantLock();
	private Condition su = lock.newCondition();
	private Condition sd = lock.newCondition();
	private Condition g = lock.newCondition();
	private int sosp_su = 0;
	private int sosp_sd = 0;
	private int sosp_g = 0;
	private int maxspoglia;
	private int maxcabine;
	private int ncabine = 0;
	private int nspoglia=0;
	private int occU = 0;
	private int occD = 0;
	private int occG = 0;
	
	
	public Spogliatoio(int max, int cabine) {
		super();
		this.maxspoglia = max;
		this.maxcabine = cabine;
	}
	
	public void entraS(int genere, int i) throws InterruptedException
	{
		Boolean fine = false;
		lock.lock();
		try {
			while (!fine) {
				if(genere == 1)
				{
					if(occD> 0||occG > 0 || sosp_sd >0 || sosp_g>0 || nspoglia ==maxspoglia || ncabine== maxcabine )
					{
						sosp_su++;
						su.await();
						sosp_su--;
					}
					else
					{
						System.out.println("[Spogliatoio]: Entrato uomo con id: "+i);
						occU++;
						ncabine++;
						nspoglia++;
						fine=true;
						System.out.println("[Spogliatoio]: Persone nello spogliatoio: "+nspoglia+", con numero cabine occupate: "+ncabine);
					}
				}
				else
				{
					if(occU >0 || occG > 0 || sosp_g>0 || nspoglia ==maxspoglia || ncabine== maxcabine )
					{
						sosp_su++;
						su.await();
						sosp_su--;
					}
					else
					{
						System.out.println("[Spogliatoio]: Entrata donna con id: "+i);
						occD++;
						ncabine++;
						nspoglia++;
						fine=true;
						System.out.println("[Spogliatoio]: Persone nello spogliatoio: "+nspoglia+", con numero cabine occupate: "+ncabine);
					}
				}
			}
		} finally{lock.unlock();}
	}
	
	public void esceS(int genere,int i)
	{
		lock.lock();
		try{
			if(genere ==1)
			{
				occU--;
				nspoglia--;
				ncabine--;
				System.out.println("[Spogliatoio]: Uscito uomo con id: "+i+" dallo spogliatoio");
			}
			else{
				occD--;
				nspoglia--;
				ncabine--;
				System.out.println("[Spogliatoio]: Uscita donna con id: "+i+" dallo spogliatoio");
			}
			System.out.println("[Spogliatoio]: Persone nello spogliatoio: "+nspoglia+", con numero cabine occupate: "+ncabine);
			if(sosp_g>0 )
			{
				g.signalAll();
			}
			else if(sosp_sd>0)
			{
				sd.signalAll();
			}
			else{
				su.signalAll();
			}
		}finally{lock.unlock();}
	}
	
	public void entraG(int numerog,int i) throws InterruptedException
	{
		Boolean fine = false;
		lock.lock();
		try {
			while (!fine) {
				if(occU >0  || occD>0 ||nspoglia+numerog >maxspoglia || ncabine== maxcabine)
				{
					sosp_g++;
					g.await();
					sosp_g--;
				}
				else
				{
					System.out.println("[Spogliatoio]: Entrato gruppo: "+i+" di "+numerog+" persone");
					occG++;
					ncabine++;
					nspoglia= nspoglia + numerog;
					fine=true;
					System.out.println("[Spogliatoio]: Persone nello spogliatoio: "+nspoglia+", con numero cabine occupate: "+ncabine);
				}
			}
		}finally{lock.unlock();}
		
	}
	
	public void esceG(int numerog,int i)
	{
		lock.lock();
		try{
				occG--;
				nspoglia = nspoglia -numerog;
				ncabine--;
				System.out.println("[Spogliatoio]: Uscito gruppo: "+i+" di "+numerog+" persone");
				System.out.println("[Spogliatoio]: Persone nello spogliatoio: "+nspoglia+", con numero cabine occupate: "+ncabine);
			if(sosp_g > 0)
			{
				g.signalAll();
			}
			else if(sosp_sd>0 )
			{
				sd.signalAll();
			}
			else{
				su.signalAll();
			}
		}finally{lock.unlock();}
	}
	
}
