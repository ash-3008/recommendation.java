import java.io.*;
import java.util.*;

/**
 * Recommendation System (Single-User Based)
 * ------------------------------------------
 * A simple command-line recommendation engine in Java that:
 * - Loads data from a CSV file
 * - Asks the user to choose a content type (Books, Movies, WebSeries)
 * - Then asks for a genre (e.g., Thriller, Comedy, Romance, etc.)
 * - Filters content based on user choices and language rules
 * 
 * Constraints:
 * - Movies: Only Bollywood (Hindi)
 * - WebSeries: Only Hindi, K-Drama, or Hollywood
 * 
 *
 * Developed for educational/demo purposes.
 */
public class Recommendation {

    // Inner class to store content items (Movies, Books, WebSeries)
    static class Item {
        String title, type, genre, language;

        Item(String title, String type, String genre, String language) {
            this.title = title;
            this.type = type;
            this.genre = genre;
            this.language = language;
        }
    }

    // List to hold all items from the CSV
    private static final List<Item> items = new ArrayList<>();

    // Predefined options for content types and genres
    private static final List<String> types = Arrays.asList("Books", "Movies", "WebSeries");
    private static final List<String> genres = Arrays.asList(
            "Thriller", "Rom-Com", "Comedy", "Romance", "Action",
            "Family", "Drama", "Suspense", "Horror", "Fantasy"
    );

    public static void main(String[] args) {
        // Load items from CSV
        loadCSV("data.csv");

        // Setup scanner for user input
        Scanner scanner = new Scanner(System.in);

        // Ask user to choose type of recommendation
        String selectedType = getUserChoice(scanner, "content type", types);

        // Ask user to choose genre
        String selectedGenre = getUserChoice(scanner, "genre", genres);

        // Display personalized recommendations
        System.out.println("\n Recommendations for you (" + selectedType + " - " + selectedGenre + "):");
        recommend(selectedType, selectedGenre);

        scanner.close();
    }

    /**
     * Prompts user to choose from a list of options.
     * Handles input validation for safe selection.
     */
    private static String getUserChoice(Scanner scanner, String prompt, List<String> options) {
        System.out.println("\nSelect a " + prompt + ":");
        for (int i = 0; i < options.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, options.get(i));
        }

        int choice = -1;
        while (choice < 1 || choice > options.size()) {
            System.out.print("Enter choice (1-" + options.size() + "): ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // clear newline
            } else {
                scanner.next(); // discard invalid input
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        return options.get(choice - 1);
    }

    /**
     * Filters and prints recommended items based on type and genre.
     * Applies language constraints depending on selected content type.
     */
    private static void recommend(String type, String genre) {
        boolean found = false;

        for (Item item : items) {
            // Check if item matches user-selected type and genre
            if (!item.type.equalsIgnoreCase(type) || !item.genre.equalsIgnoreCase(genre)) continue;

            // Movies: Only Hindi (Bollywood)
            if (type.equalsIgnoreCase("Movies") && !item.language.equalsIgnoreCase("Hindi")) continue;

            // WebSeries: Only Hindi, K-Drama, or Hollywood allowed
            if (type.equalsIgnoreCase("WebSeries")) {
                String lang = item.language.toLowerCase();
                if (!(lang.equals("hindi") || lang.equals("k-drama") || lang.equals("hollywood"))) continue;
            }

            // Print recommended item
            System.out.println("- " + item.title + " [" + item.language + "]");
            found = true;
        }

        if (!found) {
            System.out.println("No recommendations found in that category and genre.");
        }
    }

    /**
     * Loads data from a CSV file.
     * Each row should follow the format: Title,Type,Genre,Language
     */
    private static void loadCSV(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            br.readLine(); // Skip header line

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1); // split by comma
                if (parts.length == 4) {
                    items.add(new Item(
                            parts[0].trim(),
                            parts[1].trim(),
                            parts[2].trim(),
                            parts[3].trim()
                    ));
                }
            }
        } catch (IOException e) {
            System.err.println(" Error reading CSV file: " + e.getMessage());
        }
    }
}
