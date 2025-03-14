package dto;

import java.sql.Date;

public class SaleDTO {
    private int uuid;
    private double sale_percent;
    private double sale_amount;
    private Date created_at;
    public SaleDTO() {
    }
    public SaleDTO(int uuid, double sale_percent, double sale_amount, Date created_at) {
        this.uuid = uuid;
        this.sale_percent = sale_percent;
        this.sale_amount = sale_amount;
        this.created_at = created_at;

    }

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    public double getSale_percent() {
        return sale_percent;
    }

    public void setSale_percent(double sale_percent) {
        this.sale_percent = sale_percent;
    }

    public double getSale_amount() {
        return sale_amount;
    }

    public void setSale_amount(double sale_amount) {
        this.sale_amount = sale_amount;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}
