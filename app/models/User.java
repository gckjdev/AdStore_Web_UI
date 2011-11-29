package models;

import javax.persistence.Entity;

import play.data.validation.Required;
import play.db.jpa.Model;
import play.data.validation.*;

@SuppressWarnings("serial")
@Entity
public class User extends Model {
	@Required
	@Email
    public String email;
    public String password;
	@Required
	@MinSize(8)
    public String fullname;
    
    public User(String email, String password, String fullname) {
        this.email = email;
        this.password = password;
		this.fullname = fullname;
    }

	public static User connect(String email, String password) {
		return find("byEmailAndPassword", email, password).first();
	}
 
	@Override
	public String toString() {
		return email;
	}
}