package ro.mpp2026;

public interface Identifiable<ID> {
    void setId(ID id);
    ID getId();
}
