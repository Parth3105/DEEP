package in.ac.daiict.deep.constant.endpoints;

public class AdminEndpoint {
    public static final String ADMIN_BASE="/admin";
    public static final String DASHBOARD=ADMIN_BASE+"/admin-dashboard";
    public static final String OPEN_REGISTRATION=ADMIN_BASE+"/open-registration";
    public static final String EXTEND_REGISTRATION_PERIOD=ADMIN_BASE+"/extend-period";
    public static final String CLOSE_REGISTRATION=ADMIN_BASE+"/close-registration";
    public static final String CREATE_ALLOCATION_INSTANCE=ADMIN_BASE+"/create-instance";
    public static final String UPDATE_INSTANCE=ADMIN_BASE+"/update-instance";
    public static final String UPLOAD_FILE=ADMIN_BASE+"/upload/{type}";
    public static final String SUBMIT_DATA=ADMIN_BASE+"/submit-data";
    public static final String RUN_ALLOCATION=ADMIN_BASE+"/run-allocation";
    public static final String EXECUTE_ALLOCATION=ADMIN_BASE+"/execute-allocation/{semester}";
    public static final String DOWNLOAD_REPORTS=ADMIN_BASE+"/download-reports";
    public static final String DOWNLOAD_REPORT_SUBMIT =ADMIN_BASE+"/download-reports/{semester}/{name}";
    public static final String DOWNLOAD_UPLOADED_REPORT_SUBMIT=ADMIN_BASE+"/download-reports/{name}";
    public static final String DOWNLOAD_STUDENT_PREFERENCES=ADMIN_BASE+"/student-preferences/download/{semester}";
    public static final String STUDENT_PREFERENCE=ADMIN_BASE+"/student-preferences";
    public static final String STUDENT_PREFERENCE_FILTER=ADMIN_BASE+"/student-preferences/{sid}";
    public static final String ALLOCATION_RESULTS=ADMIN_BASE+"/allocation-results";
    public static final String ALLOCATION_RESULTS_FILTER=ADMIN_BASE+"/allocation-results/{sid}";

}
