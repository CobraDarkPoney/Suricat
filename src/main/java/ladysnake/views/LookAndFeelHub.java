package ladysnake.views;

import com.sun.istack.internal.NotNull;
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
    /*MULTI(new MultiLookAndFeel()),*/
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
    /**Constructs a {@link LookAndFeelHub} from a non-null {@link LookAndFeel}
     * @param laf being the {@link LookAndFeel} to construct from
     */
    LookAndFeelHub(@NotNull LookAndFeel laf){
        I_MightNoNullParams.assertNoneNull(laf);
        this.laf = laf;
    }

    /**Constructs a {@link LookAndFeelHub} from the classname of a {@link LookAndFeel}
     * @param classname being the {@link LookAndFeel}'s classname
     */
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
    /**Retrieves the {@link LookAndFeel} associated to this {@link LookAndFeelHub}
     * @return the associated {@link LookAndFeel}
     */
    public LookAndFeel getLookAndFeel(){  return this.laf; }
}
