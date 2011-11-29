package models;

import java.util.Date;

import javax.persistence.Entity;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Product extends Model {

	@Required
	public String city;

	@Required
	public String loc;

	@Required
	public String image;

	@Required
	public String title;

	@Required
	public Date startDate;

	@Required
	public Date endDate;

	@Required
	public double price;

	@Required
	public double value;

	@Required
	public int bought;

	@Required
	public String siteId;

	@Required
	public String siteName;

	@Required
	public String siteURL;

	public Product(String city, String loc, String image, String title,
			Date startDate, Date endDate, double price, double value,
			int bought, String siteId, String siteName, String siteURL) {
		super();
		this.city = city;
		this.loc = loc;
		this.image = image;
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.price = price;
		this.value = value;
		this.bought = bought;
		this.siteId = siteId;
		this.siteName = siteName;
		this.siteURL = siteURL;
	}

	@Override
	public String toString() {
		return title;
	}
}
