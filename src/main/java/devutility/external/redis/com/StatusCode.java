package devutility.external.redis.com;

public enum StatusCode {
	OK("OK"), MULTIOK("+OK");
	private String value;

	StatusCode(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}