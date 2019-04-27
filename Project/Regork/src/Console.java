import java.util.Scanner;

public class Console
{
	private static Scanner sc = new Scanner(System.in);

	public static int GetInt(String prompt, String color, int min, int max)
	{
		int id;

		while(true)
		{
			Console.Write(prompt, color);
			String sId = sc.nextLine();

			// make sure that the ID is a valid integer
			try
			{
				id = Integer.parseInt(sId);
			}
			catch (NumberFormatException e)
			{
				continue;
			}

			// make sure that the ID is in range
			if(id >= min && id <= max)
			{
				break;
			}
		}

		return id;
	}

	public static void WriteLine()
	{
		Write("\n", "");
	}

	public static void WriteLine(String message)
	{
		Write(message + "\n", "");
	}

	public static void WriteLine(String message, String color)
	{
		Write(message + "\n", color);
	}

	public static void Write(String message)
	{
		Write(message, "");
	}


	public static void Write(String message, String color)
	{
		switch(color)
		{
			case "red":
				System.out.print("\033[0;31m" + message);
				break;
			case "green":
				System.out.print("\033[0;32m" + message);
				break;
			case "cyan":
				System.out.print("\033[0;36m" + message);
				break;
			case "yellow":
				System.out.print("\033[0;33m" + message);
				break;
			default:
				System.out.print("\033[0m" + message);
		}
	}
}
