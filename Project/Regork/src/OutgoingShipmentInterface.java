import java.sql.Connection;
import java.util.ArrayList;

public class OutgoingShipmentInterface
{
	public static void Run(Connection conn)
	{

		while (true)
		{
			Console.Clear();

			Console.WriteLine();
			Console.WriteLine("Welcome, supplier relations manager!", "green");
			Console.WriteLine();

			Console.WriteLine("* * * * * * * * * * * * * * * * * * * * *");
			Console.WriteLine("*   OUTGOING SHIPMENT MANAGEMENT MENU   *");
			Console.WriteLine("* * * * * * * * * * * * * * * * * * * * *");

			Console.WriteLine();

			Console.WriteLine("Please select an action from the list below:");
			Console.WriteLine("\t1) View Outgoing Shipments");
			Console.WriteLine("\t2) Create Outgoing Shipment", "yellow");
			Console.WriteLine();
			int action = Console.GetInt("Please enter a number between 1 and 2, or enter 0 to exit: ", "blue", 0, 2);

			switch (action)
			{
				case 0:
					return;
				case 1:
					Supplier supplier = SupplierSearch(conn);
					ViewOutgoingShipments(conn, supplier);
					break;
				case 2:
					Console.WriteLine("This interface has not been implemented yet!", "yellow");
					break;
				default:
					Console.WriteLine("An unexpected error occurred. Returning to supplier relation manager's menu.", "red");
					break;
			}

			Console.Wait();
		}
	}

	private static Supplier SupplierSearch(Connection conn)
	{
		Console.WriteLine("Please search for your company.");

		// loop until there are successful search results
		while (true)
		{
			String search = Console.GetString("Enter supplier name: ", "blue");

			// search for suppliers and loop if no results found
			ArrayList<Supplier> suppliers = Supplier.FindByName(conn, search);
			if (suppliers.isEmpty())
			{
				Console.WriteLine("No results found. Please try again.", "yellow");
				continue;
			}

			// print out the instructors and break out of the loop
			Console.WriteLine("Here is a list of all matching suppliers:");
			for (Supplier supplier : suppliers)
			{
				Console.WriteLine("\t" + supplier.toString(true));
			}

			Console.WriteLine("Please enter your supplier ID.", "blue");
			int id = Console.GetInt("Please enter an integer between 0 and 9999: ", "blue", 0, 9999);

			// attempt to retrieve the supplier with the given ID
			Supplier supplier = Supplier.GetById(conn, id);
			if (supplier == null)
			{
				Console.WriteLine("There are no suppliers found with that ID.", "yellow");
				continue;
			}

			return supplier;
		}
	}

	private static void ViewOutgoingShipments(Connection conn, Supplier supplier)
	{
		ArrayList<Shipment> shipments = supplier.GetShipments(conn);

		if (shipments == null)
		{
			return;
		}

		if(shipments.size() == 0)
		{
			Console.WriteLine("There are no shipments for this supplier.", "yellow");
			return;
		}

		Console.WriteLine("Here is a list of shipments for Supplier " + supplier.toString());
		for (Shipment shipment : shipments)
		{
			Console.WriteLine("\tShipment " + shipment.toString(true));
			for (Product product : shipment.GetProducts())
			{
				Console.WriteLine("\t\tProduct " + product.toString());
			}
		}
	}
}
