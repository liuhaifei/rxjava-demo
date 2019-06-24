package com.demo.rxjava.rxjavademo.test;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;

import javax.swing.text.html.ListView;

/**
 * @author liuhaifei
 * @date 2019/6/24/024
 * @Description:
 * @Version 1.0
 */
public class MainActivity {

    private static final String TAG="RxJavaTag：";
    private Disposable mDisposable;
    private ListView listView;
    private int[] drawableRes;

    public void rxJavaBaseUse(){
        //被观察者
        Observable novel=Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                System.out.println("subscribe线程："+Thread.currentThread().getName());
                emitter.onNext("连载1");
                emitter.onNext("连载2");
                emitter.onNext("连载3");
                emitter.onComplete();
            }
        });

        //观察者
        Observer<String> reader=new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("onSubscribe线程："+Thread.currentThread().getName());
                mDisposable=d;
                System.out.println(TAG+"OnSubscribe");
            }

            @Override
            public void onNext(String value) {
                System.out.println("onNext线程："+Thread.currentThread().getName());
                if ("2".equals(value)){
                    mDisposable.dispose();
                    return;
                }
                System.out.println(TAG+"onNext:"+value);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError线程："+Thread.currentThread().getName());
                System.out.println(TAG+"onError="+e.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete线程："+Thread.currentThread().getName());
                System.out.println(TAG+"onComplete");
            }
        };

        //建立关联
        novel.safeSubscribe(reader);
    }


    public static void main(String[] args) {
//        System.out.println("主线程："+Thread.currentThread().getName());
//        MainActivity activity=new MainActivity();
//        activity.rxJavaBaseUse();

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                System.out.println("subscribe线程："+Thread.currentThread().getName());
                emitter.onNext("连载1");
                emitter.onNext("连载2");
                emitter.onNext("连载3");
                emitter.onComplete();
            }
        })
                //.observeOn()//回调在主线程
                .subscribeOn(Schedulers.io())//执行在io线程
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("onSubscribe线程："+Thread.currentThread().getName());
                        System.out.println(TAG+"onSubscribe");
                    }

                    @Override
                    public void onNext(String value) {
                        System.out.println("onNext线程："+Thread.currentThread().getName());
                        System.out.println(TAG+"onNext:"+value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError："+Thread.currentThread().getName());
                        System.out.println(TAG+"onError="+e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete线程："+Thread.currentThread().getName());
                        System.out.println(TAG+"onComplete()");
                    }
                });
    }

}
