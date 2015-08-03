package by.artkostm.rxj;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class Stub {
    @SuppressWarnings({ "static-access"})
    public static void main(String[] args) throws InterruptedException{
        Subscription subscription = Observable.create(new OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> t) {
//                t.onNext("onNext(" + Thread.currentThread().getName() + ")");
//                t.onNext("onNext(" + Thread.currentThread().getName() + ")");
                t.onNext("onNext(" + Thread.currentThread().getName() + ")");
                t.onCompleted();
            }
        })
        .from(new String[]{"one","two","three"})
        .subscribeOn(Schedulers.computation()).observeOn(Schedulers.computation())
        .map(new Func1<String, String>() {
            @Override
            public String call(String t) {
                return "map(" + Thread.currentThread().getName() + ", " + t + ")";
            }
        })
        .subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                System.out.println("onCompleted()");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError()");
            }

            @Override
            public void onNext(String t) {
                System.out.println("subscribe(" + Thread.currentThread().getName() + ", " + t + ")");
                hello(t);
                //throw new RuntimeException();
            }
        });
        System.out.println("isUnsubscribed: " + subscription.isUnsubscribed());
        
        //hello("one","two","three");
        Thread.currentThread().sleep(2000);
        System.out.println("isUnsubscribed: " + subscription.isUnsubscribed());
    }
    
    public static void hello(String... names) {
        Observable.from(names).subscribe(new Action1<String>() {

            @Override
            public void call(String s) {
                System.out.println("Hello " + Thread.currentThread().getName() + "!");
            }

        });
    }
}
