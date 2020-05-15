package exception;

public class StringTooShortException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StringTooShortException() {
		super("String entered too short");
	}

	public StringTooShortException(String message) {
		super(message);
	}

}
