package Assignment;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.awt.GridBagConstraints.LINE_START;

/*
 * CE203 Assignment 1
 * CE203_2019_Ass1.java
 * Purpose: Main application for Assignment 1
 *
 * @author Michael Allport Ma18533/Mal
 * @version 1
 */

/*
 * Class: CE203_2019_Ass1
 * Purpose: Creates the top level JFrame, drawing various JPanels, adding buttons, text fields, and labels as necessary
 * to draw the main application whilst adding button listeners and focus listeners of object ButtonHandler and
 * RGBHandler respectively. Input handler is used which contains the main List object and various member functions
 * to ensure correct input is given.
 */
public class CE203_2019_Ass1 extends JFrame
{
    JTextArea outputArea; // member variable used for non member classes to alter, package protected variable
    JTextField inputMain, inputR, inputG, inputB; // member variables used for non member classes to read/change inputs
    JButton butAdd, butFind, butRmv, butClr; //member variable used for buttonHandler to read text value
    private int size = 600; // member variable used for window size
    InputListHandler arr; // member variable used for applications main array handler

    /*
     * Default non argument constructor, initialises input handler, calls drawFrame to add panels, sets default JFrame
     * behaviour and attaches event listeners
     */
    private CE203_2019_Ass1()
    {
        arr = new InputListHandler();
        setSize(size+100, size+100);
        drawFrame();
        attachListeners();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("CE203 Assignment - Ma18533");
        setResizable(false);
    }

    /*
     * Attaches listeners to both buttons and RGB input fields
     *
     * @return void
     */
    private void attachListeners ()
    {
        ButtonHandler bh = new ButtonHandler(this);
        butAdd.addActionListener(bh);
        butFind.addActionListener(bh);
        butRmv.addActionListener(bh);
        butClr.addActionListener(bh);
    }

    /*
     * Initialises and adds the main panel components to the default border layout of the main JFrame
     *
     * @return - void
     */
    private void drawFrame()
    {

        /*
         * Creates the Button JPanel to the North section of the JFrame and applies a border
         */
        JPanel butPanel = new JPanel(new GridLayout(1, 4, 20, 20));
        Border butBord = BorderFactory.createEmptyBorder(20, 20, 20, 20);
        butAdd = new JButton("Add Word");
        butFind = new JButton("Find Word");
        butRmv = new JButton("Remove Word");
        butClr = new JButton("Clear List");
        butPanel.setBorder(butBord); // adds border to panel
        butPanel.add(butAdd);
        butPanel.add(butFind);
        butPanel.add(butRmv);
        butPanel.add(butClr);
        add(butPanel, BorderLayout.NORTH); // adds to top level JFrame

        /*
         * Creates the output panel to the center of the top level JFrame with desired border
         */
        outputArea = new JTextArea();
        Border outBord = BorderFactory.createEmptyBorder(0, 20, 0, 20);
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setText(" > Output Panel:\n\nSimple application to store words into a list. Acceptable" +
                " word format begins with a letter, a-z or A-Z, followed by a concatenation of any characters" +
                " a-z or A-Z, numbers, or the symbol ' - '.\n\nUser can also find words ending with specific" +
                " character, remove all words of a given input word from the list, or clear the list.\n\n" +
                "Additionally, user can adjust the colour of the font by adjusting Red, Green, or Blue values" +
                " between 0-255 inclusive, which updates upon function button clicks.\n");
        JScrollPane textScrollPanel = new JScrollPane(outputArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        textScrollPanel.setBorder(outBord);
        add(textScrollPanel, BorderLayout.CENTER);

        /*
         * Creates input JPanel consisting of various input fields and labels using GridBagLayout
         */
        JPanel inputPanel = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        inputPanel.setLayout(gbl);
        GridBagConstraints gbc = new GridBagConstraints();
        Insets topRowInset = new Insets(10,0,5,10);
        Insets botRowInset = new Insets(0,0,10,10);

        // Creates label for the main input word
        JLabel inputTitle = new JLabel("Input Word");
        gbc.anchor = LINE_START;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.insets = topRowInset;
        gbl.setConstraints(inputTitle, gbc);
        inputPanel.add(inputTitle);

        //creates text field for main input below label
        inputMain = new JTextField(20);
        gbc.anchor = LINE_START;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = botRowInset;
        gbl.setConstraints(inputMain, gbc);
        inputPanel.add(inputMain);

        // creates Red label
        JLabel redTit = new JLabel("Red");
        gbc.anchor = LINE_START;
        gbc.weightx = 0;
        gbc.gridy = 0;
        gbc.gridx = 3;
        gbc.gridwidth = 1;
        gbc.insets = topRowInset;
        gbl.setConstraints(redTit, gbc);
        inputPanel.add(redTit);

        // creates Red text field with default string value 0
        inputR = new JTextField("0", 5);
        gbc.gridy = 1;
        gbc.insets = botRowInset;
        gbl.setConstraints(inputR, gbc);
        inputPanel.add(inputR);

        // creates Green label
        JLabel greenTit = new JLabel("Green");
        gbc.anchor = LINE_START;
        gbc.gridy = 0;
        gbc.gridx = 4;
        gbc.insets = topRowInset;
        gbl.setConstraints(greenTit, gbc);
        inputPanel.add(greenTit);

        // creates green text field with default string value of 0
        inputG = new JTextField("0", 5);
        gbc.gridy = 1;
        gbc.insets = botRowInset;
        gbl.setConstraints(inputG, gbc);
        inputPanel.add(inputG);

        // creates final column Blue title
        topRowInset.right = 0;
        botRowInset.right = 0;
        JLabel blueTit = new JLabel("Blue");
        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.insets = topRowInset;
        gbl.setConstraints(blueTit, gbc);
        inputPanel.add(blueTit);

        // creates Blue text field
        inputB = new JTextField("0", 5);
        gbc.gridx = 5;
        gbc.gridy = 1;
        gbc.insets = botRowInset;
        gbl.setConstraints(inputB, gbc);
        inputPanel.add(inputB);

        inputPanel.setBorder(outBord);
        add(inputPanel, BorderLayout.SOUTH);
    }

    /*
     * Function created to insert input text into main output field with ' > ' character denoting output
     *
     * @param - String, text to add to outfput field
     * @return - void
     */
    void setOutput(String output)
    {
        outputArea.append("\n > " + output + "\n");
    }

    /*
     * Function called to empty input word field, and set the scroll bar to the bottom of the area so that when output
     * field is full it automatically shows the user the last entry, and bring inputMain back in view
     *
     * @return - void
     */
    void resetInputUpdteScroll()
    {
        inputMain.setText("");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
        inputMain.requestFocus();
    }

    /*
     * main method
     */
    public static void main(String[] args)
    {
        CE203_2019_Ass1 show = new CE203_2019_Ass1();
    }
}

/*
 * Class: ButtonHandler implementing the desired action for button click events
 */
class ButtonHandler implements ActionListener
{
    private CE203_2019_Ass1 app; // member variable used to access the main application object
    private RGBHandler rgb; // member variable used to apply RGB changes

    /*
     * Constructor, initialising the top level JFrame as private member variable
     *
     * @param - CE203_2019_Ass1, the top level JFrame component
     */
    ButtonHandler(CE203_2019_Ass1 theApp)
    {
        app = theApp;
        rgb = new RGBHandler(app);
    }

    /* Overrides the interfaces methods
     */
    @Override
    public void actionPerformed (ActionEvent e)
    {
        /*
         * Sets the String constant to identify which button was clicked upon the event
         */
        String button = (e.getSource() == app.butAdd) ? "Add Word" :
                (e.getSource() == app.butFind) ? "Find Word" :
                        (e.getSource() == app.butRmv) ? "Remove Word" :
                                (e.getSource() == app.butClr) ? "Clear List" : "";
        String word = app.inputMain.getText(); // gets main input word value
        switch (button)
        {
            // checks word format and adds to app's arr List
            case "Add Word" :
                if (app.arr.checkWordFormat(word))
                {
                    app.arr.addWord(word);
                    app.setOutput("The word '" + word + "' has been added.");
                    app.resetInputUpdteScroll();
                }
                else app.setOutput("Sorry, the string '" + word + "' is of invalid format.");
                app.resetInputUpdteScroll();
                break;
            // finds word, initially checking correct format via checkCharFormat, then finding the word via findWord
            case "Find Word" :
                if (word.length() == 1)
                {
                    if (!app.arr.checkCharFormat(word))
                    {
                        app.setOutput("Sorry, the string ' " + word + " ' is of invalid format, acceptable characters" +
                                " are a-z, A-Z, 0-9, and ' - '");
                        app.resetInputUpdteScroll();
                    }
                    else
                    {
                        String outPutstring = "";
                        List<String> matched = app.arr.findWord(word);
                        if (matched.size() > 0)
                        {
                            for (Iterator<String> matchedIt = matched.iterator(); matchedIt.hasNext();)
                            {
                                String hit = matchedIt.next();
                                outPutstring += (matchedIt.hasNext()) ? hit + ", " : hit + ".";
                            }
                            app.setOutput("Words found!\n > " + outPutstring);
                            app.resetInputUpdteScroll();
                        }
                        else
                        {
                            app.setOutput("Sorry, no words found with ' " + word + " '.");
                            app.resetInputUpdteScroll();
                        }
                    }
                }
                else
                {
                    app.setOutput("Sorry, the string '" + word + "' is of invalid format, please use" +
                            " singular characters only");
                    app.resetInputUpdteScroll();
                }
                break;
            // removes word
            case "Remove Word" :
                String output = (app.arr.removeWord(word)) ? "String ' " + word + " ' has successfully been removed.":
                        "There were no occurrences of ' " + word + " ' to remove.";
                app.setOutput(output);
                app.resetInputUpdteScroll();
                break;
            // clears list
            case "Clear List" :
                app.arr.clearList();
                app.setOutput("List has been cleared.");
                app.resetInputUpdteScroll();
                break;
        }
        // calls RGBHandler to handle changing of font colour and input validation
        rgb.changeMain();
    }
}

/*
 * Class: RGBHandler implementing the main rgb control functions to change colour, whilst providing data validation
 * and error catching.
 */
class RGBHandler
{
    private CE203_2019_Ass1 app; // member variable used to access main app object
    private String redInit, blueInit, greenInit; // member variable used to store initial and validly altered RGB values

    /*
     * Constructor initializing the app reference, and initial red/green/blue value strings
     *
     * @param - CE203_2019_Ass1, the top level JFrame component
     */
    RGBHandler(CE203_2019_Ass1 parent)
    {
        this.app = parent;
        redInit = app.inputR.getText();
        blueInit = app.inputB.getText();
        greenInit = app.inputG.getText();
    }

    /*
     * Function called to handle integer type casting
     *
     * @param - String, input
     * @return - int, negative value for either negative type casted integer or caught error (negative indicates error)
     */
    private int validInt(String input)
    {
        int result = -1;
        try
        {
            result = Integer.parseInt(input);
            return result;
        }
        catch (NumberFormatException e)
        {
            return result;
        }
    }

    /*
     * Function called to check if integer is within desired range
     *
     * @param - int, input
     * @return - int, 0 for valid range
     */
    private int validRange(int input)
    {
        return input < 0 ? -1 : input > 255 ? 1 : 0;
    }

    /*
     * Main function to either change the output text fields foreground colour given valid inputs, or display popup
     * error message in app's root pane if ivalid inputs detected
     */
    void changeMain()
    {
        // initializes variables
        boolean rChanged, gChanged, bChanged;
        boolean invalidInt = false, invalidRange = false;
        String currRed = app.arr.removeLeadingZeros(app.inputR.getText()),
                currBlue = app.arr.removeLeadingZeros(app.inputB.getText()),
                currGreen = app.arr.removeLeadingZeros(app.inputG.getText());
        String invalidRangeString = "" , invalidIntString = "";
        int red = Integer.parseInt(redInit), green = Integer.parseInt(greenInit), blue = Integer.parseInt(blueInit);

        // checks if any fields have been changed
        rChanged = !currRed.equals(redInit);
        gChanged = !currGreen.equals(greenInit);
        bChanged = !currBlue.equals(blueInit);

        // does input validation on red input if changed, changing boolean control variables if invalid
        if (rChanged)
        {
            red = validInt(currRed);
            if (red < 0)
            {
                invalidIntString += "Red colour input '" + currRed + "' not a valid positive integer.\n";
                invalidInt = true;
            }
            else
            {
                if (validRange(red) != 0)
                {
                    invalidRangeString += "Red colour input '" + currRed + "' not in range 0-255.\n";
                    invalidRange = true;
                }
            }
        }

        // does input validation on green input if changed, changing boolean control variables if invalid
        if (gChanged)
        {
            green = validInt(currGreen);
            if (green < 0)
            {
                invalidIntString += "Green colour input '" + currGreen + "' not a valid positive integer.\n";
                invalidInt = true;
            }
            else
            {
                if (validRange(green) != 0)
                {
                    invalidRangeString += "Green colour input '" + currGreen + "' not in range 0-255.\n";
                    invalidRange = true;
                }
            }
        }

        // does input validation on blue input if changed, changing boolean control variables if invalid
        if (bChanged)
        {
            blue = validInt(currBlue);
            if (blue < 0)
            {
                invalidIntString += "Blue colour input '" + currBlue + "' not a valid positive integer.\n";
                invalidInt = true;
            }
            else
            {
                if (validRange(blue) != 0)
                {
                    invalidRangeString += "Red colour input '" + currBlue + "' not in range 0-255.\n";
                    invalidRange = true;
                }
            }
        }

        // displays desire error message upon invalid inputs
        if (invalidInt || invalidRange)
        {
            String output = "";
            if (invalidRangeString.length() > 0) output += "Invalid range used:\n" + invalidRangeString + "\n";
            if (invalidIntString.length() > 0) output += "Invalid integer used:\n" + invalidIntString;
            app.inputR.setText(redInit);
            app.inputG.setText(greenInit);
            app.inputB.setText(blueInit);
            JOptionPane.showMessageDialog(app.getRootPane(), output);
        }
        // changes output text-field's colour if input is valid
        else
        {
            redInit = currRed;
            greenInit = currGreen;
            blueInit = currBlue;
            app.inputR.setText(currRed);
            app.inputG.setText(currGreen);
            app.inputB.setText(currBlue);
            app.outputArea.setForeground(new Color(red, green, blue));
        }
    }
}

/*
 * Class: InputListHandler consists of member functions to check input validity, format number strings, remove/find/add
 * words within the member List
 */
class InputListHandler {
    private List<String> lib; // member list

    /*
     * Default no argument constructor initializing new list as member variable
     */
    InputListHandler()
    {
        lib = new ArrayList<>();
    }

    /*
     * Function called to check input String is correct format utilizing regular expression
     * Correct format single character followed by concatenation of characters, numbers, or ' - '
     *
     * @param - String, input
     * @return - Boolean, word correct = true, incorrect = false
     */
    boolean checkWordFormat (String input)
    {
        Pattern p = Pattern.compile("^[a-zA-Z]+[a-zA-Z0-9-]+$");
        Matcher match = p.matcher(input);
        return match.matches();
    }

    /*
     * Function called to check input String is correct format utilizing regular expression
     * Correct format single character in number or letter form
     *
     * @param - String, input
     * @return - Boolean, word correct = true, incorrect = false
     */
    boolean checkCharFormat(String in)
    {
        Pattern p = Pattern.compile("[a-zA-Z0-9-]");
        Matcher match = p.matcher(in);
        return match.matches();
    }

    /*
     * Function to add input String to member list variable
     */
    void addWord (String input)
    {
        lib.add(input);
    }

    /*
     * Function called to remove all leading 0's from input string
     *
     * @param - String, input
     * @return - String, output
     */
    String removeLeadingZeros (String input)
    {
        while (input.charAt(0) == "0".charAt(0) && input.length() > 1)
        {
            input = input.substring(1);
        }
        return input;
    }

    /*
     * Function called to find and remove all occurrence of input string from member list variable
     *
     * @return - Boolean, word found and removed = true, word not found and removed = false
     */
    boolean removeWord (String input)
    {
        int count = 0;
        // iterates over each word in member list, removing where input matches and increasing count
        for (Iterator<String> arrIt = lib.iterator(); arrIt.hasNext();)
        {
            String word = arrIt.next();
            if (word.equals(input))
            {
                arrIt.remove();
                count += 1;
            }
        }
        return (count > 0) ? true: false;
    }

    /*
     * Function called to reset member list variable
     */
    void clearList ()
    {
        lib = new ArrayList<>();
    }

    /*
     * Function called to find words ending with input character, returning found words as a new list of results.
     * Input/output considered case-insensitive
     *
     * @param - Character, input
     * @return - List, array of found words with last character matching input character
     */
    List<String> findWord (String find)
    {
        List<String> matched = new ArrayList<>();
        // iterates over each word in member list adding matched words to output list
        for (Iterator<String> libIt = lib.iterator(); libIt.hasNext();)
        {
            String word = libIt.next();
            if (word.toLowerCase().charAt(word.length() - 1) == find.toLowerCase().charAt(0)) matched.add(word);
        }
        return matched;
    }
}
