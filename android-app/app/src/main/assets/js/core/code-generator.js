/*
=========================================================
SELF PAYROLL
Universal Smart Code Generator
6-10 Character Business Codes
=========================================================
*/

(function () {

  const PREFIX = {
    employee: "EMP",
    attendance: "ATT",
    payroll: "PAY",
    salary: "SLP",
    leave: "LEV",
    advance: "ADV",
    loan: "LON",
    bonus: "BON",
    deduction: "DED",
    expense: "EXP",
    asset: "AST",
    invoice: "INV",
    department: "DEP",
    designation: "DSG",
    location: "LOC",
    branch: "BRN",
    holiday: "HOL",
    shift: "SHF"
  };

  function normalizeType(type) {
    return String(type || "GEN")
      .trim()
      .toLowerCase();
  }

  function randomDigits(length) {
    let value = "";

    if (
      window.crypto &&
      typeof window.crypto.getRandomValues === "function"
    ) {
      const values = new Uint32Array(length);
      window.crypto.getRandomValues(values);

      for (let i = 0; i < length; i++) {
        value += String(values[i] % 10);
      }

      return value;
    }

    while (value.length < length) {
      value += Math.floor(Math.random() * 10);
    }

    return value;
  }

  function valid(code) {
    return (
      typeof code === "string" &&
      code.length >= 6 &&
      code.length <= 10 &&
      /^[A-Z0-9]+$/.test(code)
    );
  }

  function isReserved(code) {
    return localStorage.getItem(
      "SELF_CODE_" + code
    ) === "1";
  }

  function reserve(code) {
    localStorage.setItem(
      "SELF_CODE_" + code,
      "1"
    );
  }

  window.CodeGenerator = {

    generate(type, length = 8) {

      const key = normalizeType(type);

      const prefix =
        (PREFIX[key] || "GEN")
          .replace(/[^A-Z0-9]/g, "")
          .substring(0, 3);

      let total = Number(length);

      if (!Number.isFinite(total)) {
        total = 8;
      }

      total = Math.max(
        6,
        Math.min(10, Math.floor(total))
      );

      const digitCount =
        Math.max(1, total - prefix.length);

      let code = "";
      let attempts = 0;

      do {
        code =
          prefix +
          randomDigits(digitCount);

        attempts++;

        if (attempts > 1000) {
          throw new Error(
            "Unable to generate unique code."
          );
        }

      } while (
        !valid(code) ||
        isReserved(code)
      );

      reserve(code);

      return code;
    },

    isValid(code) {
      return valid(
        String(code || "")
          .trim()
          .toUpperCase()
      );
    },

    reserve(code) {

      code = String(code || "")
        .trim()
        .toUpperCase();

      if (!valid(code)) {
        return false;
      }

      reserve(code);
      return true;
    }

  };

})();
