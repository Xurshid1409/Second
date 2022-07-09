package second.education.model.request;

public enum ApplicationStatus {

    DEFAULT_STATUS("Ariza yuborildi");

    private final String message;

    ApplicationStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
