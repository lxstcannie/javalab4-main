package org.furniture;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.furniture.utils.ConcreteFurniture;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class FileManagerTest {

    @Test
    public void testWriteToFile() throws IOException {
        Path tempFile = Files.createTempFile("furnitures2", ".txt");

        List<Furniture> furnitureList = new ArrayList<>();
        furnitureList.add(new ConcreteFurniture(1, "Обеденный стол", 120, 2, 3000000));
        furnitureList.add(new ConcreteFurniture(2, "Кресло", 80, 1, 1500000));

        FileManager.writeToTxt(tempFile.toString(), furnitureList);

        List<String> fileContent = Files.readAllLines(tempFile);

        assertEquals(2, fileContent.size());
        assertTrue(fileContent.get(0).contains("Обеденный стол"));
        assertTrue(fileContent.get(1).contains("Кресло"));

        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testWriteToJsonFile() throws IOException {
    Path tempFile = Files.createTempFile("furnitures2", ".json");

    List<Furniture> furnitureList = new ArrayList<>();
    furnitureList.add(new ConcreteFurniture(1, "Кирпичный", 120, 2, 3000000));
    furnitureList.add(new ConcreteFurniture(2, "Деревянный", 80, 1, 1500000));

    FileManager.writeToJSON(tempFile.toString(), furnitureList);

    String fileContent = Files.readString(tempFile);
    System.out.println("Содержимое JSON файла: \n" + fileContent); 

    Gson gson = new Gson();
    List<Furniture> parsedfurnitures = gson.fromJson(fileContent, new TypeToken<List<ConcreteFurniture>>(){}.getType());

    assertEquals(2, parsedfurnitures.size());
    assertEquals("Кирпичный", parsedfurnitures.get(0).getType());
    assertEquals("Деревянный", parsedfurnitures.get(1).getType());

    Files.deleteIfExists(tempFile);
}


    @Test
    public void testWriteToXmlFile() throws IOException {
        Path tempFile = Files.createTempFile("furnitures2", ".xml");

        List<Furniture> furnitureList = new ArrayList<>();
        furnitureList.add(new ConcreteFurniture(1, "Кирпичный", 120, 2, 3000000));
        furnitureList.add(new ConcreteFurniture(2, "Деревянный", 80, 1, 1500000));

        FileManager.writeToXML(tempFile.toString(), furnitureList);

        String fileContent = Files.readString(tempFile);

        assertTrue(fileContent.contains("<type>Кирпичный</type>"));
        assertTrue(fileContent.contains("<type>Деревянный</type>"));

        Files.deleteIfExists(tempFile);
    }
}