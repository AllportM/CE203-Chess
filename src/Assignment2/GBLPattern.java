package Assignment2;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

interface GBLPattern {

    abstract void addGBL(GridBagConstraints gbc, GridBagLayout gbl, JPanel panel);
}

class GBLLeaf implements GBLPattern
{
    Insets inset;
    int width, height, x, y;
    JComponent component;
    GBLLeaf(int x, int y, JComponent component)
    {
        this.component = component;
        width = 1;
        height = 1;
        this.x = x;
        this.y = y;
        inset = new Insets(0,0,0,0);
    }
    GBLLeaf(int x, int y, int width, int height, JComponent component)
    {
        this.component = component;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        inset = new Insets(0,0,0,0);
    }

    GBLLeaf(int x, int y, int width, int height, Insets inset, JComponent component)
    {
        this.component = component;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.inset = inset;
    }

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

class ComplexGBL implements GBLPattern
{
    private List<GBLPattern> gblList;
    ComplexGBL()
    {
        gblList = new ArrayList<>();
    }

    void addToList(GBLPattern pat)
    {
        gblList.add(pat);
    }

    @Override
    public void addGBL(GridBagConstraints gbc, GridBagLayout gbl, JPanel panel) {
        for (GBLPattern pat: gblList)
        {
            pat.addGBL(gbc, gbl, panel);
        }
    }
}
