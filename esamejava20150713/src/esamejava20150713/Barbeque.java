package esamejava20150713;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Barbeque {
	private Lock lock = new ReentrantLock();
	private Condition pn = lock.newCondition();
	private Condition psg = lock.newCondition();
	private Condition inserviente = lock.newCondition();
	private int sosp_pn = 0;
	private int sosp_psg = 0;
	private int sosp_inserviente = 0;
	private int maxGriglia;	
	private int puliziaincorso = 0;
	private int nPN = 0;
	private int nPSG = 0;
	private int pulito=1;	
	private final static int PN = 1;
	private final static int PSG = 0;
	
	public Barbeque(int maxGriglia) {
		super();
		this.maxGriglia = maxGriglia;
	}
	
	public void inizioPiatto(int tipo,int id) throws InterruptedException{
		lock.lock();
		try{
		while(nPN==maxGriglia || nPSG ==maxGriglia || (tipo == PN && nPSG>0) || (tipo == PSG && nPN>0) || (tipo == PN && sosp_psg >0) || puliziaincorso == 1 || (tipo == PSG && pulito==0) )
		{
			if(tipo == PN)
			{
				sosp_pn++;
				System.out.println("[Barbeque]: Sospeso PN con id: "+id+ ", coda sospesi PN: "+sosp_pn+", coda sospesi PSG: "+sosp_psg);
				pn.await();
				sosp_pn--;
			}
			else
			{
				sosp_psg++;
				System.out.println("[Barbeque]: Sospeso PSG con id: "+id+ ", coda sospesi PN: "+sosp_pn+", coda sospesi PSG: "+sosp_psg);
				psg.await();
				sosp_psg--;
			}
		}
		if(tipo == PN)
		{
			nPN++;
			pulito=0;
			System.out.println("[Barbeque]: Inizio preparazione PN con id: "+id);
		}
		else
		{
			nPSG++;
			System.out.println("[Barbeque]: Inizio preparazione PSG con id: "+id);
		}
		System.out.println("[Barbeque]: PN sulla griglia: "+nPN+", PSG sulla griglia: "+nPSG);
		}finally{lock.unlock();}
	}
	
	public void finePiatto(int tipo,int id){
		lock.lock();
		try{
		if(tipo == PN)
		{
			nPN--;
			System.out.println("[Barbeque]: Fine preparazione PN con id: "+id);
		}
		else
		{
			nPSG--;
			System.out.println("[Barbeque]: Fine preparazione PSG con id: "+id);
		}
		System.out.println("[Barbeque]: PN sulla griglia: "+nPN+", PSG sulla griglia: "+nPSG);
		if(sosp_psg>0 && pulito==0)
		{
			inserviente.signal();
		}
		else if(sosp_psg>0 && pulito ==1)
		{
			psg.signal();
		}
		else
		{
			pn.signal();
		}
		}finally{lock.unlock();}
		
	}
	
	public void inizioPulizia() throws InterruptedException{
		lock.lock();
		try{
		while(pulito == 1 || nPN>0 ||nPSG >0 )
		{
			sosp_inserviente++;
			inserviente.await();
			sosp_inserviente--;
		}
		puliziaincorso=1;
		pulito =1;
		System.out.println("[Barbeque]: Inizio pulizia");
		}finally{lock.unlock();}
	}
	
	public void finePulizia(){
		lock.lock();
		try{
			puliziaincorso=0;
			if(sosp_psg>0)
			{
				psg.signalAll();
			}
			else if(sosp_pn>0)
			{
				pn.signalAll();
			}
			System.out.println("[Barbeque]: Fine pulizia");
		}finally{lock.unlock();}
	
	}


	
}
