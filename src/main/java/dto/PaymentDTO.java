package dto;

import com.google.gson.annotations.JsonAdapter;
import controller.PaymentController;
import service.TimestampAdapter;

import java.sql.Date;
import java.sql.Timestamp;

public class PaymentDTO {
    private int payment_id;
    private int UUID;
    private int rent_reserv_id;
    private int sale_id;
    private int cart_id;
    private double total_price;
    private String payment_method;
    private String status;
    private String imp_uid;
    private String card_num;
    private String card_name;
    @JsonAdapter(TimestampAdapter.class)
    private Timestamp create_at;

    public PaymentDTO() {
    }



    public PaymentDTO(int payment_id, int UUID, int rent_reserv_id, int sale_id, int cart_id, double total_price, String payment_method, String status, String imp_uid, String card_num, String card_name, Timestamp create_at) {
        this.payment_id = payment_id;
        this.UUID = UUID;
        this.rent_reserv_id = rent_reserv_id;
        this.sale_id = sale_id;
        this.cart_id = cart_id;
        this.total_price = total_price;
        this.payment_method = payment_method;
        this.status = status;
        this.imp_uid = imp_uid;
        this.card_num = card_num;
        this.card_name = card_name;
        this.create_at = create_at;
    }

    public int getPayment_id() {
        return payment_id;
    }

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public void setPayment_id(int payment_id) {
        this.payment_id = payment_id;
    }

    public int getUuid() {
        return UUID;
    }

    public void setUuid(int UUID) {
        this.UUID = UUID;
    }

    public int getSale_id() {
        return sale_id;
    }

    public void setSale_id(int sale_id) {
        this.sale_id = sale_id;
    }

    public int getRent_reserv_id() {
        return rent_reserv_id;
    }

    public void setRent_reserv_id(int rent_reserv_id) {
        this.rent_reserv_id = rent_reserv_id;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImp_uid() {
        return imp_uid;
    }

    public void setImp_uid(String imp_uid) {
        this.imp_uid = imp_uid;
    }

    public String getCard_num() {
        return card_num;
    }

    public void setCard_num(String card_num) {
        this.card_num = card_num;
    }

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

    public Timestamp getCreate_at() {
        return create_at;
    }


    public void setCreate_at(Timestamp create_at) {
        this.create_at = create_at;
    }

    @Override
    public String toString() {
        return "PaymentDTO{" +
                "payment_id=" + payment_id +
                ", UUID=" + UUID +
                ", rent_reserv_id=" + rent_reserv_id +
                ", sale_id=" + sale_id +
                ", cart_id=" + cart_id +
                ", total_price=" + total_price +
                ", payment_method='" + payment_method + '\'' +
                ", status='" + status + '\'' +
                ", imp_uid='" + imp_uid + '\'' +
                ", card_num='" + card_num + '\'' +
                ", card_name='" + card_name + '\'' +
                ", create_at=" + create_at +
                '}';
    }
}
