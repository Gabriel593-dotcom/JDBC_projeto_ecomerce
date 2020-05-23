package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import db.DB;
import dbexceptions.DBException;
import model.dao.ProductDao;
import model.entities.Category;
import model.entities.Product;

public class ProductDaoJDBC implements ProductDao {

	private Connection conn;
	private Integer row;

	public ProductDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Product prod) {
		PreparedStatement st = null;

		try {

			conn.setAutoCommit(false);

			st = conn.prepareStatement(
					"INSERT INTO products " + "(name, price, id_category, quantity) " + "VALUES " + "(?, ?, ?, ?)");

			st.setString(1, prod.getName());
			st.setDouble(2, prod.getPrice());
			st.setInt(3, prod.getCategory().getId());
			st.setInt(4, prod.getQuantity());

			List<Product> filteredProduct = findAll().stream().filter(p -> p.equals(prod)).collect(Collectors.toList());

			if (filteredProduct.isEmpty()) {
				row = st.executeUpdate();
				System.out.println("rows affected: " + row);
			}

			else {
				System.out.println("Product is already registered in the database.");
			}

			conn.commit();
		}

		catch (

		SQLException e) {

			try {
				conn.rollback();
				throw new DBException(e.getMessage());
			}

			catch (SQLException e1) {
				throw new DBException("Error in try to rollback. Caused by: " + e.getMessage());
			}

		}

		finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void updateById(Product prod) {
		PreparedStatement st = null;

		try {

			conn.setAutoCommit(false);

			st = conn.prepareStatement("UPDATE products " + "SET name = ?, price = ?, quantity = ? " + "WHERE id = ?");

			st.setString(1, prod.getName());
			st.setDouble(2, prod.getPrice());
			st.setInt(3, prod.getQuantity());
			st.setInt(4, prod.getId());

			row = st.executeUpdate();
			System.out.println("rows affected: " + row);

			conn.commit();
		}

		catch (SQLException e) {

			try {
				conn.rollback();
				throw new DBException(e.getMessage());
			}

			catch (SQLException e1) {

				throw new DBException("Error in try to rollback. Caused by: " + e.getMessage());
			}

		}

		finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void deletebyId(Integer id) {

		PreparedStatement st = null;

		try {
			conn.setAutoCommit(false);

			st = conn.prepareStatement("DELETE FROM products WHERE id = ?");
			st.setInt(1, id);
			row = st.executeUpdate();
			System.out.println("rows affected: " + row);
			conn.commit();
		}

		catch (SQLException e) {
			try {
				conn.rollback();
				throw new DBException(e.getMessage());
			}

			catch (SQLException e1) {

				throw new DBException("Error in try to rollback. Caused by: " + e.getMessage());
			}

		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public Product findById(Integer id) {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("SELECT products.*, category.name " + "FROM products JOIN category "
					+ "ON products.id_category = category.id WHERE products.id = ?");

			st.setInt(1, id);
			rs = st.executeQuery();
			Product prod = null;

			if (rs.next()) {
				prod = new Product(rs.getInt("id"), rs.getString("name"), rs.getDouble("price"),
						new Category(rs.getInt("id_category"), rs.getString("category.name")), rs.getInt("quantity"));
			}

			return prod;
		}

		catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

	@Override
	public List<Product> findAll() {

		PreparedStatement st = null;
		ResultSet rs = null;
		List<Product> list = new ArrayList<>();

		try {

			conn.setAutoCommit(false);

			st = conn.prepareStatement("SELECT products.*, category.name " + "FROM products JOIN category "
					+ "ON products.id_category = category.id " + "ORDER BY products.name");

			rs = st.executeQuery();

			while (rs.next()) {
				list.add(new Product(rs.getInt("id"), rs.getString("name"), rs.getDouble("price"),
						new Category(rs.getInt("id_category"), rs.getString("category.name")), rs.getInt("quantity")));
			}

			return list;
		}

		catch (SQLException e) {
			throw new DBException(e.getMessage());
		}
	}

	@Override
	public List<Product> findByCategory(Category cat) {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {

			st = conn.prepareStatement("SELECT products.*, category.name " + "FROM products INNER JOIN category "
					+ "ON products.id_category = category.id " + "WHERE category.id = ?");
			st.setInt(1, cat.getId());

			rs = st.executeQuery();

			List<Product> list = new ArrayList<>();

			while (rs.next()) {
				list.add(new Product(rs.getInt("id"), rs.getString("name"), rs.getDouble("price"),
						new Category(rs.getInt("id_category"), rs.getString("category.name")), rs.getInt("quantity")));
			}

			return list;

		}

		catch (SQLException e) {
			throw new DBException(e.getMessage());
		}

		finally {
			DB.closeResultSet(rs);
			DB.closeResultSet(rs);
		}
	}

}
