import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Create {
    private MainMenu mainMenuTab;

    private String url = 
    "jdbc:mysql://localhost:3306/dbsales?useTimezone=true&serverTimezone=UTC&user=root&password=12345";

    private int customerNumber;
    private int verifyCustomer;
    private int orderAgain;

    private String productCode;
    private int productQty;
    private int productEach;

    public Create(){
        this.mainMenuTab = new MainMenu();
    }

    public void placeOrder(){
        Scanner sc = new Scanner(System.in);
        
        getCustomer(); //Steps 1 to 3

        //Step 7
        //To iterate if user wants to order multiple times. 
        //If user enters any number except 1 then go out of the while loop.
        orderAgain = 1;
        while(orderAgain == 1){
            getProduct(); //Steps 4 to 6
            
            System.out.print("\nEnter 1 if you want to order again."
                            +"\nEnter any number to proceed."
                            +"\nDo you want to order another product?: ");
            orderAgain = sc.nextInt();
        }

        //Step 8
        System.out.print("\nEnter 1 if you want to confirm order."
                        +"\nEnter any number to reject order."
                        +"\nConfrim your order?: ");
        int confirmInput = sc.nextInt();

        if(confirmInput == 1){
            createOrder(); //Step 9
            createOrderDetails(); //Step 10
            updateProductQty(); //Step 11

            //Maybe add a flush for the array after everything is done.

            mainMenuTab.Menu(); //Return to Main Menu
        } else {
            mainMenuTab.Menu(); //Return to Main Menu
        }

        sc.close();
    }

    //Steps 1 to 3
    public void getCustomer(){
        Scanner sc = new Scanner(System.in);

        try{
            Connection conn;
            conn = DriverManager.getConnection(url);
            System.out.println("Connection Successful");
            conn.setAutoCommit(false);

            //Enter Customer Number
            System.out.print("\nEnter Customer Number: ");
            customerNumber = sc.nextInt();

            //Get the count of customer to verify if customer exists.
            PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(customerNumber) FROM customers WHERE customerNumber=?");
            pstmt.setInt(1, customerNumber);

            ResultSet rs = pstmt.executeQuery();  

            while(rs.next()){
                verifyCustomer = rs.getInt("COUNT(customerNumber)");
            }

            rs.close();

            //If customer does not exist then go back to start.
            if(verifyCustomer != 1){
                System.out.print("\nCustomer does not exist.\n");
                placeOrder();
            }
            
            //Enter the Required Date.
            System.out.print("\nEnter the Required Date: ");
            //Add an input here

            //Add here autogenerate Order Number

            pstmt.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        sc.close();
    }

    //Steps 4 to 6
    public void getProduct(){
        Scanner sc = new Scanner(System.in);
        
        try{
            Connection conn;
            conn = DriverManager.getConnection(url);
            System.out.println("Connection Successful");
            conn.setAutoCommit(false);

            //Enter Product Code
            System.out.print("Enter Product Code: ");
            productCode = sc.nextLine();

            //Enter the Quantity of the Product
            System.out.print("\nEnter the Quantity of the Product: ");
            productQty = sc.nextInt();

            //Enter the PriceEach
            System.out.print("\nEnter the Price Each: ");
            productEach = sc.nextInt();

            conn.commit();
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        sc.close();
    }

    //Step 9
    public void createOrder(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now();  
        System.out.println(dtf.format(now));  
    }

    //Step 10
    public void createOrderDetails(){

    }

    //Step 11
    public void updateProductQty(){

    }
}