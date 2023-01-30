package com.marchal.christophe.phoresttechtest.migration;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class MigratingValidator {
    private final Validator validator;

    public MigratingValidator(Validator validator) {
        this.validator = validator;
    }

    public <T> ValidatedEntity<T> validate(List<T> entities) {
        Map<Boolean, List<T>> partitionedClients = entities.stream()
                .collect(Collectors.partitioningBy(client ->
                        validator.validate(client).size() == 0
                ));
        List<T> invalidEntities = partitionedClients.get(false);

        Map<T, Set<ConstraintViolation<T>>> errors = invalidEntities.stream()
                .collect(Collectors.toMap(Function.identity(), validator::validate));

        List<T> validEntities = partitionedClients.get(true);
        return new ValidatedEntity<T>(validEntities, errors);
    }
}
