package sample.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class ConnectHandler {
    private static String sessionID;
    private static String CUUID;
    static String URL;
    static String contentType;
    static String method;


    private static void login() throws ParserConfigurationException, TransformerException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document req = builder.newDocument();

        //Скрыто

    }

    public static void logout() throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document req = builder.newDocument();

        //Скрыто

        request(convertToString(req));
    }

    public static void connect(Crate crate) throws ParserConfigurationException, IOException, SAXException, TransformerException, InterruptedException {
        login();
        int CUslot = CUSlot();
        CUUID = getDeviceList().get(CUslot);
        if (CUslot != crate.getSlotAmount() + 1) {
            setParam(CUUID, "VType", crate.getCode());
            TimeUnit.SECONDS.sleep(40);
        }
    }

    private static int CUSlot() throws TransformerException, ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document req = builder.newDocument();

        //Скрыто

        return Integer.parseInt(convertToDocument(request(convertToString(req))).getElementsByTagName("CU_SLOT").item(0).getTextContent());
    }

    public static Map<Integer, String> getDeviceList() throws ParserConfigurationException, TransformerException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document req = builder.newDocument();

        //Скрыто

        Map<Integer, String> result = new TreeMap<>();
        NodeList devices = convertToDocument(request(convertToString(req))).getElementsByTagName("DEVICE");
        for (int i = 0; i < devices.getLength(); i++) {
            Element el = (Element) devices.item(i);

            //Скрыто

        }
        return result;
    }

    public static String CUget(String param) throws TransformerException, ParserConfigurationException, IOException, SAXException {
        return getParam(CUUID, param);
    }

    private static String getParam(String UID, String param) throws TransformerException, ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document req = builder.newDocument();

        //Скрыто

        return convertToDocument(request(convertToString(req))).getElementsByTagName("VALUE").item(0).getTextContent();
    }

    public static void CUset(String param, String value) throws TransformerException, ParserConfigurationException, IOException, SAXException {
        setParam(CUUID, param, value);
    }

    private static void setParam(String UID, String param, String value) throws TransformerException, ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document req = builder.newDocument();

        //Скрыто

        request(convertToString(req));
    }

    private static String request(String request) {
        PrintWriter writer = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);

            //Скрыто

            writer.flush();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static Document convertToDocument(String string) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(string)));
    }

    private static String convertToString(Document document) throws TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        StringWriter stringWriter = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
        return stringWriter.getBuffer().toString();
    }
}
