package co.edu.uniandes.dse.parcialprueba.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import co.edu.uniandes.dse.parcialprueba.entities.EspecialidadEntity;
import co.edu.uniandes.dse.parcialprueba.repositories.EspecialidadRepository;
import co.edu.uniandes.dse.parcialprueba.repositories.MedicoRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class MedicoEspecialidadService {

    @Autowired
    EspecialidadRepository especialidadRepository;
    @Autowired
    MedicoRepository medicoRepository;

    @Transactional
    public void addEspecialidad(Long medicoId, Long especialidadId) throws IllegalArgumentException {
        log.info("Agregando especialidad a medico");
        EspecialidadEntity especialidad = especialidadRepository.findById(especialidadId).get();
        //Valide que el médico exista
        if (medicoRepository.findById(medicoId).isEmpty()) {
            throw new IllegalArgumentException("El médico no existe");
        }
        //Valide que la especialidad exista
        if (especialidadRepository.findById(especialidadId).isEmpty()) {
            throw new IllegalArgumentException("La especialidad no existe");
        }
        log.info("Termina el proceso de agregar especialidad a medico");
        medicoRepository.findById(medicoId).get().getEspecialidades().add(especialidad);
    }

}
