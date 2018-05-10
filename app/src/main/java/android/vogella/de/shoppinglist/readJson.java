package android.vogella.de.shoppinglist;

import android.os.NetworkOnMainThreadException;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class readJson{
    public String line;
    public readJson() {
        try {
            String path ="http://10.0.2.2//syafi/jsonfile.json";
            URL u = new URL(path);
            HttpURLConnection c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();
            InputStream in = c.getInputStream();
            Log.e("value",in.toString());
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            in.read(buffer); // Read from Buffer.
            bo.write(buffer); // Write Into Buffer.
            bo.close();
            line = bo.toString();
        }
        catch (NetworkOnMainThreadException e) {
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getData(){
        return line;
    }

}
