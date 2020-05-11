package person;

import java.util.Calendar;
import java.util.Date;

public class Person {

	private int id;
	private String name;
	private String surname;
	private Gender gender;
	private Date birth_date;
	private Place brith_place;
	private String tax_code;
	
	private static int progressivo=0;
	public Person( String name, String surname, Gender gender, Date birth_date, Place brith_place,
			String tax_code)
	{
		this.id=progressivo;
		progressivo++;
		this.name = name;
		this.surname = surname;
		this.gender = gender;
		this.birth_date = birth_date;
		this.brith_place = brith_place;
		this.tax_code = tax_code;
	}
	
	public Person( String name, String surname,Character gender, int birth_day, int birth_month, int birth_year, String birth_town) {
		
		
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	public Date getBirth_date() {
		return birth_date;
	}
	public void setBirth_date(Date birth_date) {
		this.birth_date = birth_date;
	}
	public Place getBrith_place() {
		return brith_place;
	}
	public void setBrith_place(Place brith_place) {
		this.brith_place = brith_place;
	}
	public String getTax_code() {
		return tax_code;
	}
	public void setTax_code(String tax_code) {
		this.tax_code = tax_code;
	}
	
}
