package in.ac.daiict.deep.constant.response;

public class ResponseMessage {
    public static int UPLOAD_COUNT = 0;

    public static final String USER_NOT_FOUND = "User Not Found!";
    public static final String NOT_REGISTERED = "Please Enroll!";
    public static final String UPLOAD_OFFERS = "Warning: Course Data has been updated. Please re-upload Course Offering file to avoid data loss.";
    public static final String NO_FILES_UPLOADED = "Warning: No files were uploaded. Please make sure to select and upload files before submitting.";
    public static final String RESULTS_NOT_FOUND = "No results found!";
    public static final String STUDENT_DATA_NOT_FOUND="Student data is not available for this semester.";
    public static final String COURSE_DATA_NOT_FOUND="Course data is not available.";
    public static final String COURSE_OFFERS_NOT_FOUND="Course offerings is not available";
    public static final String RUN_ALLOCATION_DEFAULT_STATUS="Yet to run";
    public static final String RUN_ALLOCATION_SUCCESS_STATUS ="Success";

    public static String getUploadSuccessMessage() {
        return "You're all set! " + UPLOAD_COUNT + " file(s) have been successfully uploaded and saved.";
    }
}