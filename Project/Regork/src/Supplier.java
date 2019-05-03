import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

public class Supplier
{
	private int SupplierId;
	private String Name = "";
	private String StreetName = "";
	private int StreetNumber = 0;
	private String City = "";
	private String State = "";
	private int ZipCode = 0;
	private ArrayList<String> PhoneNumbers = new ArrayList<>();

	public Supplier(int supplierId)
	{
		this.SupplierId = supplierId;
	}

	public Supplier(int supplierId, String name, String streetName, int streetNumber, String city, String state, int zipCode)
	{
		this.SupplierId = supplierId;
		SetName(name);
		SetAddress(streetName, streetNumber, city, state, zipCode);
	}

	public int GetSupplierId()
	{
		return this.SupplierId;
	}

	public String GetName()
	{
		return this.Name;
	}

	public String GetFormattedAddress()
	{
		return this.StreetNumber + " " + this.StreetName + "\n" + this.City + ", " + this.State + " " + this.ZipCode;
	}

	public ArrayList<String> GetPhoneNumbers()
	{
		return this.PhoneNumbers;
	}

	public ArrayList<Shipment> GetShipments(Connection conn)
	{
		ArrayList<Shipment> shipments = new ArrayList<>();

		try
		{
			String query =
				"SELECT s.shipmentId, s.shipmentDate, s.unitPrice, s.quantity " +
					"FROM shipment s " +
					"INNER JOIN ships sh ON s.shipmentId = sh.shipmentId " +
					"WHERE sh.supplierId = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, this.GetSupplierId());
			ResultSet res = stmt.executeQuery();

			while (res.next())
			{
				int shipmentId = res.getInt("SHIPMENTID");
				Date shipmentDate = res.getDate("SHIPMENTDATE");
				double unitPrice = res.getDouble("UNITPRICE");
				int quantity = res.getInt("QUANTITY");

				Shipment shipment = new Shipment(shipmentId, shipmentDate, unitPrice, quantity);
				shipment.LoadProducts(conn);
				shipments.add(shipment);
			}

			stmt.close();
		}
		catch (Exception e)
		{
			Console.WriteLine("An error occurred while trying to retrieve shipments. Please try again.", "red");
			return null;
		}

		return shipments;
	}

	public void SetName(String name)
	{
		this.Name = name;
	}

	public void SetAddress(String streetName, int streetNumber, String city, String state, int zipCode)
	{
		this.StreetName = streetName;
		this.StreetNumber = streetNumber;
		this.City = city;
		this.State = state;
		this.ZipCode = zipCode;
	}

	public void AddPhoneNumber(String phoneNumber)
	{
		this.PhoneNumbers.add(phoneNumber);
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
			out += String.format("%-4s", this.GetSupplierId());
		}
		else
		{
			out += this.GetSupplierId();
		}

		out += " " + this.GetName();

		return out;
	}

	public boolean Refresh(Connection conn)
	{
		boolean bSuccess = false;

		try
		{
			String query = "SELECT supplierName, streetName, streetNumber, city, state, zipCode FROM supplier WHERE supplierId = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, this.GetSupplierId());
			ResultSet res = stmt.executeQuery();

			if (res.next())
			{
				String name = res.getString("SUPPLIERNAME");
				String streetName = res.getString("STREETNAME");
				int streetNumber = res.getInt("STREETNUMBER");
				String city = res.getString("CITY");
				String state = res.getString("STATE");
				int zipCode = res.getInt("ZIPCODE");

				this.SetName(name);
				this.SetAddress(streetName, streetNumber, city, state, zipCode);

				// success of a refresh is now contingent on phone numbers being refreshed properly
				bSuccess = this.LoadPhoneNumbers(conn);
			}

			stmt.close();
		}
		catch (Exception e)
		{
			bSuccess = false;
			Console.WriteLine("An error occurred while trying to refresh the supplier. Please try again", "red");
		}

		return bSuccess;
	}

	private boolean LoadPhoneNumbers(Connection conn)
	{
		try
		{
			PreparedStatement stmt = conn.prepareStatement("SELECT phoneNumber FROM supplierPhone WHERE supplierId = ?");
			stmt.setInt(1, this.GetSupplierId());
			ResultSet res = stmt.executeQuery();

			ArrayList<String> phoneNumbers = new ArrayList<>();

			while(res.next())
			{
				phoneNumbers.add(res.getString("PHONENUMBER"));
			}

			this.PhoneNumbers = phoneNumbers;

			return true;
		}
		catch (Exception e)
		{
			Console.WriteLine("An error occurred while trying to refresh the supplier's phone numbers. Please try again.", "red");
			return false;
		}
	}

	public static Supplier GetById(Connection conn, int supplierId)
	{
		Supplier supplier = new Supplier(supplierId);

		boolean success = supplier.Refresh(conn);
		if (success)
		{
			return supplier;
		}
		else
		{
			return null;
		}
	}

	public static ArrayList<Supplier> FindByName(Connection conn, String search)
	{
		ArrayList<Supplier> suppliers = new ArrayList<>();

		try
		{
			String query =
				"SELECT supplierId, supplierName, streetName, streetNumber, city, state, zipCode " +
					"FROM supplier " +
					"WHERE INSTR(supplierName COLLATE BINARY_CI, ?) <> 0 " +
					"ORDER BY supplierId";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, search);
			ResultSet res = stmt.executeQuery();

			while (res.next())
			{
				int id = res.getInt("SUPPLIERID");
				String name = res.getString("SUPPLIERNAME");
				String streetName = res.getString("STREETNAME");
				int streetNumber = res.getInt("STREETNUMBER");
				String city = res.getString("CITY");
				String state = res.getString("STATE");
				int zipCode = res.getInt("ZIPCODE");

				Supplier supplier = new Supplier(id, name, streetName, streetNumber, city, state, zipCode);
				suppliers.add(supplier);
			}

			stmt.close();
		}
		catch (Exception e)
		{
			Console.WriteLine("An error occurred while searching for suppliers. Please try again.", "red");
		}

		return suppliers;
	}

	public boolean Save(Connection conn)
	{
		try
		{
			String query = "UPDATE supplier SET supplierName = ?, streetName = ?, streetNumber = ?, city = ?, state = ?, zipCode = ? WHERE supplierId = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, this.GetName());
			stmt.setString(2, this.StreetName);
			stmt.setInt(3, this.StreetNumber);
			stmt.setString(4, this.City);
			stmt.setString(5, this.State);
			stmt.setInt(6, this.ZipCode);
			stmt.setInt(7, this.GetSupplierId());

			int rowsAffected = stmt.executeUpdate();
			stmt.close();

			if (!this.SavePhoneNumbers(conn))
			{
				conn.rollback();
				return false;
			}

			conn.commit();

			if (rowsAffected == 0)
			{
				Console.WriteLine("An error occurred and no suppliers were affected by this update.", "red");
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

			Console.WriteLine("An error occurred while trying to save changes made to this supplier.", "red");
			Console.WriteLine("The supplier has not been modified. Please try again.", "red");
			return false;
		}
	}

	private boolean SavePhoneNumbers(Connection conn)
	{
		try
		{
			PreparedStatement stmt = conn.prepareStatement("DELETE FROM supplierPhone WHERE supplierId = ?");
			stmt.setInt(1, this.GetSupplierId());
			stmt.executeUpdate();
			stmt.close();

			stmt = conn.prepareStatement("INSERT INTO supplierPhone (supplierId, phoneNumber) VALUES (?, ?)");
			stmt.setInt(1, this.GetSupplierId());

			for(String phoneNumber : this.PhoneNumbers)
			{
				stmt.setString(2, phoneNumber);
				stmt.executeUpdate();
			}

			conn.commit();

			return true;
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

			Console.WriteLine("An error occurred while trying to save this supplier's phone numbers.", "red");
			Console.WriteLine("The supplier's phone numbers have not been modified. Please try again.", "red");
			return false;
		}
	}
}
