import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;

class MyEditor implements ActionListener
{

    String fileName = "Untitled";
    boolean saved = true;
    String applicationName = "JavaEditor";
    String searchString, replaceString;
    int lastSearchIndex;
    FontChooser fontDialog = null;
    FindDialog findReplaceDialog = null;
    JColorChooser bcolorChooser = null;
    JColorChooser fcolorChooser = null;
    JDialog backgroundDialog = null;
    JDialog foregroundDialog = null;
    boolean newFileFlag;
    String applicationTitle = "JavaEditor";

    boolean isSave()
    {
        return saved;
    }


    boolean confirmSave()
    {
        String strMsg = "<html>The text in the " + fileName + " file has been changed.<br>" +
                        "Do you want to save the changes?<html>";
        if(!saved)
        {
            int x = JOptionPane.showConfirmDialog(jf, strMsg, applicationTitle, JOptionPane.YES_NO_CANCEL_OPTION);

            if(x == JOptionPane.CANCEL_OPTION) return false;
            if(x == JOptionPane.YES_OPTION)
            {
                try
                {
                    jta.write(new OutputStreamWriter(new FileOutputStream(file1),
                                                     "utf-8"));
                }
                catch(IOException eee)
                {
                    eee.printStackTrace();
                }
            }
        }
        return true;
    }


    void goTo()
    {
        int lineNumber = 0;
        try
        {
            lineNumber = jta.getLineOfOffset(jta.getCaretPosition()) + 1;
            String tempStr = JOptionPane.showInputDialog(jf, "Enter Line Number:", "" + lineNumber);
            if(tempStr == null)
            {
                return;
            }
            lineNumber = Integer.parseInt(tempStr);
            jta.setCaretPosition(jta.getLineStartOffset(lineNumber - 1));
        }
        catch(Exception e) {}
    }

    void showForegroundColorDialog()
    {
        if(fcolorChooser == null)
            fcolorChooser = new JColorChooser();
        if(foregroundDialog == null)
            foregroundDialog = JColorChooser.createDialog
                               (jf,
                                "Set Text color",
                                false,
                                fcolorChooser,
                                new ActionListener()
        {
            public void actionPerformed(ActionEvent evvv)
            {
                jta.setForeground(fcolorChooser.getColor());
            }
        },
        null);

        foregroundDialog.setVisible(true);
    }

    void showBackgroundColorDialog()
    {
        if(bcolorChooser == null)
            bcolorChooser = new JColorChooser();
        if(backgroundDialog == null)
            backgroundDialog = JColorChooser.createDialog
                               (jf,
                                "Set Pad Color",
                                false,
                                bcolorChooser,
                                new ActionListener()
        {
            public void actionPerformed(ActionEvent evvv)
            {
                jta.setBackground(bcolorChooser.getColor());
            }
        },
        null);

        backgroundDialog.setVisible(true);
    }


    MenuListener editMenuListener = new MenuListener()
    {
        public void menuSelected(MenuEvent evvvv)
        {
            if(jta.getText().length() == 0)
            {
                find.setEnabled(false);
                findNext.setEnabled(false);
                replace.setEnabled(false);
                selectAll.setEnabled(false);
                goTo.setEnabled(false);
            }
            else
            {
                find.setEnabled(true);
                findNext.setEnabled(true);
                replace.setEnabled(true);
                selectAll.setEnabled(true);
                goTo.setEnabled(true);
            }
            if(jta.getSelectionStart() == jta.getSelectionEnd())
            {
                cut.setEnabled(false);
                copy.setEnabled(false);
                delete.setEnabled(false);
            }
            else
            {
                cut.setEnabled(true);
                copy.setEnabled(true);
                delete.setEnabled(true);
            }
        }
        public void menuDeselected(MenuEvent evvvv) {}
        public void menuCanceled(MenuEvent evvvv) {}
    };


    JFrame jf;
    JLabel jl, jl1, jl2, jl3, jl4, jl5, statusBar;
    JTextField jtf;
    JTextArea jta, jta1, jta2;
    JButton jbcompile, jbrun;
    JScrollPane jsp, jsp1, jsp2;
    Runtime r;
    JMenuBar mb;
    JMenu file, edit, format, view;
    JMenuItem open, saveas, save, newFile, exit, undo, cut, copy, paste, delete, find, findNext, replace, goTo, selectAll,
              font, setTextColor, setPadColor;
    JCheckBoxMenuItem wordWrap;
    String str = "";
    String fname = "";
    String result = "";
    String result1 = "";
    JFileChooser fc = new JFileChooser();
    File file1;
    MyEditor()
    {
        jf = new JFrame(fileName + " - " + applicationName);
        jf.setLayout(null);
        jl = new JLabel("Java class Name:");
        jl.setBounds(50, 40, 170, 25);
        jl2 = new JLabel("Custom Input:");
        jl2.setBounds(50, 650, 170, 25);
        jl1 = new JLabel("Source Code:");
        jl1.setBounds(50, 90, 170, 25);
        jl3 = new JLabel("Program Size:");
        jl3.setBounds(1120, 650, 170, 25);
        jl4 = new JLabel("");
        jl4.setBounds(1290, 650, 170, 25);
        jl5 = new JLabel("Output");
        jl5.setBounds(1120, 110, 170, 25);
        jl5.setFont(new Font("SANS_SERIF", Font.PLAIN, 18));
        jl4.setFont(new Font("SANS_SERIF", Font.PLAIN, 18));
        jl3.setFont(new Font("SANS_SERIF", Font.PLAIN, 18));
        jl2.setFont(new Font("SANS_SERIF", Font.PLAIN, 18));
        jl1.setFont(new Font("SANS_SERIF", Font.PLAIN, 18));
        jl.setFont(new Font("SANS_SERIF", Font.PLAIN, 18));
        jtf = new JTextField();
        jtf.setBounds(230, 40, 230, 25);
        jta = new JTextArea(50, 50);
        jta.addFocusListener(new MyFocusListener(this));
        jta.setFont(new Font("Varinda", Font.PLAIN, 15));
        jta1 = new JTextArea(50, 50);
        jta1.setFont(new Font("Varinda", Font.PLAIN, 15));
        jta1.setEditable(false);
        jta2 = new JTextArea(50, 50);
        jta2.setFont(new Font("Varinda", Font.PLAIN, 15));
        jsp = new JScrollPane(jta);
        jsp1 = new JScrollPane(jta1);
        jsp2 = new JScrollPane(jta2);
        jsp.setBounds(50, 110, 1050, 500);
        jsp1.setBounds(1120, 110, 790, 500);
        jsp2.setBounds(50, 670, 1050, 150);
        jf.add(jsp);
        jf.add(jsp1);
        jf.add(jsp2);
        jbcompile = new JButton("Compile");
        jbrun = new JButton("Run");
        jbcompile.setBounds(50, 860, 100, 30);
        jbrun.setBounds(230, 860, 100, 30);
        jf.add(jl);
        jf.add(jl1);
        jf.add(jl2);
        jf.add(jl3);
        jf.add(jl4);
        jf.add(jl5);
        jf.add(jtf);
        r = Runtime.getRuntime();
        jf.add(jbcompile);
        jf.add(jbrun);
        jbcompile.addActionListener(this);
        jbrun.addActionListener(this);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setSize(2000, 1100);
        jf.setVisible(true);

        file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);
        newFile = new JMenuItem("New", KeyEvent.VK_N);
        newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));

        open = new JMenuItem("Open File", KeyEvent.VK_O);
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        saveas = new JMenuItem("Save As...", KeyEvent.VK_A);
        save = new JMenuItem("Save", KeyEvent.VK_S);
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        exit = new JMenuItem("Exit");
        open.addActionListener(this);
        saveas.addActionListener(this);
        save.addActionListener(this);
        exit.addActionListener(this);
        newFile.addActionListener(this);
        file.add(open);
        file.add(saveas);
        file.add(save);
        file.add(newFile);
        file.addSeparator();
        file.add(exit);

        edit = new JMenu("Edit");
        edit.setMnemonic(KeyEvent.VK_E);
        undo = new JMenuItem("Undo", KeyEvent.VK_U);
        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        cut = new JMenuItem("Cut", KeyEvent.VK_T);
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        copy = new JMenuItem("Copy", KeyEvent.VK_C);
        copy.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        paste = new JMenuItem("Paste", KeyEvent.VK_P);
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        delete = new JMenuItem("Delete", KeyEvent.VK_L);
        delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        find = new JMenuItem("Find...", KeyEvent.VK_F);
        find.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
        findNext = new JMenuItem("Find Next", KeyEvent.VK_N);
        findNext.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
        replace = new JMenuItem("Replace", KeyEvent.VK_R);
        replace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
        goTo = new JMenuItem("Goto", KeyEvent.VK_G);
        goTo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
        selectAll = new JMenuItem("Select All", KeyEvent.VK_A);
        selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        undo.addActionListener(this);
        cut.addActionListener(this);
        copy.addActionListener(this);
        paste.addActionListener(this);
        delete.addActionListener(this);
        find.addActionListener(this);
        findNext.addActionListener(this);
        replace.addActionListener(this);
        goTo.addActionListener(this);
        selectAll.addActionListener(this);
        edit.add(undo);
        edit.add(cut);
        edit.add(copy);
        edit.add(paste);
        edit.add(delete);
        edit.addSeparator();
        edit.add(find);
        edit.add(findNext);
        edit.add(replace);
        edit.add(goTo);
        edit.addSeparator();
        edit.add(selectAll);
        edit.addMenuListener(editMenuListener);


        format = new JMenu("Format");
        format.setMnemonic(KeyEvent.VK_O);
        wordWrap = new JCheckBoxMenuItem("Word Wrap");
        wordWrap.setMnemonic(KeyEvent.VK_W);
        font = new JMenuItem("Font...", KeyEvent.VK_F);
        setTextColor = new JMenuItem("Set Text Color...", KeyEvent.VK_T);
        setPadColor = new JMenuItem("Set Pad Color...", KeyEvent.VK_P);
        font.addActionListener(this);
        setTextColor.addActionListener(this);
        wordWrap.addActionListener(this);
        setPadColor.addActionListener(this);
        format.add(wordWrap);
        format.add(font);
        format.addSeparator();
        format.add(setTextColor);
        format.add(setPadColor);




        view = new JMenu("View");
        view.setMnemonic(KeyEvent.VK_V);
        LookAndFeelMenu.createLookAndFeelMenuItem(view, jf);


        mb = new JMenuBar();
        mb.setBounds(0, 0, 1500, 30);
        mb.add(file);
        mb.add(edit);
        mb.add(format);
        mb.add(view);
        jf.add(mb);

        statusBar = new JLabel("||       Ln 1, Col 1  ", JLabel.RIGHT);
        statusBar.setBounds(50, 375, 1050, 500);
        jf.add(statusBar);
        jta.addCaretListener(
            new CaretListener()
        {
            public void caretUpdate(CaretEvent e)
            {
                int lineNumber = 0, column = 0, pos = 0;

                try
                {
                    pos = jta.getCaretPosition();
                    lineNumber = jta.getLineOfOffset(pos);
                    column = pos - jta.getLineStartOffset(lineNumber);
                }
                catch(Exception excp) {}
                if(jta.getText().length() == 0)
                {
                    lineNumber = 0;
                    column = 0;
                }
                statusBar.setText("||       Ln " + (lineNumber + 1) + ", Col " + (column + 1));
            }
        });


        DocumentListener myListener = new DocumentListener()
        {
            public void changedUpdate(DocumentEvent e)
            {
                saved = false;
            }
            public void removeUpdate(DocumentEvent e)
            {
                saved = false;
            }
            public void insertUpdate(DocumentEvent e)
            {
                saved = false;
            }
        };
        jta.getDocument().addDocumentListener(myListener);

    }

    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == jbcompile)   //when compile Button is clicked.
        {

            str = "";
            if(!jtf.getText().equals(""))  //if u hava entered classname.
            {
                try
                {
                    //writing data of Source code textBox into File whose name is same as that of Class.
                    fname = jtf.getText().trim() + ".java";
                    FileWriter fw = new FileWriter(fname);
                    String s1 = jta.getText();
                    PrintWriter pw = new PrintWriter(fw);
                    pw.println(s1);
                    pw.flush();

                    Process error = r.exec("/usr/lib/jvm/java-8-oracle/bin/javac -d . " + fname);
                    BufferedReader err = new BufferedReader(new InputStreamReader(error.getErrorStream()));
                    while(true)
                    {
                        String temp = err.readLine();
                        if(temp != null)      //if COMPILATION ERROR, store in String str.
                        {
                            str += temp;
                            str += "\n";
                        }
                        else                  //if no error
                            break;
                    }
                    if(str.equals(""))    //if no error, print "Compilation Successfull"
                    {
                        jta1.setText("Compilation Successfull " + fname);
                        err.close();
                    }
                    else
                    {
                        jta1.setText(str);  // if COMPILATION ERROR, display it in output TextField.
                        str = "";
                    }

                    result1 = "";
                    result = "";

                    //calculating subprocess size
                    File file = new File(fname);
                    if (!file.exists() || !file.isFile()) return;
                    jl4.setText(file.length() + " B");

                }
                catch(Exception e1) {}
            }
            else
                jta1.setText("Please Enter the java Program name!");   //if class Name not filled
        }
        else if(e.getSource() == jbrun)    //when Run button is clicked.
        {
            int start = 0;
            try
            {
                String arguments = jta2.getText();
                if(!arguments.equals(""))
                {
                    FileOutputStream fout = new FileOutputStream("argument.txt");
                    byte[] buff = arguments.getBytes();
                    fout.write(buff);


                }
                String fn = jtf.getText().trim();
                Process p = r.exec("/usr/lib/jvm/java-8-oracle/bin/java " + fn);
                // p.getInputStream() returns the InputStream connected to the normal output of the subprocess.
                // InputStreamReader is a bridgeStream. It takes as argument an InputStream(or its subclass) and returns a Reader.
                BufferedReader output = new BufferedReader(new InputStreamReader(p.getInputStream()));

                //p.getErrorStream() returns the input stream connected to the error output of the subprocess.
                BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));

                //reading output generated by subprocess continuously
                while(true)
                {
                    String temp = output.readLine();
                    if(temp != null)    //if valid output is present.
                    {
                        result += temp;
                        result += "\n";
                    }
                    else
                    {
                        break;
                    }
                }
                //reading Runtime Errors generated by subprocess.
                while(true)
                {
                    String temp = error.readLine();
                    if(temp != null)  //if we get RuntimeException.
                    {
                        result1 += temp;
                        result1 += "\n";

                    }
                    else
                    {


                        break;
                    }
                }
                output.close();
                error.close();
                jta1.setText(result + "\n" + result1);  //display both RuntimeException as well as valid Output generated by subprocess.

            }
            catch(Exception ee) {}

        }
        else if(e.getSource() == open)    //if clicked On Open Button.
        {

            int i = fc.showOpenDialog(null);
            if(i == JFileChooser.APPROVE_OPTION)
            {
                File f = fc.getSelectedFile();
                String filepath = f.getPath();
                try
                {
                    BufferedReader br = new BufferedReader(new FileReader(filepath));
                    String s1 = "", s2 = "";
                    while((s1 = br.readLine()) != null)
                    {
                        s2 += s1 + "\n";
                    }
                    jta.setText(s2);
                    jtf.setText(fc.getSelectedFile().getName().replaceFirst("[.][^.]+$", ""));
                    statusBar.setText("File : " + filepath + " opened successfully");
                    file1 = new File(filepath);
                    fileName = fc.getSelectedFile().getName().replaceFirst("[.][^.]+$", "");
                    jf.setTitle(fileName + " - " + applicationName);
                    br.close();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }




        else if(e.getSource() == saveas)    //if clicked On saveas Button.
        {
            fc = new JFileChooser();
            int retval = fc.showSaveDialog(save);
            if (retval == JFileChooser.APPROVE_OPTION)
            {
                file1 = fc.getSelectedFile();
                if (file1 == null)
                {
                    return;
                }
                if (!file1.getName().toLowerCase().endsWith(".java"))
                {
                    file1 = new File(file1.getParentFile(), file1.getName() + ".java");
                }
                try
                {
                    jta.write(new OutputStreamWriter(new FileOutputStream(file1),
                                                     "utf-8"));
                    saved = true;
                    statusBar.setText("File : " + file1.getPath() + " saved successfully");

                }
                catch (Exception er)
                {
                    er.printStackTrace();
                }
            }
        }



        else if(e.getSource() == save)    //if clicked On save Button.
        {
            fc = new JFileChooser();
            if(file1 == null)
            {
                int retval = fc.showSaveDialog(save);
                if (retval == JFileChooser.APPROVE_OPTION)
                {
                    file1 = fc.getSelectedFile();
                    if (file1 == null)
                    {
                        return;
                    }
                    if (!file1.getName().toLowerCase().endsWith(".java"))
                    {
                        file1 = new File(file1.getParentFile(), file1.getName() + ".java");
                    }
                    try
                    {
                        jta.write(new OutputStreamWriter(new FileOutputStream(file1),
                                                         "utf-8"));
                        saved = true;
                        statusBar.setText("File : " + file1.getPath() + " saved successfully");

                    }
                    catch (Exception er)
                    {
                        er.printStackTrace();
                    }
                }
            }
            else
            {
                try
                {
                    jta.write(new OutputStreamWriter(new FileOutputStream(file1),
                                                     "utf-8"));
                    saved = true;
                    statusBar.setText("File : " + file1.getPath() + " saved successfully");


                }
                catch (Exception err)
                {
                    err.printStackTrace();
                }

            }
        }


        else if(e.getSource() == newFile)  
        {
            if(confirmSave())
            {
                jta.setText("");
                jtf.setText("");
                file1 = null;

                saved = false;
                jf.setTitle("Untitled" + " - " + applicationTitle);
                fileName = "Untitled";
            }

        }
        else if(e.getSource() == exit)  
            System.exit(0);

        else if(e.getSource() == cut)   
            jta.cut();

        else if(e.getSource() == copy)   
            jta.copy();

        else if(e.getSource() == paste)  
            jta.paste();

        else if(e.getSource() == delete)   
            jta.replaceSelection("");

        else if(e.getSource() == find)  
        {
            if(jta.getText().length() == 0)
                return; // text box have no text
            if(findReplaceDialog == null)
                findReplaceDialog = new FindDialog(jta);
            findReplaceDialog.showDialog(jf, true); //find


        }

        else if(e.getSource() == findNext)   
        {
            if(jta.getText().length() == 0)
                return; // text box have no text

            if(findReplaceDialog == null)
                statusBar.setText("Nothing to search for, use Find option of Edit Menu first !!!!");
            else
                findReplaceDialog.findNextWithSelection();


        }

        else if(e.getSource() == replace)   
        {
            if(jta.getText().length() == 0)
                return; // text box have no text

            if(findReplaceDialog == null)
                findReplaceDialog = new FindDialog(jta);
            findReplaceDialog.showDialog(jf, false); 

        }

        else if(e.getSource() == goTo)  
        {
            if(jta.getText().length() == 0)
                return; // text box have no text
            goTo();


        }


        else if(e.getSource() == selectAll)   
            jta.selectAll();

        else if(e.getSource() == font)   
        {
            if(fontDialog == null)
                fontDialog = new FontChooser(jta.getFont());

            if(fontDialog.showDialog(jf, "Choose a font"))
                jta.setFont(fontDialog.createFont());
            jta1.setFont(fontDialog.createFont());
            jta2.setFont(fontDialog.createFont());

        }


        else if(e.getSource() == setTextColor)   
            showForegroundColorDialog();

        else if(e.getSource() == setPadColor)  
            showBackgroundColorDialog();

        else if(e.getSource() == wordWrap)
            jta.setLineWrap(wordWrap.isSelected());


    }

    public static void main(String[] args)
    {
        new MyEditor();
    }
}

class MyFocusListener extends FocusAdapter
{
    MyEditor e;
    MyFocusListener(MyEditor e)
    {
        this.e = e;
    }
    public void focusGained(FocusEvent fe)
    {
        if(e.jta.getText().equals("") && !(e.jtf.getText().trim().equals("")))
        {
            String str = e.jtf.getText().trim();
            e.jf.setTitle(str + " - " + e.applicationName);
            e.fileName = e.jtf.getText().trim();
            e.jta.setText("public class " + str + "\n" + "{" + "\n" + "public static void main(String []args)" + "\n" + "{" + "\n"  + "System.setIn(new FileInputStream(\"argument.txt\"));" + "\n" + "\n" + "}" + "\n" + "}");
        }
    }
}