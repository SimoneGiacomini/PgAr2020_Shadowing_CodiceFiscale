package person;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import tax_code_calculator.TaxCodeCalculator;

public class Person {

	public static enum Gender {
		F, M

	}

	private int id;
	private String name;
	private String surname;
	private Gender gender;
	private LocalDate birth_date;
	private Place birth_place;
	private String tax_code;

	private static int progressivo = 0;

	public Person(String name, String surname, String gender, String birth_date, String birth_town) {
		setGender(gender.trim());
		setName(name.trim());
		setSurname(surname.trim());
		setBirth_date(LocalDate.parse(birth_date.trim()));
		setBirth_place(birth_town.trim());
		newID();
		setTax_code();
	}

	public Person(String name, String surname, Character gender, int birth_day, int birth_month, int birth_year,
			String birth_town) {
		this(name, surname, gender.toString(), String.format("%04d-%02d-%02d", birth_year, birth_month, birth_day),
				birth_town);
	}

	public Person(String name, String surname, String gender, int birth_day, int birth_month, int birth_year,
			String birth_town) {
		this(name, surname, gender, String.format("%04d-%02d-%02d", birth_year, birth_month, birth_day), birth_town);
	}

	public Person(String name, String surname, Character gender, String birth_date, String birth_town) {
		this(name, surname, gender.toString(), birth_date, birth_town);
	}

	public int getId() {
		return id;
	}

	private void newID() {
		this.id = progressivo;
		progressivo++;
	}

	public String getName() {
		return name;
	}

	private void setName(String name) throws IllegalArgumentException {
		if (name.length() < 3)
			throw new IllegalArgumentException("Name entered too short");
		this.name = name.toUpperCase();
	}

	public String getSurname() {
		return surname;
	}

	private void setSurname(String surname) throws IllegalArgumentException {
		if (surname.length() < 2)
			throw new IllegalArgumentException("Surname entered too short");
		this.surname = surname.toUpperCase();
	}

	public String getGender() {
		return gender.toString();
	}

	private void setGender(String gender) throws IllegalArgumentException {
		gender = gender.toUpperCase();
		if (!(gender.equals(Gender.F.toString()) || gender.equals(Gender.M.toString()))) {
			throw new IllegalArgumentException(
					"Gender must be \"" + Gender.M.toString() + "\", or \"" + Gender.F.toString() + "\"");
		}
		this.gender = Gender.valueOf(gender.toString());
	}

	public LocalDate getBirth_date() {
		return birth_date;
	}

	private void setBirth_date(LocalDate birth_date) throws IllegalArgumentException {
		if (birth_date.isAfter(LocalDate.now()) || birth_date.isBefore(LocalDate.ofYearDay(1900, 1))) {
			throw new IllegalArgumentException("ERROR ON DATE");
		}
		this.birth_date = birth_date;
	}

	public Place getBirth_place() {
		return birth_place;
	}

	private void setBirth_place(String birth_place) {
		this.birth_place = new Place(birth_place.toUpperCase());
	}

	public String getTax_code() {
		return tax_code;
	}

	private void setTax_code() {
		this.tax_code = TaxCodeCalculator.taxCodeCalculator(this);
	}

	public String toString() {
		return String.format("%s;%s;%s;%s;%s", name, surname, getGender(), getBirth_date(),
				getBirth_place().getName_place());
	}

	public boolean isFemale() {
		return this.gender.equals(Gender.F);
	}

	public static void main(String args[]) {
						      //nome  //cognome
		Person p = new Person("maria", "bi", "f", "2000-02-02", "BRESCIA");
		System.out.println(p.getTax_code());
	}
}
