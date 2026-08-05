/*
=========================================================
SELF PAYROLL v2.0
Attendance Rule Engine
=========================================================
*/

const DEFAULT_ATTENDANCE_RULES = {

  manualPunch: true,
  widgetPunch: true,
  mobilePunch: true,
  webPunch: true,

  gpsRequired: false,
  selfieRequired: false,
  qrRequired: false,
  geofenceRequired: false,

  offlineAllowed: true,
  autoSync: true,

  lateRule: true,
  earlyOutRule: true,
  halfDayRule: true,

  otMode: "manual",
  shiftMode: "single",
  nightShift: true,

  breakMode: "manual",

  graceMinutes: 10,

  maxGpsDistance: 100,

  requireApprovalForOT: false
};

window.AttendanceRules = {

  load() {
    try {
      return JSON.parse(
        localStorage.getItem("attendanceRules")
      ) || { ...DEFAULT_ATTENDANCE_RULES };
    } catch {
      return { ...DEFAULT_ATTENDANCE_RULES };
    }
  },

  save(rules) {
    localStorage.setItem(
      "attendanceRules",
      JSON.stringify(rules)
    );
  },

  reset() {
    this.save({ ...DEFAULT_ATTENDANCE_RULES });
  }

};
