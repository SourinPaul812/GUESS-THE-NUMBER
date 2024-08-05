import java.io.*;
import java.util.*;

class Player implements Serializable {
    private static final long serialVersionUID = 1L;
    String name;
    int score;

    Player(String name, int score) {
        this.name = name;
        this.score = score;
    }

    @Override
    public String toString() {
        return name + ": " + score;
    }
}

public class GuessTheNumber {
    private static final String FILE_NAME = "highscores.dat";
    private static final int MAX_ATTEMPTS = 10;
    private static final int ROUNDS = 3;
    private static final int TOP_PLAYERS = 10;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Player> highScores = loadHighScores();

        while (true) {
            System.out.println("1. Play Game");
            System.out.println("2. View High Scores");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    playGame(scanner, highScores);
                    break;
                case 2:
                    viewHighScores(highScores);
                    break;
                case 3:
                    saveHighScores(highScores);
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void playGame(Scanner scanner, List<Player> highScores) {
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        Random random = new Random();
        int score = 0;

        for (int round = 1; round <= ROUNDS; round++) {
            System.out.println("Round " + round + " of " + ROUNDS);
            int numberToGuess = random.nextInt(100) + 1;
            boolean guessed = false;

            for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
                System.out.println("Attempt " + attempt + " of " + MAX_ATTEMPTS);
                System.out.print("Enter your guess (1-100): ");

                int userGuess = 0;
                try {
                    userGuess = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number between 1 and 100.");
                    scanner.next();  // Clear the invalid input
                    attempt--;
                    continue;
                }

                if (userGuess == numberToGuess) {
                    System.out.println("Congratulations! You guessed the number.");
                    score += (MAX_ATTEMPTS - attempt + 1) * 10; // Points based on remaining attempts
                    guessed = true;
                    break;
                } else if (userGuess < numberToGuess) {
                    System.out.println("The number is higher than your guess.");
                } else {
                    System.out.println("The number is lower than your guess.");
                }
            }

            if (!guessed) {
                System.out.println("Sorry, you've used all your attempts. The number was " + numberToGuess);
            }

            System.out.println("Your score after round " + round + " is: " + score);
            System.out.println();
        }

        System.out.println("Game Over!");
        System.out.println("Your final score is: " + score);

        highScores.add(new Player(name, score));
        highScores.sort((p1, p2) -> Integer.compare(p2.score, p1.score));
        if (highScores.size() > TOP_PLAYERS) {
            highScores.remove(highScores.size() - 1);
        }
    }

    private static void viewHighScores(List<Player> highScores) {
        System.out.println("Top " + TOP_PLAYERS + " Players:");
        for (Player player : highScores) {
            System.out.println(player);
        }
    }

    private static List<Player> loadHighScores() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (List<Player>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    private static void saveHighScores(List<Player> highScores) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(highScores);
        } catch (IOException e) {
            System.out.println("Failed to save high scores.");
        }
    }
}
     
