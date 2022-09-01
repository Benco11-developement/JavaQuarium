package fr.benco11.javaquarium.options;

/**
 * Exception levée lorsqu'une erreur survient durant le parsing d'une option
 */
public class OptionParseException extends RuntimeException {
    /**
     * Construit avec le nom de l'option
     *
     * @param optionName nom de l'option
     */
    public OptionParseException(String optionName) {
        super("Erreur de parsing de l'option '" + optionName + "'");
    }

    /**
     * Construit avec id de l'option
     *
     * @param option id de l'option
     */
    public OptionParseException(Options.StandardOption option) {
        this(option.id());
    }

    /**
     * Construit avec un message et une exception parente
     *
     * @param message   message de l'exception
     * @param exception exception parente
     */
    public OptionParseException(String message, Exception exception) {
        super(message, exception);
    }
}
