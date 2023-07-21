import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;

public class Orders {
    public int orderNumber;
    public String orderDate;
    public String requiredDate;
    public String shippedDate;
    public String status;
    public String comments;
    public int customerNumber;
    public int productCode; 

    public Orders() { }

    public int getOrder(Scanner sc) {
        //Scanner sc = new Scanner(System.in);
        System.out.print("Enter Order Number: ");
        orderNumber = sc.nextInt();
        sc.nextLine();
        //System.out.println(orderNumber);

        try {
            Connection conn;
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/dbsales?useTimezone=true&serverTimezone=UTC&user=root&password=bakugan02");
            System.out.println("Connection Successful");
            conn.setAutoCommit(false);

            //PreparedStatement pstmt = conn.prepareStatement( "SELECT orderNumber, orderDate, requiredDate, shippedDate, status, comments, customerNumber FROM orders WHERE orderNumber=? AND status NOT LIKE 'Cancelled';");
            PreparedStatement pstmt = conn.prepareStatement("SELECT o.orderNumber, o.orderDate, o.requiredDate, o.shippedDate, o.status, o.comments, o.customerNumber, od.productCode, od.quantityOrdered FROM orders o JOIN orderdetails od ON o.orderNumber = od.orderNumber WHERE o.orderNumber=? AND status NOT LIKE 'Cancelled';");
            pstmt.setInt(1, orderNumber);

            System.out.println("Press enter key to start retrieving the data");
            sc.nextLine();

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                orderDate = rs.getString("orderDate");
                requiredDate = rs.getString("requiredDate");
                shippedDate = rs.getString("shippedDate");
                status = rs.getString("status");
                comments = rs.getString("comments");
                customerNumber = rs.getInt("customerNumber");
                productCode = rs.getInt("productCode");
            }
            rs.close();

            if(orderDate != null){
                System.out.println("ORDER DETAILS");
                System.out.println("Order Date:       " + orderDate);
                System.out.println("Required Date:    " + requiredDate);
                System.out.println("Shipped Date:     " + shippedDate);
                System.out.println("Status:           " + status);
                System.out.println("Comments:         " + comments);
                System.out.println("Product Code:         " + productCode);
                System.out.println("Customer Number:  " + customerNumber+ "\n");
            } else {
                System.out.println("\nNO ORDER FOUND\n"); 
            }

            System.out.println("Press enter key to end transaction");
            sc.nextLine();

            pstmt.close();
            conn.commit();
            conn.close();
            return 1;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public int cancelOrder(Scanner sc) {
        //Scanner sc = new Scanner(System.in);
        this.getOrder(sc);
        try {
            Connection conn;
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbsales?useTimezone=true&serverTimezone=UTC&user=root&password=bakugan02");
            System.out.println("Press any key to cancel");
            sc.nextLine();

            // Set Status to Cancelled
            PreparedStatement pstmt = conn.prepareStatement("UPDATE orders SET status = 'Cancelled' WHERE orderNumber=? AND status = 'Shipped';");
            pstmt.setInt(1, orderNumber);
            pstmt.executeUpdate();

            // Retrieves Quantity Ordered Adds with Quantity in Stock
            pstmt = conn.prepareStatement("SELECT (p.quantityInStock + od.quantityOrdered) AS newQty, p.productCode FROM products p JOIN orderdetails od ON od.productCode = p.productCode WHERE od.orderNumber = ?;");
            pstmt.setInt(1, orderNumber);

            ResultSet rs = pstmt.executeQuery();

            // Retrieves Values to Update
            ArrayList<Integer> newQty = new ArrayList<>();
            ArrayList<String> productCodes = new ArrayList<>();
            while (rs.next()) {
                newQty.add(rs.getInt("newQty"));
                productCodes.add(rs.getString("productCode"));
            }
            rs.close();

            // Updates New Quantity
            for(int i = 0; i < newQty.size(); i++) {
                pstmt = conn.prepareStatement("UPDATE products SET quantityInStock = ? WHERE productCode = ?");
                pstmt.setInt(1, newQty.get(i));
                pstmt.setString(2, productCodes.get(i));
                pstmt.executeUpdate();
            }

            pstmt.close();
            conn.setAutoCommit(false);
            conn.commit();
            conn.close();
            return 1;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }
}
