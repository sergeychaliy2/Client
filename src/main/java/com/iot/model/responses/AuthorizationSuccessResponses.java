package com.iot.model.responses;

public enum AuthorizationSuccessResponses {
    VERIFICATION_CODE_WAS_SENT {
        @Override public String toString() {
            return "Код был отправлен";
        }
    },
    VERIFICATION_CODE_SENT {
        @Override public String toString() {
            return "Verify code was sent";
        }
    },
    UNKNOWN_ERROR{
        @Override public String toString(){return "Неизвестная ошибка, обратитесь в службу поддержки";}
    },
    VERIFICATION_CODE_IS_RIGHT {
        @Override public String toString() {
            return "Код подтверждения верен";
        }
    },
    SUCCESSFULLY_REGISTRATION {
        @Override public String toString() {
            return "Регистрация успешна проведена";
        }
    },
    PASSWORD_RESET {
        @Override public String toString() {
            return "Пароль успешно сброшен";
        }
    },
    AUTHORIZATIN_COMPLETE {
        @Override public String toString() {
            return "Авторизация успешно пройдена";
        }
    },
    ACCOUNT_CONFIRMED {
        @Override public String toString() {
            return "Учетная запись подтверждена";
        }
    },
    RESET_CODE_WAS_SENT {
        @Override public String toString() {
            return "Код сброса пароля был отправлен";
        }
    },
    RESET_CODE_IS_RIGHT {
        @Override public String toString() {
            return "Код сброса пароля верен";
        }
    },
    DATA_CHANGED {
        @Override public String toString() {
            return "data was successfully changed";
        }
    }
}
