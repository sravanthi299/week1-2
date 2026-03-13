import java.util.*;

class ParkingSpot {
    String licensePlate;
    long entryTime;
    boolean occupied;

    ParkingSpot() {
        this.occupied = false;
    }
}

public class ParkingLotSystem {

    private ParkingSpot[] table;
    private int size;
    private int totalProbes = 0;
    private int totalVehicles = 0;

    public ParkingLotSystem(int capacity) {
        table = new ParkingSpot[capacity];
        size = capacity;

        for (int i = 0; i < capacity; i++) {
            table[i] = new ParkingSpot();
        }
    }

    // Hash function
    private int hash(String plate) {
        return Math.abs(plate.hashCode()) % size;
    }

    // Park vehicle
    public void parkVehicle(String plate) {

        int index = hash(plate);
        int probes = 0;

        while (table[index].occupied) {
            index = (index + 1) % size; // linear probing
            probes++;
        }

        table[index].licensePlate = plate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].occupied = true;

        totalProbes += probes;
        totalVehicles++;

        System.out.println("Vehicle " + plate + " parked at spot #" + index +
                " (" + probes + " probes)");
    }

    // Exit vehicle
    public void exitVehicle(String plate) {

        int index = hash(plate);

        while (table[index].occupied) {

            if (table[index].licensePlate.equals(plate)) {

                long duration = System.currentTimeMillis() - table[index].entryTime;
                double hours = duration / (1000.0 * 60 * 60);

                double fee = hours * 5; // $5 per hour

                table[index].occupied = false;

                System.out.println("Vehicle " + plate + " exited from spot #" + index);
                System.out.println("Duration: " + String.format("%.2f", hours) +
                        " hours, Fee: $" + String.format("%.2f", fee));

                return;
            }

            index = (index + 1) % size;
        }

        System.out.println("Vehicle not found.");
    }

    // Find nearest available spot
    public int findNearestSpot() {

        for (int i = 0; i < size; i++) {
            if (!table[i].occupied) {
                return i;
            }
        }
        return -1;
    }

    // Statistics
    public void getStatistics() {

        int occupied = 0;

        for (ParkingSpot spot : table) {
            if (spot.occupied)
                occupied++;
        }

        double occupancyRate = (occupied * 100.0) / size;
        double avgProbes = totalVehicles == 0 ? 0 : (double) totalProbes / totalVehicles;

        System.out.println("Occupancy: " + occupancyRate + "%");
        System.out.println("Average Probes: " + avgProbes);
    }

    public static void main(String[] args) {

        ParkingLotSystem lot = new ParkingLotSystem(500);

        lot.parkVehicle("ABC-1234");
        lot.parkVehicle("ABC-1235");
        lot.parkVehicle("XYZ-9999");

        lot.exitVehicle("ABC-1234");

        System.out.println("Nearest Spot: #" + lot.findNearestSpot());

        lot.getStatistics();
    }
}