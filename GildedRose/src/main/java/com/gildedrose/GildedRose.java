package com.gildedrose;

class GildedRose {
    
    final String BACKSTAGE_NAME = "Backstage passes to a TAFKAL80ETC concert";
    final String SULFURAS_NAME = "Sulfuras, Hand of Ragnaros";
    final String AGEDBRIE_NAME = "Aged Brie";
    
    Item[] items;

    public GildedRose(Item[] items) {
        this.items = items;
    }

    public void updateQuality() {
        for (Item item : items) {
            switch (item.name) {
                case BACKSTAGE_NAME:
                    updateBackstage(item);
                    break;
                
                case SULFURAS_NAME:
                    break;
                
                case AGEDBRIE_NAME:
                    updateAgedbrie(item);
                    break;

                default:
                    updateCourant(item);
                    break;
            }
        }
    }

    private void updateBackstage(Item item){
        item.quality++;
        if (item.sellIn<=10) { item.quality++; }
        if (item.sellIn<=5) { item.quality++; }
        if (item.quality>50) { item.quality=50; }

        item.sellIn--;
        if (item.sellIn<=0) { item.quality=0; }
    }

    private void updateAgedbrie(Item item){
        item.quality++;
        if (item.sellIn <= 0) { item.quality++; }
        if (item.quality>50) { item.quality=50; }

        item.sellIn--;
    }

    private void updateCourant(Item item){
        item.quality--;
        if (item.sellIn<=0) { item.quality--; }

        if (item.quality<0) { item.quality=0; }

        item.sellIn--;
    }

}
