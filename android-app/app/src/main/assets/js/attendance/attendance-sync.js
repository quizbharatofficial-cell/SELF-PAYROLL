/*
=========================================================
SELF PAYROLL v2.0
Offline Sync Module
=========================================================
*/

window.AttendanceSync = {

  QUEUE_KEY: "attendanceSyncQueue",

  getQueue() {
    try {
      return JSON.parse(
        localStorage.getItem(this.QUEUE_KEY)
      ) || [];
    } catch {
      return [];
    }
  },

  add(record) {
    const queue = this.getQueue();
    queue.push(record);

    localStorage.setItem(
      this.QUEUE_KEY,
      JSON.stringify(queue)
    );
  },

  clear() {
    localStorage.removeItem(this.QUEUE_KEY);
  }

};


/* =========================================================
   Offline Attendance Queue
========================================================= */

window.AttendanceSync.queueAttendance = function(record){

    const queue = JSON.parse(
        localStorage.getItem("attendanceQueue") || "[]"
    );

    queue.push({
        ...record,
        queuedAt: new Date().toISOString()
    });

    localStorage.setItem(
        "attendanceQueue",
        JSON.stringify(queue)
    );

    return queue.length;
};

window.AttendanceSync.getQueue = function(){

    return JSON.parse(
        localStorage.getItem("attendanceQueue") || "[]"
    );

};

window.AttendanceSync.clearQueue = function(){

    localStorage.removeItem("attendanceQueue");

};


/* =========================================================
   Auto Sync
========================================================= */

window.AttendanceSync.startAutoSync = function(){

    async function sync(){

        if(!navigator.onLine){
            return;
        }

        const queue =
            window.AttendanceSync.getQueue();

        if(!queue.length){
            return;
        }

        console.log("Syncing", queue.length, "attendance record(s)...");

        // Sprint 2 placeholder:
        // Future server/API sync will be added here.

        window.AttendanceSync.clearQueue();
    }

    window.addEventListener("online", sync);

    setInterval(sync, 60000);

};

window.AttendanceSync.startAutoSync();
