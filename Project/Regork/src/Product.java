import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Product
{
    private int ProductId;
    private String Name;
    private double Price;

    public Product(int productId, String productName, double price)
    {
        this.ProductId = productId;
        this.Name = productName;
        this.Price = price;
    }

    // TODO: get formatted product Id (String)
    public int GetProductId()
    {
        return this.ProductId;
    }

    public String GetName()
    {
        return this.Name;
    }

    public double GetPrice()
    {
        return this.Price;
    }

    // TODO: get formatted price (String)
    public void SetPrice(double price)
    {
        this.Price = price;
    }

    public void SetName(String name)
    {
        this.Name = name;
    }

    public static Product GetById(Connection conn, int productId)
    {
        try
        {
            PreparedStatement stmt = conn.prepareStatement("SELECT productName, price FROM productLine WHERE productLineId = ?");
            stmt.setInt(1, productId);
            ResultSet res = stmt.executeQuery();

            if (res.next())
            {
                String name = res.getString("PRODUCTNAME");
                double price = res.getDouble("PRICE");

                Product product = new Product(productId, name, price);

                stmt.close();
                return product;
            }
        }
        catch (Exception e)
        {
            System.out.println("An error occurred while trying to retrieve the product. Please try again");
        }

        return null;
    }

    public static ArrayList<Product> FindByName(Connection conn, String search)
    {
        ArrayList<Product> products = new ArrayList<>();

        try
        {
            String query = "SELECT productLineId, productName, price FROM productLine WHERE INSTR(productName COLLATE BINARY_CI, ?) <> 0 ORDER BY productLineId";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, search);
            ResultSet res = stmt.executeQuery();

            while (res.next())
            {
                int id = res.getInt("PRODUCTLINEID");
                String name = res.getString("PRODUCTNAME");
                double price = res.getDouble("PRICE");

                Product product = new Product(id, name, price);
                products.add(product);
            }

            stmt.close();
        }
        catch (Exception e)
        {
            System.out.println("An error occurred while searching for products. Please try again.");
        }

        return products;
    }
}
