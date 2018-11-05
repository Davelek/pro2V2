package ui;

import java.awt.BorderLayout;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import model.FeedItem;
import model.TableModel;
import model.ToDoItem;
import rss.RssItem;
import rss.RssParser;

public class ProFrame extends JFrame {

    static int width = 800;
    static int height = 600;
    private TableModel model;

    public static void main(String... args) {
        ProFrame proFrame = new ProFrame();
        proFrame.init(width, height);
    }

    private void init(int width, int height) {
        //Creating the Frame
        JFrame frame = new JFrame("Frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        //Creating the MenuBar and adding components
        JMenuBar mb = new JMenuBar();
        JMenu m1 = new JMenu("FILE");
        JMenu m2 = new JMenu("Help");
        mb.add(m1);
        mb.add(m2);
        JMenuItem m11 = new JMenuItem("Open");
        JMenuItem m22 = new JMenuItem("Save as");
        m1.add(m11);
        m1.add(m22);

        //Creating the panel at bottom and adding components
        JPanel panel = new JPanel(); // the panel is not visible in output
        JLabel label = new JLabel("Enter Text");
        JTextField tf = new JTextField(10); // accepts upto 10 characters
        JButton send = new JButton("Send");
        JButton reset = new JButton("Reset");
        panel.add(label); // Components Added using Flow Layout
        panel.add(label); // Components Added using Flow Layout
        panel.add(tf);
        panel.add(send);
        panel.add(reset);

        // Text Area at the Center
        JTextArea ta = new JTextArea();

        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.NORTH, mb);
        frame.getContentPane().add(BorderLayout.CENTER, ta);
        frame.setVisible(true);

      /*  URLButton.addActionListener(action->{
            System.out.println("----------------------------------------------------------------------------------------");
           addFeed(URLTextField.getText());

            // parse(URLTextField.getText());
        });*/

        model = new TableModel();

        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);
        pack();

        setLocationRelativeTo(null); //center okna na monitoru

        readFeeds();
      //  parse("http://www.eurofotbal.cz/feed/rss/premier-league/");
    }


    private void parse(String url){
        try {
//            RssParser parser = new RssParser(new FileInputStream(new File("download.xml")));

           // String url = "http://www.eurofotbal.cz/feed/rss/premier-league/";

            URLConnection connection = new URL(url).openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            RssParser parser = new RssParser(stream);

            List<RssItem> rssitems = parser.parseItems();

            for (RssItem rssitem: rssitems
                 ) {
                System.out.println(rssitem.toString());
            }
        }catch (Exception e){

        }
    }

    private void saveItems() {
        try {
            ObjectOutputStream stream =
                    new ObjectOutputStream(
                            new FileOutputStream(
                                    new File("our.db")
                            )
                    );
            stream.writeObject(model.getItems());
            stream.flush();
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadItems() {
        try {
            ObjectInputStream stream = new ObjectInputStream(
                    new FileInputStream(
                            new File("our.db")
                    )
            );
            List<ToDoItem> items = (List<ToDoItem>) stream.readObject();
            stream.close();
            model.setItems(items);
            model.fireTableDataChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addFeed(String url){
        try {
            File file = new File("feed.txt");
            if (!file.exists()){
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            writer.write(url);
            writer.newLine();
            writer.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void readFeeds(){
        try {
            List<String> urls = new ArrayList<>();
            File file = new File("feed.txt");

            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);
            String line;
            while ((line = reader.readLine()) != null) {
                urls.add(line);
            }

            for (String url: urls) {
                System.out.println(url);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<FeedItem> getAllFeeds(){
        List<FeedItem> feedItems = new ArrayList<>();
        // parse ze souboru TODO


        try {

            File file = new File("feedItems.csv");
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            bufferedReader.readLine();//přeskočí první řádek
            String line;
            while ((line = bufferedReader.readLine()) != null){
                feedItems.add(FeedItem.parseFromCSV(line));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedItems;


    }


    private void saveAllFeeds(List<FeedItem> items){

        try {
            File file = new File("feedItems.csv");
            FileWriter writer = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write("url;addedMillis;shouldShow;alias");
            bufferedWriter.newLine();
            bufferedWriter.flush();
            for (FeedItem item: items ) {
                bufferedWriter.write(items.toString());
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
