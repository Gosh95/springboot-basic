package org.prgrms.springbasic.utils.enumm.message;

public enum ErrorMessage {

    PARSING_ERROR("Type the correct number type."),

    COMMAND_ERROR("Please check your command."),

    DISCOUNT_INFO_ERR("Check the discount information you entered."),

    NOT_EXIST_ENUM_TYPE("Can't find any enum type"),

    NOT_INSERTED("Nothing was inserted."),

    NOT_UPDATED("Nothing was updated."),

    NOT_DELETED("Nothing was deleted."),

    DUPLICATED_CUSTOMER("Duplicated customer."),

    DUPLICATED_VOUCHER("Duplicated voucher."),

    NOT_FOUND_CUSTOMER("Can't find any customer."),

    NOT_FOUND_VOUCHER("Can't find any voucher.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
