package xml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import person.Person;
import tax_code_calculator.TaxCodeCalculator;

public class XmlManager {
	private static final String BASE_PATH = "./Input Esercizio 3.2.1/";
	private static final String TAX_CODES_FILE = "codiciFiscali.xml";
	private static final String PEOPLE_INFO_FILE = "inputPersone.xml";
	private static final String BIRTH_PLACES = "comuni.xml";

	private static HashMap<String, String> towns = new HashMap<String, String>();

	/**
	 * Find the value of the next attribute of a person
	 * 
	 * @param xmlr
	 * @return the attribute value
	 * @throws XMLStreamException
	 */
	private static String getNextCharacterOfElement(XMLStreamReader xmlr) throws XMLStreamException {
		String text = "";

		try {
			while (xmlr.getEventType() != XMLStreamConstants.START_ELEMENT) {
				xmlr.nextTag();
			}

			xmlr.next();
			text = xmlr.getText();
			xmlr.nextTag();

		} catch (XMLStreamException e) {
			throw e;
		}

		return text;
	}

	private static void writeElement(XMLStreamWriter xmlw, String elName, String elValue) throws XMLStreamException {
		try {
			xmlw.writeStartElement(elName);

			xmlw.writeCharacters(elValue);
			xmlw.writeEndElement();
		} catch (XMLStreamException e) {
			throw e;
		}
	}

	/**
	 * read the "InputPersone.xml" file
	 * 
	 *
	 * @return an arrayList of people
	 * 
	 */
	public static ArrayList<Person> readPeople() {
		XMLInputFactory xmlif = null;
		XMLStreamReader xmlr = null;

		xmlif = XMLInputFactory.newInstance();

		ArrayList<Person> people = new ArrayList<Person>();
		String filePath = BASE_PATH + PEOPLE_INFO_FILE;

		try {
			xmlr = xmlif.createXMLStreamReader(filePath, new FileInputStream(filePath));

			while (xmlr.hasNext()) {
				if (xmlr.getEventType() == XMLStreamConstants.START_ELEMENT && xmlr.getLocalName().equals("persona")) {
					xmlr.next();

					String name = getNextCharacterOfElement(xmlr);
					String surname = getNextCharacterOfElement(xmlr);
					String gender = getNextCharacterOfElement(xmlr);
					String birthPlace = getNextCharacterOfElement(xmlr);
					String birthDate = getNextCharacterOfElement(xmlr);

					Person person = new Person(name, surname, gender, birthDate, birthPlace);
					people.add(person);
				}
				xmlr.next();
			}
			xmlr.close();

		} catch (FileNotFoundException | XMLStreamException e) {
			e.printStackTrace();
		}

		return people;
	}

	/**
	 * look for a town code using its name
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

		String filePath = BASE_PATH + BIRTH_PLACES;

		try {
			xmlr = xmlif.createXMLStreamReader(filePath, new FileInputStream(filePath));

			while (xmlr.hasNext()) {
				if (xmlr.getEventType() == XMLStreamConstants.START_ELEMENT && xmlr.getLocalName().equals("comune")) {
					xmlr.next();

					String townName = getNextCharacterOfElement(xmlr);
					String townCode = getNextCharacterOfElement(xmlr);

					towns.put(townName, townCode);
				}
				xmlr.next();
			}
			xmlr.close();

		} catch (FileNotFoundException | XMLStreamException e) {
			e.printStackTrace();
		}
	}

	/**
	 * read codiciFiscali.xml file
	 * 
	 * @author Alessandra
	 * @return an array list of tax codes
	 *
	 */
	public static ArrayList<String> readTaxCodes() {
		XMLInputFactory xmlif = null;
		XMLStreamReader xmlr = null;

		xmlif = XMLInputFactory.newInstance();

		ArrayList<String> taxCode = new ArrayList<String>();
		String filePath = BASE_PATH + TAX_CODES_FILE;

		try {
			xmlr = xmlif.createXMLStreamReader(filePath, new FileInputStream(filePath));

			while (xmlr.hasNext()) {
				if (xmlr.getEventType() == XMLStreamConstants.START_ELEMENT && xmlr.getLocalName().equals("codice")) {
					xmlr.next();
					taxCode.add(xmlr.getText());
				}

				xmlr.next();
			}
			xmlr.close();

		} catch (FileNotFoundException | XMLStreamException e) {
			e.printStackTrace();
		}

		return taxCode;
	}

	/**
	 * write the output "codiciPersone.xml" file
	 * 
	 * @author Alessandra
	 * @param people
	 */
	public static void writePeople(ArrayList<Person> people) {
		ArrayList<String> taxCodes = readTaxCodes();
		ArrayList<String> wrongTaxCodes = new ArrayList<String>();

		for (int i = 0; i < taxCodes.size(); i++) {
			if (!TaxCodeCalculator.isValidTaxCode(taxCodes.get(i))) {
				wrongTaxCodes.add(taxCodes.remove(i));
			}
		}

		XMLOutputFactory xmlof = null;
		XMLStreamWriter xmlw = null;

		try {
			xmlof = XMLOutputFactory.newInstance();
			xmlw = xmlof.createXMLStreamWriter(new FileOutputStream("./Output/codiciPersone.xml"), "utf-8");

		} catch (Exception e) {
			System.out.println("Errore nell'inizializzazione del writer:");
			System.out.println(e.getMessage());
		}

		try {
			xmlw.writeStartDocument("utf-8", "1.0");
			xmlw.writeStartElement("output");

			xmlw.writeStartElement("persone");
			xmlw.writeAttribute("numero", Integer.toString(people.size()));
			for (Person p : people) {
				xmlw.writeStartElement("persona");
				xmlw.writeAttribute("id", Integer.toString(p.getId()));

				writeElement(xmlw, "nome", p.getName());
				writeElement(xmlw, "cognome", p.getSurname());
				writeElement(xmlw, "sesso", p.getGender());
				writeElement(xmlw, "comune_nascita", p.getBirth_place().getName_place());
				writeElement(xmlw, "data_nascita", p.getBirth_date());

				String taxCode = "ASSENTE";
				if (taxCodes.contains(p.getTax_code())) {
					taxCode = p.getTax_code();
					taxCodes.remove(taxCode);
				}
				writeElement(xmlw, "codice_fiscale", taxCode);

				xmlw.writeEndElement();
			}
			xmlw.writeEndElement();

			xmlw.writeStartElement("codici");

			xmlw.writeStartElement("invalidi");
			xmlw.writeAttribute("numero", Integer.toString(wrongTaxCodes.size()));
			for (String taxCode : wrongTaxCodes) {
				writeElement(xmlw, "codice", taxCode);
			}
			xmlw.writeEndElement();

			xmlw.writeStartElement("spaiati");
			xmlw.writeAttribute("numero", Integer.toString(taxCodes.size()));
			for (String taxCode : taxCodes) {
				writeElement(xmlw, "codice", taxCode);
			}
			xmlw.writeEndElement();

			xmlw.writeEndElement();
			xmlw.writeEndDocument();
			xmlw.flush();
			xmlw.close();

		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		// ArrayList<Person> code = new ArrayList<Person>();
		// code.addAll(readPeople());
		// for (Person s : code) {
		// System.out.println(s.toString());
		// }

		// writePeople(people);
		// System.out.println(readPeople());
		ArrayList<Person> people = new ArrayList<Person>();

		people.add(new Person("GABRIELE", "CERESARA", "M", "2000-06-16", "BRESCIA"));

		System.out.println(readTaxCodes());

		writePeople(people);
	}

}
