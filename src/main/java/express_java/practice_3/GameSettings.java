package express_java.practice_3;

public class GameSettings {
    static int maxPlayers;
    final String gameName;
    int currentPlayers;

    static {
        maxPlayers = 3;
    }

    public GameSettings(String gameName) {
        this.gameName = gameName;
    }

    public static void setMaxPlayers(int maxPlayers) {
        GameSettings.maxPlayers = maxPlayers;
    }

    public void addPlayer() {
        if (currentPlayers < maxPlayers) {
            currentPlayers++;
        }
    }

    public void printGameStatus() {
        System.out.println("Game Name: " + gameName + ", Current Players: " + currentPlayers + ", Max Players: " + maxPlayers);
    }
}
