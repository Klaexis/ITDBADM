import java.sql.*;
import java.util.*;

public class Create {


    public void placeOrder(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Customer Number:");
        int customerNumber = sc.nextInt();

        try{
            Connection conn; 
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbsales?useTimezone=true&serverTimezone=UTC&user=root&password=1234");
            System.out.println("Connection Successful");
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement("SELECT customerNumber" 
                                                            +"FROM customers" 
                                                            +"WHERE customerNumber=?");
            pstmt.setInt(1, customerNumber);

            

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        sc.close();
    }

    public void getProduct(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Product Code: ");
        String productCode = sc.nextLine();



        sc.close();
    }
}