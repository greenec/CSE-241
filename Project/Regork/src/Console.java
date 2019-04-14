import java.util.Scanner;

public class Console
{
	private static Scanner sc = new Scanner(System.in);

	public static int GetInt(String prompt, int min, int max)
	{
		int id;

		while(true)
		{
			System.out.print(prompt);
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
}
