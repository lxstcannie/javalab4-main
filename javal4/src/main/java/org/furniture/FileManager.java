package org.furniture;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.furniture.utils.ConcreteFurniture;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class FileManager {

    // Чтение данных из TXT файла
    public static List<Furniture> readFromTxt(String filename) {
        List<Furniture> furnitures = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0].trim());
                String type = parts[1].trim();
                int weight = Integer.parseInt(parts[2].trim());
                int amount = Integer.parseInt(parts[3].trim());
                double price = Double.parseDouble(parts[4].trim());
                furnitures.add(new ConcreteFurniture(id, type, weight, amount, price));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return furnitures;
    }

    // Запись в TXT файл
    public static void writeToTxt(String filename, List<Furniture> furnitures) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Furniture furniture : furnitures) {
                writer.write(furniture.getId() + "," + furniture.getType() + "," + furniture.getWeight() + "," +
                        furniture.getAmount() + "," + furniture.getPrice() + "\n");
            }
            System.out.println("Данные успешно записаны в TXT файл.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Чтение из XML файла
    public static List<Furniture> readFromXML(String filename) {
        List<Furniture> furnitures = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(filename));

            NodeList furnitureNodes = document.getElementsByTagName("furniture");

            for (int i = 0; i < furnitureNodes.getLength(); i++) {
                Node node = furnitureNodes.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    int id = Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent());
                    String type = element.getElementsByTagName("type").item(0).getTextContent();
                    int weight = Integer.parseInt(element.getElementsByTagName("weight").item(0).getTextContent());
                    int amount = Integer.parseInt(element.getElementsByTagName("amount").item(0).getTextContent());
                    double price = Double.parseDouble(element.getElementsByTagName("price").item(0).getTextContent());

                    furnitures.add(new ConcreteFurniture(id, type, weight, amount, price));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return furnitures;
    }

    // Запись в XML файл
    public static void writeToXML(String filename, List<Furniture> furnitures) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element root = document.createElement("furnitures");
            document.appendChild(root);

            for (Furniture furniture : furnitures) {
                Element furnitureElement = document.createElement("furniture");

                Element id = document.createElement("id");
                id.appendChild(document.createTextNode(String.valueOf(furniture.getId())));
                furnitureElement.appendChild(id);

                Element type = document.createElement("type");
                type.appendChild(document.createTextNode(furniture.getType()));
                furnitureElement.appendChild(type);

                Element weight = document.createElement("weight");
                weight.appendChild(document.createTextNode(String.valueOf(furniture.getWeight())));
                furnitureElement.appendChild(weight);

                Element amount = document.createElement("amount");
                amount.appendChild(document.createTextNode(String.valueOf(furniture.getAmount())));
                furnitureElement.appendChild(amount);

                Element price = document.createElement("price");
                price.appendChild(document.createTextNode(String.valueOf(furniture.getPrice())));
                furnitureElement.appendChild(price);

                root.appendChild(furnitureElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(filename));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(domSource, streamResult);

            System.out.println("Данные успешно записаны в XML файл.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Чтение из JSON файла
    public static List<Furniture> readFromJSON(String filename) {
        List<Furniture> furnitures = new ArrayList<>();
        try (FileReader reader = new FileReader(filename)) {
            Gson gson = new Gson();
            Type furnitureListType = new TypeToken<ArrayList<ConcreteFurniture>>() {}.getType();
            furnitures = gson.fromJson(reader, furnitureListType);
        } catch (Exception e) {
        }
        return furnitures;
    }

    // Запись в JSON файл
    public static void writeToJSON(String filename, List<Furniture> furnitures) {
        try (FileWriter writer = new FileWriter(filename)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(furnitures, writer);
            System.out.println("Данные успешно записаны в JSON файл.");
        } catch (Exception e) {
        }
    }

    // Шифрование и запись данных
    public static void writeEncryptedToTxt(String filename, List<Furniture> furnitures, SecretKey secretKey) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            StringBuilder data = new StringBuilder();
            
            // Собираем все данные о мебели в строку
            for (Furniture furniture : furnitures) {
                String furnitureData = furniture.getId() + "," + furniture.getType() + "," + furniture.getWeight() + "," +
                                   furniture.getAmount() + "," + furniture.getPrice() + "\n";
                data.append(furnitureData);
            }

            // Шифруем собранные данные
            String encryptedData = EncryptionUtils.encrypt(data.toString(), secretKey);

            // Записываем зашифрованные данные в файл
            writer.write(encryptedData);
            System.out.println("Данные успешно зашифрованы и записаны в файл " + filename);
        } catch (IOException e) {
        }
    }

    // Чтение и расшифровка данных
    public static List<Furniture> readDecryptedFromTxt(String filename, SecretKey secretKey) {
        List<Furniture> furnitures = new ArrayList<>();
    
        if (secretKey == null) {
            System.out.println("Ошибка: secretKey не может быть null.");
            return furnitures; // Возврат пустого списка
        }
    
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            // Чтение зашифрованного текста из файла
            StringBuilder encryptedData = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                encryptedData.append(line);
            }
    
            if (encryptedData.length() == 0) {
                System.out.println("Файл пуст: " + filename);
                return furnitures; // Возврат пустого списка
            }
    
            // Дешифровка данных
            String decryptedData = EncryptionUtils.decrypt(encryptedData.toString(), secretKey);
    
            // Восстановление объектов furniture из дешифрованных данных
            String[] furnitureLines = decryptedData.split("\n");
            for (String furnitureLine : furnitureLines) {
                String[] parts = furnitureLine.split(",");
                if (parts.length < 5) {
                    System.out.println("Неполные данные для мебели: " + furnitureLine);
                    continue; // Пропустите эту строку, если данных недостаточно
                }
    
                try {
                    int id = Integer.parseInt(parts[0].trim());
                    String type = parts[1].trim();
                    int weight = Integer.parseInt(parts[2].trim());
                    int amount = Integer.parseInt(parts[3].trim());
                    double price = Double.parseDouble(parts[4].trim());
    
                    furnitures.add(new ConcreteFurniture(id, type, weight, amount, price));
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка формата данных в строке: " + furnitureLine);
                }
            }
    
            System.out.println("Данные успешно расшифрованы и прочитаны из файла " + filename);
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        }
    
        return furnitures;
    }
    
    // Сохранение данных и создание ZIP архива
    public static void saveDataWithEncryptionAndZip(List<Furniture> furnitures) {
        String encryptedFileName = "furniture.txt";

        try (FileOutputStream fos = new FileOutputStream("furniture.zip");
             ZipOutputStream zipOut = new ZipOutputStream(fos)) {
            File fileToZip = new File(encryptedFileName);
            FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            zipOut.closeEntry();
            fis.close();

            System.out.println("Данные заархивированы в ZIP файл.");
        } catch (IOException e) {

        }
    }
    SecretKey generateKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128); // Размер ключа AES - 128 бит
            return keyGen.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
