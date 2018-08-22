package com.data.importer.utils;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public interface Mappable<S, D> extends Converter<S, D> {
    D map(S source);

    @Override
    default D convert(MappingContext<S, D> context) {
        return map(context.getSource());
    }
}
