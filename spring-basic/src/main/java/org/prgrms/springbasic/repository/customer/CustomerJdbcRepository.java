package org.prgrms.springbasic.repository.customer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.prgrms.springbasic.domain.customer.Customer;
import org.prgrms.springbasic.domain.customer.CustomerType;
import org.prgrms.springbasic.utils.exception.NoDatabaseChangeException;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static java.util.Optional.empty;
import static org.prgrms.springbasic.utils.UUIDConverter.toUUID;
import static org.prgrms.springbasic.utils.enumm.message.ErrorMessage.NOT_INSERTED;
import static org.prgrms.springbasic.utils.enumm.message.ErrorMessage.NOT_UPDATED;
import static org.prgrms.springbasic.utils.sql.CustomerSQL.*;

@Slf4j
@Profile({"prd", "test"})
@Repository
@RequiredArgsConstructor
public class CustomerJdbcRepository implements CustomerRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .disable(WRITE_DATES_AS_TIMESTAMPS)
            .registerModule(new JavaTimeModule());

    @Override
    public Customer save(Customer customer) {
        var insertedCount = jdbcTemplate.update(CREATE_CUSTOMER.getQuery(), toParamMap(customer));

        if(insertedCount != 1) {
            log.error("Got not inserted result: {}", customer);

            throw new NoDatabaseChangeException(NOT_INSERTED.getMessage());
        }

        return customer;
    }

    @Override
    public Optional<Customer> findByCustomerId(UUID customerId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_BY_CUSTOMER_ID.getQuery(),
                    singletonMap("customerId",
                            customerId.toString().getBytes()),
                                    customerRowMapper));
        } catch (EmptyResultDataAccessException e) {
            log.error("Got empty result: {}", e.getMessage());

            return empty();
        }
    }

    @Override
    public Optional<Customer> findByVoucherId(UUID voucherId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_BY_VOUCHER_ID.getQuery(),
                    singletonMap("voucherId",
                            voucherId.toString().getBytes()),
                                    customerRowMapper));
        } catch (EmptyResultDataAccessException e) {
            log.error("Got empty result: {}", e.getMessage());

            return empty();
        }
    }

    @Override
    public List<Customer> findCustomers() {
        return jdbcTemplate.query(SELECT_CUSTOMERS.getQuery(), customerRowMapper);
    }

    @Override
    public int countCustomers() {
        var count = jdbcTemplate.queryForObject(SELECT_COUNT.getQuery(), emptyMap(), Integer.class);

        return (count == null) ? 0 : count;
    }

    @Override
    public Customer update(Customer customer) {
        var updatedCount = jdbcTemplate.update(UPDATE_CUSTOMER.getQuery(), toParamMap(customer));

        if(updatedCount != 1) {
            log.error("Got not updated result: {}", customer);

            throw new NoDatabaseChangeException(NOT_UPDATED.getMessage());
        }

        return customer;
    }

    @Override
    public void deleteByCustomerId(UUID customerId) {
        jdbcTemplate.update(DELETE_BY_CUSTOMER_ID.getQuery(),
                singletonMap("customerId",
                        customerId.toString().getBytes()));
    }

    @Override
    public void deleteCustomers() {
        jdbcTemplate.update(DELETE_CUSTOMERS.getQuery(), emptyMap());
    }

    private Map<String, Object> toParamMap(Customer customer) {
        return objectMapper.convertValue(customer, new TypeReference<HashMap<String, Object>>() {});
    }

    private static final RowMapper<Customer> customerRowMapper = (resultSet, i) -> {
        var customerId = toUUID(resultSet.getBytes("customer_id"));
        var customerType = CustomerType.valueOf(resultSet.getString("customer_type").toUpperCase());
        var name = resultSet.getString("name");
        var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        var modifiedAt = resultSet.getTimestamp("modified_at") == null ? null : resultSet.getTimestamp("modified_at").toLocalDateTime();

        return new Customer(customerId, customerType, name, createdAt, modifiedAt);
    };

}
