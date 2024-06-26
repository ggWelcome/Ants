package GUI;

import GUI.GraphEnter.GraphEnterFrame;
import Graph.Graph;
import Staff.Pair;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.util.Hashtable;
import java.util.Scanner;

// окно "параметры"
public class ParametersFrame extends JFrame
        implements ActionListener, ChangeListener, PropertyChangeListener {


    private final MainMenuFrame mainMenuFrame;    // ссылка на главное окно
    private Graph graph;                          // ссылка на граф
    private Pair<Double, Double> params;          // ссылка на параметры (жадность, скорость испарения)
    private Pair<Integer, Integer> antParams;     // ссылка на параметры муравьев


    // параметры генерации
    private int numberOfVertices = 5;             // количестов вершин
    private int percentOfEdges = 50;              // процент ребер
    private Pair<Integer, Integer> rangeOfWeights
                        = new Pair<>(1,100);      // диапозон весов


    // компоненты окна

    // группа "из файла"
    private JFileChooser fileChooser;
    private File file;
    private JTextField filePathTextField;
    private JButton chooseFileButton;
    private JButton createGraphFromFileButton;
    // группа "генерация"
    private JLabel numberOfVerticesLabel;
    private JSlider numberOfVerticesSlider;
    private JLabel percentOfEdgesLabel;
    private JSlider percentOfEdgesSlider;
    private JLabel rangeOfWeightsLabel;
    private JLabel rangeFromLabel;
    private JLabel rangeToLabel;
    private JFormattedTextField rangeFromField;
    private JFormattedTextField rangeToField;
    private JButton createGraphGenerateButton;
    // группа "ручной ввод"
    private JButton handEnterGraphButton;

    // кнопка "показать граф"
    private JButton showGraphButton;


    // конструктор
    public ParametersFrame(MainMenuFrame mainMenuFrame_, Graph graph_,
                           Pair<Double, Double> params_, Pair<Integer, Integer> antParams_) {
        super("Parameters");

        mainMenuFrame = mainMenuFrame_;
        graph = graph_;
        params = params_;
        antParams = antParams_;

        // окно
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        setResizable(false);
        setLayout(null);

        // действие при закрытии и открытии окна
        addWindowListener(new WindowListener() {
            public void windowClosing(WindowEvent event) {
                event.getWindow().setVisible(false);
                event.getWindow().dispose();
                mainMenuFrame.setVisible(true);
            }
            public void windowActivated(WindowEvent event) {
                if (graph.isCreated()) {
                    showGraphButton.setText("Display graph");
                    showGraphButton.setEnabled(true);
                } else {
                    showGraphButton.setText("Display graph (graph is missing)");
                    showGraphButton.setEnabled(false);
                }
            }
            public void windowClosed(WindowEvent event) { }
            public void windowDeactivated(WindowEvent event) { }
            public void windowDeiconified(WindowEvent event) { }
            public void windowIconified(WindowEvent event) { }
            public void windowOpened(WindowEvent event) { }
        });

        // иконка
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/GUI/images/icon.png")));

        // панель "ввод графа"
        JPanel enterGraphPanel = new JPanel();
        enterGraphPanel.setLayout(null);
        enterGraphPanel.setBounds(20, 5, 760, 270);
        TitledBorder enterGraphPanelTitle = BorderFactory.createTitledBorder("Enter graph:");
        enterGraphPanel.setBorder(enterGraphPanelTitle);

        // радио кнопки выбора ввода
        JRadioButton fromFileRadioButton = new JRadioButton("From a file:");
        fromFileRadioButton.setFont(new Font("Arial", Font.PLAIN, 14));
        fromFileRadioButton.setBounds(20, 40, 90, 20);
        fromFileRadioButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        fromFileRadioButton.setActionCommand("FromFileRadioButton");
        fromFileRadioButton.addActionListener(this);
        fromFileRadioButton.setSelected(true);

        JRadioButton generateGraphRadioButton = new JRadioButton("Generation:");
        generateGraphRadioButton.setFont(new Font("Arial", Font.PLAIN, 14));
        generateGraphRadioButton.setBounds(20, 80, 100, 20);
        generateGraphRadioButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        generateGraphRadioButton.setActionCommand("GenerateRadioButton");
        generateGraphRadioButton.addActionListener(this);

        JRadioButton handEnterGraphRadioButton = new JRadioButton("Manual input:");
        handEnterGraphRadioButton.setFont(new Font("Arial", Font.PLAIN, 14));
        handEnterGraphRadioButton.setBounds(20, 220, 110, 20);
        handEnterGraphRadioButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        handEnterGraphRadioButton.setActionCommand("HandEnterRadioButton");
        handEnterGraphRadioButton.addActionListener(this);

        // группа кнопок выбора
        ButtonGroup enterGraphGroup = new ButtonGroup();
        enterGraphGroup.add(fromFileRadioButton);
        enterGraphGroup.add(generateGraphRadioButton);
        enterGraphGroup.add(handEnterGraphRadioButton);
        // добавить на панель
        enterGraphPanel.add(fromFileRadioButton);
        enterGraphPanel.add(generateGraphRadioButton);
        enterGraphPanel.add(handEnterGraphRadioButton);


        // ввод графа из файла
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));

        chooseFileButton = new JButton("Select a file");
        chooseFileButton.setFont(new Font("Arial", Font.BOLD, 14));
        chooseFileButton.setBounds(140, 30, 150, 40);
        chooseFileButton.setActionCommand("ChooseFileButton");
        chooseFileButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        chooseFileButton.addActionListener(this);
        enterGraphPanel.add(chooseFileButton);

        filePathTextField = new JTextField("Way to the file . . .");
        filePathTextField.setBounds(310, 35, 260, 30);
        filePathTextField.setEditable(false);
        filePathTextField.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        enterGraphPanel.add(filePathTextField);

        // кнопка "создать граф из файла"
        createGraphFromFileButton = new JButton("Create a graph");
        createGraphFromFileButton.setFont(new Font("Arial", Font.BOLD, 14));
        createGraphFromFileButton.setBounds(590, 30, 150, 40);
        createGraphFromFileButton.setActionCommand("CreateGraphFromFileButton");
        createGraphFromFileButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        createGraphFromFileButton.addActionListener(this);
        createGraphFromFileButton.setEnabled(false);
        enterGraphPanel.add(createGraphFromFileButton);


        // ввод графа - генерация
        numberOfVerticesLabel = new JLabel("The number of vertices:");
        numberOfVerticesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        numberOfVerticesLabel.setBounds(140, 70, 140, 40);
        enterGraphPanel.add(numberOfVerticesLabel);
        // слайдер - количество вершин
        numberOfVerticesSlider = new JSlider(JSlider.HORIZONTAL, 2, 10, numberOfVertices);
        numberOfVerticesSlider.setBounds(310, 80, 425, 40);
        numberOfVerticesSlider.setMajorTickSpacing(1);
        numberOfVerticesSlider.setMinorTickSpacing(1);
        numberOfVerticesSlider.setPaintTicks(true);
        numberOfVerticesSlider.setPaintLabels(true);
        numberOfVerticesSlider.setFont(new Font("Arial", Font.PLAIN, 10));
        numberOfVerticesSlider.setBorder(BorderFactory.createEmptyBorder(0,0,15,0));
        numberOfVerticesSlider.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        numberOfVerticesSlider.setName("NumberOfVerticesSlider");
        numberOfVerticesSlider.addChangeListener(this);
        enterGraphPanel.add(numberOfVerticesSlider);

        percentOfEdgesLabel = new JLabel("Percentage of edges:");
        percentOfEdgesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        percentOfEdgesLabel.setBounds(140, 115, 120, 40);
        enterGraphPanel.add(percentOfEdgesLabel);
        // слайдер - процент ребер
        percentOfEdgesSlider = new JSlider(JSlider.HORIZONTAL, 20, 100, percentOfEdges);
        percentOfEdgesSlider.setBounds(310, 125, 425, 40);
        percentOfEdgesSlider.setMajorTickSpacing(10);
        percentOfEdgesSlider.setMinorTickSpacing(5);
        percentOfEdgesSlider.setPaintTicks(true);
        percentOfEdgesSlider.setPaintLabels(true);
        percentOfEdgesSlider.setFont(new Font("Arial", Font.PLAIN, 10));
        percentOfEdgesSlider.setBorder(BorderFactory.createEmptyBorder(0,0,15,0));
        percentOfEdgesSlider.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        percentOfEdgesSlider.setName("PercentOfEdgesSlider");
        percentOfEdgesSlider.addChangeListener(this);
        enterGraphPanel.add(percentOfEdgesSlider);

        // ----- форматтер ввода ------
        NumberFormat nf = NumberFormat.getIntegerInstance();
        NumberFormatter nff= new NumberFormatter(nf);
        nff.setMinimum(1);
        nff.setMaximum(100);
        // ----------------------------

        // поля - диапозон весов
        rangeOfWeightsLabel = new JLabel("Mass range:");
        rangeOfWeightsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        rangeOfWeightsLabel.setBounds(140, 170, 120, 40);
        enterGraphPanel.add(rangeOfWeightsLabel);

        // от
        rangeFromLabel = new JLabel("From:");
        rangeFromLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        rangeFromLabel.setBounds(310, 170, 30, 40);
        enterGraphPanel.add(rangeFromLabel);

        rangeFromField = new JFormattedTextField(nff);
        rangeFromField.setValue(rangeOfWeights.first);
        rangeFromField.setBounds(340, 175, 50, 30);
        rangeFromField.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        rangeFromField.setName("RangeFromField");
        rangeFromField.addPropertyChangeListener(this);
        enterGraphPanel.add(rangeFromField);

        rangeFromField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent fe) { }
            public void focusLost(FocusEvent fe) {
                try {
                    if (Integer.parseInt(rangeFromField.getText()) > Integer.parseInt(rangeToField.getText()))
                        rangeToField.setValue(100);
                } catch (Exception e1) {}
            }
        });

        // до
        rangeToLabel = new JLabel("To:");
        rangeToLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        rangeToLabel.setBounds(420, 170, 30, 40);
        enterGraphPanel.add(rangeToLabel);

        rangeToField = new JFormattedTextField(nff);
        rangeToField.setValue(rangeOfWeights.second);
        rangeToField.setBounds(450, 175, 50, 30);
        rangeToField.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        rangeToField.setName("RangeToField");
        rangeToField.addPropertyChangeListener(this);
        enterGraphPanel.add(rangeToField);

        rangeToField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent fe) { }
            public void focusLost(FocusEvent fe) {
                try {
                    if (Integer.parseInt(rangeFromField.getText()) > Integer.parseInt(rangeToField.getText()))
                        rangeFromField.setValue(1);
                } catch (Exception e1) {}
            }
        });

        // кнопка "создать граф - генерация"
        createGraphGenerateButton = new JButton("Create a graph");
        createGraphGenerateButton.setFont(new Font("Arial", Font.BOLD, 14));
        createGraphGenerateButton.setBounds(590, 170, 150, 40);
        createGraphGenerateButton.setActionCommand("CreateGraphGenerateButton");
        createGraphGenerateButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        createGraphGenerateButton.addActionListener(this);
        enterGraphPanel.add(createGraphGenerateButton);


        // ввод графа - ручной ввод
        handEnterGraphButton = new JButton("Enter a graph");
        handEnterGraphButton.setFont(new Font("Arial", Font.BOLD, 14));
        handEnterGraphButton.setBounds(140, 210, 150, 40);
        handEnterGraphButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        handEnterGraphButton.setActionCommand("EnterGraphButton");
        handEnterGraphButton.addActionListener(this);
        enterGraphPanel.add(handEnterGraphButton);


        // панель "дополнительные параметры"
        JPanel paramsPanel = new JPanel();
        paramsPanel.setLayout(null);
        paramsPanel.setBounds(20, 280, 760, 230);
        TitledBorder paramsPanelTitle = BorderFactory.createTitledBorder("Additional parameters:");
        paramsPanel.setBorder(paramsPanelTitle);

        // --- для шкалы  слайдеров ---
        Hashtable labelTable = new Hashtable();
        labelTable.put(0, new JLabel("0.0"));
        labelTable.put(10, new JLabel("0.1"));
        labelTable.put(20, new JLabel("0.2"));
        labelTable.put(30, new JLabel("0.3"));
        labelTable.put(40, new JLabel("0.4"));
        labelTable.put(50, new JLabel("0.5"));
        labelTable.put(60, new JLabel("0.6"));
        labelTable.put(70, new JLabel("0.7"));
        labelTable.put(80, new JLabel("0.8"));
        labelTable.put(90, new JLabel("0.9"));
        labelTable.put(100, new JLabel("1.0"));
        // ----------------------------

        JLabel greedOfAlgorithmLabel = new JLabel("\"Insatiability\" of the algorithm:");
        greedOfAlgorithmLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        greedOfAlgorithmLabel.setBounds(20, 20, 160, 40);
        paramsPanel.add(greedOfAlgorithmLabel);
        // слайдер - жадность алгоритма
        Double tmp = params.first * 100;
        Integer greedInt = tmp.intValue();
        JSlider greedOfAlgorithmSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, greedInt);
        greedOfAlgorithmSlider.setBounds(310, 30, 425, 40);
        greedOfAlgorithmSlider.setLabelTable(labelTable);
        greedOfAlgorithmSlider.setMajorTickSpacing(10);
        greedOfAlgorithmSlider.setMinorTickSpacing(5);
        greedOfAlgorithmSlider.setPaintTicks(true);
        greedOfAlgorithmSlider.setPaintLabels(true);
        greedOfAlgorithmSlider.setFont(new Font("Arial", Font.PLAIN, 10));
        greedOfAlgorithmSlider.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        greedOfAlgorithmSlider.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        greedOfAlgorithmSlider.setName("GreedOfAlgorithmSlider");
        greedOfAlgorithmSlider.addChangeListener(this);
        paramsPanel.add(greedOfAlgorithmSlider);

        JLabel rateOfEvaporationLabel = new JLabel("Pheromone evaporation rate:");
        rateOfEvaporationLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        rateOfEvaporationLabel.setBounds(20, 65, 220, 40);
        paramsPanel.add(rateOfEvaporationLabel);
        // слайдер - скорость испарения феромона
        Double tmp1 = params.second * 100;
        Integer evaporationSpeedInt = tmp1.intValue();
        JSlider rateOfEvaporationSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, evaporationSpeedInt);
        rateOfEvaporationSlider.setBounds(310, 75, 425, 40);
        rateOfEvaporationSlider.setLabelTable(labelTable);
        rateOfEvaporationSlider.setMajorTickSpacing(10);
        rateOfEvaporationSlider.setMinorTickSpacing(5);
        rateOfEvaporationSlider.setPaintTicks(true);
        rateOfEvaporationSlider.setPaintLabels(true);
        rateOfEvaporationSlider.setFont(new Font("Arial", Font.PLAIN, 10));
        rateOfEvaporationSlider.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        rateOfEvaporationSlider.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        rateOfEvaporationSlider.setName("RateOfEvaporationSlider");
        rateOfEvaporationSlider.addChangeListener(this);
        paramsPanel.add(rateOfEvaporationSlider);

        JLabel numberOfRandomAntsLabel = new JLabel("The number of \"Blitzes\" ant: ");
        numberOfRandomAntsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        numberOfRandomAntsLabel.setBounds(20, 115, 210, 40);
        paramsPanel.add(numberOfRandomAntsLabel);              
        // слайдер - количество блиц муравьев
        JSlider numberOfRandomAntsSlider = new JSlider(JSlider.HORIZONTAL, 1, 101, antParams.first+1);
        numberOfRandomAntsSlider.setBounds(310, 120, 425, 40);
        Hashtable<Integer, JLabel> table = new Hashtable<Integer, JLabel>();
        table.put (1, new JLabel("1"));
        table.put (11, new JLabel("10"));
        table.put (21, new JLabel("20"));
        table.put (31, new JLabel("30"));
        table.put (41, new JLabel("40"));
        table.put (51, new JLabel("50"));
        table.put (61, new JLabel("60"));
        table.put (71, new JLabel("70"));
        table.put (81, new JLabel("80"));
        table.put (91, new JLabel("90"));
        table.put (101, new JLabel("100"));
        numberOfRandomAntsSlider.setLabelTable(table);
        numberOfRandomAntsSlider.setMajorTickSpacing(10);
        numberOfRandomAntsSlider.setMinorTickSpacing(5);
        numberOfRandomAntsSlider.setPaintTicks(true);
        numberOfRandomAntsSlider.setPaintLabels(true);
        numberOfRandomAntsSlider.setFont(new Font("Arial", Font.PLAIN, 10));
        numberOfRandomAntsSlider.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        numberOfRandomAntsSlider.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        numberOfRandomAntsSlider.setName("NumberOfRandomAntsSlider");
        numberOfRandomAntsSlider.addChangeListener(this);
        paramsPanel.add(numberOfRandomAntsSlider);

        JLabel numberOfAntsLabel = new JLabel("Total number of ants: ");
        numberOfAntsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        numberOfAntsLabel.setBounds(20, 165, 210, 40);
        paramsPanel.add(numberOfAntsLabel);
        // слайдер - общее количество муравьев
        JSlider numberOfAntsSlider = new JSlider(JSlider.HORIZONTAL, 10, 10010, antParams.second+10);
        numberOfAntsSlider.setBounds(305, 165, 435, 40);
        Hashtable<Integer, JLabel> table1 = new Hashtable<Integer, JLabel>();
        table1.put (10, new JLabel("10"));
        table1.put (1010, new JLabel("1000"));
        table1.put (2010, new JLabel("2000"));
        table1.put (3010, new JLabel("3000"));
        table1.put (4010, new JLabel("4000"));
        table1.put (5010, new JLabel("5000"));
        table1.put (6010, new JLabel("6000"));
        table1.put (7010, new JLabel("7000"));
        table1.put (8010, new JLabel("8000"));
        table1.put (9010, new JLabel("9000"));
        table1.put (10010, new JLabel("10000"));
        numberOfAntsSlider.setLabelTable(table1);
        numberOfAntsSlider.setMajorTickSpacing(1000);
        numberOfAntsSlider.setMinorTickSpacing(500);
        numberOfAntsSlider.setPaintTicks(true);
        numberOfAntsSlider.setPaintLabels(true);
        numberOfAntsSlider.setFont(new Font("Arial", Font.PLAIN, 10));
        numberOfAntsSlider.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        numberOfAntsSlider.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        numberOfAntsSlider.setName("NumberOfAntsSlider");
        numberOfAntsSlider.addChangeListener(this);
        paramsPanel.add(numberOfAntsSlider);

        // кнопки
        JButton backButton = new JButton("To the main menu");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBounds(100, 520, 230, 40);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.setActionCommand("BackButton");
        backButton.addActionListener(this);
        add(backButton);

        showGraphButton = new JButton("Display graph");
        showGraphButton.setFont(new Font("Arial", Font.BOLD, 14));
        showGraphButton.setBounds(460, 520, 230, 40);
        if (!graph.isCreated()) {
            showGraphButton.setText("Display graph (graph is missing)");
            showGraphButton.setEnabled(false);
        }
        showGraphButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        showGraphButton.setActionCommand("ShowGraphButton");
        showGraphButton.addActionListener(this);
        add(showGraphButton);


        add(enterGraphPanel);
        add(paramsPanel);
        pack();
        setLocationRelativeTo(null); // центрирование окна
        setVisible(true);

        // по-умолчанию - выбор из файла
        enableEnterFromFileGraph();
        disableGenerationGraph();
        disableHandEnterGraph();
    }

    // заблокировать компоненты (радио кнопки)
    private void disableEnterFromFileGraph() {
        chooseFileButton.setEnabled(false);
        filePathTextField.setEnabled(false);
        createGraphFromFileButton.setEnabled(false);
    }
    private void disableGenerationGraph() {
        numberOfVerticesLabel.setEnabled(false);
        numberOfVerticesSlider.setEnabled(false);
        percentOfEdgesLabel.setEnabled(false);
        percentOfEdgesSlider.setEnabled(false);
        rangeOfWeightsLabel.setEnabled(false);
        rangeFromLabel.setEnabled(false);
        rangeToLabel.setEnabled(false);
        rangeFromField.setEnabled(false);
        rangeToField.setEnabled(false);
        createGraphGenerateButton.setEnabled(false);
    }
    private void disableHandEnterGraph() {
        handEnterGraphButton.setEnabled(false);
    }

    // разблокировать компоненты (радио кнопки)
    private void enableEnterFromFileGraph() {
        chooseFileButton.setEnabled(true);
        filePathTextField.setEnabled(true);
        if (file != null) createGraphFromFileButton.setEnabled(true);
    }
    private void enableGenerationGraph() {
        numberOfVerticesLabel.setEnabled(true);
        numberOfVerticesSlider.setEnabled(true);
        percentOfEdgesLabel.setEnabled(true);
        percentOfEdgesSlider.setEnabled(true);
        rangeOfWeightsLabel.setEnabled(true);
        rangeFromLabel.setEnabled(true);
        rangeToLabel.setEnabled(true);
        rangeFromField.setEnabled(true);
        rangeToField.setEnabled(true);
        createGraphGenerateButton.setEnabled(true);
    }
    private void enableHandEnterGraph() {
        handEnterGraphButton.setEnabled(true);
    }

    // слайдеры
    public void stateChanged(ChangeEvent e) {
        Object source = e.getSource();
        JSlider slider = (JSlider)source;

        switch (slider.getName()) {
            case"NumberOfVerticesSlider":
                numberOfVertices = slider.getValue();
                break;

            case "PercentOfEdgesSlider":
                percentOfEdges = slider.getValue();
                break;

            case "GreedOfAlgorithmSlider":
                // делить на 100, т. к. целочисленная шкла (1 == 0.1, 100 == 1)
                params.first = Integer.valueOf(slider.getValue()).doubleValue() / 100;
                break;

            case "RateOfEvaporationSlider":
                // делить на 100, т. к. целочисленная шкла (1 == 0.1, 100 == 1)
                params.second = Integer.valueOf(slider.getValue()).doubleValue() / 100;
                break;

            case "NumberOfRandomAntsSlider":
                if (slider.getValue() == 1) 
                	antParams.first = slider.getValue();
                else
                	antParams.first = slider.getValue()-1;
                break;

            case "NumberOfAntsSlider":
                antParams.second = slider.getValue();
                break;

            default:
                break;
        }
    }

    // поля "от" и "до"
    public void propertyChange(PropertyChangeEvent e) {
        Object source = e.getSource();
        JFormattedTextField field = (JFormattedTextField)source;

        switch (field.getName()) {
            case "RangeFromField":
                try {
                    rangeOfWeights.first = Integer.parseInt(rangeFromField.getText());
                } catch (Exception e1) { }
                break;

            case "RangeToField":
                try {
                    rangeOfWeights.second = Integer.parseInt(rangeToField.getText());
                } catch (Exception e1) { }
                break;
        }
    }

    // нажатие кнопок
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        switch(cmd) {
            // радио кнопки
            case "FromFileRadioButton":
                enableEnterFromFileGraph();
                disableGenerationGraph();
                disableHandEnterGraph();
                break;

            case "GenerateRadioButton":
                enableGenerationGraph();
                disableEnterFromFileGraph();
                disableHandEnterGraph();
                break;

            case "HandEnterRadioButton":
                enableHandEnterGraph();
                disableGenerationGraph();
                disableEnterFromFileGraph();
                break;

            // кнопки
            case "ChooseFileButton":
                int isFileOpen = fileChooser.showOpenDialog(this);
                if (isFileOpen == JFileChooser.APPROVE_OPTION) {
                    file = fileChooser.getSelectedFile();
                    filePathTextField.setText(file.getPath());
                    createGraphFromFileButton.setEnabled(true);
                }
                break;

            case "CreateGraphFromFileButton":
                try {
                    if (graph.createGraphFromFile(new Scanner(file))) {
                        showGraphButton.setText("Display graph");
                        showGraphButton.setEnabled(true);

                        if (graph.isNull())
                            JOptionPane.showMessageDialog(null,
                                    "Graph successfully created. Edges missing!", "Attention", JOptionPane.WARNING_MESSAGE);
                        else
                            JOptionPane.showMessageDialog(null,
                                    "Graph successfully created!", "Graph created", JOptionPane.PLAIN_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Unable to read graph from file", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
                break;

            case "CreateGraphGenerateButton":
                rangeOfWeights.first = Integer.parseInt(rangeFromField.getText());
                rangeOfWeights.second = Integer.parseInt(rangeToField.getText());

                graph.generateGraph(numberOfVertices, percentOfEdges,
                        rangeOfWeights.first, rangeOfWeights.second);

                showGraphButton.setText("Display graph");
                showGraphButton.setEnabled(true);

                if (graph.isNull())
                    JOptionPane.showMessageDialog(null,
                            "Graph successfully created. Edges missing!", "Attention", JOptionPane.WARNING_MESSAGE);
                else
                    JOptionPane.showMessageDialog(null,
                            "Graph successfully created!", "Graph created", JOptionPane.PLAIN_MESSAGE);
                break;

            case "EnterGraphButton":
                graph.deleteGraph();
                new GraphEnterFrame(this, graph);
                setVisible(false);
                break;

            case "ShowGraphButton":
                new ShowGraphFrame(this, graph);
                setVisible(false);
                break;

            case "BackButton":
                setVisible(false);
                dispose();
                mainMenuFrame.setVisible(true);
                break;

            default:
                break;
        }
    }
}
