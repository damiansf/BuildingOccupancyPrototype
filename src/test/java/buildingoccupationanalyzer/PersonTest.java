package buildingoccupationanalyzer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Class for testing the Person record
 */
public class PersonTest {
    @Test
    public void accessorsReturnConstructorArguments() {
        Person p = new Person("Alice", "555-0101");
        assertEquals("Alice", p.name());
        assertEquals("555-0101", p.phoneNumber());
    }

    @Test
    public void equalsIsValueBased() {
        Person a = new Person("Alice", "555-0101");
        Person b = new Person("Alice", "555-0101");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void differentNameNotEqual() {
        Person a = new Person("Alice", "555-0101");
        Person b = new Person("Bob",   "555-0101");
        assertNotEquals(a, b);
    }

    @Test
    public void differentPhoneNotEqual() {
        Person a = new Person("Alice", "555-0101");
        Person b = new Person("Alice", "555-0202");
        assertNotEquals(a, b);
    }

    @Test
    public void toStringContainsBothFields() {
        String s = new Person("Alice", "555-0101").toString();
        assertEquals(true, s.contains("Alice"));
        assertEquals(true, s.contains("555-0101"));
    }

    @Test
    public void allowsNullFields() {
        Person p = new Person(null, null);
        assertEquals(null, p.name());
        assertEquals(null, p.phoneNumber());
    }
}
