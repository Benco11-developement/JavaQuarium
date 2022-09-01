package fr.benco11.javaquarium.io;

/**
 * Exception pouvant être levée lors de la lecture d'un fichier aquarium
 */
public class AquariumReadException extends RuntimeException {
    /**
     * Construit avec un message
     *
     * @param message message de l'exception
     */
    public AquariumReadException(String message) {
        super(message);
    }

    /**
     * Constructeur avec un numéro de ligne
     *
     * @param line ligne de l'erreur
     */
    public AquariumReadException(int line) {
        this("Erreur de lecture", line);
    }

    /**
     * Constructeur avec un message et un numéro de ligne (message + ligne)
     *
     * @param message message de l'exception
     * @param line    ligne de l'erreur
     */
    public AquariumReadException(String message, int line) {
        this(message + " à la ligne " + line);
    }
}
