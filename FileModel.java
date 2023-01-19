package Notepad;
import java.util.*;
import java.io.*;
public class FileModel{
public String readFile(String path){
    String s="";
    try {
      File myObj = new File(path);
      Scanner myReader = new Scanner(myObj);
      while (myReader.hasNextLine()) {
        String data = myReader.nextLine();
        s+=data;
      }
      myReader.close();
    } catch (Exception e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
    return s;
}
public void writeToFile(String path,String data){
  try {
      FileWriter myWriter = new FileWriter(path);
      myWriter.write(data);
      myWriter.close();
      System.out.println("Successfully wrote to the file.");
    } catch (Exception e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
}
}