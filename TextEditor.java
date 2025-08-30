import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * A Simple Text Editor application built with Java Swing.
 * This project demonstrates GUI development with a menu bar, JTextArea for text
 * input, file I/O operations, and a real-time status bar.
 */
public class TextEditor extends JFrame implements ActionListener {

    // --- UI Components ---
    private JTextArea textArea;
    private JMenuBar menuBar;
    private JMenu fileMenu, editMenu;
    private JMenuItem newItem, openItem, saveItem, cutItem, copyItem, pasteItem, exitItem; // New: Added newItem
    private JLabel statusBar;

    /**
     * Constructor to set up the text editor's GUI.
     */
    public TextEditor() {
        // --- Frame Setup ---
        setTitle("Simple Text Editor");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // --- Text Area Setup ---
        textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        // Add the text area to a scroll pane to enable scrolling
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        // --- Menu Bar Setup ---
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // --- File Menu ---
        fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        newItem = new JMenuItem("New"); // New: Create New item
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");
        exitItem = new JMenuItem("Exit");

        newItem.addActionListener(this); // New: Add listener
        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        exitItem.addActionListener(this);

        fileMenu.add(newItem); // New: Add New item to menu
        fileMenu.addSeparator(); // New: Add separator for better organization
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator(); // Adds a dividing line in the menu
        fileMenu.add(exitItem);

        // --- Edit Menu ---
        editMenu = new JMenu("Edit");
        menuBar.add(editMenu);

        cutItem = new JMenuItem("Cut");
        copyItem = new JMenuItem("Copy");
        pasteItem = new JMenuItem("Paste");

        cutItem.addActionListener(this);
        copyItem.addActionListener(this);
        pasteItem.addActionListener(this);

        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);
        
        // --- Status Bar Setup ---
        statusBar = new JLabel(" Words: 0 | Characters: 0 ");
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        add(statusBar, BorderLayout.SOUTH);

        // --- Document Listener for Real-Time Updates ---
        // This listener will call updateStatus() whenever text is changed.
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateStatus();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateStatus();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Not used for plain text components
            }
        });

        // --- Finalize Frame ---
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    /**
     * This method is called whenever a menu item is clicked.
     * @param e The ActionEvent object containing details about the event.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // --- Edit Menu Actions ---
        if (e.getSource() == cutItem) {
            textArea.cut();
        } else if (e.getSource() == copyItem) {
            textArea.copy();
        } else if (e.getSource() == pasteItem) {
            textArea.paste();
        } 
        // --- File Menu Actions ---
        else if (e.getSource() == newItem) { // New: Handle New action
            newFile();
        } else if (e.getSource() == openItem) {
            openFile();
        } else if (e.getSource() == saveItem) {
            saveFile();
        } else if (e.getSource() == exitItem) {
            System.exit(0);
        }
    }

    /**
     * New: Handles the logic for creating a new, empty file.
     */
    private void newFile() {
        textArea.setText("");
        setTitle("Simple Text Editor");
        // The DocumentListener will automatically call updateStatus()
    }

    /**
     * Handles the logic for opening a text file.
     */
    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                setTitle("Simple Text Editor - " + file.getName());
                FileReader reader = new FileReader(file);
                textArea.read(reader, null);
                reader.close();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error opening file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Handles the logic for saving the current text to a file.
     */
    private void saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                // Ensure the file has a .txt extension
                if (!file.getName().toLowerCase().endsWith(".txt")) {
                    file = new File(file.getParentFile(), file.getName() + ".txt");
                }
                setTitle("Simple Text Editor - " + file.getName());
                FileWriter writer = new FileWriter(file);
                textArea.write(writer);
                writer.close();
            } catch (IOException ex) {
                 JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Updates the status bar with the current word and character count.
     */
    private void updateStatus() {
        String text = textArea.getText();
        int charCount = text.length();

        // Split text by whitespace to count words.
        // The trim() method removes leading/trailing whitespace to avoid counting empty strings.
        String[] words = text.trim().split("\\s+");
        int wordCount = text.trim().isEmpty() ? 0 : words.length;

        statusBar.setText(" Words: " + wordCount + " | Characters: " + charCount + " ");
    }


    /**
     * The main method to create and run the text editor application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TextEditor());
    }
}

