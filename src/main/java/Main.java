import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Console-based prototype for the Smart Infrastructure Maintenance Planning System.
 * Priority value 1 is the most urgent; larger values indicate lower urgency.
 */
public class Main {
    private static final Path DEFAULT_DATA_FILE = Path.of("data", "sample-infrastructure-data.csv");
    private final List<InfrastructureAsset> assets;
    private final Scanner scanner = new Scanner(System.in);

    private Main(List<InfrastructureAsset> assets) {
        this.assets = assets;
    }

    public static void main(String[] args) {
        Path dataFile = args.length > 0 ? Path.of(args[0]) : DEFAULT_DATA_FILE;
        try {
            List<InfrastructureAsset> loaded = loadAssets(dataFile);
            if (loaded.isEmpty()) {
                System.out.println("No infrastructure records found in " + dataFile.toAbsolutePath());
                return;
            }
            new Main(loaded).run();
        } catch (IOException | IllegalArgumentException ex) {
            System.err.println("Could not load the CSV data: " + ex.getMessage());
            System.err.println("Run the program from the repository root or pass a CSV path as an argument.");
        }
    }

    private void run() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Smart Infrastructure Maintenance Planner ===");
            System.out.println("1. View all infrastructure records");
            System.out.println("2. Search by asset ID");
            System.out.println("3. Show maintenance queue (most urgent first)");
            System.out.println("4. Update maintenance status");
            System.out.println("5. Show records sorted by priority");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> showAll();
                case "2" -> searchById();
                case "3" -> showMaintenanceQueue();
                case "4" -> updateStatus();
                case "5" -> showSortedByPriority();
                case "0" -> running = false;
                default -> System.out.println("Invalid option. Please enter 0 to 5.");
            }
        }
        System.out.println("Thank you for using the maintenance planner.");
    }

    private static List<InfrastructureAsset> loadAssets(Path csvPath) throws IOException {
        List<InfrastructureAsset> result = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(csvPath)) {
            String header = reader.readLine();
            if (header == null) {
                return result;
            }
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.isBlank()) continue;
                String[] fields = line.split(",", -1);
                if (fields.length != 8) {
                    throw new IllegalArgumentException("Expected 8 comma-separated fields at line " + lineNumber);
                }
                try {
                    result.add(new InfrastructureAsset(
                        fields[0].trim(), fields[1].trim(), fields[2].trim(),
                        fields[3].trim(), fields[4].trim(), fields[5].trim(),
                        Integer.parseInt(fields[6].trim()), fields[7].trim()
                    ));
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("Priority must be a number at line " + lineNumber, ex);
                }
            }
        }
        return result;
    }

    private void showAll() {
        printHeader();
        assets.forEach(this::printAsset);
    }

    private void searchById() {
        System.out.print("Enter asset ID (for example, I001): ");
        String id = scanner.nextLine().trim();
        assets.stream()
            .filter(asset -> asset.getId().equalsIgnoreCase(id))
            .findFirst()
            .ifPresentOrElse(this::printAsset, () -> System.out.println("No asset found with ID " + id + "."));
    }

    private void showMaintenanceQueue() {
        PriorityQueue<InfrastructureAsset> queue = new PriorityQueue<>(
            Comparator.comparingInt(InfrastructureAsset::getPriority)
                .thenComparing(InfrastructureAsset::getId)
        );
        for (InfrastructureAsset asset : assets) {
            if (!asset.getStatus().equalsIgnoreCase("Completed")) {
                queue.offer(asset);
            }
        }
        System.out.println("\nMaintenance queue (priority 1 is most urgent):");
        printHeader();
        while (!queue.isEmpty()) {
            printAsset(queue.poll());
        }
    }

    private void updateStatus() {
        System.out.print("Enter asset ID: ");
        String id = scanner.nextLine().trim();
        InfrastructureAsset asset = assets.stream()
            .filter(item -> item.getId().equalsIgnoreCase(id))
            .findFirst().orElse(null);
        if (asset == null) {
            System.out.println("No asset found with ID " + id + ".");
            return;
        }

        System.out.print("Enter new status (Pending / In Progress / Completed): ");
        String status = scanner.nextLine().trim();
        if (!List.of("Pending", "In Progress", "Completed").stream()
                .anyMatch(valid -> valid.equalsIgnoreCase(status))) {
            System.out.println("Invalid status. No changes made.");
            return;
        }
        asset.setStatus(normalizeStatus(status));
        System.out.println("Status updated in this session:");
        printAsset(asset);
        System.out.println("Note: this prototype does not save changes back to the CSV file.");
    }

    private void showSortedByPriority() {
        printHeader();
        assets.stream()
            .sorted(Comparator.comparingInt(InfrastructureAsset::getPriority)
                .thenComparing(InfrastructureAsset::getId))
            .forEach(this::printAsset);
    }

    private static String normalizeStatus(String status) {
        return switch (status.toLowerCase()) {
            case "pending" -> "Pending";
            case "in progress" -> "In Progress";
            case "completed" -> "Completed";
            default -> status;
        };
    }

    private void printHeader() {
        System.out.printf("%-6s | %-18s | %-12s | %-8s | %-10s | %-8s | %-8s%n",
            "ID", "Infrastructure", "Type", "Location", "Condition", "Priority", "Status");
        System.out.println("-".repeat(91));
    }

    private void printAsset(InfrastructureAsset asset) {
        System.out.printf("%-6s | %-18s | %-12s | %-8s | %-10s | %-8d | %-8s%n",
            asset.getId(), asset.getName(), asset.getType(), asset.getLocation(),
            asset.getCondition(), asset.getPriority(), asset.getStatus());
    }
}

final class InfrastructureAsset {
    private final String id;
    private final String name;
    private final String type;
    private final String location;
    private final String condition;
    private final String severity;
    private final int priority;
    private String status;

    InfrastructureAsset(String id, String name, String type, String location,
                        String condition, String severity, int priority, String status) {
        if (id.isBlank() || name.isBlank()) {
            throw new IllegalArgumentException("Asset ID and name cannot be empty.");
        }
        if (priority < 1) {
            throw new IllegalArgumentException("Priority must be 1 or greater.");
        }
        this.id = id;
        this.name = name;
        this.type = type;
        this.location = location;
        this.condition = condition;
        this.severity = severity;
        this.priority = priority;
        this.status = status;
    }

    String getId() { return id; }
    String getName() { return name; }
    String getType() { return type; }
    String getLocation() { return location; }
    String getCondition() { return condition; }
    String getSeverity() { return severity; }
    int getPriority() { return priority; }
    String getStatus() { return status; }
    void setStatus(String status) { this.status = status; }
}
