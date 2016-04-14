import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;



public class VenueHireSystem {
	
	//variables
	private ArrayList<Venue> venueList;
	private ArrayList<Request> requestList;
	
	
	
	//constructor
	public VenueHireSystem(){
		this.venueList = new ArrayList<Venue>();
		this.requestList = new ArrayList<Request>();
		
	}
	
	//functions
	/**
	 * handle input 
	 * @param inputLine
	 */
	public void handleInput(String inputLine){
		String[] splitInput = inputLine.split(" ");
		switch(splitInput[0]){
			case "Venue":	runVenue(splitInput);break;
			case "Request":	runRequest(splitInput);break;
			case "Cancel":	runCancel(splitInput);break;
			case "Change":	runChange(splitInput);break;
			case "Print":	runPrint(splitInput);break;
			
		
		
		}
		
		
	}
	/**
	 * handle input which add a Venue
	 * @param input
	 */
	public void runVenue(String[] input){
		//get a new room
		Room room = new Room(input[2], input[3],input[1]);
		//find venue
		boolean venueExist = false;
		for(Venue v:this.venueList){
			if(v.getName().equals(input[1])){
				v.addRoom(room);
				venueExist = true;
				break;
			}
		}
		if(venueExist == false){
			Venue venue = new Venue(input[1]);
			venue.addRoom(room);
			this.venueList.add(venue);
		}
		
		
	}
	
	/**]
	 * handle input which is a request
	 * @param input
	 */
	public void runRequest(String[] input){
		
		int ID = Integer.parseInt(input[1]);
		Calendar dateFrom = Calendar.getInstance();
		dateFrom.set(2016, nameToMonth(input[2]), Integer.parseInt(input[3]) );
		Calendar dateTo = Calendar.getInstance();
		dateTo.set(2016,nameToMonth(input[4]), Integer.parseInt(input[5]));
		
		ArrayList<Room> roomList = new ArrayList<Room>();
		int roomTypes[] = {0,0,0};
		
		
		boolean findRoom = false;
		
		
		for(Venue venue:this.venueList){
			//check room request
			int i = 6;
			while(i<input.length){
				int numberOfRoom = Integer.parseInt(input[i]);
				String roomType = input[i+1];
			
				switch(roomType){
				case "large":	roomTypes[2] = numberOfRoom;break;
				case "middle":	roomTypes[1] = numberOfRoom;break;
				case "small":	roomTypes[0] = numberOfRoom;break;
			
				}
				i+=2;
			}
			for(Room room:venue.getRoomList()){
				//if get a proper room
				//check roomtype
				String roomType = room.getType();
				int roomTypeIndex = 0;
				switch(roomType){
				case "large":	roomTypeIndex = 2;break;
				case "middle":	roomTypeIndex = 1;break;
				case "small":	roomTypeIndex = 0;break;
			
				}
				//if Roomtype doesn't fit
				if(roomTypes[roomTypeIndex] == 0){
					continue;
				}
				//check room status
				if(room.checkStat(dateFrom,dateTo)){
					roomList.add(room);
					roomTypes[roomTypeIndex] --;
				
				}
				
				
			}
			//check if find all the room
			if(roomTypes[0] == 0 && roomTypes[1] == 0 && roomTypes[2] == 0){
				findRoom = true;
				break;
			}
			//initialize params
			roomList.clear();
		}
		if(findRoom == true){
			//create the request
			Request r = new Request(ID, roomTypes, roomList, dateFrom, dateTo);
			this.requestList.add(r);
			String info = roomList.get(0).getVenue();
			for(Room room:roomList){
				info += " "+room.getName();
			}
			System.out.println("Reservation "+ID+" "+info);
			
		}
		if(findRoom == false) System.out.println("Request rejected");
		
		
	}
	
	/**
	 * handle input which is cancel
	 * @param input
	 */
	public void runCancel(String[] input){
		
		int ID = Integer.parseInt(input[1]);
		for(Request r:this.requestList){
			if(ID == r.getID()){
				r.removeDate();
				this.requestList.remove(r);
				break;
			}
			
		}
		System.out.println("Cancel " + ID);
		
		
	}
	
	/**
	 * handle input which is change
	 * @param input
	 */
	public void runChange(String[] input){
		int ID = Integer.parseInt(input[1]);
		for(Request request: this.requestList){
			if(ID == request.getID()){
				//remove relations
				request.removeDate();
				this.requestList.remove(request);
				
				//change request
				Calendar dateFrom = Calendar.getInstance();
				dateFrom.set(2016, nameToMonth(input[2]), Integer.parseInt(input[3]) );
				Calendar dateTo = Calendar.getInstance();
				dateTo.set(2016,nameToMonth(input[4]), Integer.parseInt(input[5]));
				
				ArrayList<Room> roomList = new ArrayList<Room>();
				int roomTypes[] = {0,0,0};
				
				
				boolean findRoom = false;

				
				for(Venue venue:this.venueList){
					//check room request
					int i = 6;
					while(i<input.length){
						int numberOfRoom = Integer.parseInt(input[i]);
						String roomType = input[i+1];
					
						switch(roomType){
						case "large":	roomTypes[2] = numberOfRoom;break;
						case "middle":	roomTypes[1] = numberOfRoom;break;
						case "small":	roomTypes[0] = numberOfRoom;break;
					
						}
						i+=2;
					}
					for(Room room:venue.getRoomList()){
						//if get a proper room
						//check roomtype
						String roomType = room.getType();
						int roomTypeIndex = 0;
						switch(roomType){
						case "large":	roomTypeIndex = 2;break;
						case "middle":	roomTypeIndex = 1;break;
						case "small":	roomTypeIndex = 0;break;
					
						}
						//if Roomtype doesn't fit
						if(roomTypes[roomTypeIndex] == 0){
							continue;
						}
						//check room status
						if(room.checkStat(dateFrom,dateTo)){
							roomList.add(room);
							roomTypes[roomTypeIndex] --;
						
						}
						
						
					}
					//check if find all the room
					if(roomTypes[0] == 0 && roomTypes[1] == 0 && roomTypes[2] == 0){
						findRoom = true;
						break;
					}
					//initialize params
					roomList.clear();
				}
				
				if(findRoom == true){
					//create the request
					Request r = new Request(ID, roomTypes, roomList, dateFrom, dateTo);
					this.requestList.add(r);
					String info = roomList.get(0).getVenue();
					for(Room room:roomList){
						info += " "+room.getName();
					}
					System.out.println("Change "+ID+" "+info);
					
					
				}
				//if didnt find ,,reject change and add relations back
				if(findRoom == false){
					request.addDate();
					this.requestList.add(request);
					System.out.println("Change rejected");
				}
				break;
			}
			
		}
		
	}
	/**
	 * handle input which is print
	 * @param input
	 */
	public void runPrint(String[] input){
		String name = input[1];
		for(Venue v: this.venueList){
			if(name.equals(v.getName())){
				v.printInfo();
				break;
				
			}
		}
		
	}
	
	/**
	 * transform month Name into num
	 * @param name
	 * @return
	 */
	public int nameToMonth(String name){
		//month to num
		switch(name){
		case "Jan":	return 1;
		case "Feb":	return 2;
		case "Mar":	return 3;
		case "Apr":	return 4;
		case "May": return 5;
		case "Jun":	return 6;
		case "Jul":	return 7;
		case "Aug": return 8;
		case "Sep": return 9;
		case "Oct": return 10;
		case "Nov": return 11;
		case "Dec": return 12;
		}
		return 0;
		
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = null;
	      try
	      {
	          sc = new Scanner(new FileReader(args[0]));    // args[0] is the first command line argument
	          VenueHireSystem VenueHireSystem = new VenueHireSystem();
	          while(sc.hasNextLine()){
	        		VenueHireSystem.handleInput(sc.nextLine());
	          }
	          
	      }
	      catch (FileNotFoundException e) {}
	      finally
	      {
	          if (sc != null) sc.close();
	      }
	}

}
