package com.courge.shop.dao.implementation;

import com.courge.shop.Exception.BadRequestException;
import com.courge.shop.dao.CustomerDao;
import com.courge.shop.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import sun.invoke.empty.Empty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcCustomerDao implements CustomerDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Customer createCustomer(Customer newCustomer) {
        String sql = "INSERT INTO customer(uuid, first_name, last_name, email, telephone, is_available) " +
                "VALUES(?, ?, ?, ?, ?, true ) ";
        Object[] sqlParameters = { newCustomer.getUuid(), newCustomer.getFirstName(),
                newCustomer.getLastName(), newCustomer.getEmail(), newCustomer.getTelephone() };

        try {
            this.jdbcTemplate.update(sql, sqlParameters);
            return newCustomer;
        } catch ( DataAccessException ex ){
            throw BadRequestException.create(ex.getMessage());
        }
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
            BadRequestException.create(ex.getMessage());
        }

        return null;
    }

    @Override
    public Customer findByUUID(String uuid) {
        String sql = "SELECT * FROM customer WHERE is_available = true AND uuid = ?";
        try {
            Customer customer = this.jdbcTemplate.queryForObject(sql, new Object[]{uuid}, new CustomerMapper());
            return  customer;
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (DataAccessException ex) {
            throw BadRequestException.create(ex.getMessage());
        }
    }

    @Override
    public Customer findByEmail(String email) {
        String sql = "SELECT * FROM customer WHERE is_available = true AND email = ?";
        try {
            Customer customer = this.jdbcTemplate.queryForObject(sql, new Object[]{email}, new CustomerMapper());
            return customer;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Customer findByTelephone(String telephone) {
        String sql = "SELECT * FROM customer WHERE is_available = true AND telephone = ?";
        try {
            Customer customer = this.jdbcTemplate.queryForObject(sql, new Object[]{telephone}, new CustomerMapper());
            return customer;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Customer updateCustomer(Customer updatedCustomer) {
        String sql = "UPDATE  customer SET first_name = ?, last_name = ?," +
                " email = ?, telephone = ? WHERE is_available = true AND uuid = ?";

        Object[] sqlParameters = { updatedCustomer.getFirstName(), updatedCustomer.getLastName(),
                updatedCustomer.getEmail(), updatedCustomer.getTelephone(), updatedCustomer.getUuid() };

        try {
            this.jdbcTemplate.update(sql, sqlParameters);
            return updatedCustomer;
        } catch (DataAccessException ex) {
            throw BadRequestException.create(ex.getMessage());
        }
    }

    @Override
    public String deleteCustomer(String uuid) {
        String sql = "UPDATE customer SET is_available = false WHERE uuid = ?";

        try{
            this.jdbcTemplate.update(sql, uuid);
            return uuid;
        } catch (DataAccessException ex){
            throw BadRequestException.create(ex.getMessage());
        }
    }

    private Integer count(){
        String sql = "SELECT COUNT(*) FROM customer WHERE is_available = true";
        return this.jdbcTemplate.queryForObject(sql, new Object[]{}, Integer.class);
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
