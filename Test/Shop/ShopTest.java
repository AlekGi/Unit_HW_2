package Shop;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ShopTest {

    // Создаем набор продуктов для магазина:
    public static List<Product> getStoreItems() {
        List<Product> products = new ArrayList<>();

        // Три массива Названия, Цены, Кол-во
        String[] productNames = {"bacon", "beef", "ham", "salmon", "carrot", "potato", "onion", "apple", "melon", "rice", "eggs", "yogurt"};
        Double[] productPrice = {170.00d, 250.00d, 200.00d, 150.00d, 15.00d, 30.00d, 20.00d, 59.00d, 88.00d, 100.00d, 80.00d, 55.00d};
        Integer[] stock = {10, 10, 10, 10, 10, 10, 10, 70, 13, 30, 40, 60};

        // Последовательно наполняем список продуктами
        for (int i = 0; i < productNames.length; i++) {
            products.add(new Product(i + 1, productNames[i], productPrice[i], stock[i]));
        }

        // тоже самое
        // Product product = new Product(1,"bacon", 170.00d, 10);
        // products.add(product);
        return products;
    }

    private ByteArrayOutputStream output = new ByteArrayOutputStream();

    private Shop shop;
    private Cart cart;

    @BeforeEach
    void setup() {
        shop = new Shop(getStoreItems());
        cart = new Cart(shop);
    }


/*
            ID | Название  | Цена, р. | Кол-во в магазине, шт.
            1  | bacon     | 170.0    | 10
            2  | beef      | 250.0    | 10
            3  | ham       | 200.0    | 10
            4  | salmon    | 150.0    | 10
            5  | carrot    | 15.0     | 10
            6  | potato    | 30.0     | 10
            7  | onion     | 20.0     | 10
            8  | apple     | 59.0     | 70
            9  | melon     | 88.0     | 13
            10 | rice      | 100.0    | 30
            11 | eggs      | 80.0     | 40
            12 | yogurt    | 55.0     | 60
*/

    /**
     * 2.1. Разработайте модульный тест для проверки, что общая стоимость
     * корзины с разными товарами корректно рассчитывается
     * <br><b>Ожидаемый результат:</b>
     * Стоимость корзины посчиталась корректно
     */
    @Test
    void priceCartIsCorrectCalculated() {
        // Arrange (Подготовка)

        // Act (Выполнение)
        cart.addProductToCartByID(1); // 170 +
        cart.addProductToCartByID(2); // 250 +
        cart.addProductToCartByID(3); // 200 = 620
        cart.removeProductByID(2); // 620 - 250 = 370
        // Assert (Проверка утверждения)
        assertThat(cart.getTotalPrice()).isEqualTo(370);

    }

    /**
     * 2.2. Создайте модульный тест для проверки, что общая стоимость
     * корзины с множественными экземплярами одного и того же продукта корректно рассчитывается.
     * <br><b>Ожидаемый результат:</b>
     * Стоимость корзины посчиталась корректно
     */
    @Test
    void priceCartProductsSameTypeIsCorrectCalculated() {
        // Arrange (Подготовка)

        // Act (Выполнение)
        cart.addProductToCartByID(1); // 170 +
        cart.addProductToCartByID(1); // 170 +
        cart.addProductToCartByID(1); // 170 +
        cart.addProductToCartByID(2); // 250 +
        cart.addProductToCartByID(2); // 250 +
        cart.addProductToCartByID(2); // 250 = 1260
        // Assert (Проверка утверждения)
        assertThat(cart.getTotalPrice()).isEqualTo(1260);
    }

    /**
     * 2.3. Напишите модульный тест для проверки, что при удалении
     * товара из корзины происходит перерасчет общей стоимости корзины.
     * <br><b>Ожидаемый результат:</b>
     * Вызывается метод пересчета стоимости корзины, стоимость корзины меняется
     */
    @Test
    void whenChangingCartCostRecalculationIsCalled() {
        // Подготовка

        // Выполнение
        cart.addProductToCartByID(1); // 170 +
        cart.addProductToCartByID(2); // 250 +
        cart.addProductToCartByID(3); // 200 = 620
        // Проверка
        assertThat(cart.getTotalPrice()).isEqualTo(620);
    }

    /**
     * 2.4. Разработайте модульный тест для проверки, что при добавлении определенного количества товара в корзину,
     * общее количество этого товара в магазине соответствующим образом уменьшается.
     * <br><b>Ожидаемый результат:</b>
     * Количество товара в магазине уменьшается на число продуктов в корзине пользователя
     */
    @Test
    void quantityProductsStoreChanging() {

        // Выполнение
        cart.addProductToCartByID(1); // 1
        cart.addProductToCartByID(1); // 2
        cart.addProductToCartByID(1); // 3
        cart.addProductToCartByID(1); // 4
        cart.addProductToCartByID(1); // 5
        cart.addProductToCartByID(1); // 6
        cart.addProductToCartByID(1); // 7
        cart.addProductToCartByID(1); // 8
        cart.addProductToCartByID(1); // 9
        cart.addProductToCartByID(1); // 10
        cart.addProductToCartByID(1); // Нет в наличии
        // Проверка
        assertThat(cart.cartItems.get(0).getQuantity()).isEqualTo(10);
        assertThat(shop.getProductsShop().get(0).getQuantity()).isEqualTo(0);

    }

    /**
     * 2.5. Создайте модульный тест для проверки, что если пользователь забирает все имеющиеся продукты о
     * пределенного типа из магазина, эти продукты больше не доступны для заказа.
     * <br><b>Ожидаемый результат:</b>
     * Больше такой продукт заказать нельзя, он не появляется на полке
     */

    @Test
    void lastProductsDisappearFromStore() {
        // Подготовка

        // Выполнение
        cart.addProductToCartByID(1); // 1
        cart.addProductToCartByID(1); // 2
        cart.addProductToCartByID(1); // 3
        cart.addProductToCartByID(1); // 4
        // Проверка
        assertThat(shop.getProductsShop().get(0).getQuantity()).isEqualTo(6);

    }

    /**
     * 2.6. Напишите модульный тест для проверки, что при удалении товара из корзины,
     * общее количество этого товара в магазине соответствующим образом увеличивается.
     * <br><b>Ожидаемый результат:</b>
     * Количество продуктов этого типа на складе увеличивается на число удаленных из корзины продуктов
     */
    @Test
    void deletedProductIsReturnedToShop() {
        // Подготовка

        // Выполнение
        cart.addProductToCartByID(1); // 9
        cart.addProductToCartByID(1); // 8
        cart.addProductToCartByID(1); // 7
        cart.addProductToCartByID(1); // 6
        cart.addProductToCartByID(1); // 5
        cart.addProductToCartByID(1); // 4
        cart.addProductToCartByID(1); // 3
        cart.addProductToCartByID(1); // 2
        cart.addProductToCartByID(1); // 1
        cart.addProductToCartByID(1); // 0
        cart.removeProductByID(1); // 1
        cart.removeProductByID(1); // 2
        cart.removeProductByID(1); // 3
        cart.removeProductByID(1); // 4
        // Проверка
        assertThat(shop.getProductsShop().get(0).getQuantity()).isEqualTo(4); // Товаров осталось в магазине
        assertThat(cart.cartItems.get(0).getQuantity()).isEqualTo(6); // Товаров в корзине

        String exceptionMessage = "Этого товара нет в наличии";

        assertThat(exceptionMessage);


    }

    /**
     * 2.7. Разработайте параметризованный модульный тест для проверки,
     * что при вводе неверного идентификатора товара генерируется исключение RuntimeException.
     * <br><b>Ожидаемый результат:</b>
     * Исключение типа RuntimeException и сообщение Не найден продукт с id
     * *Сделать тест параметризованным
     */
//    @Test
    @ParameterizedTest
    @ValueSource(ints = {-100, 100})
    void incorrectProductSelectionCausesException(int i) {
        // Подготовка

        int id = i;
        // Выполнение
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cart.addProductToCartByID(id);
        });
        // Проверка
        String exceptionMessage = "Не найден продукт с id: " + id;
        String actualMessage = exception.getMessage();

        assertEquals(exceptionMessage, actualMessage);
    }

    /**
     * 2.8.      * 2.8. Создайте модульный тест для проверки, что при попытке удалить из корзины больше товаров,
     * чем там есть, генерируется исключение RuntimeException.удаляет продукты до того, как их добавить)
     * <br><b>Ожидаемый результат:</b> Исключение типа NoSuchFieldError и сообщение "В корзине не найден продукт с id"
     */
    @Test
    void incorrectProductRemoveCausesException() {
        // Подготовка

        int id = 1;
        // Выполнение
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cart.removeProductByID(id);
        });
        // Проверка
        String exceptionMessage = "В корзине не найден продукт с id: " + id;
        String actualMessage = exception.getMessage();

        assertEquals(exceptionMessage, actualMessage);
    }

    /**
     * 2.9. Нужно восстановить тест
     */
//     boolean Сломанный-Тест() {
//         // Assert (Проверка утверждения)
//         assertThat(cart.getTotalPrice()).isEqualTo(cart.getTotalPrice());
//         // Act (Выполнение)
//         cart.addProductToCartByID(2); // 250
//         cart.addProductToCartByID(2); // 250
//         // Arrange (Подготовка)
//         Shop shop = new Shop(getStoreItems());
//         Cart cart = new Cart(shop);
//     }
    @Test
    void testSUM() {
        // Arrange (Подготовка)

        // Act (Выполнение)
        cart.addProductToCartByID(2); // 250
        cart.addProductToCartByID(2); // 250
        // Assert (Проверка утверждения)
        assertThat(cart.getTotalPrice()).isEqualTo(500);
    }


    /**
     * 2.10. Нужно оптимизировать тестовый метод, согласно следующим условиям:
     * <br> 1. Отображаемое имя - "Advanced test for calculating TotalPrice"
     * <br> 2. Тест повторяется 10 раз
     * <br> 3. Установлен таймаут на выполнение теста 70 Миллисекунд (unit = TimeUnit.MILLISECONDS)
     * <br> 4. После проверки работоспособности теста, его нужно выключить
     */

    @DisplayName("Advanced test for calculating TotalPrice")
    @RepeatedTest(10)
    @Timeout(value = 700, unit = TimeUnit.MILLISECONDS)
    @Disabled
    void optimazedTest() {
        // Подготовка

        // Выполнение
        cart.addProductToCartByID(1);
        cart.removeProductByID(1);
        // Проверка
        assertThat(cart.getTotalPrice()).isEqualTo(0);
    }

}
