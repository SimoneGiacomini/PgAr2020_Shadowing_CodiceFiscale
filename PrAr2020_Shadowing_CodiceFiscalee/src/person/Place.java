package person;

public class Place {

	private String code_place;
	private String name_place;

	public Place(String code_place, String name_place) {
		this.code_place = code_place;
		this.name_place = name_place;
	}

	public String getCode_place() {
		return code_place;
	}

	public void setCode_place(String code_place) {
		this.code_place = code_place;
	}

	public String getName_place() {
		return name_place;
	}

	public void setName_place(String name_place) {
		this.name_place = name_place;
	}

	public boolean equals(Place another_place) {
		return code_place.equalsIgnoreCase(another_place.getCode_place())
				&& name_place.equalsIgnoreCase(another_place.getName_place());
	}
	public String toString() {
		return String.format("%s; %s; ", code_place,name_place);
	}
}
