package com.iot.model.consts;

public final class CommonErrors {
    private CommonErrors() {}
    /**
     * ConstAuthorizationErrors
     */
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

    }
    /**
     * ConstAuthorizationSuccessResponses
     */
    public static final class AuthorizationSuccessResponses {
        private AuthorizationSuccessResponses() {}
        public static final String VERIFICATION_CODE_WAS_SENT = "Код был отправлен";
        public static final String VERIFICATION_CODE_SENT = "Verify code was sent";
        public static final String UNKNOWN_ERROR = "Неизвестная ошибка, обратитесь в службу поддержки";
        public static final String VERIFICATION_CODE_IS_RIGHT = "Код подтверждения верен";
        public static final String SUCCESSFULLY_REGISTRATION = "Регистрация успешна проведена";
        public static final String PASSWORD_RESET = "Пароль успешно сброшен";
        public static final String AUTHORIZATION_COMPLETE = "Авторизация успешно пройдена";
        public static final String ACCOUNT_CONFIRMED = "Учетная запись подтверждена";
        public static final String RESET_CODE_WAS_SENT = "Код сброса пароля был отправлен";
        public static final String RESET_CODE_IS_RIGHT = "Код сброса пароля верен";
        public static final String DATA_CHANGED = "data was successfully changed";
        public static final String STATE_CHANGE = "device state has been updated";
        public static final String RESET_STATUS = "Device listening state was reset";
    }
    /**
     * ConstServiceErrors
     */
    public static final class Service {
        private Service() {}
        public static final String NO_DEVICE = "Устройство не было обнаружено";
        public static final String NO_USER_DEVICE = "Пользователь или устройство не было найдено";
        public static final String NO_STATE_DEVICE = "Состояние устройства не было обновлено";
        public static final String NO_LISTEN_SOCKET = "Устройство не прослушивается сокетом в текущий момент";
        public static final String NO_STANDART = "Переданное состояние устройства для изменения не соответствует стандартам";
        public static final String TOKEN_IS_ENDED = "Время жизни токена было истрачено или его структура некорректна";
        public static final String NO_USER_ID = "В Payload JWT отсутствует айди пользователя";
    }
        /**
         * ConstEndpoints
         */
        public static final class Endpoints {
            private Endpoints() {}
            public static final String SEND_CODE = "/code/send";
            public static final String CONFIRM_CODE = "/code/verify";
            public static final String AUTHORIZATION = "/account/login";
            public static final String REGISTRATION = "/account/register";
            public static final String RESET_PASSWORD = "/account/change/password";
            public static final String APP_CONNECTION = "/connection/app";
            public static final String UPDATE_TOKEN = "/token/update";
            public static final String RECEIVING_DEVICES = "/management/user/devices";
            public static final String OBTAINING_DEVICE_INFORMATION = "/management/user/devices/{id}";
            public static final String STATE_CHANGE = "/management/user/devices/{id}/change";
            public static final String RESET_STATUS = "/management/user/devices/{id}/reset";
        }
}
