package org.example.ducksocialnetworkui.domain;

public abstract class Duck extends User{
    protected TipRata tip;
    protected double viteza;
    protected double rezistenta;
    protected long cardId;

    public Duck(Long id, String username, String email, String password, TipRata tip, double viteza, double rezistenta,Long cardId) {
        super(id, username, email, password);
        this.tip = tip;
        this.viteza = viteza;
        this.rezistenta = rezistenta;
        this.cardId = cardId;
    }

    public TipRata getTip() { return tip; }
    public double getViteza() { return viteza; }
    public double getRezistenta() { return rezistenta; }
    public long getCardId() { return cardId; }

    public void setTip(TipRata tip) { this.tip = tip; }
    public void setViteza(double viteza) { this.viteza = viteza; }
    public void setRezistenta(double rezistenta) { this.rezistenta = rezistenta; }
    public void setCard(long cardId) { this.cardId = cardId; }

    @Override
    public String toString() {
        return "Duck{" +
                "id=" + getId() +
                ", username='" + getUsername() + '\'' +
                ", tip=" + tip +
                ", viteza=" + viteza +
                ", rezistenta=" + rezistenta +
                ", card=" + (cardId != 0 ? cardId : " - ") +
                '}';
    }
}
