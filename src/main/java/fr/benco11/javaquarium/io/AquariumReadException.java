package fr.benco11.javaquarium.io;

/**
 * Exception pouvant être levée lors de la lecture d'un fichier aquarium
 */
public class AquariumReadException extends RuntimeException {
    /**
     * Constructeur à partir d'un message
     *
     * @param message message de l'exception
     */
    public AquariumReadException(String message) {
        super(message);
    }

    /**
     * Constructeur à partir d'une ligne
     *
     * @param line ligne de l'erreur
     */
    public AquariumReadException(int line) {
        super("Erreur de lecture de la ligne "+line);
    }
}
