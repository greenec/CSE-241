import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

public class Shipment
{
	private int ShipmentId;
	private Date ShipmentDate;
	private double UnitPrice;
	private int Quantity;
	private ArrayList<Product> Products = new ArrayList<>();

	public Shipment(int shipmentId, Date shipmentDate, double unitPrice, int quantity)
	{
		this.ShipmentId = shipmentId;
		this.ShipmentDate = shipmentDate;
		this.UnitPrice = unitPrice;
		this.Quantity = quantity;
	}

	public int GetShipmentId()
	{
		return this.ShipmentId;
	}

	public Date GetShipmentDate()
	{
		return this.ShipmentDate;
	}

	public ArrayList<Product> GetProducts()
	{
		return this.Products;
	}

	public String toString()
	{
		return toString(false);
	}

	public String toString(boolean formatId)
	{
		String out = "#";

		if (formatId)
		{
			out += String.format("%-4s", this.GetShipmentId());
		}
		else
		{
			out += this.GetShipmentId();
		}

		out += " " + this.GetShipmentDate();

		return out;
	}

	public boolean LoadProducts(Connection conn)
	{
		ArrayList<Product> products = new ArrayList<>();

		try
		{
			String query =
				"SELECT p.productLineId, p.productName, p.price " +
					"FROM productLine p " +
					"INNER JOIN madeFrom m ON p.productLineId = m.productLineId " +
					"INNER JOIN contains c ON m.batchId = c.batchId " +
					"WHERE c.shipmentId = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, this.GetShipmentId());
			ResultSet res = stmt.executeQuery();

			while (res.next())
			{
				int productId = res.getInt("PRODUCTLINEID");
				String productName = res.getString("PRODUCTNAME");
				double price = res.getDouble("PRICE");

				Product product = new Product(productId, productName, price);
				products.add(product);
			}

			this.Products = products;
			return true;
		}
		catch (Exception e)
		{
			Console.WriteLine("An error occurred while loading products for this shipment. Please try again.", "red");
			return false;
		}
	}
}
