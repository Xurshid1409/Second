package second.education.model.request;

public enum DefaultRole {

    ROLE_ABITURIYENT("ROLE_ABITURIYENT"),
    ROLE_ADMIN("ROLE_ADMIN");


    private final String message;

    DefaultRole(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
