/** Adapted from https://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/ */

package tunecomposer;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class TuneParser {

    /**
     * Takes in file and reads from it; gets all children and adds to
     * BuildPlayables.
     * 
     * @param file
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static void parseFile(File file) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);

        Element root = doc.getDocumentElement();
        // TODO: make sure root has tagname "Composition"

        NodeList allChildren = root.getChildNodes();

        buildPlayables(allChildren);
    }

    /**
     * Parses string input, adds to buildPlayables.
     * 
     * @param str
     */
    public static void parseString(String str) {
        Document doc = StringToXMLConverter.convertStringToDocument(str);

        Element root = doc.getDocumentElement();
        // TODO: make sure root has tagname "Composition"

        NodeList allChildren = root.getChildNodes();

        buildPlayables(allChildren);
    }

    /**
     * Takes in a NodeList of all nodes and adds to allPlayables.
     * 
     * @param children
     */
    private static void buildPlayables(NodeList children) {
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            String name = child.getNodeName();
            NamedNodeMap attributes = child.getAttributes();
            NodeList innerChildren = child.getChildNodes();

            if (name == "Gesture") {
                NoteHandler.allPlayables.add(addGesture(attributes, innerChildren));
            } else if (name == "Note") {
                NoteHandler.allPlayables.add(addNote(attributes, innerChildren));
            }
        }
    }

    private static Gesture addGesture(NamedNodeMap attributes, NodeList children) {
        Set<Playable> gesturePlayables = new HashSet<Playable>();
        MoveableRect outerRect = null;
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            String name = child.getNodeName();
            NamedNodeMap attr = child.getAttributes();
            NodeList innerChildren = child.getChildNodes();

            if (name == "Gesture") {
                gesturePlayables.add(addGesture(attr, innerChildren));
            } else if (name == "Note") {
                gesturePlayables.add(addNote(attr, innerChildren));
            } else if (name == "MoveableRect") {
                outerRect = createRect(child);
            }
        }
        return new Gesture(Boolean.parseBoolean(attributes.getNamedItem("isSelected").getNodeValue()),
                Double.parseDouble(attributes.getNamedItem("margin").getNodeValue()), gesturePlayables, outerRect);
    }

    private static Note addNote(NamedNodeMap attributes, NodeList children) {
        return new Note(Integer.parseInt(attributes.getNamedItem("pitch").getNodeValue()),
                Integer.parseInt(attributes.getNamedItem("startTime").getNodeValue()),
                Instrument.getInstrument(attributes.getNamedItem("instrument").getNodeValue()),
                Boolean.parseBoolean(attributes.getNamedItem("isSelected").getNodeValue()),
                createRect(children.item(0)));
    }

    private static MoveableRect createRect(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        return new MoveableRect(Double.parseDouble(attributes.getNamedItem("x_coord").getNodeValue()),
                Double.parseDouble(attributes.getNamedItem("y_coord").getNodeValue()),
                Double.parseDouble(attributes.getNamedItem("rectWidth").getNodeValue()),
                Double.parseDouble(attributes.getNamedItem("rectHeight").getNodeValue()));
    }
}