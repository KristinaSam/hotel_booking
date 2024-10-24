document.addEventListener('DOMContentLoaded', function () {
    const checkInDateInput = document.getElementById('checkInDate');
    const checkOutDateInput = document.getElementById('checkOutDate');
    const adultsCountInput = document.getElementById('adultsCount');
    const childrenCountInput = document.getElementById('childrenCount');
    const bookingForm = document.querySelector('form');

    // Устанавливаем текущее значение для checkInDate
    const today = new Date();
    const day = String(today.getDate()).padStart(2, '0');
    const month = String(today.getMonth() + 1).padStart(2, '0'); // Январь начинается с 0!
    const year = today.getFullYear();
    checkInDateInput.value = '';

    checkInDateInput.addEventListener('change', function () {
        // Устанавливаем checkOutDate минимальным значением в checkInDate
        const checkInDate = new Date(checkInDateInput.value);
        checkOutDateInput.min = checkInDate.toISOString().split('T')[0];
    });

    // Проверка формы перед отправкой
    bookingForm.addEventListener('submit', function (event) {
        let valid = true;

        if (!adultsCountInput.value) {
            valid = false;
            adultsCountInput.style.borderColor = 'red';
        } else {
            adultsCountInput.style.borderColor = '';
        }

        if (!checkInDateInput.value) {
            valid = false;
            checkInDateInput.style.borderColor = 'red';
        } else {
            checkInDateInput.style.borderColor = '';
        }

        if (!checkOutDateInput.value) {
            valid = false;
            checkOutDateInput.style.borderColor = 'red';
        } else {
            checkOutDateInput.style.borderColor = '';
        }

        if (!childrenCountInput.value) {
            valid = false;
            childrenCountInput.style.borderColor = 'red';
        } else {
            childrenCountInput.style.borderColor = '';
        }

        if (!valid) {
            event.preventDefault(); // Предотвращаем отправку формы
            alert('Пожалуйста, заполните все обязательные поля.');
        }
    });
});