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
 * Formats and rewrites in a human-readable way the xml file
 * 
 * @author Gabriele
 */
public class XMLFormatter {
    /**
     * Write on the documet a specified number of tabs
     * 
     * @author Gabriele
     * @param xmlw    the writer
     * @param howMuch the number of tabs to write
     * @throws XMLStreamException
     */
    private static void writeTabs(XMLStreamWriter xmlw, int howMuch) throws XMLStreamException {
        try {
            for (int i = 0; i < howMuch; i++) {
                xmlw.writeCharacters("\t");
            }

        } catch (XMLStreamException e) {
            throw e;
        }
    }

    /**
     * Rewrites the specified xml file
     * 
     * @author Gabriele
     * @param filePath the path of the file to rewrite
     * @throws XMLStreamException
     * @throws FileNotFoundException
     */
    public static void format(String filePath) throws XMLStreamException, FileNotFoundException {
        // Initializes the reader
        XMLInputFactory xmlif = XMLInputFactory.newInstance();
        XMLStreamReader xmlr = null;

        // Initializes the writer
        XMLOutputFactory xmlof = XMLOutputFactory.newInstance();
        XMLStreamWriter xmlw = null;

        // Keeps track of the "level" on which the code is for identating
        int numberOfTabs = 0;
        int lastEvent = XMLStreamConstants.START_DOCUMENT;

        String newFilePath = filePath.replace(".xml", "Formatted.xml");

        try {
            xmlr = xmlif.createXMLStreamReader(filePath, new FileInputStream(filePath));
            xmlw = xmlof.createXMLStreamWriter(new FileOutputStream(newFilePath), "utf-8");

            /**
             * Iterates all the elements of the file to rewrite and rewrites them in a
             * prettier way
             */
            while (xmlr.hasNext()) {
                switch (xmlr.getEventType()) {
                    case XMLStreamConstants.START_DOCUMENT:
                        xmlw.writeStartDocument(xmlr.getEncoding(), xmlr.getVersion());
                        xmlw.writeCharacters("\n");
                        break;

                    case XMLStreamConstants.START_ELEMENT:
                        /**
                         * If the previous element was a START_ELEMENT go to a new line and increments
                         * the number of tabs to write
                         */
                        if (lastEvent == XMLStreamConstants.START_ELEMENT) {
                            numberOfTabs++;
                            xmlw.writeCharacters("\n");
                        }

                        // If the previous element was a END_ELEMENT go to a new line
                        if (lastEvent == XMLStreamConstants.END_ELEMENT) {
                            xmlw.writeCharacters("\n");
                        }

                        writeTabs(xmlw, numberOfTabs);

                        // Writes START_ELEMENT whit its ATTRIBUTEs
                        xmlw.writeStartElement(xmlr.getLocalName());
                        for (int i = 0; i < xmlr.getAttributeCount(); i++) {
                            xmlw.writeAttribute(xmlr.getAttributeLocalName(i), xmlr.getAttributeValue(i));
                        }

                        lastEvent = XMLStreamConstants.START_ELEMENT;
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        /**
                         * If the previous element was a END_ELEMENT go to a new line and decrements the
                         * number of tabs to write
                         */
                        if (lastEvent == XMLStreamConstants.END_ELEMENT) {
                            numberOfTabs--;
                            xmlw.writeCharacters("\n");
                            writeTabs(xmlw, numberOfTabs);
                        }

                        xmlw.writeEndElement();

                        lastEvent = XMLStreamConstants.END_ELEMENT;
                        break;

                    case XMLStreamConstants.CHARACTERS:
                        // Check if the CHARACTER is not empty
                        if (xmlr.getText().trim().length() > 0) {
                            xmlw.writeCharacters(xmlr.getText());
                        }

                        lastEvent = XMLStreamConstants.CHARACTERS;
                        break;

                    case XMLStreamConstants.END_DOCUMENT:
                        xmlw.writeEndDocument();
                        break;
                }

                xmlr.next();
            }

            // Save and close
            xmlw.flush();
            xmlw.close();
            xmlr.close();

        } catch (XMLStreamException | FileNotFoundException e) {
            throw e;
        }
    }
}