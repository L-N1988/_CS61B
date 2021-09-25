public class HelloNumbers 
{
	public static void main(String[] args)
	{
		int i = 0;
		while (i < 10)
		{
			int j = 0;
			int sum = 0;
			for (j = 0; j <= i; j++)
			{
				sum += j;
			}
			System.out.print(sum + " ");
			i += 1;
		}
		System.out.println("");
	}
}
