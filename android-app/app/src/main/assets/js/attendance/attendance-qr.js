/*
=========================================================
SELF PAYROLL v2.0
QR Module
=========================================================
*/

window.AttendanceQR = {

  async verify() {

    if (
      typeof Android !== "undefined" &&
      typeof Android.scanQRCode === "function"
    ) {
      return Android.scanQRCode();
    }

    return Promise.reject(
      "QR scanner not available."
    );
  }

};
