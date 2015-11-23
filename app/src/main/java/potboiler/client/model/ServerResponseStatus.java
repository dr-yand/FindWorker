package potboiler.client.model;

import java.util.HashMap;
import java.util.Map;

public enum ServerResponseStatus {
    OK(0), ERROR(-200), AUTH_ERROR(-100), NO_FILLED_ERROR(1), LOGIN_ALREADY_USE_ERROR(2), LOGIN_SHORT_ERROR(3),
    EMAIL_ALREADY_USE_ERROR(4), WRONG_LOGIN_PASSWORD_ERROR(5), EMAIL_NOT_FOUND_ERROR(6), RESTORE_LIMIT_EXCEEDED(7),
    SOC_AUTH_ERROR(8),NOT_FILLED_SEARCH(9),NOT_FOUND(10),ALREADY_VOTED(11),ALREADY_LINKED_ACCOUNT(12),NOT_SOCIAL_NETWORK  (13),
    CAN_NOT_UNLINKING(14),;

    public final int value;

    private static Map<Integer, ServerResponseStatus> map = new HashMap<Integer, ServerResponseStatus>();

    static {
        for (ServerResponseStatus srs : ServerResponseStatus.values()) {
            map.put(srs.value, srs);
        }
    }

    private ServerResponseStatus(int i) {
        value = i;
    }

    public static ServerResponseStatus valueOf(int i){
        return map.get(i);
    }

    @Override
    public String toString(){
        if(value==OK.value){
            return "Успешно";
        }
        if(value==NO_FILLED_ERROR.value){
            return "Не все обязательные данные переданы";
        }
        if(value==LOGIN_ALREADY_USE_ERROR.value){
            return "Такой логин уже существует";
        }
        if(value==LOGIN_SHORT_ERROR.value){
            return "Логин слишком короткий";
        }
        if(value==EMAIL_ALREADY_USE_ERROR.value){
            return "Такой email уже существует";
        }
        if(value==WRONG_LOGIN_PASSWORD_ERROR.value){
            return "Неверная пара логин/пароль";
        }
        if(value==EMAIL_NOT_FOUND_ERROR.value){
            return "Email для восстановления не найден";
        }
        if(value==RESTORE_LIMIT_EXCEEDED.value){
            return "Превышено кол-во неудачных попыток авторизации";
        }
        if(value==SOC_AUTH_ERROR.value){
            return "Ошибка при авторизации через соцсеть (неверный токен или другая причина)";
        }
        if(value==NOT_FILLED_SEARCH.value){
            return "Поисковое слово пустое";
        }
        if(value==NOT_FOUND.value){
            return "По данному запросу ничего не найдено";
        }
        if(value==ALREADY_VOTED.value){
            return "Вы уже проголосовали за данного пользователя в данном заказе";
        }
        if(value==ALREADY_LINKED_ACCOUNT.value){
            return "Уже привязан данный аккаунт";
        }
        if(value==NOT_SOCIAL_NETWORK.value){
            return "Нет такой соцсети у данного пользователя";
        }
        if(value==CAN_NOT_UNLINKING.value){
            return "Это единственный способ авторизации нельзя отвязать!";
        }
        return "Произошла ошибка (код "+value+")";
    }
}
