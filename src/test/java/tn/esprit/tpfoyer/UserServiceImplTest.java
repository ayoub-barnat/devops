package tn.esprit.tpfoyer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer.entity.Etudiant;
import tn.esprit.tpfoyer.repository.EtudiantRepository;
import tn.esprit.tpfoyer.service.EtudiantServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
class EtudiantServiceImplTest {

    private final Etudiant e1 = new Etudiant("ayoub", "barnat", 123, new Date());

    private final List<Etudiant> listEtudiant = new ArrayList<Etudiant>() {{
        add(new Etudiant("barnat", "ayoub", 123, new Date()));
        add(new Etudiant("john", "doe", 456, new Date()));
    }};

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private EtudiantServiceImpl us;

    @Test
    @Order(1)
    public void testRetrieveAllEtudiants() {
        Mockito.when(etudiantRepository.findAll()).thenReturn(listEtudiant);
        List<Etudiant> retrievedEtudiants = us.retrieveAllEtudiants();
        Assertions.assertEquals(2, retrievedEtudiants.size());
    }

    @Test
    @Order(2)
    public void testRetrieveEtudiant() {
        Mockito.when(etudiantRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(e1));
        Etudiant etudiant = us.retrieveEtudiant(1L);
        Assertions.assertNotNull(etudiant);
        Assertions.assertEquals(e1.getNomEtudiant(), etudiant.getNomEtudiant());
    }

    @Test
    @Order(3)
    public void testAddEtudiant() {
        Mockito.when(etudiantRepository.save(Mockito.any(Etudiant.class))).thenReturn(e1);
        Etudiant savedEtudiant = us.addEtudiant(e1);
        Assertions.assertEquals(e1.getNomEtudiant(), savedEtudiant.getNomEtudiant());
    }

    @Test
    @Order(4)
    public void testUpdateEtudiant() {
        // Prepare the updated student
        Etudiant updatedEtudiant = new Etudiant("ayoub", "updated", 789, new Date());

        // Use lenient stubbing on findById to avoid UnnecessaryStubbingException
        Mockito.lenient().when(etudiantRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(e1));

        // Mock the save operation
        Mockito.when(etudiantRepository.save(Mockito.any(Etudiant.class))).thenReturn(updatedEtudiant);

        // Call the update method
        Etudiant etudiant = us.modifyEtudiant(updatedEtudiant);

        // Assert that the student's first name was updated correctly
        Assertions.assertEquals("updated", etudiant.getPrenomEtudiant());

        // Verify that save was called once
        Mockito.verify(etudiantRepository).save(Mockito.any(Etudiant.class));
    }


    @Test
    @Order(5)
    public void testDeleteEtudiant() {
        // Use lenient stubbing to avoid UnnecessaryStubbingException
        Mockito.lenient().when(etudiantRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(e1));

        us.removeEtudiant(1L);
        Mockito.verify(etudiantRepository, Mockito.times(1)).deleteById(1L);
    }
}
