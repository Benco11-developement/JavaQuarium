package fr.benco11.javaquarium.options;

/**
 * Exception levée lorsqu'une erreur survient durant le parsing d'une option
 */
public class OptionParseException extends RuntimeException {
    /**
     * Constructeur à partir du nom de l'option
     *
     * @param optionName nom de l'option
     */
    public OptionParseException(String optionName) {
        super("Erreur de parsing de l'option '"+optionName+"'");
    }

    /**
     * Constructeur à partir de l'id de l'option
     *
     * @param option id de l'option
     */
    public OptionParseException(Options.StandardOption option) {
        this(option.id());
    }

    /**
     * Constructeur à partir d'un message et d'une exception parente
     *
     * @param message   message de l'exception
     * @param exception exception parente
     */
    public OptionParseException(String message, Exception exception) {
        super(message, exception);
    }
}
