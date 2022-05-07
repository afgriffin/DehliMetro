//CSCM12J coursework
//name: Allie Griffin
//student id: 2148000

import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedList;
import java.io.*;


class Station
{	
	//class Station
	boolean visited = false;
	String _name;
	//Station previous = null;
	ArrayList<Line> _lines = new ArrayList<Line>();
	Station (String n) {_name = n;}
	//accessors
	public String name(){return _name;}
	public ArrayList<Line> lines(){return _lines;}
	public void setVisited() {visited = true;}
	public void setUnvisited() {visited = false;}
	public boolean hasBeenVisited() {return visited;}

	//Task 4 returns a list of all the stations that are linked with the current object (ie, just before or after in a given line) (hint: linked to line.index())
	public ArrayList<Station> neighbourStations() {
		ArrayList<Station> neighbours = new ArrayList<Station>();

		//iterate through the arraylist of lines associated with the station in question
		for(Line line : this._lines) {
			//get the index of the station in each line
			int index = line.index(this);
				//make sure the index stays within bounds and get the index of the stations one place to the left and one to the right in the line list
				//add both stations to the neighbors arraylist for this station
				if(index > 0)
						neighbours.add(line.stations.get(index - 1));
				if(index < line.stations.size() - 1)
						neighbours.add(line.stations.get(index + 1));
		}
		return neighbours;
	}

}

class Line
{
	//class Line
	String _name;
	ArrayList<Station> stations = new ArrayList<Station>();
	HashMap<Station,Integer> stationMap = new HashMap<Station,Integer>(); //needed to quickly access the index of the station in the ArrayList
	Line(String name){ this._name = new String(name);}
	//accessors
	public String name(){return _name;}
	public ArrayList<Station>  stations(){return stations;}
	public HashMap<Station, Integer> stationMap() {return stationMap;}
	//Task 2 returns Station s's index in the line - make use of hashmap stationMap
	public int index(Station s)
	{
		int index = stationMap.get(s);
		return index;
	}
	//Task 1 add a station to the class member stations and map a station to its index in stations using a hashmap (stationMap)
	public void addStation(Station s) 
	{
		//add the station to the arraylist
		stations.add(s);
		//get the index of the station
		int index = stations.size() - 1;
		//add the station and index to the hashmap
		stationMap.put(s, index);
	}	
}

class Metro 
{
	//class Metro
	ArrayList<Line> _lines = new ArrayList<Line>();
	ArrayList<Station> _stations = new ArrayList<Station>(); 
	
	//accessors
	ArrayList<Line> lines() {return _lines;}
	public ArrayList<Station> stations() {return _stations;}
	public HashMap<String,Station> stationMap = new HashMap<String,Station>(); //name, station - needed to quickly access the index of the station in the ArrayList
	public HashMap<String,Station> stationMap() {return stationMap;}
	
	public static void printStationList(String message, ArrayList<Station> slist)
	{
		System.out.println(message);
		for (Station s:slist)
			System.out.println("=>" + s.name());
		System.out.println("\t(" + slist.size() + " stations)\n");
	}

	public static void printStationListWithLines(String message, ArrayList<Station> slist)
	{
		System.out.println(message);
		for (Station s:slist)
		{
			System.out.print("=>" + s.name() + "\t\t");
			for (Line l:s.lines())
				System.out.print("\t*" + l.name());
			System.out.println();
		}
		System.out.println("\t(" + slist.size() + " stations)\n");
	}

	public Station getStation(String name)
	{
		return stationMap.get(name);
	}
	
	//Task 3 this function getAllConnectionHubs is to be completed. It shall return the list of all the stations that are a hub.
	//returns a list of all the stations in the metro that have connections between lines
	public ArrayList<Station>  getAllConnectionHubs() 
	{
		ArrayList<Station> connectionHubs = new ArrayList<Station>(); 

		//iterate through the arraylist of stations in this metro
		//if the list of lines associated with the station is larger than 1, add the station to the list of connection hubs
		for(Station station : this._stations) {
			if (station.lines().size() > 1) {
				connectionHubs.add(station);
			}
		}
		return connectionHubs;

	}
	//Task 5 returns a shortest path between two stations a and b (measured as the number of hops between stations)
	//result should be the list of stations on the path starting from a and finishing at b
	public ArrayList<Station>  shortestPath(String a, String b)
	{
		Station origin = getStation(a);
		Station destination = getStation(b);
		ArrayList<Station> shortestPath = new ArrayList<>();
		LinkedList<Station> q = new LinkedList<Station>();
		HashMap<Station, Station> previousStation = new HashMap<>();

		//set all stations to unvisited by default
		for(Station s : this._stations) {
			s.setUnvisited();
		}

		//set source station as visited and add it to the beginning of the queue
		origin.setVisited();
		q.add(origin);

		//check if the queue is empty, get the first station and remove it from the queue
		while(!q.isEmpty()) {
			Station v = q.get(0);
			q.remove(0);

			//iterate through the neighboring stations of the first station in the queue
			for(Station w : v.neighbourStations()) {
				//check if they've already been visited, mark them as visited, add them to the queue
				//add them to the hashmap with the previous station to keep track of the path
				if(!w.hasBeenVisited()) {
					w.setVisited();
					q.add(w);
					previousStation.put(w, v);
					//stop when you reach the destination station, iterate through the hashmap to get the path and clear the queue to stop the loop
					if(w.equals(destination)) {
						Station route = destination;
						while(route != null) {
							shortestPath.add(route);
							route = previousStation.get(route);
						}
						q.clear();
					}
				}
			}
		}

		//reverse the path so it goes from beginning to end
		for(int i = 0; i < shortestPath.size(); i++) {
			shortestPath.add(i, shortestPath.get(shortestPath.size() - 1));
			shortestPath.remove(shortestPath.size() - 1);
		}
		return shortestPath;
	}
	//Task 6 returns all the possible paths between two stations a and b
	//result should be a list of paths, always starting from a and finishing at b
	//paths should not have a cycle in them
	//algorithm implemented should be efficient and time-effective
	public ArrayList<ArrayList<Station>>  allPaths(String a, String b)
	{
		
		Station origin = getStation(a);
		Station destination = getStation(b);
		ArrayList<ArrayList<Station>> allPaths = new ArrayList<>();
		LinkedList<Station> q = new LinkedList<>();
		ArrayList<Station> path = new ArrayList<>();
		HashMap<Station, Station> previousStation = new HashMap<>();
		/**
		//set all stations to unvisited by default
		for(Station s : this._stations) {
			s.setUnvisited();
		}

		origin.setVisited();
		path.add(origin);
		q.add(origin);

		while(!q.isEmpty()) {
			Station v = q.get(0);
			q.remove(0);
			v.setVisited();

			for(Station w : v.neighbourStations()) {
				if(!w.hasBeenVisited()) {
					q.add(w);
					w.setVisited();
					previousStation.put(w, v);
					if(w.equals(destination)) {
						Station route = destination;
						while (route != null) {
							path.add(route);
							route = previousStation.get(route);
						}
						allPaths.add(path);
						w.setUnvisited();
						path.remove(path.size() - 1);
						q.clear();
					}
				}
				v.setUnvisited();
			}
		}
		**/
		return allPaths;
		
	}
	
	void readLines(String txt)
	{
		//reading the metro lines
		try (BufferedReader br = new BufferedReader(new FileReader(txt))) {
			String line;
			Line currentLine = null;
			while ((line = br.readLine()) != null) 
			{
				if (line.length() > 0)
					if (line.contains(":")) 
					{   //We have a new line
						currentLine = new Line(line.replace(":",""));
						lines().add(currentLine);
					}
					else
					{
						//we have a station
						Station s = stationMap.get(line);
						if (s == null)
						{
							//station is new
							s = new Station(line);
							_stations.add(s);
							stationMap.put(line,s); //Adding a station into the hashmap so that it can be retrieved quickly.
						}
						s.lines().add(currentLine);
						currentLine.addStation(s);
					}
			   // process the line.
			}
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}

public class Main
{
	static Metro delhi = new Metro();

	public static void main(String[] args)
	{
		delhi.readLines("lines.txt");
		System.out.println("number of stations:" + delhi.stations().size());
		Metro.printStationListWithLines("\nList of hubs:\n",delhi.getAllConnectionHubs());
		if (args.length == 2)
		{
			Metro.printStationList("\nShortest Path Stations: " + args[0] + " => " + args[1] + "\n",delhi.shortestPath(args[0],args[1]));
			long startTime = System.currentTimeMillis();
			ArrayList<ArrayList<Station>> res = delhi.allPaths(args[0], args[1]);
			long endTime = System.currentTimeMillis();
			System.out.println("\n Number of different paths found between " + args[0] + " and " + args[1] + ":" + res.size() + "\n");
			System.out.println("Calculation took " + (endTime - startTime) + " ms!");
		}
	}
}















