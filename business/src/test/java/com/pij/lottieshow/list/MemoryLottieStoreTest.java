package com.pij.lottieshow.list;

import com.pij.lottieshow.model.LottieFile;

import org.apache.commons.collections4.IterableUtils;
import org.junit.Test;

import java.io.File;
import java.util.List;

import rx.observers.TestSubscriber;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

/**
 * <p>Created on 11/04/2017.</p>
 * @author Pierrejean
 */
public class MemoryLottieStoreTest {

    @Test
    public void emitsEmptyListFirst() {
        MemoryLottieStore sut = new MemoryLottieStore();
        TestSubscriber<List<LottieFile>> subscriber = TestSubscriber.create();

        sut.lottieFiles().map(IterableUtils::toList).subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.assertValue(emptyList());
    }

    @Test
    public void emitsSingletonWithFirstItemSecond() {
        MemoryLottieStore sut = new MemoryLottieStore();
        TestSubscriber<List<LottieFile>> subscriber = TestSubscriber.create();
        sut.lottieFiles().map(IterableUtils::toList).subscribe(subscriber);

        sut.add(LottieFile.create(new File("aa", "bbb")));

        subscriber.assertNoErrors();
        //noinspection unchecked
        subscriber.assertValues(emptyList(), singletonList(LottieFile.create(new File("aa", "bbb"))));
    }

    @Test
    public void emitsListWithLatestAddedAtEnd() {
        MemoryLottieStore sut = new MemoryLottieStore();
        TestSubscriber<List<LottieFile>> subscriber = TestSubscriber.create();
        sut.lottieFiles().map(IterableUtils::toList).subscribe(subscriber);

        sut.add(LottieFile.create(new File("11", "111")));
        sut.add(LottieFile.create(new File("22", "222")));

        subscriber.assertNoErrors();
        //noinspection unchecked
        subscriber.assertValues(emptyList(),
                                singletonList(LottieFile.create(new File("11", "111"))),
                                asList(LottieFile.create(new File("11", "111")),
                                       LottieFile.create(new File("22", "222"))));
    }

    @Test
    public void emitLastListForSecondSubscriber() {
        MemoryLottieStore sut = new MemoryLottieStore();
        sut.lottieFiles().subscribe();
        sut.add(LottieFile.create(new File("11", "111")));
        sut.add(LottieFile.create(new File("22", "222")));

        TestSubscriber<List<LottieFile>> subscriber = TestSubscriber.create();
        sut.lottieFiles().map(IterableUtils::toList).subscribe(subscriber);

        subscriber.assertNoErrors();
        //noinspection unchecked
        subscriber.assertValues(asList(LottieFile.create(new File("11", "111")),
                                       LottieFile.create(new File("22", "222"))));
    }
}