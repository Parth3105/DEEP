package in.ac.daiict.deep.constant.status;

public enum AllocationStatusEnum {
    PENDING, ALLOCATED;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
