package client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private String API_TOKEN;
    private final String url;
    private final HttpClient client;

    public KVTaskClient(String url){
        this.url = url;
        this.client = HttpClient.newHttpClient();
        try{
            URI uri = URI.create(url + "/register");
            HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
            String response = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
            this.API_TOKEN = response;
        }
        catch(IOException | InterruptedException e){
            System.out.println("error while starting server possibly wrong url");
        }
    }
    public void put(String key,String json) throws IOException, InterruptedException {
        URI uri = URI.create(url + "/save/" +key+ "?API_TOKEN=" + API_TOKEN);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }
    public String get(String key) throws IOException, InterruptedException {
        URI uri = URI.create(url + "/load/" +key+ "?API_TOKEN=" + API_TOKEN);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }



 public void printAPITOCKEN(){
      System.out.println(API_TOKEN);
   }

    public static void main(String[] args) throws IOException, InterruptedException {
        KVTaskClient client = new KVTaskClient("http://localhost:8078");
        client.printAPITOCKEN();
        client.put("test","test");
        if(client.get("t").isEmpty()){
            System.out.println(1);
        }
        System.out.println(client.get("t"));
    }
}
