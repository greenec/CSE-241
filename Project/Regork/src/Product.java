import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Product
{
	private int ProductId;
	public String Name = "";
	public double Price = 0;

	public Product(int productId)
	{
		this.ProductId = productId;
	}

	public Product(int productId, String productName, double price)
	{
		this.ProductId = productId;
		this.Name = productName;
		this.Price = price;
	}

	public int GetProductId()
	{
		return this.ProductId;
	}

	public String GetFormattedPrice()
	{
		return String.format("%.2f", this.Price);
	}

	public String toString()
	{
		return toString(false, false);
	}

	public String toString(boolean formatId)
	{
		return toString(formatId, false);
	}

	public String toString(boolean formatId, boolean showPrice)
	{
		String out = "#";

		if (formatId)
		{
			out += String.format("%-4s", this.GetProductId());
		}
		else
		{
			out += this.GetProductId();
		}

		out += " " + this.Name;

		if (showPrice)
		{
			out += " ($" + this.GetFormattedPrice() + ")";
		}

		return out;
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

				this.Name = name;
				this.Price = price;

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
		} else
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

	public ArrayList<Product> GetIngredients(Connection conn)
	{
		ArrayList<Product> ingredients = new ArrayList<>();

		try
		{
			String query =
				"SELECT p.productLineId, p.productName, p.price " +
					"FROM ingredients i " +
					"INNER JOIN productLine p ON i.componentId = p.productLineId " +
					"WHERE i.productId = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, this.GetProductId());
			ResultSet res = stmt.executeQuery();

			while (res.next())
			{
				int id = res.getInt("PRODUCTLINEID");
				String name = res.getString("PRODUCTNAME");
				double price = res.getDouble("PRICE");

				Product ingredient = new Product(id, name, price);
				ingredients.add(ingredient);
			}

			stmt.close();
		}
		catch (Exception e)
		{
			Console.WriteLine("An error occurred while retrieving ingredients. Please try again.", "red");
		}

		return ingredients;
	}

	public boolean Create(Connection conn)
	{
		try
		{
			String query = "INSERT INTO productLine (productName, price) VALUES (?, ?)";
			PreparedStatement stmt = conn.prepareStatement(query, new String[] {"productLineId"});
			stmt.setString(1, this.Name);
			stmt.setDouble(2, this.Price);

			int rowsAffected = stmt.executeUpdate();

			conn.commit();

			if (rowsAffected == 0)
			{
				Console.WriteLine("An error occurred and this product was not created.");

				stmt.close();
				return false;
			}

			boolean bRet = false;
			try (ResultSet generatedKeys = stmt.getGeneratedKeys())
			{
				if (generatedKeys.next())
				{
					this.ProductId = generatedKeys.getInt(1);
					bRet = true;
				}
				else
				{
					Console.WriteLine("There was an error creating the product. No new ID was assigned.", "red");
				}
			}

			stmt.close();
			return bRet;
		}
		catch (Exception e)
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

			Console.WriteLine("An error occurred while trying to create this product. Please try again.", "red");
			return false;
		}
	}

	public boolean Save(Connection conn)
	{
		try
		{
			String query = "UPDATE productLine SET productName = ?, price = ? WHERE productLineId = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, this.Name);
			stmt.setDouble(2, this.Price);
			stmt.setInt(3, this.GetProductId());

			int rowsAffected = stmt.executeUpdate();
			stmt.close();

			conn.commit();

			if (rowsAffected == 0)
			{
				Console.WriteLine("An error occurred and no products were affected by this update.", "red");
				return false;
			}

			return true;
		}
		catch (Exception e)
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
