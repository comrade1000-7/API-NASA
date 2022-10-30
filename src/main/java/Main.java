import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.net.URL;

public class Main {
    public static ObjectMapper objectMapper = new ObjectMapper();
    public static String URL = "https://api.nasa.gov/planetary/apod?api_key=eLq8SfoxkDPkwB0f3pXW8Kgyd2TRwCu7AJXpoWNf";

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet request = new HttpGet(URL);
        CloseableHttpResponse response = httpClient.execute(request);

        HDUrl hdUrl = objectMapper.readValue(response.getEntity().getContent().readAllBytes(), HDUrl.class);

        String[] arr = hdUrl.getHdurl().split("/");
        String nameFile = arr[arr.length - 1];
        System.out.println("Имя файла " + nameFile);

        try {
            URL url = new URL(hdUrl.getHdurl());
            InputStream inputStream = url.openStream();
            OutputStream outputStream = new FileOutputStream(nameFile);
            byte[] buffer = new byte[2048];
            int length;

            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.close();
        httpClient.close();
    }
}
