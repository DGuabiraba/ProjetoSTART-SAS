package com.sas.sas_backend.utils.password; // Mover para um pacote mais adequado, ex: com.sas.sas_backend.converters

import com.sas.sas_backend.models.enumerated.TipoSanguineo;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoSanguineoConverter implements AttributeConverter<TipoSanguineo, String> {

    @Override
    public String convertToDatabaseColumn(TipoSanguineo tipo) {
        return tipo != null ? tipo.getValor() : null;
    }

    @Override
    public TipoSanguineo convertToEntityAttribute(String valor) {
        return valor != null ? TipoSanguineo.fromValor(valor) : null;
    }
}