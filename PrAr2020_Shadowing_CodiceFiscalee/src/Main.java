import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.xml.stream.XMLStreamException;

import person.Person;
import xml.XMLFormatter;
import xml.XmlManager;

public class Main {
    private static final String OUTPUT_PATH = "./output/codiciPersone.xml";

    public static void main(String[] args) {
        ArrayList<Person> people = XmlManager.readPeople();

        XmlManager.writePeople(people, OUTPUT_PATH);

        try {
            XMLFormatter.format(OUTPUT_PATH);
        } catch (FileNotFoundException | XMLStreamException e) {
            e.printStackTrace();
        }
    }
}