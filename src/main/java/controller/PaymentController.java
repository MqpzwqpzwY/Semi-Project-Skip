package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.PaymentDao;
import dao.ReservationDAO;
import dto.PaymentDTO;
import dto.RentReservationItemDTO;
import dto.RentalReservationDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;
import service.SqlSessionFactoryService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@WebServlet("/controller/payment/*")
public class PaymentController extends HttpServlet {
    private PaymentDao paymentDao;
    private ReservationDAO reservationDAO;
    private final Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        paymentDao = new PaymentDao();
        reservationDAO = new ReservationDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            // 결제 데이터 읽기
            JsonObject paymentData = readPaymentData(request);
            System.out.println("Payment Data: " + paymentData);
            
            // 중복 결제 체크
            String impUid = paymentData.get("imp_uid").getAsString();
            if (isDuplicatePayment(impUid)) {
                sendErrorResponse(response, "중복 결제입니다.");
                return;
            }

            // RentalReservation 생성 및 저장
            RentalReservationDTO rentalReservation = createRentalReservationDTO(paymentData);
            int rentReservId = reservationDAO.insertRentalReservation(rentalReservation);
            System.out.println("Rental Reservation ID: " + rentReservId);
            
            // RentReservationItem 생성 및 저장
            RentReservationItemDTO rentReservationItem = createRentReservationItemDTO(paymentData, rentReservId);
            reservationDAO.insertRentReservationItem(rentReservationItem);
            System.out.println(rentReservationItem);

            // Payment 생성 및 저장
            PaymentDTO payment = createPaymentDTO(paymentData, rentReservId);
            System.out.println("Payment before insert: " + payment);

            // 성공 응답 보내기
            JsonObject jsonResponse = new JsonObject();


            jsonResponse.addProperty("success", true);
            jsonResponse.addProperty("message", "결제 및 예약이 완료되었습니다.");
            jsonResponse.addProperty("payment_id", payment.getPayment_id());
            jsonResponse.addProperty("redirect_url", request.getContextPath() + "/success.jsp");

            response.getWriter().write(jsonResponse.toString());

        } catch (Exception e) {
            sendErrorResponse(response, "결제 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        System.out.println("JDJFJJIDJI");
        try {
                // payment_id 파라미터 가져오기
                String paymentIdStr = request.getParameter("payment_id");
                if (paymentIdStr == null || paymentIdStr.trim().isEmpty()) {
                    throw new IllegalArgumentException("payment_id is required");
                }
                System.out.println(paymentIdStr);
                int paymentId = Integer.parseInt(paymentIdStr);
                PaymentDTO payment = paymentDao.getPaymentById(paymentId);
            System.out.println("1111111112121====" + payment);
                if (payment != null) {
                    // PaymentDTO를 JSON으로 변환
                    JsonObject jsonResponse = new JsonObject();
                    jsonResponse.addProperty("success", true);
                    jsonResponse.addProperty("payment_id", payment.getPayment_id());
                    jsonResponse.addProperty("total_price", payment.getTotal_price());
                    jsonResponse.addProperty("payment_method", payment.getPayment_method());
                    jsonResponse.addProperty("create_at", payment.getCreate_at().getTime());

                    response.getWriter().write(jsonResponse.toString());
                    String responseString = jsonResponse.toString();
                    System.out.println("Response being sent: " + responseString);  // 응답 데이터 출력
                } else {
                    sendErrorResponse(response, "결제 정보를 찾을 수 없습니다.");
                }

        } catch (Exception e) {
            sendErrorResponse(response, "결제 정보 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    private JsonObject readPaymentData(HttpServletRequest request) throws IOException {
        BufferedReader reader = request.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return gson.fromJson(sb.toString(), JsonObject.class);
    }

    private boolean isDuplicatePayment(String impUid) {
        PaymentDTO existingPayment = paymentDao.getPaymentByImpUid(impUid);
        return existingPayment != null;
    }

    private RentalReservationDTO createRentalReservationDTO(JsonObject paymentData) {
        RentalReservationDTO dto = new RentalReservationDTO();
        try {
            JsonObject reservationDetail = paymentData.getAsJsonObject("reservationDetail");
            
            dto.setRentalshop_id(reservationDetail.get("rentalshop_id").getAsInt());
            dto.setUuid(paymentData.get("uuid").getAsInt());
            
            // timestamp를 Instant로 변환 후 LocalDateTime으로 변환
            long startMillis = reservationDetail.get("rental_start").getAsLong();
            long endMillis = reservationDetail.get("rental_end").getAsLong();
            
            LocalDateTime startDateTime = Instant.ofEpochMilli(startMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
            
            LocalDateTime endDateTime = Instant.ofEpochMilli(endMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
            
            dto.setRental_start(Timestamp.valueOf(startDateTime));
            dto.setRental_end(Timestamp.valueOf(endDateTime));
            
            dto.setTotal_price(paymentData.get("total_price").getAsDouble());
            dto.setStatus("예약완료");
            
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("렌탈 예약 정보 생성 중 오류: " + e.getMessage());
        }
    }

    private RentReservationItemDTO createRentReservationItemDTO(JsonObject paymentData, int rentReservId) {
        JsonObject detailData = paymentData.getAsJsonObject("reservationDetail");
        RentReservationItemDTO rentItem = new RentReservationItemDTO();
        
        try {
            rentItem.setRent_reserv_id(rentReservId);
            rentItem.setItem_id(detailData.get("item_id").getAsInt());
            rentItem.setQuantity(detailData.get("quantity").getAsInt());
            rentItem.setSubtotal_price(detailData.get("price").getAsInt());
            
            return rentItem;
        } catch (Exception e) {
            throw new RuntimeException("렌탈 예약 아이템 정보 생성 중 오류: " + e.getMessage());
        }
    }

    private PaymentDTO createPaymentDTO(JsonObject paymentData, int rentReservId) {
        PaymentDTO payment = new PaymentDTO();
        try {
            payment.setPayment_id(0); // auto-increment
            payment.setImp_uid(paymentData.get("imp_uid").getAsString());
            payment.setRent_reserv_id(rentReservId);
            payment.setCart_id(paymentData.get("item_id").getAsInt());
            payment.setTotal_price(paymentData.get("total_price").getAsDouble());
            payment.setPayment_method(paymentData.get("payment_method").getAsString());
            payment.setStatus("PAID");
            payment.setSale_id(paymentData.get("sale_id").getAsInt());
            payment.setUuid(paymentData.get("uuid").getAsInt());
            if (paymentData.has("card_name")) {
                payment.setCard_name(paymentData.get("card_name").getAsString());
            }
            if (paymentData.has("card_number")) {
                payment.setCard_num(paymentData.get("card_number").getAsString());
            }

            
            payment.setCreate_at(new Timestamp(System.currentTimeMillis()));
            int generatedPaymentId = paymentDao.insert(payment);  // PaymentDao의 insert 메서드가 생성된 ID를 반환하도록 수정
            payment.setPayment_id(generatedPaymentId);
            
            return payment;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("결제 정보 생성 중 오류: " + e.getMessage());
        }
    }


    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("success", false);
        jsonResponse.addProperty("message", message);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write(jsonResponse.toString());
    }
}

