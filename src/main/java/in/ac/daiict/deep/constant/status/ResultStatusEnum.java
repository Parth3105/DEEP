package in.ac.daiict.deep.constant.status;

public enum ResultStatusEnum {
    PENDING, DECLARED;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}