package at.fhtw.service.trading;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import static org.junit.jupiter.api.Assertions.*;

class TradingControllerTest {
    @Test
    void testCreateTradingDeal() throws IOException {
        URL url = new URL("http://localhost:10001/tradings");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Authorization", "Basic Hans-mtcgToken");
        urlConnection.setDoOutput(true);
        OutputStream outputStream = urlConnection.getOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.write("{\"Id\": \"6cd85277-4590-49d4-b0cf-ba0a921faad0\", \"CardToTrade\": \"b2237eca-0271-43bd-87f6-b22f70d42ca4\", \"Type\": \"monster\", \"MinimumDamage\": 15}");
        printWriter.close();

        int responseCode = urlConnection.getResponseCode();

        assertEquals(HttpURLConnection.HTTP_OK, responseCode);
    }

    @Test
    void testCreateTradingDealWithoutAuth() throws IOException {
        URL url = new URL("http://localhost:10001/tradings");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setDoOutput(true);
        OutputStream outputStream = urlConnection.getOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.write("{\"Id\": \"6cd85277-4590-49d4-b0cf-ba0a921faad0\", \"CardToTrade\": \"e85e3976-7c86-4d06-9a80-641c2019a79f\", \"Type\": \"monster\", \"MinimumDamage\": 15}");
        printWriter.close();

        int responseCode = urlConnection.getResponseCode();

        assertEquals(HttpURLConnection.HTTP_UNAUTHORIZED, responseCode);
    }

    @Test
    void testCreateTradingDealWithLockedCard() throws IOException {
        URL url = new URL("http://localhost:10001/tradings");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Authorization", "Basic kienboec-mtcgToken");
        urlConnection.setDoOutput(true);
        OutputStream outputStream = urlConnection.getOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.write("{\"Id\": \"6cd85277-4590-49d4-b0cf-ba0a921faad0\", \"CardToTrade\": \"e85e3976-7c86-4d06-9a80-641c2019a79f\", \"Type\": \"monster\", \"MinimumDamage\": 15}");
        printWriter.close();

        int responseCode = urlConnection.getResponseCode();

        assertEquals(HttpURLConnection.HTTP_FORBIDDEN, responseCode);
    }

    @Test
    void testCreateTradingDealWithoutOwningCard() throws IOException {
        URL url = new URL("http://localhost:10001/tradings");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Authorization", "Basic kienboec-mtcgToken");
        urlConnection.setDoOutput(true);
        OutputStream outputStream = urlConnection.getOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.write("{\"Id\": \"6cd85277-4590-49d4-b0cf-ba0a921faad0\", \"CardToTrade\": \"b2237eca-0271-43bd-87f6-b22f70d42ca4\", \"Type\": \"monster\", \"MinimumDamage\": 15}");
        printWriter.close();

        int responseCode = urlConnection.getResponseCode();

        assertEquals(HttpURLConnection.HTTP_FORBIDDEN, responseCode);
    }

    @Test
    void testCreateTradingDealDuplicateID() throws IOException {
        URL url = new URL("http://localhost:10001/tradings");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Authorization", "Basic kienboec-mtcgToken");
        urlConnection.setDoOutput(true);
        OutputStream outputStream = urlConnection.getOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.write("{\"Id\": \"6cd85277-4590-49d4-b0cf-ba0a921faad0\", \"CardToTrade\": \"b2237eca-0271-43bd-87f6-b22f70d42ca4\", \"Type\": \"monster\", \"MinimumDamage\": 15}");
        printWriter.close();

        int responseCode = urlConnection.getResponseCode();

        assertEquals(HttpURLConnection.HTTP_FORBIDDEN, responseCode);
    }

    @Test
    void testDeleteTradingDeal() throws IOException {
        URL url = new URL("http://localhost:10001/tradings/6cd85277-4590-49d4-b0cf-ba0a921faad0");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("DELETE");
        urlConnection.setRequestProperty("Authorization", "Basic Hans-mtcgToken");

        int responseCode = urlConnection.getResponseCode();

        assertEquals(HttpURLConnection.HTTP_OK, responseCode);
    }

    @Test
    void testDeleteTradingDealWrongUser() throws IOException {
        URL url = new URL("http://localhost:10001/tradings/6cd85277-4590-49d4-b0cf-ba0a921faad0");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("DELETE");
        urlConnection.setRequestProperty("Authorization", "Basic Martin-mtcgToken");

        int responseCode = urlConnection.getResponseCode();

        assertEquals(HttpURLConnection.HTTP_FORBIDDEN, responseCode);
    }

    @Test
    void testDeleteTradingDealWrongID() throws IOException {
        URL url = new URL("http://localhost:10001/tradings/6cd85277-4590-49d4-b0cf-0a921faad0");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("DELETE");
        urlConnection.setRequestProperty("Authorization", "Basic Hans-mtcgToken");

        int responseCode = urlConnection.getResponseCode();

        assertEquals(HttpURLConnection.HTTP_NOT_FOUND, responseCode);
    }

    @Test
    void testRetrieveDeals() throws IOException {
        URL url = new URL("http://localhost:10001/tradings");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Authorization", "Basic Hans-mtcgToken");

        int responseCode = urlConnection.getResponseCode();
        if(responseCode == HttpURLConnection.HTTP_OK){
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null) {
                System.out.println(inputLine + "\n");
            }
        }else {
            Assertions.assertFalse(true);
        }
    }

    @Test
    void testRetrieveDealsEmpty() throws IOException {
        URL url = new URL("http://localhost:10001/tradings");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Authorization", "Basic Hans-mtcgToken");

        int responseCode = urlConnection.getResponseCode();
        assertEquals(HttpURLConnection.HTTP_NO_CONTENT, responseCode);
    }

    @Test
    void testExecuteDeal() throws IOException {
        URL url = new URL("http://localhost:10001/tradings/6cd85277-4590-49d4-b0cf-ba0a921faad0");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Authorization", "Basic kienboec-mtcgToken");
        urlConnection.setDoOutput(true);
        OutputStream outputStream = urlConnection.getOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.write("dfdd758f-649c-40f9-ba3a-8657f4b3439f");
        printWriter.close();

        int responseCode = urlConnection.getResponseCode();
        assertEquals(HttpURLConnection.HTTP_OK, responseCode);
    }

    @Test
    void testExecuteDealWrongId() throws IOException {
        URL url = new URL("http://localhost:10001/tradings/6cd85277-4590-49d4-ba0a921faad0");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Authorization", "Basic kienboec-mtcgToken");
        urlConnection.setDoOutput(true);
        OutputStream outputStream = urlConnection.getOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.write("dfdd758f-649c-40f9-ba3a-8657f4b3439f");
        printWriter.close();

        int responseCode = urlConnection.getResponseCode();
        assertEquals(HttpURLConnection.HTTP_NOT_FOUND, responseCode);
    }

    @Test
    void testExecuteDealLockedCard() throws IOException {
        URL url = new URL("http://localhost:10001/tradings/6cd85277-4590-49d4-b0cf-ba0a921faad0");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Authorization", "Basic kienboec-mtcgToken");
        urlConnection.setDoOutput(true);
        OutputStream outputStream = urlConnection.getOutputStream();
        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.write("1cb6ab86-bdb2-47e5-b6e4-68c5ab389334");
        printWriter.close();

        int responseCode = urlConnection.getResponseCode();
        assertEquals(HttpURLConnection.HTTP_FORBIDDEN, responseCode);
    }
}