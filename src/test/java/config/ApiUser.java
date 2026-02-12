package config;

import userauthorization.LoginUser;
import userauthorization.RegisterUser;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class ApiUser {

    public Response createUser(RegisterUser userData) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .baseUri(EvnConfig.BASE_URL)
                .body(userData)
                .when()
                .post(EvnConfig.USER_CREATE_ENDPOINT)
                .then()
                .statusCode(200)
                .extract()
                .response();
    }

    public String getAccessToken(RegisterUser userData) {
        LoginUser credentials = new LoginUser(userData.getEmail(), userData.getPassword());

        String token = RestAssured.given()
                .contentType(ContentType.JSON)
                .baseUri(EvnConfig.BASE_URL)
                .body(credentials)
                .when()
                .post(EvnConfig.LOGIN_ENDPOINT)
                .then()
                .statusCode(200)
                .extract()
                .path("accessToken");

        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }

    public void deleteUser(RegisterUser userData) {
        String accessToken = getAccessToken(userData);

        if (accessToken == null || accessToken.trim().isEmpty()) {
            throw new RuntimeException("Не удалось получить токен для удаления пользователя");
        }

        String[] authAttempts = {"Bearer " + accessToken, accessToken};

        for (String authHeader : authAttempts) {
            Response response = RestAssured.given()
                    .header("Authorization", authHeader)
                    .contentType(ContentType.JSON)
                    .baseUri(EvnConfig.BASE_URL)
                    .when()
                    .delete(EvnConfig.USER_DELETE_ENDPOINT);

            if (response.getStatusCode() == 202) {
                return;
            }
        }

        throw new RuntimeException("Не удалось удалить пользователя");
    }


    public RegisterUser createTestUser() {
        long ts = System.currentTimeMillis();
        String name = "TestUser_" + ts;
        String email = "test_" + ts + "@example.com";
        String password = "TestPass123";

        RegisterUser user = new RegisterUser(email, password, name);
        createUser(user);
        return user;
    }

    public void logUserInfo(RegisterUser userData) {
        System.out.println("Для ручного удаления:");
        System.out.println("   Email: " + userData.getEmail());
        System.out.println("   Пароль: " + userData.getPassword());
    }
}

