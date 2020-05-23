package application;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.ProductDao;
import model.entities.Category;
import model.entities.Product;

public class Program {

	public static void main(String[] args) {
		
		
		ProductDao prodDao = DaoFactory.createProduct();
		
		System.out.println("<TESTE INSERT>");
		prodDao.insert(new Product(0, "Celular Xiomi Note 8 Pro 64gb 6 ram", 1250.0, new Category(1, null), 12));
		
		System.out.println("\nTESTE FIND ALL");
		List<Product> list = prodDao.findAll();
		list.forEach(System.out::println);
		
		System.out.println("\nTESTE UPDATE");
		prodDao.updateById(new Product(24, "Xiomi Note 8 Pro 64gb 6 ram", 1250.0, null, 12));
		
		System.out.println("\nTESTE DELETE");
		prodDao.deletebyId(23);
		
		System.out.println("\nTESTE FIND BY ID");
		System.out.println(prodDao.findById(15));
		
		System.out.println("\nTESTE FIND BY CATEGORY");
		list = prodDao.findByCategory(new Category(4, null));
		list.forEach(System.out::println);
	}
}
