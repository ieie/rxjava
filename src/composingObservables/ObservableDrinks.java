package composingObservables;

import rx.Observable;

import java.util.ArrayList;
import java.util.List;

import static rx.observers.Observers.create;

public class ObservableDrinks {

    static List<List<? extends Drink>> drinks = new ArrayList<>();

    public static void main(String[] args) {
        Observable<List<Drink>> drinks$ = getDrinks();

        drinks$
            //.flatMap(list -> Observable.from(list))
            .subscribe(
                data -> System.out.println("Subscriber received " + data),
                (error) -> System.err.println(error),
                () -> System.out.println("*** The stream is over ***")
            );
    }

    public static Observable<List<Drink>> getDrinks(){

        drinks.add(loadBeers());
        drinks.add(loadSoftDrinks());


        Observable<List<Drink>> beerTrolley = Observable.create(subscriber -> {

            subscriber.onNext(loadBeers());   // push a beer trolley

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            subscriber.onNext(loadSoftDrinks()); // push a soft drink trolley

            subscriber.onCompleted();
        });

        return beerTrolley;
    }

    static List<Drink> loadBeers(){
        List<Drink> beerStock = new ArrayList<>();

        beerStock.add(new Beer("Obolon", "Ukraine", 4.00f));
        beerStock.add(new Beer("Stella", "Belgium", 7.75f));
        beerStock.add(new Beer("Sam Adams", "USA", 7.00f));
        return beerStock;
    }


    static List<Drink> loadSoftDrinks(){
        List<Drink> softStock = new ArrayList<>();

        softStock.add(new SoftDrink("Lemonade", "Ukraine", 1.00f));
        softStock.add(new SoftDrink("Pepsi", "USA", 2.00f));
        softStock.add(new SoftDrink("Fanta", "USA", 3.00f));
        return softStock;
    }
}