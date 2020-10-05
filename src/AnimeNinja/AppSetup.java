package AnimeNinja;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class AppSetup {

    public boolean checkChromeInstallation()
    {
        for(int i=0 ; i<OsDetector.chromePaths.size() ; i++)
        {
            System.out.println(OsDetector.chromePaths.get(i).getAbsolutePath());
            if(OsDetector.chromePaths.get(i).exists())
                return true;
        }
        JOptionPane.showMessageDialog(null, "This program requires Google Chrome to be installed\nPlease install it and try again"
                , "Anime Ninja", JOptionPane.WARNING_MESSAGE);
        try {
            Desktop.getDesktop().browse(new URI("https://www.google.com/intl/en_us/chrome/"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void checkInstallationDirectory()
    {
        if(!OsDetector.installationDirectory.exists())
            OsDetector.installationDirectory.mkdir();
    }
    public boolean checkChromeDriver()
    {
        File chromeDriver = new File(OsDetector.installationDirectory+File.separator+OsDetector.driverName);
        if(chromeDriver.exists())
            return true;
        else
            return false;
    }

}
