package controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.User;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

import com.orange.common.mongodb.MongoDBClient;
import com.orange.common.utils.DateUtil;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.dao.Product;
import com.orange.groupbuy.manager.ProductManager;

@With(Secure.class)
public class Admin extends Controller {

	private static final MongoDBClient mongoClient = new MongoDBClient(
			DBConstants.D_GROUPBUY);

	@Before
	static void setConnectedUser() {
		if (Security.isConnected()) {
			User user = User.find("byEmail", Security.connected()).first();
			renderArgs.put("user", user.fullname);
		}
	}

	public static void index() {
		// List<String> categorys = ProductManager.getAllCategoryNames();
		List<Product> products = new ArrayList<Product>();
		String city = "";
		String category = String.valueOf(DBConstants.C_CATEGORY_UNKNOWN);
		String maxCount = String.valueOf(Integer.MAX_VALUE);
		// for (String category : categorys) {
		List<Product> p = ProductManager.getAllProductsByCategory(mongoClient,
				city, category, "0", maxCount);
		products.addAll(p);
		// }
		render(products);
	}

	public static void form(String id) {
		if (id != null) {
			Product product = ProductManager.findProductById(mongoClient, id);
			render(product);
		}
		render();
	}

	public static void save(String id, String title, String loc, String image,
			Date startDate, Date endDate, String siteName, String siteURL) {
		Product product3 = null;
		if (id != null) {
			product3 = ProductManager.findProductById(mongoClient, id);
		} else {
			product3 = new Product();
		}

		double price = DBConstants.C_PRICE_NA;
		int type = DBConstants.C_PRODUCT_TYPE_AD;
		String siteId = "";
		product3.setMandantoryFields(DBConstants.C_NATIONWIDE, loc, image,
				title, startDate, endDate, price, price, 0, siteId, siteName,
				siteURL);
		product3.setProductType(type);

		if (id != null) {
			ProductManager.save(mongoClient, product3);
		} else {
			ProductManager.createProduct(mongoClient, product3);
		}
		index();
	}
}
