package org.example.sort;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class EvenNumbersNaturalOrderTest {

    static final class Item {
        final Number num;
        final String tag;
        Item(Number n) {
            this(n, "");
        }
        Item(Number n, String tag) {
            this.num = n; this.tag = tag;
        }
        @Override public String toString() { return tag + ":" + num;
        }
    }
    @Test
    void integers_evenPicked_oddsKeptOut_andOrderPreserved() {
        List<Item> data = new ArrayList<>(List.of(new Item(10, "a"),new Item(7,  "b"),new Item(4,  "c"),new Item(3,  "d"),new Item(8,  "e")));
        EvenNumbersNaturalOrder.EvenRecord<Item> rec = EvenNumbersNaturalOrder.getEvenNumbersRecord(data, it -> it.num);
        assertEquals(List.of(0, 2, 4), rec.getEvenIndices());
        assertEquals(List.of("a:10", "c:4", "e:8"),rec.getEvenElements().stream().map(Object::toString).toList());
    }
    @Test
    void longs_shorts_bytes_evenPicked() {
        List<Item> data = new ArrayList<>(List.of(new Item(2L, "L2"),new Item((short) 3, "S3"), new Item((byte) 4, "B4"),new Item(5L, "L5")));
        EvenNumbersNaturalOrder.EvenRecord<Item> rec = EvenNumbersNaturalOrder.getEvenNumbersRecord(data, it -> it.num);
        assertEquals(List.of(0, 2), rec.getEvenIndices());
        assertEquals(List.of("L2:2", "B4:4"), rec.getEvenElements().stream().map(Object::toString).toList());
    }
    @Test
    void doublesAndFloats_onlyIntegralEvensPicked_fractionalsIgnored() {
        List<Item> data = new ArrayList<>(List.of(new Item(6.0, "d6"),new Item(5.5, "d55"),new Item(4.0f, "f4"),new Item(7.0, "d7"),new Item(2.000f, "f2")));
        EvenNumbersNaturalOrder.EvenRecord<Item> rec = EvenNumbersNaturalOrder.getEvenNumbersRecord(data, it -> it.num);
        assertEquals(List.of(0, 2, 4), rec.getEvenIndices());
        assertEquals(List.of("d6:6.0", "f4:4.0", "f2:2.0"),rec.getEvenElements().stream().map(Object::toString).toList());
    }
    @Test    void emptyList_returnsEmptyRecord() {
        List<Item> data = List.of();
        EvenNumbersNaturalOrder.EvenRecord<Item> rec = EvenNumbersNaturalOrder.getEvenNumbersRecord(data, it -> it.num);
        assertNotNull(rec);
        assertNotNull(rec.getEvenIndices());
        assertNotNull(rec.getEvenElements());
        assertTrue(rec.getEvenIndices().isEmpty());
        assertTrue(rec.getEvenElements().isEmpty());
    }
    @Test
    void nullList_throwsNpe() {
        assertThrows(NullPointerException.class,() -> EvenNumbersNaturalOrder.getEvenNumbersRecord(null, it -> 0));
    }
    @Test
    void indicesMatchElements_fromOriginalPositions() {List<Item> data = new ArrayList<>(List.of(new Item(1, "x"),new Item(2, "y"),new Item(3, "z"),new Item(4, "w"),new Item(6, "k")));
        EvenNumbersNaturalOrder.EvenRecord<Item> rec = EvenNumbersNaturalOrder.getEvenNumbersRecord(data, it -> it.num);
        List<Integer> idx = rec.getEvenIndices();
        List<Item> els = rec.getEvenElements();
        assertEquals(idx.size(), els.size());
        for (int i = 0; i < idx.size(); i++) {
            assertSame(data.get(idx.get(i)), els.get(i));
        }
    }
}