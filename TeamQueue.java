import java.io.*;

/**
 * Represents a person in the queue.
 * int data is the name of the person
 * Node next is the person in front of this person in the queue (null if first in queue)
 */
class Node
{
	int data;
	Node next;
	
	public Node(int data)
	{
		this.data = data;
		this.next = null;
	}
}

/**
 * Main class
 * Node head is the person at the very front of the team queue
 * Node tail is the person at the very end of the team queue
 * int[][] teams is an array of teams
 */
public class TeamQueue
{
	private Node head;
	private Node tail;
	private int[][] teams;
	
	public TeamQueue(int[][] teams)
	{
		head = null;
		tail = null;
		this.teams = teams;
	}
	
	/**
	 * ENQUEUEs a person into the queue.
	 */
	public void enqueue(int data)
	{
		// If no one is queued, that means data is the first element.
		// data will be the head (front) and tail (end) node.
		if(head == null)
		{
			head = tail = new Node(data);
		}
		// If someone else is already queued,
		else
		{
			// Find the team where the data belongs.
			int[] team = findTeam(data);
			boolean found = false;
			// Loop through the queue from head to tail to see if someone is a team member of data.
			for(Node current = head; current != tail; current = current.next)
			{
				// If current person is a team member
				if(teamMember(current.data, team))
				{
					found = true;
					
					// Loop until the person behind the next person is NOT a team member
					for(;current != tail; current = current.next)
					{
						if(!teamMember(current.next.data, team))
							break;
					}
					
					// Perform singit now na in front of the NOT team mate
					Node singit = new Node(data);
					singit.next = current.next;
					current.next = singit;
					
					break;
				}
			}
			
			// If no team member was found, the data becomes the last element of the queue
			if(!found)
			{
				tail.next = new Node(data);
				tail = tail.next;
			}
		}
	}
	
	/**
	 * Dequeues the person in the front and
	 * makes the next person the new front
	 */
	public int dequeue()
	{
		int data = head.data;
		head = head.next;
		return data;
	}
	
	/**
	 * Find the team of data.
	 * @param	data	the value of the person to find which team it belongs to
	 * @return			the team or null if the data doesn't belong to any
	 */
	private int[] findTeam(int data)
	{
		for(int[] team : teams)
		{
			for(int i = 0; i < team.length; i++)
			{
				if(team[i] == data)
					return team;
			}
		}
		return null;
	}
	
	/**
	 * Checks if data is a member of the team.
	 * @param	data	the value of the person to check if it belongs to 
	 * @param	team	the team to check whether the person belongs to
	 * @return			true if data is a member; false if not.
	 */
	private boolean teamMember(int data, int[] team)
	{
		for(int i = 0; i < team.length; i++)
			if(data == team[i])
				return true;
		return false;
	}
	
	/**
	 * BufferedReader reader for reading files.
	 */
	static BufferedReader reader;
	
	/**
	 * Main function
	 */
	public static void main(String[] args)
	{
		if(args.length == 0)
		{
			System.out.println("Incorrect input.\nFormat:\t\tjava TeamQueue <input file>\nExample:\tjava TeamQueue input.txt");
			return;
		}
		
		try
		{
			reader = new BufferedReader(new FileReader(new File(args[0])));
			int t;
			
			// The input will be terminated by a value of 0 for t.
			while((t = Integer.parseInt(reader.readLine())) != 0)
			{
				// 1 <= t <= 1000
				checkIfWithinRange(t, 1, 1000);
				
				// There are t number of teams
				int[][] teams = new int[t][];
				for(int i = 0; i < t; i++)
				{
					
					String[] teamData = reader.readLine().split(" ");
					teams[i] = new int[Integer.parseInt(teamData[0])];
					
					// A team may consist of up to 1000 elements
					checkIfWithinRange(teams[i].length, 0, 1000);
					for(int j = 0; j < teams[i].length; j++)
					{
						teams[i][j] = Integer.parseInt(teamData[j+1]);
						// Elements are integers in the range 0 - 999999
						checkIfWithinRange(teams[i][j], 0, 999999);
					}
				}
				
				// Initialize the team queue
				TeamQueue teamQueue = new TeamQueue(teams);
				String[] command;
				int commandsIssued = 0;
				
				// Scenario k
				int k = 1;
				System.out.println(String.format("Scenario #%d:", k));
				
				// while command is not equal to STOP
				while(!(command = reader.readLine().split(" "))[0].equals("STOP"))
				{
					// A test case may only contain up to 200000 commands.
					if(commandsIssued > 200000)
						throw new Exception();
					
					// If ENQUEUE, enter element x int team queue
					if(command[0].equals("ENQUEUE"))
					{
						teamQueue.enqueue(Integer.parseInt(command[1]));
					}
					// If DEQUEUE, process the first element and remove it from queue
					else if(command[0].equals("DEQUEUE"))
					{
						System.out.println(teamQueue.dequeue());
					}
					// If none of the above, throw an error exception
					else
					{
						throw new Exception();
					}
					commandsIssued++;
					k++;
				}
				System.out.println();
			}
		}
		catch(FileNotFoundException error)
		{
			System.out.println("File \"" + args[0] + "\" not found.");
		}
		catch(Exception error)
		{
			System.out.println("Incorrect input." + error.getMessage());
		}
	}
	
	/**
	 * Throws an error exception if input is not within the range of min and max
	 */
	public static void checkIfWithinRange(int x, int min, int max) throws Exception
	{
		if(x < min || x > max) throw new Exception();
	}
}