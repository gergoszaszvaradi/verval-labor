package service;

import domain.Grade;
import domain.Homework;
import domain.Student;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import repository.GradeXMLRepository;
import repository.HomeworkXMLRepository;
import repository.StudentXMLRepository;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ServiceMockTest {
    Service service;
    @Mock
    StudentXMLRepository studentXMLRepository;
    @Mock
    HomeworkXMLRepository homeworkXMLRepository;
    @Mock
    GradeXMLRepository gradeXMLRepository;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this); // initMocks is deprecated in mockito-junit-jupiter:4.4.0
        service = new Service(studentXMLRepository, homeworkXMLRepository, gradeXMLRepository);
    }

    @AfterEach
    public void cleanUp() {
        Mockito.clearInvocations(studentXMLRepository);
        Mockito.clearInvocations(homeworkXMLRepository);
        Mockito.clearInvocations(gradeXMLRepository);
    }

    @Test
    public void testSaveStudentShouldReturnOne() {
        Mockito.when(studentXMLRepository.save(Mockito.any(Student.class))).thenReturn(null);
        int result = service.saveStudent("3", "student", 500);
        Assertions.assertEquals(1, result);
        Mockito.verify(studentXMLRepository).save(Mockito.any(Student.class));
    }

    @Test
    public void testSaveStudentShouldReturnZero() {
        Mockito.when(studentXMLRepository.save(Mockito.any(Student.class))).thenReturn(Mockito.mock(Student.class));
        int result = service.saveStudent("4", "student", 500);
        assertEquals(0, result);
        Mockito.verify(studentXMLRepository).save(Mockito.any(Student.class));
    }

    @Test
    public void testSaveShouldReturnOne() {
        Mockito.when(studentXMLRepository.delete(Mockito.any(String.class))).thenReturn(Mockito.mock(Student.class));
        int result = service.deleteStudent("2");
        assertEquals(1, result);
        Mockito.verify(studentXMLRepository).delete(Mockito.any(String.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "-1", "5"})
    public void testDeleteStudentShouldReturnZero(String input) {
        Mockito.when(studentXMLRepository.delete(Mockito.any(String.class))).thenReturn(null);
        int result = service.deleteStudent(input);
        assertEquals(0, result);
        Mockito.verify(studentXMLRepository).delete(Mockito.any(String.class));
    }

    @Test
    public void testSaveGradeShouldReturnNegative() {
        Mockito.when(studentXMLRepository.findOne(Mockito.any(String.class))).thenReturn(null);
        Mockito.when(homeworkXMLRepository.findOne(Mockito.any(String.class))).thenReturn(null);
        int result = service.saveGrade("2", "2", 7.0, 6, "");
        assertEquals(-1, result);
        Mockito.verify(studentXMLRepository).findOne(Mockito.any(String.class));
        Mockito.verify(homeworkXMLRepository, Mockito.never()).findOne(Mockito.any(String.class));
        Mockito.clearInvocations(studentXMLRepository);
        Mockito.clearInvocations(homeworkXMLRepository);

        Mockito.when(studentXMLRepository.findOne(Mockito.any(String.class))).thenReturn(null);
        Mockito.when(homeworkXMLRepository.findOne(Mockito.any(String.class))).thenReturn(Mockito.any(Homework.class));
        result = service.saveGrade("2", "2", 7.0, 6, "");
        assertEquals(-1, result);
        Mockito.verify(studentXMLRepository).findOne(Mockito.any(String.class));
        Mockito.verify(homeworkXMLRepository, Mockito.never()).findOne(Mockito.any(String.class));
        Mockito.clearInvocations(studentXMLRepository);
        Mockito.clearInvocations(homeworkXMLRepository);

        Mockito.when(studentXMLRepository.findOne(Mockito.any(String.class))).thenReturn(Mockito.mock(Student.class));
        Mockito.when(homeworkXMLRepository.findOne(Mockito.any(String.class))).thenReturn(null);
        result = service.saveGrade("2", "2", 7.0, 6, "");
        assertEquals(-1, result);
        Mockito.verify(studentXMLRepository).findOne(Mockito.any(String.class));
        Mockito.verify(homeworkXMLRepository).findOne(Mockito.any(String.class));
        Mockito.clearInvocations(studentXMLRepository);
        Mockito.clearInvocations(homeworkXMLRepository);

        Mockito.verify(gradeXMLRepository, Mockito.never()).save(Mockito.any(Grade.class));
    }

    @Test
    public void testSaveGradeShouldReturnZero() {
        Mockito.when(studentXMLRepository.findOne(Mockito.any(String.class))).thenReturn(Mockito.mock(Student.class));
        Mockito.when(homeworkXMLRepository.findOne(Mockito.any(String.class))).thenReturn(Mockito.mock(Homework.class));
        Mockito.when(gradeXMLRepository.save(Mockito.any(Grade.class))).thenReturn(Mockito.mock(Grade.class));

        int result = service.saveGrade("2", "2", 7.0, 6, "");

        assertEquals(0, result);
        Mockito.verify(studentXMLRepository).findOne(Mockito.any(String.class));
        Mockito.verify(homeworkXMLRepository, Mockito.times(2)).findOne(Mockito.any(String.class));
        Mockito.verify(gradeXMLRepository).save(Mockito.any(Grade.class));
    }

    @Test
    public void testSaveGradeShouldReturnOne() {
        Mockito.when(studentXMLRepository.findOne(Mockito.any(String.class))).thenReturn(Mockito.mock(Student.class));
        Mockito.when(homeworkXMLRepository.findOne(Mockito.any(String.class))).thenReturn(Mockito.mock(Homework.class));
        Mockito.when(gradeXMLRepository.save(Mockito.any(Grade.class))).thenReturn(null);

        int result = service.saveGrade("2", "2", 7.0, 6, "");

        assertEquals(1, result);
        Mockito.verify(studentXMLRepository).findOne(Mockito.any(String.class));
        Mockito.verify(homeworkXMLRepository, Mockito.times(2)).findOne(Mockito.any(String.class));
        Mockito.verify(gradeXMLRepository).save(Mockito.any(Grade.class));
    }
}
