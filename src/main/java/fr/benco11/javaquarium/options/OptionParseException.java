package fr.benco11.javaquarium.options;

/**
 * Exception levée lorsqu'une erreur survient durant le parsing d'une option
 */
public class OptionParseException extends RuntimeException {
    public OptionParseException(String optionName) {
        super("Erreur de parsing de l'option '"+optionName+"'");
    }

    public OptionParseException(Options.StandardOption option) {
        this(option.id());
    }

    public OptionParseException(String message, Exception exception) {
        super(message, exception);
    }
}
