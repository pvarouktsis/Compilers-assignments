public class Error {
	private String id;
	private int line;
	private String message;

	Error(String id, int line, String message) {
		this.id = id;
		this.line = line;
		this.message = message;
	}

	String getId() {
		return this.id;
	}

	void setId(String id) {
		this.id = id;
	}

	int getLine() {
		return this.line;
	}

	void setMessage(int line) {
		this.line = line;
	}

	String getMessage() {
		return this.message;
	}

	void setMessage(String message) {
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
