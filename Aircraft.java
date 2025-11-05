package assignment;

import java.util.*;


class Aircraft {
private String model; private int totalSeats;

public Aircraft(String model, int totalSeats) { this.model = model;
this.totalSeats = totalSeats;
}


public String getModel() { return model;
}


public int getTotalSeats() { return totalSeats;
}
}


class Seat {
private int seatNumber; private boolean booked;

public Seat(int seatNumber) { this.seatNumber = seatNumber; this.booked = false;
}


public int getSeatNumber() {
 
return seatNumber;
}


public boolean isBooked() { return booked;
}


public void setBooked(boolean booked) { this.booked = booked;
}
}


class Flight {
private int flightNumber; private String source; private String destination; private Aircraft aircraft;
private ArrayList<Seat> seats;


public Flight(int flightNumber, String source, String destination, Aircraft aircraft) { this.flightNumber = flightNumber;
this.source = source; this.destination = destination; this.aircraft = aircraft; this.seats = new ArrayList<>();
for (int i = 1; i <= aircraft.getTotalSeats(); i++) { seats.add(new Seat(i));
}
}


public int getFlightNumber() { return flightNumber;
 
}


public String getSource() { return source;
}


public String getDestination() { return destination;
}


public ArrayList<Seat> getSeats() { return seats;
}


public Seat getAvailableSeat() { for (Seat s : seats) {
if (!s.isBooked()) return s;
}
return null;
}


@Override
public String toString() {
return "Flight " + flightNumber + " (" + source + " -> " + destination + ") Aircraft: " + aircraft.getModel() +
" Seats Available: " + getAvailableSeatsCount();
}


public int getAvailableSeatsCount() { int count = 0;
for (Seat s : seats) {
if (!s.isBooked()) count++;
 
}
return count;
}
}


class Passenger { private int id; private String name; private int age;

public Passenger(int id, String name, int age) { this.id = id;
this.name = name; this.age = age;
}


public int getId() { return id; }
public String getName() { return name; } public int getAge() { return age; }

@Override
public String toString() {
return "Passenger " + id + ": " + name + ", Age: " + age;
}
}


class Payment {
private double amount; private boolean confirmed;

public Payment(double amount) { this.amount = amount;
 
this.confirmed = false;
}


public void confirmPayment() { this.confirmed = true;
}


public boolean isConfirmed() { return confirmed;
}


public double getAmount() { return amount;
}
}


class Booking {
private Passenger passenger; private Flight flight;
private Seat seat;
private Payment payment;
private BoardingPass boardingPass;


public Booking(Passenger passenger, Flight flight, Seat seat, Payment payment) { this.passenger = passenger;
this.flight = flight; this.seat = seat; this.payment = payment; this.boardingPass = null;
}


public Passenger getPassenger() { return passenger; }
 
public Flight getFlight() { return flight; } public Seat getSeat() { return seat; }
public Payment getPayment() { return payment; }
public BoardingPass getBoardingPass() { return boardingPass; }


public void confirmBooking() { if (!seat.isBooked()) {
seat.setBooked(true);
}
}


public void cancelBooking() { seat.setBooked(false);
}


public void issueBoardingPass() {
if (payment.isConfirmed() && seat.isBooked()) { this.boardingPass = new BoardingPass(this);
}
}


@Override
public String toString() {
return passenger.getName() + " booked Flight " + flight.getFlightNumber() + " Seat " + seat.getSeatNumber() + " | Payment: " + (payment.isConfirmed() ? "Confirmed" : "Pending");
}
}


class BoardingPass { private Booking booking; private Date timestamp;
 

public BoardingPass(Booking booking) {
this.booking = booking; this.timestamp = new Date();
}


@Override
public String toString() {
return "Boarding Pass:\nPassenger: " + booking.getPassenger().getName() + "\nFlight: " + booking.getFlight().getFlightNumber() +
"\nSeat: " + booking.getSeat().getSeatNumber() + "\nIssued at: " + timestamp;
}
}


public class AirlineSystem {
private ArrayList<Flight> flights = new ArrayList<>();
private ArrayList<Passenger> passengers = new ArrayList<>(); private ArrayList<Booking> bookings = new ArrayList<>();

private Scanner sc = new Scanner(System.in);


public void addFlight() { System.out.print("Enter Flight Number: "); int fno = sc.nextInt(); sc.nextLine(); System.out.print("Enter Source: ");
String src = sc.nextLine(); System.out.print("Enter Destination: "); String dest = sc.nextLine(); System.out.print("Enter Aircraft Model: "); String model = sc.nextLine(); System.out.print("Enter Total Seats: ");
 
int seats = sc.nextInt();
flights.add(new Flight(fno, src, dest, new Aircraft(model, seats))); System.out.println("✅ Flight added successfully!");
}


public void addPassenger() { System.out.print("Enter Passenger ID: "); int id = sc.nextInt(); sc.nextLine(); System.out.print("Enter Name: ");
String name = sc.nextLine(); System.out.print("Enter Age: "); int age = sc.nextInt();
passengers.add(new Passenger(id, name, age)); System.out.println("✅ Passenger added successfully!");
}


public void createBooking() {
if (flights.isEmpty() || passengers.isEmpty()) { System.out.println("❌ Please add flights and passengers first!");
return;
}
System.out.println("Available Flights:"); for (Flight f : flights) System.out.println(f);

System.out.print("Enter Flight Number: "); int fno = sc.nextInt();
Flight flight = null; for (Flight f : flights) {
if (f.getFlightNumber() == fno) { flight = f; break; }
}
if (flight == null) {
System.out.println("❌ Flight not found!");
 
return;
}


Seat seat = flight.getAvailableSeat(); if (seat == null) {
System.out.println("❌ No seats available on this flight!"); return;
}


System.out.println("Available Passengers:");
for (Passenger p : passengers) System.out.println(p); System.out.print("Enter Passenger ID: ");
int pid = sc.nextInt(); Passenger passenger = null;
for (Passenger p : passengers) {
if (p.getId() == pid) { passenger = p; break; }
}
if (passenger == null) {
System.out.println("❌ Passenger not found!"); return;
}


System.out.print("Enter Ticket Fare: "); double fare = sc.nextDouble();

Payment payment = new Payment(fare); payment.confirmPayment();
Booking booking = new Booking(passenger, flight, seat, payment); booking.confirmBooking();
bookings.add(booking);
System.out.println("✅ Booking created successfully!");
}
 

public void checkInAndIssueBoardingPass() { if (bookings.isEmpty()) {
System.out.println("❌ No bookings found!"); return;
}
System.out.println("Bookings:");
for (int i = 0; i < bookings.size(); i++) { System.out.println((i+1) + ". " + bookings.get(i));
}
System.out.print("Select booking number: "); int idx = sc.nextInt();
if (idx < 1 || idx > bookings.size()) { System.out.println("❌ Invalid selection!");
return;
}
Booking booking = bookings.get(idx-1); booking.issueBoardingPass();
if (booking.getBoardingPass() != null) { System.out.println("✅ Boarding Pass Issued!"); System.out.println(booking.getBoardingPass());
} else {
System.out.println("❌ Cannot issue boarding pass. Payment not confirmed!");
}
}


public void cancelBooking() { if (bookings.isEmpty()) {
System.out.println("❌ No bookings found!"); return;
}
System.out.println("Bookings:");
 
for (int i = 0; i < bookings.size(); i++) { System.out.println((i+1) + ". " + bookings.get(i));
}
System.out.print("Select booking number to cancel: "); int idx = sc.nextInt();
if (idx < 1 || idx > bookings.size()) { System.out.println("❌ Invalid selection!");
return;
}
Booking booking = bookings.get(idx-1); booking.cancelBooking(); bookings.remove(idx-1);
System.out.println("✅ Booking cancelled. Seat released!");
}


public void displayFlights() { if (flights.isEmpty()) {
System.out.println("No flights available!"); return;
}
for (Flight f : flights) { System.out.println(f);
}
}


public void menu() { int choice;
do {
System.out.println("\n--- Airline Reservation & Check-in System ---"); System.out.println("1. Add Flight");
System.out.println("2. Add Passenger"); System.out.println("3. Create Booking");
 
System.out.println("4. Check-in & Issue Boarding Pass"); System.out.println("5. Cancel Booking"); System.out.println("6. Display Flights"); System.out.println("7. Exit");
System.out.print("Enter choice: "); choice = sc.nextInt();

switch (choice) {
case 1: addFlight(); break; case 2: addPassenger(); break; case 3: createBooking(); break;
case 4: checkInAndIssueBoardingPass(); break;
case 5: cancelBooking(); break; case 6: displayFlights(); break;
case 7: System.out.println("Exiting..."); break; default: System.out.println("Invalid choice!");
}
} while (choice != 7);
}


public static void main(String[] args) { AirlineSystem system = new AirlineSystem(); system.menu();
}
}
