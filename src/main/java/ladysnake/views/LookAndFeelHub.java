package ladysnake.views;

import com.sun.java.swing.plaf.motif.MotifLookAndFeel;
import com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel;
import ladysnake.helpers.utils.I_MightNoNullParams;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.multi.MultiLookAndFeel;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.plaf.synth.SynthLookAndFeel;

public enum LookAndFeelHub {
    NIMBUS(new NimbusLookAndFeel()),
    METAL(new MetalLookAndFeel()),
    MULTI(new MultiLookAndFeel()),
    WINDOWS_CLASSIC(new WindowsClassicLookAndFeel()),
    SYNTH(new SynthLookAndFeel()),
    MOTIF(new MotifLookAndFeel()),
    OS_DEFAULT(UIManager.getSystemLookAndFeelClassName());

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Properties
    ////////////////////////////////////////////////////////////////////////////////////////////
    LookAndFeel laf;


    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////
    LookAndFeelHub(LookAndFeel laf){
        I_MightNoNullParams.assertNoneNull(laf);
        this.laf = laf;
    }

    LookAndFeelHub(String classname){
        try {
            this.laf = (LookAndFeel) (Class.forName(classname).newInstance());
        }catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            this.laf = null;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    ////Methods
    ////////////////////////////////////////////////////////////////////////////////////////////
    public LookAndFeel getLookAndFeel(){  return this.laf; }
}
