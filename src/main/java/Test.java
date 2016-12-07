import encyclopediaofphilosophy.EncyclophiaOfPhilosophySpeechlet;

public class Test {
	
	public static void main(String[] args) {
		new Test().run();
	}
	private void run() {
		EncyclophiaOfPhilosophySpeechlet speechlet = new EncyclophiaOfPhilosophySpeechlet();
		System.out.println(  speechlet.getEntryFromStanford() );
	}

}
