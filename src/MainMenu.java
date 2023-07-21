import java.util.*;

public class MainMenu {
    private Create createTab;
    private Orders ordersTab;
    
    public MainMenu(){
        //this.createTab = new Create();
        this.ordersTab = new Orders();
    }

    public void Starts(Scanner sc)
    {
        boolean isContinue = true;

        while(isContinue)//Menu will always continue until exitted.
        {
            isContinue = this.Menu(sc);
        }
    }

    public boolean Menu(Scanner sc)
    {
        System.out.print("Main Menu\n"
                        +"Enter 1 to Create and Order \n"
                        +"Enter 2 to Inquire for Products\n"
                        +"Enter 3 to Retrieve Info about the Order\n"
                        +"Enter 4 to Cancel Order\n"
                        +"Enter 5 to Exit\n\n");
        boolean isContinue = true;

        System.out.print("Enter a number (1-5): ");
        int userInput = sc.nextInt();

        if(userInput == 1)//Create and Order
        {
            createTab.placeOrder();
            isContinue = true;
        }
        else if(userInput == 2)//Inquire for Products
        {
            isContinue = true;
        }
        else if(userInput == 3)//Retrieve Info about the Order
        {
            ordersTab.getOrder(sc);
            isContinue = true;
        }
        else if(userInput == 4)//Cancel Order
        {
            isContinue = true;
        }
        else if(userInput == 5)//Exit the Program
        {
            isContinue = false;
        }
        else 
        {
            System.out.println("INVALID INPUT");
            isContinue = true;
        }
        return isContinue;
    }
}
