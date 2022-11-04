package org.art;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.SneakyThrows;
import org.art.entity.Birthday;
import org.art.entity.User;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

class HibernateRunnerTest {

    // Как Хибер под капотом парсит ответ из БД в JavaPojo
    @SneakyThrows
    @Test
    void checkGetReflectionApi() {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.getString("username");
        resultSet.getString("lastname");
        resultSet.getString("firstname");

        Class<User> clazz = User.class;

        Constructor<User> constructor = clazz.getConstructor();
        User user = constructor.newInstance();
        Field usernameField = clazz.getDeclaredField("username");
        usernameField.setAccessible(true);
        usernameField.set(user, resultSet.getString("username"));
    }

    @SneakyThrows
    @Test
    void checkReflectionApi() {
        User user = User.builder()
                .username("ivan@gmail.com")
                .firstname("Ivan")
                .lastname("Ivanov")
                .birthDate(new Birthday(LocalDate.of(2000, 1, 19)))
                .build();

        // как хибер составляет sql-запрос при помощи reflection-api:
        // нужно сформировать
        // insert into public.users (age, birth_date, firstname, lastname, username) values (?, ?, ?, ?, ?)

        // 1. Заготовка:
        String sql = """
                INSERT
                INTO
                %s
                (%s)
                VALUES
                (%s)
                """;
        // Теперь в динамичускую составляющую %s нужно подставить при помощи reflection-api значения

        // 2. Получим название таблицы, куда нужно записать данные, но учитывая, что аннотации может не быть, т.к. название
        // таблицы может соответствовать названию класса - тогда хибер берет название класса
        String tableName = Optional.ofNullable(user.getClass().getAnnotation(Table.class))
                .map(tableAnnotation -> tableAnnotation.schema() + "." + tableAnnotation.name())
                .orElse(user.getClass().getName());

        // 3. Получим названия полей с учетом, что поля могут иметь аннотацию указывающую на другое название колоки в БД
        // getDeclaredFields() не гарантирует нам порядок, поэтому в идеале стоило бы их отсортировать по названию
        Field[] declaredFields = user.getClass().getDeclaredFields();
        String columnNames = Arrays.stream(declaredFields)
                .map(field -> Optional.ofNullable(field.getAnnotation(Column.class))
                        .map(Column::name)
                        .orElse(field.getName()))
                .collect(Collectors.joining(", "));

        // 4. Вставим вопросики. Сколько полей, столько и вопросиков
        String columnValues = Arrays.stream(declaredFields)
                .map(field -> "?")
                .collect(Collectors.joining(", "));

        System.out.println(sql.formatted(tableName, columnNames, columnValues));

        // 5. Осталось в prepareStatement передать этот sql и циклом пройтись по каждому полю
        Connection connection = null;
        PreparedStatement preparedStatement = connection
                .prepareStatement(sql.formatted(tableName, columnNames, columnValues));

        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            // declaredField.get(user) - получаем у филда значение у объекта user
            preparedStatement.setObject(1, declaredField.get(user));
        }
    }
}