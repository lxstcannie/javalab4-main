package org.furniture;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.furniture.utils.ConcreteFurniture;

public class FurnitureFactory {

    private static final String ALGORITHM = "AES"; 
    private static final String KEY_FILE = "secretKey.key"; 
    private static FurnitureCollection furnitureCollection = new FurnitureList();
    private static FurnitureCollection furnitureCollection2 = new FurnitureList();
    static SecretKey key;
    
    public static void saveKeyToFile(SecretKey secretKey) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(KEY_FILE)) {
            fos.write(secretKey.getEncoded());
        }
    }

    // Загрузка ключа из файла
    public static SecretKey loadKeyFromFile() throws IOException {
        byte[] keyBytes = new byte[32]; 
        FileInputStream fis = new FileInputStream(KEY_FILE);
        fis.read(keyBytes);
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }
    

    public static void main(String[] args) throws IOException {
        try {
            key = loadKeyFromFile();
            System.out.println("Ключ загружен из файла.");
        } catch (IOException e) {
            System.out.println("Ключ не найден, генерируем новый.");    
            key = EncryptionUtils.generateKey();
            System.out.println("Новый ключ сгенерирован и сохранен.");
        }
        //System.out.println(key);
        Scanner scanner = new Scanner(System.in);
        String txtFilename = "furniture.txt";
        String xmlFilename = "furniture.xml";
        String jsonFilename = "furniture.json";
        String encryptedFilename = "encrypted.txt";

        Set<Furniture> furnitureSet = new HashSet<>();
        furnitureSet.addAll(FileManager.readFromTxt(txtFilename));
        furnitureSet.addAll(FileManager.readFromXML(xmlFilename));
        furnitureSet.addAll(FileManager.readFromJSON(jsonFilename));
        furnitureCollection.getAllFurniture().addAll(furnitureSet);

        while (true) {
            System.out.println("1. Добавить мебель");
            System.out.println("2. Показать всю мебель");
            System.out.println("3. Сохранить и зашифровать данные");
            System.out.println("4. Прочитать и расшифровать данные");
            System.out.println("5. Сохранить данные и создать архив (ZIP)");
            System.out.println("6. Удалить мебель");
            System.out.println("7. Обновить мебель");
            System.out.println("8. Сортировать по цене");
            System.out.println("9. Сортировать по весу");
            System.out.println("10. Сохранить дома в файл");
            System.out.println("11. Прочитать дома из файла");
            System.out.println("12. Выход");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    addFurniture(scanner);
                    break;
                case 2:
                    printAllFurniture();
                    break;
                case 3:
                    saveAndEncryptData();
                    break;
                case 4:
                    readAndDecryptData();
                    break;
                case 5:
                    saveDataAndCreateZip();
                    break;
                case 6:
                    removeFurniture(scanner);
                    break;
                case 7:
                    updateFurniture(scanner);
                    break;
                case 8:
                    sortFurnitureByPrice();
                    break;
                case 9:
                    sortFurnitureByWeight();
                    break;
                case 10:
                    saveToFile(txtFilename, xmlFilename, jsonFilename);
                    break;
                case 11:
                    readFromFile(txtFilename, xmlFilename, jsonFilename);
                    break;
                case 12:
                    System.out.println("Выход из программы.");
                    saveKeyToFile(key);
                    return;
                default:
                    System.out.println("Неверный ввод. Пожалуйста, попробуйте снова.");
            }
        }
    }

    // Добавление нового дома
    private static void addFurniture(Scanner scanner) {
        System.out.println("Введите id мебели:");
        int id = scanner.nextInt();
        System.out.println("Введите тип мебели:");
        String type = scanner.next();
        System.out.println("Введите вес мебели:");
        int weight = scanner.nextInt();
        System.out.println("Введите количество мебели:");
        int amount = scanner.nextInt();
        System.out.println("Введите цену мебели:");
        double price = scanner.nextDouble();

        Furniture newFurniture = new ConcreteFurniture(id, type, weight, amount, price);
        furnitureCollection.addFurniture(newFurniture);
    }

    // Удаление Мебели
    private static void removeFurniture(Scanner scanner) {
        System.out.println("Введите id мебели для удаления:");
        int id = scanner.nextInt();
        furnitureCollection.removeFurniture(id);
    }

    // Обновление Мебели
    private static void updateFurniture(Scanner scanner) {
        System.out.println("Введите id мебели для обновления:");
        int id = scanner.nextInt();
        Furniture furniture = furnitureCollection.getFurnitureById(id);

        if (furniture != null) {
            System.out.println("Введите новый тип мебели:");
            String type = scanner.next();
            System.out.println("Введите новый вес мебели:");
            int weight = scanner.nextInt();
            System.out.println("Введите новое количество мебели:");
            int amount = scanner.nextInt();
            System.out.println("Введите новую цену мебели:");
            double price = scanner.nextDouble();

            furniture.setType(type);
            furniture.setWeight(weight);
            furniture.setAmount(amount);
            furniture.setPrice(price);
        } else {
            System.out.println("Мебель с таким id не найдена.");
        }
    }

    // Показать всю мебель
    private static void printAllFurniture() {
        List<Furniture> furnitures = furnitureCollection.getAllFurniture();
        if (furnitures.isEmpty()) {
            System.out.println("Мебель отсутствует.");
        } else {
            for (Furniture furniture : furnitures) {
                System.out.println(furniture);
            }
        }
    }
    private static void printAllFurniture2() {
        List<Furniture> furnitures = furnitureCollection2.getAllFurniture();
        if (furnitures.isEmpty()) {
            System.out.println("Мебель отсутствует.");
        } else {
            for (Furniture furniture : furnitures) {
                System.out.println(furniture);
            }
        }
    }

    // Сортировка по цене
    private static void sortFurnitureByPrice() {
        List<Furniture> furnitures = furnitureCollection.getAllFurniture();
        furnitures.sort(Comparator.comparingDouble(Furniture::getPrice));
        System.out.println("Мебель отсортирована по цене:");
        printAllFurniture();
    }

    // Сортировка по площади
    private static void sortFurnitureByWeight() {
        List<Furniture> furnitures = furnitureCollection.getAllFurniture();
        furnitures.sort(Comparator.comparingInt(Furniture::getWeight));
        System.out.println("Мебель отсортирована по площади:");
        printAllFurniture();
    }

    // Сохранение и шифрование данных
    private static void saveAndEncryptData() {
            FileManager.writeEncryptedToTxt("encrypted.txt", furnitureCollection.getAllFurniture(), key);
            System.out.println("Данные успешно зашифрованы и сохранены.");
    }

    // Чтение и расшифровка данных
    private static void readAndDecryptData() {
            List<Furniture> decryptedFurniture = FileManager.readDecryptedFromTxt("encrypted.txt", key);
            System.out.println("Дешифрованные данные:");
            furnitureCollection2.getAllFurniture().addAll(decryptedFurniture);
            printAllFurniture2();
    }

    // Сохранение данных и создание ZIP архива
    private static void saveDataAndCreateZip() {
            FileManager.saveDataWithEncryptionAndZip(furnitureCollection.getAllFurniture());
    }

    // Сохранение данных в файлы
    private static void saveToFile(String txtFilename, String xmlFilename, String jsonFilename) {
        List<Furniture> furnitures = furnitureCollection.getAllFurniture();
        FileManager.writeToTxt(txtFilename, furnitures);
        FileManager.writeToXML(xmlFilename, furnitures);
        FileManager.writeToJSON(jsonFilename, furnitures);
        System.out.println("Данные успешно сохранены в файлы.");
    }

    // Чтение данных из файлов
    private static void readFromFile(String txtFilename, String xmlFilename, String jsonFilename) {
        Set<Furniture> furnitureSet = new HashSet<>();
        furnitureSet.addAll(FileManager.readFromTxt(txtFilename));
        furnitureSet.addAll(FileManager.readFromXML(xmlFilename));
        furnitureSet.addAll(FileManager.readFromJSON(jsonFilename));
        furnitureCollection.getAllFurniture().clear();
        furnitureCollection.getAllFurniture().addAll(furnitureSet);
        System.out.println("Данные успешно прочитаны из файлов.");
        printAllFurniture();
    }
}
