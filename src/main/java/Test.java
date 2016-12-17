
import encyclopediaofphilosophy.PingSEP;

public class Test {
	
    private PingSEP pingSEP = new PingSEP();
    
	public static void main(String[] args) {
		new Test().run();
	}
	public void run() {
		new Thread(pingSEP).start();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new Thread(pingSEP).start();
	}

}
