import java.util.Scanner; 

public class Main {
	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		MainMenu mainMenu = new MainMenu();
		System.out.println("Transaction Management\n"
				+ "ITDBADM\n"
				+ "Created By:\n"
				+ "Aleck Jasper Lim\n"
				+ "Jeff Alison Wang\n"
				+ "Jonathan Lin\n");
		mainMenu.Starts(sc);

		sc.close();
	}
}