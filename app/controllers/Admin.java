package controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.User;
import play.data.validation.Required;
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
		// for (String category : categorys) {
		List<Product> products = ProductManager.getAllProductsWithType(
				mongoClient, true, DBConstants.C_PRODUCT_TYPE_AD, 0,
				Integer.MAX_VALUE);
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

	public static void save(String id, @Required String title,
			@Required String loc, @Required String image,
			@Required Date startDate, @Required Date endDate,
			@Required String siteName, @Required String siteUrl) {
		Product product3 = null;
		if (id != null) {
			product3 = ProductManager.findProductById(mongoClient, id);
		} else {
			product3 = new Product();
		}

		double price = DBConstants.C_PRICE_NA;
		int type = DBConstants.C_PRODUCT_TYPE_AD;
		// TODO:
		String siteId = "DUMMY_SITEID";

		boolean validate = product3.setMandantoryFields(
				DBConstants.C_NATIONWIDE, loc, image, title, startDate,
				endDate, price, price, 0, siteId, siteName, siteUrl);
		// TODO: validate before
		if (validate) {
			product3.setProductType(type);

			if (id != null) {
				ProductManager.save(mongoClient, product3);
			} else {
				ProductManager.createProduct(mongoClient, product3);
			}
			index();
		}
	}
}
