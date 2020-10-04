package AnimeNinja;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class OsDetector {
    public static String appName;
    public static String updaterBatchName;
    public static String cmdName;
    public static File installationDirectory;
    public static String driverName;
    public static ArrayList<File> chromePaths = new ArrayList<File>();
    public static ArrayList<String> runShellCommands = new ArrayList<String>();
    private static String lookAndFeel;

    public static void detectAndSetup()
    {
        String osName = System.getProperty("os.name");
        if(osName.contains("Windows"))
        {
            if(!System.getProperty("user.dir").toLowerCase().contains("desktop"))
            {
                JOptionPane.showMessageDialog(null , "Please put the program in the Desktop"
                        ,"Anime Ninja",JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            closeUpdateWindow();
            appName = "Anime.Ninja.exe";
            updaterBatchName = "updater.bat";
            runShellCommands.add("cmd");
            runShellCommands.add("/C");
            runShellCommands.add("start");
            installationDirectory = new File(System.getProperty("user.home")+"\\AppData\\Local\\Anime Ninja");
            driverName = "chromedriver.exe";
            chromePaths.add(new File("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe"));
            chromePaths.add(new File("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe"));
            lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
        }
        else
        {
            appName = "Anime.Ninja.jar";
            updaterBatchName = "updater.sh";
            runShellCommands.add("sh");
            installationDirectory = new File(System.getProperty("user.home")+"/.local/share/Anime Ninja");
            driverName = "chromedriver";
            chromePaths.add(new File("/opt/google/chrome"));
            lookAndFeel = "javax.swing.plaf.metal.MetalLookAndFeel";
        }
        runShellCommands.add(updaterBatchName);
    }

    public static void setLookAndFeel()
    {
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }
    private static void closeUpdateWindow()
    {
        File updateBatch = new File("updater.bat");
        while(true)
        {
            if(updateBatch.exists())
            {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                try {
                    Runtime.getRuntime().exec("taskkill /F /IM cmd.exe");
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
