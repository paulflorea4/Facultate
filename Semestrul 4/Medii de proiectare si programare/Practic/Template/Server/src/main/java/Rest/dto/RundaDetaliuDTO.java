package Rest.dto;

import java.io.Serializable;

public class RundaDetaliuDTO implements Serializable {
    private String cuvantTrimis;
    private String cuvantDeGhicit;
    private int punctaj;

    public RundaDetaliuDTO(String cuvantTrimis,String cuvantDeGhicit ,int punctaj) {
        this.cuvantTrimis = cuvantTrimis;
        this.cuvantDeGhicit = cuvantDeGhicit;
        this.punctaj = punctaj;
    }

    public String getCuvantTrimis() { return cuvantTrimis; }
    public void setCuvantTrimis(String raspunsuri) { this.cuvantTrimis = raspunsuri; }

    public int getPunctaj() { return punctaj; }
    public void setPunctaj(int punctaj) { this.punctaj = punctaj; }

    public String getCuvantDeGhicit() {
        return cuvantDeGhicit;
    }

    public void setCuvantDeGhicit(String cuvantDeGhicit) {
        this.cuvantDeGhicit = cuvantDeGhicit;
    }
}
