package com.iot.model.constants;

public final class Responses {
    private Responses() {}


    public static final class Authorization {
        private Authorization() {}
        public static final String FORM_IS_NOT_FILLED_OR_HAS_INCORRECT_DATA = "Форма не заполнена\nили содержит некорректные данные";
        public static final String PASSWORD_FORMAT_IS_INCORRECT = "Некорректный формат пароля";
        public static final String EMAIL_FORMAT_IS_NOT_VALID = "Некорректный формат электронного адреса";
        public static final String NOT_AUTHORIZED = "Для управленияя вы должны быть авторизованы";
        public static final String VERIFICATION_CODE_IS_NOT_VALID = "Код подтверждения неверный";
        public static final String ERROR_VERIFICATION_CODE_LENGTH_OR_FORMAT_INCORRECT = "Код некорректной длины или имеет неправильный формат";
        public static final String EMAIL_MESSAGE_ALREADY_SENT = "Такое почтовое сообщение уже было отправлено";
        public static final String EMAIL_MESSAGE_FAILED = "Почтовое письмо не было отправлено";
        public static final String CONNECTION_TIME_WAS_EXPIRED = "Время подключения вышло";
        public static final String USER_ALREADY_EXISTS = "Пользователь c такими данными уже существует";
        public static final String CLIENT_IS_NOT_AUTHENTICATED = "Сначала пройдите верификацию по коду подтверждения";
        public static final String PASSWORD_IS_NOT_CORRECT = "Указан неверный пароль";
        public static final String TOKEN_IS_ENDED = "Время жизни токена было истрачено или его структура некорректна";
        public static final String NO_USER_ID = "В Payload JWT отсутствует айди пользователя";
        public static final String NO_USER = "Пользователь не найден";
        public static final String WRONG_DATA_USER = "Неверные данные пользователя";
        public static final String ERROR_AUTHORIZED = "Ошибка авторизации";
        public static final String VERIFICATION_CODE_WAS_SENT = "Код был отправлен";
        public static final String VERIFICATION_CODE_SENT = "Verify code was sent";
        public static final String UNKNOWN_ERROR = "Неизвестная ошибка, обратитесь в службу поддержки";
        public static final String VERIFICATION_CODE_IS_RIGHT = "Код подтверждения верен";
        public static final String SUCCESSFULLY_REGISTRATION = "Регистрация успешно пройдена";
        public static final String PASSWORD_RESET = "Пароль успешно сброшен";
        public static final String AUTHORIZATION_COMPLETE = "Авторизация успешно пройдена";
        public static final String ACCOUNT_CONFIRMED = "Учетная запись подтверждена";
        public static final String RESET_CODE_WAS_SENT = "Код сброса пароля был отправлен";
        public static final String RESET_CODE_IS_RIGHT = "Код сброса пароля верен";
        public static final String DATA_CHANGED = "Данные были успешно изменены";
    }

    public static final class Socket {
        public static final String UUID_FORMAT_IS_NOT_CORRECT = "Формат введённого айди неправильный";
        public static final String UNKNOWN_SOCKET_EXCEPTION = "Неизвестная ошибка при подключении";
        public static final String BOARD_WAS_NOT_FOUND = "Устройство не было найдено";
        public static final String BOARD_WAS_FOUND_RU = "Устройство было найдено";
        public static final String CLIENT_SEARCHING = "Поиск клиента...";

        public static final String BOARD_WAS_FOUND ="{\"message\":\"Board was found\"}";
        public static final String CLIENTS_WAS_NOT_CONNECTED = "Clients was not connected";
        public static final String BOARD_ID_SUCCESSFULLY_RECEIVED = "Board id successfully received";
        public static final String SENDING_BOARD_ID_ACCEPTED = "Sending board id accepted";
    }

    public static final class Service {
        private Service() {}

        public static final String DEVICE_STATE_HAS_BEEN_UPDATED = "Device state has been updated";
        public static final String DEVICE_LISTENING_STATE_WAS_RESET = "Device listening state was reset";
        public static final String DEVICE_WAS_NOT_FOUND = "Устройство не было обнаружено";
        public static final String USER_OR_DEVICE_WAS_NOT_FOUND = "Пользователь или устройство не было найдено";
        public static final String DEVICE_STATE_WAS_NOT_UPDATED = "Состояние устройства не было обновлено";
        public static final String DEVICE_IS_NOT_LISTENING = "Устройство не прослушивается сокетом в текущий момент";
        public static final String STATE_OF_SENSOR_IS_NOT_VALID = "Переданное состояние устройства для изменения не соответствует стандартам";
        public static final String TOKEN_IS_NOT_VALID = "Время жизни токена было истрачено или его структура некорректна";
        public static final String TOKEN_PAYLOAD_IS_INCORRECT = "В Payload JWT отсутствует айди пользователя";
    }
}
