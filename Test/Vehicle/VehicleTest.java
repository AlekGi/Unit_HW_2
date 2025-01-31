package Vehicle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class VehicleTest {

    public Car car;
    public Motorcycle motorcycle;

    @BeforeEach
    void setUp() {
        car = new Car("SEAT", "Leon", 2003);
        motorcycle = new Motorcycle("Suzuki", "SV 400", 2004);
    }

    /** Проверка того, что экземпляр объекта Car также является экземпляром транспортного средства; (instanceof)
    */
    @Test
    void instanceOf(){
        assertThat(car instanceof Vehicle);
    }

    /** Проверка того, объект Car создается с 4-мя колесами
    */
    @Test
    void isCarHasWheels (){
        assertThat(car.getNumWheels()).isEqualTo(4);
    }

    /** Проверка того, объект Motorcycle создается с 2-мя колесами
     */
    @Test
    void isMotorcycleHasWheels (){
        assertThat(motorcycle.getNumWheels()).isEqualTo(2);
    }

    /** Проверка того, объект Car развивает скорость 60 в режиме тестового вождения (testDrive())
     */
    @Test
    void testDriveCar (){
        car.testDrive();
        assertThat(car.getSpeed()).isEqualTo(60);
    //  System.out.println("Автомобиль в режиме тест-драйва, его скорость: " + car.getSpeed());
    }

    /** Проверка того, объект Motorcycle развивает скорость 75 в режиме тестового вождения (testDrive())
     */
    @Test
    void testDriveMotorcycle (){
        motorcycle.testDrive();
        assertThat(motorcycle.getSpeed()).isEqualTo(75);
    //  System.out.println("Мотоцикл в режиме тест-драйва, его скорость: " + motorcycle.getSpeed());
    }

    /** Проверить, что в режиме парковки (сначала testDrive, потом park, т.е эмуляция движения транспорта)
     * машина останавливается (speed = 0)
     */
    @Test
    void parkCar(){
        car.testDrive();
    //  System.out.println("Автомобиль в движении, его скорость: " + car.getSpeed());
        car.park();
        assertThat(car.getSpeed()).isEqualTo(0);
    //  System.out.println("Автомобиль припарковался, его скорость: " + car.getSpeed());
    }
    /** Проверить, что в режиме парковки (сначала testDrive, потом park, т.е эмуляция движения транспорта)
     * мотоцикл останавливается (speed = 0)
    */
    @Test
    void parkMotorcycle() {
        motorcycle.testDrive();
    //  System.out.println("Мотоцикл в движении, его скорость: " + motorcycle.getSpeed());
        motorcycle.park();
        assertThat(motorcycle.getSpeed()).isEqualTo(0);
    //  System.out.println("Мотоцикл припарковался, его скорость: " + motorcycle.getSpeed());
    }

}
