package com.herbert.PixAPI.Pix;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PixRepository extends JpaRepository<Pix, UUID> {
    List<Pix> findByNumeroContaAndNumeroAgencia(int numeroConta,
                                                int numeroAgencia);

}
