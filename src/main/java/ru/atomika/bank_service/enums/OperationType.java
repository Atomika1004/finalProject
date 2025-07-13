package ru.atomika.bank_service.enums;

public enum OperationType {
    PUT("Добавить"),
    TAKE("Снять"),
    TRANSFER("Перевод")
    ;
    final String translation;

    public String getTranslation (OperationType translation) {
        return this.translation;
    }

    OperationType(String translation) {
        this.translation = translation;
    }
}