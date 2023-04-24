package com.iot.model.responses;

public enum ServiceErrors {
    NO_DEVICE {
        @Override public String toString() {
            return "Устройство не было обнаружено";
        }
    },
    NO_USER_DEVICE {
        @Override public String toString() {
            return "Пользователь или устройство не было найдено";
        }
    },
    NO_STATE_DEVICE{
        @Override public String toString(){return "Состояние устройства не было обновлено";}
    },
    NO_LISTEN_SOCKET{
        @Override public String toString(){return "Устройство не прослушивается сокетом в текущий момент";}
    },
    NO_STANDART{
        @Override public String toString() {
            return "Переданное состояние устройства для изменения не соответствует стандартам";
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
    },;
}
