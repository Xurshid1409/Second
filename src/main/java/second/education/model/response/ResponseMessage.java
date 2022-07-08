package second.education.model.response;

public enum ResponseMessage {

    NOT_FOUND("Topilmadi"),
    SUCCESSFULLY_SAVED("Muvaffaqiyatli saqlandi"),
    SUCCESSFULLY("Muvaffaqiyatli bajarildi"),
    ERROR_SAVED("Saqlashda xatolik"),
    SUCCESSFULLY_UPDATE("Muvaffaqiyatli yangilandi"),
    ERROR_UPDATE("Yangilashda xatolik"),
    SUCCESSFULLY_DELETED("Muvaffaqiyatli o'chirildi"),
    ERROR_DELETED("O'chirishda xatolik"),
    ALREADY_EXISTS("Allaqachon saqlangan"),
    ERROR("Xatolik");

    private final String message;

    ResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
