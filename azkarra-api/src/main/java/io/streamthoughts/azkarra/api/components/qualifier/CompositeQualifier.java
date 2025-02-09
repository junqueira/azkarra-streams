/*
 * Copyright 2019 StreamThoughts.
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.streamthoughts.azkarra.api.components.qualifier;

import io.streamthoughts.azkarra.api.components.ComponentDescriptor;
import io.streamthoughts.azkarra.api.components.Qualifier;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompositeQualifier<T> implements Qualifier<T> {

    private final List<Qualifier<T>> qualifiers;

    /**
     * Creates a new {@link CompositeQualifier} instance.
     *
     * @param qualifiers    the list of {@link Qualifier}.
     */
    public CompositeQualifier(final List<Qualifier<T>> qualifiers) {
        this.qualifiers = qualifiers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<ComponentDescriptor<T>> filter(final Class<T> componentType,
                                                 final Stream<ComponentDescriptor<T>> candidates) {
        Stream<ComponentDescriptor<T>> reduced = candidates;
        for (Qualifier<T> qualifier : qualifiers) {
            reduced = qualifier.filter(componentType, reduced);
        }
        return reduced;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompositeQualifier)) return false;
        CompositeQualifier<?> that = (CompositeQualifier<?>) o;
        return Objects.equals(qualifiers, that.qualifiers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(qualifiers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return qualifiers.stream().map(Object::toString).collect(Collectors.joining(" and "));
    }
}
