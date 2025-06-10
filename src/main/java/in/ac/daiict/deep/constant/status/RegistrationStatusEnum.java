package in.ac.daiict.deep.constant.status;

public enum RegistrationStatusEnum {
    OPEN, CLOSE, NEVER;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
