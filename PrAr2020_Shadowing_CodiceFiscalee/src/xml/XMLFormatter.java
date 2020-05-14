package xml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/**
 * LAVORI IN CORSO!!!11!
 */
public class XMLFormatter {
    private static void writeTabs(XMLStreamWriter xmlw, int howMuch) throws XMLStreamException {
        try {
            for (int i = 0; i < howMuch; i++) {
                xmlw.writeCharacters("\t");
            }

        } catch (XMLStreamException e) {
            throw e;
        }
    }

    public static void format(String filePath) throws XMLStreamException, FileNotFoundException {
        XMLInputFactory xmlif = XMLInputFactory.newInstance();
        XMLStreamReader xmlr = null;

        XMLOutputFactory xmlof = XMLOutputFactory.newInstance();
        XMLStreamWriter xmlw = null;

        int numberOfTabs = 0;
        int lastEvent = XMLStreamConstants.START_DOCUMENT;

        try {
            xmlr = xmlif.createXMLStreamReader(filePath, new FileInputStream(filePath));
            xmlw = xmlof.createXMLStreamWriter(new FileOutputStream(filePath), "utf-8");

            while (xmlr.hasNext()) {
                switch (xmlr.getEventType()) {
                    case XMLStreamConstants.START_DOCUMENT:
                        xmlw.writeStartDocument(xmlr.getEncoding(), xmlr.getVersion());
                        xmlw.writeCharacters("\n");
                        break;
                }

                xmlr.next();
            }

        } catch (XMLStreamException | FileNotFoundException e) {
            throw e;
        }
    }
}