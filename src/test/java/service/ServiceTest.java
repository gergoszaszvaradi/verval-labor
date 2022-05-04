package service;

import domain.Grade;
import domain.Homework;
import domain.Student;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import repository.GradeXMLRepository;
import repository.HomeworkXMLRepository;
import repository.StudentXMLRepository;
import validation.*;

import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ServiceTest {
    Service service;

    @BeforeEach
    public void setUp() {
        Validator<Student> studentValidator = new StudentValidator();
        Validator<Homework> homeworkValidator = new HomeworkValidator();
        Validator<Grade> gradeValidator = new GradeValidator();

        StudentXMLRepository fileRepository1 = new StudentXMLRepository(studentValidator, "students.xml");
        HomeworkXMLRepository fileRepository2 = new HomeworkXMLRepository(homeworkValidator, "homework.xml");
        GradeXMLRepository fileRepository3 = new GradeXMLRepository(gradeValidator, "grades.xml");

        service = new Service(fileRepository1, fileRepository2, fileRepository3);
    }

    @AfterAll
    public void cleanUp() {
        service.deleteStudent("3");
        service.saveStudent("2", "Maria", 222);
    }

    @Test
    public void testSaveStudentShouldReturnOne() {
        int result = service.saveStudent("3", "student", 500);
        assertEquals(1, result);
        assertTrue(StreamSupport.stream(service.findAllStudents().spliterator(), false).anyMatch(student -> student.getID().equals("3")));
    }

    @Test
    public void testSaveStudentShouldReturnZero() {
        int result = service.saveStudent("4", "student", 500);
        assertEquals(0, result);
    }

    @Test
    public void testSaveStudentShouldThrowIfInvalid() {
        assertThrows(ValidationException.class, () -> service.saveStudent("", "student", 500));
        assertThrows(ValidationException.class, () -> service.saveStudent("3", "", 500));
        assertThrows(ValidationException.class, () -> service.saveStudent("3", "student", 0));
    }

    @Test
    public void testDeleteStudentShouldReturnOne() {
        int result = service.deleteStudent("2");
        assertEquals(1, result);
        assertFalse(StreamSupport.stream(service.findAllStudents().spliterator(), false).anyMatch(student -> student.getID().equals("2")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "-1", "5"})
    public void testDeleteStudentShouldReturnZero(String input) {
        int result = service.deleteStudent(input);
        assertEquals(0, result);
    }

    @Test
    public void testDeleteStudentShouldThrowIfInvalid() {
        assertThrows(IllegalArgumentException.class, () -> service.deleteStudent(null));
    }
}
