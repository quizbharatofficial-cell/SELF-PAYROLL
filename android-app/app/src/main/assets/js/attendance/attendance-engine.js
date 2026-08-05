/*
=========================================================
SELF PAYROLL v2.0
Attendance Engine
=========================================================
*/

window.AttendanceEngine = {

  validate(context = {}) {

    const rules = AttendanceRules.load();

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
