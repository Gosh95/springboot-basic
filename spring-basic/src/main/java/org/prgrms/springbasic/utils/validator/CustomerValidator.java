package org.prgrms.springbasic.utils.validator;

import lombok.extern.slf4j.Slf4j;
import org.prgrms.springbasic.domain.customer.Customer;
import org.prgrms.springbasic.domain.customer.CustomerType;
import org.prgrms.springbasic.utils.exception.NonExistentCommand;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.prgrms.springbasic.utils.enumm.message.ErrorMessage.COMMAND_ERROR;

@Slf4j
public class CustomerValidator {

    public static CustomerType validateCustomerType(String command) {
        CustomerType customerType;

        try {
            customerType = CustomerType.valueOf(command.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("Got non existent command: {}", command);
            throw new NonExistentCommand(COMMAND_ERROR.getMessage());
        }

        return customerType;
    }

    public static List<Customer> validateCustomers(List<Customer> customers) {
        if (customers.size() == 0) {
            log.error("Can't find any customer.");

            return emptyList();
        }

        return customers;
    }
}
