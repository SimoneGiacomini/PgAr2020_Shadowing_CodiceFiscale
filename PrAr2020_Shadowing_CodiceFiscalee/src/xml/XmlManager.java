package xml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import person.Person;

public class XmlManager {

	private static HashMap<String, String> towns = new HashMap<String, String>();

	/**
	 * read the InputPersone.xml file 
	 * 
	 *
	 * @return an arrayList of people
	 */
	public static ArrayList<Person> readPeople() {
		XMLInputFactory xmlif = null;
		XMLStreamReader xmlr = null;
		xmlif = XMLInputFactory.newInstance();
		ArrayList<Person> people = new ArrayList<Person>();
		try {
			xmlr = xmlif.createXMLStreamReader("./Input Esercizio 3.2.1/InputPersone.xml",
					new FileInputStream("./Input Esercizio 3.2.1/InputPersone.xml"));
			while (xmlr.hasNext()) {
				if (xmlr.getEventType() == XMLStreamConstants.START_ELEMENT && xmlr.getLocalName().equals("persona")) {
					xmlr.nextTag();
					xmlr.next();
					String name = xmlr.getText();
					xmlr.nextTag();
					xmlr.nextTag();
					xmlr.next();
					String surname = xmlr.getText();
					xmlr.nextTag();
					xmlr.nextTag();
					xmlr.next();
					String gender = xmlr.getText();
					xmlr.nextTag();
					xmlr.nextTag();
					xmlr.next();
					String birthPlace = xmlr.getText();
					xmlr.nextTag();
					xmlr.nextTag();
					xmlr.next();
					String birthDate = xmlr.getText();
					// Person person =new Person();
					// String taxCode=calcTaxCode(person);
					// people.add(person);
				}
				xmlr.next();
			}
		} catch (FileNotFoundException | XMLStreamException e) {
			e.printStackTrace();
		}
		try {
			xmlr.close();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		return people;
	}

	/**
	 * look for a town code using
	 * 
	 * @param townName
	 * @return the townCode
	 */
	public static String searchBirthPlace(String townName) {
		if (towns.isEmpty()) {
			readBirthPlace();
		}
		return towns.get(townName);
	}

	/**
	 * read the comuni.xml file
	 *
	 *
	 * @return
	 */
	private static void readBirthPlace() {
		XMLInputFactory xmlif = null;
		XMLStreamReader xmlr = null;
		xmlif = XMLInputFactory.newInstance();
		try {
			xmlr = xmlif.createXMLStreamReader("./Input Esercizio 3.2.1/comuni.xml",
					new FileInputStream("./Input Esercizio 3.2.1/comuni.xml"));
			while (xmlr.hasNext()) {
				if (xmlr.getEventType() == XMLStreamConstants.START_ELEMENT && xmlr.getLocalName().equals("comune")) {
					xmlr.nextTag();
					xmlr.next();
					String townName = xmlr.getText();
					xmlr.nextTag();
					xmlr.nextTag();
					xmlr.next();
					String townCode = xmlr.getText();
					towns.put(townName, townCode);
				}
				xmlr.next();
			}
		} catch (FileNotFoundException | XMLStreamException e) {
			e.printStackTrace();
		} finally {
			try {
				xmlr.close();
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
		}

	}

//	public static void main(String args[]) {
//		//readPeople();
//
//	}
}
