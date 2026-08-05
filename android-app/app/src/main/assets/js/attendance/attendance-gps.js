/*
=========================================================
SELF PAYROLL v2.0
GPS Module
=========================================================
*/

window.AttendanceGPS = {

  getLocation() {

    return new Promise((resolve, reject) => {

      if (!navigator.geolocation) {
        reject("Geolocation not supported.");
        return;
      }

      navigator.geolocation.getCurrentPosition(

        position => {

          resolve({
            latitude: position.coords.latitude,
            longitude: position.coords.longitude,
            accuracy: position.coords.accuracy
          });

        },

        error => {
          reject(error.message);
        },

        {
          enableHighAccuracy: true,
          timeout: 15000,
          maximumAge: 0
        }

      );

    });

  }

};
