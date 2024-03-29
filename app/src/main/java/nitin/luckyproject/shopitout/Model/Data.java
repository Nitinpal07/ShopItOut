package nitin.luckyproject.shopitout.Model;

public class Data {
    private String type;
    private String amount;
    private String note;
    private String date;
    private String id;

    public Data(){

    }
    public Data(String type, String amount, String note, String date, String id) {
        this.type = type;
        this.amount = amount;
        this.note = note;
        this.date = date;
        this.id = id;
    }

    public  String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public  String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public  String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public  String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
