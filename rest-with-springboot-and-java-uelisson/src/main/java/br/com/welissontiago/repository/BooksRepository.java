package br.com.welissontiago.repository;

import br.com.welissontiago.model.BooksModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BooksRepository extends JpaRepository<BooksModel, Long> {
}
