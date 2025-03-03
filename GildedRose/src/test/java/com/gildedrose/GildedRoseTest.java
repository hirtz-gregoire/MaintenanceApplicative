package com.gildedrose;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GildedRoseTest {

    @Test
    void foo() {
        Item[] items = new Item[] { new Item("foo", 0, 0) };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        assertEquals("foo", app.items[0].name);
    }

    @Test
    void test_NormalItem(){
        Item[] items = new Item[] { 
            new Item("o0", 10, 10), 
            new Item("o1", 8, 12) 
        };
        GildedRose app = new GildedRose(items);
        app.updateQuality();
        
        assertEquals("o0", app.items[0].name);
        assertEquals(9, app.items[0].sellIn);
        assertEquals(9, app.items[0].quality);

        assertEquals("o1", app.items[1].name);
        assertEquals(7, app.items[1].sellIn);
        assertEquals(11, app.items[1].quality);
    }

    @Test
    void test_NormalItem_borneInf(){
        Item[] items = new Item[] { 
            new Item("o0", 1, 10), 
            new Item("o1", 0, 10),
            new Item("o2", 0, 0),
            new Item("o3", 10, 50),
            new Item("o4", 0, 50)
        };
        GildedRose app = new GildedRose(items);
        app.updateQuality();

        assertEquals("o0", app.items[0].name);
        assertEquals(0, app.items[0].sellIn);
        assertEquals(9, app.items[0].quality);

        assertEquals("o1", app.items[1].name);
        assertEquals(-1, app.items[1].sellIn);
        assertEquals(8, app.items[1].quality);

        assertEquals("o2", app.items[2].name);
        assertEquals(-1, app.items[2].sellIn);
        assertEquals(0, app.items[2].quality);

        assertEquals("o3", app.items[3].name);
        assertEquals(9, app.items[3].sellIn);
        assertEquals(49, app.items[3].quality);

        assertEquals("o4", app.items[4].name);
        assertEquals(-1, app.items[4].sellIn);
        assertEquals(48, app.items[4].quality);
    }

    @Test
    void test_AgedBrie(){
        Item[] items = new Item[] { 
            new Item("Aged Brie", 10, 10),
            new Item("Aged Brie", 5, 10),
            new Item("Aged Brie", 0, 10),
            new Item("Aged Brie", -1, 50)
        };
        GildedRose app = new GildedRose(items);
        app.updateQuality();

        assertEquals("Aged Brie", app.items[0].name);
        assertEquals(9, app.items[0].sellIn);
        assertEquals(11, app.items[0].quality);

        assertEquals("Aged Brie", app.items[1].name);
        assertEquals(4, app.items[1].sellIn);
        assertEquals(11, app.items[1].quality);

        assertEquals("Aged Brie", app.items[2].name);
        assertEquals(-1, app.items[2].sellIn);
        assertEquals(12, app.items[2].quality);

        assertEquals("Aged Brie", app.items[3].name);
        assertEquals(-2, app.items[3].sellIn);
        assertEquals(50, app.items[3].quality);
    }

    @Test
    void test_Backstage(){
        Item[] items = new Item[] { 
            new Item("Backstage passes to a TAFKAL80ETC concert", 15, 10),
            new Item("Backstage passes to a TAFKAL80ETC concert", 10, 10),
            new Item("Backstage passes to a TAFKAL80ETC concert", 5, 10),
            new Item("Backstage passes to a TAFKAL80ETC concert", 5, 49),
        };
        GildedRose app = new GildedRose(items);
        app.updateQuality();

        assertEquals("Backstage passes to a TAFKAL80ETC concert", app.items[0].name);
        assertEquals(14, app.items[0].sellIn);
        assertEquals(11, app.items[0].quality);

        assertEquals("Backstage passes to a TAFKAL80ETC concert", app.items[1].name);
        assertEquals(9, app.items[1].sellIn);
        assertEquals(12, app.items[1].quality);

        assertEquals("Backstage passes to a TAFKAL80ETC concert", app.items[2].name);
        assertEquals(4, app.items[2].sellIn);
        assertEquals(13, app.items[2].quality);

        assertEquals("Backstage passes to a TAFKAL80ETC concert", app.items[3].name);
        assertEquals(4, app.items[3].sellIn);
        assertEquals(50, app.items[3].quality);
    }

    @Test
    void test_Sulfuras(){
        Item[] items = new Item[] { 
            new Item("Sulfuras, Hand of Ragnaros", 0, 80),
            new Item("Sulfuras, Hand of Ragnaros", -1, 80)
        };
        GildedRose app = new GildedRose(items);
        app.updateQuality();

        assertEquals("Sulfuras, Hand of Ragnaros", app.items[0].name);
        assertEquals(0, app.items[0].sellIn);
        assertEquals(80, app.items[0].quality);

        assertEquals("Sulfuras, Hand of Ragnaros", app.items[1].name);
        assertEquals(-1, app.items[1].sellIn);
        assertEquals(80, app.items[1].quality);
    }
}
