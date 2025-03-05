package co.edu.uniandes.dse.parcialprueba.services;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.context.annotation.ComponentScan;

import co.edu.uniandes.dse.parcialprueba.entities.MedicoEntity;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import co.edu.uniandes.dse.parcialprueba.exceptions.IllegalOperationException;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import({MedicoService.class})
@ComponentScan(basePackages = {"co.edu.uniandes.dse.parcialprueba.repositories"})
public class MedicoServiceTest {
//Implemente las pruebas para el método createMedicos del servicio de médico. Asegúrese de crear dos pruebas: una donde el médico se crea correctamente y otra donde se lanza una excepción de negocio por la violación de la regla de negocio.
	
    @Autowired
	private MedicoService medicoService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();
	private List<MedicoEntity> medicos = new ArrayList<>();

	/**
	 * Configuración inicial de la prueba.
	 */
	@BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from MedicoEntity").executeUpdate();
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		for (int i = 0; i < 3; i++) {
			MedicoEntity medicoEntity = factory.manufacturePojo(MedicoEntity.class);
			entityManager.persist(medicoEntity);
			medicos.add(medicoEntity);
		}
	}

    @Test
    void testCreateMedico() throws IllegalOperationException {
        //FACTORY
        MedicoEntity newEntity = factory.manufacturePojo(MedicoEntity.class);
        newEntity.setRegistroMedico("RM1234");
        newEntity.setEspecialidades(null);
        newEntity.setNombre("Juan");

        //SERVICE
        MedicoEntity result = medicoService.createMedico(newEntity);
        assertNotNull(result);

        //ENTITY MANAGER    
        MedicoEntity entity = entityManager.find(MedicoEntity.class, result.getId());
        //ASSERTS
        assertEquals(newEntity.getId(), entity.getId());
        assertEquals(newEntity.getNombre(), entity.getNombre());
        assertEquals(newEntity.getRegistroMedico(), entity.getRegistroMedico());
        assertEquals(newEntity.getEspecialidades(), entity.getEspecialidades());
    }

    @Test
    void testCreateMedicoInvalid() {
        MedicoEntity newEntity = factory.manufacturePojo(MedicoEntity.class);
        newEntity.setRegistroMedico("123"); 
        newEntity.setEspecialidades(null);
        
        assertThrows(IllegalOperationException.class, () -> {
            medicoService.createMedico(newEntity);
        });
    }

}
