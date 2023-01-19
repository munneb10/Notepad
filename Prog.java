import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.*;
import java.awt.event.*;
import java.awt.event.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import Notepad.FileModel;
import javax.swing.event.*;
class Prog{
    public static void main(String [] args){
        Notepad notepad=new Notepad();
    }
}

interface NotepadComponent{
    public void setup();
    public Component getState();
}
interface EventClass{
    public void call();
}
interface OpenFileEvent{
public void getPath(String path);
}
interface SaveFileEvent{
public void getPath(String path);
}  
final class SubMenuItem implements NotepadComponent{
    String title;
    JMenuItem sub_menu;
    public SubMenuItem(String title){
        this.title=title;
        this.setup();
    }
    @Override
    public void setup() {
        sub_menu=new JMenuItem(title);
    }
    public void addAction(EventClass c){
        sub_menu.addActionListener((ActionEvent e) -> {
            c.call();
       });
    }
    @Override
    public Component getState() {
        return sub_menu;
    }
}
final class MenuItem implements NotepadComponent{
    JMenu menu_item;
    String title;
    HashMap<String,SubMenuItem> sub_item;
    public MenuItem(String title){
        this.title=title;
        sub_item=new HashMap<>();
        this.setup();
    }
    @Override
    public void setup(){
        menu_item=new JMenu(title);
    }
    public void addSubMenuItem(String name){
        sub_item.put(name,new SubMenuItem(name));
        menu_item.add(sub_item.get(name).getState());
    }
    public SubMenuItem getSubMenuItem(String name){
        return sub_item.get(name);
    }
    @Override
    public Component getState(){
        return menu_item;
    }
}
final class FileHistory implements NotepadComponent{
    JPanel panel;
    JLabel b;
    ArrayList<JButton> files;
    public FileHistory(){
        panel=new JPanel();
        b=new JLabel("History");
        files=new ArrayList<>();
        this.setup();
    }
    @Override
    public void setup() {
        panel.add(b);
    }
    @Override
    public Component getState() {
        return panel;
    }
    
}
final class MenuBar implements NotepadComponent{
    JMenuBar menu_bar;
    MenuItem file,edit;
    String path;
    OpenFileEvent open_file_event;
    SaveFileEvent save_file_event;
    JFrame frame;
    public MenuBar(JFrame frame){
        menu_bar=new JMenuBar();
        this.frame=frame;
        this.setup();
    }
    @Override
    public void setup(){
        setupFile();
        setupEdit();
        this.placeComponent();
    }
    public String getPath(){
        return path;
    }
    public void setupFile(){
        file=new MenuItem("File");
        file.addSubMenuItem("Open");
        class OpenEvent implements EventClass{
            @Override
            public void call() {
                JFileChooser fc=new JFileChooser();
                int i=fc.showOpenDialog(frame);   
                if(i==JFileChooser.APPROVE_OPTION){    
                File f=fc.getSelectedFile();    
                String filepath=f.getPath(); 
                path=filepath;
                open_file_event.getPath(path);
                }
            }
        }
        OpenEvent open_event=new OpenEvent();
        file.getSubMenuItem("Open").addAction(open_event);
        file.addSubMenuItem("Save as");
        class SaveEvent implements EventClass{
            @Override
            public void call() {
                JFileChooser fc=new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int i=fc.showOpenDialog(frame);   
                if(i==JFileChooser.APPROVE_OPTION){    
                File f=fc.getSelectedFile();    
                String filepath=f.getPath(); 
                path=filepath;
                save_file_event.getPath(path);
                }
            }
        }
        SaveEvent save_event=new SaveEvent();
        file.getSubMenuItem("Save as").addAction(save_event);
    }
    public void setupEdit(){
        edit=new MenuItem("Edit");
        edit.addSubMenuItem("Undo   ctrl + Z");
        edit.addSubMenuItem("Redo ctrl + Y");
    }
    public void placeComponent(){
        menu_bar.add(file.getState());
        menu_bar.add(edit.getState());
    }
    public void addOpenFileEvent(OpenFileEvent open_file_event){
        this.open_file_event=open_file_event;
    }
    public void addSaveFileEvent(SaveFileEvent save_file_event){
        this.save_file_event=save_file_event;
    }
    @Override
    public Component getState() {
        return menu_bar;
    }
}
final class TextArea implements NotepadComponent{
    JTextArea ta; 
    KeyListener kl;
    StringBuffer text;
    StringBuffer temp_text;
    String path;
    float initail_font_size=20;
    boolean control_pressed;
    FileModel fm;
    public TextArea(){
        control_pressed=false;
        ta=new JTextArea();
        setupKeyListener();
        ta.addKeyListener(kl);
        fm=new FileModel();
        this.setup();
    }
    @Override
    public void setup() {
        ta.setFont(ta.getFont().deriveFont(20f));
        handleChange();
    }
    public void handleChange(){
        ta.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void removeUpdate(DocumentEvent e) {
            temp_text=new StringBuffer(ta.getText());
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            temp_text=new StringBuffer(ta.getText());
        }

        @Override
        public void changedUpdate(DocumentEvent arg0) {
            System.out.println("cu");
        }
    });
    }
    @Override
    public Component getState() {
        return ta;
    }
    private void increaseFont(){
        initail_font_size+=5;
        ta.setFont(ta.getFont().deriveFont(initail_font_size));
    }
    private void decreaseFont(){
        if(initail_font_size<=20)return;
        initail_font_size-=5;
        ta.setFont(ta.getFont().deriveFont(initail_font_size));
    }
    private void setupKeyListener() {
        kl=new KeyListener(){
    @Override
    public void keyPressed(KeyEvent e){
        System.out.println(e.getKeyCode());
        int keycode=e.getKeyCode();
        if(keycode == 17){
            control_pressed=true;
        }
        if(control_pressed && keycode==61){
            increaseFont();
        }
        if(control_pressed && keycode==45){
            decreaseFont();
        }
        if(control_pressed && keycode==83){
            SaveData(path);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == 17){
            control_pressed=false;
        }
        // System.out.println(e.getKeyCode());
    }
};
    }

    void changeTextAreaFileData(String path) {
        this.path=path;
        System.out.println(path);
        changeTextAreaState();
    }
    void SaveData(String path){
        this.path=path;
        text=temp_text;
        fm.writeToFile(path,text.toString());
        // System.out.println(path);
    }
    void changeTextAreaState(){
        text=new StringBuffer(fm.readFile(path));
        temp_text=text;
        ta.append(temp_text.toString());
    }
}
final class Notepad{
    JFrame frame;
    MenuBar menu_bar;
    FileHistory history;
    TextArea text_area;
    String openPath="";
    Notepad(){
        frame=new JFrame("Notepad");
        menu_bar=new MenuBar(frame);
        history=new FileHistory();
        text_area=new TextArea();
        this.setup();
    }
    public void setup(){
        setupMenuText();
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.placeComponent();
        frame.setVisible(true);
    }
    public void setupMenuText(){
        class OpenFile implements OpenFileEvent{
            public void getPath(String path){
                text_area.changeTextAreaFileData(path);
            }
        }
        OpenFile of=new OpenFile();
        menu_bar.addOpenFileEvent(of);
        class SaveFile implements SaveFileEvent{
            public void getPath(String path){
                text_area.SaveData(path);
            }
        }
        SaveFile sf=new SaveFile();
        menu_bar.addSaveFileEvent(sf);
    }
    public void placeComponent(){
        frame.add(menu_bar.getState());
        frame.getContentPane().add(BorderLayout.NORTH, menu_bar.getState());
        frame.add(history.getState());
        frame.getContentPane().add(BorderLayout.WEST, history.getState());
        frame.add(text_area.getState());
        frame.getContentPane().add(BorderLayout.CENTER, text_area.getState());
    }
}
