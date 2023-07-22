import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;

public class Orders {
    public int  orderNumber;
    public String orderDate;
    public String requiredDate;
    public String shippedDate;
    public String status;
    public String comments;
    public int customerNumber;

    public Orders() {}

    // c. Retrieve Info about the Order
    public int getOrder() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Order Number:");
        this.orderNumber = sc.nextInt();

        try {
            Connection conn;
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbsales?useTimezone=true&serverTimezone=UTC&user=root&password=12345");
            System.out.println("Connection Successful");
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement("SELECT orderNumber, orderDate, requiredDate, shippedDate, status, comments, customerNumber FROM orders WHERE orderNumber=? AND status NOT LIKE 'Cancelled' LOCK IN SHARE MODE;");
            pstmt.setInt(1, orderNumber);

            System.out.println("Press enter key to start retrieving the data");
            sc.nextLine();

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                this.orderDate      = rs.getString("orderDate");
                this.requiredDate   = rs.getString("requiredDate");
                this.shippedDate    = rs.getString("shippedDate");
                this.status         = rs.getString("status");
                this.comments       = rs.getString("comments");
                this.customerNumber = rs.getInt("customerNumber");
            }
            rs.close();

            System.out.println("Order Date:         " + this.orderDate);
            System.out.println("Required Date:      " + this.requiredDate);
            System.out.println("Shipped Date:       " + this.shippedDate);
            System.out.println("Status:             " + this.status);
            System.out.println("Comments:           " + this.comments);
            System.out.println("Customer Number:    " + this.customerNumber);

            pstmt = conn.prepareStatement("SELECT od.orderLineNumber, od.productCode, p.productName, od.quantityOrdered, od.priceEach FROM orderdetails od JOIN products p ON p.productCode = od.productCode WHERE od.orderNumber = ? ORDER BY od.orderLineNumber LOCK IN SHARE MODE;");
            pstmt.setInt(1, orderNumber);

            rs = pstmt.executeQuery();
            float totalPrice = 0;

            while (rs.next()) {
                int quantityOrdered = rs.getInt("quantityOrdered");
                float priceEach = rs.getFloat("priceEach");

                System.out.println("Order Line No.:     " + rs.getString("orderLineNumber"));
                System.out.println("Product Code:       " + rs.getString("productCode"));
                System.out.println("Product Name:       " + rs.getString("productName"));
                System.out.println("Quantity Ordered:   " + quantityOrdered);
                System.out.println("Price Each:         " + priceEach);
                System.out.println("-------------------------------------------------------------");

                totalPrice += (quantityOrdered * priceEach);
            }
            rs.close();

            System.out.println("Order Total Price: " + totalPrice);
            System.out.println("\n");
            System.out.println("Press enter key to end transaction");
            sc.nextLine();

            pstmt.close();
            conn.commit();
            conn.close();
            return 1;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    // d. Cancel Order
    public int cancelOrder() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Order Number:");
        this.orderNumber = sc.nextInt();

        try {
            Connection conn;
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbsales?useTimezone=true&serverTimezone=UTC&user=root&password=12345");
            System.out.println("Connection Successful");
            conn.setAutoCommit(false);

            PreparedStatement pstmt = conn.prepareStatement("SELECT orderNumber, orderDate, requiredDate, shippedDate, status, comments, customerNumber FROM orders WHERE orderNumber = ? FOR UPDATE;");
            pstmt.setInt(1, orderNumber);

            System.out.println("Press enter key to start retrieving the data");
            sc.nextLine();

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                this.orderDate      = rs.getString("orderDate");
                this.requiredDate   = rs.getString("requiredDate");
                this.shippedDate    = rs.getString("shippedDate");
                this.status         = rs.getString("status");
                this.comments       = rs.getString("comments");
                this.customerNumber = rs.getInt("customerNumber");
            }
            rs.close();

            System.out.println("Order Date:             " + this.orderDate);
            System.out.println("Required Date:          " + this.requiredDate);
            System.out.println("Shipped Date:           " + this.shippedDate);
            System.out.println("Status:                 " + this.status);
            System.out.println("Comments:               " + this.comments);
            System.out.println("Customer Number:        " + this.customerNumber);

            System.out.println("-------------------------------------------------------------");

            // Retrieves Quantity Ordered Adds with Quantity in Stock
            pstmt = conn.prepareStatement("SELECT orderNumber, productCode, quantityOrdered, priceEach, orderLineNumber FROM orderdetails WHERE orderNumber = ?;");
            pstmt.setInt(1, orderNumber);
            rs = pstmt.executeQuery();

            // Retrieves Values to Update
            while (rs.next()) {
                System.out.println("Product Code Ordered:   " + rs.getString("productCode"));
                System.out.println("Quantity Ordered:       " + rs.getInt("quantityOrdered"));
                System.out.println("Price Each:             " + rs.getInt("priceEach"));
                System.out.println("Order Line Number:      " + rs.getInt("orderLineNumber"));
                System.out.println("-------------------------------------------------------------");
            }
            rs.close();

            System.out.println("Press any key to cancel");
            sc.nextLine();

            if (!this.status.equals("Shipped")) {
                pstmt = conn.prepareStatement("UPDATE orders SET status = 'Cancelled' WHERE orderNumber=? AND status NOT LIKE 'Cancelled';");
                pstmt.setInt(1, orderNumber);
                pstmt.executeUpdate();


                pstmt = conn.prepareStatement("SELECT (p.quantityInStock + od.quantityOrdered) AS newQty, p.productCode FROM products p JOIN orderdetails od ON od.productCode = p.productCode WHERE od.orderNumber = ? FOR UPDATE;");
                pstmt.setInt(1, orderNumber);

                rs = pstmt.executeQuery();

                ArrayList<Integer> newQty = new ArrayList<>();
                ArrayList<String> productCodes = new ArrayList<>();
                while (rs.next()) {
                    newQty.add(rs.getInt("newQty"));
                    productCodes.add(rs.getString("productCode"));
                }
                rs.close();

                // Updates New Quantity
                for(int i = 0; i < newQty.size(); i++) {
                    pstmt = conn.prepareStatement("UPDATE products SET quantityInStock = ? WHERE productCode = ?;");
                    pstmt.setInt(1, newQty.get(i));
                    pstmt.setString(2, productCodes.get(i));
                    pstmt.executeUpdate();
                }

                System.out.println("Order Number " + this.orderNumber + " has successfully been cancelled.");
            }
            else System.out.println("You cannot cancel an order that has already been shipped.");

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

    public static void main (String args[]) {
        Scanner sc = new Scanner (System.in);
        int     choice = 0;
        System.out.println("Enter [1] Create and Order [2] Inquire Products [3] Retrieve Order  [4] Cancel Order:");
        choice = sc.nextInt();

        Orders o = new Orders();
//        if (choice == 1) o.getOrder();
        if (choice == 2) new Products().getInfo();
        if (choice == 3) o.getOrder();
        if (choice == 4) o.cancelOrder();

        System.out.println("Press enter key to continue....");
        sc.nextLine();
    }
}
