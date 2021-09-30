package sample.util;

public enum ResultMessage {
    OK("НОРМА"),
    ERROR("ОШИБКА"),
    UNREACHED("НЕ ПРОВЕРЕНО"),
    PS_ERROR("ОШИБКА ПИТАНИЯ"),
    PASSED("Тест пройден"),
    FAILED("Тест не пройден");

    private String message;

    ResultMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}