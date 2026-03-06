import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;

public class TextEditor extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private JTextArea textArea;
    private JMenuBar menuBar;
    private JMenu fileMenu, editMenu;
    private JMenuItem newItem, openItem, saveItem, exitItem;
    private JMenuItem cutItem, copyItem, pasteItem, findItem;
    private JLabel statusBar;

    public TextEditor() {

        setTitle("Advanced Text Editor");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 16));

        JScrollPane scrollPane = new JScrollPane(textArea);

        LineNumberView lineNumbers = new LineNumberView(textArea);
        scrollPane.setRowHeaderView(lineNumbers);

        add(scrollPane, BorderLayout.CENTER);

        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        fileMenu = new JMenu("File");
        editMenu = new JMenu("Edit");

        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        newItem = new JMenuItem("New");
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");
        exitItem = new JMenuItem("Exit");

        cutItem = new JMenuItem("Cut");
        copyItem = new JMenuItem("Copy");
        pasteItem = new JMenuItem("Paste");
        findItem = new JMenuItem("Find");

        newItem.setAccelerator(KeyStroke.getKeyStroke("control N"));
        openItem.setAccelerator(KeyStroke.getKeyStroke("control O"));
        saveItem.setAccelerator(KeyStroke.getKeyStroke("control S"));
        findItem.setAccelerator(KeyStroke.getKeyStroke("control F"));

        newItem.addActionListener(this);
        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        exitItem.addActionListener(this);
        cutItem.addActionListener(this);
        copyItem.addActionListener(this);
        pasteItem.addActionListener(this);
        findItem.addActionListener(this);

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);
        editMenu.addSeparator();
        editMenu.add(findItem);

        statusBar = new JLabel(" Words: 0 | Characters: 0 ");
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        add(statusBar, BorderLayout.SOUTH);

        textArea.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                updateStatus();
            }

            public void removeUpdate(DocumentEvent e) {
                updateStatus();
            }

            public void changedUpdate(DocumentEvent e) {}
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == cutItem)
            textArea.cut();

        else if (e.getSource() == copyItem)
            textArea.copy();

        else if (e.getSource() == pasteItem)
            textArea.paste();

        else if (e.getSource() == newItem)
            newFile();

        else if (e.getSource() == openItem)
            openFile();

        else if (e.getSource() == saveItem)
            saveFile();

        else if (e.getSource() == findItem)
            findText();

        else if (e.getSource() == exitItem)
            System.exit(0);
    }

    private void newFile() {
        textArea.setText("");
        setTitle("Advanced Text Editor");
    }

    private void openFile() {

        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);

        if (option == JFileChooser.APPROVE_OPTION) {

            try (FileReader reader = new FileReader(fileChooser.getSelectedFile())) {

                textArea.read(reader, null);

            } catch (IOException ex) {

                JOptionPane.showMessageDialog(this, "Error opening file");

            }
        }
    }

    private void saveFile() {

        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(this);

        if (option == JFileChooser.APPROVE_OPTION) {

            try (FileWriter writer = new FileWriter(fileChooser.getSelectedFile())) {

                textArea.write(writer);

            } catch (IOException ex) {

                JOptionPane.showMessageDialog(this, "Error saving file");

            }
        }
    }

    private void findText() {

        String search = JOptionPane.showInputDialog(this, "Find:");

        if (search == null || search.isEmpty())
            return;

        String content = textArea.getText();

        int index = content.indexOf(search);

        if (index >= 0) {

            textArea.setCaretPosition(index);
            textArea.select(index, index + search.length());

        } else {

            JOptionPane.showMessageDialog(this, "Text not found");
        }
    }

    private void updateStatus() {

        String text = textArea.getText();

        int charCount = text.length();

        String[] words = text.trim().split("\\s+");

        int wordCount = text.trim().isEmpty() ? 0 : words.length;

        statusBar.setText(" Words: " + wordCount + " | Characters: " + charCount);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new TextEditor());
    }
}

class LineNumberView extends JTextArea {

    private JTextArea textArea;

    public LineNumberView(JTextArea textArea) {

        this.textArea = textArea;

        setEditable(false);
        setBackground(Color.LIGHT_GRAY);
        setFont(textArea.getFont());

        textArea.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                updateLineNumbers();
            }

            public void removeUpdate(DocumentEvent e) {
                updateLineNumbers();
            }

            public void changedUpdate(DocumentEvent e) {}
        });

        updateLineNumbers();
    }

    private void updateLineNumbers() {

    int lines = textArea.getLineCount();

    StringBuilder numbers = new StringBuilder();

    for (int i = 1; i <= lines; i++) {

        numbers.append(i).append("\n");
    }

    setText(numbers.toString());
}
}