import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Product
{
    private int ProductId;
    private String Name = "";
    private double Price = 0;

    public Product(int productId)
    {
        this.ProductId = productId;
    }

    public Product(int productId, String productName, double price)
    {
        this.ProductId = productId;
        SetName(productName);
        SetPrice(price);
    }

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

    public String GetFormattedPrice()
	{
		return String.format("%.2f", this.Price);
	}

    public void SetPrice(double price)
    {
        this.Price = price;
    }

    public void SetName(String name)
    {
        this.Name = name;
    }

    public String toString()
    {
        return "Product #" + this.GetProductId() + ": " + this.GetName() + " ($" + this.GetFormattedPrice() + ")";
    }

    public boolean Refresh(Connection conn)
    {
        boolean bSuccess = false;

        try
        {
            PreparedStatement stmt = conn.prepareStatement("SELECT productName, price FROM productLine WHERE productLineId = ?");
            stmt.setInt(1, this.GetProductId());
            ResultSet res = stmt.executeQuery();

            if (res.next())
            {
                String name = res.getString("PRODUCTNAME");
                double price = res.getDouble("PRICE");

                this.SetName(name);
                this.SetPrice(price);

                bSuccess = true;
            }

            stmt.close();
        }
        catch (Exception e)
        {
            bSuccess = false;
            Console.WriteLine("An error occurred while trying to refresh the product. Please try again", "red");
        }

        return bSuccess;
    }

    public static Product GetById(Connection conn, int productId)
    {
        Product product = new Product(productId);

        boolean success = product.Refresh(conn);
        if (success)
        {
            return product;
        }
        else
        {
            return null;
        }
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
            Console.WriteLine("An error occurred while searching for products. Please try again.", "red");
        }

        return products;
    }

    public boolean Save(Connection conn)
    {
        try
        {
            String query = "UPDATE productLine SET productName = ?, price = ? WHERE productLineId = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, this.GetName());
            stmt.setDouble(2, this.GetPrice());
            stmt.setInt(3, this.GetProductId());

            int rowsAffected = stmt.executeUpdate();
            stmt.close();

            conn.commit();

            if (rowsAffected == 0)
            {
                Console.WriteLine("An error occurred and no products were affected by this update.", "red");
                return false;
            }
            else
            {
                return true;
            }

        }
        catch(Exception e)
        {
            try
            {
                conn.rollback();
            }
            catch (Exception rollbackException)
            {
                Console.WriteLine("An error occurred while attempting to revert changes.", "red");
                return false;
            }

            Console.WriteLine("An error occurred while trying to save changes made to this product.", "red");
            Console.WriteLine("The product has not been modified. Please try again.", "red");
            return false;
        }
    }
}
