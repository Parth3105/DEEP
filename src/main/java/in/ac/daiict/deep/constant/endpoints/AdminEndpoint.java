package in.ac.daiict.deep.constant.endpoints;

public class AdminEndpoint {
    public static final String DASHBOARD="/admin-dashboard";
    public static final String CREATE_ALLOCATION_INSTANCE="create-instance";
    public static final String UPDATE_INSTANCE="/update-instance";
    public static final String SUBMIT_DATA="/submit-data";
    public static final String RUN_ALLOCATION="/run-allocation/";
    public static final String EXECUTE_ALLOCATION="/execute-allocation/{semester}";
    public static final String DOWNLOAD_REPORTS="/download-reports";
    public static final String DOWNLOAD_REPORT_SUBMIT ="/download-reports/{semester}/{name}";
    public static final String DOWNLOAD_UPLOADED_REPORT_SUBMIT="/download-reports/{name}";
    public static final String STUDENT_PREFERENCE="/student-preferences";
    public static final String STUDENT_PREFERENCE_FILTER="/student-preferences/{sid}";
    public static final String ALLOCATION_RESULTS="/allocation-results";
    public static final String ALLOCATION_RESULTS_FILTER="/allocation-results/{sid}";

}
