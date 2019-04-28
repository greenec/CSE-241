import java.util.Scanner;

public class Console
{
	private enum Background { Light, Dark };

	private static Scanner sc = new Scanner(System.in);
	private static Background BackgroundColor = Background.Dark;

	public static void Initialize()
	{
		WriteLine("Please describe your terminal so that colored text can be displayed clearly.");
		while(true)
		{
			Write("Is your terminal background 'light' or 'dark'? ");
			String background = sc.nextLine().toLowerCase().trim();

			if (background.equals("light"))
			{
				BackgroundColor = Background.Light;
				WriteLine("Thanks! Output will be optimized for terminals with a 'light' background.", "green");
				return;
			}

			if (background.equals("dark"))
			{
				BackgroundColor = Background.Dark;
				WriteLine("Thanks! Output will be optimized for terminals with a 'dark' background.", "green");
				return;
			}
		}
	}

	public static int GetInt(String prompt, String color, int min, int max)
	{
		int id;

		while(true)
		{
			Console.Write(prompt, color);
			String sId = sc.nextLine().trim();

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
		// clear formatting after each message
		message += "\033[0m";

		if (BackgroundColor == Background.Dark)
		{
			switch (color) {
				case "red":
					System.out.print("\033[1;31m" + message);
					break;
				case "green":
					System.out.print("\033[1;32m" + message);
					break;
				case "yellow":
					System.out.print("\033[1;33m" + message);
					break;
				case "blue":
					System.out.print("\033[1;36m" + message);
					break;
				default:
					System.out.print("\033[0m" + message);
			}
		}
		else if (BackgroundColor == Background.Light)
		{
			switch (color) {
				case "red":
					System.out.print("\033[0;31m" + message);
					break;
				case "green":
					System.out.print("\033[1;32m" + message);
					break;
				case "yellow":
					System.out.print("\033[1;33m" + message);
					break;
				case "blue":
					System.out.print("\033[0;34m" + message);
					break;
				default:
					System.out.print("\033[0m" + message);
			}
		}
	}
}
