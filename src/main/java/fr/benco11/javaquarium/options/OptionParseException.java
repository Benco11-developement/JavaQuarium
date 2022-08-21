package fr.benco11.javaquarium.options;

public class OptionParseException extends RuntimeException {
    public OptionParseException(String optionName) {
        super("Erreur de parsing de l'option '"+optionName+"'");
    }

    public OptionParseException(String optionName, Exception exception) {
        super("Erreur de parsing de l'option '"+optionName+"'", exception);
    }
}
