import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Shipment
{
	private int ShipmentId;
	public Date ShipmentDate;
	public double UnitPrice;
	public int Quantity;
	public ArrayList<Product> Products = new ArrayList<>();

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

		out += " (" + this.ShipmentDate + ")";

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

	public boolean Create(Connection conn, int batchId, int supplierId)
	{
		try
		{
			if ((this.ShipmentId = GetNextId(conn)) == 0)
			{
				Console.WriteLine("There was an error while creating this shipment. Please try again.", "red");
				return false;
			}

			String query = "INSERT INTO shipment (shipmentId, shipmentDate, unitPrice, quantity) VALUES(?, ?, ?, ?)";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, this.GetShipmentId());
			stmt.setDate(2, this.ShipmentDate);
			stmt.setDouble(3, this.UnitPrice);
			stmt.setInt(4, this.Quantity);
			int rowsAffected = stmt.executeUpdate();
			stmt.close();

			if (rowsAffected == 0)
			{
				Console.WriteLine("An error occurred and this shipment was not created.");
				conn.rollback();
				return false;
			}

			stmt = conn.prepareStatement("INSERT INTO contains(shipmentId, batchId) VALUES(?, ?)");
			stmt.setInt(1, this.GetShipmentId());
			stmt.setInt(2, batchId);
			rowsAffected = stmt.executeUpdate();
			stmt.close();

			if (rowsAffected == 0)
			{
				Console.WriteLine("An error occurred and this shipment was not created.");
				conn.rollback();
				return false;
			}

			stmt = conn.prepareStatement("INSERT INTO ships(shipmentId, supplierId) VALUES(?, ?)");
			stmt.setInt(1, this.GetShipmentId());
			stmt.setInt(2, supplierId);
			rowsAffected = stmt.executeUpdate();
			stmt.close();

			if (rowsAffected == 0)
			{
				Console.WriteLine("An error occurred and this shipment was not created.");
				conn.rollback();
				return false;
			}

			conn.commit();
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

			Console.WriteLine("An error occurred while trying to create this shipment. Please try again.", "red");
			return false;
		}
	}

	private static int GetNextId(Connection conn)
	{
		try
		{
			PreparedStatement stmt = conn.prepareStatement("SELECT MAX(shipmentId) + 1 FROM shipment");
			ResultSet res = stmt.executeQuery();
			if (res.next())
			{
				return res.getInt(1);
			}
		}
		catch (Exception e)
		{
			return 0;
		}
		return 0;
	}
}
