package code;

public class Error {
	private String id;
	private int line;
	private String message;

	public Error(String id, int line, String message) {
		this.id = id;
		this.line = line;
		this.message = message;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getLine() {
		return this.line;
	}

	public void setMessage(int line) {
		this.line = line;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		if (line == -1) {
			return "Error: " + message;
		}

		return "Error at line " + line + ": " + message;
	}

}
