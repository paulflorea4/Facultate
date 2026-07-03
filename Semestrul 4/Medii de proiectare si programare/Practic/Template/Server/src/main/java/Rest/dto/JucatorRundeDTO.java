package Rest.dto;

import java.io.Serializable;
import java.util.List;

public class JucatorRundeDTO implements Serializable {
    private String aliasJucator;
    private List<RundaDetaliuDTO> detaliiRunde;

    public JucatorRundeDTO(String aliasJucator, List<RundaDetaliuDTO> detaliiRunde) {
        this.aliasJucator = aliasJucator;
        this.detaliiRunde = detaliiRunde;
    }

    public String getAliasJucator() { return aliasJucator; }
    public void setAliasJucator(String aliasJucator) { this.aliasJucator = aliasJucator; }

    public List<RundaDetaliuDTO> getDetaliiRunde() { return detaliiRunde; }
    public void setDetaliiRunde(List<RundaDetaliuDTO> detaliiRunde) { this.detaliiRunde = detaliiRunde; }
}
