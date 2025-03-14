package dto;

import java.sql.Date;

public class TransactionsDTO {
    private int uuid;
    private double commission_amount;
    private double company_amount;
    private Date create_at;

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    public double getCommission_amount() {
        return commission_amount;
    }

    public void setCommission_amount(int commission_amount) {
        this.commission_amount = commission_amount;
    }

    public double getCompany_amount() {
        return company_amount;
    }

    public void setCompany_amount(int company_amount) {
        this.company_amount = company_amount;
    }

    public Date getCreate_at() {
        return create_at;
    }

    public void setCreate_at(Date create_at) {
        this.create_at = create_at;
    }

    public TransactionsDTO(int uuid, double commission_amount, double company_amount, Date create_at) {
        this.uuid = uuid;
        this.commission_amount = commission_amount;
        this.company_amount = company_amount;
        this.create_at = create_at;
    }
}
