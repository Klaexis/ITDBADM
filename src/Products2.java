import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Products2 {

    public String   productCode;
    public String   productName;
    public String   productLine;
    public String   productScale;
    public String   productVendor;
    public String   productDescription;
    public int      quantityInStock;
    public float    buyPrice;
    public float    MSRP;
    
    public Products2() {}

    public int getProduct() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Product Code:");
        this.productCode = sc.nextLine();

        try {
        Connection conn;
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbsales?useTimezone=true&serverTimezone=UTC&user=root&password=12345");
        System.out.println("Connection Successful");
        conn.setAutoCommit(false);

        PreparedStatement pstmt = conn.prepareStatement("SELECT productCode, productName, productLine, productScale, productVendor, productDescription, quantityInStock, buyPrice, MSRP FROM products WHERE productCode=? LOCK IN SHARE MODE");
        pstmt.setString(1, productCode);

        System.out.println("Press enter key to start retrieving the data");
        sc.nextLine();

        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            this.productName      = rs.getString("productName");
            this.productLine   = rs.getString("productLine");
            this.productScale    = rs.getString("productScale");
            this.productVendor         = rs.getString("productVendor");
            this.productDescription       = rs.getString("productDescription");
            this.quantityInStock = rs.getInt("quantityInStock");
            this.buyPrice = rs.getFloat("buyPrice");
            this.MSRP = rs.getFloat("MSRP");
        }
        rs.close();

        System.out.println("Product Name:         " + this.productName);
        System.out.println("Product Line:      " + this.productLine);
        System.out.println("Product Scale:       " + this.productScale);
        System.out.println("Product Vendor:             " + this.productVendor);
        System.out.println("Product Description:           " + this.productDescription);
        System.out.println("Quantity in Stock:    " + this.quantityInStock);
        System.out.println("Buy Price:      " + this.buyPrice);
        System.out.println("MSRP:       " + this.MSRP);

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

    public static void main (String args[]) {
        Scanner sc     = new Scanner (System.in);
        int     choice = 0;
        System.out.println("Enter [1] Get Product Info");
        choice = sc.nextInt();

        Products2 p = new Products2();
        if (choice==1) p.getProduct();
        
        System.out.println("Press enter key to continue....");
        sc.nextLine();
    }
    
}
