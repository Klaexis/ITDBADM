import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Create {
    private int customerNumber;
    private int orderAgain;

    private String productCode;
    private int productQty;
    private int productEach;

    public void placeOrder(){
        Scanner sc = new Scanner(System.in);
        System.out.print("\nEnter Customer Number: ");
        customerNumber = sc.nextInt();

        System.out.print("\nEnter the Required Date: ");
        //Add an input here

        orderAgain = 0;
        while(orderAgain == 1){
            getProduct();
            System.out.print("\nEnter 1 if you want to order again."
                            +"\nEnter any number to proceed."
                            +"\nDo you want to order another product?: ");
            orderAgain = sc.nextInt();
        }

        System.out.print("\nEnter 1 if you want to confirm order.");

        sc.close();
    }

    public void getProduct(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Product Code: ");
        productCode = sc.nextLine();

        System.out.print("\nEnter the Quantity of the Product: ");
        productQty = sc.nextInt();

        System.out.print("\nEnter the Price Each: ");
        productEach = sc.nextInt();

        sc.close();
    }

    public void createOrder(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now();  
        System.out.println(dtf.format(now));  
    }

    public void createOrderDetails(){

    }

    public void updateProductQty(){

    }
}