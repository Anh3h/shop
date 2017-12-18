package com.courge.shop.dao.implementation;

import com.courge.shop.Exception.BadRequestException;
import com.courge.shop.dao.ProductDao;
import com.courge.shop.model.Product;
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
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class JdbcProductDao implements ProductDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcCustomerDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Product createProduct(Product newProduct) {
        String sql = "INSERT INTO product(uuid, name, price, amount, is_available) " +
                "VALUES( ?, ?, ?, ?, true)";

        Object[] sqlParameters = { newProduct.getUuid(), newProduct.getName(),
                newProduct.getPrice(), newProduct.getAmount() };

        return (Product) this.executeSqlUpdate(newProduct, sql, sqlParameters);
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int offset = (pageable.getPageNumber() - 1) * pageSize;
        String sql = "SELECT * FROM product WHERE is_available = true LIMIT ?, ? ";
        try {
            return new PageImpl<>(this.jdbcTemplate.query(sql, new Object[]{offset, pageSize}, new ProductMapper()),
                    pageable, count());
        } catch (DataAccessException ex) {
            LOGGER.error(ex.getMessage());
            throw BadRequestException.create(ex.getMessage());
        }
    }

    @Override
    public Product findById(String uuid) {
        String sql = "SELECT * FROM product WHERE is_available = true AND uuid = ?";
        return this.executeSqlObjectQuery(sql,new Object[]{uuid});
    }

    @Override
    public Product findByName(String name) {
        String sql = "SELECT * FROM product WHERE is_available = true AND name = ?";
        return this.executeSqlObjectQuery(sql,new Object[]{name});
    }

    @Override
    public Product updateProduct(Product updatedProduct) {
        String sql = "UPDATE  product SET name = ?, price = ?," +
                " amount = ? WHERE is_available = true AND uuid = ?";

        Object[] sqlParameters = {updatedProduct.getName(),
                updatedProduct.getPrice(), updatedProduct.getAmount(), updatedProduct.getUuid()};

        return (Product) this.executeSqlUpdate(updatedProduct, sql,sqlParameters);
    }

    @Override
    public String deleteProduct(String uuid) {
        String sql = "UPDATE product SET is_available = false WHERE uuid = ?";
        return (String) this.executeSqlUpdate(uuid, sql, new Object[]{uuid});
    }

    private Integer count(){
        String sql = "SELECT COUNT(*) FROM product WHERE is_available = true ";
        return this.jdbcTemplate.queryForObject(sql, new Object[]{}, Integer.class);
    }

    Object executeSqlUpdate(Object returnValue, String sql, Object[] sqlParameters) {
        try {
            this.jdbcTemplate.update(sql, sqlParameters);
            return returnValue;
        } catch (DataAccessException ex) {
            LOGGER.error(ex.getMessage());
            throw BadRequestException.create(ex.getMessage());
        }
    }

    Product executeSqlObjectQuery(String sql, Object[] sqlParameters) {
        try {
            Product product = this.jdbcTemplate.queryForObject(sql, sqlParameters, new ProductMapper());
            return product;
        } catch (EmptyResultDataAccessException e) {
            LOGGER.error(e.getMessage());
            return null;
        } catch (DataAccessException ex) {
            LOGGER.error(ex.getMessage());
            throw BadRequestException.create(ex.getMessage());
        }
    }


        private static final class ProductMapper implements RowMapper<Product> {

        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            Product product = new Product();
            product.setUuid(rs.getString("uuid"));
            product.setName(rs.getString("name"));
            product.setPrice(rs.getDouble("price"));
            product.setAmount(rs.getLong("amount"));

            return product;
        }
    }

}
