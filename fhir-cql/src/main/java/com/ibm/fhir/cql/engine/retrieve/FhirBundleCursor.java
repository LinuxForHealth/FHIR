package com.ibm.fhir.cql.engine.retrieve;

import java.util.Collections;
import java.util.function.Function;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Resource;

public class FhirBundleCursor implements Iterable<Object> {

    public static interface PageLoader extends Function<String, Bundle> {
    };

    private PageLoader pageLoader;
    private Bundle results;

    public FhirBundleCursor(PageLoader pageLoader, Bundle results) {
        this.pageLoader = pageLoader;
        this.results = results;
    }

    public Iterator<Object> iterator() {
        return new FhirBundleIterator(pageLoader, results);
    }

    private static class FhirBundleIterator implements Iterator<Object> {

        private PageLoader pageLoader;
        private Bundle results;
        private int current;
        private List<? extends Resource> currentEntry;

        public FhirBundleIterator(PageLoader pageLoader, Bundle results) {
            this.pageLoader = pageLoader;
            this.results = results;
            this.current = -1;
            this.currentEntry = getEntry();
        }

        public boolean hasNext() {
            return (current < this.currentEntry.size() - 1 || this.getLink().isPresent());
        }

        private List<? extends Resource> getEntry() {
            if (this.results.getEntry() != null) {
                return this.results.getEntry().stream().map(e -> e.getResource()).collect(Collectors.toList());
            } else {
                return Collections.emptyList();
            }
        }

        private Optional<String> getLink() {
            return this.results.getLink().stream().filter(l -> l.getRelation().getValue().equals("next")).map(l -> l.getUrl().getValue()).reduce((a, b) -> {
                throw new IllegalStateException("Multiple 'next' links found");
            });
        }

        public Object next() {
            current++;
            if (current < this.currentEntry.size()) {
                return this.currentEntry.get(current);
            } else {
                try {
                    String url = getLink().get();
                    this.results = pageLoader.apply(url);
                    this.currentEntry = getEntry();
                    this.current = 0;
                    if (current < this.currentEntry.size()) {
                        return this.currentEntry.get(this.current);
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }

            throw new RuntimeException("The iteration has no more elements.");
        }
    }
}
