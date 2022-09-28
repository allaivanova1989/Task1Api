package tests;

import api.spec.Specifications;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;


public class BaseTest {
    @BeforeSuite
    public void setup() {
        Specifications.installSpecification();
    }
}
