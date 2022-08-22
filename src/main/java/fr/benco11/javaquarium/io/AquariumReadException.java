package fr.benco11.javaquarium.io;

public class AquariumReadException extends RuntimeException {
    public AquariumReadException(String message) {
        super(message);
    }

    public AquariumReadException(int line) {
        super("Erreur de lecture de la ligne "+line);
    }
}
