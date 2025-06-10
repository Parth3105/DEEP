package in.ac.daiict.deep.constant.status;

public enum UpdateInstanceStatusEnum {
    OPEN, NEVER;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}