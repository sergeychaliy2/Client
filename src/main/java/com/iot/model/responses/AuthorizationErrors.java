package com.iot.model.responses;


public enum AuthorizationErrors{

    PASSWORD_FORMAT_IS_INCORRECT {
        @Override
        public String toString() {
            return "Некорректный формат пароля";
        }
    },

    EMAIL_FORMAT_IS_NOT_VALID {
        @Override
        public String toString() {
            return "Некорректный формат электронного адреса";
        }
    },
    NOT_AUTHORIZED {
        @Override
        public String toString() {
            return "Для управленияя вы должны быть авторизованы";
        }
    },
    AUTHORIZED_SERVICE {
        @Override
        public String toString() {
            return "Управление доступно для использования";
        }
    },

    VERIFICATION_CODE_IS_NOT_VALID {
        @Override
        public String toString() {
            return "Код подтверждения неверный";
        }
    },
    ERROR_VERIFICATION_CODE_LENGTH_OR_FORMAT_INCORRECT {
        @Override
        public String toString() {
            return "Код некорректной длины или имеет неправильный формат";
        }
    },
    EMAIL_MESSAGE_ALREADY_SENT {
        @Override
        public String toString() {
            return "Такое почтовое сообщение уже было отправлено";
        }
    },
    EMAIL_MESSAGE_FAILED {
        @Override
        public String toString() {
            return "Почтовое письмо не было отправлено";
        }
    },
    CONNECTION_TIME_WAS_EXPIRED {
        @Override
        public String toString() {
            return "Время подключения вышло";
        }
    },
    USER_ALREADY_EXISTS {
        @Override
        public String toString() {
            return "Пользователь c такими данными уже существует";
        }
    },
    CLIENT_IS_NOT_AUTHENTICATED {
        @Override
        public String toString() {
            return "Сначала пройдите верификацию по коду подтверждения";
        }
    },
    PASSWORD_IS_NOT_CORRECT {
        @Override
        public String toString() {
            return "Указан неверный пароль";
        }
    },
    TOKEN_IS_ENDED {
        @Override
        public String toString() {
            return "Время жизни токена было истрачено или его структура некорректна";
        }
    },
    NO_USER_ID {
        @Override
        public String toString() {
            return "В Payload JWT отсутствует айди пользователя";
        }
    },
    NO_USER {
        @Override
        public String toString() {
            return "Пользователь не найден";
        }
    },
    WRONG_DATA_USER {
        @Override
        public String toString() {
            return "Неверные данные пользователя";
        }
    },
    ERROR_AUTHORIZED {
        @Override
        public String toString() {
            return "";
        }
    };
}
