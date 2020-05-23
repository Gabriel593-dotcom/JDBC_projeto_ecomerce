package model.dao;

import java.util.List;

import model.entities.Category;
import model.entities.Product;

public interface ProductDao {
	
	void insert(Product prod);
	void updateById(Product prod);
	void deletebyId(Integer id);
	Product findById(Integer id);
	List<Product> findAll();
	List<Product> findByCategory(Category cat);
}
