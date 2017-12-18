package com.courge.shop.dao.implementation;

import com.courge.shop.Exception.BadRequestException;
import com.courge.shop.dao.CustomerDao;
import com.courge.shop.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class JdbcCustomerDao implements CustomerDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcCustomerDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Customer createCustomer(Customer newCustomer) {
        String sql = "INSERT INTO customer(uuid, first_name, last_name, email, telephone, is_available) " +
                "VALUES(?, ?, ?, ?, ?, true ) ";

        Object[] sqlParameters = { newCustomer.getUuid(), newCustomer.getFirstName(),
                newCustomer.getLastName(), newCustomer.getEmail(), newCustomer.getTelephone() };

        return (Customer) this.executeSqlUpdate(newCustomer,sql,sqlParameters);
    }

    @Override
    public Page<Customer> findAll(Pageable pageable) {

        int pageSize = pageable.getPageSize();
        int offset = (pageable.getPageNumber() - 1) * pageSize;
        String sql = "SELECT * FROM customer WHERE is_available = true LIMIT ?, ? ";
        try {
            return new PageImpl<>(this.jdbcTemplate.query(sql, new Object[]{offset, pageSize}, new CustomerMapper()),
                    pageable, count());
        } catch (DataAccessException ex)  {
            LOGGER.error(ex.getMessage());
            throw BadRequestException.create(ex.getMessage());
        }
    }

    @Override
    public Customer findByUUID(String uuid) {
        String sql = "SELECT * FROM customer WHERE is_available = true AND uuid = ?";
        return this.executeSqlObjectQuery(sql, new Object[]{uuid});
    }

    @Override
    public Customer findByEmail(String email) {
        String sql = "SELECT * FROM customer WHERE is_available = true AND email = ?";
        return this.executeSqlObjectQuery(sql, new Object[]{email});
    }

    @Override
    public Customer findByTelephone(String telephone) {
        String sql = "SELECT * FROM customer WHERE is_available = true AND telephone = ?";
        return this.executeSqlObjectQuery(sql, new Object[]{telephone});
    }

    @Override
    public Customer updateCustomer(Customer updatedCustomer) {
        String sql = "UPDATE  customer SET first_name = ?, last_name = ?," +
                " email = ?, telephone = ? WHERE is_available = true AND uuid = ?";

        Object[] sqlParameters = { updatedCustomer.getFirstName(), updatedCustomer.getLastName(),
                updatedCustomer.getEmail(), updatedCustomer.getTelephone(), updatedCustomer.getUuid() };

        return (Customer) this.executeSqlUpdate(updatedCustomer, sql, sqlParameters);
    }

    @Override
    public String deleteCustomer(String uuid) {
        String sql = "UPDATE customer SET is_available = false WHERE uuid = ?";
        return (String) this.executeSqlUpdate(uuid, sql, new Object[]{uuid});
    }

    private Integer count(){
        String sql = "SELECT COUNT(*) FROM customer WHERE is_available = true";
        return this.jdbcTemplate.queryForObject(sql, new Object[]{}, Integer.class);
    }

    Object executeSqlUpdate(Object returnValue, String sql, Object[] sqlParameters) {
        try{
            this.jdbcTemplate.update(sql, sqlParameters);
            return returnValue;
        } catch (DataAccessException ex){
            LOGGER.error(ex.getMessage());
            throw BadRequestException.create(ex.getMessage());
        }
    }

    Customer executeSqlObjectQuery(String sql, Object[] sqlParameters) {
        try {
            Customer customer = this.jdbcTemplate.queryForObject(sql, sqlParameters, new CustomerMapper());
            return customer;
        } catch (EmptyResultDataAccessException e) {
            LOGGER.error(e.getMessage());
            return null;
        } catch (DataAccessException ex) {
            LOGGER.error(ex.getMessage());
            throw BadRequestException.create(ex.getMessage());
        }
    }

    private static final class CustomerMapper implements RowMapper<Customer> {

        @Override
        public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
            Customer customer = new Customer();
            customer.setUuid(rs.getString("uuid"));
            customer.setFirstName(rs.getString("first_name"));
            customer.setLastName(rs.getString("last_name"));
            customer.setEmail(rs.getString("email"));
            customer.setTelephone(rs.getString("telephone"));

            return customer;
        }
    }

}
