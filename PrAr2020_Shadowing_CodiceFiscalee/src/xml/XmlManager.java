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

import org.xml.sax.XMLFilter;

import person.Person;

public class XmlManager {

	private static HashMap<String, String> towns = new HashMap<String, String>();

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
		try {
			xmlr = xmlif.createXMLStreamReader("./Input Esercizio 3.2.1 (1)/inputPersone.xml",
					new FileInputStream("./Input Esercizio 3.2.1 (1)/inputPersone.xml"));
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
					Person person = new Person(name, surname, gender, birthDate, birthPlace);
					people.add(person);
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
		try {
			xmlr = xmlif.createXMLStreamReader("./Input Esercizio 3.2.1 (1)/comuni.xml",
					new FileInputStream("./Input Esercizio 3.2.1 (1)/comuni.xml"));
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

	/**
	 * write the output "codiciPersone.xml" file
	 * 
	 * @author Alessandra
	 * @param people
	 */
	public static void writePeople(ArrayList<Person> people) {
		XMLOutputFactory xmlof = null;
		XMLStreamWriter xmlw = null;
		try {
			xmlof = XMLOutputFactory.newInstance();
			xmlw = xmlof.createXMLStreamWriter(new FileOutputStream("./Output/codiciPersone.xml"), "utf-8");
			xmlw.writeStartDocument("utf-8", "1.0");
		} catch (Exception e) {
			System.out.println("Errore nell'inizializzazione del writer:");
			System.out.println(e.getMessage());
		}
		try {
			xmlw.writeStartElement("output");
			xmlw.writeAttribute("persone numero", Integer.toString(people.size()));
			for (Person p : people) {
				xmlw.writeStartElement("persona");
				xmlw.writeAttribute("id", Integer.toString(p.getId()));
				xmlw.writeStartElement("nome");
				xmlw.writeCharacters(p.getName());
				xmlw.writeEndElement();
				xmlw.writeStartElement("cognome");
				xmlw.writeCharacters(p.getSurname());
				xmlw.writeEndElement();
				xmlw.writeStartElement("sesso");
				xmlw.writeCharacters(p.getGender());
				xmlw.writeEndElement();
				xmlw.writeStartElement("comune_nascita");
				xmlw.writeCharacters(p.getBirth_place().toString());
				xmlw.writeEndElement();
				xmlw.writeStartElement("data_nascita");
				xmlw.writeCharacters(p.getBirth_date());
				xmlw.writeEndElement();
				xmlw.writeEndElement();
				// gestione del codice fiscale
				// xmlw.writeStartElement("codice_fiscale");
				// xmlw.writeCharacters(compareTaxcode(p.getTax_code()));
				// xmlw.writeEndElement();
			}
		} catch (XMLStreamException e) {
			e.printStackTrace();
			//System.out.println("not written");
		} finally {
			// documento finisce con la scrittura dei vari codici invalidi e codici assenti
			// writeInvalidTaxCode(xmlw);
			// writeAbsenceTaxCode(xmlw);
			try {
				xmlw.writeEndElement();
				xmlw.writeEndDocument();
				xmlw.flush();
				xmlw.close();
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * read codiciFiscali.xml file
	 * 
	 * @author Alessandra
	 * @return an array list of tax codes
	 *
	 */
	public static ArrayList<String> readTaxCode() {
		XMLInputFactory xmlif = null;
		XMLStreamReader xmlr = null;
		ArrayList<String> taxCode = new ArrayList<String>();
		xmlif = XMLInputFactory.newInstance();
		try {
			xmlr = xmlif.createXMLStreamReader("./Input Esercizio 3.2.1 (1)/codiciFiscali.xml",
					new FileInputStream("./Input Esercizio 3.2.1 (1)/codiciFiscali.xml"));
			while (xmlr.hasNext()) {
				if (xmlr.getEventType() == XMLStreamConstants.START_ELEMENT && xmlr.getLocalName().equals("codice")) {
					xmlr.next();
					taxCode.add(xmlr.getText());
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
		return taxCode;
	}

	public static void main(String args[]) {
//		ArrayList<Person> code = new ArrayList<Person>();
//		code.addAll(readPeople());
//		for (Person s : code) {
//			System.out.println(s.toString());
//		}

		// writePeople(people);
		// System.out.println(readTaxCode());
	}

}
