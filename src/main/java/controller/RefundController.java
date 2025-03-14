package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.PaymentDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@WebServlet("/controller/refund")
public class RefundController extends HttpServlet {
    private static final String API_KEY = "2001388363125560"; // 포트원 REST API 키
    private static final String API_SECRET = "BneSqyR9Z5pDhGXCwEq0KHn0T3BG7nFr6tDFI2UppwXsgP3Nq0lJI5ZvAnMd8BJ7u8NFt9Ct2HtZVUee"; // 포트원 REST API Secret
    private static final Gson gson = new Gson(); // Gson 객체를 클래스 레벨에서 생성

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 요청 본문 읽기
        BufferedReader reader = request.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        
        // JSON 파싱
        JsonObject refundRequest = gson.fromJson(sb.toString(), JsonObject.class);
        
        try {
            // 액세스 토큰 발급 받기
            String token = getAccessToken();
            
            // 환불 요청
            boolean refundResult = processRefund(token, refundRequest);
            
            if (refundResult) {
                // 환불 성공 시 데이터베이스 업데이트
                updateRefundStatus(refundRequest.get("merchant_uid").getAsString());
                
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"success\": true}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\": false, \"message\": \"환불 처리 실패\"}");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }

    private String getAccessToken() throws IOException {
        HttpURLConnection conn = getUrlConnection();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            
            JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);
            return jsonResponse.getAsJsonObject("response").get("access_token").getAsString();
        }
    }

    private static HttpURLConnection getUrlConnection() throws IOException {
        URL url = new URL("https://api.iamport.kr/users/getToken");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("imp_key", API_KEY);
        requestBody.addProperty("imp_secret", API_SECRET);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = requestBody.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        return conn;
    }

    private boolean processRefund(String token, JsonObject refundRequest) throws IOException {
        URL url = new URL("https://api.iamport.kr/payments/cancel");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", token);
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = refundRequest.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();

        // 응답 읽기
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            // 응답 파싱
            JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);

            // 응답 코드가 0이면 성공
            return jsonResponse.get("code").getAsInt() == 0;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void updateRefundStatus(String impId) {
        PaymentDao paymentDAO = new PaymentDao();
        boolean updated = paymentDAO.updatePaymentStatusToRefunded(impId);
        if (!updated) {
            throw new RuntimeException("환불 상태 업데이트 실패");
        }
    }
} 