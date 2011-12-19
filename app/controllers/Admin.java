package controllers;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import models.User;
import net.sf.oval.constraint.Future;
import play.data.validation.InFuture;
import play.data.validation.Required;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

import com.orange.common.mongodb.MongoDBClient;
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
		List<Product> products = ProductManager.getAllProductsWithType(
				mongoClient, DBConstants.C_PRODUCT_TYPE_AD, true, 0,
				Integer.MAX_VALUE);
		render(products);
	}

	public static void form(String id) {
		Product product = new Product();
		product.setStartDate(getStartDateDefault());
		product.setEndDate(getEndDateDefault());
		if (id != null) {
			 product = ProductManager.findProductById(mongoClient, id);
		}
		render(product);
	}

	public static void save(String id, @Required String title,
			@Required String loc, @Required String image,
			@Required Date startDate, @Required Date endDate,
			@Required String siteName, @Required String siteUrl) {
		Product product = null;
		if (id != null) {
			product = ProductManager.findProductById(mongoClient, id);
		} else {
			product = new Product();
			product.setTitle(title);
			product.setLoc(loc);
			product.setImage(image);
			product.setStartDate(startDate);
			product.setEndDate(endDate);
			product.setSiteName(siteName);
			product.setSiteUrl(siteUrl);
		}

		//TODO:
		if(endDate.before(startDate)){
			validation.addError("endDate", "end date should be later than start date");
		}
		//validate
		if (validation.hasErrors()) {
			render("@form", product);
		}
		double price = DBConstants.C_PRICE_NA;
		int type = DBConstants.C_PRODUCT_TYPE_AD;
		// TODO:
		String siteId = "DUMMY_SITEID";

		boolean validate = product.setMandantoryFields(
				DBConstants.C_NATIONWIDE, loc, image, title, startDate,
				endDate, price, price, 0, siteId, siteName, siteUrl);
		if (validate) {
			product.setProductType(type);

			if (id != null) {
				ProductManager.save(mongoClient, product);
			} else {
				ProductManager.createProduct(mongoClient, product);
			}
			index();
		}else{			
			// TODO: what if failed.
		}
	}

	private static Date getEndDateDefault() {
		Date endDate;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 30);
		endDate = cal.getTime();
		return endDate;
	}

	private static Date getStartDateDefault() {
		Date startDate;
		startDate = new Date();
		return startDate;
	}
}
