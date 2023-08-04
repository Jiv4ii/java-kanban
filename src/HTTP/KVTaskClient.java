package HTTP;


import entities.CustomException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;


public class KVTaskClient {
    private String path;
    public String token;
   private final Logger logger = Logger.getLogger(KVTaskClient.class.getName());

    private final HttpClient client;
    public KVTaskClient(String path) throws CustomException {



        /*Честно говоря я не разобрался с выбросом исключений, и сделал чудовищ расположенных ниже*/



        this.path = path;
        client = HttpClient.newHttpClient();
        URI url = URI.create(path + "/register");
        System.out.println(url);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {

            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200){
                logger.log(Level.SEVERE, "Недопустимый код состояния ответа");
                throw new CustomException("Недопустимый код состояния ответа");
            }
            token = response.body();
        }  catch ( InterruptedException | IOException e){
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new CustomException("Проблемы с потоками (-_-)");
        }

    }

    public void put(String key,String json) throws CustomException{
        try {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(path + "/save/" + key + "?API_TOKEN=" + token);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();


            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch ( InterruptedException | IOException e){
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new CustomException("Проблемы с потоками (-_-)");
        }

    }

    public String load(String key) throws CustomException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(path + "/load/" + key + "?API_TOKEN=" + token);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200){
                logger.log(Level.SEVERE, "Недопустимый код состояния ответа");
                throw new CustomException("Недопустимый код состояния ответа");
            }
            return response.body();
        } catch ( InterruptedException | IOException e){
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new CustomException("Проблемы с потоками (-_-)");
        }

    }
}
