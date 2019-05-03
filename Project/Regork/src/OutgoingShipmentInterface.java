import java.sql.Connection;
import java.sql.Date;
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
			Console.WriteLine("\t2) Create Outgoing Shipment");
			Console.WriteLine();
			int action = Console.GetInt("Please enter a number between 1 and 2, or enter 0 to exit: ", "blue", 0, 2);

			Supplier supplier;
			switch (action)
			{
				case 0:
					return;
				case 1:
					supplier = SupplierSearch(conn);
					ViewOutgoingShipments(conn, supplier);
					break;
				case 2:
					supplier = SupplierSearch(conn);
					CreateShipment(conn, supplier);
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
			for (Product product : shipment.Products)
			{
				Console.WriteLine("\t\tProduct " + product.toString());
			}
		}
	}

	private static void CreateShipment(Connection conn, Supplier supplier)
	{
		ArrayList<Batch> batches = supplier.GetBatches(conn);

		if (batches == null)
		{
			Console.WriteLine("Returning to outgoing shipment menu.", "yellow");
			return;
		}

		if (batches.isEmpty())
		{
			Console.WriteLine(supplier.Name + " hasn't manufactured any batches, and therefore cannot ship any product.");
			Console.WriteLine("Returning to outgoing shipment menu.", "yellow");
			return;
		}

		Console.WriteLine("Here is a list of batches manufactured by " + supplier.Name + ": ");
		ArrayList<Integer> batchIds = new ArrayList<>();

		for (Batch batch : batches)
		{
			batchIds.add(batch.GetBatchId());
			Console.WriteLine("\tBatch " + batch.toString());
			Console.WriteLine("\t\tContains Product " + batch.Product.toString());
		}

		int batchId = Console.GetInt("Please enter the batch ID that you are shipping: ", "blue", 0, 9999);
		if (!batchIds.contains(batchId))
		{
			Console.WriteLine(supplier.Name + " didn't manufacture Batch #" + batchId + " and therefore cannot ship it.", "yellow");
			Console.WriteLine("Returning to outgoing shipment menu.", "yellow");
			return;
		}

		Date shipmentDate = Console.GetDate("Please enter the shipment date in the format mm-dd-yyyy: ", "blue");
		double unitPrice = Console.GetDouble("Please enter the unit price of the shipment: ", "blue", 0.0001, 999999.9999);
		int quantity = Console.GetInt("Please enter the quantity of the shipment: ", "blue", 0, 99999999);

		Shipment shipment = new Shipment(0, shipmentDate, unitPrice, quantity);

		boolean bCreated = shipment.Create(conn, batchId, supplier.GetSupplierId());

		if (bCreated)
		{
			Console.WriteLine("Shipment successfully created! Status is Shipment " + shipment.toString(), "green");
		}
	}
}
