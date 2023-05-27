package com.iot.model.constants;

public final class Responses {
    private Responses() {}


    public static final class Authorization {
        private Authorization() {}
        public static final String FORM_IS_NOT_FILLED_OR_HAS_INCORRECT_DATA = "Форма не заполнена\nили содержит некорректные данные";
        public static final String PASSWORD_FORMAT_IS_INCORRECT = "Некорректный формат пароля";
        public static final String EMAIL_FORMAT_IS_NOT_VALID = "Некорректный формат электронного адреса";
        public static final String VERIFICATION_CODE_IS_NOT_VALID = "Код подтверждения неверный";
        public static final String ERROR_VERIFICATION_CODE_LENGTH_OR_FORMAT_INCORRECT = "Код некорректной длины или имеет неправильный формат";
        public static final String EMAIL_MESSAGE_ALREADY_SENT = "Такое почтовое сообщение уже было отправлено";
        public static final String EMAIL_MESSAGE_FAILED = "Почтовое письмо не было отправлено";
        public static final String CONNECTION_TIME_WAS_EXPIRED = "Время подключения вышло";
        public static final String USER_ALREADY_EXISTS = "Пользователь c такими данными уже существует";
        public static final String CLIENT_IS_NOT_AUTHENTICATED = "Сначала пройдите верификацию по коду подтверждения";
        public static final String PASSWORD_IS_NOT_CORRECT = "Указан неверный пароль";
        public static final String NO_USER = "Пользователь не найден";
        public static final String ERROR_AUTHORIZED = "Указаны недействительные данные";
        public static final String VERIFICATION_CODE_WAS_SENT = "Код был отправлен";
        public static final String VERIFICATION_CODE_SENT = "Verify code was sent";
        public static final String VERIFICATION_CODE_IS_RIGHT = "Код подтверждения верен";
        public static final String RESET_CODE_WAS_SENT = "Код сброса пароля был отправлен";
        public static final String RESET_CODE_IS_RIGHT = "Код сброса пароля верен";
        public static final String DATA_CHANGED = "Данные были успешно изменены";
    }

    public static final class Socket {
        public static final String UUID_FORMAT_IS_NOT_CORRECT = "Формат введённого айди неправильный";
        public static final String BOARD_WAS_NOT_FOUND = "Устройство не было найдено";
        public static final String BOARD_WAS_FOUND_RU = "Устройство было найдено";
        public static final String CLIENT_SEARCHING = "Поиск клиента...";

        public static final String BOARD_WAS_FOUND = "Board was found";
        public static final String CLIENTS_WAS_NOT_CONNECTED = "Clients was not connected";
        public static final String BOARD_ID_SUCCESSFULLY_RECEIVED = "Board id successfully received";
        public static final String SENDING_BOARD_ID_ACCEPTED = "Sending board id accepted";
    }

    public static final class Service {
        private Service() {}

        public static final String DEVICE_STATE_HAS_BEEN_UPDATED = "Device state has been updated";
        public static final String DEVICE_LISTENING_STATE_WAS_RESET = "Device listening state was reset";
        public static final String USER_OR_DEVICE_WAS_NOT_FOUND = "Пользователь или устройство не было найдено\n" +
                "Обратитесь в техническую поддержку";
        public static final String SENSOR_WAS_NOT_FOUND = "Датчик не был найден";
        public static final String DEVICE_STATE_WAS_NOT_UPDATED = "Состояние устройства не было обновлено";
        public static final String DEVICE_IS_NOT_LISTENING = "Устройство не присоединено к серверу";
        public static final String ACCESS_TOKEN_WAS_UPDATED = "Access token was updated";
        public static final String YOU_ARE_NOT_LOGIN_IN = "Вы не вошли в аккаунт";
        public static final String SETTINGS_WAS_RESET = "Настройки были сброшены";
        public static final String EXIT_SUGGESTION = "Вы хотите выйти?";
        public static final String AT_LEAST_ONE_STATE_MUST_BE_USING = "Вам необходимо выбрать хотя бы одно состояние перед подтверждением";
        public static final String SENSOR_STATE_MUST_BE_ONLY_NUMERIC = "Состояние датчика может быть только числовым";
    }
    public static final class PersonalData {
        private PersonalData() {}

        public static final String USER_WAS_NOT_FOUND = "Пользователь не был найден";
        public static final String PASSWORD_IS_NOT_EQUALS_TO_EACH_OTHER = "Пароли не равны друг другу";
        public static final String DATA_WAS_SUCCESSFULLY_CHANGED = "Data was successfully changed";
    }
}
