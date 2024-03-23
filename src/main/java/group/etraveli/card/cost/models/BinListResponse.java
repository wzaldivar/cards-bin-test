package group.etraveli.card.cost.models;

public class BinListResponse {
    private BinListCountry country;

  public   BinListResponse(){}

    public BinListResponse(BinListCountry country){
      this.country = country;
    }

    public BinListCountry getCountry(){return country;}

    public void setCountry(BinListCountry country) {
        this.country = country;
    }
}
