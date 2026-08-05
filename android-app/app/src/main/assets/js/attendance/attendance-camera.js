/*
=========================================================
SELF PAYROLL v2.0
Camera Module
=========================================================
*/

window.AttendanceCamera = {

  async capture(inputElementId = "selfieInput") {

    return new Promise((resolve, reject) => {

      const input = document.getElementById(inputElementId);

      if (!input) {
        reject("Camera input not found.");
        return;
      }

      input.onchange = () => {

        const file = input.files && input.files[0];

        if (!file) {
          reject("No image selected.");
          return;
        }

        resolve({
          file,
          name: file.name,
          size: file.size,
          type: file.type
        });

      };

      input.click();

    });

  }

};
