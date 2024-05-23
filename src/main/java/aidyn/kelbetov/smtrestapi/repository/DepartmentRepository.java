    package aidyn.kelbetov.smtrestapi.repository;

    import aidyn.kelbetov.smtrestapi.model.Department;
    import aidyn.kelbetov.smtrestapi.model.User;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
    import org.springframework.stereotype.Repository;

    import java.util.List;
    import java.util.Optional;

    @Repository
    @EnableJpaRepositories
    public interface DepartmentRepository extends JpaRepository<Department, Long> {
    }
