package Flyweight;

import java.io.*;
import java.util.*;

public class Driver{
    public static void main(String[] args){
        Document testDocument = new Document();

        testDocument.addCharacter('H', "Arial", "Red", 12);
        testDocument.addCharacter('e', "Calibri", "Blue", 14);
        testDocument.addCharacter('l', "Verdana", "Black", 16);
        testDocument.addCharacter('l', "Arial", "Red", 12);
        testDocument.addCharacter('o', "Calibri", "Blue", 14);
        testDocument.addCharacter('W', "Verdana", "Black", 16);
        testDocument.addCharacter('o', "Arial", "Red", 12);
        testDocument.addCharacter('r', "Calibri", "Blue", 14);
        testDocument.addCharacter('l', "Verdana", "Black", 16);
        testDocument.addCharacter('d', "Arial", "Red", 12);
        testDocument.addCharacter('C', "Verdana", "Black", 16);
        testDocument.addCharacter('S', "Arial", "Red", 12);
        testDocument.addCharacter('5', "Calibri", "Blue", 14);
        testDocument.addCharacter('8', "Verdana", "Black", 16);
        testDocument.addCharacter('0', "Arial", "Red", 12);
        testDocument.addCharacter('0', "Calibri", "Blue", 14);
        testDocument.saveToFile("variation1.txt");

        Document loadedDocument = new Document();
        loadedDocument.loadFromFile("variation1.txt");
        loadedDocument.editCharacterProperties(0, "Arial", "Black", 18);
        loadedDocument.editCharacterProperties(5, "Arial", "Black", 18);
        loadedDocument.editCharacterProperties(6, "Arial", "Black", 18);
        loadedDocument.saveToFile("variation2.txt");

        loadedDocument.editCharacterProperties(5, "Calibri", "Blue", 12);
        loadedDocument.editCharacterProperties(6, "Calibri", "Red", 12);
        loadedDocument.saveToFile("variation3.txt");

        loadedDocument.editCharacterProperties(7, "Calibri", "Blue", 14);
        loadedDocument.editCharacterProperties(8, "Calibri", "Red", 14);
        loadedDocument.editCharacterProperties(9, "Verdana", "Blue", 14);
        loadedDocument.editCharacterProperties(10, "Verdana", "Red", 14);
        loadedDocument.saveToFile("variation4.txt");
    }
}

class CharacterProperties{
    private final String font;
    private final String color;
    private final int size;

    public CharacterProperties(String font, String color, int size){
        this.font = font;
        this.color = color;
        this.size = size;
    }

    public String getFont(){
        return font;
    }

    public String getColor(){
        return color;
    }

    public int getSize(){
        return size;
    }
}

class CharacterPropertiesFactory {
    private final Map<String, CharacterProperties> propertiesMap = new HashMap<>();

    public CharacterProperties getCharacterProperties(String font, String color, int size){
        String key = font + "-" + color + "-" + size;

        if(!propertiesMap.containsKey(key)){
            propertiesMap.put(key, new CharacterProperties(font, color, size));
        }

        return propertiesMap.get(key);
    }
}

class Character{
    private final char character;
    private final CharacterProperties characterProperty;

    public Character(char character, CharacterProperties characterProperty){
        this.character = character;
        this.characterProperty = characterProperty;
    }

    public char getCharacter(){
        return character;
    }

    public String getFont(){
        return characterProperty.getFont();
    }

    public String getColor(){
        return characterProperty.getColor();
    }

    public int getSize(){
        return characterProperty.getSize();
    }
}

class Document{
    private final List<Character> characters = new ArrayList<>();
    private final CharacterPropertiesFactory propertiesFactory = new CharacterPropertiesFactory();

    public void addCharacter(char c, String font, String color, int size){
        CharacterProperties properties = propertiesFactory.getCharacterProperties(font, color, size);
        characters.add(new Character(c, properties));
    }

    public void editCharacterProperties(int index, String font, String color, int size){
        if(index >= 0 && index < characters.size()){
            Character character = characters.get(index);

            CharacterProperties newProperties = propertiesFactory.getCharacterProperties(font, color, size);
            characters.set(index, new Character(character.getCharacter(), newProperties));
        }else{
            System.out.println("Invalid index.");
        }
    }

    public void saveToFile(String filename){
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))){
            for(Character character : characters){
                writer.println(character.getCharacter() + "," + character.getFont() + "," + character.getColor() + "," + character.getSize());
            }
            System.out.println("Document saved: " + filename);
        }catch(IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void loadFromFile(String filename){
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String line;
            while((line = reader.readLine()) != null){
                String[] parts = line.split(",");
                if(parts.length == 4){
                    char c = parts[0].charAt(0);
                    String font = parts[1];
                    String color = parts[2];
                    int size = Integer.parseInt(parts[3]);
                    addCharacter(c, font, color, size);
                }
            }
            System.out.println("Document loaded: " + filename);
        }catch(IOException | NumberFormatException e){
            System.err.println("Error loading: " + e.getMessage());
        }
    }
}
