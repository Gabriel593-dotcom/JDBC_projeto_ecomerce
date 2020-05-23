package model.dao;

import model.dao.impl.ProductDaoJDBC;

import db.DB;

public class DaoFactory {

	
	public static ProductDao createProduct() {
		return new ProductDaoJDBC(DB.getConnection());  
	}
}
