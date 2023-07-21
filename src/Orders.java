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

    public Orders() {
    }

    public int getOrder(Scanner sc) {
<<<<<<< Updated upstream
        // Scanner sc = new Scanner(System.in);
        System.out.println("Enter Order Number:");
=======
        //Scanner sc = new Scanner(System.in);
        System.out.print("Enter Order Number: ");
>>>>>>> Stashed changes
        orderNumber = sc.nextInt();
        //System.out.println(orderNumber);

        try {
            Connection conn;
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/dbsales?useTimezone=true&serverTimezone=UTC&user=root&password=1234");
            System.out.println("Connection Successful");
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT orderNumber, orderDate, requiredDate, shippedDate, status, comments, customerNumber FROM orders WHERE orderNumber=? AND status NOT LIKE 'Cancelled';");
            pstmt.setInt(1, orderNumber);

            //System.out.println("Press enter key to start retrieving the data");
            //sc.nextLine();

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                orderDate = rs.getString("orderDate");
                requiredDate = rs.getString("requiredDate");
                shippedDate = rs.getString("shippedDate");
                status = rs.getString("status");
                comments = rs.getString("comments");
                customerNumber = rs.getInt("customerNumber");
            }
            rs.close();

            if(orderDate != null){
                System.out.println("\nORDER DETAILS");
                System.out.println("Customer Number:  " + customerNumber);
                System.out.println("Order Date:       " + orderDate);
                System.out.println("Required Date:    " + requiredDate);
                System.out.println("Shipped Date:     " + shippedDate);
                System.out.println("Status:           " + status);
                System.out.println("Comments:         " + comments + "\n");
            } else {
                System.out.println("\nNO ORDER FOUND\n"); 
            }
            pstmt.close();
            conn.commit();
            conn.close();
            return 1;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

<<<<<<< Updated upstream
    /*
     * public int cancelOrder() {
     * Scanner sc = new Scanner(System.in);
     * this.getOrder();
     * try {
     * Connection conn;
     * conn = DriverManager.getConnection(
     * "jdbc:mysql://localhost:3306/dbsales?useTimezone=true&serverTimezone=UTC&user=root&password=1234"
     * );
     * System.out.println("Press any key to cancel");
     * sc.nextLine();
     * 
     * // Set Status to Cancelled
     * PreparedStatement pstmt = conn.prepareStatement(
     * "UPDATE orders SET status = 'Cancelled' WHERE orderNumber=? AND status =
     * 'Shipped';");
     * pstmt.setInt(1, orderNumber);
     * pstmt.executeUpdate();
     * 
     * // Retrieves Quantity Ordered Adds with Quantity in Stock
     * pstmt = conn.prepareStatement(
     * "SELECT (p.quantityInStock + od.quantityOrdered) AS newQty, p.productCode
     * FROM products p JOIN orderdetails od ON od.productCode = p.productCode WHERE
     * od.orderNumber = ?;");
     * pstmt.setInt(1, orderNumber);
     * 
     * ResultSet rs = pstmt.executeQuery();
     * 
     * // Retrieves Values to Update
     * ArrayList<Integer> newQty = new ArrayList<>();
     * ArrayList<String> productCodes = new ArrayList<>();
     * while (rs.next()) {
     * newQty.add(rs.getInt("newQty"));
     * productCodes.add(rs.getString("productCode"));
     * }
     * rs.close();
     * 
     * // Updates New Quantity
     * for (int i = 0; i < newQty.size(); i++) {
     * pstmt = conn.prepareStatement("UPDATE products SET quantityInStock = ? WHERE
     * productCode = ?");
     * pstmt.setInt(1, newQty.get(i));
     * pstmt.setString(2, productCodes.get(i));
     * pstmt.executeUpdate();
     * }
     * 
     * pstmt.close();
     * conn.setAutoCommit(false);
     * conn.commit();
     * conn.close();
     * return 1;
     * } catch (Exception e) {
     * System.out.println(e.getMessage());
     * return 0;
     * }
     * }
     * 
     * public static void main(String args[]) {
     * Scanner sc = new Scanner(System.in);
     * int choice = 0;
     * System.out.println("Enter [1] Create and Order [2] Inquire Products [3]
     * Retrieve Order [4] Cancel Order:");
     * choice = sc.nextInt();
     * 
     * Orders o = new Orders();
     * // if (choice == 1) o.getOrder();
     * // if (choice == 2) new Products().getInfo();
     * if (choice == 3)
     * o.getOrder();
     * if (choice == 4)
     * o.cancelOrder();
     * 
     * System.out.println("Press enter key to continue....");
     * sc.nextLine();
     * }
     */

=======
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

    /* 
     public static void main (String args[]) {
        Scanner sc = new Scanner (System.in);
        int     choice = 0;
        System.out.println("Enter [1] Create and Order [2] Inquire Products [3] Retrieve Order  [4] Cancel Order:");
        choice = sc.nextInt();

        Orders o = new Orders();
        // if (choice == 1) o.getOrder();
        // if (choice == 2) new Products().getInfo();
        if (choice == 3) o.getOrder();
        if (choice == 4) o.cancelOrder();

        System.out.println("Press enter key to continue....");
        sc.nextLine();
    }*/
    
>>>>>>> Stashed changes
}
