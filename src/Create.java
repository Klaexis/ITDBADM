import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Create {
    private MainMenu mainMenuTab;

    private String url = "jdbc:mysql://localhost:3306/dbsales";
    private String username = "root";
    private String password = "1234";

    private int customerNumber;
    private int verifyCustomer;
    private int orderAgain;
    private String requiredDate;
    private int newOrderNumber;

    private String productCode;
    private int verifyProduct;
    private int productQty;
    private int getQuantity;
    private float priceEach;

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
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn;
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connection Successful");
            conn.setAutoCommit(false);

            //Step 1
            //Enter Customer Number
            System.out.print("\nEnter Customer Number: ");
            customerNumber = sc.nextInt();

            //Get the count of customer to verify if customer exists.
            PreparedStatement verifyCustomerStatement = conn.prepareStatement("SELECT COUNT(customerNumber) FROM customers WHERE customerNumber=?");
            verifyCustomerStatement.setInt(1, customerNumber);

            ResultSet verifyCustomerResultSet = verifyCustomerStatement.executeQuery();  

            while(verifyCustomerResultSet.next()){
                verifyCustomer = verifyCustomerResultSet.getInt("COUNT(customerNumber)");
            }

            verifyCustomerResultSet.close();

            //If customer does not exist then go back to start.
            if(verifyCustomer != 1){
                System.out.print("\nCustomer does not exist.\n");
                placeOrder();
            }
            
            //Step 2
            //Enter the Required Date.
            System.out.print("\nEnter the Required Date (YYYY-MM-DD): ");
            requiredDate = sc.nextLine();

            //Step 3
            //Autogenerate Order Number
            PreparedStatement incrementKeyStatement = conn.prepareStatement("SELECT max(orderNumber) FROM orders");

            ResultSet incrementKeyResultSet = incrementKeyStatement.executeQuery();  

            while(incrementKeyResultSet.next()){
                //Increment Order Number
                newOrderNumber = incrementKeyResultSet.getInt("orderNumber") + 1;
            }

            incrementKeyResultSet.close();
            verifyCustomerStatement.close();
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
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn;
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connection Successful");
            conn.setAutoCommit(false);

            //Step 4
            //Enter Product Code
            System.out.print("Enter Product Code: ");
            productCode = sc.nextLine();

            //Get the count of product to verify if product exists.
            PreparedStatement verifyProductStatement = conn.prepareStatement("SELECT COUNT(productCode) FROM products WHERE productCode=?");
            verifyProductStatement.setString(1, productCode);

            ResultSet verifyProductResultSet = verifyProductStatement.executeQuery();  

            while(verifyProductResultSet.next()){
                verifyProduct = verifyProductResultSet.getInt("COUNT(productCode)");
            }

            verifyProductResultSet.close();

            //If product does not exist then go back to start.
            if(verifyProduct != 1){
                System.out.print("\nProduct does not exist.\n");
                placeOrder();
            }

            //Step 5
            //Enter the Quantity of the Product
            System.out.print("\nEnter the Quantity of the Product: ");
            productQty = sc.nextInt();

            //Get the quantityInStock of the product.
            PreparedStatement qtyStockStatement = conn.prepareStatement("SELECT quantityInStock FROM products WHERE productCode=?");
            qtyStockStatement.setString(1, productCode);

            ResultSet qtyStockResultSet = qtyStockStatement.executeQuery();  

            while(qtyStockResultSet.next()){
                getQuantity = qtyStockResultSet.getInt("COUNT(productCode)");
            }

            qtyStockResultSet.close();

            //If customer ordered more than the quantity in stock then go back to start.
            if(productQty > getQuantity){
                System.out.print("\nExceeded the total quantity in stock.\n");
                placeOrder();
            }

            //Step 6
            //Enter the PriceEach
            System.out.print("\nEnter the Price Each: ");
            priceEach = sc.nextFloat();

            verifyProductStatement.close();
            qtyStockStatement.close();
            conn.commit();
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        sc.close();
    }

    //Step 9
    public void createOrder(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn;
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connection Successful");
            conn.setAutoCommit(false);

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
            LocalDateTime now = LocalDateTime.now();  
            System.out.println(dtf.format(now));  
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
    }

    //Step 10
    public void createOrderDetails(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn;
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connection Successful");
            conn.setAutoCommit(false);

            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //Step 11
    public void updateProductQty(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn;
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connection Successful");
            conn.setAutoCommit(false);

            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args)
	  {
        Create c = new Create();
	    c.placeOrder();
	  }
}