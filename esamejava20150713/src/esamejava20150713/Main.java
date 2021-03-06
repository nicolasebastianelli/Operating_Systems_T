package esamejava20150713;

public class Main {
	final static int nPiatti = 10;
	final static int maxGriglia = 5;

	public static void main(String args[]) {
		System.out.println("***************** AVVIO ESECUZIONE *****************");
		System.out.println("[MAIN]: Numero massino di piatti sulla griglia: " + maxGriglia);
		Barbeque barbeque = new Barbeque(maxGriglia);
		Piatti[] piatti = new Piatti[nPiatti];
		Inserviente inserviente = new Inserviente(barbeque);
		for (int i = 0; i < nPiatti; i++) {
			piatti[i] = new Piatti(barbeque, i);
		}
		for (int i = 0; i < nPiatti; i++) {
			piatti[i].start();
		}
		inserviente.start();
	}
}
