package exception;

public class GenderMismatchExcepiton extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -920062007051761563L;

	public GenderMismatchExcepiton() {
		super("Gender entered invalid");
	}
	public GenderMismatchExcepiton(String message) {
		super(message);
	}
}
