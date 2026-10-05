package ste.lazyjava;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;
import static ste.lazyjava.£.LegacyPerson;

class £AliasTest {

    @Test
    void lclass_alias_should_generate_factory_methods() {
        var p = LegacyPerson("Alias", 99);
        then(p.getName()).isEqualTo("Alias");
        then(p.getAge()).isEqualTo(99);
    }
}
