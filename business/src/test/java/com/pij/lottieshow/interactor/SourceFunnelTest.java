package com.pij.lottieshow.interactor;

import android.support.annotation.NonNull;

import com.pij.lottieshow.model.LottieFile;

import org.apache.commons.collections4.IterableUtils;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.File;
import java.util.List;

import rx.observers.TestSubscriber;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static rx.Observable.just;

/**
 * <p>Created on 09/04/2017.</p>
 * @author Pierrejean
 */

public class SourceFunnelTest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    @Test
    public void singleSourceEmptyListEmitEmptyList() {
        LottieSource lottieSource = () -> just(emptyList());
        SourceFunnel sut = createDefaultSut(singletonList(lottieSource));
        TestSubscriber<List<LottieFile>> subscriber = TestSubscriber.create();

        sut.lottieFiles().map(IterableUtils::toList).subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.assertValue(emptyList());
    }

    @Test
    public void singleSourceSingletonListEmitSingletonList() {
        LottieFile lottie = LottieFile.create(new File("parent", "label"));
        LottieSource lottieSource = () -> just(singletonList(lottie));
        SourceFunnel sut = createDefaultSut(singletonList(lottieSource));
        TestSubscriber<List<LottieFile>> subscriber = TestSubscriber.create();

        sut.lottieFiles().map(IterableUtils::toList).subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.assertValue(singletonList(LottieFile.create(new File("parent", "label"))));
    }

    @Test
    public void twoSourcesOneEmptyOneSingletonEmitSingletonList() {
        LottieSource lottieSource1 = () -> just(emptyList());
        LottieFile lottie = LottieFile.create(new File("parent", "label"));
        LottieSource lottieSource2 = () -> just(singletonList(lottie));
        SourceFunnel sut = createDefaultSut(asList(lottieSource1, lottieSource2));
        TestSubscriber<List<LottieFile>> subscriber = TestSubscriber.create();

        sut.lottieFiles().map(IterableUtils::toList).subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.assertValue(singletonList(LottieFile.create(new File("parent", "label"))));
    }

    @Test
    public void twoSingletonSourcesEmit2ItemList() {
        LottieFile lottie1 = LottieFile.create(new File("parent1", "label1"));
        LottieSource lottieSource1 = () -> just(singletonList(lottie1));
        LottieFile lottie2 = LottieFile.create(new File("parent2", "label2"));
        LottieSource lottieSource2 = () -> just(singletonList(lottie2));
        SourceFunnel sut = createDefaultSut(asList(lottieSource1, lottieSource2));
        TestSubscriber<List<LottieFile>> subscriber = TestSubscriber.create();

        sut.lottieFiles().map(IterableUtils::toList).subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.assertValue(asList(LottieFile.create(new File("parent1", "label1")),
                                      LottieFile.create(new File("parent2", "label2"))));
    }

    @NonNull
    private SourceFunnel createDefaultSut(List<LottieSource> lottieSources) {
        return new SourceFunnel(lottieSources);
    }
}
