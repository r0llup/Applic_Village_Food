/**
 * Applic_Village_Food
 *
 * Copyright (C) 2012 Sh1fT
 *
 * This file is part of Applic_Village_Food.
 *
 * Applic_Village_Food is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 *
 * Applic_Village_Food is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Applic_Village_Food; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 */

package applic_village_food;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Properties;
import org.xml.sax.InputSource;
import utils.PropertiesLauncher;

/**
 * Manage a {@link Applic_Village_Food}
 * @author Sh1fT
 */
public class Applic_Village_Food {
    private PropertiesLauncher propertiesLauncher;
    private Boolean logged;

    /**
     * Create a new {@link Applic_Village_Food} instance
     */
    public Applic_Village_Food() {
        this.setPropertiesLauncher(new PropertiesLauncher(
                System.getProperty("file.separator") + "properties" +
                System.getProperty("file.separator") + "Applic_Village_Food.properties"));
        this.setLogged(false);
    }

    /**
     * Launch
     */
    public void launch() {
        try {
            Socket socket = new Socket(this.getServerAddress(), this.getServerPort());
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            InputSource is = new InputSource(new InputStreamReader(socket.getInputStream()));
            BufferedReader br = new BufferedReader(is.getCharacterStream());
            pw.println("DOWNMENU");
            String serverURI = br.readLine();
            pw.close();
            br.close();
            socket.close();
            Desktop.getDesktop().browse(new URI(serverURI));
        } catch (UnknownHostException ex) {
            System.out.println("Error: " + ex.getLocalizedMessage());
            System.exit(1);
        } catch (IOException | URISyntaxException ex) {
            System.out.println("Error: " + ex.getLocalizedMessage());
            System.exit(1);
        }
    }

    /**
     * Do the login
     * @return 
     */
    public Boolean doLogin() {
        try {
            Socket socket = new Socket(this.getServerAddress(), this.getServerPort());
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            InputSource is = new InputSource(new InputStreamReader(socket.getInputStream()));
            BufferedReader br = new BufferedReader(is.getCharacterStream());
            pw.println("LOGINVILLAGE:"+this.getAuthUsername()+":"+this.getAuthPassword());
            String result = br.readLine();
            if ((result != null) && (result.compareToIgnoreCase("OK") == 0))
                return true;
            return false;
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getLocalizedMessage());
            System.exit(1);
        }
        return null;
    }

    public PropertiesLauncher getPropertiesLauncher() {
        return propertiesLauncher;
    }

    public void setPropertiesLauncher(PropertiesLauncher propertiesLauncher) {
        this.propertiesLauncher = propertiesLauncher;
    }

    public Boolean getLogged() {
        return logged;
    }

    public void setLogged(Boolean logged) {
        this.logged = logged;
    }

    public Properties getProperties() {
        return this.getPropertiesLauncher().getProperties();
    }

    public String getServerAddress() {
        return this.getProperties().getProperty("serverAddress");
    }

    public Integer getServerPort() {
        return Integer.parseInt(this.getProperties().getProperty("serverPort"));
    }

    public String getAuthUsername() {
        return this.getProperties().getProperty("authUsername");
    }

    public String getAuthPassword() {
        return this.getProperties().getProperty("authUsername");
    }

    public static void main(String args[]) {
        Applic_Village_Food avf = new Applic_Village_Food();
        if (avf.doLogin()) {
            avf.setLogged(true);
            avf.launch();
        }
    }
}