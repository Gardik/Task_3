package config;

public class EvnConfig {

    public static final String BASE_URL = "https://stellarburgers.education-services.ru/";

    // API endpoints
    public static final String USER_CREATE_ENDPOINT = "api/auth/register";
    public static final String LOGIN_ENDPOINT = "api/auth/login";
    public static final String USER_DELETE_ENDPOINT = "api/auth/user";

    // UI URLs
    public static final String MAIN_PAGE = BASE_URL;
    public static final String LOGIN_PAGE = BASE_URL + "login";
    public static final String REGISTER_PAGE = BASE_URL + "register";
    public static final String FORGOT_PASSWORD_PAGE = BASE_URL + "forgot-password";

    private EvnConfig() {}
}
