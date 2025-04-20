package br.com.welissontiago;

import br.com.welissontiago.model.PersonModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<PersonModel, Long> {

    Long id(Long id);
}
