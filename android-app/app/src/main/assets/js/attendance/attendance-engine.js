/*
=========================================================
SELF PAYROLL v2.0
Attendance Engine
=========================================================
*/

window.AttendanceEngine = {

  validate(context = {}) {

    const rules = AttendanceRules.load();

    if (typeof Android !== "undefined") {

        if (rules.gpsRequired) {
            if (!Android.hasLocationPermission()) {
                Android.requestLocationPermission();
                return {
                    ok:false,
                    message:"Location permission required."
                };
            }
        }

        if (rules.selfieRequired) {
            if (!Android.hasCameraPermission()) {
                Android.requestCameraPermission();
                return {
                    ok:false,
                    message:"Camera permission required."
                };
            }
        }

        if (rules.qrRequired) {
            if (!Android.isQrScannerAvailable()) {
                return {
                    ok:false,
                    message:"QR Scanner unavailable."
                };
            }
        }

    }


    if (rules.gpsRequired && !context.gps) {
      return {
        ok: false,
        message: "GPS verification required."
      };
    }

    if (rules.selfieRequired && !context.selfie) {
      return {
        ok: false,
        message: "Selfie verification required."
      };
    }

    if (rules.qrRequired && !context.qr) {
      return {
        ok: false,
        message: "QR verification required."
      };
    }

    return {
      ok: true,
      message: "Attendance validation passed."
    };
  }

};
