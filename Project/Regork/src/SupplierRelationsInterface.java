import java.sql.Connection;
import java.util.ArrayList;

public class SupplierRelationsInterface
{
	public static void Run(Connection conn)
	{
		Console.WriteLine("Welcome, supplier relations manager!", "green");

		while (true)
		{
			Console.WriteLine("* * * * * * * * * * * * * * * * * * * * *");
			Console.WriteLine("*   SUPPLIER RELATION MANAGEMENT MENU   *");
			Console.WriteLine("* * * * * * * * * * * * * * * * * * * * *");

			Console.WriteLine("Please select an action from the list below:");
			Console.WriteLine("\t1) Search Suppliers");
			Console.WriteLine("\t2) Add a New Supplier");

			int action = Console.GetInt("Please enter a number between 1 and 2, or enter 0 to exit: ", "blue", 0, 2);

			switch (action)
			{
				case 0:
					return;
				case 1:
					SupplierSearch(conn);
					break;
				case 2:
					Console.WriteLine("This interface has not been implemented yet!", "yellow");
					break;
				default:
					Console.WriteLine("An unexpected error occurred. Returning to supplier relation manager's menu.", "red");
					break;
			}
		}
	}

	private static void SupplierSearch(Connection conn)
	{
		Console.WriteLine("Welcome to the supplier search!");

		// loop until there are successful search results
		while (true)
		{
			String search = Console.GetString("Please enter a supplier name for the search: ", "blue");

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
				Console.WriteLine("    #" + String.format("%-4s", supplier.GetSupplierId()) + " " + supplier.GetName());
			}
			break;
		}

		Console.WriteLine("Enter the ID for the supplier you seek.", "blue");
		int id = Console.GetInt("Please enter an integer between 0 and 9999: ", "blue", 0, 9999);

		// attempt to retrieve the product with the given ID
		Supplier supplier = Supplier.GetById(conn, id);
		if (supplier == null)
		{
			Console.WriteLine("There are no products found with that ID.", "yellow");
			return;
		}

		while(true)
		{
			Console.WriteLine("Please select an action from the list below:");
			Console.WriteLine("\t1) View Supplier's Address");
			Console.WriteLine("\t2) List Supplier's Phone Numbers");
			Console.WriteLine("\t3) Add Phone Number for Supplier");

			int action = Console.GetInt("Please enter a number between 1 and 2, or enter 0 to exit: ", "blue", 0, 3);

			switch (action)
			{
				case 0:
					return;
				case 1:
					Console.WriteLine(supplier.GetFormattedAddress(), "green");
					break;
				case 2:
					ListSupplierPhoneNumbers(supplier);
					break;
				case 3:
					AddPhoneNumber(conn, supplier);
					break;
				default:
					Console.WriteLine("An unexpected error occurred. Returning to supplier relation manager's menu.", "red");
					return;
			}
		}
	}

	private static void ListSupplierPhoneNumbers(Supplier supplier)
	{
		if (supplier.GetPhoneNumbers().size() == 0)
		{
			Console.WriteLine("This supplier has no phone numbers in the database!", "yellow");
		}

		for (String phoneNumber : supplier.GetPhoneNumbers())
		{
			Console.WriteLine(phoneNumber, "green");
		}
	}

	private static void AddPhoneNumber(Connection conn, Supplier supplier)
	{
		String phoneNumber = Console.GetString("Please enter another phone number for this supplier: ", "blue");
		supplier.AddPhoneNumber(phoneNumber);

		if(supplier.Save(conn))
		{
			Console.WriteLine("Phone number '" + phoneNumber + "' added successfully!", "green");
		}
		else
		{
			supplier.Refresh(conn);
		}
	}
}
