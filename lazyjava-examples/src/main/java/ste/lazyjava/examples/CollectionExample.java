/**
 * Copyright 2026 the original author or authors from the lazy-java project
 * (https://github.com/stefanofornari/lazy-java).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package ste.lazyjava.examples;

import ste.lazyjava.annotation.NonEmpty;
import ste.lazyjava.annotation.NonNull;
import ste.lazyjava.annotation.£;

import java.util.List;

/**
 * Example demonstrating @NonEmpty with Collections and arrays.
 */
@£
public class CollectionExample {

    private List<String> tags;
    private String[] categories;
    private int[] scores;

    public CollectionExample(@NonNull String name,
                             @NonEmpty List<String> tags,
                             @NonEmpty String[] categories,
                             @NonEmpty int[] scores) {
        this.tags = tags;
        this.categories = categories;
        this.scores = scores;
    }

    public void setTags(@NonEmpty List<String> tags) {
        this.tags = tags;
    }

    public void setCategories(@NonEmpty String[] categories) {
        this.categories = categories;
    }

    public void setScores(@NonEmpty int[] scores) {
        this.scores = scores;
    }

    public List<String> getTags() {
        return tags;
    }

    public String[] getCategories() {
        return categories;
    }

    public int[] getScores() {
        return scores;
    }

    @Override
    public String toString() {
        return "CollectionExample{tags=" + tags + ", categories=" + java.util.Arrays.toString(categories) + ", scores=" + java.util.Arrays.toString(scores) + "}";
    }
}