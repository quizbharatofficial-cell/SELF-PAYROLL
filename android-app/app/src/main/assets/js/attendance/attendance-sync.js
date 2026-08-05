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
