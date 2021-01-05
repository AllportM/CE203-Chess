package Assignment2;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/*
 * GBLPattern is a composite design pattern used to group a collection of objects with a common method together.
 * Its purpose in this instance is to create a group of JComponents together alongside their grid bag constraints
 * and apply and add them to a JPanel.
 */
interface GBLPattern {

    void addGBL(GridBagConstraints gbc, GridBagLayout gbl, JPanel panel); // main method to add to JPanel
}

/*
 * The GBLLeaf class's main purpose is to add individual JComponents alongside their grid bag constraints
 */
class GBLLeaf implements GBLPattern
{
    Insets inset; // padding for the component
    int width, height, x, y; // positional values
    JComponent component; // JComponent for constraints to be applied

    /*
     * Constructor for where only posiion values are needed, using defaults for other variables
     *
     * @params
     *      x, x positional value
     *      y, y positional value
     *      component, JComponent to which grid bag constraints are to be applied
     */
    GBLLeaf(int x, int y, JComponent component)
    {
        this.component = component;
        width = 1;
        height = 1;
        this.x = x;
        this.y = y;
        inset = new Insets(0,0,0,0);
    }

    /*
     * Constructor for where width and heights are also needed with default insets
     *
     * @params
     *      x, x positional value
     *      y, y positional value
     *      width, how many columns the component is to take up
     *      height, how many rows the column is to take up
     *      component, JComponent to which grid bag constraints are to be applied
     */
    GBLLeaf(int x, int y, int width, int height, JComponent component)
    {
        this.component = component;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        inset = new Insets(0,0,0,0);
    }

    /*
     * Constructor for where insets are also specified, the most used one
     *
     * @params
     *      x, x positional value
     *      y, y positional value
     *      width, how many columns the component is to take up
     *      height, how many rows the column is to take up
     *      inset, the padding for the component
     *      component, JComponent to which grid bag constraints are to be applied
     */
    GBLLeaf(int x, int y, int width, int height, Insets inset, JComponent component)
    {
        this.component = component;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.inset = inset;
    }

    // overrides the main method to add the component and constraints to the panel
    @Override
    public void addGBL(GridBagConstraints gbc, GridBagLayout gbl, JPanel panel) {
        gbc.gridwidth = width;
        gbc.gridheight = height;
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.insets = inset;
        gbl.setConstraints(component, gbc);
        panel.add(component);
    }
}

/*
 * ComplexGBL's purpose is to store a collection of components, and call the individual addGBL method upon
 * each of the stored GBLLeafs
 */
class ComplexGBL implements GBLPattern
{
    private List<GBLPattern> gblList; // collection of GBLLeafs

    // default no argument constructor instantiating the list
    ComplexGBL()
    {
        gblList = new ArrayList<>();
    }

    /*
     * addToLists's purpose is to add a GBLLeaf, or other ComplexGBL, to its list
     *
     * @param
     *      par, a GBLPattern object to add to list
     */
    void addToList(GBLPattern pat)
    {
        gblList.add(pat);
    }

    // overrides main addGBL
    @Override
    public void addGBL(GridBagConstraints gbc, GridBagLayout gbl, JPanel panel) {
        for (GBLPattern pat: gblList)
        {
            pat.addGBL(gbc, gbl, panel);
        }
    }
}
