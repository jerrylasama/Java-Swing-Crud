import mdlaf.MaterialLookAndFeel;
import javax.swing.UIManager;
import ui.TableFrame;

public class Main{

    public static void main(String args[])
    {
        // Mencoba menghubungkan ke database
        try
        {
            // Aktifkan tema material design
            UIManager.setLookAndFeel (new MaterialLookAndFeel ());
            new TableFrame();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}