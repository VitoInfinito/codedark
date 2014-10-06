package core;



/**
 
 * A wrapper for the shop. We should only have
 * one shop (better use CDI more later)
 * @author 
 */
 

//Possibly use and rework
public enum SingletonExample {

    INSTANCE;
    /*
    private final IShop shop = Shop.newInstance();

    public IShop getShop() {
        return shop;
    }*/

}
