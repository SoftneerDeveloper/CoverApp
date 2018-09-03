package ke.co.coverapp.coverapp.utility;

import java.util.ArrayList;

/**
 * Created by intrepid on 12/15/17.
 */

interface PermissionResultCallback {
    void PermissionGranted(int request_code);
    void PartialPermissionGranted(int request_code, ArrayList granted_permissions);
    void PermissionDenied(int request_code);
    void NeverAskAgain(int request_code);
}
